package com.example._026javag03.gui.controller;

import com.example._026javag03.domein.controller.*;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.gui.controller.lijst.*;
import com.example._026javag03.gui.controller.login.LoginController;
import com.example._026javag03.gui.controller.login.NieuwWachtwoordController;
import com.example._026javag03.gui.weergave.ViewManager;
import com.example._026javag03.repository.GenericDaoJpa;
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
    private Button machinesBtn;
    private Button notificatiesBtn;

    private VBox sidebar;
    private StackPane centerPane;

    private GebruikerController gc;
    private SiteController sc;
    private TeamController tc;
    private MachineController mc;
    private NotificatieController nc;

    private ViewManager viewManager;

    private Label gebruikerLabel;
    private HBox gebruikerBox;

    private BorderPane root;

    private List<Button> menuButtons = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        root = new BorderPane();

        gc = new GebruikerController();
        sc = new SiteController();
        tc = new TeamController();
        mc = new MachineController();
        nc = new NotificatieController();

        viewManager = new ViewManager(root);

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
        notificatiesBtn = new Button("Notificaties");

        // toevoegen aan lijst
        menuButtons.add(gebruikersBtn);
        menuButtons.add(sitesBtn);
        menuButtons.add(teamsBtn);
        menuButtons.add(machinesBtn);
        menuButtons.add(notificatiesBtn);

        // styling
        for (Button btn : menuButtons) {
            btn.getStyleClass().add("menu-button");
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        sidebar.getChildren().addAll(
                gebruikersBtn,
                sitesBtn,
                teamsBtn,
                machinesBtn,
                notificatiesBtn
        );

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
                    new MachineLijstController(mc, sc, gc, viewManager).getView()
            );
        });

        notificatiesBtn.setOnAction(e -> {
            setActiveButton(notificatiesBtn);
            viewManager.showView(
                    new NotificatieLijstController(nc, viewManager).getView()
            );
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
            controller.setGc(gc);
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

        gebruikerLabel.setText(gebruiker.voornaam());

        // alles verbergen
        for (Button btn : menuButtons) {
            btn.setVisible(false);
            btn.setManaged(false);
        }

        switch (gebruiker.rol()) {

            case ADMINISTRATOR -> {
                showAllButtons();
                setActiveButton(gebruikersBtn);
                root.setCenter(
                        new GebruikerLijstController(gc, viewManager).getView()
                );
            }

            case MANAGER -> {
                sitesBtn.setVisible(true);
                sitesBtn.setManaged(true);

                teamsBtn.setVisible(true);
                teamsBtn.setManaged(true);

                notificatiesBtn.setVisible(true);
                notificatiesBtn.setManaged(true);

                setActiveButton(sitesBtn);
                root.setCenter(
                        new SiteLijstController(sc, viewManager).getView()
                );
            }

            case VERANTWOORDELIJKE -> {
                teamsBtn.setVisible(true);
                teamsBtn.setManaged(true);

                machinesBtn.setVisible(true);
                machinesBtn.setManaged(true);

                notificatiesBtn.setVisible(true);
                notificatiesBtn.setManaged(true);

                setActiveButton(teamsBtn);
                root.setCenter(
                        new TeamLijstController(tc, sc, gc, viewManager).getView()
                );
            }

            case WERKNEMER -> {

                notificatiesBtn.setVisible(true);
                notificatiesBtn.setManaged(true);

                setActiveButton(notificatiesBtn);
                root.setCenter(
                        new NotificatieLijstController(nc, viewManager).getView()
                );
            }
        }
    }

    private void showAllButtons() {
        for (Button btn : menuButtons) {
            btn.setVisible(true);
            btn.setManaged(true);
        }
    }

    public void toonNieuwWachtwoordScherm(GebruikerDTO gebruiker) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/_026javag03/gui/NieuwWachtwoordScherm.fxml"));

            Parent view = loader.load();

            NieuwWachtwoordController controller = loader.getController();
            controller.init(gebruiker, this);

            root.setCenter(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {

        sidebar.setVisible(false);
        sidebar.setManaged(false);

        gebruikerLabel.setText("");

        gebruikerBox.setVisible(false);
        gebruikerBox.setManaged(false);

        toonLoginScherm();
    }

    private void setActiveButton(Button active) {

        for (Button btn : menuButtons) {
            btn.getStyleClass().remove("menu-button-active");
        }

        active.getStyleClass().add("menu-button-active");
    }

    @Override
    public void stop() {

        tc.close();
        gc.close();
        sc.close();
        mc.close();
        nc.close();

        GenericDaoJpa.closeFactory();
    }
}