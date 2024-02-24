package com.example.mokkivaraus;


import java.sql.*;
import java.util.ArrayList;


// Database
public class DataBase {
    // Attributes
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "vn";
    private final String LOGIN = "root";
    private final String PASS = "passWORD$";

    // Constructor
    // Methods
    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionUrl = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection dbConnection = DriverManager.getConnection(connectionUrl, LOGIN, PASS);
        return dbConnection;
    }

    public ArrayList<Mokki> getMokit(){
        String sql = "SELECT * FROM mokki ORDER BY id DESC";
        ArrayList<Mokki> mokit = new ArrayList<>();

        try {
            PreparedStatement ps = getDbConnection().prepareStatement(sql);
            ResultSet res = ps.executeQuery();

            while(res.next()) {
                Mokki mokki = new Mokki(
                        res.getInt("mokki_id"),
                        res.getInt("alue_id"),
                        res.getString("postinro"),
                        res.getString("mokkinimi"),
                        res.getString("katuosoite"),
                        res.getDouble("hinta"),
                        res.getString("kuvaus"),
                        res.getInt("henkilomaara"),
                        res.getString("varustelu")
                );
                mokit.add(mokki);
            }
        } catch (Exception ex) {
            System.out.println("!!Exception DataBase.getMokit:" + ex.getMessage());
        }

        return mokit;
    }
}
