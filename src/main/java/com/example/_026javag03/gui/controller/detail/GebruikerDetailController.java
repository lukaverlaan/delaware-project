package com.example._026javag03.gui.controller.detail;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.gebruiker.ValidatieUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class GebruikerDetailController {
    private final GebruikerController dc;
    private final GebruikerDTO dto;

    public GebruikerDetailController(GebruikerController dc, GebruikerDTO dto) {
        this.dc = dc;
        this.dto = dto;
    }

    @FXML
    private TextField personeelsnummerField;
    @FXML
    private TextField naamField;
    @FXML
    private TextField voornaamField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telefoonField;
    @FXML
    private TextField straatField;
    @FXML
    private TextField nummerField;
    @FXML
    private TextField postbusField;
    @FXML
    private TextField stadField;
    @FXML
    private TextField postcodeField;
    @FXML
    private DatePicker geboorteDatumPicker;
    @FXML
    private ComboBox<String> rolComboBox;
    @FXML
    private RadioButton actiefRadio;
    @FXML
    private RadioButton inactiefRadio;

    @FXML
    private void initialize() {
        rolComboBox.getItems().addAll("ADMINISTRATOR", "MANAGER", "VERANTWOORDELIJKE", "WERKNEMER");
        personeelsnummerField.setText(dto.personeelsnummer());
        personeelsnummerField.setEditable(false);
        personeelsnummerField.setDisable(true);
        naamField.setText(dto.naam());
        voornaamField.setText(dto.voornaam());
        emailField.setText(dto.email());
        telefoonField.setText(dto.telefoonnummer() != null ? dto.telefoonnummer() : "");
        straatField.setText(dto.straat());
        nummerField.setText(dto.nummer());
        postbusField.setText(dto.postbus() != null ? dto.postbus() : "");
        stadField.setText(dto.stad());
        postcodeField.setText(String.valueOf(dto.postcode()));
        geboorteDatumPicker.setValue(dto.geboorteDatum());
        rolComboBox.setValue(dto.rol().toString());
        if (dto.status() == Status.ACTIEF) actiefRadio.setSelected(true);
        else inactiefRadio.setSelected(true);
    }

    @FXML
    private void handleOpslaan() {
        if (!valideerInput()) return;
        try {
            GebruikerDTO updatedDTO = new GebruikerDTO(dto.id(), dto.personeelsnummer(), naamField.getText().trim(), voornaamField.getText().trim(), emailField.getText().trim(), telefoonField.getText().isBlank() ? null : telefoonField.getText().trim(), straatField.getText().trim(), nummerField.getText().trim(), postbusField.getText().isBlank() ? null : postbusField.getText().trim(), stadField.getText().trim(), Integer.parseInt(postcodeField.getText().trim()), geboorteDatumPicker.getValue(), Rol.valueOf(rolComboBox.getValue()), actiefRadio.isSelected() ? Status.ACTIEF : Status.INACTIEF);
            dc.updateGebruiker(updatedDTO);
            sluitScherm();
        } catch (IllegalArgumentException e) {
            toonFoutmelding(e.getMessage());
        }
    }

    @FXML
    private void handleVerwijder() {
        GebruikerDTO updatedDTO = new GebruikerDTO(dto.id(), dto.personeelsnummer(), dto.naam(), dto.voornaam(), dto.email(), dto.telefoonnummer(), dto.straat(), dto.nummer(), dto.postbus(), dto.stad(), dto.postcode(), dto.geboorteDatum(), dto.rol(), Status.INACTIEF);
        dc.updateGebruiker(updatedDTO);
        toonInfo("Gebruiker heeft nu status inactief.");
        sluitScherm();
    }

    private boolean valideerInput() {
        if (naamField.getText().isBlank()) {
            toonFoutmelding("Naam is verplicht.");
            return false;
        }
        if (voornaamField.getText().isBlank()) {
            toonFoutmelding("Voornaam is verplicht.");
            return false;
        }
        if (!ValidatieUtil.isGeldigEmail(emailField.getText())) {
            toonFoutmelding("Ongeldig e-mailadres.");
            return false;
        }
        if (!ValidatieUtil.isGeldigePostcode(postcodeField.getText().trim())) {
            toonFoutmelding("Postcode moet exact 4 cijfers bevatten.");
            return false;
        }
        if (straatField.getText().isBlank()) {
            toonFoutmelding("Straat is verplicht.");
            return false;
        }
        if (nummerField.getText().isBlank()) {
            toonFoutmelding("Huisnummer is verplicht.");
            return false;
        }
        if (stadField.getText().isBlank()) {
            toonFoutmelding("Stad is verplicht.");
            return false;
        }
        if (geboorteDatumPicker.getValue() == null) {
            toonFoutmelding("Geboortedatum is verplicht.");
            return false;
        }
        if (rolComboBox.getValue() == null) {
            toonFoutmelding("Rol is verplicht.");
            return false;
        }
        return true;
    }

    private void toonFoutmelding(String boodschap) {
        new Alert(Alert.AlertType.ERROR, boodschap).showAndWait();
    }

    private void toonInfo(String boodschap) {
        new Alert(Alert.AlertType.INFORMATION, boodschap).showAndWait();
    }

    private void sluitScherm() {
        Stage stage = (Stage) naamField.getScene().getWindow();
        stage.close();
    }
}