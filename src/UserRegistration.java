import java.sql.*;

public class UserRegistration {
    private Connection conn;

    public UserRegistration(Connection conn) {
        this.conn = conn;
    }

    public void addUser(String username, String password) {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
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