package com.example.mokkivaraus;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class AlueView implements Initializable {
    // Attributes
    @FXML
    private VBox alueBoxes;
    @FXML
    public TextField searchTF;
    @FXML
    public void addBtnClicked(ActionEvent actionEvent) {
        AlueDC alueDC = new AlueDC("alue", "alue_id");
        loadDialog(alueDC);
    }
    @FXML
    public void updateBtnClicked(ActionEvent actionEvent) {
        updateAlueList();
    }

    // Methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateAlueList();
        initSearch();
    }

    /**
     * Refreshes list of AlueBox:s
     */
    private void updateAlueList() {
        ObservableList<Alue> alueet = SessionData.dataBase.getAllRows("alue", "alue_id", Alue.class);
        updateAlueList(alueet);
    }
    /**
     * Refreshes list of AlueBox:s
     * @param alueet list of Alue-objects
     */
    private void updateAlueList(ObservableList<Alue> alueet) {
        this.alueBoxes.getChildren().clear();
        Collection<AlueBox> alueBoxes = new ArrayList<>();
        for(Alue alue : alueet) {
            alueBoxes.add(new AlueBox(this.alueBoxes, alue));
        }
        this.alueBoxes.getChildren().addAll(alueBoxes);
    }

    /**
     * Initials search for list of AlueBox:s
     */
    private void initSearch() {
        FilteredList<Alue> filteredData = new FilteredList<>(SessionData.dataBase.getAllRows("alue", "alue_id", Alue.class), b -> true);

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(alue -> {
                // if no search value
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                // Search Conditions
                return alue.getNimi().toLowerCase().contains(newValue.trim().toLowerCase());
            });

            ObservableList<Alue> alueet = new SortedList<>(filteredData);
            updateAlueList(alueet);
        });
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
            updateAlueList();
            initSearch();
        } catch (IOException ex) {
            System.out.println("DialogPane load error!!" + ex);
        }
    }
}

