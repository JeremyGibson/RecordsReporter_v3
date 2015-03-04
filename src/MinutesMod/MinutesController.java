package MinutesMod;

import ContactMod.AddContactsController;
import LoginMod.classes.User;
import Models.Contact;
import Models.Minute;
import TableModels.MinutesTableModel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import libs.ControlledScreen;
import libs.Database;
import libs.ScreenViewSwitcher;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by jgibson on 2/20/2015.
 */
public class MinutesController implements Initializable, ControlledScreen {
    ScreenViewSwitcher myController;
    User user;
    Database db;

    //<editor-fold desc="@FMXLs">
    @FXML private TableColumn<Minute, Number> mid_col = new TableColumn<>();
    @FXML private TableColumn<Minute, LocalDate> date_added_col = new TableColumn<>();
    @FXML private TableColumn<Minute, String> sender_col = new TableColumn<>();
    @FXML private TableColumn<Minute, String> sender_office_col = new TableColumn<>();
    @FXML private TableColumn<Minute, String> type_col = new TableColumn<>();
    @FXML private TableColumn<Minute, Number> num_pages_col = new TableColumn<>();
    @FXML private TableColumn<Minute, Number> tally_col = new TableColumn<>();

    @FXML private Button add_entity;
    @FXML private Button delete_entity;

    @FXML private TableView<Minute> minutes_table = new TableView<Minute>();

    @FXML private void handleAdd() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_minutes.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Add Minutes");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddMinutesController amc = loader.<AddMinutesController>getController();
        amc.setUser(user);
        amc.registerTable(minutes_table);
        amc.setParent(dialogStage);
        amc.setDatabase(db);
        amc.init();
        dialogStage.show();
    }

    @FXML private void handleDelete() throws SQLException {
        String delete_contacts = "DELETE FROM minutes where mid=?";
        PreparedStatement ps = db.getConnection().prepareStatement(delete_contacts);
        for(Minute m : minutes_table.getSelectionModel().getSelectedItems()) {
            ps.setInt(1, m.getMid());
            ps.executeUpdate();
            minutes_table.getItems().remove(m);
        }
        ps.close();
    }
    //</editor-fold>

    private ObservableList<Minute> getList(int uid, int type) {
        MinutesTableModel mtm = new MinutesTableModel();
        ObservableList<Minute> minutes = null;
        try {
            minutes = mtm.getModel(uid, type, db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return minutes;
    }

    private void editMinute() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_minutes.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Edit Minute");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddMinutesController amc = loader.<AddMinutesController>getController();
        amc.setUser(user);
        amc.registerTable(minutes_table);
        amc.setParent(dialogStage);
        amc.setDatabase(db);
        amc.init(minutes_table.getSelectionModel().getSelectedItem());
        dialogStage.show();
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
    public void setDatabase(Database db) {
        this.db = db;
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
                    try {
                        editMinute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    //</editor-fold>


}
