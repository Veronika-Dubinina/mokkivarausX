package com.example.mokkivaraus;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;

import javax.swing.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
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
    private final String FILE_PATH = "src/main/pdf/raportti.pdf";
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

            try {
                // Create PDF document
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(FILE_PATH));
                // Fill document with data
                setDocumentContent(document);
            } catch (DocumentException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    public void printBtnClicked(ActionEvent actionEvent) {
        // Print Document
        printDocumnet(FILE_PATH);
    }

    // Methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Hide vbox
        raporttiContent.getStyleClass().add("hidden");
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
     * Fill document with content
     * @param document Documnet
     */
    private void setDocumentContent(Document document) {
        try {
            // Open document
            document.open();

            // Content
            // Empty paragraph
            Paragraph empty = new Paragraph(" ");
            empty.setPaddingTop((float) 10.0);
            // Report name and period
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String text1 = raportti + " " + alkuDtP.getValue().format(dateFormatter) + "-" + loppuDtP.getValue().format(dateFormatter);
            Paragraph paragraph1 = new Paragraph(text1);
            paragraph1.setAlignment(Element.ALIGN_RIGHT);
            paragraph1.setFont(new Font(Font.FontFamily.SYMBOL, (float) 14.0, Font.NORMAL));
            // Table
            PdfPTable table = new PdfPTable(raporttiTable.getColumns().size());
            addTableHeader(table);
            addRows(table);

            // Add content
            document.add(paragraph1);
            document.add(empty);
            document.add(table);
            // Close document
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets values for table columns
     * @param table Table
     */
    private void addTableHeader(PdfPTable table) {
        List.of(tables.get(raportti)[2])
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPadding((float) 5.0);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    Phrase phrase = new Phrase(columnTitle);
                    header.setPhrase(phrase);
                    table.addCell(header);
                });
    }

    /**
     * Sets values for table cells
     * @param table Table
     */
    private void addRows(PdfPTable table) {
        for (Map row : raporttiTable.getItems()) {
            for (String column : tables.get(raportti)[1]) {
                PdfPCell cell = new PdfPCell();
                cell.setPhrase(new Phrase(row.get(column).toString()));
                cell.setPadding((float) 5.0);
                table.addCell(cell);
            }
        }
    }

    /**
     * Initializes printer work. Prints document.
     * @param path Path to document
     */
    private void printDocumnet(String path) {
        try {
            PDDocument doc = Loader.loadPDF(new File(path));
            // Create a PrinterJob object
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            // Create a PageFormat object and set it to a default size and orientation
            PageFormat pageFormat = printerJob.defaultPage();
            // Return a copy of the Paper object associated with this PageFormat
            Paper paper = pageFormat.getPaper();
            // Set the imageable area of this Paper
            paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());
            // Set the Paper object for this PageFormat
            pageFormat.setPaper(paper);

            // Call painter to render the pages in the specified format
            printerJob.setPrintable(new PDFPrintable(doc), pageFormat);
            // Display the print dialog
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    if (printerJob.printDialog()) {
                        try {
                            printerJob.print();
                        } catch (PrinterException e) {
                            System.out.println("RaporttiController:printDocumnet. Error!!! " + e.getMessage());
                        }
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
