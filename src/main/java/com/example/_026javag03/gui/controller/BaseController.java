package com.example._026javag03.gui.controller;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.gui.controller.lijst.GebruikerLijstController;
import com.example._026javag03.gui.controller.lijst.SiteLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BaseController extends Application {

    private Button gebruikersBtn;
    private Button sitesBtn;

    private List<Button> menuButtons = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        GebruikerController gc = new GebruikerController();
        SiteController sc = new SiteController();
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
        sitesBtn = new Button("Sites");
        menuButtons.add(gebruikersBtn);
        menuButtons.add(sitesBtn);
        gebruikersBtn.getStyleClass().add("menu-button");
        sitesBtn.getStyleClass().add("menu-button");
        gebruikersBtn.setMaxWidth(Double.MAX_VALUE);
        sitesBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(gebruikersBtn, sitesBtn);
        root.setLeft(sidebar);

        StackPane centerPane = new StackPane();
        centerPane.getStyleClass().add("content-pane");
        root.setCenter(centerPane);

        // ================= ACTIES =================

        gebruikersBtn.setOnAction(e -> {
            setActiveButton(gebruikersBtn);
            viewManager.showView(
                    new GebruikerLijstController(gc, viewManager).getView()
            );
        });

        sitesBtn.setOnAction(e -> {
            setActiveButton(sitesBtn);
            viewManager.showView(
                    new SiteLijstController(sc, viewManager).getView()
            );
        });

        // Startpagina
        setActiveButton(gebruikersBtn);
        viewManager.showView(
                new GebruikerLijstController(gc, viewManager).getView()
        );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/_026javag03/gui/css/delaware.css").toExternalForm()
        );

        stage.setTitle("delaware - Management");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void setActiveButton(Button active) {

        for (Button btn : menuButtons) {
            btn.getStyleClass().remove("menu-button-active");
        }

        active.getStyleClass().add("menu-button-active");
    }
}