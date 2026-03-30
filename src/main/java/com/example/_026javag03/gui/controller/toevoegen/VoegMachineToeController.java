package com.example._026javag03.gui.controller.toevoegen;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.exceptions.MachineException;
import com.example._026javag03.gui.controller.lijst.MachineLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.machine.ProductieStatusMachine;
import com.example._026javag03.util.machine.StatusMachine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VoegMachineToeController {

    private final ViewManager viewManager;
    private final MachineController mc;
    private final SiteController sc;
    private final GebruikerController gc;

    private final ObservableList<GebruikerDTO> werknemers =
            FXCollections.observableArrayList();

    private final ObservableList<GebruikerDTO> beschikbareWerknemers =
            FXCollections.observableArrayList();

    public VoegMachineToeController(
            MachineController mc,
            SiteController sc,
            GebruikerController gc,
            ViewManager viewManager
    ) {
        this.viewManager = viewManager;
        this.mc = mc;
        this.sc = sc;
        this.gc = gc;
    }

    @FXML private ComboBox<SiteDTO> comboBoxSite;
    @FXML private ComboBox<ProductieStatusMachine> comboBoxProdStatus;
    @FXML private ComboBox<StatusMachine> comboBoxStatus;

    @FXML private TextField txtLocatie;
    @FXML private TextArea txtProdInfo;

    @FXML private ListView<GebruikerDTO> werknemersList;
    @FXML private ComboBox<GebruikerDTO> werknemerCombo;
    @FXML private Button btnToevoegen;
    @FXML private Button btnVerwijderen;

    @FXML
    public void initialize(){

        comboBoxStatus.getItems().addAll(StatusMachine.values());
        comboBoxProdStatus.getItems().addAll(ProductieStatusMachine.values());

        comboBoxStatus.setValue(StatusMachine.GESTOPT_AUTO);
        comboBoxProdStatus.setValue(ProductieStatusMachine.OFFLINE);

        // placeholder
        txtLocatie.setPromptText("bv. Verdiep 1, lokaal 103");

        // sites
        comboBoxSite.getItems().setAll(sc.getSites());

        comboBoxSite.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(SiteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.naam());
            }
        });

        comboBoxSite.setButtonCell(comboBoxSite.getCellFactory().call(null));

        // werknemers laden
        beschikbareWerknemers.addAll(
                gc.getGebruikers()
                        .stream()
                        .filter(g -> g.rol() == Rol.WERKNEMER)
                        .toList()
        );

        Comparator<GebruikerDTO> sorteer =
                Comparator.comparing(g -> g.voornaam() + g.naam());

        beschikbareWerknemers.sort(sorteer);

        werknemersList.setItems(werknemers);
        werknemerCombo.setItems(beschikbareWerknemers);

        // weergave
        werknemersList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(GebruikerDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.voornaam() + " " + item.naam());
            }
        });

        werknemerCombo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(GebruikerDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.voornaam() + " " + item.naam());
            }
        });

        werknemerCombo.setButtonCell(
                werknemerCombo.getCellFactory().call(null)
        );

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
    private void handleMaak() {

        try {

            if (comboBoxSite.getValue() == null) {
                throw new IllegalArgumentException("Site is verplicht");
            }

            if (txtLocatie.getText() == null || txtLocatie.getText().isBlank()) {
                throw new IllegalArgumentException("Locatie is verplicht");
            }

            if (comboBoxStatus.getValue() == null) {
                throw new IllegalArgumentException("Status is verplicht");
            }

            if (comboBoxProdStatus.getValue() == null) {
                throw new IllegalArgumentException("Productie status is verplicht");
            }

            if (txtProdInfo.getText() == null || txtProdInfo.getText().isBlank()) {
                throw new IllegalArgumentException("Productie info is verplicht");
            }

            List<Long> ids = werknemers.stream()
                    .map(GebruikerDTO::id)
                    .collect(Collectors.toList());

            mc.voegMachineToe(
                    comboBoxSite.getValue().id(),
                    txtLocatie.getText(),
                    txtProdInfo.getText(),
                    comboBoxStatus.getValue().name(),
                    comboBoxProdStatus.getValue().name(),
                    null,
                    ids
            );

            handleTerug();

        } catch (IllegalArgumentException | MachineException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleTerug() {
        viewManager.showView(
                new MachineLijstController(mc, sc, gc, viewManager).getView()
        );
    }

    public Parent getView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/VoegMachineToeScherm.fxml")
            );
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}