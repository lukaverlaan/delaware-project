package com.example._026javag03.gui;

import com.example._026javag03.domein.GebruikerController;
import com.example._026javag03.gui.weergave.ViewManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BaseController extends Application {

    private Button gebruikersBtn;
    private Button nieuweBtn;

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        GebruikerController dc = new GebruikerController();
        ViewManager viewManager = new ViewManager(root);

        // ================= FAVICON =================
        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/delaware-favicon.png"))
        );

        // ================= HEADER =================
        BorderPane header = new BorderPane();
        header.getStyleClass().add("header");

        ImageView logo = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/delaware-logo.png"))
        );
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);

        header.setCenter(logo);
        root.setTop(header);

        // ================= SIDEBAR =================
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        gebruikersBtn = new Button("Gebruikers");
        gebruikersBtn.getStyleClass().add("menu-button");
        gebruikersBtn.setMaxWidth(Double.MAX_VALUE);

        nieuweBtn = new Button("Nieuwe gebruiker");
        nieuweBtn.getStyleClass().add("menu-button");
        nieuweBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(gebruikersBtn, nieuweBtn);
        root.setLeft(sidebar);

        StackPane centerPane = new StackPane();
        centerPane.getStyleClass().add("content-pane");
        root.setCenter(centerPane);

        // ================= ACTIES =================

        gebruikersBtn.setOnAction(e -> {
            setActiveButton(gebruikersBtn);
            viewManager.showView(
                    new GebruikerLijstController(dc, viewManager).getView()
            );
        });

        nieuweBtn.setOnAction(e -> {
            setActiveButton(nieuweBtn);
            viewManager.showView(
                    new VoegGebruikerToeController(dc).getView()
            );
        });

        // Startpagina
        setActiveButton(gebruikersBtn);
        viewManager.showView(
                new GebruikerLijstController(dc, viewManager).getView()
        );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/_026javag03/gui/css/delaware.css").toExternalForm()
        );

        stage.setTitle("Delaware - Gebruikersbeheer");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void setActiveButton(Button active) {

        gebruikersBtn.getStyleClass().remove("menu-button-active");
        nieuweBtn.getStyleClass().remove("menu-button-active");

        active.getStyleClass().add("menu-button-active");
    }
}