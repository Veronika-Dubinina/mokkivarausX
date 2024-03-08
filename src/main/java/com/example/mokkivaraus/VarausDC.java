package com.example.mokkivaraus;

import javafx.scene.control.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;

public class VarausDC extends DialogController {
    // Attributes
    private Varaus varaus = new Varaus();

    private ComboBox<Asiakas> asiakasCmBox = new ComboBox<>(dataBase.getAllRows("asiakas", "asiakas_id", Asiakas.class));
    private ComboBox<Mokki> mokkiCmBox = new ComboBox<>(dataBase.getAllRows("mokki", "mokki_id", Mokki.class));
    private DatePicker varattuPvmPicker = new DatePicker();
    private DatePicker vahvistusPvmPicker = new DatePicker();
    private DatePicker varattuAlkupvmPicker = new DatePicker();
    private DatePicker varattuLoppupvmPicker = new DatePicker();

    // Constructor
    public VarausDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
    }

    // Methods
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
        // DialogPane title
        dialogTitle.setText("Lisää uusi varaus");

        // Labels
        formsGridPane.add(new Label("Asiakas:"), 0, 1, 1, 1);
        formsGridPane.add(new Label("Mökki:"), 0, 2, 1, 1);
        formsGridPane.add(new Label("Varattu pvm:"), 0, 3, 1, 1);
        formsGridPane.add(new Label("Vahvistus pvm:"), 0, 4, 1, 1);
        formsGridPane.add(new Label("Varattu alkupvm:"), 0, 5, 1, 1);
        formsGridPane.add(new Label("Varattu loppupvm:"), 0, 6, 1, 1);

        // Fields
        formsGridPane.add(asiakasCmBox, 1, 1, 1, 1);
        formsGridPane.add(mokkiCmBox, 1, 2, 1, 1);
        formsGridPane.add(varattuPvmPicker, 1, 3, 1, 1);
        formsGridPane.add(vahvistusPvmPicker, 1, 4, 1, 1);
        formsGridPane.add(varattuAlkupvmPicker, 1, 5, 1, 1);
        formsGridPane.add(varattuLoppupvmPicker, 1, 6, 1, 1);
    }
// не добавлят окно редактирования почему то я хз
    @Override
    void setEditContent() {
        // DialogPane title
        dialogTitle.setText("Päivitä varauksen tiedot");
        System.out.println("V1");

        // Set data from varaus-object
        asiakasCmBox.setValue(dataBase.getRow("asiakas", "asiakas_id", varaus.getAsiakas_id(), Asiakas.class));
        mokkiCmBox.setValue(dataBase.getRow("mokki", "mokki_id", varaus.getMokki_mokki_id(), Mokki.class));
        varattuPvmPicker.setValue(varaus.getVarattu_pvm().toLocalDateTime().toLocalDate());
        vahvistusPvmPicker.setValue(varaus.getVahvistus_pvm().toLocalDateTime().toLocalDate());
        varattuAlkupvmPicker.setValue(varaus.getVarattu_alkupvm().toLocalDateTime().toLocalDate());
        varattuLoppupvmPicker.setValue(varaus.getVarattu_loppupvm().toLocalDateTime().toLocalDate());
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("asiakas_id", asiakasCmBox.getValue().getAsiakas_id());
        attrMap.put("mokki_mokki_id", mokkiCmBox.getValue().getMokki_id());
        attrMap.put("varattu_pvm", varattuPvmPicker.getValue());
        attrMap.put("vahvistus_pvm", vahvistusPvmPicker.getValue());
        attrMap.put("varattu_alkupvm", varattuAlkupvmPicker.getValue());
        attrMap.put("varattu_loppupvm", varattuLoppupvmPicker.getValue());

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
        } else if (varattuPvmPicker.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu päivämäärä";
            return false;
        } else if (vahvistusPvmPicker.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse vahvistus päivämäärä";
            return false;
        } else if (varattuAlkupvmPicker.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu alkupäivämäärä";
            return false;
        } else if (varattuLoppupvmPicker.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu loppupäivämäärä";
            return false;
        } else {
            alertTitle = "Menestys";
            alertMessage = "Tiedot tarkastettu ja hyväksytty.";
            return true;
        }
    }
}
