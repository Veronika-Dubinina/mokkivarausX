package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.HashMap;

public class Palvelu {
    // Attributes
    private int palvelu_id;
    private int alue_id;
    private String nimi;
    private int tyyppi;
    private String kuvaus;
    private Double hinta;
    private Double alv;

    // Constructors
    // Default
    public Palvelu() {
    }
    // Fill from result set
    public Palvelu(ResultSet res) {
        try {
            this.palvelu_id = res.getInt("palvelu_id");
            this.alue_id = res.getInt("alue_id");
            this.nimi = res.getString("nimi");
            this.tyyppi = res.getInt("tyyppi");
            this.kuvaus = res.getString("kuvaus");
            this.hinta = res.getDouble("hinta");
            this.alv = res.getDouble("alv");
        } catch (Exception e) {
            System.out.println("!!Exc. PalveluConstr : " + e);
        }
    }
    // Fill all attributes from user input
    public Palvelu(int alue_id, String nimi, int tyyppi, String kuvaus, Double hinta, Double alv) {
        this.alue_id = alue_id;
        this.nimi = nimi;
        this.tyyppi = tyyppi;
        this.kuvaus = kuvaus;
        this.hinta = hinta;
        this.alv = alv;
    }

    // Methods
    public int getPalvelu_id() {
        return palvelu_id;
    }

    public void setPalvelu_id(int palvelu_id) {
        this.palvelu_id = palvelu_id;
    }

    public int getAlue_id() {
        return alue_id;
    }

    public void setAlue_id(int alue_id) {
        this.alue_id = alue_id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(int tyyppi) {
        this.tyyppi = tyyppi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public Double getHinta() {
        return hinta;
    }

    public void setHinta(Double hinta) {
        this.hinta = hinta;
    }

    public Double getAlv() {
        return alv;
    }

    public void setAlv(Double alv) {
        this.alv = alv;
    }

    @Override
    public String toString() {
        return "Palvelu: " + palvelu_id + " " + nimi;
    }

    public HashMap<String, Object> getAttrMap() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("palvelu_id", palvelu_id);
        attrMap.put("alue_id", alue_id);
        attrMap.put("nimi", nimi);
        attrMap.put("tyyppi", tyyppi);
        attrMap.put("kuvaus", kuvaus);
        attrMap.put("hinta", hinta);
        attrMap.put("alv", alv);

        return attrMap;
    }
}
