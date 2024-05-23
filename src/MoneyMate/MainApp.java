package MoneyMate;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainApp extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private int userId;

    public MainApp() {
        super("MoneyMate");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        SQLreg sqlReg = new SQLreg();
        Connection conn = sqlReg.getConnection();

        LoginPanel loginPanel = new LoginPanel(this, conn);
        loginPanel.setName("Login");
        RegistrationPanel registrationPanel = new RegistrationPanel(this, conn);
        registrationPanel.setName("Register");
        ExpenseManagerPanel expenseManagerPanel = new ExpenseManagerPanel(this, conn);
        expenseManagerPanel.setName("ExpenseManager");

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registrationPanel, "Register");
        mainPanel.add(expenseManagerPanel, "ExpenseManager");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
        if ("ExpenseManager".equals(name)) {
            ExpenseManagerPanel expenseManagerPanel = (ExpenseManagerPanel) getComponentFromName("ExpenseManager");
            expenseManagerPanel.setUserId(userId);
        }
    }

    private Component getComponentFromName(String name) {
        for (Component comp : mainPanel.getComponents()) {
            if (name.equals(comp.getName())) {
                return comp;
            }
        }
        return null;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}

