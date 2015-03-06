package db_export;

import libs.Database;
import libs.EasyDate;

import javax.sql.rowset.CachedRowSet;
import javax.xml.bind.SchemaOutputResolver;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * Created by jgibson on 3/2/2015.
 */
public class exportContacts {

    private static final String old_db_path = "L:\\Intranet\\ar\\Government_Records\\Records_Analysis\\RecReporterDBs\\export\\";
    private static final String new_db_path = "H:\\Development\\Intellij\\RecordsReporter_v3\\";
    private static final String SELECT_FROM_OLD = "SELECT * FROM Contacts";
    private static final String INSERT_INTO_NEW = "INSERT INTO contacts VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private String test = "INSERT INTO contacts VALUES (null, %d, '%s', %d, %d, '%s', '%s', " +
            "'%s', '%s', %d, %d, %d, '%s', '%s')";
    private PreparedStatement insert_into;

    public exportContacts() throws SQLException, ParseException {
        Database new_db = new Database(new_db_path, "rrv3.db3");
        Database old_db = new Database(old_db_path, "rreports_export.db");
        insert_into = new_db.getConnection().prepareStatement(INSERT_INTO_NEW);
        EasyDate ed = new EasyDate();

        CachedRowSet resultSet = old_db.read(SELECT_FROM_OLD);
        while(resultSet.next()) {
            EasyDate cDate = new EasyDate(resultSet.getString("contact_date"));
            EasyDate iDate = new EasyDate(resultSet.getString("insert_date"));
            EasyDate mDate = new EasyDate("2015-03-02");
            /*
            new_db.create(String.format(test,
                    resultSet.getInt("analyst"),
                    resultSet.getString("contact_date"),
                    resultSet.getInt("agency_type"),
                    resultSet.getInt("contact_type"),
                    resultSet.getString("contact_agency"),
                    resultSet.getString("contact_agency_office"),
                    resultSet.getString("contact_person"),
                    resultSet.getString("contact_description"),
                    resultSet.getInt("contact_class"),
                    resultSet.getInt("num_contacts"),
                    resultSet.getInt("additional_analysts"),
                    resultSet.getString("insert_date"),
                    "2015-03-02"));
            */
            System.out.println(String.format("Inserting %s into new Database", resultSet.getString("contact_date")));
            insert_into.setInt(1, resultSet.getInt("analyst"));
            insert_into.setLong(2, cDate.getSqlDateAsLong());
            insert_into.setInt(3, resultSet.getInt("agency_type"));
            insert_into.setInt(4, resultSet.getInt("contact_type"));
            insert_into.setString(5, resultSet.getString("contact_agency"));
            insert_into.setString(6, resultSet.getString("contact_agency_office"));
            insert_into.setString(7, resultSet.getString("contact_person"));
            insert_into.setString(8, resultSet.getString("contact_description"));
            insert_into.setInt(9, resultSet.getInt("contact_class"));
            insert_into.setInt(10, resultSet.getInt("num_contacts"));
            insert_into.setInt(11, resultSet.getInt("additional_analysts"));
            insert_into.setLong(12, iDate.getSqlDateAsLong());
            insert_into.setLong(13, mDate.getSqlDateAsLong());
            insert_into.executeUpdate();

        }
        System.out.println("Export Finished");
    }

    public static void main(String [] args) {
        try {
            new exportContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}


