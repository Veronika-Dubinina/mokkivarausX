package com.example.mokkivaraus;

import javafx.scene.control.*;
import java.util.HashMap;
import java.util.Map;

public class AsiakasDC extends DialogController {
    // Attributes
    private Asiakas asiakas = new Asiakas();

    private TextField postinroField = new TextField();
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
        formsGridPane.add(postinroField, 1, 0);

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
        postinroField.setText(asiakas.getPostinro());
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
        String postinroText = postinroField.getText();
        if (postinroText == null) {
            asiakas.setPostinro(null); // Поле может быть пустым, поэтому установим значение null
        } else {
            String postinro = postinroText.trim();
            if (postinro.isEmpty()) {
                asiakas.setPostinro(null); // Поле может быть пустым, поэтому установим значение null
            } else if (postinro.length() != 5) {
                errorMessages.put(postinroField, "Postinumeron tulee olla 5 merkkiä pitkä.");
                return false;
            } else {
                asiakas.setPostinro(postinro);
            }
        }
        return true;
    }

    private boolean checkEtunimi() {
        String etunimiText = etunimiField.getText();
        if (etunimiText == null) {
            asiakas.setEtunimi(null); // Поле может быть пустым, поэтому установим значение null
        } else {
            String etunimi = etunimiText.trim();
            if (etunimi.isEmpty()) {
                asiakas.setEtunimi(null); // Поле может быть пустым, поэтому установим значение null
            } else if (etunimi.length() > 20) {
                errorMessages.put(etunimiField, "Etunimi ei saa ylittää 20 merkkiä.");
                return false;
            } else {
                asiakas.setEtunimi(etunimi);
            }
        }
        return true;
    }

    private boolean checkSukunimi() {
        String sukunimiText = sukunimiField.getText();
        if (sukunimiText == null) {
            asiakas.setSukunimi(null); // Поле может быть пустым, поэтому установим значение null
        } else {
            String sukunimi = sukunimiText.trim();
            if (sukunimi.isEmpty()) {
                asiakas.setSukunimi(null); // Поле может быть пустым, поэтому установим значение null
            } else if (sukunimi.length() > 40) {
                errorMessages.put(sukunimiField, "Sukunimi ei saa ylittää 40 merkkiä.");
                return false;
            } else {
                asiakas.setSukunimi(sukunimi);
            }
        }
        return true;
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
        String puhelinnroText = puhelinnroField.getText();
        if (puhelinnroText == null) {
            asiakas.setPuhelinnro(null); // Поле может быть пустым, поэтому установим значение null
        } else {
            String puhelinnro = puhelinnroText.trim();
            if (puhelinnro.isEmpty()) {
                asiakas.setPuhelinnro(null); // Поле может быть пустым, поэтому установим значение null
            } else if (puhelinnro.length() > 15) {
                errorMessages.put(puhelinnroField, "Puhelinnumero saa olla enintään 15 merkkiä pitkä.");
                return false;
            } else if (!puhelinnro.matches("\\d+")) {
                errorMessages.put(puhelinnroField, "Puhelinnumero saa sisältää vain numeroita.");
                return false;

            } else {
                asiakas.setPuhelinnro(puhelinnro);
            }
        }
        return true;
    }
}
