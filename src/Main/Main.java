package Main;

import ContactMod.ContactsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import LoginMod.ControlLogin;
import LoginMod.classes.User;
import org.controlsfx.dialog.Dialogs;

import java.io.IOException;

public class Main extends Application {
    ControlLogin controlLogin;
    ContactsController mainController;
    private Stage mainStage;
    private User user;
    private static final int CONTACTS = 1;
    private int current_view = 1;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        primaryStage.setTitle("Records Reporter V3");
        //showLoginView();
        user = new User();
        user.setFirst_name("Jeremy");
        user.setLast_name("Gibson");
        user.setUid(1);
        user.setRole(0);
        System.setProperty("uid", String.format("%s", user.getUid()));
        showMainView();

    }

    public void showLoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginMod/login.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Login");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(mainStage);

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        controlLogin = loader.getController();
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
        MainViewController mvc = loader.<MainViewController>getController();
        mvc.setUser(user);
        mvc.init_screens();
        mainStage.show();
    }

    public User getUser() {
        return user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
