package com.example.mokkivaraus;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.util.HashMap;
import java.time.LocalDate;
public class VarausDC extends DialogController {
    // Attributes
    private Varaus varaus = new Varaus();

    private ComboBox<Asiakas> asiakasCmBox = new ComboBox<>(dataBase.getAllRows("asiakas", "asiakas_id", Asiakas.class));
    private ComboBox<Mokki> mokkiCmBox = new ComboBox<>(dataBase.getAllRows("mokki", "mokki_id", Mokki.class));
    private DatePicker varattuPvmPicker = new DatePicker();
    private DatePicker vahvistusPvmPicker = new DatePicker();
    private DatePicker varattuAlkupvmPicker = new DatePicker();
    private DatePicker varattuLoppupvmPicker = new DatePicker();

    private ComboBox<String> varattuPvmHour = new ComboBox<>();
    private ComboBox<String> varattuPvmMinute = new ComboBox<>();
    private ComboBox<String> vahvistusPvmHour = new ComboBox<>();
    private ComboBox<String> vahvistusPvmMinute = new ComboBox<>();
    private ComboBox<String> varattuAlkupvmHour = new ComboBox<>();
    private ComboBox<String> varattuAlkupvmMinute = new ComboBox<>();
    private ComboBox<String> varattuLoppupvmHour = new ComboBox<>();
    private ComboBox<String> varattuLoppupvmMinute = new ComboBox<>();

