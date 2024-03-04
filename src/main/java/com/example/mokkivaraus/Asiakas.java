package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.HashMap;

public class Asiakas {
    // Attributes
    private int asiakas_id;
    private String postinro;
    private String etunimi;
    private String sukunimi;
    private String lahiosoite;
    private String email;
    private String puhelinnro;

    // Constructors
    // Default constructor
    public Asiakas() {}
    // Fill Asiakas from result set
    public Asiakas(ResultSet res) {
        try{
            this.asiakas_id = res.getInt("asiakas_id");
            this.postinro = res.getString("postinro");
            this.etunimi = res.getString("etunimi");
            this.sukunimi = res.getString("sukunimi");
            this.lahiosoite = res.getString("lahiosoite");
            this.email = res.getString("email");
            this.puhelinnro = res.getString("puhelinnro");
        } catch (Exception e) {
            System.out.println("!!Exc. Asiakas.Constr : " + e);
        }
    }
    // Fill all attributes from user input
    public Asiakas(String postinro, String etunimi, String sukunimi, String lahiosoite, String email, String puhelinnro) {
        this.postinro = postinro;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.lahiosoite = lahiosoite;
        this.email = email;
        this.puhelinnro = puhelinnro;
    }

    // Methods
    public int getAsiakas_id() {
        return asiakas_id;
    }

    public void setAsiakas_id(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }

    public String getPostinro() {
        return postinro;
    }

    public void setPostinro(String postinro) {
        this.postinro = postinro;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getLahiosoite() {
        return lahiosoite;
    }

    public void setLahiosoite(String lahiosoite) {
        this.lahiosoite = lahiosoite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPuhelinnro() {
        return puhelinnro;
    }

    public void setPuhelinnro(String puhelinnro) {
        this.puhelinnro = puhelinnro;
    }

    @Override
    public String toString() {
        return etunimi + " " + sukunimi;
    }

    public HashMap<String, Object> getAttrMap() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("postinro", postinro);
        attrMap.put("etunimi", etunimi);
        attrMap.put("sukunimi", sukunimi);
        attrMap.put("lahiosoite", lahiosoite);
        attrMap.put("email", email);
        attrMap.put("puhelinnro", puhelinnro);

        return attrMap;
    }
}

