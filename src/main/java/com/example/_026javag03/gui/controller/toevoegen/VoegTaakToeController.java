package com.example._026javag03.gui.controller.toevoegen;

import com.example._026javag03.domein.controller.TaakController;
import com.example._026javag03.gui.weergave.ViewManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class VoegTaakToeController {
    public VoegTaakToeController(TaakController tc, ViewManager viewManager) {
    }

    public Node getView() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/VoegTaakToeScherm.fxml")
            );

            loader.setController(this);

            return loader.load();

        } catch (IOException e) {
            throw new RuntimeException("Kan FXML niet laden");
        }
    }
}
