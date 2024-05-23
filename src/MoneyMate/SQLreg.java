package MoneyMate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLreg {
    private static final String URL = "jdbc:mysql://localhost:3306/moneymate";
    private static final String USER = "root";
    private static final String PASSWORD = "ValentinCampa1919";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
