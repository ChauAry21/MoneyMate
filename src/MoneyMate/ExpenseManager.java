package MoneyMate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {

    private String user;

    /**
     * Adds a new expense to the database.
     *
     * @param expense the expense to add.
     * @param username the username of the user adding the expense.
     */
    public void addExpense(Expense expense, String username) {
        user = username;

        String query = "INSERT INTO expenses (username, date, category, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(UserHandler.getDbUrl(), UserHandler.getDbUser(), UserHandler.getDbPassword());
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setDate(2, Date.valueOf(expense.getDate()));
            stmt.setString(3, expense.getCategory());
            stmt.setDouble(4, expense.getAmount());

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

        try (Connection conn = DriverManager.getConnection(UserHandler.getDbUrl(), UserHandler.getDbUser(), UserHandler.getDbPassword());
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpensesByAmount(double amount) {
        String query = "DELETE FROM expenses WHERE amount = ?";

        try (Connection conn = DriverManager.getConnection(UserHandler.getDbUrl(), UserHandler.getDbUser(), UserHandler.getDbPassword());
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, amount);
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
        String query = "SELECT * FROM expenses WHERE username = " + UserHandler.getCurrentUser();

        try (Connection conn = DriverManager.getConnection(UserHandler.getDbUrl(), UserHandler.getDbUser(), UserHandler.getDbPassword());
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, UserHandler.getCurrentUser());

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String username = rs.getString("username");
                    String date = rs.getDate("date").toString();
                    String category = rs.getString("category");
                    double amount = rs.getDouble("amount");
                    expenses.add(new Expense(username, date, category, amount));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }
}
