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

public class MokkiController extends TabController<Mokki> implements Initializable  {
    // Constructor
    public MokkiController() {
        // Set class attributes
        super("mokki", "mokki_id", Mokki.class);
    }

    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{"id", "nimi", "postinro", "katuosoite", "hinta", "kuvaus", "henkilomäärä", "varustelu"};
        String[] attrs = new String[]{"mokki_id", "mokkinimi", "postinro", "katuosoite", "hinta", "kuvaus", "henkilomaara", "varustelu"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new MokkiDC(tableName, identifierKey);
    }

    @Override
    boolean getSearchConditions(Mokki mokki, String newValue) {
        String searchKeyword = newValue.trim().toLowerCase();
        if (mokki.getMokkinimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("nimi"))) {
            return true; // Match in Nimi
        } else if (mokki.getKatuosoite().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("osoite"))) {
            return true; // Match in Osoite
        } else if (mokki.getKuvaus().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("kuvaus"))) {
            return true; // Match in Kuvaus
        } else if (mokki.getVarustelu().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("varustelu"))) {
            return true; // Match in Varustelu
        } else if (mokki.getPostinro().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("postinro"))) {
            return true; // Match in Postinumero
        } else if (String.valueOf(mokki.getHinta()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("hinta"))) {
            return true; // Match in Hinta
        } else if (String.valueOf(mokki.getHenkilomaara()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("hm"))) {
            return true; // Match in Henkilomaara
        } else {
            return false; // No match found
        }
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "nimi", "postinro", "osoite", "hinta", "kuvaus", "hm", "varustelu"};
    }
}
