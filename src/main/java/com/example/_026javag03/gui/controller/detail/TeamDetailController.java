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

    private final ObservableList<GebruikerDTO> werknemers =
            FXCollections.observableArrayList();

    private final ObservableList<GebruikerDTO> beschikbareWerknemers =
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

    @FXML private ListView<GebruikerDTO> werknemersList;
    @FXML private ComboBox<GebruikerDTO> werknemerCombo;
    @FXML private Button btnToevoegen;
    @FXML private Button btnVerwijderen;

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
        beschikbareWerknemers.addAll(
                gc.getGebruikers()
                        .stream()
                        .filter(g -> g.rol() == Rol.WERKNEMER)
                        .toList()
        );

        // bestaande werknemers
        if (dto.werknemers() != null) {

            dto.werknemers().forEach(id -> {

                gc.getGebruikers()
                        .stream()
                        .filter(g -> g.id().equals(id))
                        .findFirst()
                        .ifPresent(g -> {
                            werknemers.add(g);
                            beschikbareWerknemers.remove(g);
                        });
            });
        }

        Comparator<GebruikerDTO> sorteer =
                Comparator.comparing(g -> g.voornaam() + g.naam());

        werknemers.sort(sorteer);
        beschikbareWerknemers.sort(sorteer);

        werknemersList.setItems(werknemers);

        werknemersList.setCellFactory(list -> new ListCell<>() {

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

        werknemerCombo.setItems(beschikbareWerknemers);

        werknemerCombo.setCellFactory(list -> new ListCell<>() {

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

        werknemerCombo.setButtonCell(werknemerCombo.getCellFactory().call(null));

        // Toevoegen knop
        werknemerCombo.valueProperty().addListener((obs, oldV, newV) -> {
            btnToevoegen.setDisable(newV == null);
        });

        // Verwijderen knop
        werknemersList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            btnVerwijderen.setDisable(newV == null);
        });

        btnToevoegen.setDisable(true);
        btnVerwijderen.setDisable(true);
    }

    @FXML
    private void handleToevoegen() {

        GebruikerDTO geselecteerd = werknemerCombo.getValue();

        if (geselecteerd != null) {

            werknemers.add(geselecteerd);
            beschikbareWerknemers.remove(geselecteerd);

            werknemerCombo.setValue(null);
        }
    }

    @FXML
    private void handleVerwijderen() {

        GebruikerDTO geselecteerd =
                werknemersList.getSelectionModel().getSelectedItem();

        if (geselecteerd != null) {

            werknemers.remove(geselecteerd);
            beschikbareWerknemers.add(geselecteerd);
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

            List<Long> ids = werknemers.stream()
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