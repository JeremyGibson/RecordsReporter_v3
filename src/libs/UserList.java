package libs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import LoginMod.classes.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by jgibson on 2/16/2015.
 */
public class UserList {
    private User current_user;
    public int USER_LIST_NO_CURRENT = 0;
    public int USER_LIST_A = 1;
    private static final String USER_LIST_X_CURRENT = "SELECT * FROM users where uid !=?";
    private static final String USER_LIST_ALL = "SELECT * FROM users";


    public UserList(User current_user) {
        this.current_user = current_user;
    }

    public ObservableList<String> getList(int type) {
        Database db = new Database();
        ObservableList<String> ol = FXCollections.observableArrayList();
        PreparedStatement ps;
        try {
            if(type == USER_LIST_NO_CURRENT) {
                ps = db.getConnection().prepareStatement(USER_LIST_X_CURRENT);
                ps.setInt(1, current_user.getUid());
            } else {
                ps = db.getConnection().prepareStatement(USER_LIST_ALL);
            }
            ResultSet rs = ps.executeQuery();
            HashMap<String, Integer> hm = new HashMap<String, Integer>(ol.size());
            while (rs.next()) {
                ol.add(rs.getString("f_name") + " " + rs.getString("l_name"));
                hm.put(rs.getString("f_name") + " " + rs.getString("l_name"), rs.getInt("uid"));
            }

            if(ol.size() != current_user.sizeOfOtherUsers()) {
                current_user.setOther_users(hm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ol;
    }
}
