package com.example.mokkivaraus;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

public class VarausDC extends DialogController {
    // Attributes
    private Varaus varaus = new Varaus();

    private ComboBox<Asiakas> asiakasCmBox = new ComboBox<>(SessionData.getAsiakkaat());
    private ComboBox<Mokki> mokkiCmBox = new ComboBox<>(SessionData.getMokit());
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

        // Asiakas
        SessionData.getAsiakkaat().forEach(asiakas -> {
            if (asiakas.getAsiakas_id() == varaus.getAsiakas_id()) {
                asiakasCmBox.setValue(asiakas);
                return;
            }
        });
        // Mokki
        SessionData.getMokit().forEach(mokki -> {
            if (mokki.getMokki_id() == varaus.getMokki_mokki_id()) {
                mokkiCmBox.setValue(mokki);
                return;
            }
        });
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
        return varaus.getAttrMap();
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
            setData();
            return true;
        }
    }

    /**
     * Saves reservation data into Varaus-object
     */
    private void setData() {
        varaus.setAsiakas_id(asiakasCmBox.getValue().getAsiakas_id());
        varaus.setMokki_mokki_id(mokkiCmBox.getValue().getMokki_id());

        LocalDateTime varattuPvmDate = varattuPvmPicker.getValue().atTime(Integer.parseInt(varattuPvmHour.getValue()), Integer.parseInt(varattuPvmMinute.getValue()));
        varaus.setVarattu_pvm(Timestamp.valueOf(varattuPvmDate));

        LocalDateTime vahvistusPvmDate = vahvistusPvmPicker.getValue().atTime(Integer.parseInt(vahvistusPvmHour.getValue()), Integer.parseInt(vahvistusPvmMinute.getValue()));
        varaus.setVahvistus_pvm(Timestamp.valueOf(vahvistusPvmDate));

        LocalDateTime varattuAlkupvmDate = varattuAlkupvmPicker.getValue().atTime(Integer.parseInt(varattuAlkupvmHour.getValue()), Integer.parseInt(varattuAlkupvmMinute.getValue()));
        varaus.setVarattu_alkupvm(Timestamp.valueOf(varattuAlkupvmDate));

        LocalDateTime varattuLoppupvmDate = varattuLoppupvmPicker.getValue().atTime(Integer.parseInt(varattuLoppupvmHour.getValue()), Integer.parseInt(varattuLoppupvmMinute.getValue()));
        varaus.setVarattu_loppupvm(Timestamp.valueOf(varattuLoppupvmDate));
    }
}
