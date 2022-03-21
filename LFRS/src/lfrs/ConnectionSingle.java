package lfrs;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author NatalieM
 */
public class ConnectionSingle {

    private static Connection con;

    /**
     * To get a single instance of the connection object
     *
     * @return Connection object
     */
    public static Connection getInstance() {
        try {
            if (con == null) {
                con = CreateConnection();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return con;
    }

    /**
     * Create a Connection instance
     *
     * @return @throws SQLException
     */
    private static Connection CreateConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:C:\\SQlite\\db\\LFRS.db");
        return c;
    }
}
