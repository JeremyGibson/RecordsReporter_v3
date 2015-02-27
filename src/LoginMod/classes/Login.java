package LoginMod.classes;

import libs.Database;
import org.apache.commons.codec.digest.DigestUtils;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jgibson on 2/9/2015.
 *
 *TABlE users SCHEMA
 *
 * uid in PK
 * f_name varchar(32)
 * l_name varchar(32)
 * logged_in int
 * role int (1 user, 0 admin)
 * email varchar(128)
 * password (MD5 Hex)
 *
 */
public class Login {
    private String DOES_USER_EXIST = "SELECT uid, f_name, l_name, email, role FROM users where password=? AND email=?";
    private String ADD_A_USER = "INSERT INTO users (f_name, l_name, logged_in, role, email, password) VALUES (?,?,?,?,?,?)";
    private String LOG_A_USER_IN = "UPDATE users SET logged_in=? where uid=?";
    private PreparedStatement due;
    private PreparedStatement aau;
    private PreparedStatement laui;
    private Database db;
    private User user;

    public Login() {
        db = new Database(new String("mssql"));
        try {
            due = db.getConnection().prepareStatement(DOES_USER_EXIST);
            aau = db.getConnection().prepareStatement(ADD_A_USER);
            laui = db.getConnection().prepareStatement(LOG_A_USER_IN);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean checkLogin(String password, String email) throws SQLException {
        String hash = DigestUtils.md5Hex(password + email);
        due.setString(1, hash);
        due.setString(2, email);
        ResultSet rs = due.executeQuery();
        if(!rs.isBeforeFirst()) {
            return false;
        }
        log_them_in(rs);
        return true;
    }


    public void addUser(User u) throws SQLException {
        user = u;
        aau.setString(1, user.getFirst_name());
        aau.setString(2, user.getLast_name());
        aau.setInt(3, 1);
        aau.setInt(4, user.getRole());
        aau.setString(5, user.getEmail());
        aau.setString(6, user.getPassword());
        aau.executeQuery();
    }

    public void log_them_in(ResultSet rs) throws SQLException {
        rs.next();
        user = new User();
        user.setFirst_name(rs.getString("f_name"));
        user.setLast_name(rs.getString("l_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getInt("role"));
        user.setUid(rs.getInt("uid"));
        laui.setInt(1, 1);
        laui.setInt(2, rs.getInt("uid"));
        laui.executeUpdate();
    }

    public void log_them_out(int uid) throws SQLException {
        laui.setInt(1, 0);
        laui.setInt(2, uid);
        laui.executeUpdate();
    }

    public User getUser() {
        return user;
    }
}
