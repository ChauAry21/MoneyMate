package MoneyMate;

import javax.swing.*;
import java.awt.*;

import javax.swing.table.DefaultTableModel;

public class ExpenseTrackerGui {
    private JFrame frame;
    private JTextField dateField, categoryField, amountField;
    private JButton addExpenseButton, generateReportButton, viewExpensesButton, deleteExpenseButton, logoutButton;
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    private ExpenseManager expenseManager;

    public ExpenseTrackerGui(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
        createFrame();
        createFields();
        createLayout();
        createListeners();
    }

    private void createFrame() {
        frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
    }

    private void createFields() {
        dateField = new JTextField(15);
        categoryField = new JTextField(15);
        amountField = new JTextField(15);
        addExpenseButton = new JButton("Add Expense");
        generateReportButton = new JButton("Generate Report");
        viewExpensesButton = new JButton("View Expenses");
        deleteExpenseButton = new JButton("Delete Expense");
        logoutButton = new JButton("Logout");

        tableModel = new DefaultTableModel(new String[]{"ID", "Date", "Category", "Amount"}, 0);
        expensesTable = new JTable(tableModel);
    }

    private void createLayout() {
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(generateReportButton);
        buttonPanel.add(viewExpensesButton);
        buttonPanel.add(deleteExpenseButton);
        buttonPanel.add(logoutButton);

        container.add(inputPanel, BorderLayout.NORTH);
        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(new JScrollPane(expensesTable), BorderLayout.SOUTH);
    }

    private void createListeners() {
        addExpenseButton.addActionListener(e -> addExpense());
        viewExpensesButton.addActionListener(e -> refreshTable());
        deleteExpenseButton.addActionListener(e -> deleteExpense());
        logoutButton.addActionListener(e -> frame.dispose());
    }

    private void addExpense() {
        try {
            String date = dateField.getText();
            String category = categoryField.getText();
            double amount = Double.parseDouble(amountField.getText());

            Expense expense = new Expense(0, date, category, amount); // 0 as placeholder, assuming DB handles ID
            expenseManager.addExpense(expense);
            JOptionPane.showMessageDialog(frame, "Expense added successfully.");
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error adding expense: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter ID of expense to delete:"));
            expenseManager.deleteExpense(id);
            JOptionPane.showMessageDialog(frame, "Expense deleted successfully.");
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error deleting expense: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        try {
            java.util.List<Expense> expenses = expenseManager.viewExpenses();
            tableModel.setRowCount(0);
            for (Expense expense : expenses) {
                tableModel.addRow(new Object[]{expense.getId(), expense.getDate(), expense.getCategory(), expense.getAmount()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error retrieving expenses: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}
