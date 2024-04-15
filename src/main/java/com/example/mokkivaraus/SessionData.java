package com.example.mokkivaraus;

import javafx.collections.ObservableList;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class SessionData {
    // Attributes
    static DataBase dataBase = new DataBase();
    static Alue alue;
    static String filter;
    static ObservableList<Alue> alueet;
    static ObservableList<Mokki> mokit;
    static ObservableList<Palvelu> palvelut;
    static ObservableList<Asiakas> asiakkaat;
    static ObservableList<Varaus> varaukset;
    static ObservableList<Lasku> laskut;

    // Methods
    public static Alue getAlue() {
        return alue;
    }

    public static void setAlue(Alue alue) {
        SessionData.alue = alue;
    }

    public static String getFilter() {
        return filter;
    }

    public static void setFilter() {
        SessionData.filter = "WHERE alue_id = " + alue.getAlue_id();
    }

    public static ObservableList<Alue> getAlueet() {
        return alueet;
    }

    public static void setAlueet() {
        SessionData.alueet = dataBase.getAllRows("alue", "alue_id", Alue.class);
    }

    public static ObservableList<Mokki> getMokit() {
        return mokit;
    }

    public static void setMokit() {
        SessionData.mokit = dataBase.getAllRows("mokki", "mokki_id", Mokki.class, filter);
    }

    public static ObservableList<Palvelu> getPalvelut() {
        return palvelut;
    }

    public static void setPalvelut() {
        SessionData.palvelut = dataBase.getAllRows("palvelu", "palvelu_id", Palvelu.class, filter);
    }

    public static ObservableList<Asiakas> getAsiakkaat() {
        return asiakkaat;
    }

    public static void setAsiakkaat() {
        SessionData.asiakkaat = dataBase.getAllRows("asiakas", "asiakas_id", Asiakas.class);
    }

    public static ObservableList<Varaus> getVaraukset() {
        return varaukset;
    }

    public static void setVaraukset() {
        SessionData.varaukset = dataBase.getAllRows("varauksen_tiedot", "varaus_id", Varaus.class, filter);
    }

    public static ObservableList<Lasku> getLaskut() {
        return laskut;
    }

    public static void setLaskut() {
        SessionData.laskut = dataBase.getAllRows("asiakkaiden_laskut", "lasku_id", Lasku.class);
    }

    public static void refreshLists() {
        setFilter();
        setAlueet();
        setMokit();
        setPalvelut();
        setAsiakkaat();
        setVaraukset();
        setLaskut();
    }

    public static <T> ObservableList<?> getList(Class<T> tableClass) {
        if (tableClass.getSimpleName().equals("Alue")) {
            return SessionData.alueet;
        }
        if (tableClass.getSimpleName().equals("Mokki")) {
            return SessionData.mokit;
        }
        if (tableClass.getSimpleName().equals("Palvelu")) {
            return SessionData.palvelut;
        }
        if (tableClass.getSimpleName().equals("Asiakas")) {
            return SessionData.asiakkaat;
        }
        if (tableClass.getSimpleName().equals("Varaus")) {
            return SessionData.varaukset;
        }
        if (tableClass.getSimpleName().equals("Lasku")) {
            return SessionData.laskut;
        }

        return null;
    }

    public static <T> void updateList(Class<T> tableClass) {
        if (tableClass.getSimpleName().equals("Alue")) {
            setAlueet();
        }
        if (tableClass.getSimpleName().equals("Mokki")) {
            setMokit();
        }
        if (tableClass.getSimpleName().equals("Palvelu")) {
            setPalvelut();
        }
        if (tableClass.getSimpleName().equals("Asiakas")) {
            setAsiakkaat();
        }
        if (tableClass.getSimpleName().equals("Varaus")) {
            setVaraukset();
        }
        if (tableClass.getSimpleName().equals("Lasku")) {
            setLaskut();
        }
    }
}
