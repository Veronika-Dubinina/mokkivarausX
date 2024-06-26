package com.example.mokkivaraus;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class VarausController extends TabController<Varaus> {
    // Constructor
    public VarausController() {
        // Set class attributes
        super(Varaus.class);
        dialogFXML = "varaus-dialog.fxml";
    }

    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{ "varaus_id","asiakas", "mokkinimi", "varattu_pvm", "vahvistus_pvm", "varattu_alkupvm", "varattu_loppupvm"};
        String[] attrs = new String[]{ "varaus_id","asiakasnimi", "mokkinimi", "varattu_pvm", "vahvistus_pvm", "varattu_alkupvm", "varattu_loppupvm"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new VarausDialogController("varaus", "varaus_id");
    }

    @Override
    boolean getSearchConditions(Varaus varaus, String newValue) {
        String searchKeyword = newValue.toLowerCase();
        // Implement your search conditions here
        if (varaus.getAsiakasnimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("asiakas"))) {
            return true; // Match by Asiakas
        } else if (varaus.getMokkinimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("mokki"))) {
            return true; // Match by Mokki
        } else if (String.valueOf(varaus.getVarattu_pvm()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varattu_pvm"))) {
            return true; // Match by booking date
        } else if (String.valueOf(varaus.getVahvistus_pvm()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("vahvistus_pvm"))) {
            return true; // Match by confirmation date
        } else if (String.valueOf(varaus.getVarattu_alkupvm()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varattu_alkupvm"))) {
            return true; // Match by start date
        } else if (String.valueOf(varaus.getVarattu_loppupvm()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varattu_loppupvm"))) {
            return true; // Match by end date
        } else
            return false; // No match
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "asiakas", "mokki", "varattu_pvm", "vahvistus_pvm", "varattu_alkupvm", "varattu_loppupvm"};
    }
}
