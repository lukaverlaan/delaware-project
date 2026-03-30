package com.example._026javag03.gui.controller.toevoegen;

import com.example._026javag03.domein.controller.NotificatieController;
import com.example._026javag03.dto.NotificatieDTO;
import com.example._026javag03.gui.controller.lijst.NotificatieLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import com.example._026javag03.util.notificatie.NotificatieStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;

public class VoegNotificatieToeController {

    private final NotificatieController nc;
    private final ViewManager viewManager;

    public VoegNotificatieToeController(NotificatieController nc, ViewManager viewManager) {
        this.nc = nc;
        this.viewManager = viewManager;
    }

    @FXML private TextField typeField;
    @FXML private TextArea inhoudArea;

    @FXML
    private void handleToevoegen() {

        if (!valideerInput()) return;

        try {

            NotificatieDTO dto = new NotificatieDTO(
                    null,
                    LocalDateTime.now(),
                    typeField.getText().trim(),
                    inhoudArea.getText().trim(),
                    NotificatieStatus.AANGEMAAKT
            );

            nc.insertNotificatie(dto);

            viewManager.showView(
                    new NotificatieLijstController(nc, viewManager).getView()
            );

        } catch (Exception e) {
            toonFoutmelding(e.getMessage());
        }
    }

    @FXML
    private void handleTerug() {

        viewManager.showView(
                new NotificatieLijstController(nc, viewManager).getView()
        );
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

        return true;
    }

    private void toonFoutmelding(String boodschap) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Ongeldige invoer");
        alert.setContentText(boodschap);
        alert.showAndWait();
    }

    public Parent getView() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/VoegNotificatieToeScherm.fxml")
            );

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException("Kan FXML niet laden");
        }
    }
}