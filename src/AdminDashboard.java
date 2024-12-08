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

	private JButton manageCustomersButton;
	private JButton manageMenuButton;
	private JButton customerLoginButton;

	public AdminDashboard(JFrame parent, User admin) {
		super("Admin Dashboard");
		this.admin = admin;
		UserManager userManager = new UserManager(); // TODO: don't forget this

		// Create frame
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		//Create welcome label
		JLabel header = new JLabel("Welcome, " + admin.getFirstName() + "!");
		header.setFont(new Font(Font.SERIF, Font.BOLD, 36));

		// create and add listeners to each button
		manageCustomersButton = new JButton("Manage Customers");
		manageCustomersButton.addActionListener(new ClickActionListener());

		manageMenuButton = new JButton("Manage Menu");
		manageMenuButton.addActionListener(new ClickActionListener());

		customerLoginButton = new JButton("Login as Customer");
		customerLoginButton.addActionListener(new ClickActionListener());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.VERTICAL;
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

	// TODO: CustomerManagementScreen and MenuManagementSceen will likely need a different constructor call
	class ClickActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Redirect to Customer Management Dashboard
			if (e.getSource() == manageCustomersButton) {
				CustomerManagementScreen customerManagement = new CustomerManagementScreen(AdminDashboard.this, admin);
				// Redirect to Menu Management Dashboard
			} else if (e.getSource() == manageMenuButton) {
				MenuManagementScreen menuManagement = new MenuManagementScreen();
				// redirect to customer dashboard
			} else if (e.getSource() == customerLoginButton) {
				CustomerDashboard customerDashboard = new CustomerDashboard(AdminDashboard.this, admin);
			} else {
				System.out.println("Error :(");
			}
		}
	}
}
