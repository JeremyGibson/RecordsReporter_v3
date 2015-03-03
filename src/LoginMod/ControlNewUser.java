package LoginMod;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import LoginMod.classes.Login;
import LoginMod.classes.User;
import libs.Database;

import java.sql.SQLException;

/**
 * Created by jgibson on 2/10/2015.
 */
public class ControlNewUser {
    @FXML
    private TextField nu_first_name;
    @FXML
    private TextField nu_last_name;
    @FXML
    private TextField nu_login;
    @FXML
    private TextField nu_email;
    @FXML
    private PasswordField nu_password;
    @FXML
    private Button nu_btn_added;
    @FXML
    private Button nu_btn_reset;

    //Controlling Stage
    private Stage ownerStage;

    //User
    private User user;

    private Database db;

    @FXML
    private void initialize() {

    }

    public void setOwningStage(Stage s) {
        this.ownerStage = s;
    }

    @FXML
    private void handleAdd() {
        user = new User();
        Login lgn = new Login(db);

        user.setFirst_name(nu_first_name.getText());
        user.setLast_name(nu_last_name.getText());
        user.setEmail(nu_email.getText());
        user.setPassword(nu_password.getText());
        user.setRole(1);

        try {
            lgn.addUser(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        ownerStage.close();
    }

    public User getUser() {
        return user;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    @FXML
    private void handleReset() {
        this.nu_email.setText("");
        this.nu_first_name.setText("");
        this.nu_last_name.setText("");
        this.nu_login.setText("");
        this.nu_password.setText("");
    }
}
