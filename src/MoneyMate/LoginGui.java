package MoneyMate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGui {
    public JFrame frame;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JButton createUserButton;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginGui() {
        createFrame();
        createFields();
        createLayout();
        createListeners();
    }

    private void createFrame() {
        frame = new JFrame("User Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
    }

    private void createFields() {
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        createUserButton = new JButton("Create User");
        loginButton = new JButton("Login");
        messageLabel = new JLabel("");
    }

    private void createLayout() {
        Container container = frame.getContentPane();
        container.setLayout(new GridLayout(6, 1));

        container.add(usernameLabel);
        container.add(usernameField);
        container.add(passwordLabel);
        container.add(passwordField);
        container.add(createUserButton);
        container.add(loginButton);
        container.add(messageLabel);
        UserHandler.createUsersTableIfNotExists();
    }

    private void createListeners() {
        createUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordArray = passwordField.getPassword();
                String password = new String(passwordArray);

                if (UserHandler.createUser(username, password)) {
                    JOptionPane.showMessageDialog(frame, "User created successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: User could not be created.");
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordArray = passwordField.getPassword();
                String password = new String(passwordArray);

                if (UserHandler.checkUser(username, password)) {
                    JOptionPane.showMessageDialog(frame, "User exists and credentials are correct.");
                    frame.dispose();  // Close the login GUI
                    ExpenseManager expenseManager = new ExpenseManager();
                    ExpenseTrackerGui expenseTrackerGUI = new ExpenseTrackerGui(expenseManager);
                    expenseTrackerGUI.show();
                } else {
                    JOptionPane.showMessageDialog(frame, "User does not exist or credentials are incorrect.");
                }
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        LoginGui loginGui = new LoginGui();
        loginGui.show();
    }
}