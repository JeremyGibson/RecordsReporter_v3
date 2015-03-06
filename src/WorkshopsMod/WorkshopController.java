package WorkshopsMod;

import Models.Schedule;
import Models.Workshop;
import ScheduleMod.AddScheduleController;
import TableModels.SchedulesTableModel;
import TableModels.WorkshopsTableModel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import libs.ControlledScreen;
import libs.Database;
import libs.ScreenViewSwitcher;
import LoginMod.classes.User;


import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by jgibson on 2/13/2015.
 */
public class WorkshopController implements Initializable, ControlledScreen {
    private User user;
    private Database db;
    private Stage parent;


    @FXML private TableColumn<Workshop, LocalDate> workshop_date;
    @FXML private TableColumn<Workshop, String> staff;
    @FXML private TableColumn<Workshop, Number> num_attending;
    @FXML private TableColumn<Workshop, String> location;
    @FXML private TableColumn<Workshop, String> description;
    @FXML private TableView<Workshop> workshops_table;
    @FXML private Button add_entity;
    @FXML private Button delete_entity;



    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @Override
    public void init() {
        workshop_date.setCellValueFactory(ud->ud.getValue().workshop_dateProperty());
        staff.setCellValueFactory(ud->ud.getValue().getAdditionalAnalysts());
        num_attending.setCellValueFactory(ud->ud.getValue().num_attendingProperty());
        location.setCellValueFactory(ud->ud.getValue().locationProperty());
        description.setCellValueFactory(ud->ud.getValue().description_propertyProperty());

        final ObservableList<Workshop> data = getList(user.getUid(), WorkshopsTableModel.ALL_WORKSHOPS);
        workshops_table.setItems(data);
        workshops_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    try {
                        editWorkshop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }

    private ObservableList<Workshop> getList(int uid, int type) {
        WorkshopsTableModel wtm = new WorkshopsTableModel();
        ObservableList<Workshop> workshops = null;
        try {
            workshops = wtm.getModel(uid, type, db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return workshops;
    }

    private void editWorkshop() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_workshop.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Edit Workshop");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddWorkshopController aws = loader.<AddWorkshopController>getController();
        aws.setUser(user);
        aws.registerTable(workshops_table);
        aws.setParent(dialogStage);
        aws.setDatabase(db);
        aws.init(workshops_table.getSelectionModel().getSelectedItem());
        dialogStage.show();
    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }

    @FXML private void handleAdd() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_workshop.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Add Workshop");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddWorkshopController awc = loader.<AddWorkshopController>getController();
        awc.setUser(user);
        awc.registerTable(workshops_table);
        awc.setParent(dialogStage);
        awc.setDatabase(db);
        awc.init();
        dialogStage.show();
    }

    @FXML private void handleDelete() throws SQLException {
        String delete_workshops = "DELETE FROM workshops where wid=?";
        PreparedStatement ps = db.getConnection().prepareStatement(delete_workshops);
        for(Workshop w : workshops_table.getSelectionModel().getSelectedItems()) {
            ps.setInt(1, w.getWid());
            ps.executeUpdate();
            workshops_table.getItems().remove(w);
        }
        ps.close();
    }
}
