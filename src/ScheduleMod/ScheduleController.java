package ScheduleMod;

import LoginMod.classes.User;
import Models.Schedule;
import TableModels.SchedulesTableModel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import libs.ControlledScreen;
import libs.Database;
import libs.Lookups;
import libs.ScreenViewSwitcher;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by jgibson on 3/4/2015.
 */
public class ScheduleController implements ControlledScreen {
    ScreenViewSwitcher myController;
    User user;
    Database db;

    //<editor-fold desc="@FMXLs">
    @FXML
    private TableColumn<Schedule, LocalDate> effective_date_col = new TableColumn<>();
    @FXML private TableColumn<Schedule, LocalDate> date_added_col = new TableColumn<>();
    @FXML private TableColumn<Schedule, String> job_type_col = new TableColumn<>();
    @FXML private TableColumn<Schedule, String> job_num_col = new TableColumn<>();
    @FXML private TableColumn<Schedule, String> agency_col = new TableColumn<>();
    @FXML private TableColumn<Schedule, Number> num_items_col = new TableColumn<>();

    @FXML private Button add_entity;
    @FXML private Button delete_entity;

    @FXML private TableView<Schedule> schedules_table = new TableView<Schedule>();

    @FXML private void handleAdd() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_schedule.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Add Schedules");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddScheduleController asc = loader.<AddScheduleController>getController();
        asc.setUser(user);
        asc.registerTable(schedules_table);
        asc.setParent(dialogStage);
        asc.setDatabase(db);
        asc.init();
        dialogStage.show();
    }

    @FXML private void handleDelete() throws SQLException {
        String delete_contacts = "DELETE FROM schedules where sid=?";
        PreparedStatement ps = db.getConnection().prepareStatement(delete_contacts);
        for(Schedule s : schedules_table.getSelectionModel().getSelectedItems()) {
            ps.setInt(1, s.getSid());
            ps.executeUpdate();
            schedules_table.getItems().remove(s);
        }
        ps.close();
    }
    //</editor-fold>

    private ObservableList<Schedule> getList(int uid, int type) {
        SchedulesTableModel stm = new SchedulesTableModel();
        ObservableList<Schedule> schedules = null;
        try {
            schedules = stm.getModel(uid, type, db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return schedules;
    }

    private void editSchedule() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_schedule.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Edit Schedule");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddScheduleController asc = loader.<AddScheduleController>getController();
        asc.setUser(user);
        asc.registerTable(schedules_table);
        asc.setParent(dialogStage);
        asc.setDatabase(db);
        asc.init(schedules_table.getSelectionModel().getSelectedItem());
        dialogStage.show();
    }

    //<editor-fold desc="Overrides">
    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {
        myController = screenPage;
    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }

    @Override
    public void init() {
        date_added_col.setCellValueFactory(ud->ud.getValue().date_addedProperty());
        effective_date_col.setCellValueFactory(ud -> ud.getValue().effective_dateProperty());
        job_type_col.setCellValueFactory(ud-> ud.getValue().getJobTypeAsString());
        job_num_col.setCellValueFactory(ud->ud.getValue().job_numberProperty());
        agency_col.setCellValueFactory(ud->ud.getValue().agencyProperty());
        num_items_col.setCellValueFactory(ud->ud.getValue().num_itemsProperty());

        final ObservableList<Schedule> data = getList(user.getUid(), SchedulesTableModel.USER_SCHEDULES);
        schedules_table.setItems(data);
        schedules_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    try {
                        editSchedule();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    //</editor-fold>


}
