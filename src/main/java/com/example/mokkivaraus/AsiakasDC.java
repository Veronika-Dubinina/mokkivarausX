package com.example.mokkivaraus;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AsiakasDC extends DialogController {
    // Attributes
    private Asiakas asiakas = new Asiakas();

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
    private TextField etunimiField = new TextField();
    private TextField sukunimiField = new TextField();
    private TextField lahiosoiteField = new TextField();
    private TextField emailField = new TextField();
    private TextField puhelinnroField = new TextField();

    // Map to store error messages for each field
    private Map<TextField, String> errorMessages = new HashMap<>();

    // Constructor
    public AsiakasDC(String tableName, String identifierKey) {
        super(tableName, identifierKey);
    }

    // Methods
    public Asiakas getAsiakas() {
        return this.asiakas;
    }

    @Override
    void setObject(Object object) {
        this.editMode = true;
        this.asiakas = (Asiakas) object;
        this.identifierValue = asiakas.getAsiakas_id();
    }

    @Override
    void setDialogContent() {
        // DialogPane title
        dialogTitle.setText("Lisää uusi asiakas");

        // Labels and Fields
        formsGridPane.add(new Label("Postinro:"), 0, 0);
        formsGridPane.add(postinroACTF, 1, 0);

        formsGridPane.add(new Label("Etunimi:"), 0, 1);
        formsGridPane.add(etunimiField, 1, 1);

        formsGridPane.add(new Label("Sukunimi:"), 0, 2);
        formsGridPane.add(sukunimiField, 1, 2);

        formsGridPane.add(new Label("Lähiosoite:"), 0, 3);
        formsGridPane.add(lahiosoiteField, 1, 3);

        formsGridPane.add(new Label("Email:"), 0, 4);
        formsGridPane.add(emailField, 1, 4);

        formsGridPane.add(new Label("Puhelinnumero:"), 0, 5);
        formsGridPane.add(puhelinnroField, 1, 5);
    }

    @Override
    void setEditContent() {
        // DialogPane title
        dialogTitle.setText("Päivitä asiakkaan tiedot");

        // Set data from asiakas-object
        SessionData.getPostit().forEach(p -> {
            if (Objects.equals(asiakas.getPostinro(), p.getPostinro())) {
                postinroACTF.setLastSelectedItem(p);
                return;
            }
        });
        etunimiField.setText(asiakas.getEtunimi());
        sukunimiField.setText(asiakas.getSukunimi());
        lahiosoiteField.setText(asiakas.getLahiosoite());
        emailField.setText(asiakas.getEmail());
        puhelinnroField.setText(asiakas.getPuhelinnro());
    }

    @Override
    HashMap<String, Object> listOfAttributes() {
        return asiakas.getAttrMap();
    }

    @Override
    boolean checkData() {
        clearErrorMessages(); // Очистим любые предыдущие сообщения об ошибках
        boolean dataValid = true; // Переменная для отслеживания общего статуса данных
        dataValid &= checkPostinro();
        dataValid &= checkEtunimi();
        dataValid &= checkSukunimi();
        dataValid &= checkLahiosoite();
        dataValid &= checkEmail();
        dataValid &= checkPuhelinnro();

        if (!dataValid) {
            alertTitle = "Virhe tietojen tarkastuksessa";
            alertMessage = "";
            for (Map.Entry<TextField, String> entry : errorMessages.entrySet()) {
                // Проверим, была ли уже установлена общая ошибка
                if (alertMessage.isEmpty()) {
                    alertMessage = entry.getValue();
                } else {
                    // Если была, добавим новое сообщение в конец
                    alertMessage += "\n" + entry.getValue();
                }
            }
        } else {
            alertTitle = "Menestys";
            alertMessage = "Tiedot tarkastettu и hyväksytty.";
        }
        return dataValid;
    }


    // Очистить карту сообщений об ошибках
    private void clearErrorMessages() {
        errorMessages.clear();
    }

    // Методы для проверки каждого поля индивидуально
    private boolean checkPostinro() {
        var posti = postinroACTF.getLastSelectedItem();
        if (posti == null) { // is empty
            alertMessage = "Valitse Posti, kiitos!";
            return false;
        }

        // if ok
        asiakas.setPostinro(((Posti) posti).getPostinro());
        return true;
    }

    private boolean checkEtunimi() {
        String etunimi = etunimiField.getText().trim();
        if (etunimi.isEmpty()) // is empty
            errorMessages.put(etunimiField, "Kirjoita Etunimi, kiitos!");
        else if (etunimi.length() > 20) // longer than 20 chars
            errorMessages.put(etunimiField, "Etunimi ei saa ylittää 20 merkkiä.");
        else { // if ok
            asiakas.setEtunimi(etunimi);
            return true;
        }
        return false;
    }

    private boolean checkSukunimi() {
        String sukunimi = sukunimiField.getText().trim();
        if (sukunimi.isEmpty()) // is empty
            errorMessages.put(etunimiField, "Kirjoita Sukunimi, kiitos!");
        else if (sukunimi.length() > 40) // longer than 40 chars
            errorMessages.put(etunimiField, "Sukunimi ei saa ylittää 40 merkkiä.");
        else { // if ok
            asiakas.setSukunimi(sukunimi);
            return true;
        }
        return false;
    }

    private boolean checkLahiosoite() {
        String lahiosoiteText = lahiosoiteField.getText();
        if (lahiosoiteText == null) {
            asiakas.setLahiosoite(null); // Поле может быть пустым, поэтому установим значение null
        } else {
            String lahiosoite = lahiosoiteText.trim();
            if (lahiosoite.isEmpty()) {
                asiakas.setLahiosoite(null); // Поле может быть пустым, поэтому установим значение null
            } else if (lahiosoite.length() > 40) {
                errorMessages.put(lahiosoiteField, "Lähiosoite ei saa ylittää 40 merkkiä.");
                return false;
            } else {
                asiakas.setLahiosoite(lahiosoite);
            }
        }
        return true;
    }

    private boolean checkEmail() {
        String emailText = emailField.getText();
        if (emailText == null) {
            asiakas.setEmail(null); // Поле может быть пустым, поэтому установим значение null
        } else {
            String email = emailText.trim();
            if (email.isEmpty()) {
                asiakas.setEmail(null); // Поле может быть пустым, поэтому установим значение null
            } else if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
                errorMessages.put(emailField, "Anna oikea sähköpostimuoto.");
                return false;
            } else if (email.length() > 50) {
                errorMessages.put(emailField, "Sähköposti ei saa ylittää 50 merkkiä.");
                return false;
            } else {
                asiakas.setEmail(email);
            }
        }
        return true;
    }

    private boolean checkPuhelinnro() {
        String puhelinnro = puhelinnroField.getText().trim();
        if (puhelinnro.isEmpty()) {
            errorMessages.put(puhelinnroField, "Kirjoita puhelinnumero, kiitos!");
        } else if (puhelinnro.length() > 15) {
            errorMessages.put(puhelinnroField, "Puhelinnumero saa olla enintään 15 merkkiä pitkä.");
        } else if (!puhelinnro.matches("\\+?[0-9]+")) {
            errorMessages.put(puhelinnroField, "Puhelinnumero saa sisältää vain numeroita.");
        } else {
            asiakas.setPuhelinnro(puhelinnro);
            return true;
        }
        return false;
    }
}
