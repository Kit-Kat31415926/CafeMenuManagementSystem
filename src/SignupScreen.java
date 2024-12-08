import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

/*
 * Creates signup screen for user to create an account
 * @author - Kaitlyn Chiu
 */
public class SignupScreen extends JDialog {
	private CafeOnlineOrderSystemGUI mainGUI;
    private cafe mycafe;
    private UserManager userManager;

    private JTextField firstNameInput;
    private JTextField lastNameInput;
    private JTextField emailInput;
    private JPasswordField passwordInput;
    private JComboBox<String> roleInput;

    /*
     * Shows signup screen to user
     */
   	public SignupScreen(CafeOnlineOrderSystemGUI mainGUI, cafe mycafe) {
        super(mainGUI, "Signup", true);
        this.mainGUI = mainGUI;
        this.mycafe = mycafe;
        this.userManager = CafeOnlineOrderSystemGUI.USER_MANAGER;

        // Set up dialog box
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(mainGUI);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill =      GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, -100, 10, -100);

        // Add components
        add(new JLabel("First Name"), gbc);
        firstNameInput = new JTextField();
        add(firstNameInput, gbc);

        add(new JLabel("Last Name"), gbc);
        lastNameInput = new JTextField();
        add(lastNameInput, gbc);

        add(new JLabel("Email"), gbc);
        emailInput = new JTextField();
        add(emailInput, gbc);

        add(new JLabel("Password"), gbc);
        passwordInput = new JPasswordField();
        add(passwordInput, gbc);

        add(new JLabel("Role"), gbc);
        roleInput = new JComboBox<String>();
        roleInput.addItem("Customer");
        roleInput.addItem("Admin");
        add(roleInput, gbc);

        JPanel buttons = new JPanel();
        buttons.add(new JButton("Signup") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent action) {
                    if (firstNameInput.getText().trim().isEmpty() ||
                            lastNameInput.getText().trim().isEmpty() ||
                            emailInput.getText().trim().isEmpty() ||
                            passwordInput.getPassword().length == 0) {
                        JOptionPane.showMessageDialog(SignupScreen.this, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        String password = new String(passwordInput.getPassword());
                        // Checks if password is valid using password exceptions
                        if (!password.matches(".*[a-z].*")) {
                            throw new LowerCaseCharacterMissing("Password needs a lowercase character.");
                        } else if (password.length() < 8) {
                            throw new Minimum8CharactersRequired("Password needs at least 8 characters.");
                        } else if (!password.matches(".*\\d.*")){
                            throw new NumberCharacterMissing("Password needs a number.");
                        } else if (!password.matches(".*[~!@#$%^&*()?<>|].*")) {
                            throw new SpecialCharacterMissing("Password needs a special character.");
                        } else if (!password.matches(".*[A-Z].*")) {
                            throw new UpperCaseCharacterMissing("Password needs an uppercase character.");
                        }
                    } catch (PasswordException pe) {
                        JOptionPane.showMessageDialog(SignupScreen.this, pe.getMessage(), "Password Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(SignupScreen.this, "Something went wrong :(", "Unknown Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Generate username with random numbers
                    String username = firstNameInput.getText().trim();
                    for (int i = 0; i < 4; i++) {
                        username += (int) (Math.random() * 10);
                    }
                    // Create new User based on role
                    if (roleInput.getSelectedItem().equals("Admin")) {
                        userManager.addUser(new Admin(firstNameInput.getText().trim(), lastNameInput.getText().trim(), emailInput.getText().trim(), username, new String(passwordInput.getPassword()), true));
                    } else {
                        userManager.addUser(new Customer(firstNameInput.getText().trim(), lastNameInput.getText().trim(), emailInput.getText().trim(), username, new String(passwordInput.getPassword()), true));
                    }
                    JOptionPane.showMessageDialog(SignupScreen.this, "Signup successful! Your username is " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
                    SignupScreen.this.dispose();
                }
            });
        }});

        buttons.add(new JButton("Cancel") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SignupScreen.this.dispose();
                }
            });
        }});

        add(buttons, gbc);

        setVisible(true);
    }
}
