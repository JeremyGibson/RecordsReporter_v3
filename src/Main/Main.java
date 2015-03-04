package Main;

import ContactMod.ContactsController;
import LoginMod.classes.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import LoginMod.ControlLogin;
import LoginMod.classes.User;
import libs.Database;
import org.controlsfx.dialog.Dialogs;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    ControlLogin controlLogin;
    ContactsController mainController;
    private Stage mainStage;
    private User user;
    private static final int CONTACTS = 1;
    private int current_view = 1;
    Database db;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.setProperty("db_path", "H:\\Development\\Intellij\\RecordsReporter_v3\\");
        System.setProperty("db_name", "rrv3.db3");
        db = new Database();
        mainStage = primaryStage;
        primaryStage.setTitle("Records Reporter V3");
        showLoginView();
        System.setProperty("uid", String.format("%s", user.getUid()));
        showMainView();

    }

    public void showLoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../LoginMod/login.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Login");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(mainStage);

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        controlLogin = loader.getController();
        controlLogin.setDatabase(db);
        controlLogin.init();
        if(user != null)
            controlLogin.setUser(user);
        controlLogin.setDialogStage(dialogStage);
        dialogStage.showAndWait();

        if(controlLogin.getValidated()) {
            user = controlLogin.user;
        } else {
            Dialogs.create()
                    .title("No Login")
                    .masthead("User not found")
                    .message("Your email and password must match the email and password you first setup")
                    .showWarning();
            showLoginView();
        }
    }

    public void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main_view.fxml"));
        mainStage.setTitle(String.format("%s %s's Main Page", user.getFirst_name(), user.getLast_name()));
        mainStage.setScene(new Scene((AnchorPane) loader.load()));
        mainStage.getScene().getStylesheets().add("Main/main.css");
        MainViewController mvc = loader.<MainViewController>getController();
        mvc.setUser(user);
        mvc.setDatabase(db);
        mvc.init_screens();
        mainStage.show();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void stop() {
        try {
            new Login(db).log_them_out(user.getUid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
