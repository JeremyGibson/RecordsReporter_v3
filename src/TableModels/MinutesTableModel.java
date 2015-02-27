package TableModels;

import Models.Contact;
import Models.Minute;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import libs.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jgibson on 2/20/2015.
 */
public class MinutesTableModel {
    private ObservableList<Minute> minutes_list = FXCollections.observableArrayList();
    private static final String GET_USER_MINUTES = "Select users.f_name, users.l_name, minutes.* " +
            "from users, minutes where minutes.uid=users.uid " +
            "AND minutes.uid=? ORDER BY minutes.date_added DESC";
    private static final String GET_ALL_MINUTES = "Select users.f_name, users.l_name, minutes.* " +
            "from users, minutes where minutes.uid=users.uid " +
            "ORDER BY minutes.date_added DESC";
    public static final int USER_MINUTES = 0;
    public static final int ALL_MINUTES = 1;
    private PreparedStatement gum;
    private PreparedStatement gam;
    private Database db;
    private int uid;

    public ObservableList<Minute> getModel(int uid, int get_type) throws SQLException {
        this.uid = uid;
        db = new Database("mssql");
        ResultSet rs;
        switch (get_type) {
            case USER_MINUTES:
                gum = db.getConnection().prepareStatement(GET_USER_MINUTES);
                gum.setInt(1, uid);
                rs = gum.executeQuery();
                break;
            case ALL_MINUTES:
                gam = db.getConnection().prepareStatement(GET_ALL_MINUTES);
                rs = gam.executeQuery();
                break;
            default:
                gam = db.getConnection().prepareStatement(GET_ALL_MINUTES);
                rs = gam.executeQuery();
                break;
        }

        while (rs.next()) {
            minutes_list.add(new Minute(
                    rs.getInt("mid"),
                    rs.getInt("uid"),
                    rs.getString("sender"),
                    rs.getString("sender_office"),
                    rs.getString("type"),
                    rs.getInt("num_pages"),
                    rs.getInt("tally"),
                    rs.getString("notes"),
                    rs.getDate("date_added").toLocalDate()
            ));
        }
        return minutes_list;
    }
}
