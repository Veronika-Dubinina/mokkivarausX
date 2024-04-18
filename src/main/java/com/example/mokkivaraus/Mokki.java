package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Mokki {
    // Attributes
    private int mokki_id;
    private int alue_id;
    private String postinro;
    private String mokkinimi;
    private String katuosoite;
    private double hinta;
    private String kuvaus;
    private int henkilomaara;
    private String varustelu;

    // Constructors
    // Default constructor
    public Mokki() {
        this(0, "", "", "", 0.0, "", 0, "");
    }
    // Fill from result set
    public Mokki(ResultSet res) {
        try {
            this.mokki_id = res.getInt("mokki_id");
            this.alue_id = res.getInt("alue_id");
            this.postinro = res.getString("postinro");
            this.mokkinimi = res.getString("mokkinimi");
            this.katuosoite = res.getString("katuosoite");
            this.hinta = res.getDouble("hinta");
            this.kuvaus = res.getString("kuvaus");
            this.henkilomaara = res.getInt("henkilomaara");
            this.varustelu = res.getString("varustelu");
        } catch (Exception e) {
            System.out.println("!!Exc. MokkiConstr : " + e);
        }
    }
    // Fill all attributes from user input
    public Mokki(int alue_id, String postinro, String mokkinimi, String katuosoite, double hinta, String kuvaus, int henkilomaara, String varustelu) {
        this.alue_id = alue_id;
        this.postinro = postinro;
        this.mokkinimi = mokkinimi;
        this.katuosoite = katuosoite;
        this.hinta = hinta;
        this.kuvaus = kuvaus;
        this.henkilomaara = henkilomaara;
        this.varustelu = varustelu;
    }

    // Methods
    public int getMokki_id() {
        return mokki_id;
    }

    public void setMokki_id(int mokki_id) {
        this.mokki_id = mokki_id;
    }

    public int getAlue_id() {
        return alue_id;
    }

    public void setAlue_id(int alue_id) {
        this.alue_id = alue_id;
    }

    public String getPostinro() {
        return postinro;
    }

    public void setPostinro(String postinro) {
        this.postinro = postinro;
    }

    public String getMokkinimi() {
        return mokkinimi;
    }

    public void setMokkinimi(String mokkinimi) {
        this.mokkinimi = mokkinimi;
    }

    public String getKatuosoite() {
        return katuosoite;
    }

    public void setKatuosoite(String katuosoite) {
        this.katuosoite = katuosoite;
    }

    public double getHinta() {
        return hinta;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public int getHenkilomaara() {
        return henkilomaara;
    }

    public void setHenkilomaara(int henkilomaara) {
        this.henkilomaara = henkilomaara;
    }

    public String getVarustelu() {
        return varustelu;
    }

    public void setVarustelu(String varustelu) {
        this.varustelu = varustelu;
    }

    @Override
    public String toString() {
        return "Mokki: " + mokki_id + " " + mokkinimi;
    }

    public HashMap<String, Object> getAttrMap() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("alue_id", alue_id);
        attrMap.put("postinro", postinro);
        attrMap.put("mokkinimi", mokkinimi);
        attrMap.put("katuosoite", katuosoite);
        attrMap.put("hinta", hinta);
        attrMap.put("kuvaus", kuvaus);
        attrMap.put("henkilomaara", henkilomaara);
        attrMap.put("varustelu", varustelu);

        return attrMap;
    }
}
