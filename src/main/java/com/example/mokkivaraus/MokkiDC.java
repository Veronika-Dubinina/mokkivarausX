package com.example.mokkivaraus;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class MokkiDC extends DialogController{
    // Attributes
    private Mokki mokki = new Mokki();

    private ComboBox<Alue> alueCmBox = new ComboBox<>(dataBase.getAllRows("alue", "alue_id", Alue.class));
    private TextField nimiField = new TextField();
    private AutoCompleteTextField<Posti> postinroField = new AutoCompleteTextField<>(dataBase.getAllRows("posti", "postinro", Posti.class), true) {
        @Override
        public void onCreateLabelClicked() {
            // Create popover window
            PopOver popover = new PopOver();
            VBox content = new VBox();
            content.setSpacing(10);
            content.setPadding(new Insets(10));

            // Posti attributes
            AtomicReference<String> postinro = new AtomicReference<>("");
            AtomicReference<String> toimipaikka = new AtomicReference<>("");

            // Popover nodes
            // Postinumero text field
            TextField postinroTF = new TextField();
            postinroTF.setPromptText("Postinro");
            postinroTF.textProperty().addListener((object, oldValue, newValue) -> {
                String nro = newValue.trim();
                if (nro.length() == 5 && nro.matches("[0-9]+")) {
                    postinro.set(nro);
                    postinroTF.setStyle("-fx-border-color: green");
                } else {
                    postinro.set("");
                    postinroTF.setStyle("-fx-border-color: red");
                }
            });
            // Toimipaikka text field
            TextField toimipaikkaTF = new TextField();
            toimipaikkaTF.setPromptText("Toimipaikka");
            toimipaikkaTF.textProperty().addListener((object, oldValue, newValue) -> {
                String tp = newValue.trim();
                if (!tp.isEmpty() && tp.length() <= 45) {
                    toimipaikka.set(tp);
                    toimipaikkaTF.setStyle("-fx-border-color: green");

                } else {
                    toimipaikka.set("");
                    toimipaikkaTF.setStyle("-fx-border-color: red");
                }
            });
            // Close button
            Button close = new Button("Peruuta");
            close.setOnAction(event -> {
                // Hide popover
                popover.hide();
            });
            // Add button
            Button add = new Button("Lisää");
            add.setOnAction(event -> {
                if (postinro.get().isEmpty() || toimipaikka.get().isEmpty()) {
                    return;
                } else {
                    Posti posti = new Posti(postinro.get(), toimipaikka.get());
                    dataBase.addRow("posti", posti.getAttrMap());
                    setLastSelectedItem(posti);
                }

                popover.hide();
            });
            // Buttons container
            HBox buttons = new HBox(close, add);
            buttons.setSpacing(10);

            // Popover root node
            content.getChildren().addAll(
                    postinroTF,
                    toimipaikkaTF,
                    buttons
            );

            // Set popover content and show it
            popover.setContentNode(content);
            popover.show(this);
        }
    };
    private TextField katuosoiteField = new TextField();
    private TextField hintaField = new TextField();
    private TextField henkilomaaraField = new TextField();
    private TextArea kuvausArea = new TextArea();
    private TextArea varusteluArea = new TextArea();

    // Constructor
    public MokkiDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
    }

    // Methods
    public Mokki getMokki() {return this.mokki;}

    @Override
    void setObject(Object object) {
        this.editMode = true;
        this.mokki = (Mokki) object;
        this.identifierValue = mokki.getMokki_id();
    }

    @Override
    void setDialogContent() {
        // DialogPane title
        dialogTitle.setText("Lisää uusi mökki");

        // Labels
        formsGridPane.add(new Label("Alue:"), 0, 0, 1, 1);
        formsGridPane.add(new Label("Nimi:"), 0, 1, 1, 1);
        formsGridPane.add(new Label("Postinro:"), 0, 2, 1, 1);
        formsGridPane.add(new Label("Katuosoite:"), 0, 3, 1, 1);
        formsGridPane.add(new Label("Hinta:"), 0, 4, 1, 1);
        formsGridPane.add(new Label("Henkilömrä:"), 0, 5, 1, 1);
        formsGridPane.add(new Label("Kuvaus:"), 0, 6, 1, 1);
        formsGridPane.add(new Label("Varustelu:"), 0, 7, 1, 1);

        // Fields
        formsGridPane.add(alueCmBox, 1, 0, 1, 1);
        formsGridPane.add(nimiField, 1, 1, 1, 1);
        formsGridPane.add(postinroField, 1, 2, 1, 1);
        formsGridPane.add(katuosoiteField, 1, 3, 1, 1);
        formsGridPane.add(hintaField, 1, 4, 1, 1);
        formsGridPane.add(henkilomaaraField, 1, 5, 1, 1);
        formsGridPane.add(kuvausArea, 1, 6, 1, 1);
        formsGridPane.add(varusteluArea, 1, 7, 1, 1);
    }

    @Override
    void setEditContent() {
        // DialogPane title
        dialogTitle.setText("Päivitä mökin tiedot");

        // Set data from mokki-object
        alueCmBox.setValue(dataBase.getRow("alue", "alue_id", mokki.getAlue_id(), Alue.class));
        nimiField.setText(mokki.getMokkinimi());
        postinroField.setLastSelectedItem(dataBase.getRow("posti", "postinro", mokki.getPostinro(), Posti.class));
        katuosoiteField.setText(mokki.getKatuosoite());
        hintaField.setText(String.valueOf(mokki.getHinta()));
        henkilomaaraField.setText(String.valueOf(mokki.getHenkilomaara()));
        kuvausArea.setText(mokki.getKuvaus());
        varusteluArea.setText(mokki.getVarustelu());
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        return mokki.getAttrMap();
    }

    @Override
    boolean checkData() {
        if (!checkAlue()) // Alue
            alertTitle = "Alue virhe";
        else if (!checkNimi()) // Nimi
            alertTitle = "Nimi virhe";
        else if (!checkPostinro()) // Postinumero
            alertTitle = "Postinumero virhe";
        else if (!checkOsoite()) // Osoite
            alertTitle = "Osoite virhe";
        else if (!checkHinta()) // Hinta
            alertTitle = "Hinta virhe";
        else if (!checkHenkilomaara()) // Henkilömäärä
            alertTitle = "Henkilömäärä virhe";
        else if (!checkKuvaus()) // Kuvaus
            alertTitle = "Kuvaus virhe";
        else if (!checkVarustelu()) // Varustelu
            alertTitle = "Varustelu virhe";
        else { // if there is no data failure
            alertTitle = "Menestys";
            alertMessage = "Taulukko on päivitetty";
            return true;
        }

        // Returns false if there is a data failure
        return false;
    }

    /**
     * Checks alue-field. If alue is not selected returns false
     * @return false - if alue-field is empty
     */
    private boolean checkAlue() {
        var alue = alueCmBox.getValue();
        if (alue == null) { // is empty
            alertMessage = "Valitse Alue, kiitos!";
            return false;
        }

        // if ok
        mokki.setAlue_id(((Alue) alue).getAlue_id());
        return true;
    }

    /**
     * Checks nimi-field. Returns false if the field does not match the parameters
     * @return false - if nimi-field is empty or nimi length is more than 45 characters
     */
    private boolean checkNimi() {
        String n = nimiField.getText();
        if (n.isEmpty() || n.isBlank()) // is empty
            alertMessage = "Kirjoita Nimi, kiitos!";
        else if (n.length() > 45) // longer than 45 chars
            alertMessage = "Nimi pituus on liian pitkä (maximi 45 merkkiä)";
        else { // if ok
            mokki.setMokkinimi(n);
            return true;
        }
        return false;
    }

    /**
     * Checks posti-field. If posti is not selected returns false
     * @return false - if posti-field is empty
     */
    private boolean checkPostinro() {
        var posti = postinroField.getLastSelectedItem();
        if (posti == null) { // is empty
            alertMessage = "Valitse Posti, kiitos!";
            return false;
        }

        // if ok
        mokki.setPostinro(((Posti) posti).getPostinro());
        return true;
    }

    /**
     * Checks osoite-field. Returns false if the field does not match the parameters
     * @return false - if osoite-field is empty or osoite length is more than 45 characters
     */
    private boolean checkOsoite() {
        String ko = katuosoiteField.getText();
        if (ko.isEmpty() || ko.isBlank()) // is empty
            alertMessage = "Kirjoita Osoite, kiitos!";
        else if (ko.length() > 45) // longer than 45 chars
            alertMessage = "Osoite pituus on liian pitkä (maximi 45 merkkiä)";
        else { // if ok
            mokki.setKatuosoite(ko);
            return true;
        }

        return false;
    }

    /**
     * Checks hinta-field. Returns false if the field does not match the parameters
     * @return false - if hinta-field is empty, hinta is not a number or
     * hinta is too big (should be less than 1 billion)
     */
    private boolean checkHinta() {
        try {
            // is empty or is not a number
            double h = Double.parseDouble(hintaField.getText().replace(',', '.'));
            // bigger than 1 billion
            if (h / Math.pow(10,8) > 1) {
                alertMessage = "Hinta on liian suuri (on oltava alle 1 miljardi)";
                return false;
            }

            // if ok
            mokki.setHinta(h);
            return true;
        } catch (Exception ex) {
            alertMessage = "Hinta on ilmoitettava numerona";
            return false;
        }
    }

    /**
     * Checks kuvaus-field. Returns false if the field does not match the parameters
     * @return false - if kuvaus-field is empty or kuvaus length is more than 150 characters
     */
    private boolean checkKuvaus() {
        String k = kuvausArea.getText();
        if (k.isEmpty() || k.isBlank()) // is empty
            alertMessage = "Kirjoita kuvaus, kiitos!";
        else if (k.length() > 150) // is longer than 150 chars
            alertMessage = "Kuvaus pituus on liian pitkä (maximi 150 merkkiä)";
        else { // if ok
            mokki.setKuvaus(k);
            return true;
        }

        return false;
    }

    /**
     * Checks henkilomaara-field. Returns false if the field does not match the parameters
     * @return false - if henkilomaara-field is empty or henkilomaara is not a number
     */
    private boolean checkHenkilomaara() {
        try {
            // if empty or is not a number
            int hm = Integer.parseInt(henkilomaaraField.getText());

            // if ok
            mokki.setHenkilomaara(hm);
            return true;
        } catch (Exception ex) {
            alertMessage = "Henkilömäärä on ilmoitettava numerona";
            return false;
        }
    }

    /**
     * Checks varustelu-field. Returns false if the field does not match the parameters
     * @return false - if varustelu-field is empty or varustelu length is more than 150 characters
     */
    private boolean checkVarustelu() {
        String v = varusteluArea.getText();
        if (v.isEmpty() || v.isBlank()) // is empty
            alertMessage = "Kirjoita Varustelu, kiitos!";
        else if (v.length() > 100) // is longer than 100 chars
            alertMessage = "Varustelu pituus on liian pitkä (maximi 100 merkkiä)";
        else { // if ok
            mokki.setVarustelu(v);
            return true;
        }

        return false;
    }

}
