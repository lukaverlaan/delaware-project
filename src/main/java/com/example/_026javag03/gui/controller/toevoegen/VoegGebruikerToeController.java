package com.example._026javag03.gui.controller.toevoegen;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.gui.controller.lijst.GebruikerLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.gebruiker.ValidatieUtil;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;

public class VoegGebruikerToeController {

    private final GebruikerController dc;
    private final ViewManager viewManager;

    public VoegGebruikerToeController(GebruikerController dc, ViewManager viewManager) {
        this.dc = dc;
        this.viewManager = viewManager;
    }

    @FXML private TextField naamField;
    @FXML private TextField voornaamField;
    @FXML private TextField emailField;
    @FXML private Label telefoonLabel;
    @FXML private TextField telefoonField;
    @FXML private TextField straatField;
    @FXML private TextField nummerField;
    @FXML private TextField postbusField;
    @FXML private TextField stadField;
    @FXML private TextField postcodeField;
    @FXML private DatePicker geboorteDatumPicker;
    @FXML private ComboBox<String> rolComboBox;
    @FXML private RadioButton actiefRadio;
    @FXML private RadioButton inactiefRadio;

    @FXML private Label emailBezigLabel;
    @FXML private Button toevoegenBtn;

    @FXML
    private void initialize() {

        actiefRadio.setSelected(true);

        rolComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue == null) {
                telefoonLabel.setText("Telefoonnummer (optioneel)");
                return;
            }

            Rol rol = Rol.valueOf(newValue);

            telefoonLabel.setText(
                    rol == Rol.WERKNEMER
                            ? "Telefoonnummer"
                            : "Telefoonnummer (optioneel)"
            );
        });
    }

    @FXML
    private void handleTerug() {
        viewManager.showView(
                new GebruikerLijstController(dc, viewManager).getView()
        );
    }

    @FXML
    private void handleToevoegen() {

        if (!valideerInput()) return;

        toonEmailBezigToast();
        toevoegenBtn.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                String gsm = getOptionalText(telefoonField);
                String postbus = getOptionalText(postbusField);

                GebruikerDTO dto = new GebruikerDTO(
                        null,
                        null,
                        getRequiredText(naamField),
                        getRequiredText(voornaamField),
                        getRequiredText(emailField),
                        gsm,
                        getRequiredText(straatField),
                        getRequiredText(nummerField),
                        postbus,
                        getRequiredText(stadField),
                        Integer.parseInt(postcodeField.getText().trim()),
                        geboorteDatumPicker.getValue(),
                        Rol.valueOf(rolComboBox.getValue()),
                        actiefRadio.isSelected() ? Status.ACTIEF : Status.INACTIEF,
                        true
                );

                dc.insertGebruiker(dto);

                return null;
            }
        };

        task.setOnSucceeded(e -> {

            viewManager.setEmailVerstuurd(true);

            viewManager.showView(
                    new GebruikerLijstController(dc, viewManager).getView()
            );

        });

        task.setOnFailed(e -> {

            toevoegenBtn.setDisable(false);
            toonFoutmelding(task.getException().getMessage());

        });

        new Thread(task).start();
    }

    private void toonEmailBezigToast() {

        emailBezigLabel.setOpacity(0);
        emailBezigLabel.setVisible(true);

        FadeTransition fadeIn =
                new FadeTransition(Duration.millis(200), emailBezigLabel);

        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private boolean valideerInput() {

        if (!checkNotBlank(voornaamField, "Voornaam is verplicht.")) return false;
        if (!checkNotBlank(naamField, "Naam is verplicht.")) return false;
        if (!checkEmail()) return false;
        if (!checkNotBlank(straatField, "Straat is verplicht.")) return false;
        if (!checkNotBlank(nummerField, "Huisnummer is verplicht.")) return false;
        if (!checkPostcode()) return false;
        if (!checkNotBlank(stadField, "Stad is verplicht.")) return false;

        if (geboorteDatumPicker.getValue() == null) {
            toonFoutmelding("Geboortedatum is verplicht.");
            return false;
        }

        if (rolComboBox.getValue() == null) {
            toonFoutmelding("Rol is verplicht.");
            return false;
        }

        if (!actiefRadio.isSelected() && !inactiefRadio.isSelected()) {
            toonFoutmelding("Status is verplicht.");
            return false;
        }

        return checkGsm();
    }

    private boolean checkNotBlank(TextField field, String message) {

        if (field.getText() == null || field.getText().isBlank()) {
            toonFoutmelding(message);
            return false;
        }

        return true;
    }

    private boolean checkEmail() {

        if (!ValidatieUtil.isGeldigEmail(emailField.getText())) {
            toonFoutmelding("Ongeldig e-mailadres.");
            return false;
        }

        return true;
    }

    private boolean checkPostcode() {

        if (!ValidatieUtil.isGeldigePostcode(postcodeField.getText())) {
            toonFoutmelding("Postcode moet exact 4 cijfers bevatten.");
            return false;
        }

        return true;
    }

    private boolean checkGsm() {

        Rol rol = Rol.valueOf(rolComboBox.getValue());
        String gsm = telefoonField.getText().trim();

        if (rol == Rol.WERKNEMER && gsm.isBlank()) {
            toonFoutmelding("Gsm is verplicht voor werknemers.");
            return false;
        }

        if (!gsm.isBlank() && !ValidatieUtil.isGeldigGsm(gsm)) {
            toonFoutmelding("Ongeldig gsm-nummer.");
            return false;
        }

        return true;
    }

    private String getRequiredText(TextField field) {
        return field.getText().trim();
    }

    private String getOptionalText(TextField field) {

        String value = field.getText();
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private void toonFoutmelding(String boodschap) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Onvolledige invoer");
        alert.setContentText(boodschap);
        alert.showAndWait();
    }

    public Parent getView() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/com/example/_026javag03/gui/VoegGebruikerToeScherm.fxml"
                    ));

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException("Kan FXML niet laden");
        }
    }
}