package com.example.mokkivaraus;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

abstract class TabController<T> implements Initializable {
    // Attributes
    protected DataBase dataBase = new DataBase();
    protected String tableName;
    protected String identifierKey;
    protected String searchFilter = getSearchFilters()[0];
    protected Class<T> tableClass = null;
    protected String filter = "";

    @FXML
    public TextField searchTF;
    @FXML
    public ComboBox<String> searchFilterCB;
    @FXML
    public TableView<T> tableView;

    // Constructors
    public TabController(String tableName, String identifierKey, Class<T> tableClass) {
        this.tableName = tableName;
        this.identifierKey = identifierKey;
        this.tableClass = tableClass;
    }

    // Methods
    @FXML
    public void onAddBtnClicked(ActionEvent event) {
        loadDialog(getController());
    }
    @FXML
    public void setSearchFilter(ActionEvent actionEvent) {
        searchFilter = searchFilterCB.getValue();
    }
    @FXML
    public void onRefreshBtnClicked(ActionEvent actionEvent) {
        updateTable();
        initSearch();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set action on table row double click
        tableView.setRowFactory( tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    tableDoubleClick(row);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    tableMouseRightClick(row);
                }
            });
            return row ;
        });

        // Initial table and fill it with content
        initTable();
        updateTable();

        // Initial search
        searchFilterCB.getItems().addAll(getSearchFilters());
        initSearch();
    }

    /**
     * Matches table columns with class attributes
     */
    private void initTable() {
        ArrayList<String[]> colToAttr = getColToAttr();
        // Set table columns
        for (int i = 0; i < colToAttr.get(0).length; i++) {
            // Create new column
            TableColumn<T, ?> tc = new TableColumn<>(colToAttr.get(0)[i]);
            // Map column to class attribute
            tc.setCellValueFactory(new PropertyValueFactory<>(colToAttr.get(1)[i]));
            tableView.getColumns().add(tc);
        }
    }

    /**
     * Refreshes table
     */
    private void updateTable() {
        try {
            ObservableList<T> list = dataBase.getAllRows(tableName, identifierKey, tableClass, filter);
            tableView.setItems(list);
            tableView.refresh();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Opens dialog window to edit or add a new object in the table
     * @param dialogController Controller
     */
    private void loadDialog(Object dialogController) {
        // Open DialogController window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("changeTable-dialog.fxml"));
            fxmlLoader.setController(dialogController);
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            // Prevent from using main window while dialog is open
            newStage.initModality(Modality.APPLICATION_MODAL);
            // Non resizable
            newStage.setResizable(false);

            newStage.setTitle("");
            newStage.setScene(scene);
            newStage.showAndWait();

            // Reset table data
            updateTable();
            initSearch();
        } catch (IOException ex) {
            System.out.println("DialogPane load error!!" + ex);
        }
    }

    /**
     * Initials search for Table
     */
    private void initSearch() {
        FilteredList<T> filteredData = new FilteredList<>(dataBase.getAllRows(tableName, identifierKey, tableClass, filter), b -> true);
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(object -> {
                // if no search value
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                // Search Conditions
                return getSearchConditions(object, newValue);
            });
        });

        SortedList<T> sortedData = new SortedList<>(filteredData);

        // Bind sorted result with TableView
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Apply filtered sorted data to the TableView
        tableView.setItems(sortedData);
    };

    /**
     * Menetelmä, jota kutsutaan kaksoisnapsautettaessa taulukkoriviä
     * @param row Table row
     */
    protected void tableDoubleClick(TableRow<T> row) {
        T rowData = row.getItem();
        DialogController dialogController = getController();
        dialogController.setObject(rowData);
        loadDialog(dialogController);
    }

    /**
     * Menetelmä, jota kutsutaan, kun napsautat hiiren kakkospainikkeella taulukkoriviä
     * @param row Table row
     */
    protected void tableMouseRightClick(TableRow<T> row) {
    }

    /**
     * Returns list of table columns mapped to class attributes
     * @return ArrayList of table column and class attribute
     */
    abstract ArrayList<String[]> getColToAttr();

    /**
     * Returns DialogController-object
     * @return DialogController-object
     */
    abstract DialogController getController();

    /**
     * List of search conditions
     * @param object object to compare
     * @param newValue Search field text
     * @return True - if condition match
     */
    abstract boolean getSearchConditions(T object, String newValue);

    /**
     * Returns list of search filters
     * @return Array of search filters
     */
    abstract String[] getSearchFilters();
}
