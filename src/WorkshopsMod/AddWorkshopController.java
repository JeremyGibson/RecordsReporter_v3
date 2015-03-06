package WorkshopsMod;

import LoginMod.classes.User;
import Models.Workshop;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import libs.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * Created by jgibson on 3/4/2015.
 */
public class AddWorkshopController implements ControlledScreen {
    private TableView<Workshop> current_table;
    private Stage parent;
    private User user;
    private Database db;
    private Workshop current_workshop;
    private boolean editing = false;


    @FXML private DatePicker workshop_date;
    @FXML private TextField workshop_location;
    @FXML private ComboBox<String> workshop_type;
    @FXML private TextField num_attending;
    @FXML private ListView<String> staff;
    @FXML private HTMLEditor description;
    @FXML private CheckBox add_staff;
    @FXML private Button btn_add;
    @FXML private Button btn_reset;

    //<editor-fold desc="FXML Methods">
    @FXML private void handle_add() throws SQLException, ParseException {
        if(editing==false) {
            addWorkshop();
        } else {
            updateWorkshop();
            parent.close();
        }
    }

    @FXML private void handle_reset() {
        staff.getSelectionModel().clearSelection();
        workshop_location.setText("");
        num_attending.setText("0");
        workshop_type.getSelectionModel().select(0);
        description.setHtmlText("");
    }

    @FXML private void handle_analyst_cxbx() {
        UserList ul = new UserList(user);
        staff.setItems(ul.getList(ul.USER_LIST_NO_CURRENT));
        staff.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        staff.setDisable(!staff.isDisabled());
        staff.getSelectionModel().clearSelection();
    }
    //</editor-fold>


    //<editor-fold desc="Public Methods">

    //<editor-fold desc="Basic Overrides">
    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {

    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }

    public void registerTable(TableView<Workshop> sched) {
        current_table = sched;
    }

    public void setParent(Stage s) {
        parent = s;
    }
    //</editor-fold>

    @Override
    public void init() {
        workshop_date.setValue(LocalDate.now());
        workshop_type.setItems(Lookups.workshop_type);
        workshop_type.getSelectionModel().select(0);
    }

    public void init(Workshop s) {
        workshop_date.setValue(s.getWorkshop_date());
        workshop_type.setItems(Lookups.workshop_type);
        workshop_type.getSelectionModel().select(s.getWorkshop_type());
        num_attending.setText(Integer.toString(s.getNum_attending()));
        workshop_location.setText(s.getLocation());
        description.setHtmlText(s.getDescription_property());
    }
    //</editor-fold>


    //<editor-fold desc="Private Methods">
    private void addWorkshop() throws SQLException, ParseException {
        int wid;
        boolean additional_uids;
        PreparedStatement ps_main = db.getConnection().prepareStatement("" +
                "INSERT INTO workshops VALUES (null,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement ps_uids = db.getConnection().prepareStatement("" +
                "INSERT INTO workshop_uids VALUES(?,?)");

        //<editor-fold desc="ps_main">
        ps_main.setInt(1, user.getUid());
        ps_main.setLong(2, new EasyDate(workshop_date.getValue()).getDateAsLong());
        if(staff.getSelectionModel().getSelectedItems().size() > 0) {
            additional_uids = true;
            ps_main.setInt(3, 1);
        } else {
            additional_uids = false;
            ps_main.setInt(3, 0);
        }
        ps_main.setString(4, workshop_location.getText());
        ps_main.setInt(5, Integer.parseInt(num_attending.getText()));
        ps_main.setInt(6, workshop_type.getSelectionModel().getSelectedIndex());
        ps_main.setString(7, description.getHtmlText());
        ps_main.setLong(8, new EasyDate(LocalDate.now()).getDateAsLong());
        ps_main.setLong(9, new EasyDate(LocalDate.now()).getDateAsLong());
        int affectedRows = ps_main.executeUpdate();
        if(affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try(ResultSet generatedKeys = ps_main.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                wid = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            generatedKeys.close();
        }
        ps_main.close();
        //</editor-fold>

        //<editor-fold desc="ps_uids">
        if(additional_uids) {
            for (String s : staff.getItems()) {
                ps_uids.setInt(1, wid);
                ps_uids.setInt(2, user.getOther_users().get(s));
                ps_uids.executeUpdate();
            }
            ps_uids.close();
        }
        //</editor-fold>

        current_workshop = new Workshop(
                wid,
                user.getUid(),
                workshop_date.getValue(),
                (additional_uids) ? 1:0,
                workshop_location.getText(),
                Integer.parseInt(num_attending.getText()),
                workshop_type.getSelectionModel().getSelectedIndex(),
                description.getHtmlText(),
                LocalDate.now(),
                LocalDate.now()
        );

        current_table.getItems().add(0, current_workshop);
        handle_reset();
    }

    private void updateWorkshop() throws SQLException, ParseException {

    }

    private String sanitize(String check_value) {
        if(check_value.matches("\\/")){
            check_value.replaceAll("\\/", "");
        }

        if(check_value.matches("\\\\")) {
            check_value.replaceAll("\\\\", "");
        }
        return check_value;
    }
    //</editor-fold>



}
