package MoneyMate;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Handles user login functionality for the MoneyMate application.
 */
public class UserLogin {
    private Connection conn;

    /**
     * Constructor to initialize the UserLogin with a database connection.
     *
     * @param conn the database connection
     */
    public UserLogin(Connection conn) {
        this.conn = conn;
    }

    /**
     * Checks if the user with the given username and password exists in the database.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the user ID if the user exists and the password is correct, -1 otherwise
     */
    public int checkUser(String username, String password) {
        String sql = "SELECT id, password FROM users WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (BCrypt.checkpw(password, storedHash)) {
                        return rs.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
