package TableModels;

import Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import libs.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jgibson on 2/11/2015.
 */
public class ContactsTableModel {
    private ObservableList<Contact> contacts_list = FXCollections.observableArrayList();
    private static final String GET_USER_CONTACTS = "Select users.f_name, users.l_name, contacts.* " +
            "from users, contacts where contacts.uid=users.uid " +
            "AND contacts.uid=? ORDER BY contacts.contact_date DESC";
    private static final String GET_ALL_CONTACTS = "Select users.f_name, users.l_name, contacts.* " +
            "from users, contacts where contacts.uid=users.uid " +
            "ORDER BY contacts.contact_date DESC";
    public static final int USER_CONTACTS = 0;
    public static final int ALL_CONTACTS = 1;
    private PreparedStatement guc;
    private PreparedStatement gac;
    private Database db;
    private int uid;

    public ObservableList<Contact> getModel(int uid, int get_type) throws SQLException {
        this.uid = uid;
        db = new Database("mssql");
        ResultSet rs;
        switch (get_type) {
            case USER_CONTACTS:
                guc = db.getConnection().prepareStatement(GET_USER_CONTACTS);
                guc.setInt(1, uid);
                rs = guc.executeQuery();
                break;
            case ALL_CONTACTS:
                gac = db.getConnection().prepareStatement(GET_ALL_CONTACTS);
                rs = gac.executeQuery();
                break;
            default:
                gac = db.getConnection().prepareStatement(GET_ALL_CONTACTS);
                rs = gac.executeQuery();
                break;
        }

        while(rs.next()) {
            contacts_list.add(new Contact(
                    rs.getInt("cid"),
                    rs.getInt("uid"),
                    String.format("%s %s", rs.getString("f_name"), rs.getString("l_name")),
                    rs.getDate("contact_date").toLocalDate(),
                    rs.getDate("insert_date").toLocalDate(),
                    rs.getInt("agency_type"),
                    rs.getInt("contact_type"),
                    rs.getString("contact_agency"),
                    rs.getString("contact_agency_office"),
                    rs.getString("contact_person"),
                    rs.getString("contact_description"),
                    rs.getInt("contact_class"),
                    rs.getInt("num_contacts"),
                    rs.getInt("additional_analysts")
            ));
        }
        return  contacts_list;
    }
}
