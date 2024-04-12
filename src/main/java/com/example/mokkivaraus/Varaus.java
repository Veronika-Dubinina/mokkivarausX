package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Varaus {
    class SQLDateTime extends Timestamp {
        public SQLDateTime(long time) {
            super(time);
        }
        public SQLDateTime(Timestamp tst) {
            this(tst.getTime());
        }

        @Override
        public String toString() {
            // formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            return this.toLocalDateTime().format(formatter);
        }
    }
    // Attributes
    private int varaus_id;
    private int asiakas_id;
    private int mokki_mokki_id;
    private String asiakasnimi;
    private String mokkinimi;
    private SQLDateTime varattu_pvm;
    private SQLDateTime vahvistus_pvm;
    private SQLDateTime varattu_alkupvm;
    private SQLDateTime varattu_loppupvm;

    // Constructors
    public Varaus() {}

    public Varaus(ResultSet res) {
        try {
            this.varaus_id = res.getInt("varaus_id");
            this.asiakas_id = res.getInt("asiakas_id");
            this.mokki_mokki_id = res.getInt("mokki_mokki_id");
            this.asiakasnimi = res.getString("asiakas");
            this.mokkinimi = res.getString("mokkinimi");
            this.varattu_pvm = new SQLDateTime(res.getTimestamp("varattu_pvm"));
            this.vahvistus_pvm = new SQLDateTime(res.getTimestamp("vahvistus_pvm"));
            this.varattu_alkupvm = new SQLDateTime(res.getTimestamp("varattu_alkupvm"));
            this.varattu_loppupvm = new SQLDateTime(res.getTimestamp("varattu_loppupvm"));
        } catch (Exception e) {
            System.out.println("!!Exc. Varaus.Constr : " + e);
        }
    }

    public Varaus(int asiakas_id, int mokki_mokki_id, Timestamp varattu_pvm, Timestamp vahvistus_pvm, Timestamp varattu_alkupvm, Timestamp varattu_loppupvm) {
        this.asiakas_id = asiakas_id;
        this.mokki_mokki_id = mokki_mokki_id;
        this.varattu_pvm = new SQLDateTime(varattu_pvm);
        this.vahvistus_pvm = new SQLDateTime(vahvistus_pvm);
        this.varattu_alkupvm = new SQLDateTime(varattu_alkupvm);
        this.varattu_loppupvm = new SQLDateTime(varattu_loppupvm);
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

    public String getAsiakasnimi() {
        return asiakasnimi;
    }

    public void setAsiakasnimi(String asiakasnimi) {
        this.asiakasnimi = asiakasnimi;
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
        this.varattu_pvm = new SQLDateTime(varattu_pvm);
    }

    public Timestamp getVahvistus_pvm() {
        return vahvistus_pvm;
    }

    public void setVahvistus_pvm(Timestamp vahvistus_pvm) {
        this.vahvistus_pvm = new SQLDateTime(vahvistus_pvm);
    }

    public Timestamp getVarattu_alkupvm() {
        return varattu_alkupvm;
    }

    public void setVarattu_alkupvm(Timestamp varattu_alkupvm) {
        this.varattu_alkupvm = new SQLDateTime(varattu_alkupvm);
    }

    public Timestamp getVarattu_loppupvm() {
        return varattu_loppupvm;
    }

    public void setVarattu_loppupvm(Timestamp varattu_loppupvm) {
        this.varattu_loppupvm = new SQLDateTime(varattu_loppupvm);
    }

    // Additional methods
    @Override
    public String toString() {
        return "Varaus ID: " + varaus_id;
    }

    public HashMap<String, Object> getAttrMap() {
        // Date formatter
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
