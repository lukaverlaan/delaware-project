package com.example._026javag03.gui.controller.toevoegen;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.domein.controller.TeamController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.exceptions.TeamException;
import com.example._026javag03.gui.controller.lijst.TeamLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;

public class VoegTeamToeController {
    private final TeamController tc;
    private final SiteController sc;
    private final GebruikerController gc;
    private final ViewManager viewManager;

    public VoegTeamToeController(TeamController tc, SiteController sc, GebruikerController gc, ViewManager viewManager) {
        this.tc = tc;
        this.sc = sc;
        this.gc = gc;
        this.viewManager = viewManager;
    }

    @FXML private ComboBox<SiteDTO> siteBox;
    @FXML private ComboBox<GebruikerDTO> verantwoordelijkeBox;
    @FXML
    private Label lblInfo;

    @FXML
    private Button btnToevoegen;

    @FXML
    public void initialize() {

        var sites = sc.getSites();
        var verantwoordelijken = gc.getVerantwoordelijken();

        siteBox.getItems().setAll(sites);
        verantwoordelijkeBox.getItems().setAll(verantwoordelijken);

        boolean geenSites = sites.isEmpty();
        boolean geenVerantw = verantwoordelijken.isEmpty();

        if (geenSites) {
            siteBox.setPromptText("Er zijn nog geen sites beschikbaar");
            siteBox.setDisable(true);
        }

        if (geenVerantw) {
            verantwoordelijkeBox.setPromptText("Er zijn geen verantwoordelijken beschikbaar");
            verantwoordelijkeBox.setDisable(true);
        }

        if (geenSites || geenVerantw) {

            lblInfo.setText(
                    "Je kan nog geen team aanmaken. Voeg eerst een site en een verantwoordelijke toe."
            );

            btnToevoegen.setDisable(true);
        }
    }

    @FXML
    private void handleTerug() {

        viewManager.showView(
                new TeamLijstController(tc, sc, gc, viewManager).getView()
        );
    }

    @FXML
    private void handleToevoegen() {

        SiteDTO site = siteBox.getValue();
        GebruikerDTO verantwoordelijke = verantwoordelijkeBox.getValue();

        if (site == null || verantwoordelijke == null) {
            return;
        }

        try {

            tc.voegTeamToe(site.id(), verantwoordelijke.id());

            handleTerug();

        } catch (TeamException e) {
            toonFoutmelding(e.getMessage());
        }
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
                    getClass().getResource("/com/example/_026javag03/gui/VoegTeamToeScherm.fxml")
            );

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException("Kan FXML niet laden");
        }
    }
}
