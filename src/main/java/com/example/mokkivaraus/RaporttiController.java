package com.example.mokkivaraus;

import javafx.fxml.Initializable;
import org.json.JSONArray;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RaporttiController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataBase db = new DataBase(){
            @Override
            public void toDoResultSet(ResultSet resultSet){
                try {
                    while (resultSet.next()) {
                        System.out.println(
                            resultSet.getString("mokkinimi") + " " +
                            resultSet.getInt("käyttöaste_pvm") + " " +
                            resultSet.getString("käyttöaste_pros") + " " +
                            resultSet.getInt("myyntisumma")
                        );
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        db.getData("CALL majoitusRaportti(128, '2024-03-20', '2024-03-30')");
    }
}
