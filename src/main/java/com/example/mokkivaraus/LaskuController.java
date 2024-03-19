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

public class LaskuController extends TabController<Lasku> implements Initializable {
    // Constructor
    public LaskuController() {
        // Set class attributes
        super("lasku", "lasku_id", Lasku.class);
    }

    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{"id", "varaus_id", "summa", "alv", "maksettu"};
        String[] attrs = new String[]{"lasku_id", "varaus_id", "summa", "alv", "maksettu"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new LaskuDC(tableName, identifierKey);
    }

    @Override
    boolean getSearchConditions(Lasku lasku, String newValue) {
        // Implement your search conditions here
        // For example:
        String searchKeyword = newValue.toLowerCase();
        if (String.valueOf(lasku.getLasku_id()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("id"))) {
            return true; // Match in ID
        } else if (String.valueOf(lasku.getVaraus_id()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varaus_id"))) {
            return true; // Match in Varaus ID
        } else if (String.valueOf(lasku.getSumma()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("summa"))) {
            return true; // Match in Summa
        } else if (String.valueOf(lasku.getAlv()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("alv"))) {
            return true; // Match in Alv
        } else if (String.valueOf(lasku.isMaksettu()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("maksettu"))) {
            return true; // Match in Maksettu
        }
        return false; // No match found
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "id", "varaus_id", "summa", "alv", "maksettu"};
    }
}
