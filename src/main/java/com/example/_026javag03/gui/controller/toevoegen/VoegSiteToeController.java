package com.example._026javag03.gui.controller.toevoegen;

import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.gui.controller.lijst.SiteLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import com.example._026javag03.util.site.ProductieStatus;
import com.example._026javag03.util.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class VoegSiteToeController {

    private final SiteController sc;
    private final ViewManager viewManager;

    public VoegSiteToeController(SiteController sc, ViewManager viewManager) {
        this.sc = sc;
        this.viewManager = viewManager;
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

        operationeleStatusCombo.setValue("ACTIEF");
        productieStatusCombo.setValue("OPERATIONEEL");
    }

    @FXML
    private void handleToevoegen() {

        if (!valideerInput()) return;

        try {

            SiteDTO dto = new SiteDTO(
                    null,
                    naamField.getText().trim(),
                    locatieField.getText().trim(),
                    Integer.parseInt(capaciteitField.getText().trim()),
                    Status.valueOf(operationeleStatusCombo.getValue()),
                    ProductieStatus.valueOf(productieStatusCombo.getValue())
            );

            sc.insertSite(dto);

            toonInfo("Site succesvol toegevoegd.");
            resetVelden();

        } catch (Exception e) {

            toonFoutmelding(e.getMessage());
        }
    }

    @FXML
    private void handleTerug() {

        viewManager.showView(
                new SiteLijstController(sc, viewManager).getView()
        );
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

    private void resetVelden() {

        naamField.clear();
        locatieField.clear();
        capaciteitField.clear();

        operationeleStatusCombo.setValue("ACTIEF");
        productieStatusCombo.setValue("OPERATIONEEL");
    }

    public Parent getView() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/VoegSiteToeScherm.fxml")
            );

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException("Kan FXML niet laden");
        }
    }
}