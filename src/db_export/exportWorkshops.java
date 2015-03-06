package db_export;

import libs.Database;
import libs.EasyDate;
import libs.Lookups;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * Created by jgibson on 3/2/2015.
 */
public class exportWorkshops {

    private static final String old_db_path = "L:\\Intranet\\ar\\Government_Records\\Records_Analysis\\RecReporterDBs\\export\\";
    private static final String new_db_path = "H:\\Development\\Intellij\\RecordsReporter_v3\\";
    private static final String SELECT_FROM_OLD = "SELECT * FROM Workshops";
    private static final String INSERT_INTO_NEW = "INSERT INTO workshops VALUES (null,?,?,?,?,?,?,?,?,?)";
    private static final String ADD_UIDS = "INSERT INTO workshop_uids VALUES (?,?)";
    private PreparedStatement insert_into;
    private PreparedStatement uids;
    String[] initials;
    boolean additional_uids = false;

    public exportWorkshops() throws SQLException, ParseException {
        Database new_db = new Database(new_db_path, "rrv3.db3");
        Database old_db = new Database(old_db_path, "rreports_export.db");
        Connection conn = new_db.getConnection();

        EasyDate ed = new EasyDate();
        int x = 1;
        CachedRowSet resultSet = old_db.read(SELECT_FROM_OLD);
        while(resultSet.next()) {
            insert_into = conn.prepareStatement(INSERT_INTO_NEW);
            EasyDate cDate = new EasyDate(resultSet.getString("workshop_date"));
            EasyDate mDate = new EasyDate(LocalDate.now());

            System.out.println(String.format("Inserting record %s into new database", x));

            insert_into.setInt(1, exportLookups.analyst_lookup.get(resultSet.getString("analyst")));
            insert_into.setLong(2, cDate.getDateAsLong());
            if(handleAdditionalStaff(resultSet.getString("staff")) == 0) {
                insert_into.setInt(3, 0);
            } else {
                insert_into.setInt(3, 1);
                additional_uids = true;
            }
            insert_into.setString(4, resultSet.getString("location"));
            insert_into.setInt(5, resultSet.getInt("num_attending"));
            insert_into.setInt(6, Lookups.workshop_type_lookup.get(resultSet.getString("workshop_type")));
            insert_into.setString(7, resultSet.getString("description"));
            insert_into.setLong(8, cDate.getSqlDateAsLong());
            insert_into.setLong(9, mDate.getSqlDateAsLong());
            insert_into.executeUpdate();
            insert_into.close();
            if(additional_uids) {
                uids = conn.prepareStatement(ADD_UIDS);
                for (String s : initials) {
                    uids.setInt(1, x);
                    uids.setInt(2, exportLookups.analyst_lookup.get(s));
                    uids.executeUpdate();
                }
                uids.close();
            }
            x++;
        }
        System.out.println("Export Finished");
    }

    private int handleAdditionalStaff(String staff_line) {
        String strip_brackets = staff_line.replace("[","");
        strip_brackets = strip_brackets.replace("]","");
        strip_brackets = strip_brackets.replace(" ", "");
        if(strip_brackets.equals("")) { return 0;}
        initials = strip_brackets.split(",");
        return 1;
    }


    public static void main(String [] args) {
        try {
            new exportWorkshops();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}


