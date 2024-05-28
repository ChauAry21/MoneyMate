package MoneyMate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        frame = new JFrame("MoneyMate - Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Adjusted size to 800x600
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
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(generateReportButton);
        buttonPanel.add(viewExpensesButton);
        buttonPanel.add(deleteExpenseButton);
        buttonPanel.add(logoutButton);

        container.add(inputPanel, BorderLayout.NORTH);
        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(new JScrollPane(expensesTable), BorderLayout.SOUTH);

        frame.pack(); // Ensure components are sized correctly
    }

    private void createListeners() {
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());

                try {
                    int id = generateUniqueId(); // Implement this method as needed
                    Expense expense = new Expense(id, date, category, amount);
                    expenseManager.addExpense(expense);
                    JOptionPane.showMessageDialog(frame, "Expense added successfully.");
                    refreshTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error adding expense: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewExpensesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        deleteExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter ID of expense to delete:"));
                    expenseManager.deleteExpense(id);
                    JOptionPane.showMessageDialog(frame, "Expense deleted successfully.");
                    refreshTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error deleting expense: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginGui().show();
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Report generated (functionality to be implemented).");
            }
        });
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

    private int generateUniqueId() {
        return (int) (Math.random() * 10000); // Simple random ID generator
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ExpenseManager expenseManager = new ExpenseManager();
        ExpenseTrackerGui expenseTrackerGUI = new ExpenseTrackerGui(expenseManager);
        expenseTrackerGUI.show();
    }
}
