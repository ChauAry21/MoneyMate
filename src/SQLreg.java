import java.sql.*;

public class SQLreg {
    private Connection conn;

    public SQLreg() {
        // Connect to the database
        String url = "jdbc:mysql://127.0.0.1:3306/moneymate";
        String user = "Aryan";
        String password = "Dl?l_r#0ife8To&4igLF3Uha4aruStlD";

        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }
}