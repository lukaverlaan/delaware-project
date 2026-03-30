package com.example._026javag03.gui.controller.lijst;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.gui.controller.detail.MachineDetailController;
import com.example._026javag03.gui.controller.toevoegen.VoegMachineToeController;
import com.example._026javag03.gui.observable.ObservableMachine;
import com.example._026javag03.gui.observable.beheer.ObservableMachineBeheer;
import com.example._026javag03.gui.weergave.ViewManager;
import com.example._026javag03.util.machine.StatusMachine;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MachineLijstController {

    private final ViewManager viewManager;
    private final MachineController mc;
    private final SiteController sc;
    private final GebruikerController gc;

    private ObservableMachineBeheer observableBeheer;

    public MachineLijstController(MachineController mc, SiteController sc, GebruikerController gc, ViewManager viewManager) {
        this.mc = mc;
        this.sc = sc;
        this.gc = gc;
        this.viewManager = viewManager;
    }

    @FXML private TextField txtFilter;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private ComboBox<SiteDTO> siteFilterComboBox;
    @FXML private TableView<ObservableMachine> tblvMachines;
    @FXML private Button btnNieuweMachine;

    @FXML private TableColumn<ObservableMachine,String> tblcStatus;
    @FXML private TableColumn<ObservableMachine, String> tblcProductInfo;
    @FXML private TableColumn<ObservableMachine, String> tblcSite;
    @FXML private TableColumn<ObservableMachine, String> tblcOnderhoud;
    @FXML private TableColumn<ObservableMachine, String> tblcUptime;

    @FXML
    private void initialize() {

        observableBeheer = new ObservableMachineBeheer(mc, sc);

        // 🔎 filters
        txtFilter.textProperty().addListener((obs, o, n) -> updateFilter());

        statusFilterComboBox.getItems().add("ALLE");
        for (StatusMachine s : StatusMachine.values()) {
            statusFilterComboBox.getItems().add(s.name());
        }
        statusFilterComboBox.setValue("ALLE");
        statusFilterComboBox.valueProperty().addListener((obs, o, n) -> updateFilter());

        siteFilterComboBox.getItems().add(null); // null = alle
        siteFilterComboBox.getItems().addAll(sc.getSites());
        siteFilterComboBox.valueProperty().addListener((obs, o, n) -> updateFilter());

        siteFilterComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(SiteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "ALLE" : item.naam());
            }
        });
        siteFilterComboBox.setButtonCell(siteFilterComboBox.getCellFactory().call(null));

        // tabel
        SortedList<ObservableMachine> sorted =
                new SortedList<>(observableBeheer.getFilteredMachines());

        sorted.comparatorProperty().bind(tblvMachines.comparatorProperty());
        tblvMachines.setItems(sorted);

        tblcStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        tblcProductInfo.setCellValueFactory(cell -> cell.getValue().productInfoProperty());
        tblcSite.setCellValueFactory(cell -> cell.getValue().siteProperty());
        tblcOnderhoud.setCellValueFactory(cell -> cell.getValue().onderhoudProperty());
        tblcUptime.setCellValueFactory(cell -> cell.getValue().uptimeProperty());

        // knop nieuwe machine
        btnNieuweMachine.setOnAction(e ->
                viewManager.showView(new VoegMachineToeController(mc, sc, gc, viewManager).getView())
        );

        // dubbelklik detail
        tblvMachines.setRowFactory(tv -> {
            TableRow<ObservableMachine> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    openDetailScherm(row.getItem());
                }
            });
            return row;
        });
    }

    private void updateFilter() {

        String filter = txtFilter.getText();
        String statusFilter = statusFilterComboBox.getValue();
        SiteDTO siteFilter = siteFilterComboBox.getValue();

        observableBeheer.getFilteredMachines().setPredicate(m -> {

            boolean matchesText = true;

            if (filter != null && !filter.isBlank()) {
                String lower = filter.toLowerCase();

                matchesText =
                        m.statusProperty().get().toLowerCase().contains(lower)
                                || m.productInfoProperty().get().toLowerCase().contains(lower)
                                || m.siteProperty().get().toLowerCase().contains(lower);
            }

            boolean matchesStatus = true;

            if (statusFilter != null && !statusFilter.equals("ALLE")) {
                matchesStatus = m.statusProperty().get().equalsIgnoreCase(statusFilter);
            }

            boolean matchesSite = true;

            if (siteFilter != null) {
                matchesSite = m.siteProperty().get().equalsIgnoreCase(siteFilter.naam());
            }

            return matchesText && matchesStatus && matchesSite;
        });
    }

    private void openDetailScherm(ObservableMachine observableMachine) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/MachineDetailScherm.fxml")
            );

            loader.setController(
                    new MachineDetailController(mc, sc, gc, observableMachine.getDto())
            );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/delaware-favicon.png"))
            );

            stage.setTitle("Machine details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            observableBeheer.refresh();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Parent getView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/MachineLijstScherm.fxml")
            );
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}