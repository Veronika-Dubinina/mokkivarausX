package com.example.mokkivaraus;

import javafx.scene.control.*;

import java.util.HashMap;

public class AlueDC extends DialogController{
    // Attributes
    private Alue alue = new Alue();
    private TextField nimiField = new TextField();

    // Constructor
    public AlueDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
    }

    // Methods
    @Override
    void setObject(Object object) {
    }

    @Override
    void setDialogContent() {
        // DialogPane title
        dialogTitle.setText("Lisää uusi alue");

        // Labels
        formsGridPane.add(new Label("Nimi:"), 0, 1, 1, 1);

        // Fields
        formsGridPane.add(nimiField, 1, 1, 1, 1);
    }

    @Override
    void setEditContent() {
    }
    @Override
    HashMap<String, Object> listOfAttributes() {
        return alue.getAttrMap();
    }

    @Override
    boolean checkData() {
        if (!checkNimi()) // Nimi
            alertTitle = "Nimi virhe";
        else { // if there is no data failure
            alertTitle = "Menestys";
            alertMessage = "Taulukko on päivitetty";
            return true;
        }

        // Returns false if there is a data failure
        return false;
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
            alue.setNimi(n);
            return true;
        }
        return false;
    }
}
