package libs;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;

/**
 * Created by jgibson on 6/12/2014.
 */
public interface CRUD {
    int create(String sql);
    int update(String sql);
    CachedRowSet read(String sql);
    int delete(String sql);
}
