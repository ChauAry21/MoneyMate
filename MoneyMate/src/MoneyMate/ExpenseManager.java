package MoneyMate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/moneymate";
    private static final String USER = "moneymate";
    private static final String PASSWORD = "moneymate";

    /**
     * Adds a new expense to the database.
     *
     * @param expense the expense to add.
     */
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

    /**
     * Deletes an expense from the database by ID.
     *
     * @param id the ID of the expense to delete.
     */
    public void deleteExpense(int id) {
        String query = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Views all the expenses from the database.
     *
     * @return a list of all expenses.
     */
    public List<Expense> viewExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT id, date, category, amount FROM expenses";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String date = rs.getDate("date").toString();
                String category = rs.getString("category");
                double amount = rs.getDouble("amount");
                expenses.add(new Expense(id, date, category, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }
}
