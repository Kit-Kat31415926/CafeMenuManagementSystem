import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;

/*
 * Create new Cafe Management System
 * @author - Kaitlyn Chiu
 */
public class CafeOnlineOrderSystemGUI extends JFrame {
    private JButton loginButton;
    private JButton signupButton;
    private JButton exitButton;

    private cafe cafedata = cafe.DB;

    // Eager instantiation of managers
    public static final UserManager USER_MANAGER = new UserManager();
    public static final MenuManager MENU_MANAGER = new MenuManager();

    /*
     * Create new instance of Cafe Management System
     * Display home page to user, where they can signup for an account, login, or exit the application
     */
	public CafeOnlineOrderSystemGUI() {
        /*
         * Creates action listener for button clicks
         * User may sign up, log in, or exit
         */
        class ClickActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Redirect to login screen
                if (e.getSource() == loginButton) {
                    LoginScreen loginScreen = new LoginScreen(CafeOnlineOrderSystemGUI.this, cafedata.getUsers());
                // Redirect to signup screen
                } else if (e.getSource() == signupButton) {
                    SignupScreen signupScreen = new SignupScreen(CafeOnlineOrderSystemGUI.this, cafedata);
                // Exit program
                } else if (e.getSource() == exitButton) {
                    System.exit(0);
                } else {
                    System.out.println("Error :(");
                }
            }
        }

        setTitle("The Best Café Co.");
        // Set background image
        JLabel background = new JLabel(new ImageIcon("src/resources/background.jpg"));
        background.setLayout(new GridBagLayout());
        // Set up frame
        setSize(960, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   		// Create screen components
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill =      GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 50, 50);

        // Create title
        JLabel header = new JLabel();
        header.setText("<html><center>Welcome to The<br />Best Café Co.!</center></html>");
        header.setFont(new Font(Font.SERIF, Font.BOLD, 36));
        content.add(header, gbc);

        // Create buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        GridBagConstraints buttongbc = new GridBagConstraints();
        buttongbc.gridwidth = GridBagConstraints.REMAINDER;
        buttongbc.fill =      GridBagConstraints.HORIZONTAL;
        buttongbc.insets = new Insets(0, -120, 10, -120);
        buttons.setOpaque(false);

        // TODO: Style login, exit, and signup buttons
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ClickActionListener());
        loginButton.setBackground(Color.PINK);
        buttons.add(loginButton, buttongbc);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ClickActionListener());
        exitButton.setBackground(Color.GRAY);
        buttons.add(exitButton, buttongbc);

        content.add(buttons, gbc);

        // Create signup
        JLabel signupMessage = new JLabel("Don't have an account?");
        gbc.insets = new Insets(10, 50, 0, 50);
        content.add(signupMessage, gbc);

        signupButton = new JButton("Signup");
        signupButton.addActionListener(new ClickActionListener());
        signupButton.setBackground(Color.PINK);
        gbc.insets = new Insets(10, 50, 50, 50);
        content.add(signupButton, gbc);

        add(content);

        background.add(content, new GridBagConstraints() );
        add(background);

        setVisible(true);
	}

    /*
     * Writes information to database when updated
     */
    public static void writeToDatabase() {
        try (FileWriter writer = new FileWriter("cafeData.txt")){
            String res = "";
            res += ("Menu:\n");
            for (MenuItem m : MENU_MANAGER.getMenu()) {
                res += (m.getMenuType() + ";" + m.getTitle() + ";" + m.getItemID() + ";" + m.getDescription() + ";" + m.getPrice() + ";" + m.getCount() + ";" + m.isCurrent());
                res += ("\n");
            }
            res += ("Users:\n");
            for (User u : USER_MANAGER.getAllUsers()) {
                res += (u.getRole() + ";" + u.getFirstName() + ";" + u.getLastName() + ";" + u.getEmail() + ";" + u.getUserName() + ";" + u.getPassword() + ";" + u.isActive());
                if (u.getOrderedItems().isEmpty()) {
                    res += (";");
                } else {
                    for (String itemID : u.getOrderedItems()) {
                        res += (";" + itemID);
                    }
                }
                res += ("\n");
            }
            writer.write(res);
        } catch (Exception ex) {
            System.out.println("Failed to write users to database: " + ex);
        }
    }

    /*
     * Cafe Management System start
     */
    public static void main(String[] args) {
        CafeOnlineOrderSystemGUI cafe = new CafeOnlineOrderSystemGUI();
    }
}
