package com.example._026javag03.gui.controller.lijst;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.domein.controller.TeamController;
import com.example._026javag03.gui.controller.detail.TeamDetailController;
import com.example._026javag03.gui.controller.toevoegen.VoegTeamToeController;
import com.example._026javag03.gui.observable.ObservableTeam;
import com.example._026javag03.gui.observable.beheer.ObservableTeamBeheer;
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

public class TeamLijstController {

    private final TeamController tc;
    private final SiteController sc;
    private final GebruikerController gc;
    private final ViewManager viewManager;

    private ObservableTeamBeheer observableBeheer;

    public TeamLijstController(
            TeamController tc,
            SiteController sc,
            GebruikerController gc,
            ViewManager viewManager) {

        this.tc = tc;
        this.sc = sc;
        this.gc = gc;
        this.viewManager = viewManager;
    }

    @FXML
    private TextField txtFilter;

    @FXML
    private TableView<ObservableTeam> tblvTeams;

    @FXML
    private TableColumn<ObservableTeam,String> tblcCode;

    @FXML
    private TableColumn<ObservableTeam,String> tblcSite;

    @FXML
    private TableColumn<ObservableTeam,String> tblcVerantwoordelijke;

    @FXML
    private TableColumn<ObservableTeam,Integer> tblcAantalMedewerkers;

    @FXML
    private Button btnNieuwTeam;

    @FXML
    private void initialize() {

        observableBeheer = new ObservableTeamBeheer(tc, sc, gc);

        txtFilter.textProperty().addListener((obs,o,n) -> updateFilter());

        tblcCode.setCellValueFactory(cell ->
                cell.getValue().codeProperty());

        tblcSite.setCellValueFactory(cell ->
                cell.getValue().siteProperty());

        tblcVerantwoordelijke.setCellValueFactory(cell ->
                cell.getValue().verantwoordelijkeProperty());

        tblcAantalMedewerkers.setCellValueFactory(cell ->
                cell.getValue().aantalMedewerkersProperty().asObject());

        SortedList<ObservableTeam> sorted =
                new SortedList<>(observableBeheer.getFilteredTeams());

        sorted.comparatorProperty().bind(tblvTeams.comparatorProperty());

        tblvTeams.setItems(sorted);

        btnNieuwTeam.setOnAction(e -> openNieuwTeam());

        if (gc.isVerantwoordelijke()) {
            btnNieuwTeam.setVisible(false);
        }

        tblvTeams.setRowFactory(tv -> {
            TableRow<ObservableTeam> row = new TableRow<>();

            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    openDetailScherm(row.getItem());
                }
            });

            return row;
        });
    }

    private void openNieuwTeam() {
        viewManager.showView(
                new VoegTeamToeController(tc, sc, gc, viewManager).getView()
        );
    }

    private void updateFilter() {
        observableBeheer.changeFilter(txtFilter.getText());
    }

    private boolean contains(String value, String filter) {
        return value != null &&
                value.toLowerCase().contains(filter);
    }

    private void openDetailScherm(ObservableTeam observableTeam) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/TeamDetailScherm.fxml")
            );

            loader.setController(
                    new TeamDetailController(
                            tc,
                            sc,
                            gc,
                            observableTeam.getDto()
                    )
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/delaware-favicon.png"))
            );
            stage.setTitle("Team details");

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();

            observableBeheer.refresh();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Parent getView() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/_026javag03/gui/TeamLijstScherm.fxml")
            );

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}