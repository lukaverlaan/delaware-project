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

public class GebruikerLijstController {

    private final GebruikerController dc;
    private final ViewManager viewManager;

    private ObservableGebruikerBeheer observableBeheer;

    public GebruikerLijstController(GebruikerController dc, ViewManager viewManager) {
        this.dc = dc;
        this.viewManager = viewManager;
    }

    @FXML private TextField txtFilter;
    @FXML private TableView<ObservableGebruiker> tblvGebruikers;
    @FXML private TableColumn<ObservableGebruiker, String> tblcVoornaam;
    @FXML private TableColumn<ObservableGebruiker, String> tblcNaam;
    @FXML private TableColumn<ObservableGebruiker, String> tblcEmail;
    @FXML private TableColumn<ObservableGebruiker, String> tblcGsm;
    @FXML private TableColumn<ObservableGebruiker, String> tblcRol;
    @FXML private TableColumn<ObservableGebruiker, String> tblcStatus;

    @FXML
    private void initialize() {

        observableBeheer = new ObservableGebruikerBeheer(dc);

        tblcVoornaam.setCellValueFactory(cell ->
                cell.getValue().voornaamProperty());

        tblcNaam.setCellValueFactory(cell ->
                cell.getValue().naamProperty());

        tblcEmail.setCellValueFactory(cell ->
                cell.getValue().emailProperty());

        tblcGsm.setCellValueFactory(cell ->
                cell.getValue().gsmProperty());

        tblcRol.setCellValueFactory(cell ->
                cell.getValue().rolProperty());

        tblcStatus.setCellValueFactory(cell ->
                cell.getValue().statusProperty());

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

            // 🔥 FAVICON TOEVOEGEN
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