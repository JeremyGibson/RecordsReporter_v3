package ScheduleMod;

import LoginMod.classes.User;
import Models.Schedule;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import libs.*;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.Dialogs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Created by jgibson on 3/4/2015.
 */
public class AddScheduleController implements ControlledScreen {
    private TableView<Schedule> current_table;
    private Stage parent;
    private User user;
    private Database db;
    private Schedule current_schedule;
    private boolean editing = false;

    @FXML private DatePicker effective_date;
    @FXML private TextField agency;
    @FXML private ComboBox<String> schedule_type;
    @FXML private TextField job_number;
    @FXML private ComboBox<String> job_type;
    @FXML private TextField num_items;
    @FXML private HTMLEditor description;
    @FXML private Button btn_add;
    @FXML private Button btn_reset;


    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {

    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void init() {
        effective_date.setValue(LocalDate.now());
        schedule_type.setItems(Lookups.slu);
        schedule_type.getSelectionModel().select(0);
        job_type.setItems(Lookups.sched_job_type);
        job_type.getSelectionModel().select(0);
        num_items.setText("0");

        try {
            TextFields.bindAutoCompletion(
                    agency,
                    (Collection) Lookups.auto_fill_text("schedules", "agency", user.getUid(), db)
            );
        } catch (SQLException e) {
            System.out.println("");
        }
    }

    public void init(Schedule s) {
        editing = true;
        current_schedule = s;
        effective_date.setValue(s.getEffective_date());
        schedule_type.setItems(Lookups.slu);
        job_type.setItems(Lookups.sched_job_type);
        schedule_type.getSelectionModel().select(s.getSchedule_type());
        job_type.getSelectionModel().select(s.getJob_type());
        job_number.setText(s.getJob_number());
        num_items.setText(Integer.toString(s.getNum_items()));
        description.setHtmlText(s.getDescription());
        try {
            TextFields.bindAutoCompletion(
                    agency,
                    (Collection) Lookups.auto_fill_text("schedules", "agency", user.getUid(), db)
            );
        } catch (SQLException e) {
            System.out.println("");
        }
        agency.setText(s.getAgency());
        btn_add.setText("Update Schedule");
    }

    @FXML private void handle_add() throws SQLException, ParseException {
        if(editing==false) {
            addSchedule();
        } else {
            updateSchedule();
            parent.close();
        }
    }

    @FXML private void handle_reset() {
        schedule_type.getSelectionModel().select(0);
        job_type.getSelectionModel().select(0);
        job_number.setText("");
        num_items.setText("0");
        description.setHtmlText("");
        agency.setText("");
    }

    private void addSchedule() throws SQLException, ParseException {
        int sid = 0;
        current_schedule = new Schedule(
                sid,
                user.getUid(),
                effective_date.getValue(),
                Lookups.slu_lookup.get(schedule_type.getValue()),
                agency.getText(),
                sanitize(job_number.getText()),
                Lookups.job_type_lookup.get(job_type.getValue()),
                Integer.parseInt(num_items.getText()),
                description.getHtmlText(),
                LocalDate.now(),
                LocalDate.now());

        PreparedStatement ps = db.getConnection().prepareStatement("" +
                "INSERT INTO schedules VALUES(null,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, user.getUid());
        ps.setLong(2, new EasyDate(current_schedule.getEffective_date()).getDateAsLong());
        ps.setString(3, current_schedule.getAgency());
        ps.setInt(4, current_schedule.getSchedule_type());
        ps.setString(5, current_schedule.getJob_number());
        ps.setInt(6, current_schedule.getJob_type());
        ps.setInt(7, current_schedule.getNum_items());
        ps.setString(8, current_schedule.getDescription());
        ps.setLong(9, new EasyDate(LocalDate.now()).getDateAsLong());
        ps.setLong(10, new EasyDate(LocalDate.now()).getDateAsLong() );
        ps.executeUpdate();
        try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                sid = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            generatedKeys.close();
        }
        current_schedule.setSid(sid);
        ps.close();

        current_table.getItems().add(0, current_schedule);
        handle_reset();
    }

    private void updateSchedule() throws SQLException, ParseException {
        PreparedStatement ps = db.getConnection().prepareStatement("" +
                "UPDATE schedules SET " +
                "uid=?," +
                "effective_date=?," +
                "agency=?," +
                "schedule_type=?," +
                "job_number=?," +
                "job_type=?," +
                "num_items=?," +
                "description=?," +
                "date_added=?," +
                "date_modified=?" +
                "WHERE " +
                "sid=?");

        current_schedule.setAgency(agency.getText());
        current_schedule.setEffective_date(effective_date.getValue());
        current_schedule.setSchedule_type(Lookups.slu_lookup.get(schedule_type.getValue()));
        current_schedule.setJob_number(job_number.getText());
        current_schedule.setJob_type(Lookups.job_type_lookup.get(job_type.getValue()));
        current_schedule.setNum_items(Integer.parseInt(num_items.getText()));
        current_schedule.setDescription(description.getHtmlText());
        current_schedule.setDate_modified(LocalDate.now());

        ps.setInt(1, user.getUid());
        ps.setLong(2, new EasyDate(current_schedule.getEffective_date()).getDateAsLong());
        ps.setString(3, current_schedule.getAgency());
        ps.setInt(4, current_schedule.getSchedule_type());
        ps.setString(5, current_schedule.getJob_number());
        ps.setInt(6, current_schedule.getJob_type());
        ps.setInt(7, current_schedule.getNum_items());
        ps.setString(8, current_schedule.getDescription());
        ps.setLong(9, new EasyDate(current_schedule.getDate_added()).getDateAsLong());
        ps.setLong(10, new EasyDate(current_schedule.getDate_modified()).getDateAsLong());
        ps.executeUpdate();
        ps.close();
    }

    private String sanitize(String check_value) {
        if(check_value.matches("\\/")){
            check_value.replaceAll("\\/", "");
        }

        if(check_value.matches("\\\\")) {
            check_value.replaceAll("\\\\", "");
        }
        return check_value;
    }


    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }

    public void registerTable(TableView<Schedule> sched) {
        current_table = sched;
    }

    public void setParent(Stage s) {
        parent = s;
    }
}
