package com.example.mokkivaraus;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;

public class AlueBox extends HBox {
    // Attributes
    private VBox parent;
    private Alue alue;
    private Label alueLbl = new Label();
    private TextField nimiTF = new TextField();
    private Button editBtn = new Button("Muoka");
    private Button deleteBtn = new Button("Poista");

    // Constructor
    public AlueBox(VBox parent, Alue alue) {
        super();
        this.parent = parent;
        this.alue = alue;

        // Nodes
        // Label and TextField
        alueLbl.setText(alue.getNimi());

        nimiTF.setVisible(false);
        nimiTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            nimiTF.setVisible(newValue);
        });

        AnchorPane pane = new AnchorPane(alueLbl, nimiTF);
        setHgrow(pane, Priority.ALWAYS);
        pane.setOnMouseClicked(event -> {
            SessionData.setAlue(alue);
            SessionData.refreshLists();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 900, 600);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Buttons
        editBtn.setOnAction(event -> editBtnClicked());
        editBtn.getStyleClass().add("editBtn");
        deleteBtn.setOnAction(event -> deleteBtnClicked());
        deleteBtn.getStyleClass().add("deleteBtn");

        // Box
        this.getChildren().addAll(pane, editBtn, deleteBtn);
        this.getStyleClass().add("alueBox");
        this.setPadding(new Insets(5));
        this.setSpacing(5);
    }

    // Methods
    private void editBtnClicked() {
        nimiTF.setVisible(true);
        nimiTF.setText(alue.getNimi());
        nimiTF.requestFocus();
        nimiTF.setOnAction(event -> {
            String nimi = nimiTF.getText().trim();
            if (nimi.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Alueen nimi ei voi olla tyhjä");
                alert.show();
            } else if (nimi.length() > 45) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Alueen nimi ei voi olla niin pitkä");
                alert.show();
            } else {
                updateAlue(nimi);
            }
        });
    }

    private void deleteBtnClicked() {
        deleteAlue();
    }

    private void updateAlue(String nimi) {
        String oldNimi = alue.getNimi();
        alue.setNimi(nimi);
        if (SessionData.dataBase.updateRow("alue", alue.getAttrMap(), "alue_id", alue.getAlue_id())) {
            alueLbl.setText(nimi);
        } else {
            alue.setNimi(oldNimi);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Tietokantayhteys epäonnistui");
            alert.show();
        }
        nimiTF.setVisible(false);

    }

    private void deleteAlue() {
        // Delete alue-object from database
        if (SessionData.dataBase.deleteRow("alue", "alue_id", alue.getAlue_id())) {
            // Delete from alue list
            parent.getChildren().remove(this);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Alue ei voida poistaa");
            alert.show();
        }
    }
}
