import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Create new Cafe Management System
 * @author - Kaitlyn Chiu
 */
public class CafeOnlineOrderSystemGUI extends JFrame{

    /*
     * Create new instance of Cafe Management System
     * Display home page to user, where they can signup for an account, login, or exit the application
     */
	public CafeOnlineOrderSystemGUI() throws Exception {
        setTitle("The Best Café Co.");
        // Set background image
        JLabel background = new JLabel(new ImageIcon("src/resources/background.jpg"));
        background.setLayout(new GridBagLayout());
        // Set up frame
        setSize(960, 600);
        // setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   		// Create screen components
        JPanel content = new JPanel();
        content.add(new JLabel("Welcome to The Best Café Co.!"));
        content.add(new JButton("Login"));
        content.add(new Button("Signup"));
        content.add(new Button("Exit"));
        add(content);

        cafe cafedata = cafe.DB;
        background.add(content, new GridBagConstraints() );
        add(background);

        setVisible(true);

        class ClickActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                // Login, signup, and exit buttons
                // new LoginScreen(this, cafedata.getUsers())
            }
        }
	}

    /*
     * Cafe Management System start
     */
    public static void main(String[] args) throws Exception {
        CafeOnlineOrderSystemGUI cafe = new CafeOnlineOrderSystemGUI();
    }
}
