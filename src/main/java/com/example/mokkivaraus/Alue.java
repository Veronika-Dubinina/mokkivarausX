package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.HashMap;

public class Alue {
    // Attributes
    private int alue_id;
    private String nimi;

    // Constructors
    // Default constructor
    public Alue() {}

    // Fill Alue from result set
    public Alue(ResultSet res) {
        try{
            this.alue_id = res.getInt("alue_id");
            this.nimi = res.getString("nimi");
        } catch (Exception e) {
            System.out.println("!!Exc. Alue.Constr : " + e);
        }
    }

    // Methods
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

    @Override
    public String toString() {
        return nimi;
    }
}
