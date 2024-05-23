package MoneyMate;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Handles user registration functionality for the MoneyMate application.
 */
public class UserRegistration {
    private Connection conn;

    /**
     * Constructor to initialize the UserRegistration with a database connection.
     *
     * @param conn the database connection
     */
    public UserRegistration(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds a new user to the database with the given username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     */
    public void addUser(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
