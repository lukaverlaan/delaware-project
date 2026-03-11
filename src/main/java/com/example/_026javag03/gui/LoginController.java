package com.example._026javag03.gui;

import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.gui.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;

public class LoginController {

    @FXML
    private TextField emailVeld;

    @FXML
    private PasswordField wachtwoordVeld;

    @FXML
    private Label errorLabel;

    private final GebruikerController gc = new GebruikerController();

    @Setter
    private BaseController baseController;

    @FXML
    public void handleLogin() {

        try {

            GebruikerDTO gebruiker = gc.login(
                    emailVeld.getText(),
                    wachtwoordVeld.getText()
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