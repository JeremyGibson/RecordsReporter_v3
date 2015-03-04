package MinutesMod;

import LoginMod.classes.User;
import Models.Contact;
import Models.Minute;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import libs.*;
import org.controlsfx.control.textfield.TextFields;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.Collection;

/**
 * Created by jgibson on 3/3/2015.
 */
public class AddMinutesController implements ControlledScreen {
    private Database db;
    private User user;

    @FXML private DatePicker date_added;
    @FXML private TextField tf_sender;
    @FXML private TextField tf_sender_office;
    @FXML private TextField tf_type;
    @FXML private TextField tf_num_pages;
    @FXML private TextField tf_tally;
    @FXML private HTMLEditor notes;
    @FXML private Button btn_add;

    private int current_mid;
    private Minute minute;
    private boolean editing;
    private Stage parent;
    private TableView<Minute> tblview;


    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {
        //STUB
    }

    @Override
    public void setUser(User u) {
        this.user = u;
    }

    @Override
    public void init() {
        date_added.setValue(LocalDate.now());
        tf_num_pages.setText("1");
        tf_tally.setText("1");
        try {
            TextFields.bindAutoCompletion(
                    tf_sender,
                    (Collection) Lookups.auto_fill_text("minutes", "sender", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_sender_office,
                    (Collection) Lookups.auto_fill_text("minutes", "sender_office", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_type,
                    (Collection) Lookups.auto_fill_text("minutes", "type", user.getUid(), db)
            );
        } catch (SQLException e) {
            System.out.println("");
        }

    }

    public void init(Minute min) throws SQLException {
        current_mid = min.getMid();
        minute = min;
        try {
            TextFields.bindAutoCompletion(
                    tf_sender,
                    (Collection) Lookups.auto_fill_text("minutes", "sender", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_sender_office,
                    (Collection) Lookups.auto_fill_text("minutes", "sender_office", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_type,
                    (Collection) Lookups.auto_fill_text("minutes", "type", user.getUid(), db)
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getMinuteToEdit();
    }

    private void getMinuteToEdit() throws SQLException {
        String sql = "SELECT * FROM minutes where mid=?";
        editing = true;
        PreparedStatement ps = db.getConnection().prepareStatement(sql);

        ps.setInt(1, current_mid);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new SQLException(String.format("No Contact found with CID=%s", current_mid));
        }
        date_added.setValue(rs.getDate("date_added").toLocalDate());
        tf_sender.setText(rs.getString("sender"));
        tf_sender_office.setText(rs.getString("sender_office"));
        tf_type.setText(rs.getString("type"));
        tf_num_pages.setText(rs.getString("num_pages"));
        tf_tally.setText(rs.getString("tally"));
        notes.setHtmlText(rs.getString("notes"));
        ps.close();
        btn_add.setText("Edit Minute");
    }

    public void setParent(Stage s) {
        parent = s;
    }

    private void addMinute() throws SQLException {
        int mid;
        //<editor-fold desc="Insert Code">
        PreparedStatement ps = db.getConnection().prepareStatement("INSERT INTO minutes " +
                "VALUES (null,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, user.getUid());
        ps.setString(2, tf_sender.getText());
        ps.setString(3, tf_sender_office.getText());
        ps.setString(4, tf_type.getText());
        ps.setInt(5, Integer.parseInt(tf_num_pages.getText()));
        ps.setInt(6, Integer.parseInt(tf_tally.getText()));
        ps.setString(7, notes.getHtmlText());
        try {
            ps.setLong(8, new EasyDate(date_added.getValue()).getSqlDateAsLong());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.setLong(9, new EasyDate().getDateAsLong());
        int affectedRows = ps.executeUpdate();
        if(affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                mid = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            generatedKeys.close();
        }
        ps.close();
        //</editor-fold>

        //<editor-fold desc="Update Table">
        minute = new Minute(
                mid,
                user.getUid(),
                tf_sender.getText(),
                tf_sender_office.getText(),
                tf_type.getText(),
                Integer.parseInt(tf_num_pages.getText()),
                Integer.parseInt(tf_tally.getText()),
                notes.getHtmlText(),
                date_added.getValue()
        );
        tblview.getItems().add(0, minute);
        //</editor-fold>
    }

    private void editMinute() throws SQLException {
        //<editor-fold desc="Update Statement">
        PreparedStatement ps = db.getConnection().prepareStatement("UPDATE minutes " +
                "SET " +
                "sender=?, " + //1
                "sender_office=?, " + //2
                "type=?, " + //3
                "num_pages=?, " + //4
                "tally=?, " + //5
                "notes=?, " + //6
                "date_added=?, " + //7
                "date_modified=? " + //8
                "WHERE " +
                "mid=?"); //11
        //</editor-fold>
        minute.setSender(tf_sender.getText());
        minute.setSender_office(tf_sender_office.getText());
        minute.setType(tf_type.getText());
        minute.setNum_pages(Integer.parseInt(tf_num_pages.getText()));
        minute.setTally(Integer.parseInt(tf_tally.getText()));
        minute.setNotes(notes.getHtmlText());
        minute.setDate_added(date_added.getValue());

        ps.setString(1, minute.getSender());
        ps.setString(2, minute.getSender_office());
        ps.setString(3, minute.getType());
        ps.setInt(4, minute.getNum_pages());
        ps.setInt(5, minute.getTally());
        ps.setString(6, minute.getNotes());
        try {
            ps.setLong(7, new EasyDate(minute.getDate_added()).getDateAsLong());
            ps.setLong(8, new EasyDate(LocalDate.now()).getDateAsLong());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }

    @FXML private void handle_add() throws SQLException {
        if(editing == false) {
            addMinute();
        } else {
            editMinute();
            parent.close();
        }
    }

    public void registerTable(TableView<Minute> t) {
        tblview = t;
    }

    @FXML private void handle_reset() {
        reset();
    }

    private void reset() {
        tf_sender.setText("");
        tf_sender_office.setText("");
        tf_type.setText("");
        tf_num_pages.setText("1");
        tf_tally.setText("1");
        notes.setHtmlText("");
    }

}
