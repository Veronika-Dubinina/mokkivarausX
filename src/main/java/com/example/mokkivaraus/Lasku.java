package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.HashMap;

public class Lasku {
    // Attributes
    private int lasku_id;
    private int varaus_id;
    private double summa;
    private double alv;
    private boolean maksettu;

    // Constructors
    public Lasku() {}

    public Lasku(ResultSet res) {
        try {
            this.lasku_id = res.getInt("lasku_id");
            this.varaus_id = res.getInt("varaus_id");
            this.summa = res.getDouble("summa");
            this.alv = res.getDouble("alv");
            this.maksettu = res.getBoolean("maksettu");
        } catch (Exception e) {
            System.out.println("!!Exc. Lasku.Constr : " + e);
        }
    }

    public Lasku(int varaus_id, double summa, double alv, boolean maksettu) {
        this.varaus_id = varaus_id;
        this.summa = summa;
        this.alv = alv;
        this.maksettu = maksettu;
    }

    // Getters and Setters
    public int getLasku_id() {
        return lasku_id;
    }

    public void setLasku_id(int lasku_id) {
        this.lasku_id = lasku_id;
    }

    public int getVaraus_id() {
        return varaus_id;
    }

    public void setVaraus_id(int varaus_id) {
        this.varaus_id = varaus_id;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public double getAlv() {
        return alv;
    }

    public void setAlv(double alv) {
        this.alv = alv;
    }

    public boolean isMaksettu() {
        return maksettu;
    }

    public void setMaksettu(boolean maksettu) {
        this.maksettu = maksettu;
    }

    // Other methods as needed
}
