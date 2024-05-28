package MoneyMate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 * A graphical user interface for users to track and manage expenses.
 */
public class ExpenseTrackerGui {
    /**
     * all necessary fields for ExpenseTrackerGui.java
     */
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
    private String user;

    /**
     * Constructs a new ExpenseTrackerGui instance.
     *
     * @param expenseManager the expense manager to use for adding and deleting expenses
     * @param user the current user
     */
    public ExpenseTrackerGui(ExpenseManager expenseManager, String user) {
        this.user = user;
        this.expenseManager = expenseManager;
        createFrame();
        createFields();
        createLayout();
        createListeners();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        show();
    }

    /**
     * Creates the main frame of the GUI.
     */
    private void createFrame() {
        frame = new JFrame("MoneyMate");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null);

        Image iconImage = null;
        try {
            iconImage = ImageIO.read(getClass().getResourceAsStream("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (iconImage != null) {
            frame.setIconImage(iconImage);
        }
    }

    /**
     * Creates the GUI fields (labels, text fields, buttons, etc.).
     */
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

    /**
     * Creates the layout of the GUI components.
     */
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

        tableModel = new DefaultTableModel(new String[]{"Category", "Date", "Amount"}, 30);
        outputTable = new JTable(tableModel);
        container.add(new JScrollPane(outputTable), BorderLayout.SOUTH);
    }

    /**
     * Creates the event listeners for the GUI components.
     */
    private void createListeners() {
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());

                String username = user;

                Expense expense = new Expense(username, date, category, amount);

                expenseManager.addExpense(expense, username);

                DefaultTableModel tableModel = (DefaultTableModel) outputTable.getModel();
                tableModel.addRow(new Object[]{date, category, amount});

                refreshTable();
            }
        });

        generateReportButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = DriverManager.getConnection(UserHandler.getDbUrl(), UserHandler.getDbUser(), UserHandler.getDbPassword());
                    String sql = "SELECT * FROM expenses WHERE username = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, UserHandler.getCurrentUser());

                    ResultSet rs = pstmt.executeQuery();

                    File file = new File("expenses_report.txt");
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);

                    while (rs.next()) {
                        String date = rs.getString("date");
                        String category = rs.getString("category");
                        double amount = rs.getDouble("amount");
                        bw.write(date + "\t" + category + "\t" + amount);
                        bw.newLine();
                    }

                    bw.close();
                    fw.close();
                    rs.close();
                    pstmt.close();
                    conn.close();

                    // Open the generated file
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(file);

                    JOptionPane.showMessageDialog(null, "Report generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error generating report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                String valueToDelete = JOptionPane.showInputDialog("Enter the value to delete:");
                if (valueToDelete != null) {
                    try {
                        double amountToDelete = Double.parseDouble(valueToDelete);
                        expenseManager.deleteExpensesByAmount(amountToDelete);
                        refreshTable();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid amount.");
                    }
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginGui loginGui = new LoginGui();
                loginGui.show();
            }
        });
    }

    /**
     * Refreshes the table with the latest expenses.
     */
    public void refreshTable() {
        try {
            Connection conn = DriverManager.getConnection(UserHandler.getDbUrl(), UserHandler.getDbUser(), UserHandler.getDbPassword());
            String sql = "SELECT * FROM expenses WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, UserHandler.getCurrentUser());
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

    /**
     * Shows the GUI.
     */
    public void show() {
        frame.setVisible(true);
    }
}