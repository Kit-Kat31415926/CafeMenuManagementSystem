import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Displays dashboard for admins
 * @author - Arianna Gonzalez
 */
public class AdminDashboard extends JFrame {
	private User admin;

	public AdminDashboard(JFrame parent, User admin) {
		super("Admin Dashboard");
		this.admin = admin;
		UserManager userManager = CafeOnlineOrderSystemGUI.USER_MANAGER; // TODO: don't forget this

		// Create frame
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		//Create welcome label
		JLabel header = new JLabel("Welcome, " + admin.getFirstName() + "!", SwingConstants.CENTER);
		header.setFont(new Font(Font.SERIF, Font.BOLD, 36));

		// create and add listeners to each button
		JButton manageCustomersButton = new JButton("Manage Customers");
		manageCustomersButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				CustomerManagementScreen customerManagement = new CustomerManagementScreen(AdminDashboard.this, admin);
			}
		});
		JButton manageMenuButton = new JButton("Manage Menu");
		manageMenuButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				MenuManagementScreen menuManagement = new MenuManagementScreen(AdminDashboard.this, admin);
			}
		});

		JButton customerLoginButton = new JButton("Login as Customer");
		customerLoginButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				CustomerDashboard customerDashboard = new CustomerDashboard(AdminDashboard.this, admin); 
			}
		});

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, -150, 50, -150);


		// create gbc for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3));
		buttonPanel.setOpaque(false);

		// add buttons to panel, add to frame 
		buttonPanel.add(manageCustomersButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(1,10)));
		buttonPanel.add(manageMenuButton);

		contentPanel.add(header, gbc);
		contentPanel.add(buttonPanel, gbc);
		contentPanel.add(customerLoginButton, gbc);

		add(contentPanel);
		setVisible(true);
	}
}
