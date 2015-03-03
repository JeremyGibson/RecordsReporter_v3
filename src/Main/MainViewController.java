package Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import libs.ControlledScreen;
import libs.Database;
import libs.ScreenViewSwitcher;
import LoginMod.classes.User;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jgibson on 2/13/2015.
 */
public class MainViewController implements Initializable {
    @FXML private StackPane sp_main_view;
    @FXML private MenuBar mb_main_view;
    @FXML private MenuItem mi_contacts;
    @FXML private MenuItem mi_workshops;
    @FXML private MenuItem mi_minutes;
    @FXML private MenuItem mi_schedules;
    @FXML private MenuItem mi_pref_login;

    public static final String CONTACTS = "Contacts";
    public static final String SCHEDULES = "Schedules";
    public static final String WORKSHOPS = "Workshops";
    public static final String MINUTES = "Minutes";

    private ScreenViewSwitcher svs;
    private boolean default_screen = true;
    private String active_pane = "";
    private AnchorPane current_main_view;
    private ControlledScreen current_controller;

    private Stage ownerStage;
    private User user;
    private Database db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init_screens() {
        svs = new ScreenViewSwitcher();
        svs.registerRoot(this);
        svs.registerUser(user);
        svs.registerDatabase(db);
        svs.loadScreen(CONTACTS, "../ContactMod/contacts.fxml");
        svs.loadScreen(MINUTES, "../MinutesMod/minutes.fxml");
        //svs.loadScreen(SCHEDULES, "../scheduleMod/schedule_table.fxml");
        //svs.loadScreen(WORKSHOPS, "../workshopMod/workshop_table.fxml");
        switch_active_pane(CONTACTS);
    }


    public void setStage(Stage s) {
        ownerStage = s;

    }

    private void switch_active_pane(String pane) {
        active_pane = pane;
        if (sp_main_view.getChildren().size() > 0) sp_main_view.getChildren().remove(0);
        sp_main_view.getChildren().add(svs.getCurrentPane(pane));
        current_main_view = (AnchorPane) svs.getCurrentPane(pane);
        current_controller = svs.getLoader(pane).getController();
        return;
    }

    public void switch_pane(String pane) {
        switch_active_pane(pane);
    }

    @FXML
    private void handle_mi_contacts() {
        switch_active_pane(CONTACTS);
    }

    @FXML
    private void handle_mi_workshops() {
        switch_active_pane(WORKSHOPS);
    }

    @FXML private void handle_mi_minutes() {
        switch_active_pane(MINUTES);
    }

    @FXML private void handle_admin_login() {

    }

    public Stage getParentStage() {
        return ownerStage;
    }

    public void setUser(User u) {
        user = u;
    }
    public void setDatabase(Database db) {
        this.db = db;
    }


}
