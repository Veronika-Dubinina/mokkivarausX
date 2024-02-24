package com.example.mokkivaraus;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MokkiController extends TabController implements Initializable  {
    // Attributes
    private final DataBase dataBase = new DataBase();
    //private ObservableList<Mokki> tableData = dataBase.getAllRows("mokki", "mokki_id", Mokki.class);
    private String searchFilters[] = {"kaikki", "nimi", "postinro", "osoite", "hinta", "kuvaus", "hm", "varustelu"};
    private String searchFilter = "kaikki";

    @FXML
    public TextField searchTF;
    @FXML
    public ComboBox<String> searchFilterCB;
    @FXML
    private TableView<Mokki> mokkiTable;

    public MokkiController() {
        // Set class attributes
        super("mokki", "mokki_id", Mokki.class);
    }

    // Methods
    @FXML
    public void setSearchFilter(ActionEvent actionEvent) {
        searchFilter = searchFilterCB.getValue();
    }

    @FXML
    public void onRefreshBtnClicked(ActionEvent actionEvent) {
        // Refreshes mokki-table content
        updateTable(mokkiTable, dataBase.getAllRows("mokki", "mokki_id", Mokki.class));
        initSearch();
    }

    @Override
    DialogController getController() {
        return new MokkiDC(tableName, identifierKey);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initial mokki-table and fill it with content
        mokkiTable.setRowFactory( tv -> {
            TableRow<Mokki> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Mokki rowData = row.getItem();
                    DialogController dialogController = getController();
                    dialogController.setObject(rowData);
                    loadDialog(dialogController);
                }
            });
            return row ;
        });
        initTable(mokkiTable, getColToAttr());
        updateTable(mokkiTable, dataBase.getAllRows("mokki", "mokki_id", Mokki.class));
        // Initial searchFilter
        searchFilterCB.getItems().addAll(searchFilters);

        // Initial search
        initSearch();
    }

    /**
     * @return Map of table column and class attribute
     */
    private HashMap<String, String> getColToAttr() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mokki_id", "mokki_id");
        map.put("mokki_postinro", "postinro");
        map.put("mokki_nimi", "mokkinimi");
        map.put("mokki_osoite", "katuosoite");
        map.put("mokki_hinta", "hinta");
        map.put("mokki_kuvaus", "kuvaus");
        map.put("mokki_henkilomaara", "henkilomaara");
        map.put("mokki_varustelu", "varustelu");

        return map;
    }

    @Override
    void initSearch() {
        FilteredList<Mokki> filteredData = new FilteredList<>(dataBase.getAllRows("mokki", "mokki_id", Mokki.class), b -> true);
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(mokki -> {
                // if no search value
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                if (mokki.getMokkinimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("nimi"))) {
                    return true; // Match in Nimi
                } else if (mokki.getKatuosoite().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("osoite"))) {
                    return true; // Match in Osoite
                } else if (mokki.getKuvaus().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("kuvaus"))) {
                    return true; // Match in Kuvaus
                } else if (mokki.getVarustelu().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varustelu"))) {
                    return true; // Match in Varustelu
                } else if (mokki.getPostinro().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("postinro"))) {
                    return true; // Match in Postinumero
                } else if (String.valueOf(mokki.getHinta()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("hinta"))) {
                    return true; // Match in Hinta
                } else if (String.valueOf(mokki.getHenkilomaara()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("hm"))) {
                    return true; // Match in Henkilomaara
                } else {
                    return false; // No match found
                }
            });
        });

        SortedList<Mokki> sortedData = new SortedList<>(filteredData);

        // Bind sorted result with TableView
        sortedData.comparatorProperty().bind(mokkiTable.comparatorProperty());

        // Apply filtered sorted data to the TableView
        mokkiTable.setItems(sortedData);
    }

}
