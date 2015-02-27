package libs;

import LoginMod.classes.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import Main.Main;
import Main.MainViewController;

import java.util.HashMap;

/**
 * Created by jgibson on 2/13/2015.
 */
public class ScreenViewSwitcher extends StackPane {
    private HashMap<String, Node> screens = new HashMap<String, Node>();
    private HashMap<String, ControlledScreen> controllers = new HashMap<String, ControlledScreen>();
    private HashMap<String, FXMLLoader> loaders = new HashMap<String, FXMLLoader>();
    private MainViewController mvc;
    private User user;

    public ScreenViewSwitcher() {
        super();
    }

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(Main.class.getResource(resource));
            loaders.put(name, myLoader);
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());
            myScreenController.setScreenParent(this);
            myScreenController.setUser(user);
            myScreenController.init();
            setUserData(myScreenController);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Node getCurrentPane(String pane_name) {
        return screens.get(pane_name);
    }

    public FXMLLoader getLoader(String name) {
        return loaders.get(name);
    }

    public void registerRoot(MainViewController rvc) {
        this.mvc = rvc;
    }

    public void registerUser(User u) {
        user = u;
    }

    public void activatePane(String view) {
        mvc.switch_pane(view);
    }
}
