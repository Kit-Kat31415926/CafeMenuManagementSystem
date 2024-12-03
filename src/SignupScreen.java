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
                    boolean signupOK = true;
                    try {
                        // TODO: Check if password is valid using password exceptions
                        if (false /* regex here */) {
                            throw new LowerCaseCharacterMissing("Need a lowercase character");
                        }
                    } catch (PasswordException pe) {
                        signupOK = false;
                        JOptionPane.showMessageDialog(SignupScreen.this, pe.getMessage(), "Password Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        signupOK = false;
                        JOptionPane.showMessageDialog(SignupScreen.this, "Something went wrong :(", "Unknown Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (signupOK) {
                        // TODO: Generate username based on requirements
                        String username = "";
                        // TODO: Create new User based on role
                        JOptionPane.showMessageDialog(SignupScreen.this, "Signup successful! Your username is " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
                        SignupScreen.this.dispose();
                    }
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
