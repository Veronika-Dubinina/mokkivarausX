package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;

public class Lasku {
    // Attributes
    static int last_id = -1;
    private int lasku_id;
    private int varaus_id;
    private String asiakas_etunimi;
    private String asiakas_sukunimi;
    private String mokkinimi;
    private Timestamp varattu_pvm;
    private double summa;
    private double alv;
    private int maksettu;

    // Constructors
    public Lasku() {
        this(0, 0, 24, 0);
        last_id ++;
        this.lasku_id = last_id;
    }

    public Lasku(ResultSet res) {
        try {
            this.lasku_id = res.getInt("lasku_id");
            if (lasku_id > last_id)
                last_id = lasku_id;
            this.varaus_id = res.getInt("varaus_id");
            this.summa = res.getDouble("summa");
            this.alv = res.getDouble("alv");
            this.maksettu = res.getInt("maksettu");

            this.asiakas_etunimi = res.getString("asiakas_etunimi");
            this.asiakas_sukunimi = res.getString("asiakas_sukunimi");
            this.mokkinimi = res.getString("mokkinimi");
            this.varattu_pvm = res.getTimestamp("varattu_pvm");

        } catch (Exception e) {
            System.out.println("!!Exc. Lasku.Constr : " + e);
        }
    }

    public Lasku(int varaus_id, double summa, double alv, int maksettu) {
        this.varaus_id = varaus_id;
        this.summa = summa;
        this.alv = alv;
        this.maksettu = maksettu;
        last_id ++;
        this.lasku_id = last_id;
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

    public String getAsiakas_etunimi() {
        return asiakas_etunimi;
    }

    public void setAsiakas_etunimi(String asiakas_etunimi) {
        this.asiakas_etunimi = asiakas_etunimi;
    }

    public String getAsiakas_sukunimi() {
        return asiakas_sukunimi;
    }

    public void setAsiakas_sukunimi(String asiakas_sukunimi) {
        this.asiakas_sukunimi = asiakas_sukunimi;
    }

    public String getMokkinimi() {
        return mokkinimi;
    }

    public void setMokkinimi(String mokkinimi) {
        this.mokkinimi = mokkinimi;
    }

    public Timestamp getVarattu_pvm() {
        return varattu_pvm;
    }

    public void setVarattu_pvm(Timestamp varattu_pvm) {
        this.varattu_pvm = varattu_pvm;
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

    public int getMaksettu() {
        return maksettu;
    }

    public void setMaksettu(int maksettu) {
        this.maksettu = maksettu;
    }

    // Other methods as needed
    @Override
    public String toString() {
        return "Lasku: " + lasku_id + " " + varaus_id;
    }

    public HashMap<String, Object> getAttrMap() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("lasku_id", lasku_id);
        attrMap.put("varaus_id", varaus_id);
        attrMap.put("summa", summa);
        attrMap.put("alv", alv);
        attrMap.put("maksettu", maksettu);

        return attrMap;
    }
}
