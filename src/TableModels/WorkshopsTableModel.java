package TableModels;

import Models.Schedule;
import Models.Workshop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import libs.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jgibson on 3/4/2015.
 */
public class WorkshopsTableModel {
    private ObservableList<Workshop> workshops_list = FXCollections.observableArrayList();
    private static final String GET_USER_WORKSHOPS = "Select users.f_name, users.l_name, workshops.* " +
            "from users, workshops where workshops.uid=users.uid " +
            "AND workshops.uid=? ORDER BY workshops.date_added DESC";
    private static final String GET_ALL_WORKSHOPS = "Select users.f_name, users.l_name, workshops.* " +
            "from users, workshops where workshops.uid=users.uid " +
            "ORDER BY workshops.date_added DESC";

    private static final String GET_X_UIDS = "SELECT users.fname, workshop_uids.uid " +
            "FROM users, workshop_uids " +
            "WHERE " +
            "workshop_uids.uid=users.uid AND " +
            "workshop_uids.wid=?";
    public static final int USER_WORKSHOPS = 0;
    public static final int ALL_WORKSHOPS = 1;
    private PreparedStatement guw;
    private PreparedStatement gaw;
    private PreparedStatement gxuid;
    private Database db;
    private int uid;

    public ObservableList<Workshop> getModel(int uid, int get_type, Database db) throws SQLException {
        this.uid = uid;
        this.db = db;
        ResultSet rs;
        switch (get_type) {
            case USER_WORKSHOPS:
                guw = db.getConnection().prepareStatement(GET_USER_WORKSHOPS);
                guw.setInt(1, uid);
                rs = guw.executeQuery();
                break;
            case ALL_WORKSHOPS:
                gaw = db.getConnection().prepareStatement(GET_ALL_WORKSHOPS);
                rs = gaw.executeQuery();
                break;
            default:
                gaw = db.getConnection().prepareStatement(GET_ALL_WORKSHOPS);
                rs = gaw.executeQuery();
                break;
        }

        while (rs.next()) {
            workshops_list.add(new Workshop(
                    rs.getInt("wid"),
                    rs.getInt("uid"),
                    rs.getDate("workshop_date").toLocalDate(),
                    rs.getInt("additional_uids"),
                    rs.getString("location"),
                    rs.getInt("num_attending"),
                    rs.getInt("workshop_type"),
                    rs.getString("description"),
                    rs.getDate("date_added").toLocalDate(),
                    rs.getDate("date_modified").toLocalDate()

            ));

            if(rs.getInt("additional_uids") == 1) {
                gxuid = db.getConnection().prepareStatement(GET_X_UIDS);
                gxuid.setInt(1, rs.getInt("wid"));
                ResultSet resultSet = gxuid.executeQuery();
                ObservableList<Integer> uids = FXCollections.observableArrayList();
            }
        }
        return workshops_list;
    }
}
