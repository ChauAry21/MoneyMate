package MoneyMate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    public void addExpense(Expense expense) {
        String query = "INSERT INTO expenses (date, category, amount) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(expense.getDate()));
            stmt.setString(2, expense.getCategory());
            stmt.setDouble(3, expense.getAmount());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
