package com.example._026javag03.gui.controller.detail;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.dto.MachineDTO;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.exceptions.MachineException;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.machine.ProductieStatusMachine;
import com.example._026javag03.util.machine.StatusMachine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MachineDetailController {

    private final MachineController mc;
    private final SiteController sc;
    private final GebruikerController gc;
    private final MachineDTO dto;

    private final ObservableList<GebruikerDTO> werknemers =
            FXCollections.observableArrayList();

    private final ObservableList<GebruikerDTO> beschikbareWerknemers =
            FXCollections.observableArrayList();

    private Timeline uptimeTimer;
    private int elapsedSeconds = 0;
    private boolean running = false;

    public MachineDetailController(
            MachineController mc,
            SiteController sc,
            GebruikerController gc,
            MachineDTO dto
    ) {
        this.mc = mc;
        this.sc = sc;
        this.gc = gc;
        this.dto = dto;
    }

    @FXML private TextField codeField;
    @FXML private ComboBox<SiteDTO> siteCombo;

    @FXML private TextField locatieField;
    @FXML private TextArea productInfoArea;
    @FXML private TextField uptimeField;
    @FXML private DatePicker onderhoudPicker;

    @FXML private ComboBox<StatusMachine> statusCombo;
    @FXML private ComboBox<ProductieStatusMachine> productieStatusCombo;

    @FXML private ListView<GebruikerDTO> werknemersList;
    @FXML private ComboBox<GebruikerDTO> werknemerCombo;
    @FXML private Button btnToevoegen;
    @FXML private Button btnVerwijderen;

    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button resetButton;

    @FXML
    private void initialize() {
        codeField.setDisable(true);

        // enums
        statusCombo.getItems().addAll(StatusMachine.values());
        productieStatusCombo.getItems().addAll(ProductieStatusMachine.values());

        // sites
        siteCombo.getItems().setAll(sc.getSites());

        siteCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(SiteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.naam());
            }
        });
        siteCombo.setButtonCell(siteCombo.getCellFactory().call(null));

        // waarden
        codeField.setText(String.valueOf(dto.code()));
        codeField.setEditable(false);

        locatieField.setText(dto.locatie());
        productInfoArea.setText(dto.productinfo());

        statusCombo.setValue(dto.status());
        productieStatusCombo.setValue(dto.productieStatus());

        if (dto.laatsteOnderhoud() != null) {
            onderhoudPicker.setValue(dto.laatsteOnderhoud());
        }

        siteCombo.getItems().stream()
                .filter(s -> s.id().equals(dto.siteId()))
                .findFirst()
                .ifPresent(siteCombo::setValue);

        uptimeField.setEditable(false);

        if (dto.uptime() != null && !dto.uptime().isBlank() && dto.uptime().contains(":")) {
            String[] parts = dto.uptime().split(":");
            if (parts.length == 3) {
                elapsedSeconds =
                        Integer.parseInt(parts[0]) * 3600 +
                                Integer.parseInt(parts[1]) * 60 +
                                Integer.parseInt(parts[2]);
            }
        }

        if (statusCombo.getValue() == null) {
            statusCombo.setValue(StatusMachine.GESTOPT_AUTO);
        }

        if (productieStatusCombo.getValue() == null) {
            productieStatusCombo.setValue(ProductieStatusMachine.OFFLINE);
        }

        uptimeTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    elapsedSeconds++;
                    updateUptimeField();
                    updateButtons();
                })
        );
        uptimeTimer.setCycleCount(Timeline.INDEFINITE);

        updateUptimeField();
        updateButtons();

        // werknemers
        beschikbareWerknemers.addAll(
                gc.getGebruikers()
                        .stream()
                        .filter(g -> g.rol() == Rol.WERKNEMER)
                        .toList()
        );

        if (dto.werknemerIds() != null) {
            dto.werknemerIds().forEach(id -> {
                gc.getGebruikers().stream()
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
        werknemerCombo.setItems(beschikbareWerknemers);

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

        statusCombo.valueProperty().addListener((obs, oldV, newV) -> {
            updateButtons();
        });
    }

    private void updateButtons() {

        boolean onderhoud = statusCombo.getValue() == StatusMachine.ONDERHOUD_NODIG;

        startButton.setDisable(running || onderhoud);
        resetButton.setDisable(running);
        stopButton.setDisable(!running);
        resetButton.setVisible(elapsedSeconds > 0);
    }

    private void updateUptimeField() {
        int hours = elapsedSeconds / 3600;
        int minutes = (elapsedSeconds % 3600) / 60;
        int seconds = elapsedSeconds % 60;

        uptimeField.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    @FXML
    private void handleStartUptime() {

        if (statusCombo.getValue() == StatusMachine.ONDERHOUD_NODIG) {
            toonFoutmelding("Machine heeft onderhoud nodig en kan niet gestart worden.");
            return;
        }

        uptimeTimer.play();
        running = true;
        statusCombo.setValue(StatusMachine.DRAAIT);
        productieStatusCombo.setValue(ProductieStatusMachine.GEZOND);
        updateButtons();
    }

    @FXML
    private void handleStopUptime() {
        uptimeTimer.stop();
        running = false;
        statusCombo.setValue(StatusMachine.GESTOPT_MANUEEL);
        productieStatusCombo.setValue(ProductieStatusMachine.OFFLINE);
        updateButtons();
    }

    @FXML
    private void handleResetUptime() {
        uptimeTimer.stop();
        running = false;
        elapsedSeconds = 0;
        statusCombo.setValue(StatusMachine.GESTOPT_AUTO);
        productieStatusCombo.setValue(ProductieStatusMachine.OFFLINE);
        updateUptimeField();
        updateButtons();
    }

    // bestaande logica

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
    private void handleOpslaan() {

        if (!valideerInput()) return;

        try {

            List<Long> ids = werknemers.stream()
                    .map(GebruikerDTO::id)
                    .collect(Collectors.toList());

            MachineDTO updated = new MachineDTO(
                    dto.id(),
                    dto.code(),
                    siteCombo.getValue().id(),
                    locatieField.getText().trim(),
                    productInfoArea.getText().trim(),
                    statusCombo.getValue(),
                    productieStatusCombo.getValue(),
                    uptimeField.getText(),
                    getOnderhoud(),
                    ids
            );

            mc.updateMachine(updated);
            sluitScherm();

        } catch (MachineException e) {
            toonFoutmelding(e.getMessage());
        }
    }

    @FXML
    private void handleVerwijder() {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Machine verwijderen");
        confirm.setHeaderText("Deze machine permanent verwijderen?");
        confirm.setContentText("Deze actie kan niet ongedaan worden.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            mc.verwijderMachine(dto.id());

            toonInfo("Machine succesvol verwijderd.");
            sluitScherm();
        }
    }

    private boolean valideerInput() {

        if (siteCombo.getValue() == null) {
            toonFoutmelding("Site is verplicht.");
            return false;
        }

        if (isLeeg(locatieField)) {
            toonFoutmelding("Locatie is verplicht.");
            return false;
        }

        if (isLeeg(productInfoArea)) {
            toonFoutmelding("Product info is verplicht.");
            return false;
        }

        if (statusCombo.getValue() == null) {
            toonFoutmelding("Status is verplicht.");
            return false;
        }

        if (productieStatusCombo.getValue() == null) {
            toonFoutmelding("Productiestatus is verplicht.");
            return false;
        }

        return true;
    }

    private boolean isLeeg(TextInputControl field) {
        return field.getText() == null || field.getText().isBlank();
    }

    private LocalDate getOnderhoud() {
        return onderhoudPicker.getValue();
    }

    private void toonFoutmelding(String boodschap) {
        new Alert(Alert.AlertType.ERROR, boodschap).showAndWait();
    }

    private void toonInfo(String boodschap) {
        new Alert(Alert.AlertType.INFORMATION, boodschap).showAndWait();
    }

    private void sluitScherm() {
        Stage stage = (Stage) locatieField.getScene().getWindow();
        stage.close();
    }
}