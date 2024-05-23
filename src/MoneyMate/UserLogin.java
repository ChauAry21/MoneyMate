package MoneyMate;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserLogin {
    private Connection conn;

    public UserLogin(Connection conn) {
        this.conn = conn;
    }

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
