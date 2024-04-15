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

public class AsiakasController extends TabController<Asiakas> {
    // Constructor
    public AsiakasController() {
        // Set class attributes
        super(Asiakas.class);
    }
    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{"id", "postinro", "etunimi", "sukunimi", "lahiosoite", "email", "puhelinnro"};
        String[] attrs = new String[]{"asiakas_id", "postinro", "etunimi", "sukunimi", "lahiosoite", "email", "puhelinnro"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new AsiakasDC("asiakas", "asiakas_id");
    }

    @Override
    boolean getSearchConditions(Asiakas asiakas, String newValue) {
        String searchKeyword = newValue.toLowerCase();
        if (asiakas.getEtunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("etunimi"))) {
            return true; // Match in Etunimi
        } else if (asiakas.getSukunimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("sukunimi"))) {
            return true; // Match in Sukunimi
        } else if (asiakas.getPostinro().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("postinro"))) {
            return true; // Match in Postinro
        } else if (asiakas.getPuhelinnro().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("puhelinnro"))) {
            return true; // Match in Puhelinnro
        } else if (asiakas.getLahiosoite().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("lahiosoite"))) {
            return true; // Match in Lahiosoite
        } else {
            return false; // No match found
        }
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "etunimi", "sukunimi","postinro", "puhelinnro","lahiosoite"};
    }
}
