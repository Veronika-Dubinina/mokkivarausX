package com.example.mokkivaraus;

import java.util.ArrayList;

public class PalveluController extends TabController<Palvelu> {
    // Constructor
    public PalveluController() {
        super(Palvelu.class);
    }

    // Methods
    @Override
    ArrayList<String[]> getColToAttr() {
        String[] cols = new String[]{"id", "nimi", "tyyppi", "kuvaus", "hinta", "alv"};
        String[] attrs = new String[]{"palvelu_id", "nimi", "tyyppi", "kuvaus", "hinta", "alv"};
        ArrayList<String[]> colToAttr = new ArrayList<>();
        colToAttr.add(cols);
        colToAttr.add(attrs);
        return colToAttr;
    }

    @Override
    DialogController getController() {
        return new PalveluDC("palvelu", "palvelu_id");
    }

    @Override
    boolean getSearchConditions(Palvelu palvelu, String newValue) {
        String searchKeyword = newValue.trim().toLowerCase();
        if (palvelu.getNimi().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("nimi"))) {
            return true; // Match in Nimi
        } else if (String.valueOf(palvelu.getTyyppi()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("tyyppi"))) {
            return true; // Match in Tyyppi
        } else if (palvelu.getKuvaus().toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("kuvaus"))) {
            return true; // Match in Kuvaus
        } else if (String.valueOf(palvelu.getHinta()).toLowerCase().contains(searchKeyword) && (searchFilter.equals("kaikki") || searchFilter.equals("hinta"))) {
            return true; // Match in Hinta
        } else {
            return false; // No match found
        }
    }

    @Override
    String[] getSearchFilters() {
        return new String[]{"kaikki", "id", "nimi", "tyyppi", "kuvaus", "hinta"};
    }
}
