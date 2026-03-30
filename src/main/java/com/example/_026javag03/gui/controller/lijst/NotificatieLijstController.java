package com.example._026javag03.gui.controller.lijst;

import com.example._026javag03.domein.controller.NotificatieController;
import com.example._026javag03.gui.controller.detail.NotificatieDetailController;
import com.example._026javag03.gui.controller.toevoegen.VoegNotificatieToeController;
import com.example._026javag03.gui.observable.ObservableNotificatie;
import com.example._026javag03.gui.observable.beheer.ObservableNotificatieBeheer;
import com.example._026javag03.gui.weergave.ViewManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class NotificatieLijstController {

    private final NotificatieController nc;
    private final ViewManager viewManager;

    private ObservableNotificatieBeheer beheer;

    public NotificatieLijstController(NotificatieController nc, ViewManager viewManager) {
        this.nc = nc;
        this.viewManager = viewManager;
    }

    @FXML private TextField txtFilter;

    @FXML private TableView<ObservableNotificatie> tblvNotificaties;

    @FXML private TableColumn<ObservableNotificatie, String> tblcCode;
    @FXML private TableColumn<ObservableNotificatie, String> tblcDatum;
    @FXML private TableColumn<ObservableNotificatie, String> tblcType;
    @FXML private TableColumn<ObservableNotificatie, String> tblcInhoud;
    @FXML private TableColumn<ObservableNotificatie, String> tblcStatus;

    @FXML private Button btnNieuweNotificatie;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm");

    @FXML
    private void initialize() {

        beheer = new ObservableNotificatieBeheer(nc);

        tblcCode.setCellValueFactory(c -> c.getValue().codeProperty());

        tblcDatum.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDatum() != null
                                ? c.getValue().getDatum().format(formatter)
                                : ""
                ));

        tblcType.setCellValueFactory(c -> c.getValue().typeProperty());
        tblcInhoud.setCellValueFactory(c -> c.getValue().inhoudProperty());
        tblcStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        SortedList<ObservableNotificatie> sorted =
                new SortedList<>(beheer.getFilteredNotificaties());

        sorted.comparatorProperty()
                .bind(tblvNotificaties.comparatorProperty());

        tblvNotificaties.setItems(sorted);

        txtFilter.textProperty().addListener((obs, o, n) ->
                beheer.setFilter(n)
        );

        btnNieuweNotificatie.setOnAction(e -> openNieuweNotificatie());

        tblvNotificaties.setRowFactory(tv -> {
            TableRow<ObservableNotificatie> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    openDetailScherm(row.getItem());
                }
            });
            return row;
        });
    }

    private void openDetailScherm(ObservableNotificatie observable) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/NotificatieDetailScherm.fxml")
            );

            loader.setController(
                    new NotificatieDetailController(nc, observable.getDto())
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/delaware-favicon.png"))
            );
            stage.setTitle("Notificatie details");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            beheer.refresh();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openNieuweNotificatie() {

        viewManager.showView(
                new VoegNotificatieToeController(nc, viewManager).getView()
        );
    }

    public Parent getView() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/_026javag03/gui/NotificatieLijstScherm.fxml")
            );

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}