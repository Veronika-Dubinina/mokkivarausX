package com.example.mokkivaraus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

abstract class DialogController implements Initializable{
    // Attributes
    protected String tableName;
    protected String identifierKey;
    protected Object identifierValue;
    protected boolean editMode;
    protected String alertTitle = "";
    protected String alertMessage = "";

    @FXML
    protected DialogPane dialogPane;
    @FXML
    protected Label dialogTitle;
    @FXML
    protected VBox mainContent;
    @FXML
    protected GridPane formsGridPane;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button closeBtn;

    // Constructor
    public DialogController(String tableName, String identifierKey) {
        this.tableName = tableName;
        this.identifierKey = identifierKey;
        this.editMode = false;
    }

    // Methods
    @FXML
    void closeBtnClicked(ActionEvent event){
        // Close dialog window
        closeStage();
    }

    @FXML
    void addBtnClicked(ActionEvent event) {
        if (addData()) {
            showAlert(Alert.AlertType.INFORMATION);
            closeStage();
        } else
            showAlert(Alert.AlertType.WARNING);
    }

    @FXML
    void updateBtnClicked(ActionEvent event) {
        if (updateData()) {
            showAlert(Alert.AlertType.INFORMATION);
            closeStage();
        } else
            showAlert(Alert.AlertType.WARNING);
    }

    @FXML
    void deleteBtnClicked(ActionEvent event) {
        if (deleteData()) {
            closeStage();
        } else
            showAlert(Alert.AlertType.WARNING);
    }

    @FXML
    void createBtnClicked(ActionEvent event) {
        // Pass
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize
        setDialogContent();
        // Edit mode
        if (editMode) {
            EditMode();
        }
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /** Edit mode */
    void EditMode() {
        setEditContent();
        addBtn.setVisible(false);
        deleteBtn.setVisible(true);
        updateBtn.setVisible(true);
    }

    /**
     * Object from table
     * @param object Object of TableClass
     */
    abstract void setObject(Object object);

    /**
     * Checks all user's input data. If there is  a data failure
     * returns false
     * @return false - if there is a data failure
     */
    abstract boolean checkData();

    /**
     * Sets DialogPane main content
     */
    abstract void setDialogContent();

    /**
     * Sets DialogPane edit mode content
     */
    abstract void setEditContent();

    /**
     * Returns list table row attributes
     * @return Row attributes
     */
    abstract HashMap<String, Object> listOfAttributes();

    /**
     * Shows alert window of the specified type.
     * With title and message derived from the class alertTitle and alertMessage attributes' values.
     * @param type Alert type
     */
    void showAlert(Alert.AlertType type) {
        Alert a = new Alert(Alert.AlertType.NONE);

        a.setAlertType(type);
        a.setTitle(alertTitle);
        a.setContentText(alertMessage);
        a.show();
    }

    /** Close dialog window */
    void closeStage() {
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();
    }

    /** Add new row into table */
    boolean addData() {
        // Check user's input data, if there is data failure, show warning window
        if (checkData()) {
            // Add new row into table
            if (SessionData.dataBase.addRow(tableName, listOfAttributes()))
                return true;
            else
                alertMessage = "Tietokannan vika";
        }

        return false;
    }

    /** Update row in table */
    boolean updateData() {
        // Check user's input data, if there is data failure, show warning window
        if (checkData()) {
            // Add new row into table
            if (SessionData.dataBase.updateRow(tableName, listOfAttributes(), identifierKey, identifierValue))
                return true;
            else
                alertMessage = "Tietokannan vika";
        }

        return false;
    }

    /** Delete row in table */
    boolean deleteData() {
        if (SessionData.dataBase.deleteRow(tableName, identifierKey, identifierValue)) {
            return true;
        } else {
            alertMessage = "Tietokannan vika";
            return false;
        }
    }
}
