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

public class LaskuController extends TabController<Lasku> {
    // Constructor
    public LaskuController() {
        // Set class attributes
        super("asiakkaiden_laskut", "lasku_id", Lasku.class);
        filter = "WHERE alue_id = " + SessionData.alue.getAlue_id();
    }

    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{"id", "asiakas_etunimi", "asiakas_sukunimi", "mokki", "varattu_pvm", "summa", "alv", "maksettu"};
        String[] attrs = new String[]{"lasku_id", "asiakas_etunimi", "asiakas_sukunimi", "mokkinimi", "varattu_pvm", "summa", "alv", "maksettu"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new LaskuDC("lasku", identifierKey);
    }

    @Override
    boolean getSearchConditions(Lasku lasku, String newValue) {
        // Implement your search conditions here
        // For example:
        String searchKeyword = newValue.toLowerCase();
        if (String.valueOf(lasku.getLasku_id()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("id"))) {
            return true; // Match in ID
        } else if (lasku.getAsiakas_etunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("as_etunimi"))) {
            return true; // Match in Asiakas etunimi
        } else if (lasku.getAsiakas_sukunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("as_sukunimi"))) {
            return true; // Match in Asiakas sukunimi
        } else if (lasku.getMokkinimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("mökki"))) {
            return true; // Match in Mokkinimi
        } else if (String.valueOf(lasku.getVarattu_pvm()).contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varattu_pvm"))) {
            return true; // Match in Varaus varattu_pvm
        } else if (String.valueOf(lasku.getSumma()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("summa"))) {
            return true; // Match in Summa
        } else if (String.valueOf(lasku.getAlv()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("alv"))) {
            return true; // Match in Alv
        } else if (String.valueOf(lasku.getMaksettu()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("maksettu"))) {
            return true; // Match in Maksettu
        }
        return false; // No match found
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "id", "as_etunimi", "as_sukunimi", "mökki", "varattu_pvm", "summa", "alv", "maksettu"};
    }
}
