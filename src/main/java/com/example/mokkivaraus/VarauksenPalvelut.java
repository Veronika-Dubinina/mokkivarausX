package com.example.mokkivaraus;

import java.sql.ResultSet;

class VarauksenPalvelut {
    // Attributes
    private int varaus_id;
    private int palvelu_id;
    private int lkm;
    private String nimi;
    private double hinta;
    private double alv;

    // Constructors
    public VarauksenPalvelut() {}
    public VarauksenPalvelut (ResultSet res) {
        try {
            this.varaus_id = res.getInt("varaus_id");
            this.palvelu_id = res.getInt("palvelu_id");
            this.lkm = res.getInt("lkm");

            this.nimi = res.getString("nimi");
            this.hinta = res.getDouble("hinta");
            this.alv = res.getDouble("alv");
        } catch (Exception e) {
            System.out.println("!!Exc. VarauksenPalvelut Constr: " + e);
        }
    }
    public VarauksenPalvelut(Palvelu palvelu) {
        this.palvelu_id = palvelu.getPalvelu_id();
        this.nimi = palvelu.getNimi();
        this.hinta = palvelu.getHinta();
        this.alv = palvelu.getAlv();
    }

    // Methods
    public int getVaraus_id() {
        return varaus_id;
    }

    public void setVaraus_id(int varaus_id) {
        this.varaus_id = varaus_id;
    }

    public int getPalvelu_id() {
        return palvelu_id;
    }

    public void setPalvelu_id(int palvelu_id) {
        this.palvelu_id = palvelu_id;
    }

    public int getLkm() {
        return lkm;
    }

    public void setLkm(int lkm) {
        this.lkm = lkm;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public double getHinta() {
        return hinta;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public double getAlv() {
        return alv;
    }

    public void setAlv(double alv) {
        this.alv = alv;
    }
}
