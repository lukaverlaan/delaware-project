package com.example._026javag03.gui.weergave;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

public class ViewManager {

    private BorderPane root;

    @Setter
    @Getter
    private boolean emailVerstuurd;

    public ViewManager(BorderPane root) {
        this.root = root;
    }

    public void showView(Node view) {
        root.setCenter(view);
    }
}