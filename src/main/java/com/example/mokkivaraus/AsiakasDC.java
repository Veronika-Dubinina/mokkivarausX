package com.example.mokkivaraus;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
        formsGridPane.add(new Label("Postinro:"), 0, 0, 1, 1);
        formsGridPane.add(postinroField, 1, 0, 1, 1);

        formsGridPane.add(new Label("Etunimi:"), 0, 1, 1, 1);
        formsGridPane.add(etunimiField, 1, 1, 1, 1);

        formsGridPane.add(new Label("Sukunimi:"), 0, 2, 1, 1);
        formsGridPane.add(sukunimiField, 1, 2, 1, 1);

        formsGridPane.add(new Label("Lähiosoite:"), 0, 3, 1, 1);
        formsGridPane.add(lahiosoiteField, 1, 3, 1, 1);

        formsGridPane.add(new Label("Email:"), 0, 4, 1, 1);
        formsGridPane.add(emailField, 1, 4, 1, 1);

        formsGridPane.add(new Label("Puhelinnro:"), 0, 5, 1, 1);
        formsGridPane.add(puhelinnroField, 1, 5, 1, 1);
    }

    @Override
    void setEditContent() {
        // DialogPane title
        dialogTitle.setText("Päivitä asiakkaan tiedot");

        // Set data from asiakas-object
        // Populate fields with existing data for editing
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        return asiakas.getAttrMap();
    }

    @Override
    boolean checkData() {
        if (!validatePostinro()) // Postinro
            alertTitle = "Postinro virhe";
        else if (!validateEtunimi()) // Etunimi
            alertTitle = "Etunimi virhe";
        else if (!validateSukunimi()) // Sukunimi
            alertTitle = "Sukunimi virhe";
        else if (!validateLahiosoite()) // Lahiosoite
            alertTitle = "Lähiosoite virhe";
        else if (!validateEmail()) // Email
            alertTitle = "Email virhe";
        else if (!validatePuhelinnro()) // Puhelinnro
            alertTitle = "Puhelinnro virhe";
        else { // if all data is valid
            alertTitle = "Menestys";
            alertMessage = "Taulukko on päivitetty";
            return true;
        }

        // Returns false if there is a data failure
        return false;
    }

    /**
     * Checks postinro-field. Returns false if the field does not match the parameters
     *
     * @return false - if postinro-field is empty or invalid
     */
    private boolean validatePostinro() {
        String postinro = postinroField.getText();
        // Implement postinro validation logic
        return true; // Replace true with validation logic
    }

    /**
     * Checks etunimi-field. Returns false if the field does not match the parameters
     *
     * @return false - if etunimi-field is empty or invalid
     */
    private boolean validateEtunimi() {
        String etunimi = etunimiField.getText();
        // Implement etunimi validation logic
        return true; // Replace true with validation logic
    }

    /**
     * Checks sukunimi-field. Returns false if the field does not match the parameters
     *
     * @return false - if sukunimi-field is empty or invalid
     */
    private boolean validateSukunimi() {
        String sukunimi = sukunimiField.getText();
        // Implement sukunimi validation logic
        return true; // Replace true with validation logic
    }

    /**
     * Checks lahiosoite-field. Returns false if the field does not match the parameters
     *
     * @return false - if lahiosoite-field is empty or invalid
     */
    private boolean validateLahiosoite() {
        String lahiosoite = lahiosoiteField.getText();
        // Implement lahiosoite validation logic
        return true; // Replace true with validation logic
    }

    /**
     * Checks email-field. Returns false if the field does not match the parameters
     *
     * @return false - if email-field is empty or invalid
     */
    private boolean validateEmail() {
        String email = emailField.getText();
        // Implement email validation logic
        return true; // Replace true with validation logic
    }

    /**
     * Checks puhelinnro-field. Returns false if the field does not match the parameters
     *
     * @return false - if puhelinnro-field is empty or invalid
     */
    private boolean validatePuhelinnro() {
        String puhelinnro = puhelinnroField.getText();
        // Implement puhelinnro validation logic
        return true; // Replace true with validation logic
    }
}
