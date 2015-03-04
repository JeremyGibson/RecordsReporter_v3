package ScheduleMod;

import LoginMod.classes.User;
import Models.Schedule;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import libs.ControlledScreen;
import libs.Database;
import libs.ScreenViewSwitcher;

/**
 * Created by jgibson on 3/4/2015.
 */
public class AddScheduleController implements ControlledScreen {
    private TableView<Schedule> current_table;
    private Stage parent;
    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {

    }

    @Override
    public void setUser(User u) {

    }

    @Override
    public void init() {

    }

    public void init(Schedule s) {

    }

    @Override
    public void setDatabase(Database db) {

    }

    public void registerTable(TableView<Schedule> sched) {
        current_table = sched;
    }

    public void setParent(Stage s) {
        parent = s;
    }
}
