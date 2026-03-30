package com.example._026javag03.gui.controller.detail;

import com.example._026javag03.domein.controller.NotificatieController;
import com.example._026javag03.dto.NotificatieDTO;
import com.example._026javag03.util.notificatie.NotificatieStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificatieDetailController {

    private final NotificatieController nc;
    private final NotificatieDTO dto;

    public NotificatieDetailController(NotificatieController nc, NotificatieDTO dto) {
        this.nc = nc;
        this.dto = dto;
    }

    @FXML private TextField codeField;
    @FXML private TextField datumField;
    @FXML private TextField typeField;

    @FXML private TextArea inhoudArea;

    @FXML private ComboBox<String> statusCombo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm");

    @FXML
    private void initialize() {

        statusCombo.getItems().addAll(
                "AANGEMAAKT",
                "ONGELEZEN",
                "GELEZEN"
        );

        codeField.setText(dto.code());
        codeField.setDisable(true);

        datumField.setText(dto.datum() != null ? dto.datum().format(formatter) : "");
        datumField.setDisable(true);

        typeField.setText(dto.type());

        inhoudArea.setText(dto.inhoud());

        if (dto.status() != null) {
            statusCombo.setValue(dto.status().toString());
        }
    }

    @FXML
    private void handleOpslaan() {

        if (!valideerInput()) return;

        try {

            NotificatieDTO updated = new NotificatieDTO(
                    dto.code(),
                    dto.datum() != null ? dto.datum() : LocalDateTime.now(),
                    typeField.getText().trim(),
                    inhoudArea.getText().trim(),
                    NotificatieStatus.valueOf(statusCombo.getValue())
            );

            nc.updateNotificatie(updated);

            sluitScherm();

        } catch (Exception e) {
            toonFoutmelding(e.getMessage());
        }
    }

    @FXML
    private void handleVerwijder() {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Notificatie verwijderen");
        confirm.setHeaderText("Deze notificatie permanent verwijderen?");
        confirm.setContentText("Deze actie kan niet ongedaan worden.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            nc.verwijderNotificatie(dto.code());

            toonInfo("Notificatie succesvol verwijderd.");
            sluitScherm();
        }
    }

    private boolean valideerInput() {

        if (typeField.getText() == null || typeField.getText().isBlank()) {
            toonFoutmelding("Type is verplicht.");
            return false;
        }

        if (inhoudArea.getText() == null || inhoudArea.getText().isBlank()) {
            toonFoutmelding("Inhoud is verplicht.");
            return false;
        }

        if (statusCombo.getValue() == null) {
            toonFoutmelding("Status is verplicht.");
            return false;
        }

        return true;
    }

    private void toonFoutmelding(String boodschap) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Ongeldige invoer");
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

    private void sluitScherm() {

        Stage stage = (Stage) codeField.getScene().getWindow();
        stage.close();
    }
}