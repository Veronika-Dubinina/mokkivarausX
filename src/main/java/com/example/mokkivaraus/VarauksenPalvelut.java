package com.example.mokkivaraus;

import java.sql.ResultSet;
import java.util.HashMap;

class VarauksenPalvelut {
    // Attributes
    private int varaus_id;
    private int palvelu_id;
    private int lkm;

    // Constructors
    public VarauksenPalvelut() {
        this(0, 0, 1);
    }
    public VarauksenPalvelut (ResultSet res) {
        try {
            this.varaus_id = res.getInt("varaus_id");
            this.palvelu_id = res.getInt("palvelu_id");
            this.lkm = res.getInt("lkm");
        } catch (Exception e) {
            System.out.println("!!Exc. VarauksenPalvelut Constr: " + e);
        }
    }

    public VarauksenPalvelut(int varaus_id, int palvelu_id, int lkm) {
        this.varaus_id = varaus_id;
        this.palvelu_id = palvelu_id;
        this.lkm = lkm;
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

    @Override
    public String toString() {
        return "Palvelu: " + palvelu_id;
    }

    public HashMap<String, Object> getAttrMap() {
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("varaus_id", varaus_id);
        attrMap.put("palvelu_id", palvelu_id);
        attrMap.put("lkm", lkm);

        return attrMap;
    }
}
