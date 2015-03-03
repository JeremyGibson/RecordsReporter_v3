package ContactMod;

import Models.Contact;
import org.controlsfx.control.textfield.TextFields;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import libs.*;
import LoginMod.classes.User;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;


/**
 * Created by jgibson on 2/10/2015.
 */
public class AddContactsController implements ControlledScreen {
    @FXML ComboBox combob_slu;
    @FXML ComboBox combob_type_contact;
    @FXML ComboBox combob_contact_class;
    @FXML DatePicker contact_date;
    @FXML ListView lv_additional_analysts;
    @FXML TextField tf_contact_name;
    @FXML TextField tf_office;
    @FXML TextField tf_agency;
    @FXML TextField tf_num_contacts;
    @FXML HTMLEditor description;
    @FXML Button btn_reset;
    @FXML Button btn_add_contact;
    @FXML CheckBox cxbx_add_analyst;


    private int current_cid;
    private User user;
    private Contact this_contact;
    private TableView<Contact> tblview;
    private boolean editing = false;
    private ObservableList<String> pre_edit_additional;
    private Stage parent;
    private Database db;

    @FXML
    private void initialize() {
        db = new Database();
        editing = false;
    }

    public void init() {
        combob_slu.setItems(Lookups.slu);
        combob_slu.getSelectionModel().selectFirst();
        combob_type_contact.setItems(Lookups.type_contact);
        combob_type_contact.getSelectionModel().selectFirst();
        combob_contact_class.setItems(Lookups.contact_class);
        combob_contact_class.getSelectionModel().selectFirst();
        contact_date.setValue(LocalDate.now());
        tf_num_contacts.setText("1");
        try {
            TextFields.bindAutoCompletion(
                    tf_contact_name, (Collection) Lookups.contacts_auto_fill_text("contact_person", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_agency, (Collection) Lookups.contacts_auto_fill_text("contact_agency", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_office, (Collection) Lookups.contacts_auto_fill_text("contact_agency_office", user.getUid(), db)
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        editing = false;
    }

    public void init(Contact c) throws SQLException {
        current_cid = c.getCid();
        this_contact = c;
        combob_slu.setItems(Lookups.slu);
        combob_type_contact.setItems(Lookups.type_contact);
        combob_contact_class.setItems(Lookups.contact_class);
        try {
            TextFields.bindAutoCompletion(
                    tf_contact_name, (Collection) Lookups.contacts_auto_fill_text("contact_person", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_agency, (Collection) Lookups.contacts_auto_fill_text("contact_agency", user.getUid(), db)
            );
            TextFields.bindAutoCompletion(
                    tf_office, (Collection) Lookups.contacts_auto_fill_text("contact_agency_office", user.getUid(), db)
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        getContactToEdit();
    }

    private void getContactToEdit() throws SQLException {
        String sql = "SELECT * FROM contacts where cid=?";
        editing = true;
        PreparedStatement ps = db.getConnection().prepareStatement(sql);

        ps.setInt(1, current_cid);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            throw new SQLException(String.format("No Contact found with CID=%s", current_cid));
        }
        combob_slu.getSelectionModel().select(rs.getInt("agency_type"));
        combob_type_contact.getSelectionModel().select(rs.getInt("contact_type"));
        combob_contact_class.getSelectionModel().select(rs.getInt("contact_class"));
        contact_date.setValue(rs.getDate("contact_date").toLocalDate());
        tf_agency.setText(rs.getString("contact_agency"));
        tf_office.setText(rs.getString("contact_agency_office"));
        tf_contact_name.setText(rs.getString("contact_person"));
        description.setHtmlText(rs.getString("contact_description"));
        tf_num_contacts.setText(Integer.toString(rs.getInt("num_contacts")));
        if(rs.getInt("additional_analysts") == 1) {
            handle_analyst_cxbx();
            cxbx_add_analyst.selectedProperty().setValue(true);
            String additional = "SELECT DISTINCT additional_analysts.uid, users.f_name, users.l_name " +
                    "FROM additional_analysts, users " +
                    "WHERE additional_analysts.cid=? " +
                    "AND additional_analysts.uid = users.uid";
            PreparedStatement ps1 = db.getConnection().prepareStatement(additional);
            ps1.setInt(1, current_cid);
            ResultSet adRs = ps1.executeQuery();
            while(adRs.next()) {
                lv_additional_analysts.getSelectionModel()
                        .select(adRs.getString("f_name") + " " + adRs.getString("l_name"));
            }
            pre_edit_additional = FXCollections.observableArrayList(lv_additional_analysts.getSelectionModel().getSelectedItems());
            FXCollections.copy(pre_edit_additional, lv_additional_analysts.getSelectionModel().getSelectedItems());
        }
        db.closeConnection();
        btn_add_contact.setText("Edit Contact");
    }

    @FXML
    private void handle_add_contact() throws SQLException {
        if(editing == false) {
            addContact();
        } else {
            editContact();
            parent.close();
        }
    }

    private void addContact() throws SQLException {
        int cid;
        PreparedStatement ps = db.getConnection().prepareStatement("INSERT INTO contacts " +
                "VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement add_analysts = db.getConnection().prepareStatement("INSERT INTO additional_analysts " +
                "VALUES (?,?)");

        //<editor-fold desc="Setup and Execute PreparedStatement">
        ps.setInt(1, user.getUid());
        try {
            ps.setLong(2, new EasyDate(contact_date.getValue()).getSqlDateAsLong());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ps.setInt(3, Lookups.slu_lookup.get(combob_slu.getValue().toString()));
        ps.setInt(4, Lookups.type_contact_lookup.get(combob_type_contact.getValue().toString()));
        ps.setString(5, tf_agency.getText());
        ps.setString(6, tf_office.getText());
        ps.setString(7, tf_contact_name.getText());
        ps.setString(8, description.getHtmlText());
        ps.setInt(9, Lookups.contact_class_lookup.get(combob_contact_class.getValue().toString()));
        ps.setInt(10, Integer.parseInt(tf_num_contacts.getText()));
        if(lv_additional_analysts.isDisabled()) {
            ps.setInt(11, 1);
        } else {
            ps.setInt(11, 0);
        }
        ps.setLong(12, new EasyDate().getSqlDateAsLong());
        ps.setLong(13, new EasyDate().getDateAsLong());

        int affectedRows = ps.executeUpdate();
        if(affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                cid = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            generatedKeys.close();
        }
        ps.close();
        //</editor-fold>

        //<editor-fold desc="Check for Additional Analysts">
        int xtra_analysts = 0;
        if(!lv_additional_analysts.isDisabled()) {
            xtra_analysts = 1;
            ObservableList<String> analysts = lv_additional_analysts.getSelectionModel().getSelectedItems();
            for(String s : analysts) {
                createAA(add_analysts, cid, s);
            }
        }
        add_analysts.close();
        //</editor-fold>

        //<editor-fold desc="Add the Contact to TableView">
        this_contact = new Contact(cid,
                user.getUid(),
                user.getFirst_name() + " " + user.getLast_name(),
                contact_date.getValue(),
                LocalDate.now(),
                Lookups.slu_lookup.get(combob_slu.getValue().toString()),
                Lookups.type_contact_lookup.get(combob_type_contact.getValue().toString()),
                tf_agency.getText(),
                tf_office.getText(),
                tf_contact_name.getText(),
                description.getHtmlText(),
                Lookups.contact_class_lookup.get(combob_contact_class.getValue().toString()),
                Integer.parseInt(tf_num_contacts.getText()),
                xtra_analysts,
                LocalDate.now());
        tblview.getItems().add(0,this_contact);
        //</editor-fold>

        reset();
    }

    private void editContact() throws SQLException {

        //<editor-fold desc="Update Statement">
        PreparedStatement ps = db.getConnection().prepareStatement("UPDATE contacts " +
                "SET " +
                "contact_date=?, " + //1
                "agency_type=?, " + //2
                "contact_type=?, " + //3
                "contact_agency=?, " + //4
                "contact_agency_office=?, " + //5
                "contact_person=?, " + //6
                "contact_description=?, " + //7
                "contact_class=?, " + //8
                "num_contacts=?, " + //9
                "additional_analysts=? " + //10
                "WHERE " +
                "cid=?"); //11
        //</editor-fold>


        try {
            ps.setDate(1, new EasyDate(contact_date.getValue()).getSql_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.setInt(2, Lookups.slu_lookup.get(combob_slu.getValue().toString()));
        this_contact.setAgency_type(Lookups.slu_lookup.get(combob_slu.getValue().toString()));

        ps.setInt(3, Lookups.type_contact_lookup.get(combob_type_contact.getValue().toString()));
        this_contact.setContact_date(contact_date.getValue());

        ps.setString(4, tf_agency.getText());
        this_contact.setContact_agency(tf_agency.getText());

        ps.setString(5, tf_office.getText());
        this_contact.setContact_agency_office(tf_office.getText());

        ps.setString(6, tf_contact_name.getText());
        this_contact.setContact_person(tf_contact_name.getText());

        ps.setString(7, description.getHtmlText());
        this_contact.setContact_description(description.getHtmlText());

        ps.setInt(8, Lookups.contact_class_lookup.get(combob_contact_class.getValue().toString()));
        this_contact.setContact_class(Lookups.contact_class_lookup.get(combob_contact_class.getValue().toString()));

        ps.setInt(9, Integer.parseInt(tf_num_contacts.getText()));
        this_contact.setNum_contacts(Integer.parseInt(tf_num_contacts.getText()));

        //<editor-fold desc="Handle Additional Analyst Edits">
        PreparedStatement add_analysts = db.getConnection().prepareStatement("INSERT INTO additional_analysts " +
                "VALUES (?,?)");
        //Check to see if nothing has changed
        if((pre_edit_additional == null || pre_edit_additional.size() == 0) && lv_additional_analysts.getSelectionModel().getSelectedItems().size() == 0) {
            ps.setInt(10, 0);
            ps.setInt(11, current_cid);
            ps.executeUpdate();
            ps.close();
            return;
        }

        //Rather than trying to figure out what has changed remove and rebuild
        String update = "DELETE FROM additional_analysts where cid=?";
        PreparedStatement aa = db.getConnection().prepareStatement(update);
        aa.setInt(1, current_cid);
        aa.executeUpdate();

        ps.setInt(10, 0);
        if(!lv_additional_analysts.isDisabled()) {
            ps.setInt(10, 1);
            ObservableList<String> analysts = lv_additional_analysts.getSelectionModel().getSelectedItems();
            for(String s : analysts) {
                createAA(add_analysts, current_cid, s);
            }
        }

        ps.setInt(11, current_cid);
        ps.executeUpdate();
        ps.close();
        aa.close();
        add_analysts.close();
    //</editor-fold>
    }

    private void createAA(PreparedStatement ps, int cid, String analyst) throws SQLException {
        ps.setInt(1, cid);
        ps.setInt(2, user.getOther_users().get(analyst));
        ps.executeUpdate();
    }

    @FXML
    private void handle_reset() {
        reset();
    }

    private void reset() {
        combob_slu.getSelectionModel().selectFirst();
        combob_type_contact.getSelectionModel().selectFirst();
        combob_contact_class.getSelectionModel().selectFirst();
        lv_additional_analysts.getSelectionModel().clearSelection();
        tf_contact_name.setText("");
        tf_office.setText("");
        tf_agency.setText("");
        tf_num_contacts.setText("");
        description.setHtmlText("");
    }

    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {

    }

    public void setParent(Stage s) {
        parent = s;
    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }

    public void registerTable(TableView<Contact> t) {
        tblview = t;
    }

    @FXML
    private void handle_analyst_cxbx() {
        UserList ul = new UserList(user);
        lv_additional_analysts.setItems(ul.getList(ul.USER_LIST_NO_CURRENT));
        lv_additional_analysts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lv_additional_analysts.setDisable(!lv_additional_analysts.isDisabled());
        lv_additional_analysts.getSelectionModel().clearSelection();
    }
}
