package com.example.mokkivaraus;

import javafx.scene.control.*;
import java.util.HashMap;

public class AsiakasDC extends DialogController {
    // Attributes
    private Asiakas asiakas = new Asiakas();

    private TextField postinroField = new TextField();
    private TextField etunimiField = new TextField();
    private TextField sukunimiField = new TextField();
    private TextField lahiosoiteField = new TextField();
    private TextField emailField = new TextField();
    private TextField puhelinnroField = new TextField();

    // Constructor
    public AsiakasDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
    }

    // Methods
    public Asiakas getAsiakas() {
        return this.asiakas;
    }

    @Override
    void setObject(Object object) {
        this.editMode = true;
        this.asiakas = (Asiakas) object;
        this.identifierValue = asiakas.getAsiakas_id();
    }

    @Override
    void setDialogContent() {
        // DialogPane title
        dialogTitle.setText("Lisää uusi asiakas");

        // Labels and Fields
        formsGridPane.add(new Label("Postinro:"), 0, 0);
        formsGridPane.add(postinroField, 1, 0);

        formsGridPane.add(new Label("Etunimi:"), 0, 1);
        formsGridPane.add(etunimiField, 1, 1);

        formsGridPane.add(new Label("Sukunimi:"), 0, 2);
        formsGridPane.add(sukunimiField, 1, 2);

        formsGridPane.add(new Label("Lähiosoite:"), 0, 3);
        formsGridPane.add(lahiosoiteField, 1, 3);

        formsGridPane.add(new Label("Email:"), 0, 4);
        formsGridPane.add(emailField, 1, 4);

        formsGridPane.add(new Label("Puhelinnumero:"), 0, 5);
        formsGridPane.add(puhelinnroField, 1, 5);
    }

    @Override
    void setEditContent() {
        // DialogPane title
        dialogTitle.setText("Päivitä asiakkaan tiedot");

        // Set data from asiakas-object
        postinroField.setText(asiakas.getPostinro());
        etunimiField.setText(asiakas.getEtunimi());
        sukunimiField.setText(asiakas.getSukunimi());
        lahiosoiteField.setText(asiakas.getLahiosoite());
        emailField.setText(asiakas.getEmail());
        puhelinnroField.setText(asiakas.getPuhelinnro());
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("postinro", postinroField.getText());
        attrMap.put("etunimi", etunimiField.getText());
        attrMap.put("sukunimi", sukunimiField.getText());
        attrMap.put("lahiosoite", lahiosoiteField.getText());
        attrMap.put("email", emailField.getText());
        attrMap.put("puhelinnro", puhelinnroField.getText());
        return attrMap;
    }

    @Override
    boolean checkData() {
        if (!checkPostinro() || !checkEtunimi() || !checkSukunimi() || !checkLahiosoite() || !checkEmail() || !checkPuhelinnro()) {
            alertTitle = "Virhe tietojen tarkastuksessa";
            alertMessage = "Tarkista syöttämäsi tiedot ja yritä uudelleen.";
            return false;
        } else {
            alertTitle = "Menestys";
            alertMessage = "Tiedot tarkastettu ja hyväksytty.";
            return true;
        }
    }

    // Methods to check each field individually
    private boolean checkPostinro() {
        // Implement your validation logic for postinro field
        return true; // Return true if validation passes
    }

    private boolean checkEtunimi() {
        // Implement your validation logic for etunimi field
        return true; // Return true if validation passes
    }

    private boolean checkSukunimi() {
        // Implement your validation logic for sukunimi field
        return true; // Return true if validation passes
    }

    private boolean checkLahiosoite() {
        // Implement your validation logic for lahiosoite field
        return true; // Return true if validation passes
    }

    private boolean checkEmail() {
        // Implement your validation logic for email field
        return true; // Return true if validation passes
    }

    private boolean checkPuhelinnro() {
        // Implement your validation logic for puhelinnro field
        return true; // Return true if validation passes
    }
}
