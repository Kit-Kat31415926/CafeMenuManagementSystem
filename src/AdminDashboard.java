import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {

    private User admin;

    public AdminDashboard(JFrame parent, User admin) {
        super("Admin Dashboard");
        this.admin = admin;
        UserManager userManager = new UserManager(); 

   		// xxx your codes
        setSize(960, 600);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setVisible(true);
	}
}
