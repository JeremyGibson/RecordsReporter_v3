package WorkshopsMod;

import Models.Workshop;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import libs.ControlledScreen;
import libs.Database;
import libs.ScreenViewSwitcher;
import LoginMod.classes.User;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by jgibson on 2/13/2015.
 */
public class WorkshopController implements Initializable, ControlledScreen {
    private User user;
    private Database db;
    private Stage parent;

    @FXML private TableColumn<Workshop, LocalDate> workshop_date;
    @FXML private TableColumn<Workshop, String> staff;
    @FXML private TableColumn<Workshop, Number> num_attending;
    @FXML private TableColumn<Workshop, String> location;
    @FXML private TableColumn<Workshop, String> description;

    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML private void handleAddWorkshop() {

    }

    @Override
    public void init() {

    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }
}
