package com.example._026javag03.gui.weergave;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ViewManager {

    private BorderPane root;

    public ViewManager(BorderPane root) {
        this.root = root;
    }

    public void showView(Node view) {
        if (view instanceof Region region) {
            region.setBorder(new Border(new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(0),
                    new BorderWidths(2)
            )));
            region.setPadding(new Insets(10));
        }
        root.setCenter(view);
    }
}