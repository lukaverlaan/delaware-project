package com.example._026javag03.gui.controller.lijst;

import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.gui.controller.detail.SiteDetailController;
import com.example._026javag03.gui.controller.toevoegen.VoegSiteToeController;
import com.example._026javag03.gui.observable.ObservableSite;
import com.example._026javag03.gui.observable.beheer.ObservableSiteBeheer;
import com.example._026javag03.gui.weergave.ViewManager;
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

public class SiteLijstController {

    private final SiteController sc;
    private final ViewManager viewManager;

    private ObservableSiteBeheer observableBeheer;

    public SiteLijstController(SiteController sc, ViewManager viewManager) {
        this.sc = sc;
        this.viewManager = viewManager;
    }

    @FXML private TextField txtFilter;

    @FXML private CheckBox actiefCheckBox;
    @FXML private CheckBox inactiefCheckBox;

    @FXML private ComboBox<String> productieStatusFilterComboBox;

    @FXML private TableView<ObservableSite> tblvSites;

    @FXML private TableColumn<ObservableSite,String> tblcNaam;
    @FXML private TableColumn<ObservableSite,String> tblcLocatie;
    @FXML private TableColumn<ObservableSite,Integer> tblcCapaciteit;
    @FXML private TableColumn<ObservableSite,String> tblcOperationeleStatus;
    @FXML private TableColumn<ObservableSite,String> tblcProductieStatus;

    @FXML private Button btnNieuweSite;

    @FXML
    private void initialize() {

        productieStatusFilterComboBox.getItems().addAll(
                "ALLE",
                "GEZOND",
                "PROBLEMEN",
                "OFFLINE"
        );

        productieStatusFilterComboBox.setValue("ALLE");

        actiefCheckBox.selectedProperty()
                .addListener((obs,o,n) -> updateFilter());

        inactiefCheckBox.selectedProperty()
                .addListener((obs,o,n) -> updateFilter());

        productieStatusFilterComboBox.valueProperty()
                .addListener((obs,o,n) -> updateFilter());

        txtFilter.textProperty()
                .addListener((obs,o,n) -> updateFilter());

        observableBeheer = new ObservableSiteBeheer(sc);

        tblcNaam.setCellValueFactory(cell -> cell.getValue().naamProperty());

        tblcLocatie.setCellValueFactory(cell -> cell.getValue().locatieProperty());

        tblcCapaciteit.setCellValueFactory(cell ->
                cell.getValue().capaciteitProperty().asObject());

        tblcOperationeleStatus.setCellValueFactory(cell ->
                cell.getValue().operationeleStatusProperty());

        tblcProductieStatus.setCellValueFactory(cell ->
                cell.getValue().productieStatusProperty());

        SortedList<ObservableSite> sorted =
                new SortedList<>(observableBeheer.getFilteredSites());

        sorted.comparatorProperty()
                .bind(tblvSites.comparatorProperty());

        tblvSites.setItems(sorted);

        btnNieuweSite.setOnAction(e -> openNieuweSite());

        // Dubbelklik voor detail
        tblvSites.setRowFactory(tv -> {
            TableRow<ObservableSite> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    openDetailScherm(row.getItem());
                }
            });
            return row;
        });
    }

    private void openNieuweSite() {

        viewManager.showView(
                new VoegSiteToeController(sc, viewManager).getView()
        );
    }

    private void openDetailScherm(ObservableSite observableSite) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/SiteDetailScherm.fxml")
            );

            loader.setController(
                    new SiteDetailController(sc, observableSite.getDto())
            );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/delaware-favicon.png"))
            );

            stage.setTitle("Site details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Na sluiten detailscherm → refresh lijst
            observableBeheer.refresh();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateFilter() {

        String zoektekst = txtFilter.getText();
        boolean toonActief = actiefCheckBox.isSelected();
        boolean toonInactief = inactiefCheckBox.isSelected();
        String productieFilter = productieStatusFilterComboBox.getValue();

        observableBeheer.getFilteredSites().setPredicate(site -> {

            boolean tekstMatch = true;

            if (zoektekst != null && !zoektekst.isBlank()) {

                String lower = zoektekst.toLowerCase();

                tekstMatch =
                        contains(site.getNaam(), lower) ||
                                contains(site.getLocatie(), lower) ||
                                String.valueOf(site.getCapaciteit()).contains(lower) ||
                                contains(site.getProductieStatus(), lower) ||
                                contains(site.getOperationeleStatus(), lower);
            }

            boolean statusMatch =
                    (!toonActief && !toonInactief) ||
                            (toonActief && site.getOperationeleStatus().equals("ACTIEF")) ||
                            (toonInactief && site.getOperationeleStatus().equals("INACTIEF"));

            boolean productieMatch =
                    productieFilter.equals("ALLE") ||
                            site.getProductieStatus().equals(productieFilter);

            return tekstMatch && statusMatch && productieMatch;

        });
    }

    private boolean contains(String value, String filter) {

        return value != null &&
                value.toLowerCase().contains(filter);
    }

    public Parent getView() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/_026javag03/gui/SiteLijstScherm.fxml")
            );

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}