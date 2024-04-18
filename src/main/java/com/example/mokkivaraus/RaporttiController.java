package com.example.mokkivaraus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class RaporttiController implements Initializable {
    // Attributes
    private String[] raportit = new String[]{"Majoittumisten raportti", "Lisäpalvelujen raportti"};
    private String raportti;
    private HashMap<String, String[][]> tables = getTables();
    private DataBase dataBase = new DataBase(){
        @Override
        public void toDoResultSet(ResultSet resultSet){
            try {
                // Report name
                raporttiLbl.setText(raportti);
                // Clear table
                raporttiTable.getItems().clear();
                raporttiTable.getColumns().clear();
                String[] columns = tables.get(raportti)[1];
                // Create columns
                for (String col : columns) {
                    TableColumn<Map, Object> tableColumn = new TableColumn<>(col);
                    tableColumn.setCellValueFactory(new MapValueFactory<>(col));
                    raporttiTable.getColumns().add(tableColumn);
                }
                // Fill table with data
                while (resultSet.next()) {
                    Map<String, Object> rowData = new HashMap<>();
                    for (String col : columns) {
                        rowData.put(col, resultSet.getObject(col));
                    }
                    raporttiTable.getItems().add(rowData);
                }
                // Enable user-resizable columns
                raporttiTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
                raporttiTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    };
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private DatePicker alkuDtP;
    @FXML
    private DatePicker loppuDtP;
    @FXML
    private ComboBox<String> raporttiCmB;
    @FXML
    public VBox raporttiContent;
    @FXML
    public Label raporttiLbl;
    @FXML
    public TableView<Map> raporttiTable;
    @FXML
    void luodaBtnClicked(ActionEvent event) {
        if (!checkData())
            return;

        Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Raportti on luotu");
        raportti = raporttiCmB.getValue();
        // SQL query
        String sql = String.format(tables.get(raportti)[0][0],
                SessionData.alue.getAlue_id(),
                alkuDtP.getValue(),
                loppuDtP.getValue());
        // Run query
        if (dataBase.getData(sql)) {
            raporttiContent.getStyleClass().remove("hidden");
            success.show();
        }
    }
    @FXML
    public void saveBtnClicked(ActionEvent actionEvent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String period = formatter.format(alkuDtP.getValue()) + "-" + formatter.format(loppuDtP.getValue());
        String fileName = "(" + SessionData.alue.toString() + ")" + raportti + "_" + period;
        
        Window stage = raporttiContent.getScene().getWindow();
        fileChooser.setTitle("Save dialog");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("xlsx file", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveReportToFile(file, period);
        }
    }

    // Methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Reports
        raporttiCmB.getItems().addAll(raportit);

        // Calendar restriction "From"
        alkuDtP.valueProperty().addListener((observable, oldDate, newDate) -> {
            loppuDtP.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(DatePicker datePicker) {
                    return new DateCell(){
                        @Override
                        public void updateItem(LocalDate localDate, boolean b) {
                            super.updateItem(localDate, b);
                            // Disable
                            if (localDate.isBefore(newDate)) {
                                setDisable(true);
                            }
                        }
                    };
                }
            });
        });
        // Calendar restriction "To"
        loppuDtP.valueProperty().addListener((observable, oldDate, newDate) -> {
            alkuDtP.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(DatePicker datePicker) {
                    return new DateCell(){
                        @Override
                        public void updateItem(LocalDate localDate, boolean b) {
                            super.updateItem(localDate, b);
                            // Disable
                            if (localDate.isAfter(newDate)) {
                                setDisable(true);
                            }
                        }
                    };
                }
            });
        });
    }

    /**
     * Returns Map with report parameters
     * @return HashMap
     */
    private HashMap<String, String[][]> getTables() {
        HashMap<String, String[][]> tables = new HashMap<>();
        tables.put(raportit[0], new String[][]{{"CALL majoitusRaportti(%d, '%s', '%s')"}, {"mokkinimi", "käyttöaste_pvm", "käyttöaste_pros", "myyntisumma"}, {"MÖKKI", "KÄYTTÖASTE pvm.", "KÄYTTÖASTE %", "SUMMA"}} );
        tables.put(raportit[1], new String[][]{{"CALL palveluRaportti(%d, '%s', '%s')"}, {"nimi", "lkm", "summa", "alv"}, {"PALVELU", "LKM", "SUMMA", "ALV"}});
        return tables;
    }

    /**
     * Checks input data
     * @return False if there is data failure
     */
    private boolean checkData() {
        Alert error = new Alert(Alert.AlertType.WARNING);

        // Check data before making a report
        if (raporttiCmB.getValue() == null) {
            error.setContentText("Valitse raportti");
            error.show();
            return false;
        }
        if (alkuDtP.getValue() == null) {
            error.setContentText("Vilitse alkupvm");
            error.show();
            return false;
        }
        if (loppuDtP.getValue() == null){
            error.setContentText("Vilitse loppupvm");
            error.show();
            return false;
        }

        return true;
    }

    /**
     * Saves report data to xlsx file
     * @param file File
     * @param period Period formatted to String
     */
    private void saveReportToFile(File file, String period) {
        // Workbook
        Workbook workbook = new XSSFWorkbook();

        // Sheet
        Sheet sheet = workbook.createSheet(period);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        // Create table
        addTableHeader(workbook, sheet);
        addRows(workbook, sheet);

        // Write to file
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets values for table columns
     * @param workbook Workbook in xlsx file
     * @param sheet Sheet in xlsx file
     */
    private void addTableHeader(Workbook workbook, Sheet sheet) {
        Row header = sheet.createRow(0);

        // Style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // Font
        XSSFFont headerFont = ((XSSFWorkbook) workbook).createFont();
        headerFont.setFontName("Calibri");
        headerFont.setFontHeightInPoints((short) 16);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Headers
        AtomicInteger i = new AtomicInteger();
        List.of(tables.get(raportti)[2])
                .forEach(columnTitle -> {
                    Cell headerCell = header.createCell(i.get());
                    headerCell.setCellValue(columnTitle);
                    headerCell.setCellStyle(headerStyle);
                    i.getAndIncrement();
                });
    }

    /**
     * Sets values for table cells
     * @param workbook Workbook in xlsx file
     * @param sheet Sheet in xlsx file
     */
    private void addRows(Workbook workbook, Sheet sheet) {
        // Style
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        // Font
        XSSFFont contentFont = ((XSSFWorkbook) workbook).createFont();
        contentFont.setFontName("Calibri");
        contentFont.setFontHeightInPoints((short) 14);
        style.setFont(contentFont);

        // Content
        int i = 1;
        for (Map row : raporttiTable.getItems()) {
            Row content = sheet.createRow(i);
            int j = 0;
            for (String column : tables.get(raportti)[1]) {
                Cell contentCell = content.createCell(j);
                contentCell.setCellValue(row.get(column).toString());
                contentCell.setCellStyle(style);
                j++;
            }
            i++;
        }
    }
}
