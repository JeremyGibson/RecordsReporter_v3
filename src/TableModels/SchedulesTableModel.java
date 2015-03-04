package TableModels;

import Models.Minute;
import Models.Schedule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import libs.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jgibson on 3/4/2015.
 */
public class SchedulesTableModel {
    private ObservableList<Schedule> schedules_list = FXCollections.observableArrayList();
    private static final String GET_USER_SCHEDULES = "Select users.f_name, users.l_name, schedules.* " +
            "from users, schedules where schedules.uid=users.uid " +
            "AND minutes.uid=? ORDER BY schedules.date_added DESC";
    private static final String GET_ALL_SCHEDULES = "Select users.f_name, users.l_name, schedules.* " +
            "from users, schedules where schedules.uid=users.uid " +
            "ORDER BY schedules.date_added DESC";
    public static final int USER_SCHEDULES = 0;
    public static final int ALL_SCHEDULES = 1;
    private PreparedStatement gus;
    private PreparedStatement gas;
    private Database db;
    private int uid;

    public ObservableList<Schedule> getModel(int uid, int get_type, Database db) throws SQLException {
        this.uid = uid;
        this.db = db;
        ResultSet rs;
        switch (get_type) {
            case USER_SCHEDULES:
                gus = db.getConnection().prepareStatement(GET_USER_SCHEDULES);
                gus.setInt(1, uid);
                rs = gus.executeQuery();
                break;
            case ALL_SCHEDULES:
                gas = db.getConnection().prepareStatement(GET_ALL_SCHEDULES);
                rs = gas.executeQuery();
                break;
            default:
                gas = db.getConnection().prepareStatement(GET_ALL_SCHEDULES);
                rs = gas.executeQuery();
                break;
        }

        while (rs.next()) {
            schedules_list.add(new Schedule(
                    rs.getInt("sid"),
                    rs.getInt("uid"),
                    rs.getDate("effective_date").toLocalDate(),
                    rs.getInt("schedule_type"),
                    rs.getString("agency"),
                    rs.getInt("job_number"),
                    rs.getInt("job_type"),
                    rs.getInt("num_items"),
                    rs.getString("description"),
                    rs.getDate("date_added").toLocalDate(),
                    rs.getDate("date_modified").toLocalDate()
            ));
        }
        return schedules_list;
    }
}
