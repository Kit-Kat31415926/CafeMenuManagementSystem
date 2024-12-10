import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/*
 * Creates login screen of user
 * @author - Kaitlyn Chiu
 */
public class LoginScreen extends JDialog {
	private Map<String, User> users;

	private JTextField usernameInput;
	private JPasswordField passwordInput;

	/*
	 * Creates login screen
	 */
	public LoginScreen(JFrame parent, Map<String, User> users) {
		super(parent, "Login", true);
		this.users = users;
		UserManager userManager = CafeOnlineOrderSystemGUI.USER_MANAGER;
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
                    // Check if fields are empty
                    if (usernameInput.getText().trim().isEmpty() || passwordInput.getPassword().length == 0) {
                        JOptionPane.showMessageDialog(LoginScreen.this, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Check if username exists
                    if (!userManager.isUser(usernameInput.getText())) {
                        JOptionPane.showMessageDialog(LoginScreen.this, "The username " + usernameInput.getText() + " doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Check if login is correct
                    if (userManager.correctLogin(usernameInput.getText(), new String(passwordInput.getPassword()))) {
                        // Get user and show proper dashboard
                        User user = userManager.getUser(usernameInput.getText());
						JOptionPane.showMessageDialog(LoginScreen.this, "Login successful! Welcome, " + user.getUserName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        LoginScreen.this.dispose();
                        if (user.isAdmin()) {
							AdminDashboard adminDashboard = new AdminDashboard(parent, user);
						} else {
							CustomerDashboard customerDashboard = new CustomerDashboard(parent, user);
						}
                    } else {
                        JOptionPane.showMessageDialog(LoginScreen.this, "Incorrect password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
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
