package com.example.mokkivaraus;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;

public class AlueBox extends HBox {
    // Attributes
    private VBox parent;
    private Alue alue;
    private Label alueLbl = new Label();
    private Button editBtn = new Button("Muoka");
    private Button deleteBtn = new Button("Poista");

    // Constructor
    public AlueBox(VBox parent, Alue alue) {
        super();
        this.parent = parent;
        this.alue = alue;

        // Label
        this.alueLbl.setText(alue.getNimi());
        Pane pane = new Pane(alueLbl);
        setHgrow(pane, Priority.ALWAYS);
        pane.setOnMouseClicked(event -> {
            SessionData.alue = this.alue;
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
        this.editBtn.setOnAction(event -> updateAlue());
        this.editBtn.getStyleClass().add("editBtn");
        this.deleteBtn.setOnAction(event -> deleteAlue());
        this.deleteBtn.getStyleClass().add("deleteBtn");

        // Box
        this.getChildren().addAll(pane, editBtn, deleteBtn);
        this.getStyleClass().add("alueBox");
        this.setPadding(new Insets(5));
        this.setSpacing(5);
    }

    // Methods
    private void updateAlue() {
        // Create popover window
        PopOver popover = new PopOver();
        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(10));
        TextField nimiTF = new TextField();
        Button updateBtn = new Button("Päivitä");

        // Nimi text field
        nimiTF.setText(alue.getNimi());
        nimiTF.textProperty().addListener((object, oldValue, newValue) -> {
            String nimi = newValue.trim();
            if (nimi.isEmpty() || nimi.length() > 45) {
                updateBtn.setDisable(true);
            } else {
                updateBtn.setDisable(false);
            }
        });

        // Update button
        updateBtn.setOnAction(event -> {
            alue.setNimi(nimiTF.getText().trim());
            SessionData.dataBase.updateRow("alue", alue.getAttrMap(), "alue_id", alue.getAlue_id());
            alueLbl.setText(alue.getNimi());
            popover.hide();
        });

        // Popover root node
        content.getChildren().addAll(
                new Label("Uusi nimi"),
                nimiTF,
                updateBtn
        );

        // Set popover content and show it
        popover.setContentNode(content);
        popover.show(this);
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
