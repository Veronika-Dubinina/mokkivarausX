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
        super("varaus", "varaus_id", Varaus.class);
    }

    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{ "varaus_id","asiakas_id", "mokki_mokki_id", "varattu_pvm", "vahvistus_pvm", "varattu_alkupvm", "varattu_loppupvm"};
        String[] attrs = new String[]{ "varaus_id","asiakas_id", "mokki_mokki_id", "varattu_pvm", "vahvistus_pvm", "varattu_alkupvm", "varattu_loppupvm"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new VarausDC(tableName, identifierKey);
    }

    @Override
    boolean getSearchConditions(Varaus varaus, String newValue) {
        String searchKeyword = newValue.toLowerCase();
        // Implement your search conditions here
        return false;
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "asiakas_id", "mokki_mokki_id", "varattu_pvm", "vahvistus_pvm", "varattu_alkupvm", "varattu_loppupvm"};
    }
}
