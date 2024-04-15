package com.example.mokkivaraus;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class VarausDialogController implements Initializable {
    // Attributes
    private Varaus varaus = new Varaus();
    private ObservableList<VarauksenPalvelut> palvelut = FXCollections.observableArrayList();
    private DoubleProperty summ = new SimpleDoubleProperty(0.0);
    @FXML
    private GridPane tiedotGrP;
    @FXML
    private AutoCompleteTextField<Asiakas> asiakasACTF = new AutoCompleteTextField<>(SessionData.asiakkaat, true);
    @FXML
    private ComboBox<Mokki> mokkiCmB;
    @FXML
    private ListView<VarauksenPalvelut> palvelutListView;
    @FXML
    private VBox palvelutVBox;
    @FXML
    private ComboBox<String> vahvistusPvmHour;
    @FXML
    private ComboBox<String> vahvistusPvmMin;
    @FXML
    private DatePicker vahvistusPvmPicker;
    @FXML
    private ComboBox<String> varattuAlkupvmHour;
    @FXML
    private ComboBox<String> varattuAlkupvmMin;
    @FXML
    private DatePicker varattuAlkupvmPicker;
    @FXML
    private ComboBox<String> varattuLoppupvmHour;
    @FXML
    private ComboBox<String> varattuLoppupvmMin;
    @FXML
    private DatePicker varattuLoppupvmPicker;
    @FXML
    private ComboBox<String> varattuPvmHour;
    @FXML
    private ComboBox<String> varattuPvmMin;
    @FXML
    private DatePicker varattuPvmPicker;
    @FXML
    private Label varausNroLbl;
    @FXML
    private Label mokkiSummaLbl;
    @FXML
    private Label palvelutSummaLbl;
    @FXML
    private Label summaLbl;
    @FXML
    private Label alvLbl;
    @FXML
    private CheckBox paperiLaskuChB;

    // Constructor
    public VarausDialogController(Varaus varaus) {
        this.varaus = varaus;
        setVarausData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        summaLbl.setText(summ.getValue().toString());
        summ.addListener(((observableValue, oldValue, newValue) -> {
            summaLbl.setText(newValue.toString());
        }));

        tiedotGrP.add(asiakasACTF, 1, 0);
        mokkiCmB.getItems().addAll(SessionData.getMokit());
        initializeTimeComboBoxes();
        initPalvelut();
    }

    // Methods
    private void initializeTimeComboBoxes() {
        for (int i = 0; i < 24; i++) {
            String formattedHour = String.format("%02d", i);
            varattuPvmHour.getItems().add(formattedHour);
            vahvistusPvmHour.getItems().add(formattedHour);
            varattuAlkupvmHour.getItems().add(formattedHour);
            varattuLoppupvmHour.getItems().add(formattedHour);
        }
        for (int i = 0; i < 60; i++) {
            String formattedMinute = String.format("%02d", i);
            varattuPvmMin.getItems().add(formattedMinute);
            vahvistusPvmMin.getItems().add(formattedMinute);
            varattuAlkupvmMin.getItems().add(formattedMinute);
            varattuLoppupvmMin.getItems().add(formattedMinute);
        }
    }

    private void initPalvelut() {
        // Add listener to list of palvelu
        palvelut.addListener(new ListChangeListener<VarauksenPalvelut>() {
            @Override
            public void onChanged(Change<? extends VarauksenPalvelut> change) {
                palvelutListView.getItems().clear();
                palvelutListView.getItems().addAll(palvelut);
            }
        });

        // Add palvelut
        SessionData.getPalvelut().forEach(palvelu -> {
            Spinner<Integer> lkm = new Spinner<>(1, 50, 1);
            lkm.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
                summ.set(summ.get() + palvelu.getHinta() * (newValue - oldValue));
            }));
            lkm.setEditable(true);
            lkm.setDisable(true);

            CheckBox palveluBox = new CheckBox(palvelu.toString());
            palveluBox.setOnAction(actionEvent -> {
                if (palveluBox.isSelected()) {
                    palvelut.add(new VarauksenPalvelut(palvelu));
                    lkm.setDisable(false);
                    summ.set(summ.get() + palvelu.getHinta() * lkm.getValue());
                } else {
                    palvelut.remove(palvelu);
                    lkm.setDisable(true);
                    summ.set(summ.get() - (palvelu.getHinta() * lkm.getValue()));
                }
            });

            HBox container = new HBox(palveluBox, lkm);
            palvelutVBox.getChildren().add(container);
        });
    }

    private void setVarausData() {
        // Asiakas
        SessionData.getAsiakkaat().forEach(asiakas -> {
            if (asiakas.getAsiakas_id() == varaus.getAsiakas_id()) {
                asiakasACTF.setLastSelectedItem(asiakas);
                return;
            }
        });
        // Mokki
        SessionData.getMokit().forEach(mokki -> {
            if (mokki.getMokki_id() == varaus.getMokki_mokki_id()) {
                mokkiCmB.setValue(mokki);
                return;
            }
        });
        // Pvm
        varattuPvmPicker.setValue(varaus.getVarattu_pvm().toLocalDateTime().toLocalDate());
        varattuPvmHour.setValue(String.format("%02d", varaus.getVarattu_pvm().toLocalDateTime().getHour()));
        varattuPvmMin.setValue(String.format("%02d", varaus.getVarattu_pvm().toLocalDateTime().getMinute()));

        vahvistusPvmPicker.setValue(varaus.getVahvistus_pvm().toLocalDateTime().toLocalDate());
        vahvistusPvmHour.setValue(String.format("%02d", varaus.getVahvistus_pvm().toLocalDateTime().getHour()));
        vahvistusPvmMin.setValue(String.format("%02d", varaus.getVahvistus_pvm().toLocalDateTime().getMinute()));

        varattuAlkupvmPicker.setValue(varaus.getVarattu_alkupvm().toLocalDateTime().toLocalDate());
        varattuAlkupvmHour.setValue(String.format("%02d", varaus.getVarattu_alkupvm().toLocalDateTime().getHour()));
        varattuAlkupvmMin.setValue(String.format("%02d", varaus.getVarattu_alkupvm().toLocalDateTime().getMinute()));

        varattuLoppupvmPicker.setValue(varaus.getVarattu_loppupvm().toLocalDateTime().toLocalDate());
        varattuLoppupvmHour.setValue(String.format("%02d", varaus.getVarattu_loppupvm().toLocalDateTime().getHour()));
        varattuLoppupvmMin.setValue(String.format("%02d", varaus.getVarattu_loppupvm().toLocalDateTime().getMinute()));
    }

    private void setPalvelut() {
        String filter = "WHERE varaus_id = " + varaus.getVaraus_id();
        palvelut = SessionData.dataBase.getAllRows("varauksen_palvelutT", "varauksen_id", VarauksenPalvelut.class, filter);


    }
}
