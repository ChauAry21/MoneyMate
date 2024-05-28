package MoneyMate;

import javax.swing.*;
import java.awt.*;

public class LoginGui {
    public JFrame frame;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JButton createUserButton;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JLabel messageLabel;
    private JLabel logoLabel;

    public LoginGui() {
        createFrame();
        createFields();
        createLayout();
        createListeners();
    }

    private void createFrame() {
        frame = new JFrame("MoneyMate - User Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        addLogo();
    }

    private void addLogo() {
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
            logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            frame.getContentPane().add(logoLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
    }

    private void createFields() {
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        createUserButton = new JButton("Create User");
        loginButton = new JButton("Login");
        forgotPasswordButton = new JButton("Forgot Password?");
        messageLabel = new JLabel("");
    }

    private void createLayout() {
        Container container = frame.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Logo placement
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(logoLabel, gbc);

        // Username label and field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.LINE_END;
        container.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        container.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        container.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        container.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(createUserButton, gbc);

        gbc.gridx = 1;
        container.add(loginButton, gbc);

        // Forgot Password Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(forgotPasswordButton, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(messageLabel, gbc);
    }

    private void createListeners() {
        createUserButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordArray = passwordField.getPassword();
            String password = new String(passwordArray);
            if (UserHandler.createUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "User created successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Error: User could not be created.");
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordArray = passwordField.getPassword();
            String password = new String(passwordArray);
            if (UserHandler.checkUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "User exists and credentials are correct.");
                frame.dispose(); // Close the login GUI
                ExpenseManager expenseManager = new ExpenseManager();
                ExpenseTrackerGui expenseTrackerGUI = new ExpenseTrackerGui(expenseManager);
                expenseTrackerGUI.show();
            } else {
                JOptionPane.showMessageDialog(frame, "User does not exist or credentials are incorrect.");
            }
        });

        forgotPasswordButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter your username for password reset:");
            if (username != null && !username.isEmpty()) {
                if (UserHandler.initiatePasswordReset(username)) {
                    JOptionPane.showMessageDialog(frame, "Password reset instructions have been sent to your email.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: Unable to send password reset instructions.");
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
