package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.HashMap;

public class Posti {
    // Attributes
    private String postinro;
    private String toimipaikka;

    // Constructors
    // Default constructor
    public Posti() {}

    // Fill Posti from result set
    public Posti(ResultSet res) {
        try {
            this.postinro = res.getString("postinro");
            this.toimipaikka = res.getString("toimipaikka");
        } catch (Exception e) {
            System.out.println("!!Exc. Posti.Constr : " + e);
        }
    }

    // Methods
    public String getPostinro() {
        return postinro;
    }

    public void setPostinro(String postinro) {
        this.postinro = postinro;
    }

    public String getToimipaikka() {
        return toimipaikka;
    }

    public void setToimipaikka(String toimipaikka) {
        this.toimipaikka = toimipaikka;
    }

    @Override
    public String toString() {
        return postinro + " - " + toimipaikka;
    }
}
