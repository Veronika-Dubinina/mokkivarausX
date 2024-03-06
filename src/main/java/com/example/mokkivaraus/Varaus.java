package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.HashMap;

public class Varaus {
    // Attributes
    private int varaus_id;
    private int asiakas_id;
    private int mokki_mokki_id;
    private String varattu_pvm;
    private String vahvistus_pvm;
    private String varattu_alkupvm;
    private String varattu_loppupvm;

    // Constructors
    public Varaus() {}

    public Varaus(ResultSet res) {
        try {
            this.varaus_id = res.getInt("varaus_id");
            this.asiakas_id = res.getInt("asiakas_id");
            this.mokki_mokki_id = res.getInt("mokki_mokki_id");
            this.varattu_pvm = res.getString("varattu_pvm");
            this.vahvistus_pvm = res.getString("vahvistus_pvm");
            this.varattu_alkupvm = res.getString("varattu_alkupvm");
            this.varattu_loppupvm = res.getString("varattu_loppupvm");
        } catch (Exception e) {
            System.out.println("!!Exc. Varaus.Constr : " + e);
        }
    }

    public Varaus(int asiakas_id, int mokki_mokki_id, String varattu_pvm, String vahvistus_pvm, String varattu_alkupvm, String varattu_loppupvm) {
        this.asiakas_id = asiakas_id;
        this.mokki_mokki_id = mokki_mokki_id;
        this.varattu_pvm = varattu_pvm;
        this.vahvistus_pvm = vahvistus_pvm;
        this.varattu_alkupvm = varattu_alkupvm;
        this.varattu_loppupvm = varattu_loppupvm;
    }

    // Getters and setters
    public int getVaraus_id() {
        return varaus_id;
    }

    public void setVaraus_id(int varaus_id) {
        this.varaus_id = varaus_id;
    }

    public int getAsiakas_id() {
        return asiakas_id;
    }

    public void setAsiakas_id(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }

    public int getMokki_mokki_id() {
        return mokki_mokki_id;
    }

    public void setMokki_mokki_id(int mokki_mokki_id) {
        this.mokki_mokki_id = mokki_mokki_id;
    }

    public String getVarattu_pvm() {
        return varattu_pvm;
    }

    public void setVarattu_pvm(String varattu_pvm) {
        this.varattu_pvm = varattu_pvm;
    }

    public String getVahvistus_pvm() {
        return vahvistus_pvm;
    }

    public void setVahvistus_pvm(String vahvistus_pvm) {
        this.vahvistus_pvm = vahvistus_pvm;
    }

    public String getVarattu_alkupvm() {
        return varattu_alkupvm;
    }

    public void setVarattu_alkupvm(String varattu_alkupvm) {
        this.varattu_alkupvm = varattu_alkupvm;
    }

    public String getVarattu_loppupvm() {
        return varattu_loppupvm;
    }

    public void setVarattu_loppupvm(String varattu_loppupvm) {
        this.varattu_loppupvm = varattu_loppupvm;
    }

    // Additional methods
    @Override
    public String toString() {
        return "Varaus ID: " + varaus_id;
    }

    public HashMap<String, Object> getAttrMap() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("asiakas_id", asiakas_id);
        attrMap.put("mokki_mokki_id", mokki_mokki_id);
        attrMap.put("varattu_pvm", varattu_pvm);
        attrMap.put("vahvistus_pvm", vahvistus_pvm);
        attrMap.put("varattu_alkupvm", varattu_alkupvm);
        attrMap.put("varattu_loppupvm", varattu_loppupvm);
        return attrMap;
    }
}
