package LoginMod;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import LoginMod.classes.Login;
import LoginMod.classes.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControlLogin implements Initializable {
    @FXML TextField email;
    @FXML PasswordField password;
    @FXML Button lgn_btn;
    @FXML Button nu_btn;

    public Login login;
    public User user;
    private Stage dialogStage;

    Boolean validated_user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login = new Login();
        user = new User();
        validated_user = Boolean.FALSE;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField password) {
        this.password = password;
    }

    public Boolean getValidated() {
        return validated_user;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUser(User u) {
        this.user = u;
        this.email.setText(user.getEmail());
    }

    @FXML
    protected void validate_lgn() {
        try {
            if(login.checkLogin(getPassword().getText(), getEmail().getText())) {
                validated_user = Boolean.TRUE;
                user = login.getUser();
            } else {
                validated_user = Boolean.FALSE;
            }
            dialogStage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void create_new_user() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("new_user.fxml"));
        try {
            AnchorPane page = (AnchorPane) loader.load();
            Stage newUserDialog = new Stage();
            newUserDialog.setTitle("Add New User");
            newUserDialog.initModality(Modality.APPLICATION_MODAL);
            newUserDialog.initOwner(dialogStage);

            Scene scene = new Scene(page);
            newUserDialog.setScene(scene);
            //Set the controller
            final ControlNewUser nuc = loader.getController();
            nuc.setOwningStage(newUserDialog);

            newUserDialog.showAndWait();
            user = nuc.getUser();
            this.email.setText(user.getEmail());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
