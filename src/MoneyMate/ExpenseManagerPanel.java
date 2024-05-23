package MoneyMate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseManagerPanel extends JPanel {
    private Connection conn;
    private MainApp mainApp;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private int userId;

    public ExpenseManagerPanel(MainApp mainApp, Connection conn) {
        this.mainApp = mainApp;
        this.conn = conn;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton logoutButton = new JButton("Logout");
        JButton viewButton = new JButton("View Expenses");
        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Expense");

        topPanel.add(logoutButton);
        topPanel.add(viewButton);
        topPanel.add(addButton);
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Date", "Category", "Amount", "Description"}, 0);
        expenseTable = new JTable(tableModel);
        add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);  // Clear the table
                mainApp.showPanel("Login");
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadExpenses();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExpense();
            }
        });
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void loadExpenses() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM expenses WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String date = rs.getString("date");
                    String category = rs.getString("category");
                    double amount = rs.getDouble("amount");
                    String description = rs.getString("description");
                    tableModel.addRow(new Object[]{id, date, category, amount, description});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addExpense() {
        JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField descriptionField = new JTextField();
        Object[] message = {
                "Date (YYYY-MM-DD):", dateField,
                "Category:", categoryField,
                "Amount:", amountField,
                "Description:", descriptionField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionField.getText();
                String sql = "INSERT INTO expenses (user_id, date, category, amount, description) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, userId);
                    stmt.setString(2, formattedDate);
                    stmt.setString(3, category);
                    stmt.setDouble(4, amount);
                    stmt.setString(5, description);
                    stmt.executeUpdate();
                    loadExpenses();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please enter the date as YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) expenseTable.getValueAt(selectedRow, 0);
            String sql = "DELETE FROM expenses WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                loadExpenses();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No expense selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
