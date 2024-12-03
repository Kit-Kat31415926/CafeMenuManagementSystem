import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LoginScreen extends JDialog {
	private Map<String, User> users;

    private JTextField usernameInput;
    private JPasswordField passwordInput;

    public LoginScreen(JFrame parent, Map<String, User> users) {
        super(parent, "Login", true);
        this.users = users;
        UserManager userManager = new UserManager();
        setLayout(new GridBagLayout());

        // Set up dialog box
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill =      GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, -100, 10, -100);

        // Add components
        add(new JLabel("Username"), gbc);
        usernameInput = new JTextField();
        add(usernameInput, gbc);

        add(new JLabel("Password"), gbc);
        passwordInput = new JPasswordField();
        add(passwordInput, gbc);

        JPanel buttons = new JPanel();
        buttons.add(new JButton("Login") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent action) {
                    boolean loginOK = true;
                    // TODO: Check if username and password is valid using User Manager
                    if (!loginOK) {
                        // TODO: Specify error for login
                        JOptionPane.showMessageDialog(LoginScreen.this, "<Insert Error Here>", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // TODO: Get User if sign up is successful
                        User user = new Admin("", "", "", "", "", true);
                        JOptionPane.showMessageDialog(LoginScreen.this, "Login successful! Welcome, " + user.getUserName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        LoginScreen.this.dispose();
                        if (user.isAdmin()) {
                            AdminDashboard adminDashboard = new AdminDashboard(parent, user);
                        } else {
                            CustomerDashboard customerDashboard = new CustomerDashboard(parent, user);
                        }
                    }
                }
            });
        }});

        buttons.add(new JButton("Cancel") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoginScreen.this.dispose();
                }
            });
        }});

        add(buttons, gbc);

        setVisible(true);
    }
}
