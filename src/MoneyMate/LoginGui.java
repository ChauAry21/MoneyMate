package MoneyMate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * A graphical user interface for user login and creation.
 */
public class LoginGui {
    /**
     * all necessary fields for LoginGui.java
     */
    public JFrame frame;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JButton createUserButton;
    private JButton loginButton;
    private JLabel messageLabel;
    private JLabel logoLabel;

    /**
     * Compile the GUI components.
     */
    public LoginGui() {
        createFrame();
        createFields();
        createLayout();
        createListeners();
    }

    /**
     * Creates the main frame of the GUI.
     */
    private void createFrame() {
        frame = new JFrame("MoneyMate - User Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500); // Adjusted size to accommodate logo
        frame.setLocationRelativeTo(null);
        addLogo(); // Method call to add logo

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
     * Adds the application logo to the GUI.
     */
    private void addLogo() {
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("logo.png"));
            logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            frame.getContentPane().add(logoLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
    }

    /**
     * Creates the GUI fields (labels, text fields, buttons, etc.).
     */
    private void createFields() {
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        createUserButton = new JButton("Create User");
        loginButton = new JButton("Login");
        messageLabel = new JLabel("");
    }

    /**
     * Creates the layout of the GUI components.
     */
    private void createLayout() {
        Container container = frame.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Logo placement
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding around the logo
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(logoLabel, gbc);

        // Username label and field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10); // Padding for input fields
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

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(messageLabel, gbc);
    }

    /**
     * Creates the event listeners for the GUI components.
     */
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
                    frame.dispose(); // Close the login GUI
                    ExpenseManager expenseManager = new ExpenseManager();
                    ExpenseTrackerGui expenseTrackerGUI = new ExpenseTrackerGui(expenseManager, usernameField.getText());
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

    /**
     * The main class for the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        LoginGui loginGui = new LoginGui();
        loginGui.show();
    }
}
