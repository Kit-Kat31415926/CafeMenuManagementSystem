import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CustomerManagementScreen extends JFrame {

	private UserManager userManager;
	private User currentUser; // The user who is currently logged in
	private JTextPane inactiveCustomersPane;
	private JTextPane activeCustomersPane;
	private StyledDocument inactiveUsersDoc;
	private StyledDocument activeUsersDoc;
	private JComboBox<String> userTypeComboBox;
	private Map<String, String> nameToUserNameMap = new HashMap<>();
	private JButton logout;
	private JFrame parent;

	public CustomerManagementScreen(JFrame parent, User user) {
		super("Customer Management Dashboard");
		this.currentUser = user;
		this.parent = parent;

		setSize(960, 600);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		//create the name display and logout button
		JPanel topBar = new JPanel();
		topBar.setAlignmentY(1);
		logout = new JButton("Logout");
		logout.addActionListener(new ClickActionListener());
		JLabel name = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName() + 
				" : " + currentUser.getUserName());
		topBar.add(name);
		topBar.add(logout);
		
		// create the labels for the customer panes
		JPanel inactive = new JPanel();
		JPanel active = new JPanel();
		JLabel inactiveLabel = new JLabel("Café Inactive Customers:");
		inactiveLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));
		JLabel activeLabel = new JLabel("Café Active Customers:");
		activeLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));
		
		// create and write customers to panes
		sortActiveInactive(loadCustomers("cafeData.txt"));
		
		// create activate and reactivateButtons
		// TODO: add clickListeners
		JButton reactivate = new JButton("Reactivate");
		JButton inactivate = new JButton("Inactivate");
		
		inactive.setLayout(new BoxLayout(inactive, BoxLayout.Y_AXIS));
		inactive.add(inactiveLabel);
		inactive.add(inactiveCustomersPane);
		inactive.add(reactivate);
		
		active.setLayout(new BoxLayout(active, BoxLayout.Y_AXIS));
		active.add(activeLabel);
		active.add(activeCustomersPane);
		active.add(inactivate);
		
		JPanel customersPanel = new JPanel();
		customersPanel.add(inactive);
		customersPanel.add(active);
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(topBar);
		this.add(customersPanel);



		setVisible(true);
	}
	
	private void sortActiveInactive(ArrayList<Customer> customers) {
		activeCustomersPane = new JTextPane();
		activeUsersDoc = activeCustomersPane.getStyledDocument();
		inactiveCustomersPane = new JTextPane();
		inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();
		
		for (Customer c : customers) {
			if (c.isActive()) {
				try {
					activeUsersDoc.insertString(0, c.getFirstName() + " " + c.getLastName() + "\n", null);
				} catch (BadLocationException e) {
					
				}
			} else {
				try {
					inactiveUsersDoc.insertString(0, c.getFirstName() + " " + c.getLastName() + "\n", null);
				} catch (BadLocationException e) {
					
				}
			}
		}
	}
	
	private ArrayList<Customer> loadCustomers(String filename) {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		try {
			File file = new File(filename);
			Scanner scnr = new Scanner(file);

			// scans until it reaches the user data
			while (scnr.hasNextLine()) {
				if (scnr.nextLine().trim().equals("Users:")) {
					break;
				}
			}
			
			// scans each line to create customer objects
			while (scnr.hasNextLine()) {   
				String temp = scnr.nextLine();
				if (temp.trim().length() > 0 ) {  
					String[] customerDetails = temp.split(";");
					if (customerDetails[0].equals("Customer")) { // create a customer, don't need admins
						Customer newCustomer = new Customer(customerDetails[1], customerDetails[2], customerDetails[3], 
															customerDetails[4], customerDetails[5], customerDetails[6].equals("true"));
						if(customerDetails.length > 6) { // if there are ordered items 
							ArrayList<String> order = new ArrayList<String>();
							for (int i = 7; i < customerDetails.length; i++) {
								order.add(customerDetails[i]);
							}
							newCustomer.setOrderedItems(order);
						}
						customers.add(newCustomer);
					} 
				}
			}
			scnr.close();
		} catch (IOException ex ) {
			System.out.println(ex.getMessage());
		}
		return customers;
	}
	

	class ClickActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// close dashboards and redirect to homescreen
			if (e.getSource() == logout) {
				dispose();
				parent.dispose();
			}  else {
				System.out.println("Error :(");
			}
		}
	}
}
