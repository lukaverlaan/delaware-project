package com.example._026javag03.gui.controller;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.domein.controller.TeamController;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.gui.LoginController;
import com.example._026javag03.gui.controller.lijst.GebruikerLijstController;
import com.example._026javag03.gui.controller.lijst.MachineLijstController;
import com.example._026javag03.gui.controller.lijst.SiteLijstController;
import com.example._026javag03.gui.controller.lijst.TeamLijstController;
import com.example._026javag03.gui.weergave.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BaseController extends Application {

    private Button gebruikersBtn;
    private Button sitesBtn;
    private Button teamsBtn;
    private VBox sidebar;
    private StackPane centerPane;
    private GebruikerController gc;
    private SiteController sc;
    private TeamController tc;
    private ViewManager viewManager;
    private Label gebruikerLabel;
    private HBox gebruikerBox;
    private Button machinesBtn;

    private BorderPane root;

    private List<Button> menuButtons = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        root = new BorderPane();

        gc = new GebruikerController();
        sc = new SiteController();
        tc = new TeamController();
        viewManager = new ViewManager(root);
        GebruikerController gc = new GebruikerController();
        SiteController sc = new SiteController();
        MachineController mc = new MachineController();
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

        // ========= gebruiker rechtsboven ===========

        ImageView userIcon = new ImageView(
                new Image(getClass().getResourceAsStream("/com/example/_026javag03/gui/images/user-icon.png"))
        );
        userIcon.setFitHeight(24);
        userIcon.setFitWidth(24);

        gebruikerLabel = new Label();

        Button logoutBtn = new Button("Uitloggen");
        logoutBtn.getStyleClass().add("menu-button");
        logoutBtn.setOnAction(e -> logout());

        gebruikerBox = new HBox(8, userIcon, gebruikerLabel, logoutBtn);
        gebruikerBox.setAlignment(Pos.CENTER_RIGHT);
        gebruikerBox.setVisible(false);

        header.setRight(gebruikerBox);



        root.setTop(header);

        // ================= SIDEBAR =================
        sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        gebruikersBtn = new Button("Gebruikers");
        sitesBtn = new Button("Sites");
        teamsBtn = new Button("Teams");

        machinesBtn = new Button("Machines");
        menuButtons.add(gebruikersBtn);
        menuButtons.add(sitesBtn);
        menuButtons.add(teamsBtn);

        menuButtons.add(machinesBtn);
        gebruikersBtn.getStyleClass().add("menu-button");
        sitesBtn.getStyleClass().add("menu-button");
        teamsBtn.getStyleClass().add("menu-button");

        machinesBtn.getStyleClass().add("menu-button");
        gebruikersBtn.setMaxWidth(Double.MAX_VALUE);
        sitesBtn.setMaxWidth(Double.MAX_VALUE);
        teamsBtn.setMaxWidth(Double.MAX_VALUE);
        machinesBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(gebruikersBtn, sitesBtn, teamsBtn, machinesBtn);
        root.setLeft(sidebar);

        centerPane = new StackPane();
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

        teamsBtn.setOnAction(e -> {
            setActiveButton(teamsBtn);
            viewManager.showView(
                    new TeamLijstController(tc, sc, gc, viewManager).getView()
            );
        });

        machinesBtn.setOnAction(e -> {
            setActiveButton(machinesBtn);
            viewManager.showView(
                    new MachineLijstController(mc, viewManager).getView()
            );
            //TODO Machinecontroller toevoegen
        });

        // Startpagina
        toonLoginScherm();
//        setActiveButton(gebruikersBtn);
//        viewManager.showView(
//                new GebruikerLijstController(gc, viewManager).getView()
//        );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/_026javag03/gui/css/delaware.css").toExternalForm()
        );

        stage.setTitle("delaware - Management");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void toonLoginScherm() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/_026javag03/gui/LoginScherm.fxml"));

            Parent loginView = loader.load();

            LoginController controller = loader.getController();
            controller.setBaseController(this);

            sidebar.setVisible(false);
            sidebar.setManaged(false);

            gebruikerBox.setVisible(false);
            gebruikerBox.setManaged(false);

            root.setCenter(loginView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginSucces(GebruikerDTO gebruiker) {

        sidebar.setVisible(true);
        sidebar.setManaged(true);

        gebruikerBox.setVisible(true);
        gebruikerBox.setManaged(true);

        gebruikerLabel.setText(
                gebruiker.voornaam() + " " + gebruiker.naam()
        );

        setActiveButton(gebruikersBtn);

        Parent gebruikerView =
                new GebruikerLijstController(gc, viewManager).getView();

        root.setCenter(gebruikerView);
    }

    public void logout() {

        sidebar.setVisible(false);
        sidebar.setManaged(false);

        gebruikerLabel.setText("");

        gebruikerBox.setVisible(false);
        gebruikerBox.setManaged(false);

        toonLoginScherm();
    }

    private void showViewInCenter(Parent view) {
        centerPane.getChildren().clear();
        centerPane.getChildren().add(view);
    }

    private void setActiveButton(Button active) {

        for (Button btn : menuButtons) {
            btn.getStyleClass().remove("menu-button-active");
        }

        active.getStyleClass().add("menu-button-active");
    }
}