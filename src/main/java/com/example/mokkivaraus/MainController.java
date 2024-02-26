package com.example.mokkivaraus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public AnchorPane topPane;

    @FXML
    public TabPane contentPane;

    @FXML
    private Tab mokkiTab;

    @FXML
    private Tab Test;

    // Добавить вкладку как атрибут здесь

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader fxmlMokkiLoader = new FXMLLoader();
            fxmlMokkiLoader.setLocation(getClass().getResource("tab-view.fxml"));
            MokkiController mc = new MokkiController();
            fxmlMokkiLoader.setController(mc);
            VBox view = fxmlMokkiLoader.load();
            mokkiTab.setContent(view);
            // Загрузить вкладку здесь
        } catch (IOException e) {
            System.out.println("load excp");
            throw new RuntimeException(e);
        }

    }
}
