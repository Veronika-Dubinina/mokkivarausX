package com.example.mokkivaraus;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

abstract class TabController {
    // Attributes
    protected DataBase dataBase = new DataBase();
    protected String tableName;
    protected String identifierKey;
    protected Class tableClass;

    @FXML
    public TextField searchTF;
    @FXML
    public ComboBox<String> searchFilterCB;
    @FXML
    private TableView<Mokki> mokkiTable;

    // Constructors
    public TabController(String tableName, String identifierKey, Class tableClass) {
        this.tableName = tableName;
        this.identifierKey = identifierKey;
        this.tableClass = tableClass;
    }

    // Methods
    @FXML
    public void onAddBtnClicked(ActionEvent event) {
        loadDialog(getController());
    }

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

    /**
     * Opens dialog window to edit or add new mokki-object in mokki-table
     * @param dialogController Controller
     */
    public void loadDialog(Object dialogController) {
        // Open MokkiDC window
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
            updateTable(mokkiTable, dataBase.getAllRows(tableName, identifierKey, tableClass));
            initSearch();
        } catch (IOException ex) {
            System.out.println("DialogPane load error!!" + ex);
        }
    }

    /**
     * Returns DialogController-object
     * @return DialogController-object
     */
    abstract DialogController getController();

    /**
     * Initials search for Table
     */
    abstract void initSearch();

}
