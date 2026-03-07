package com.example._026javag03.gui.controller.detail;

import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.util.site.ProductieStatus;
import com.example._026javag03.util.Status;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SiteDetailController {

    private final SiteController sc;
    private final SiteDTO dto;

    public SiteDetailController(SiteController sc, SiteDTO dto) {
        this.sc = sc;
        this.dto = dto;
    }

    @FXML private TextField naamField;
    @FXML private TextField locatieField;
    @FXML private TextField capaciteitField;

    @FXML private ComboBox<String> operationeleStatusCombo;
    @FXML private ComboBox<String> productieStatusCombo;

    @FXML
    private void initialize() {

        operationeleStatusCombo.getItems().addAll("ACTIEF","INACTIEF");

        productieStatusCombo.getItems().addAll(
                "OPERATIONEEL",
                "ONDERHOUD",
                "STILGELEGD"
        );

        naamField.setText(dto.naam());
        locatieField.setText(dto.locatie());
        capaciteitField.setText(String.valueOf(dto.capaciteit()));

        operationeleStatusCombo.setValue(dto.operationeleStatus().toString());
        productieStatusCombo.setValue(dto.productieStatus().toString());
    }

    @FXML
    private void handleOpslaan() {

        if (!valideerInput()) return;

        try {

            SiteDTO updated = new SiteDTO(
                    dto.id(),
                    naamField.getText().trim(),
                    locatieField.getText().trim(),
                    Integer.parseInt(capaciteitField.getText().trim()),
                    Status.valueOf(operationeleStatusCombo.getValue()),
                    ProductieStatus.valueOf(productieStatusCombo.getValue())
            );

            sc.updateSite(updated);

            toonInfo("Wijzigingen opgeslagen.");
            sluitScherm();

        } catch (Exception e) {

            toonFoutmelding(e.getMessage());
        }
    }

    @FXML
    private void handleVerwijder() {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Site verwijderen");
        confirm.setHeaderText("Deze site permanent verwijderen?");
        confirm.setContentText("Deze actie kan niet ongedaan worden.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            sc.verwijderSite(dto.id());

            toonInfo("Site succesvol verwijderd.");
            sluitScherm();
        }
    }

    private boolean valideerInput() {

        if (naamField.getText() == null || naamField.getText().isBlank()) {
            toonFoutmelding("Naam is verplicht.");
            return false;
        }

        if (locatieField.getText() == null || locatieField.getText().isBlank()) {
            toonFoutmelding("Locatie is verplicht.");
            return false;
        }

        try {

            int capaciteit = Integer.parseInt(capaciteitField.getText());

            if (capaciteit <= 0) {
                toonFoutmelding("Capaciteit moet groter zijn dan 0.");
                return false;
            }

        } catch (NumberFormatException e) {

            toonFoutmelding("Capaciteit moet een getal zijn.");
            return false;
        }

        if (operationeleStatusCombo.getValue() == null) {
            toonFoutmelding("Operationele status is verplicht.");
            return false;
        }

        if (productieStatusCombo.getValue() == null) {
            toonFoutmelding("Productie status is verplicht.");
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

        Stage stage = (Stage) naamField.getScene().getWindow();
        stage.close();
    }
}