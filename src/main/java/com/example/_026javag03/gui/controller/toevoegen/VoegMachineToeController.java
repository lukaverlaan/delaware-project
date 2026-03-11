package com.example._026javag03.gui.controller.toevoegen;

import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.gui.controller.lijst.GebruikerLijstController;
import com.example._026javag03.gui.controller.lijst.MachineLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class VoegMachineToeController {

    private final ViewManager viewManager;
    private final MachineController dc;

    public VoegMachineToeController(MachineController dc,ViewManager viewManager) {
        this.viewManager = viewManager;
        this.dc = dc;
    }

    @FXML
    private ComboBox<String> comboBoxSite;
    @FXML
    private ComboBox<String> comboBoxStatus;
    @FXML
    private ComboBox<String> comboBoxpProdStatus;
    @FXML
    private TextField txtLocatie;
    @FXML
    private TextField txtMedewerker;
    @FXML
    private TextArea txtProdInfo;
    @FXML
    private Button btnTerug;
    @FXML
    private Button btnMaak;

    @FXML
    public void initialize(){

    }

    @FXML
    private void handleTerug() {
        viewManager.showView(new MachineLijstController(dc,viewManager).getView());
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
