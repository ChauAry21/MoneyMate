import java.sql.*;

public class UserLogin {
    private Connection conn;

    public UserLogin(Connection conn) {
        this.conn = conn;
    }

    public boolean checkUser(String username, String password) {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}