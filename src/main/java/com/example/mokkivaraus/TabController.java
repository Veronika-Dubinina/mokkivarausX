package com.example.mokkivaraus;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Function;

public class TabContent {
    // Attributes

    // Methods
    /**
     * Matches table columns with class attributes
     * @param table TableView
     * @param colToAttr Dictionary with key-value pair like column-attribute
     */
    void initTable(TableView<?> table, HashMap<String, String> colToAttr) {
        // Loop throw list of table columns
        for (TableColumn<?,?> tc : table.getColumns()) {
            // Set column cellValueFactory as class attribute
            String id = tc.getId();
            tc.setCellValueFactory(new PropertyValueFactory<>((String) colToAttr.get(id)));
        }

    }

    /**
     * Refreshes table
     * @param table TableView to be refreshed
     * @param list List of values
     */
    void updateTable(TableView table, ObservableList list) {
        try {
            table.setItems(list);
            table.refresh();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
