package com.example.mokkivaraus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    // Attributes
    @FXML
    public TabPane contentPane;
    @FXML
    public Button alueBtn;
    @FXML
    private Tab varausTab;
    @FXML
    private Tab mokkiTab;
    @FXML
    private Tab palveluTab;
    @FXML
    private Tab alueTab;
    @FXML
    private Tab asiakasTab;
    @FXML
    private Tab raporttiTab;

    private Tab VarausTab;

    @FXML
    private Tab LaskuTab;

    // Добавить вкладку как атрибут здесь
    @FXML
    public void backToAlueView(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("alue-view.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Alue
            alueBtn.setText(SessionData.alue.toString());

            // Varaus tab
            FXMLLoader fxmlVarausLoader = new FXMLLoader();
            fxmlVarausLoader.setLocation(getClass().getResource("tab-view.fxml"));
            VarausController vc = new VarausController();
            fxmlVarausLoader.setController(vc);
            VBox varausView = fxmlVarausLoader.load();
            varausTab.setContent(varausView);

            // Mokki Tab
            FXMLLoader fxmlMokkiLoader = new FXMLLoader();
            fxmlMokkiLoader.setLocation(getClass().getResource("tab-view.fxml"));
            MokkiController mc = new MokkiController();
            fxmlMokkiLoader.setController(mc);
            VBox mokkiView = fxmlMokkiLoader.load();
            mokkiTab.setContent(mokkiView);

            // Palvelu Tab
            FXMLLoader fxmlPalveluLoader = new FXMLLoader();
            fxmlPalveluLoader.setLocation(getClass().getResource("tab-view.fxml"));
            PalveluController pc = new PalveluController();
            fxmlPalveluLoader.setController(pc);
            VBox palveluView = fxmlPalveluLoader.load();
            palveluTab.setContent(palveluView);

            // Asiakas tab
            FXMLLoader fxmlAsiakasLoader = new FXMLLoader();
            fxmlAsiakasLoader.setLocation(getClass().getResource("tab-view.fxml"));
            AsiakasController acс = new AsiakasController();
            fxmlAsiakasLoader.setController(acс);
            VBox asiakasView = fxmlAsiakasLoader.load();
            asiakasTab.setContent(asiakasView);

            // Raportti tab
            FXMLLoader fxmlRaporttiLoader = new FXMLLoader();
            fxmlRaporttiLoader.setLocation(getClass().getResource("raporttiTab-view.fxml"));
            VBox raporttiView = fxmlRaporttiLoader.load();
            raporttiTab.setContent(raporttiView);

            // Lasku tab
            FXMLLoader fxmlLaskuLoader = new FXMLLoader();
            fxmlLaskuLoader.setLocation(getClass().getResource("tab-view.fxml"));
            LaskuController lc = new LaskuController();
            fxmlLaskuLoader.setController(lc);
            VBox LaskuView = fxmlLaskuLoader.load();
            LaskuTab.setContent(LaskuView);
        } catch (IOException e) {
            System.out.println("load excp");
            throw new RuntimeException(e);
        }

    }

}
