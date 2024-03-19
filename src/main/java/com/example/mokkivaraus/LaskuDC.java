package com.example.mokkivaraus;

import javafx.scene.control.*;

import java.util.HashMap;

public class LaskuDC extends DialogController {
    // Attributes
    private Lasku lasku = new Lasku();

    private ComboBox<Varaus> varausCmBox = new ComboBox<>(dataBase.getAllRows("varaus", "varaus_id", Varaus.class));
    private TextField summaField = new TextField();
    private TextField alvField = new TextField();
    private CheckBox maksettuCheckBox = new CheckBox();

    // Constructor
    public LaskuDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
    }

    // Methods
    public Lasku getLasku() { return this.lasku; }

    @Override
    void setObject(Object object) {
        this.editMode = true;
        this.lasku = (Lasku) object;
        this.identifierValue = lasku.getLasku_id();
    }

    @Override
    void setDialogContent() {
        // DialogPane title
        dialogTitle.setText("Lisää uusi lasku");

        // Labels
        formsGridPane.add(new Label("Varaus ID:"), 0, 1, 1, 1);
        formsGridPane.add(new Label("Summa:"), 0, 2, 1, 1);
        formsGridPane.add(new Label("ALV:"), 0, 3, 1, 1);
        formsGridPane.add(new Label("Maksettu:"), 0, 4, 1, 1);

        // Fields
        formsGridPane.add(varausCmBox, 1, 1, 1, 1);
        formsGridPane.add(summaField, 1, 2, 1, 1);
        formsGridPane.add(alvField, 1, 3, 1, 1);
        formsGridPane.add(maksettuCheckBox, 1, 4, 1, 1);
    }

    @Override
    void setEditContent() {
        // DialogPane title
        dialogTitle.setText("Päivitä laskun tiedot");

        // Set data from lasku-object
        varausCmBox.setValue(dataBase.getRow("varaus", "varaus_id", lasku.getVaraus_id(), Varaus.class));
        summaField.setText(String.valueOf(lasku.getSumma()));
        alvField.setText(String.valueOf(lasku.getAlv()));
        maksettuCheckBox.setSelected(lasku.isMaksettu());
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("varaus_id", varausCmBox.getValue().getVaraus_id());
        attrMap.put("summa", Double.parseDouble(summaField.getText()));
        attrMap.put("alv", Double.parseDouble(alvField.getText()));
        attrMap.put("maksettu", maksettuCheckBox.isSelected() ? 1 : 0);
        return attrMap;
    }

    @Override
    boolean checkData() {
        // Perform data validation checks here
        // For example:
        if (!checkSumma()) // Summa
            alertTitle = "Summa virhe";
        else if (!checkAlv()) // Alv
            alertTitle = "Alv virhe";
        else { // if there is no data failure
            alertTitle = "Menestys";
            alertMessage = "Taulukko on päivitetty";
            return true;
        }

        // Returns false if there is a data failure
        return false;
    }

    /**
     * Checks summa-field. Returns false if the field does not match the parameters
     * @return false - if summa-field is empty or not a valid double
     */
    private boolean checkSumma() {
        try {
            double summa = Double.parseDouble(summaField.getText());
            if (summa <= 0) { // non-positive summa
                alertMessage = "Summa tulee olla positiivinen luku";
                return false;
            }
        } catch (NumberFormatException e) {
            alertMessage = "Syötä validi summa";
            return false;
        }
        return true;
    }

    /**
     * Checks alv-field. Returns false if the field does not match the parameters
     * @return false - if alv-field is empty or not a valid double
     */
    private boolean checkAlv() {
        try {
            double alv = Double.parseDouble(alvField.getText());
            if (alv < 0) { // negative alv
                alertMessage = "ALV ei voi olla negatiivinen";
                return false;
            }
        } catch (NumberFormatException e) {
            alertMessage = "Syötä validi arvo ALV:lle";
            return false;
        }
        return true;
    }
}
