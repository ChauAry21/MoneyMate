package MoneyMate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class ExpenseTrackerGui {
    private JFrame frame;
    private JFormattedTextField dateField, amountField;
    private JTextField categoryField;
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
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
    }

    private void createFields() {
        // Date field with placeholder and initial formatting
        try {
            MaskFormatter dateFormatter = new MaskFormatter("####-##-##");
            dateFormatter.setPlaceholderCharacter('_');
            dateField = new JFormattedTextField(dateFormatter);
            dateField.setColumns(10);
            dateField.setText("0000-00-00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Setup for amount field with custom document
        amountField = new JFormattedTextField();
        amountField.setColumns(10);
        PlainDocument doc = new PlainDocument();
        doc.setDocumentFilter(new CurrencyDocumentFilter());
        amountField.setDocument(doc);
        amountField.setHorizontalAlignment(JTextField.RIGHT);
        amountField.setText("$0.00");

        categoryField = new JTextField(15);

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
        frame.pack();  // Adjust to contents
    }

    private void createListeners() {
        addExpenseButton.addActionListener(e -> {
            String date = dateField.getText();
            String category = categoryField.getText();
            double amount = ((Number) amountField.getValue()).doubleValue();

            try {
                int id = generateUniqueId();
                Expense expense = new Expense(id, date, category, amount);
                expenseManager.addExpense(expense);
                JOptionPane.showMessageDialog(frame, "Expense added successfully.");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error adding expense: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewExpensesButton.addActionListener(e -> refreshTable());
        deleteExpenseButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter ID of expense to delete:"));
                expenseManager.deleteExpense(id);
                JOptionPane.showMessageDialog(frame, "Expense deleted successfully.");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error deleting expense: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginGui().show();
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
        return (int) (Math.random() * 10000);  // Simple random ID generator
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

class CurrencyDocumentFilter extends DocumentFilter {
    private NumberFormat formatter = new DecimalFormat("$#,##0.00;-$#,##0.00");

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("[0-9.]")) {
            super.replace(fb, offset, length, text, attrs);
            formatField(fb);
        }
    }

    private void formatField(FilterBypass fb) throws BadLocationException {
        String text = fb.getDocument().getText(0, fb.getDocument().getLength());
        text = text.replaceAll("[^0-9]", "");
        if (!text.isEmpty()) {
            try {
                Number number = Double.parseDouble(text) / 100;
                String formattedText = formatter.format(number);
                super.replace(fb, 0, fb.getDocument().getLength(), formattedText, null);
            } catch (NumberFormatException e) {
                // Handle potential number format exception if needed
            }
        }
    }
}
