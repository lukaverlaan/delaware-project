package com.example._026javag03.gui;

import com.example._026javag03.domein.GebruikerController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.util.Rol;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.ValidatieUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class VoegGebruikerToeController {

    private final GebruikerController dc;

    public VoegGebruikerToeController(GebruikerController dc) {
        this.dc = dc;
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
    private void handleToevoegen() {

        if (!valideerInput()) return;

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
                actiefRadio.isSelected() ? Status.ACTIEF : Status.INACTIEF
        );

        try {
            dc.insertGebruiker(dto);
            toonInfo("Gebruiker succesvol toegevoegd!");
            resetVelden();
        } catch (IllegalArgumentException e) {
            toonFoutmelding(e.getMessage());
        }
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

    private void toonInfo(String boodschap) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succes");
        alert.setHeaderText(null);
        alert.setContentText(boodschap);
        alert.showAndWait();
    }

    private void resetVelden() {
        naamField.clear();
        voornaamField.clear();
        emailField.clear();
        telefoonField.clear();
        straatField.clear();
        nummerField.clear();
        postbusField.clear();
        stadField.clear();
        postcodeField.clear();
        geboorteDatumPicker.setValue(null);
        rolComboBox.setValue(null);
        actiefRadio.setSelected(true);
        inactiefRadio.setSelected(false);
    }

    public Parent getView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/VoegGebruikerToeScherm.fxml")
            );
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Kan FXML niet laden");
        }
    }
}