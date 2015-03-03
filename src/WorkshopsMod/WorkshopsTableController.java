package WorkshopsMod;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import libs.ControlledScreen;
import libs.Database;
import libs.ScreenViewSwitcher;
import LoginMod.classes.User;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jgibson on 2/13/2015.
 */
public class WorkshopsTableController implements Initializable, ControlledScreen {
    private User user;
    private Database db;
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
