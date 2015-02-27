package MinutesMod;

import LoginMod.classes.User;
import Models.Minute;
import TableModels.MinutesTableModel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import libs.ControlledScreen;
import libs.ScreenViewSwitcher;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by jgibson on 2/20/2015.
 */
public class MinutesController implements Initializable, ControlledScreen {
    ScreenViewSwitcher myController;
    User user;

    //<editor-fold desc="@FMXLs">
    @FXML private TableColumn<Minute, Number> mid_col = new TableColumn<>();
    @FXML private TableColumn<Minute, LocalDate> date_added_col = new TableColumn<>();
    @FXML private TableColumn<Minute, String> sender_col = new TableColumn<>();
    @FXML private TableColumn<Minute, String> sender_office_col = new TableColumn<>();
    @FXML private TableColumn<Minute, String> type_col = new TableColumn<>();
    @FXML private TableColumn<Minute, Number> num_pages_col = new TableColumn<>();
    @FXML private TableColumn<Minute, Number> tally_col = new TableColumn<>();

    @FXML private Button add_contact;
    @FXML private Button delete_contact;

    @FXML private TableView<Minute> minutes_table = new TableView<Minute>();

    @FXML private void handleAddMinute() {

    }

    @FXML private void handleDelete() {

    }
    //</editor-fold>



    private static ObservableList<Minute> getList(int uid, int type) {
        MinutesTableModel mtm = new MinutesTableModel();
        ObservableList<Minute> minutes = null;
        try {
            minutes = mtm.getModel(uid, type);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return minutes;
    }

    private void editMinute(int mid) {

    }

    //<editor-fold desc="Overrides">
    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {
        myController = screenPage;
    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void init() {
        mid_col.setCellValueFactory(ud -> ud.getValue().midProperty());
        date_added_col.setCellValueFactory(ud->ud.getValue().date_addedProperty());
        sender_col.setCellValueFactory(ud->ud.getValue().senderProperty());
        sender_office_col.setCellValueFactory(ud->ud.getValue().sender_officeProperty());
        type_col.setCellValueFactory(ud->ud.getValue().typeProperty());
        num_pages_col.setCellValueFactory(ud->ud.getValue().num_pagesProperty());
        tally_col.setCellValueFactory(ud->ud.getValue().tallyProperty());

        final ObservableList<Minute> data = getList(user.getUid(), MinutesTableModel.ALL_MINUTES);
        minutes_table.setItems(data);
        minutes_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    //Edit Minutes here
                }

            }
        });
    }
    //</editor-fold>


}
