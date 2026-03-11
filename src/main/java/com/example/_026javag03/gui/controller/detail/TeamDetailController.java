package com.example._026javag03.gui.controller.detail;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.domein.controller.TeamController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.dto.TeamDTO;
import com.example._026javag03.util.gebruiker.Rol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeamDetailController {

    private final TeamController tc;
    private final SiteController sc;
    private final GebruikerController gc;
    private final TeamDTO dto;

    private final ObservableList<GebruikerDTO> medewerkers =
            FXCollections.observableArrayList();

    private final ObservableList<GebruikerDTO> beschikbareMedewerkers =
            FXCollections.observableArrayList();

    public TeamDetailController(
            TeamController tc,
            SiteController sc,
            GebruikerController gc,
            TeamDTO dto
    ) {
        this.tc = tc;
        this.sc = sc;
        this.gc = gc;
        this.dto = dto;
    }

    @FXML private TextField codeField;

    @FXML private ComboBox<SiteDTO> siteBox;
    @FXML private ComboBox<GebruikerDTO> verantwoordelijkeBox;

    @FXML private ListView<GebruikerDTO> medewerkersList;
    @FXML private ComboBox<GebruikerDTO> medewerkerCombo;

    @FXML
    private void initialize() {

        // code (niet wijzigbaar)
        codeField.setText(dto.teamCode());
        codeField.setDisable(true);

        // sites laden
        var sites = sc.getSites();
        siteBox.getItems().setAll(sites);

        sites.stream()
                .filter(s -> s.id().equals(dto.site()))
                .findFirst()
                .ifPresent(siteBox::setValue);

        // verantwoordelijken laden
        var verantwoordelijken = gc.getVerantwoordelijken();
        verantwoordelijkeBox.getItems().setAll(verantwoordelijken);

        verantwoordelijken.stream()
                .filter(v -> v.id().equals(dto.verantwoordelijke()))
                .findFirst()
                .ifPresent(verantwoordelijkeBox::setValue);

        // alle werknemers laden
        beschikbareMedewerkers.addAll(
                gc.getGebruikers()
                        .stream()
                        .filter(g -> g.rol() == Rol.WERKNEMER)
                        .toList()
        );

        // bestaande medewerkers
        if (dto.werknemers() != null) {

            dto.werknemers().forEach(id -> {

                gc.getGebruikers()
                        .stream()
                        .filter(g -> g.id().equals(id))
                        .findFirst()
                        .ifPresent(g -> {
                            medewerkers.add(g);
                            beschikbareMedewerkers.remove(g);
                        });
            });
        }

        Comparator<GebruikerDTO> sorteer =
                Comparator.comparing(g -> g.voornaam() + g.naam());

        medewerkers.sort(sorteer);
        beschikbareMedewerkers.sort(sorteer);

        medewerkersList.setItems(medewerkers);

        medewerkersList.setCellFactory(list -> new ListCell<>() {

            @Override
            protected void updateItem(GebruikerDTO item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.voornaam() + " " + item.naam());
                }
            }
        });

        medewerkerCombo.setItems(beschikbareMedewerkers);

        medewerkerCombo.setCellFactory(list -> new ListCell<>() {

            @Override
            protected void updateItem(GebruikerDTO item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.voornaam() + " " + item.naam());
                }
            }
        });

        medewerkerCombo.setButtonCell(medewerkerCombo.getCellFactory().call(null));
    }

    @FXML
    private void handleToevoegen() {

        GebruikerDTO geselecteerd = medewerkerCombo.getValue();

        if (geselecteerd != null) {

            medewerkers.add(geselecteerd);
            beschikbareMedewerkers.remove(geselecteerd);

            medewerkerCombo.setValue(null);
        }
    }

    @FXML
    private void handleVerwijderen() {

        GebruikerDTO geselecteerd =
                medewerkersList.getSelectionModel().getSelectedItem();

        if (geselecteerd != null) {

            medewerkers.remove(geselecteerd);
            beschikbareMedewerkers.add(geselecteerd);
        }
    }

    @FXML
    private void handleVerwijderTeam() {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Team verwijderen");
        confirm.setHeaderText("Dit team permanent verwijderen?");
        confirm.setContentText("Deze actie kan niet ongedaan worden.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            tc.verwijderTeam(dto.id());

            sluitScherm();
        }
    }

    @FXML
    private void handleOpslaan() {

        try {

            SiteDTO site = siteBox.getValue();
            GebruikerDTO verantwoordelijke = verantwoordelijkeBox.getValue();

            if (site == null || verantwoordelijke == null) {
                toonFoutmelding("Site en verantwoordelijke zijn verplicht.");
                return;
            }

            List<Long> ids = medewerkers.stream()
                    .map(GebruikerDTO::id)
                    .collect(Collectors.toList());

            TeamDTO updated = new TeamDTO(
                    dto.id(),
                    site.id(),
                    dto.teamCode(),
                    verantwoordelijke.id(),
                    ids
            );

            tc.updateTeam(updated);

            sluitScherm();

        } catch (Exception e) {

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

    private void sluitScherm() {

        Stage stage = (Stage) codeField.getScene().getWindow();
        stage.close();
    }
}