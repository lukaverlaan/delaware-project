package com.example._026javag03.gui.controller.detail;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.gebruiker.ValidatieUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MachineDetailController {
    private final MachineController dc;
//    private final GebruikerDTO dto;

    public MachineDetailController(MachineController dc) {
        this.dc = dc;

    }

    @FXML private TextField locatieInSiteField;
    @FXML private TextField productInfoField;
    @FXML private TextField uptimeField;


    @FXML
    private void initialize() {

    }

    @FXML
    private void handleOpslaan() {
//        if (!valideerInput()) return;
//        try {
//            GebruikerDTO updatedDTO = new GebruikerDTO(dto.id(), dto.personeelsnummer(), naamField.getText().trim(), voornaamField.getText().trim(), emailField.getText().trim(), telefoonField.getText().isBlank() ? null : telefoonField.getText().trim(), straatField.getText().trim(), nummerField.getText().trim(), postbusField.getText().isBlank() ? null : postbusField.getText().trim(), stadField.getText().trim(), Integer.parseInt(postcodeField.getText().trim()), geboorteDatumPicker.getValue(), Rol.valueOf(rolComboBox.getValue()), actiefRadio.isSelected() ? Status.ACTIEF : Status.INACTIEF);
//            dc.updateGebruiker(updatedDTO);
//            toonInfo("Wijzigingen opgeslagen.");
//            sluitScherm();
//        } catch (IllegalArgumentException e) {
//            toonFoutmelding(e.getMessage());
//        }
        sluitScherm();
    }

    @FXML
    private void handleVerwijder() {
//        GebruikerDTO updatedDTO = new GebruikerDTO(dto.id(), dto.personeelsnummer(), dto.naam(), dto.voornaam(), dto.email(), dto.telefoonnummer(), dto.straat(), dto.nummer(), dto.postbus(), dto.stad(), dto.postcode(), dto.geboorteDatum(), dto.rol(), Status.INACTIEF);
//        dc.updateGebruiker(updatedDTO);
//        toonInfo("Gebruiker op inactief gezet.");
        sluitScherm();
    }

    private boolean valideerInput() {
        return false;
    }

    private void toonFoutmelding(String boodschap) {
        new Alert(Alert.AlertType.ERROR, boodschap).showAndWait();
    }

    private void toonInfo(String boodschap) {
        new Alert(Alert.AlertType.INFORMATION, boodschap).showAndWait();
    }

    private void sluitScherm() {
        Stage stage = (Stage) locatieInSiteField.getScene().getWindow();
        stage.close();
    }
}