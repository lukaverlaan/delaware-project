package com.example._026javag03.gui.controller.login;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.gui.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NieuwWachtwoordController {

    @FXML
    private PasswordField wachtwoord1;

    @FXML
    private PasswordField wachtwoord2;

    @FXML
    private TextField wachtwoord1Text;

    @FXML
    private TextField wachtwoord2Text;

    @FXML
    private Label reqLength;

    @FXML
    private Label reqUpper;

    @FXML
    private Label reqLower;

    @FXML
    private Label reqDigit;

    @FXML
    private Label reqSpecial;

    @FXML
    private Label errorLabel;

    private GebruikerController gc = new GebruikerController();

    private GebruikerDTO gebruiker;

    private BaseController baseController;

    public void init(GebruikerDTO gebruiker, BaseController baseController) {
        this.gebruiker = gebruiker;
        this.baseController = baseController;

        wachtwoord1.textProperty().addListener((obs, oldVal, newVal) -> valideer(newVal));
        wachtwoord1Text.textProperty().addListener((obs, oldVal, newVal) -> valideer(newVal));

        // Om validatie kruisjes meteen rood te maken
        valideer("");
    }

    private void valideer(String wachtwoord) {

        update(reqLength, wachtwoord.length() >= 8);
        update(reqUpper, wachtwoord.matches(".*[A-Z].*"));
        update(reqLower, wachtwoord.matches(".*[a-z].*"));
        update(reqDigit, wachtwoord.matches(".*\\d.*"));
        update(reqSpecial, wachtwoord.matches(".*[@#$%^&+=!?.*_-].*"));

    }

    private void update(Label label, boolean ok) {

        if (ok) {
            label.setText("✔ " + label.getText().substring(2));
            label.setStyle("-fx-text-fill: green;");
        } else {
            label.setText("✖ " + label.getText().substring(2));
            label.setStyle("-fx-text-fill: red;");
        }

    }

    @FXML
    private void toggleWachtwoord() {

        if (wachtwoord1.isVisible()) {

            wachtwoord1Text.setText(wachtwoord1.getText());
            wachtwoord1Text.setVisible(true);
            wachtwoord1.setVisible(false);

            wachtwoord2Text.setText(wachtwoord2.getText());
            wachtwoord2Text.setVisible(true);
            wachtwoord2.setVisible(false);

        } else {

            wachtwoord1.setText(wachtwoord1Text.getText());
            wachtwoord1.setVisible(true);
            wachtwoord1Text.setVisible(false);

            wachtwoord2.setText(wachtwoord2Text.getText());
            wachtwoord2.setVisible(true);
            wachtwoord2Text.setVisible(false);

        }
    }

    @FXML
    private void wijzigWachtwoord() {

        String ww1 = wachtwoord1.isVisible() ? wachtwoord1.getText() : wachtwoord1Text.getText();
        String ww2 = wachtwoord2.isVisible() ? wachtwoord2.getText() : wachtwoord2Text.getText();

        if (!ww1.equals(ww2)) {
            toonFout("De wachtwoorden komen niet overeen.");
            return;
        }

        try {

            gc.wijzigWachtwoord(
                    gebruiker.id(),
                    ww1
            );

            baseController.loginSucces(gebruiker);

        } catch (IllegalArgumentException e) {

            toonFout(e.getMessage());

        }
    }

    private void toonFout(String boodschap) {
        errorLabel.setText(boodschap);
        errorLabel.setVisible(true);
    }
}