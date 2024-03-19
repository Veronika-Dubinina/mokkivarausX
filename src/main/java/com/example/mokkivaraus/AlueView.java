package com.example.mokkivaraus;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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

    // Methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Alue> alueet = SessionData.dataBase.getAllRows("alue", "alue_id", Alue.class);
        updateAlueList(alueet);
        initSearch();
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
}

