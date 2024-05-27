package MoneyMate;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class UserHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/moneymate";
    private static final String DB_USER = "moneymate";
    private static final String DB_PASSWORD = "moneymate";

    // Method to ensure the 'users' table exists
    public static void createUsersTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT(11) NOT NULL AUTO_INCREMENT, " +
                "username VARCHAR(50) NOT NULL, " +
                "password VARCHAR(64) NOT NULL, " +
                "PRIMARY KEY (id))";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    // Method to hash a password
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        return HexFormat.of().formatHex(hashedPassword);
    }

    // Method to create a new user
    public static boolean createUser(String username, String password) {
        createUsersTableIfNotExists();
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, hashPassword(password));
            int rowsAffected = statement.executeUpdate();
            System.out.println("User created: " + username);
            return rowsAffected > 0;
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    // Method to check if a user exists
    public static boolean checkUser(String username, String password) {
        createUsersTableIfNotExists();  // Ensure table exists
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, hashPassword(password));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                System.out.println("User check: " + username + " - Count: " + count);  // Debug statement
                return count > 0;
            } else {
                return false;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println("Error checking user: " + e.getMessage());
            return false;
        }
    }
}
