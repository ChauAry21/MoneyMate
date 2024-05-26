package MoneyMate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ExpenseTrackerGui {
    private JFrame frame;
    private JLabel dateLabel;
    private JLabel categoryLabel;
    private JLabel amountLabel;
    private JButton addExpenseButton;
    private JButton generateReportButton;
    private JButton viewExpensesButton;
    private JButton deleteExpenseButton;
    private JButton logoutButton;
    private JTextField dateField;
    private JTextField categoryField;
    private JTextField amountField;
    private ExpenseManager expenseManager;
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    private JTable outputTable;
    private DefaultTableModel outputTableModel;

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
        dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateField = new JTextField(15);
        categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(15);
        amountLabel = new JLabel("Amount:");
        amountField = new JTextField(15);
        addExpenseButton = new JButton("Add Expense");
        generateReportButton = new JButton("Generate Report");
        viewExpensesButton = new JButton("View Expenses");
        deleteExpenseButton = new JButton("Delete Expense");
        logoutButton = new JButton("Logout");
    }

    private void createLayout() {
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryField);
        inputPanel.add(amountLabel);
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

        tableModel = new DefaultTableModel(new String[]{"Date", "Category", "Amount"}, 0);
        expensesTable = new JTable(tableModel);
        container.add(new JScrollPane(expensesTable), BorderLayout.EAST);

        outputTableModel = new DefaultTableModel(new String[]{"Category", "Date", "Amount"}, 0);
        outputTable = new JTable(outputTableModel);
        container.add(new JScrollPane(outputTable), BorderLayout.SOUTH);
    }

    private void createListeners() {
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_tracker", "root", "password");
                    String sql = "INSERT INTO expenses(date, category, amount) VALUES(?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, date);
                    pstmt.setString(2, category);
                    pstmt.setDouble(3, amount);
                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();

                    JOptionPane.showMessageDialog(frame, "Expense added successfully.");
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }

                refreshTable();
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputTableModel.setRowCount(0);

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_tracker", "root", "password");
                    String sql = "SELECT * FROM expenses";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        String category = rs.getString("category");
                        String date = rs.getString("date");
                        double amount = rs.getDouble("amount");
                        outputTableModel.addRow(new Object[]{category, date, amount});
                    }

                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
            }
        });

        viewExpensesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputTableModel.setRowCount(0);

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_tracker", "root", "password");
                    String sql = "SELECT * FROM expenses";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        String category = rs.getString("category");
                        String date = rs.getString("date");
                        double amount = rs.getDouble("amount");
                        tableModel.addRow(new Object[]{date, category, amount});
                    }

                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
            }
        });

        deleteExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_tracker", "root", "password");
                    String sql = "DELETE FROM expenses WHERE category = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, categoryField.getText());
                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();

                    JOptionPane.showMessageDialog(frame, "Expense deleted successfully.");
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }

                refreshTable();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginGui();
            }
        });
    }

    public void refreshTable() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_tracker", "root", "password");
            String sql = "SELECT * FROM expenses";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                String date = rs.getString("date");
                String category = rs.getString("category");
                double amount = rs.getDouble("amount");
                tableModel.addRow(new Object[]{date, category, amount});
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}