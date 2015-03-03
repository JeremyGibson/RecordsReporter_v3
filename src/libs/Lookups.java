package libs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by jgibson on 2/16/2015.
 */
public class Lookups {

    public static final ObservableList<String> slu = FXCollections.observableArrayList(
            //State = 0
            //Local = 1
            //University = 2
            "State", "Local", "University"
    );

    public static final ObservableList<String> type_contact = FXCollections.observableArrayList(
            //Inquiry = 0
            //Consult = 1
            //Visit= 2
            "Inquiry", "Consult", "Visit"
    );

    public static final ObservableList<String> contact_class = FXCollections.observableArrayList(
            //Government = 0
            //Media = 1
            //Private = 2
            "Government", "Media", "Private"
    );

    public static final HashMap<String, Integer> slu_lookup;
    static {
        slu_lookup = new HashMap<>();
        slu_lookup.put("State", 0);
        slu_lookup.put("Local", 1);
        slu_lookup.put("University", 2);
    }

    public static final HashMap<String, Integer> type_contact_lookup;
    static {
        type_contact_lookup = new HashMap<>();
        type_contact_lookup.put("Inquiry", 0);
        type_contact_lookup.put("Consult", 1);
        type_contact_lookup.put("Visit", 2);
    }

    public static final HashMap<String, Integer> contact_class_lookup;
    static {
        contact_class_lookup = new HashMap<>();
        contact_class_lookup.put("Government", 0);
        contact_class_lookup.put("Media", 1);
        contact_class_lookup.put("Private", 2);
    }

    public static final ObservableList<String> contacts_auto_fill_text(String column_name, int uid, Database db) throws SQLException {
        String sql = String.format("SELECT %s FROM contacts where uid=%d", column_name, uid);
        CachedRowSet rs = db.read(sql);
        ObservableList<String> list = FXCollections.observableArrayList();
        while(rs.next()) {
            String s = rs.getString(column_name);
            if(!list.contains(s))
                list.add(s);
        }
        return list;
    }


}
