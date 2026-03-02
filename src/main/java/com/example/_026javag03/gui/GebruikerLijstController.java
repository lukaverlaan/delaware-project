package com.example._026javag03.gui;

import com.example._026javag03.domein.GebruikerController;
import com.example._026javag03.gui.observable.ObservableGebruiker;
import com.example._026javag03.gui.observable.ObservableGebruikerBeheer;
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

import static com.sun.javafx.util.Utils.contains;

public class GebruikerLijstController {

    private final GebruikerController dc;
    private final ViewManager viewManager;

    private ObservableGebruikerBeheer observableBeheer;

    public GebruikerLijstController(GebruikerController dc, ViewManager viewManager) {
        this.dc = dc;
        this.viewManager = viewManager;
    }

    @FXML private TextField txtFilter;
    @FXML private CheckBox actiefCheckBox;
    @FXML private CheckBox inactiefCheckBox;
    @FXML private ComboBox<String> rolFilterComboBox;

    @FXML private TableView<ObservableGebruiker> tblvGebruikers;
    @FXML private TableColumn<ObservableGebruiker, String> tblcRol;
    @FXML private TableColumn<ObservableGebruiker, String> tblcStatus;
    @FXML private TableColumn<ObservableGebruiker, String> tblcVoornaam;
    @FXML private TableColumn<ObservableGebruiker, String> tblcNaam;
    @FXML private TableColumn<ObservableGebruiker, String> tblcEmail;
    @FXML private TableColumn<ObservableGebruiker, String> tblcGsm;

    @FXML
    private void initialize() {
        rolFilterComboBox.getItems().addAll(
                "ALLE",
                "ADMINISTRATOR",
                "MANAGER",
                "VERANTWOORDELIJKE",
                "WERKNEMER"
        );

        rolFilterComboBox.setValue("ALLE");

        // Listeners toevoegen
        txtFilter.textProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        actiefCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        inactiefCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        rolFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter());

        observableBeheer = new ObservableGebruikerBeheer(dc);

        tblcRol.setCellValueFactory(cell ->
                cell.getValue().rolProperty());

        tblcStatus.setCellValueFactory(cell ->
                cell.getValue().statusProperty());

        tblcVoornaam.setCellValueFactory(cell ->
                cell.getValue().voornaamProperty());

        tblcNaam.setCellValueFactory(cell ->
                cell.getValue().naamProperty());

        tblcEmail.setCellValueFactory(cell ->
                cell.getValue().emailProperty());

        tblcGsm.setCellValueFactory(cell ->
                cell.getValue().gsmProperty());

        SortedList<ObservableGebruiker> sorted =
                new SortedList<>(observableBeheer.getFilteredGebruikers());

        sorted.comparatorProperty()
                .bind(tblvGebruikers.comparatorProperty());

        tblvGebruikers.setItems(sorted);

        txtFilter.textProperty().addListener((obs, oldVal, newVal) ->
                observableBeheer.changeFilter(newVal)
        );

        // Dubbelklik voor detail
        tblvGebruikers.setRowFactory(tv -> {
            TableRow<ObservableGebruiker> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    openDetailScherm(row.getItem());
                }
            });
            return row;
        });
    }

    private void openDetailScherm(ObservableGebruiker observableGebruiker) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/GebruikerDetailScherm.fxml")
            );

            loader.setController(
                    new GebruikerDetailController(dc, observableGebruiker.getDto())
            );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/delaware-favicon.png"))
            );

            stage.setTitle("Gebruiker details");
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
        String geselecteerdeRol = rolFilterComboBox.getValue();

        observableBeheer.getFilteredGebruikers().setPredicate(g -> {

            boolean tekstMatch = true;

            if (zoektekst != null && !zoektekst.isBlank()) {
                String lower = zoektekst.toLowerCase();

                tekstMatch =
                        contains(g.getNaam(), lower)
                                || contains(g.getVoornaam(), lower)
                                || contains(g.getEmail(), lower)
                                || contains(g.getGsm(), lower)
                                || contains(g.getRol(), lower)
                                || contains(g.getStatus(), lower)
                                || contains(g.getPersoneelsnummer(), lower)
                                || contains(g.getDto().stad(), lower);
            }

            boolean statusMatch =
                    (toonActief && g.getStatus().equals("ACTIEF"))
                            || (toonInactief && g.getStatus().equals("INACTIEF"));

            // 👤 Rolfilter
            boolean rolMatch =
                    geselecteerdeRol.equals("ALLE")
                            || g.getRol().equals(geselecteerdeRol);

            return tekstMatch && statusMatch && rolMatch;
        });
    }

    public Parent getView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/GebruikersLijstScherm.fxml")
            );
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}