    // Constructor
    public VarausDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
        initializeTimeComboBoxes();
    }

    // Methods
    private void initializeTimeComboBoxes() {
        for (int i = 0; i < 24; i++) {
            String formattedHour = String.format("%02d", i);
            varattuPvmHour.getItems().add(formattedHour);
            vahvistusPvmHour.getItems().add(formattedHour);
            varattuAlkupvmHour.getItems().add(formattedHour);
            varattuLoppupvmHour.getItems().add(formattedHour);
        }
        for (int i = 0; i < 60; i++) {
            String formattedMinute = String.format("%02d", i);
            varattuPvmMinute.getItems().add(formattedMinute);
            vahvistusPvmMinute.getItems().add(formattedMinute);
            varattuAlkupvmMinute.getItems().add(formattedMinute);
            varattuLoppupvmMinute.getItems().add(formattedMinute);
        }
    }

    public Varaus getVaraus() {
        return this.varaus;
    }

    @Override
    void setObject(Object object) {
        this.editMode = true;
        this.varaus = (Varaus) object;
        this.identifierValue = varaus.getVaraus_id();
    }

    @Override
    void setDialogContent() {
        dialogTitle.setText("Lisää uusi varaus");

        formsGridPane.add(new Label("Asiakas:"), 0, 0, 1, 1);
        formsGridPane.add(new Label("Mökki:"), 0, 1, 1, 1);
        formsGridPane.add(new Label("Varattu pvm:"), 0, 2, 1, 1);
        formsGridPane.add(new Label("Vahvistus pvm:"), 0, 3, 1, 1);
        formsGridPane.add(new Label("Varattu alkupvm:"), 0, 4, 1, 1);
        formsGridPane.add(new Label("Varattu loppupvm:"), 0, 5, 1, 1);

        formsGridPane.add(asiakasCmBox, 1, 0, 1, 1);
        formsGridPane.add(mokkiCmBox, 1, 1, 1, 1);
        formsGridPane.add(varattuPvmPicker, 1, 2, 1, 1);
        formsGridPane.add(vahvistusPvmPicker, 1, 3, 1, 1);
        formsGridPane.add(varattuAlkupvmPicker, 1, 4, 1, 1);
        formsGridPane.add(varattuLoppupvmPicker, 1, 5, 1, 1);

        formsGridPane.add(varattuPvmHour, 2, 2, 1, 1);
        formsGridPane.add(varattuPvmMinute, 3, 2, 1, 1);
        formsGridPane.add(vahvistusPvmHour, 2, 3, 1, 1);
        formsGridPane.add(vahvistusPvmMinute, 3, 3, 1, 1);
        formsGridPane.add(varattuAlkupvmHour, 2, 4, 1, 1);
        formsGridPane.add(varattuAlkupvmMinute, 3, 4, 1, 1);
        formsGridPane.add(varattuLoppupvmHour, 2, 5, 1, 1);
        formsGridPane.add(varattuLoppupvmMinute, 3, 5, 1, 1);
    }

    @Override
    void setEditContent() {
        dialogTitle.setText("Päivitä varauksen tiedot");

        asiakasCmBox.setValue(dataBase.getRow("asiakas", "asiakas_id", varaus.getAsiakas_id(), Asiakas.class));
        mokkiCmBox.setValue(dataBase.getRow("mokki", "mokki_id", varaus.getMokki_mokki_id(), Mokki.class));

        varattuPvmPicker.setValue(varaus.getVarattu_pvm().toLocalDateTime().toLocalDate());
        varattuPvmHour.setValue(String.format("%02d", varaus.getVarattu_pvm().toLocalDateTime().getHour()));
        varattuPvmMinute.setValue(String.format("%02d", varaus.getVarattu_pvm().toLocalDateTime().getMinute()));

        vahvistusPvmPicker.setValue(varaus.getVahvistus_pvm().toLocalDateTime().toLocalDate());
        vahvistusPvmHour.setValue(String.format("%02d", varaus.getVahvistus_pvm().toLocalDateTime().getHour()));
        vahvistusPvmMinute.setValue(String.format("%02d", varaus.getVahvistus_pvm().toLocalDateTime().getMinute()));

        varattuAlkupvmPicker.setValue(varaus.getVarattu_alkupvm().toLocalDateTime().toLocalDate());
        varattuAlkupvmHour.setValue(String.format("%02d", varaus.getVarattu_alkupvm().toLocalDateTime().getHour()));
        varattuAlkupvmMinute.setValue(String.format("%02d", varaus.getVarattu_alkupvm().toLocalDateTime().getMinute()));

        varattuLoppupvmPicker.setValue(varaus.getVarattu_loppupvm().toLocalDateTime().toLocalDate());
        varattuLoppupvmHour.setValue(String.format("%02d", varaus.getVarattu_loppupvm().toLocalDateTime().getHour()));
        varattuLoppupvmMinute.setValue(String.format("%02d", varaus.getVarattu_loppupvm().toLocalDateTime().getMinute()));
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("asiakas_id", asiakasCmBox.getValue().getAsiakas_id());
        attrMap.put("mokki_mokki_id", mokkiCmBox.getValue().getMokki_id());

        LocalDate varattuPvmDate = varattuPvmPicker.getValue();
        String varattuPvmString = varattuPvmDate.toString() + " " +
                varattuPvmHour.getValue() + ":" +
                varattuPvmMinute.getValue();
        attrMap.put("varattu_pvm", varattuPvmString);

        LocalDate vahvistusPvmDate = vahvistusPvmPicker.getValue();
        String vahvistusPvmString = vahvistusPvmDate.toString() + " " +
                vahvistusPvmHour.getValue() + ":" +
                vahvistusPvmMinute.getValue();
        attrMap.put("vahvistus_pvm", vahvistusPvmString);

        LocalDate varattuAlkupvmDate = varattuAlkupvmPicker.getValue();
        String varattuAlkupvmString = varattuAlkupvmDate.toString() + " " +
                varattuAlkupvmHour.getValue() + ":" +
                varattuAlkupvmMinute.getValue();
        attrMap.put("varattu_alkupvm", varattuAlkupvmString);

        LocalDate varattuLoppupvmDate = varattuLoppupvmPicker.getValue();
        String varattuLoppupvmString = varattuLoppupvmDate.toString() + " " +
                varattuLoppupvmHour.getValue() + ":" +
                varattuLoppupvmMinute.getValue();
        attrMap.put("varattu_loppupvm", varattuLoppupvmString);

        return attrMap;
    }

    @Override
    boolean checkData() {
        if (asiakasCmBox.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse asiakas";
            return false;
        } else if (mokkiCmBox.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse mökki";
            return false;
        } else if (varattuPvmPicker.getValue() == null || varattuPvmHour.getValue() == null || varattuPvmMinute.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu päivämäärä ja aika";
            return false;
        } else if (vahvistusPvmPicker.getValue() == null || vahvistusPvmHour.getValue() == null || vahvistusPvmMinute.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse vahvistus päivämäärä ja aika";
            return false;
        } else if (varattuAlkupvmPicker.getValue() == null || varattuAlkupvmHour.getValue() == null || varattuAlkupvmMinute.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu alkupäivämäärä ja aika";
            return false;
        } else if (varattuLoppupvmPicker.getValue() == null || varattuLoppupvmHour.getValue() == null || varattuLoppupvmMinute.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu loppupäivämäärä ja aika";
            return false;
        } else {
            alertTitle = "Menestys";
            alertMessage = "Tiedot tarkastettu ja hyväksytty.";
            return true;
        }
    }
}
