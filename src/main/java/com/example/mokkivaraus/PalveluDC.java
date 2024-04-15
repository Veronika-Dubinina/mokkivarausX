package com.example.mokkivaraus;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class PalveluDC extends DialogController {
    // Attributes
    private Palvelu palvelu = new Palvelu();

    private TextField idField = new TextField();
    private ComboBox<Alue> alueCmBox = new ComboBox<>(SessionData.getAlueet());
    private TextField nimiField = new TextField();
    private TextField tyyppiField = new TextField();
    private TextArea kuvausArea = new TextArea();
    private TextField hintaField = new TextField();
    private TextField alvField = new TextField();

    // Constructor
    public PalveluDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
    }

    // Methods
    @Override
    void setObject(Object object) {
        this.editMode = true;
        this.palvelu = (Palvelu) object;
        this.identifierValue = palvelu.getPalvelu_id();
    }

    @Override
    boolean checkData() {
        if (!checkId()) // Id
            alertTitle = "ID virhe";
        else if (!checkAlue()) // Alue
            alertTitle = "Alue virhe";
        else if (!checkNimi()) // Nimi
            alertTitle = "Nimi virhe";
        else if (!checkTyyppi()) // Tyyppi
            alertTitle = "Tyyppi virhe";
        else if (!checkKuvaus()) // Kuvaus
            alertTitle = "Kuvaus virhe";
        else if (!checkHinta()) // Hinta
            alertTitle = "Hinta virhe";
        else if (!checkAlv()) // ALV
            alertTitle = "ALV virhe";
        else { // if there is no data failure
            alertTitle = "Menestys";
            alertMessage = "Taulukko on päivitetty";
            return true;
        }

        // Returns false if there is a data failure
        return false;
    }

    @Override
    void setDialogContent() {
        // DialogPane title
        dialogTitle.setText("Lisää uusi palvelu");
        // Alue
        alueCmBox.setValue(SessionData.alue);
        alueCmBox.setDisable(true);

        // Labels
        formsGridPane.add(new Label("ID:"), 0, 0, 1, 1);
        formsGridPane.add(new Label("Alue:"), 0, 1, 1, 1);
        formsGridPane.add(new Label("Nimi:"), 0, 2, 1, 1);
        formsGridPane.add(new Label("Tyyppi:"), 0, 3, 1, 1);
        formsGridPane.add(new Label("Kuvaus:"), 0, 4, 1, 1);
        formsGridPane.add(new Label("Hinta:"), 0, 5, 1, 1);
        formsGridPane.add(new Label("Alv:"), 0, 6, 1, 1);

        // Fields
        formsGridPane.add(idField, 1, 0, 1, 1);
        formsGridPane.add(alueCmBox, 1, 1, 1, 1);
        formsGridPane.add(nimiField, 1, 2, 1, 1);
        formsGridPane.add(tyyppiField, 1, 3, 1, 1);
        formsGridPane.add(kuvausArea, 1, 4, 1, 1);
        formsGridPane.add(hintaField, 1, 5, 1, 1);
        formsGridPane.add(alvField, 1, 6, 1, 1);
    }

    @Override
    void setEditContent() {
        // DialogPane title
        dialogTitle.setText("Päivitä palvelun tiedot");
        // Alue
        alueCmBox.setDisable(false);

        // Set data from mokki-object
        idField.setText(String.valueOf(palvelu.getPalvelu_id()));
        idField.setDisable(true);
        alueCmBox.setValue(SessionData.alue);
        nimiField.setText(palvelu.getNimi());
        tyyppiField.setText(String.valueOf(palvelu.getTyyppi()));
        kuvausArea.setText(palvelu.getKuvaus());
        hintaField.setText(String.valueOf(palvelu.getHinta()));
        alvField.setText(String.valueOf(palvelu.getAlv()));
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        return palvelu.getAttrMap();
    }

    /**
     * Checks id-field. Returns false if the field does not match the parameters
     * @return false - if id-field is empty, id is not a number.
     */
    private boolean checkId() {
        try {
            // is empty or is not a number
            int id = Integer.parseInt(idField.getText());
            // if ok
            palvelu.setPalvelu_id(id);
            return true;
        } catch (Exception ex) {
            alertMessage = "ID on ilmoitettava numerona";
            return false;
        }
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
        palvelu.setAlue_id(((Alue) alue).getAlue_id());
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
            palvelu.setNimi(n);
            return true;
        }
        return false;
    }

    /**
     * Checks tyyppi-field. Returns false if the field does not match the parameters
     * @return false - if tyyppi-field is empty, tyyppi is not a number.
     */
    private boolean checkTyyppi() {
        try {
            // is empty or is not a number
            int t = Integer.parseInt(tyyppiField.getText());
            // if ok
            palvelu.setTyyppi(t);
            return true;
        } catch (Exception ex) {
            alertMessage = "Tyyppi on ilmoitettava numerona";
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
            palvelu.setKuvaus(k);
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
            palvelu.setHinta(h);
            return true;
        } catch (Exception ex) {
            alertMessage = "Hinta on ilmoitettava numerona";
            return false;
        }
    }

    /**
     * Checks ALV-field. Returns false if the field does not match the parameters
     * @return false - if ALV-field is empty, ALV is not a number or
     * ALV is too big (should be less than 1 billion)
     */
    private boolean checkAlv() {
        try {
            // is empty or is not a number
            double a = Double.parseDouble(alvField.getText().replace(',', '.'));
            // bigger than 1 billion
            if (a / Math.pow(10,8) > 1) {
                alertMessage = "ALV on liian suuri (on oltava alle 1 miljardi)";
                return false;
            }

            // if ok
            palvelu.setAlv(a);
            return true;
        } catch (Exception ex) {
            alertMessage = "ALV on ilmoitettava numerona";
            return false;
        }
    }
}
