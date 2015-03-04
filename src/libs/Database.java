package libs;

import com.sun.rowset.CachedRowSetImpl;
import org.apache.log4j.Logger;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.xml.crypto.Data;

/**
 * Created by jgibson on 6/12/2014.
 */
public class Database implements CRUD {
    public static final int MSSQL = 0;
    public static final int MYSQL = 1;
    public static final int SQLITE = 2;
    private int CURRENT_DB_TYPE = 2;
    private static final int NO_RETURN_RESULT = 1;
    private static final int UPDATE = 2;
    private static final int RETURN_RESULT_SET = 3;
    private Connection connection;
    private Statement statement;
    private Logger log = Logger.getLogger(this.getClass());
    private ResultSet resultSet;
    private CachedRowSet keys;
    private CachedRowSet cachedRowSet;
    private ArrayList<List> processResults;
    private String lastQuery;
    //CONNECTION ITEMS
    private String database_path;
    private String database_name;
    private String user_name;
    private String password;
    private String sqlitens = "org.sqlite.JDBC";
    private String mysqlns = "com.mysql.jdbc.Driver";
    private String mssqlns = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String name_space = sqlitens;
    private String connection_string;


    public Database() {
        //Default to sqlite
        database_path = System.getProperty("db_path");
        database_name = System.getProperty("db_name");
        connection_string = String.format("jdbc:sqlite:%s%s",
                this.database_path,
                this.database_name);
        createConnection(SQLITE);
    }

    public Database(String path, String name) {
        //Default to sqlite
        database_name = name;
        database_path = path;
        connection_string = String.format("jdbc:sqlite:%s%s",
                this.database_path,
                this.database_name);
        createConnection(SQLITE);
    }

    public Database(String db_name, String user_name, String password) {
        //Mysql
        database_name = db_name;
        this.user_name = user_name;
        this.password = password;
        name_space = mysqlns;
        connection_string = String.format("jdbc:mysql://localhost/%s?user=%s&password=%s",
                this.database_name,
                this.user_name,
                this.password);
        CURRENT_DB_TYPE = MYSQL;
        createConnection(CURRENT_DB_TYPE);
    }

    public Database(String db_name) {
        database_name = db_name;
        connection_string = String.format("jdbc:sqlserver://WP3CRAP20t\\twelve;databaseName=GovRecWorkPlan;integratedSecurity=true");
        name_space = mssqlns;
        CURRENT_DB_TYPE = MSSQL;
        createConnection(CURRENT_DB_TYPE);
    }

    @Override
    public int create(String sql) {
        lastQuery = sql;
        return usualConnection(sql, NO_RETURN_RESULT);
    }

    @Override
    public int update(String sql) {
        lastQuery = sql;
        return usualConnection(sql, UPDATE);
    }

    @Override
    public CachedRowSet read(String sql) {
        lastQuery = sql;
        usualConnection(sql, RETURN_RESULT_SET);
        return cachedRowSet;
    }

    public CachedRowSet getCachedRowSet() {
        return cachedRowSet;
    }

    public CachedRowSet getKeys() {
        return keys;
    }

    @Override
    public int delete(String sql) {
        lastQuery = sql;
        return usualConnection(sql, NO_RETURN_RESULT);
    }

    public Connection getConnection() {
        try {
            if(connection.isClosed()) {
                createConnection(SQLITE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createConnection(int type) {
        try {
            Class.forName(name_space);
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection(connection_string, config.toProperties());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private int usualConnection(String sql, int type) {
        int success = 0;
        try {
            //if(!connection.isValid(5))
            //createConnection(CURRENT_DB_TYPE);
            statement = connection.createStatement();
            statement.setQueryTimeout(5);

            switch(type) {
                case NO_RETURN_RESULT:
                    success = statement.execute(sql) ? 1 : 0;
                    keys = new CachedRowSetImpl();
                    resultSet = statement.getGeneratedKeys();
                    keys.populate(resultSet);
                    cachedRowSet = keys;
                    statement.close();
                    resultSet.close();
                    break;
                case RETURN_RESULT_SET:
                    statement.execute(sql);
                    resultSet = statement.getResultSet();
                    cachedRowSet = new CachedRowSetImpl();
                    cachedRowSet.populate(resultSet);
                    break;
                case UPDATE:
                    success = statement.executeUpdate(sql);
                    statement.close();
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.debug(e.getMessage());
            return -1;
        }
        return success;
    }
}
