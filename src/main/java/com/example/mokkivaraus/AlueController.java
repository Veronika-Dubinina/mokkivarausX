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

public class AlueController extends TabController<Alue> implements Initializable {
    // Constructor
    public AlueController() {
        // Set class attributes
        super("alue", "alue_id", Alue.class);
    }
    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{"id", "nimi"};
        String[] attrs = new String[]{"alue_id", "nimi"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new AlueDC(tableName, identifierKey);
    }

    @Override
    boolean getSearchConditions(Alue alue, String newValue) {
        String searchKeyword = newValue.toLowerCase();
        if (alue.getNimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("nimi"))) {
            return true; // Match in Nimi
        }
            return false; // No match found

    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "nimi"};
    }

}
