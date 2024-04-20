package com.example.mokkivaraus;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class VarausDialogController extends DialogController {
    class DisabledRange {

        private final LocalDate initialDate;
        private final LocalDate endDate;

        public DisabledRange(LocalDate initialDate, LocalDate endDate){
            this.initialDate=initialDate;
            this.endDate = endDate;
        }

        public LocalDate getInitialDate() { return initialDate; }
        public LocalDate getEndDate() { return endDate; }

    }

    // Attributes
    private Varaus varaus = new Varaus();
    private IntegerProperty numOfDays = new SimpleIntegerProperty(0);
    private final ObservableList<DisabledRange> rangesToDisable = FXCollections.observableArrayList();
    private ObservableList<VarauksenPalvelut> varauksenPalvelut = FXCollections.observableArrayList();
    private Lasku lasku = new Lasku();
    private HashMap<Integer, Node[]> palveluNodes = new HashMap<>();
    private DoubleProperty mokkiSumma = new SimpleDoubleProperty(0.0);
    private DoubleProperty palvelutSumma = new SimpleDoubleProperty(0.0);
    private DoubleProperty summa = new SimpleDoubleProperty(0.0);
    @FXML
    private Tab palveluTab;
    @FXML
    private Tab laskuTab;
    @FXML
    private GridPane tiedotGrP;
    @FXML
    private AutoCompleteTextField<Asiakas> asiakasACTF = new AutoCompleteTextField<>(SessionData.asiakkaat, true){
        @Override
        public void onCreateLabelClicked() {
            // Create popover window
            PopOver popover = new PopOver();
            VBox content = new VBox();
            content.setSpacing(10);
            content.setPadding(new Insets(10));

            // Asiakas properties
            AtomicReference<String> etunimi = new AtomicReference<>("");
            AtomicReference<String> sukunimi = new AtomicReference<>("");
            AtomicReference<String> puhnro = new AtomicReference<>("");
            AtomicReference<String> email = new AtomicReference<>("");

            // Popover nodes
            // Etunimi text field
            TextField etunimiTF = new TextField();
            etunimiTF.setPromptText("Etunimi");
            etunimiTF.getStyleClass().add("wrong");
            etunimiTF.textProperty().addListener((object, oldValue, newValue) -> {
                String enimi = newValue.trim();
                if (!enimi.isEmpty() && enimi.length() <= 20 && enimi.matches("[a-zA-Z- ]+")) {
                    etunimi.set(enimi);
                    etunimiTF.getStyleClass().add("right");
                } else {
                    etunimi.set("");
                    etunimiTF.getStyleClass().remove("right");
                }
            });
            // Sukunimi text field
            TextField sukunimiTF = new TextField();
            sukunimiTF.setPromptText("Sukunimi");
            sukunimiTF.getStyleClass().add("wrong");
            sukunimiTF.textProperty().addListener((object, oldValue, newValue) -> {
                String snimi = newValue.trim();
                if (!snimi.isEmpty() && snimi.length() <= 40 && snimi.matches("[a-zA-Z- ]+")) {
                    sukunimi.set(snimi);
                    sukunimiTF.getStyleClass().add("right");
                } else {
                    sukunimi.set("");
                    sukunimiTF.getStyleClass().remove("right");
                }
            });
            // Puhelinnumero text field
            TextField puhelinTF = new TextField();
            puhelinTF.setPromptText("Puhelinnro");
            puhelinTF.getStyleClass().add("wrong");
            puhelinTF.textProperty().addListener((object, oldValue, newValue) -> {
                String nro = newValue.trim();
                if (!nro.isEmpty() && nro.length() <= 15 && nro.matches("\\+?[0-9]+")) {
                    puhnro.set(nro);
                    puhelinTF.getStyleClass().add("right");
                } else {
                    puhnro.set("");
                    puhelinTF.getStyleClass().remove("right");

                }
            });
            // Email text field
            TextField emailTF = new TextField();
            emailTF.setPromptText("Email (ei pakko)");
            emailTF.textProperty().addListener((object, oldValue, newValue) -> {
                String eposti = newValue.trim();
                if (eposti.length() <= 50 && eposti.matches("[^&=+<>,']+@[a-zA-z.]+")) {
                    email.set(eposti);
                } else {
                    email.set("");
                }
            });


            // Close button
            Button close = new Button("Peruuta");
            close.setOnAction(event -> {
                // Hide popover
                popover.hide();
            });
            // Add button
            Button add = new Button("Lisää");
            add.setOnAction(event -> {
                if (etunimi.get().isEmpty() || sukunimi.get().isEmpty() || puhnro.get().isEmpty()) {
                    return;
                } else {
                    Asiakas asiakas = new Asiakas( "", etunimi.get(), sukunimi.get(), "", email.get(), puhnro.get());
                    SessionData.dataBase.addRow("asiakas", asiakas.getAttrMap());
                    String filter = "WHERE etunimi='" + etunimi.get() + "' AND sukunimi='" + sukunimi.get() + "' AND puhelinnro='" + puhnro.get() + "'";
                    asiakas = SessionData.dataBase.getRow("asiakas", filter, Asiakas.class);
                    setLastSelectedItem(asiakas);
                }

                popover.hide();
            });
            // Buttons container
            HBox buttons = new HBox(close, add);
            buttons.setSpacing(10);

            // Popover root node
            content.getChildren().addAll(
                    etunimiTF,
                    sukunimiTF,
                    puhelinTF,
                    emailTF,
                    buttons
            );

            // Set popover content and show it
            popover.setContentNode(content);
            popover.show(this);
        }

        @Override
        public boolean getSearchConditions(Asiakas object, String newValue) {
            newValue = newValue.trim().toLowerCase();
            if ((object.getEtunimi().toLowerCase() + " " + object.getSukunimi().toLowerCase()).contains(newValue))
                return true;
            else if (object.getEtunimi().toLowerCase().contains(newValue))
                return true;
            else if (object.getSukunimi().toLowerCase().contains(newValue))
                return true;
            else if (object.getPuhelinnro().toLowerCase().contains(newValue))
                return true;
            else
                return false;
        }
    };
    private FileChooser fileChooser = new FileChooser();
    private static final String BLANK_PDF_PATH = "src/main/pdf/table_data.pdf"; // Путь к файлу-болванке
    @FXML
    private ComboBox<Mokki> mokkiCmBox;
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
    @FXML
    private VBox paperiLaskuBox;
    @FXML
    private TextField katuosoiteTF;
    private AutoCompleteTextField<Posti> postinroACTF = new AutoCompleteTextField<>(SessionData.getPostit(), true) {
        @Override
        public void onCreateLabelClicked() {
            // Create popover window
            PopOver popover = new PopOver();
            VBox content = new VBox();
            content.setSpacing(10);
            content.setPadding(new Insets(10));

            // Posti attributes
            AtomicReference<String> postinro = new AtomicReference<>("");
            AtomicReference<String> toimipaikka = new AtomicReference<>("");

            // Popover nodes
            // Postinumero text field
            TextField postinroTF = new TextField();
            postinroTF.setPromptText("Postinro");
            postinroTF.getStyleClass().add("wrong");
            postinroTF.textProperty().addListener((object, oldValue, newValue) -> {
                String nro = newValue.trim();
                if (nro.length() == 5 && nro.matches("[0-9]+")) {
                    postinro.set(nro);
                    postinroTF.getStyleClass().add("right");
                } else {
                    postinro.set("");
                    postinroTF.getStyleClass().remove("right");
                }
            });
            // Toimipaikka text field
            TextField toimipaikkaTF = new TextField();
            toimipaikkaTF.setPromptText("Toimipaikka");
            toimipaikkaTF.getStyleClass().add("wrong");
            toimipaikkaTF.textProperty().addListener((object, oldValue, newValue) -> {
                String tp = newValue.trim();
                if (!tp.isEmpty() && tp.length() <= 45) {
                    toimipaikka.set(tp);
                    toimipaikkaTF.getStyleClass().add("right");

                } else {
                    toimipaikka.set("");
                    toimipaikkaTF.getStyleClass().remove("right");
                }
            });
            // Close button
            Button close = new Button("Peruuta");
            close.setOnAction(event -> {
                // Hide popover
                popover.hide();
            });
            // Add button
            Button add = new Button("Lisää");
            add.setOnAction(event -> {
                if (postinro.get().isEmpty() || toimipaikka.get().isEmpty()) {
                    return;
                } else {
                    Posti posti = new Posti(postinro.get(), toimipaikka.get());
                    SessionData.dataBase.addRow("posti", posti.getAttrMap());
                    setLastSelectedItem(posti);
                }

                popover.hide();
            });
            // Buttons container
            HBox buttons = new HBox(close, add);
            buttons.setSpacing(10);

            // Popover root node
            content.getChildren().addAll(
                    postinroTF,
                    toimipaikkaTF,
                    buttons
            );

            // Set popover content and show it
            popover.setContentNode(content);
            popover.show(this);
        }
    };

    // Constructors
    public VarausDialogController(String tableName, String identifierKey) {
       super(tableName, identifierKey);
    }

    // Methods
    @Override
    void setObject(Object object) {
        this.editMode = true;
        this.varaus = (Varaus) object;
        this.identifierValue = varaus.getVaraus_id();
    }

    @Override
    boolean checkData() {
        if (asiakasACTF.getLastSelectedItem() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse asiakas";
            return false;
        } else if (mokkiCmBox.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse mökki";
            return false;
        } else if (varattuPvmPicker.getValue() == null || varattuPvmHour.getValue() == null || varattuPvmMin.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu päivämäärä ja aika";
            return false;
        } else if (vahvistusPvmPicker.getValue() == null || vahvistusPvmHour.getValue() == null || vahvistusPvmMin.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse vahvistus päivämäärä ja aika";
            return false;
        } else if (varattuAlkupvmPicker.getValue() == null || varattuAlkupvmHour.getValue() == null || varattuAlkupvmMin.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu alkupäivämäärä ja aika";
            return false;
        } else if (varattuLoppupvmPicker.getValue() == null || varattuLoppupvmHour.getValue() == null || varattuLoppupvmMin.getValue() == null) {
            alertTitle = "Puuttuvia tietoja";
            alertMessage = "Valitse varattu loppupäivämäärä ja aika";
            return false;
        } else {
            alertTitle = "Menestys";
            alertMessage = "Tiedot tarkastettu ja hyväksytty.";
            setData();
            return true;
        }
    }

    @Override
    void setDialogContent() {
       initVaraus();
       initPalvelut();
       initLasku();
    }

    @Override
    void setEditContent() {
        setVarausData();
        setPalvelut();
        setLasku();
        palveluTab.setDisable(false);
        laskuTab.setDisable(false);
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        return varaus.getAttrMap();
    }

    @Override
    void addBtnClicked(ActionEvent event) {
        if (addData()) {
            showAlert(Alert.AlertType.INFORMATION);
            // Varaus id
            SessionData.setVaraukset();
            SessionData.getVaraukset().forEach(v -> {
                if (v.equals(varaus)) {
                    varaus = v;
                    return;
                }
            });
            addLasku(); // Lasku
            EditMode(); // Edit mode

        } else
            showAlert(Alert.AlertType.WARNING);
    }

    @Override
    void updateBtnClicked(ActionEvent event) {
        if (updateData()) {
            showAlert(Alert.AlertType.INFORMATION);
            // palvelut
            updatePalvelut();
            // lasku
            updateLasku();

            closeStage();
        }
    }

    @FXML
    void sendBtnClicked(ActionEvent event) {
        String address = "";
        if (paperiLaskuChB.isSelected()) {
            address = katuosoiteTF.getText() + "\n" + postinroACTF.getText();
        }
        // File name
        String fileName = "Lasku" + lasku.getLasku_id();
        // Save file dialog
        Window stage = paperiLaskuBox.getScene().getWindow();
        fileChooser.setTitle("Save dialog");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("pdf file", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveLaskuToFile(file);
        }
    }

    /** Fill lasku-pdf file with data */
    private void saveLaskuToFile(File file) {
        try {
            PdfDocument pdf = new PdfDocument(new PdfReader(BLANK_PDF_PATH), new PdfWriter(file));
            Document document = new Document(pdf);

            // Set margins
            document.setMargins(100, 50, 50, 100);

            // Format the data from the Lasku object
            String content = String.format("ID Lasku: %d\nID Varaus: %d\nAsiakas: %s \nMokki: %s\nVarauksen data: %s\nSumma: %.2f €\nALV: %.2f %%\nMaksettu: %s",
                    lasku.getLasku_id(), lasku.getVaraus_id(), asiakasACTF.getLastSelectedItem(),
                    mokkiCmBox.getValue(), varaus.getVarattu_pvm(), lasku.getSumma(), lasku.getAlv(), (lasku.getMaksettu() == 1 ? "joo" : "ei"));

            // Add content to the PDF at a fixed position
            Paragraph paragraph = new Paragraph(content);
            paragraph.setFixedPosition(100, 350, 400); // (x, y, width)
            document.add(paragraph);

            // Close the document
            document.close();
        } catch (IOException e) {
            System.out.println("!!Exc. VarausDC.saveLaskuToFile: " + e.toString());
        }
    }

     /** Set values to hour and minute boxes */
    private void initTimeComboBoxes() {
        // Hours
        for (int i = 0; i < 24; i++) {
            String formattedHour = String.format("%02d", i);
            varattuPvmHour.getItems().add(formattedHour);
            vahvistusPvmHour.getItems().add(formattedHour);
            varattuAlkupvmHour.getItems().add(formattedHour);
            varattuLoppupvmHour.getItems().add(formattedHour);
        }
        // Default values
        varattuPvmHour.setValue("00");
        vahvistusPvmHour.setValue("00");
        varattuAlkupvmHour.setValue("15");
        varattuLoppupvmHour.setValue("12");

        // Minutes
        for (int i = 0; i < 60; i++) {
            String formattedMinute = String.format("%02d", i);
            varattuPvmMin.getItems().add(formattedMinute);
            vahvistusPvmMin.getItems().add(formattedMinute);
            varattuAlkupvmMin.getItems().add(formattedMinute);
            varattuLoppupvmMin.getItems().add(formattedMinute);
        }
        // Default values
        varattuPvmMin.setValue("00");
        vahvistusPvmMin.setValue("00");
        varattuAlkupvmMin.setValue("00");
        varattuLoppupvmMin.setValue("00");
    }

    /** Initialize date pickers, add value listeners */
    private void initDatePickers() {
        // Calendar restriction "From"
        varattuAlkupvmPicker.valueProperty().addListener((observable, oldDate, newDate) -> {
            if (varattuLoppupvmPicker.getValue() != null) {
                numOfDays.set(newDate.until(varattuLoppupvmPicker.getValue()).getDays());
            }
            setLoppuCellValueFactory(newDate);

        });
        // Calendar restriction "To"
        varattuLoppupvmPicker.valueProperty().addListener((observable, oldDate, newDate) -> {
            if (varattuAlkupvmPicker.getValue() != null) {
                numOfDays.set(varattuAlkupvmPicker.getValue().until(newDate).getDays());
            }
            setAlkuCellValueFactory(newDate);
        });
    }

    private void setAlkuCellValueFactory(LocalDate date) {
        varattuAlkupvmPicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        boolean disable = rangesToDisable.stream()
                                .filter(r->r.initialDate.isBefore(item))
                                .anyMatch(r->r.endDate.isAfter(item));

                        long filter = rangesToDisable.stream()
                                .filter(r -> r.initialDate.isEqual(item) || r.endDate.isEqual(item))
                                .count();

                        boolean after = false;
                        if (date != null)
                            after = item.isAfter(varattuLoppupvmPicker.getValue().minusDays(1));

                        if (after || disable || filter > 0) {
                            setDisable(filter!=1 || after);
                            if (disable || (filter == 2))
                                setStyle("-fx-background-color: #ffc0cb;");
                            else if (filter == 1)
                                setStyle("-fx-background-color: #c0d2ff;");
                            else
                                setStyle("-fx-background-color: #d7d7d7;");
                        }
                    }
                };
            }
        });
    }

    private void setLoppuCellValueFactory(LocalDate date) {
        varattuLoppupvmPicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        boolean disable = rangesToDisable.stream()
                                .filter(r->r.initialDate.isBefore(item))
                                .anyMatch(r->r.endDate.isAfter(item));

                        long filter = rangesToDisable.stream()
                                .filter(r -> r.initialDate.isEqual(item) || r.endDate.isEqual(item))
                                .count();

                        boolean before = false;
                        if (date != null)
                            before = item.isBefore(varattuAlkupvmPicker.getValue().plusDays(1));

                        if (before || disable || filter > 0) {
                            setDisable(filter!=1 || before);
                            if (disable || (filter == 2))
                                setStyle("-fx-background-color: #ffc0cb;");
                            else if (filter == 1)
                                setStyle("-fx-background-color: #c0d2ff;");
                            else
                                setStyle("-fx-background-color: #c0c0c0;");
                        }
                    }
                };
            }
        });
    }

    /** Initialize varaus-data nodes */
    private void initVaraus() {
        // Asiakas
        tiedotGrP.add(asiakasACTF, 1, 0);
        // Mokki
        mokkiCmBox.getItems().addAll(SessionData.getMokit());
        mokkiCmBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            mokkiSumma.set(newValue.getHinta() * numOfDays.get());
            rangesToDisable.clear();
            SessionData.getVaraukset().forEach(v -> {
                if (v.getMokki_mokki_id() == newValue.getMokki_id()) {
                    rangesToDisable.add(new DisabledRange(
                            v.getVarattu_alkupvm().toLocalDateTime().toLocalDate(),
                            v.getVarattu_loppupvm().toLocalDateTime().toLocalDate()
                    ));
                }
            });
            setAlkuCellValueFactory(null);
            setLoppuCellValueFactory(null);
        });
        // Dates
        initDatePickers();
        initTimeComboBoxes();
        numOfDays.addListener(((observableValue, oldValue, newValue) -> {
            if (mokkiCmBox.getValue() != null)
                mokkiSumma.set(mokkiCmBox.getValue().getHinta() * newValue.intValue());
        }));
    }

    /** Initialize palvelut-data nodes */
    private void initPalvelut() {
        // Add listener to list of palvelu
        varauksenPalvelut.addListener(new ListChangeListener<VarauksenPalvelut>() {
            @Override
            public void onChanged(Change<? extends VarauksenPalvelut> change) {
                palvelutListView.getItems().clear();
                palvelutListView.getItems().addAll(varauksenPalvelut);

            }
        });

        // Add palvelut
        SessionData.getPalvelut().forEach(palvelu -> {
            Spinner<Integer> lkm = new Spinner<>(1, 50, 1);
            lkm.getStyleClass().add("spinner");
            lkm.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
                palvelutSumma.set(palvelutSumma.get() + palvelu.getHinta() * (newValue - oldValue));
            }));
            lkm.setEditable(true);
            lkm.setDisable(true);

            CheckBox palveluBox = new CheckBox(palvelu.toString());
            palveluBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    lkm.setDisable(false);
                    palvelutSumma.set(palvelutSumma.get() + palvelu.getHinta() * lkm.getValue());
                } else {
                    lkm.setDisable(true);
                    palvelutSumma.set(palvelutSumma.get() - (palvelu.getHinta() * lkm.getValue()));
                }
            });

            HBox container = new HBox(palveluBox, lkm);
            container.setSpacing(5.0);
            palvelutVBox.getChildren().add(container);
            palveluNodes.put(palvelu.getPalvelu_id(), new Node[]{palveluBox, lkm});
        });
    }

    /** Initialize lasku-data nodes and summa values*/
    public void initLasku() {
        // Summat
        mokkiSumma.addListener(((observableValue, oldValue, newValue) -> {
            mokkiSummaLbl.setText(newValue.toString());
            summa.set(newValue.doubleValue() + palvelutSumma.get());
        }));
        palvelutSumma.addListener(((observableValue, oldValue, newValue) -> {
            palvelutSummaLbl.setText(newValue.toString());
            summa.set(newValue.doubleValue() + mokkiSumma.get());
        }));

        summa.addListener(((observableValue, oldValue, newValue) -> {
            summaLbl.setText(newValue.toString());
            lasku.setSumma(newValue.doubleValue());
        }));
        // Paperi lasku
        paperiLaskuChB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                Asiakas asiakas = (Asiakas) asiakasACTF.getLastSelectedItem();
                SessionData.getPostit().forEach(p -> {
                    if (Objects.equals(asiakas.getPostinro(), p.getPostinro())) {
                       postinroACTF.setPromptText("Postinro");
                       postinroACTF.setLastSelectedItem(p);
                        return;
                    }
                });
                katuosoiteTF.setText(asiakas.getLahiosoite());
                paperiLaskuBox.getStyleClass().remove("hidden");
            } else
                paperiLaskuBox.getStyleClass().add("hidden");
        });
        paperiLaskuBox.getChildren().addFirst(postinroACTF);
    }

    /** Saves reservation data into Varaus-object */
    private void setData() {
        varaus.setAsiakas_id(((Asiakas)asiakasACTF.getLastSelectedItem()).getAsiakas_id());
        varaus.setMokki_mokki_id(mokkiCmBox.getValue().getMokki_id());

        LocalDateTime varattuPvmDate = varattuPvmPicker.getValue().atTime(Integer.parseInt(varattuPvmHour.getValue()), Integer.parseInt(varattuPvmMin.getValue()));
        varaus.setVarattu_pvm(Timestamp.valueOf(varattuPvmDate));

        LocalDateTime vahvistusPvmDate = vahvistusPvmPicker.getValue().atTime(Integer.parseInt(vahvistusPvmHour.getValue()), Integer.parseInt(vahvistusPvmMin.getValue()));
        varaus.setVahvistus_pvm(Timestamp.valueOf(vahvistusPvmDate));

        LocalDateTime varattuAlkupvmDate = varattuAlkupvmPicker.getValue().atTime(Integer.parseInt(varattuAlkupvmHour.getValue()), Integer.parseInt(varattuAlkupvmMin.getValue()));
        varaus.setVarattu_alkupvm(Timestamp.valueOf(varattuAlkupvmDate));

        LocalDateTime varattuLoppupvmDate = varattuLoppupvmPicker.getValue().atTime(Integer.parseInt(varattuLoppupvmHour.getValue()), Integer.parseInt(varattuLoppupvmMin.getValue()));
        varaus.setVarattu_loppupvm(Timestamp.valueOf(varattuLoppupvmDate));
    }

    /** Set chosen varaus-object data */
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
                mokkiCmBox.setValue(mokki);
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

    /** Set chosen varaus-object palvelut-data */
    private void setPalvelut() {
        String filter = "WHERE varaus_id = " + varaus.getVaraus_id();
        varauksenPalvelut = SessionData.dataBase.getAllRows("varauksen_palvelut", "varaus_id", VarauksenPalvelut.class, filter);
        varauksenPalvelut.forEach(vp -> {
            // CheckBox
            Node[] nodes = palveluNodes.get(vp.getPalvelu_id());
            ((CheckBox) nodes[0]).setSelected(true);
            ((Spinner<Integer>) nodes[1]).getValueFactory().setValue(vp.getLkm());
        });
    }

    /** Set chosen varaus-object lasku-data */
    private void setLasku() {
        Lasku l = SessionData.dataBase.getRow("lasku", "varaus_id", varaus.getVaraus_id(), Lasku.class);
        if (l == null)
            SessionData.dataBase.addRow("lasku", lasku.getAttrMap());
        else
            lasku = l;
    }

    /** Create new lasku/bill */
    private void addLasku() {
        lasku.setVaraus_id(varaus.getVaraus_id());
        if (SessionData.dataBase.addRow("lasku", lasku.getAttrMap())) {
            SessionData.setLaskut();
        } else {
            alertMessage = "Tiedon lisäämisen virhe";
        }
    }

    /** Update palvelut-data */
    private void updatePalvelut() {
        HashMap<Integer, VarauksenPalvelut> vpt = new HashMap<>();
        varauksenPalvelut.forEach(vp -> {
            vpt.put(vp.getPalvelu_id(), vp);
        });

        palveluNodes.forEach((p, nodes) -> {
            CheckBox chB = (CheckBox) nodes[0];
            Spinner<Integer> lkm = (Spinner) nodes[1];

            if (vpt.containsKey(p)) {
                VarauksenPalvelut vp = vpt.get(p);
                String filter = "WHERE varaus_id=" + vp.getVaraus_id() + " AND palvelu_id=" + vp.getPalvelu_id();

                if (chB.isSelected()) { // Update row
                    vp.setLkm(lkm.getValue());
                    SessionData.dataBase.updateRow("varauksen_palvelut", vp.getAttrMap(), filter);
                }
                else // Delete row
                    SessionData.dataBase.deleteRow("varauksen_palvelut", filter);
            } else {
                if (chB.isSelected()) { // Add row
                    VarauksenPalvelut vp = new VarauksenPalvelut(varaus.getVaraus_id(), p, lkm.getValue());
                    SessionData.dataBase.addRow("varauksen_palvelut", vp.getAttrMap());
                }
            }
        });
    }

    /** Update lasku-data */
    private void updateLasku() {
        SessionData.dataBase.updateRow("lasku", lasku.getAttrMap(), "lasku_id", lasku.getLasku_id());
    }

}
