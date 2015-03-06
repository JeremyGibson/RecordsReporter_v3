package db_export;

import libs.Database;
import libs.EasyDate;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by jgibson on 3/2/2015.
 */
public class exportMinutes {

    private static final String old_db_path = "L:\\Intranet\\ar\\Government_Records\\Records_Analysis\\RecReporterDBs\\export\\";
    private static final String new_db_path = "H:\\Development\\Intellij\\RecordsReporter_v3\\";
    private static final String SELECT_FROM_OLD = "SELECT * FROM Minutes";
    private static final String INSERT_INTO_NEW = "INSERT INTO minutes VALUES (null,?,?,?,?,?,?,?,?,?)";
    private PreparedStatement insert_into;

    public exportMinutes() throws SQLException, ParseException {
        Database new_db = new Database(new_db_path, "rrv3.db3");
        Database old_db = new Database(old_db_path, "rreports_export.db");
        Connection conn = new_db.getConnection();
        insert_into = conn.prepareStatement(INSERT_INTO_NEW);
        EasyDate ed = new EasyDate();
        int x = 1;
        CachedRowSet resultSet = old_db.read(SELECT_FROM_OLD);
        while(resultSet.next()) {
            EasyDate cDate = new EasyDate(resultSet.getString("date_added"));
            EasyDate mDate = new EasyDate("2015-03-02");

            System.out.println(String.format("Inserting record %s into new database", x));
            insert_into.setInt(1, resultSet.getInt("analyst"));
            insert_into.setString(2, resultSet.getString("sender"));
            insert_into.setString(3, resultSet.getString("sender_office"));
            insert_into.setString(4, resultSet.getString("type"));
            insert_into.setInt(5, resultSet.getInt("num_pages"));
            insert_into.setInt(6, resultSet.getInt("tally"));
            insert_into.setString(7, resultSet.getString("notes"));
            insert_into.setLong(8, cDate.getSqlDateAsLong());
            insert_into.setLong(9, mDate.getSqlDateAsLong());
            insert_into.executeUpdate();
            x++;
        }
        System.out.println("Export Finished");
    }

    public static void main(String [] args) {
        try {
            new exportMinutes();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}


