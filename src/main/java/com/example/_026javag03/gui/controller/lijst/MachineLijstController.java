package com.example._026javag03.gui.controller.lijst;

import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.gui.controller.detail.MachineDetailController;
import com.example._026javag03.gui.controller.toevoegen.VoegMachineToeController;
import com.example._026javag03.gui.observable.ObservableGebruiker;
import com.example._026javag03.gui.observable.ObservableMachine;
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

public class MachineLijstController {


    private final ViewManager viewManager;
    private final MachineController mc;

    private ObservableMachineBeheer observableBeheer;




    public MachineLijstController(MachineController mc, ViewManager viewManager) {
        this.mc = mc;
        this.viewManager = viewManager;
    }

    @FXML private TextField txtFilter;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private TableView<ObservableMachine> tblvMachines;
    @FXML private Button btnNieuweMachine;


    @FXML private TableColumn<ObservableMachine,String> tblcStatus;
    @FXML private TableColumn<ObservableMachine, String> tblcProductInfo;
    @FXML private TableColumn<ObservableMachine, String> tblcSite;
    @FXML private TableColumn<ObservableMachine, String> tblcOnderhoud;
    @FXML private TableColumn<ObservableMachine, String> tblcUptime;

    @FXML
    private void initialize(){

        statusFilterComboBox.getItems().addAll(
                "Draait",
                "Nood aan onderhoud",
                "Gestopt (Auto)",
                "Gestopt (Manueel)");

        tblcStatus.setCellValueFactory(cell ->
                cell.getValue().statusProperty()
              );
        tblcProductInfo.setCellValueFactory(cell ->
                cell.getValue().productInfoProperty()
        );
        tblcSite.setCellValueFactory(cell ->
                cell.getValue().siteProperty()
        );
        tblcOnderhoud.setCellValueFactory(cell ->
                cell.getValue().onderhoudProperty()
        );
        tblcUptime.setCellValueFactory(cell ->
                cell.getValue().uptimeProperty()
        );

//        SortedList<ObservableMachine> sorted =
//                new SortedList<>(observableBeheer.getFilteredMachines());
//
//        tblvMachines.setItems(sorted);



        tblvMachines.setRowFactory(tv -> {
                    TableRow<ObservableMachine> row = new TableRow<>();
                    row.setOnMouseClicked(e -> {
                        if (e.getClickCount() == 2 && !row.isEmpty()) {
                            openDetailScherm(row.getItem());
                        }
                    });
                    return row;
                });

        btnNieuweMachine.setOnAction(e -> {
            viewManager.showView(
                    new VoegMachineToeController(mc,viewManager).getView()
            );
        });
    }

        private void openDetailScherm(ObservableMachine item) {

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/_026javag03/gui/MachineDetailScherm.fxml")
                );

                loader.setController(
                        new MachineDetailController(mc)
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
//                observableBeheer.refresh();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    private void openNieuwMachineScherm() {

        viewManager.showView(
                new VoegMachineToeController(mc,viewManager).getView()
        );
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