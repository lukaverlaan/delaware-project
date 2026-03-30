package com.example._026javag03.gui.controller.lijst;

import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.domein.controller.TaakController;
import com.example._026javag03.gui.controller.detail.MachineDetailController;
import com.example._026javag03.gui.controller.toevoegen.VoegMachineToeController;
import com.example._026javag03.gui.controller.toevoegen.VoegTaakToeController;
import com.example._026javag03.gui.observable.ObservableMachine;
import com.example._026javag03.gui.observable.ObservableTaak;
import com.example._026javag03.gui.observable.ObservableTeam;
import com.example._026javag03.gui.observable.beheer.ObservableMachineBeheer;
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

public class TakenLijstController {


    private final ViewManager viewManager;
    private final TaakController tc;

    private ObservableMachineBeheer observableBeheer;




    public TakenLijstController(TaakController tc, ViewManager viewManager) {
        this.tc = tc;
        this.viewManager = viewManager;
    }

    @FXML private TextField txtFilter;
    @FXML private TableView<ObservableTaak> tblvMachines;
    @FXML private Button btnNieuweMachine;


    @FXML private TableColumn<ObservableTaak,String> tblcType;
    @FXML private TableColumn<ObservableTaak, String> tblcOmschrijving;
    @FXML private TableColumn<ObservableTaak, String> tblcDuurtijd;


    @FXML
    private void initialize(){


        tblcType.setCellValueFactory(cell ->
                cell.getValue().typeProperty()
              );
        tblcOmschrijving.setCellValueFactory(cell ->
                cell.getValue().omschrijvingProperty()
        );
        tblcDuurtijd.setCellValueFactory(cell ->
                cell.getValue().duurtijdProperty()
        );

//     SortedList<ObservableTaak> sorted =
//                new SortedList<>(observableBeheer.getFilteredTaken());



        tblvMachines.setRowFactory(tv -> {
                    TableRow<ObservableTaak> row = new TableRow<>();
                    row.setOnMouseClicked(e -> {
                        if (e.getClickCount() == 2 && !row.isEmpty()) {
                            openDetailScherm(row.getItem());
                        }
                    });
                    return row;
                });

        btnNieuweMachine.setOnAction(e -> {
            viewManager.showView(
                    new VoegTaakToeController(tc,viewManager).getView()
            );
        });
    }

        private void openDetailScherm(ObservableTaak item) {

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/_026javag03/gui/MachineDetailScherm.fxml")
                );

//                loader.setController(
//
//                );

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
//                observableBeheer.refresh();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    private void openNieuwMachineScherm() {

        viewManager.showView(
                new VoegTaakToeController(tc,viewManager).getView()
        );
    }



    public Parent getView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/TaakLijstScherm.fxml")
            );
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}