import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {

	private User admin;

	private JButton manageCustomers;
	private JButton manageMenu;
	private JButton customerLogin;

	public AdminDashboard(JFrame parent, User admin) {
		super("Admin Dashboard");
		this.admin = admin;
		UserManager userManager = new UserManager(); // TODO: don't forget this


		setSize(960, 600);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		//Create header
		JLabel header = new JLabel("Welcome, " + admin.getFirstName() + "!");
		header.setFont(new Font(Font.SERIF, Font.BOLD, 36));

		// create and add listeners to each button
		manageCustomers = new JButton("Manage Customers");
		manageCustomers.addActionListener(new ClickActionListener());

		manageMenu = new JButton("Manage Menu");
		manageMenu.addActionListener(new ClickActionListener());

		customerLogin = new JButton("Login as Customer");
		customerLogin.addActionListener(new ClickActionListener());
		
		// TODO:fix layout, so the header is above
		
		// create gbc for the header
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill =      GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 50, 50, 50);
		
		// add the header the panel, add to frame
		JPanel welcome = new JPanel();
		welcome.add(header, gbc);
		this.add(welcome);

		// create gbc for buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        GridBagConstraints buttongbc = new GridBagConstraints();
        buttongbc.gridwidth = GridBagConstraints.REMAINDER;
        buttongbc.fill =      GridBagConstraints.HORIZONTAL;
        buttongbc.insets = new Insets(0, -135, 10, -135);
        buttons.setOpaque(false);
		
		// add buttons to panel, add to frame 
		buttons.add(manageCustomers);
		buttons.add(manageMenu);
		buttons.add(customerLogin);
        this.add(buttons, gbc);

		setVisible(true);
	}

	// TODO: CustomerManagementScreen and MenuManagementSceen will likely need a different constructor call
	class ClickActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Redirect to Customer Management Dashboard
			if (e.getSource() == manageCustomers) {
				CustomerManagementScreen customerManagement = new CustomerManagementScreen(AdminDashboard.this, admin);
				// Redirect to Menu Management Dashboard
			} else if (e.getSource() == manageMenu) {
				MenuManagementScreen menuManagement = new MenuManagementScreen();
				// redirect to customer dashboard
			} else if (e.getSource() == customerLogin) {
				CustomerDashboard customerDashboard = new CustomerDashboard(AdminDashboard.this, admin); 
			} else {
				System.out.println("Error :(");
			}
		}
	}
}
