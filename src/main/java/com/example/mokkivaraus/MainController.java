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
    private Tab alueTab;

    @FXML
    private Tab AsiakasTab;

    // Добавить вкладку как атрибут здесь
    @FXML
    private Tab Test;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Mokki Tab
            FXMLLoader fxmlMokkiLoader = new FXMLLoader();
            fxmlMokkiLoader.setLocation(getClass().getResource("tab-view.fxml"));
            MokkiController mc = new MokkiController();
            fxmlMokkiLoader.setController(mc);
            VBox mokkiView = fxmlMokkiLoader.load();
            mokkiTab.setContent(mokkiView);

            // Alue Tab
            FXMLLoader fxmlAlueLoader = new FXMLLoader();
            fxmlAlueLoader.setLocation(getClass().getResource("tab-view.fxml"));
            AlueController ac = new AlueController();
            fxmlAlueLoader.setController(ac);
            VBox alueView = fxmlAlueLoader.load();
            alueTab.setContent(alueView);

            // Asiakas tab

            FXMLLoader fxmlAsiakasLoader = new FXMLLoader();
            fxmlAsiakasLoader.setLocation(getClass().getResource("tab-view.fxml"));
            AsiakasController acс = new AsiakasController();
            fxmlAsiakasLoader.setController(acс);
            VBox AsiakasView = fxmlAsiakasLoader.load();
            AsiakasTab.setContent(AsiakasView);

            // Загрузить вкладку здесь

        } catch (IOException e) {
            System.out.println("load excp");
            throw new RuntimeException(e);
        }

    }
}
