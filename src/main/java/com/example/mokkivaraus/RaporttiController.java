package com.example.mokkivaraus;

import com.mysql.cj.protocol.a.SqlDateValueEncoder;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.json.JSONArray;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class RaporttiController implements Initializable {
    // Attributes
    private DataBase dataBase = new DataBase(){
        @Override
        public void toDoResultSet(ResultSet resultSet){
            try {
                // Report name
                raporttiLbl.setText(raporttiCmB.getValue());
                // Clear table
                raporttiTable.getItems().clear();
                raporttiTable.getColumns().clear();
                String[] columns = tables.get(raporttiCmB.getValue())[1];
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
                raporttiTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    };
    private String[] raportit = new String[]{"Majoittumisten raportointi", "Lisäpalvelujen raportointi"};
    private HashMap<String, String[][]> tables = getTables();
    @FXML
    private DatePicker alkuDtP;
    @FXML
    private DatePicker loppuDtP;
    @FXML
    private ComboBox<String> raporttiCmB;
    @FXML
    public Label raporttiLbl;
    @FXML
    public TableView<Map> raporttiTable;
    @FXML
    void luodaBtnClicked(ActionEvent event) {
        // SQL query
        String sql = String.format(tables.get(raporttiCmB.getValue())[0][0],
                SessionData.alue.getAlue_id(),
                alkuDtP.getValue(),
                loppuDtP.getValue());
        // Run query
        dataBase.getData(sql);
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
        tables.put(raportit[0], new String[][]{{"CALL majoitusRaportti(%d, '%s', '%s')"}, {"mokkinimi", "käyttöaste_pvm", "käyttöaste_pros", "myyntisumma"}} );
        tables.put(raportit[1], new String[][]{{}, {}});
        return tables;
    }
}
