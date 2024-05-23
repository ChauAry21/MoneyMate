package MoneyMate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for establishing a connection to the database.
 */
public class SQLreg {
    private static final String URL = "jdbc:mysql://localhost:3306/Your Database";
    private static final String USER = "root";
    private static final String PASSWORD = "Your Password";

    /**
     * Establishes and returns a connection to the MoneyMate database.
     *
     * @return a Connection object to the database, or null if a connection could not be established
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
