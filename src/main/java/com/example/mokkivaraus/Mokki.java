package com.example.mokkivaraus;

public class Mokki {
    // Attributes
    private int mokki_id;
    private int alue_id;
    private String postinro;
    private String nimi;
    private String katuosoite;
    private double hinta;
    private String kuvaus;
    private int henkilomaara;
    private String varustelu;

    // Constructor
    public Mokki(int mokki_id, int alue_id, String postinro, String nimi, String katuosoite, double hinta, String kuvaus, int henkilomaara, String varustelu) {
        this.mokki_id = mokki_id;
        this.alue_id = alue_id;
        this.postinro = postinro;
        this.nimi = nimi;
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

    public int getAlue_id() {
        return alue_id;
    }

    public String getPostinro() {
        return postinro;
    }

    public String getNimi() {
        return nimi;
    }

    public String getKatuosoite() {
        return katuosoite;
    }

    public double getHinta() {
        return hinta;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public int getHenkilomaara() {
        return henkilomaara;
    }

    public String getVarustelu() {
        return varustelu;
    }
}
