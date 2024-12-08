import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
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
	private Map<String, User> nameToUserMap = new HashMap<>();
	private JFrame parent;
	
	// Buttons
	private JButton logout;
	private JButton reactivate;
	private JButton inactivate;
	// variables for MouseClickListener
	private int currentCursorStart;
	private int currentCursorEnd;
	private boolean isActivePane = false;

	public CustomerManagementScreen(JFrame parent, User user) {
		// basic set up
		super("Customer Management Dashboard");
		this.currentUser = user;
		this.userManager = new UserManager();
		this.parent = parent;

		setSize(960, 600);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		//create the name display and logout button at the top
		JPanel topBar = new JPanel();
		logout = new JButton("Logout");
		logout.addActionListener(new ButtonListener());
		JLabel name = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName() + " : " + currentUser.getUserName());
		topBar.add(name);
		topBar.add(logout);

		// create the labels for the customer panes
		JPanel inactive = new JPanel();
		JPanel active = new JPanel();
		JLabel inactiveLabel = new JLabel("Café Inactive Customers:");
		inactiveLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));
		JLabel activeLabel = new JLabel("Café Active Customers:");
		activeLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));	

		// set up the customer textPanes
		this.activeCustomersPane = new JTextPane();
		activeCustomersPane.setEditable(false);
		//this.activeUsersDoc = activeCustomersPane.getStyledDocument();
		MouseClickListener activeCustomersPaneListener = new MouseClickListener();
		activeCustomersPane.addMouseListener(activeCustomersPaneListener);

		this.inactiveCustomersPane = new JTextPane();
		inactiveCustomersPane.setEditable(false);
		//this.inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();
		MouseClickListener inactiveCustomersPaneListener = new MouseClickListener();
		inactiveCustomersPane.addMouseListener(inactiveCustomersPaneListener);

		// create and write customers to panes
		writeToDocs();

		// create activate and reactivateButtons
		reactivate = new JButton("Reactivate");
		reactivate.addActionListener(new ButtonListener());
		inactivate = new JButton("Inactivate");
		inactivate.addActionListener(new ButtonListener());

		// create management buttons
		// TODO: implement buttons
		JButton add = new JButton("Add");
		JButton edit = new JButton("Edit");
		JButton delete = new JButton("Delete");

		// add management buttons to panel
		JPanel managementButtons = new JPanel();
		managementButtons.add(add);
		managementButtons.add(edit);
		managementButtons.add(delete);

		//create label and comboBox for sort order
		JLabel sortOrder = new JLabel("Sort Order:");
		JComboBox<String> sortOrderComboBox = new JComboBox<String>();
		sortOrderComboBox.addItem("Ascending");
		sortOrderComboBox.addItem("Decending");
		sortOrderComboBox.setOpaque(false);

		// create label and comboBox for search/sort by
		JLabel searchSortBy = new JLabel("Search/Sort By:");
		JComboBox<String> searchSortByComboBox = new JComboBox<String>();
		searchSortByComboBox.addItem("First Name");
		searchSortByComboBox.addItem("Last Name");
		searchSortByComboBox.addItem("Username");
		searchSortByComboBox.addItem("Email");
		searchSortByComboBox.setOpaque(false);

		// create label and textField for searching/sorting
		// TODO: implement sort and search button
		JButton sort = new JButton("Sort");
		JTextField searchInput = new JTextField();
		searchInput.setPreferredSize(new Dimension(200, 25));
		JButton search = new JButton("Search");

		// adding search/sort components to a panel
		JPanel searchSortPanel = new JPanel();
		searchSortPanel.add(sortOrder);
		searchSortPanel.add(sortOrderComboBox);
		searchSortPanel.add(searchSortBy);
		searchSortPanel.add(searchSortByComboBox);
		searchSortPanel.add(sort);
		searchSortPanel.add(searchInput);
		searchSortPanel.add(search);

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
		this.add(managementButtons);
		this.add(searchSortPanel);


		// TODO: fix GUI, probably using GridBagLayout
		setVisible(true);
	}
	
	public void resetAllDocStyling() {
		Style cartnostyle = activeUsersDoc.addStyle("nostyle", null);
		Style menunostyle = inactiveUsersDoc.addStyle("nostyle", null);
		StyleConstants.setBackground(cartnostyle, Color.white);
		StyleConstants.setBackground(menunostyle, Color.white);
		activeUsersDoc.setCharacterAttributes(0, activeUsersDoc.getLength(), cartnostyle, false);
		inactiveUsersDoc.setCharacterAttributes(0, inactiveUsersDoc.getLength(), menunostyle, false);
	}
	
	public String getSelectedName() {
		try {
			String line = null;
			if (isActivePane) {
				line = activeUsersDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
			} else {
				line = inactiveUsersDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
			}
			return line;
		} catch (BadLocationException ex) {
			System.out.println("Could not get customer name");
		}
		return null;
	}
	
	private void writeToDocs() {
		activeUsersDoc = activeCustomersPane.getStyledDocument();
		try {
			activeUsersDoc.remove(0, activeUsersDoc.getLength());
		} catch (BadLocationException e) {}
		inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();
		try {
			inactiveUsersDoc.remove(0, inactiveUsersDoc.getLength());
		} catch (BadLocationException e) {}
		
		for(Customer c: userManager.getActiveCustomers()) {
			try {
				activeUsersDoc.insertString(0, c.getFirstName() + " " + c.getLastName() + "\n", null);
			} catch (BadLocationException e) {}
		}

		for(Customer c: userManager.getInactiveCustomers()) {
			try {
				inactiveUsersDoc.insertString(0, c.getFirstName() + " " + c.getLastName() + "\n", null);
			} catch (BadLocationException e) {}
		}
	}
	
	class MouseClickListener implements MouseListener {


		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				resetAllDocStyling();
				// Highlight selected text
				JTextPane styledPane = ((JTextPane) e.getSource());
				StyledDocument styledDoc = styledPane.getStyledDocument();
				int position = styledPane.getCaretPosition();
				int cursorStart = styledDoc.getText(0, styledDoc.getLength()).lastIndexOf("\n", position);
				if (cursorStart == -1) {
					cursorStart = 0;
				}
				int cursorEnd = styledDoc.getText(0, styledDoc.getLength()).indexOf("\n", position);
				if (cursorEnd == -1) {
					cursorEnd = styledDoc.getLength();
				}

				// Color newly clicked menu item
				Style highlightstyle = styledDoc.addStyle("highlightstyle", null);
				StyleConstants.setBackground(highlightstyle, Color.PINK);
				// Set style
				styledDoc.setCharacterAttributes(cursorStart, cursorEnd - cursorStart, highlightstyle, false);
				// Edge case: double click on menu item
				if (position >= currentCursorStart && position <= currentCursorEnd) {
					User user = userManager.getUserFromName(getSelectedName());
					String userDetails = "Name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
							"Username: " + user.getUserName() + "\n" +
							"Email: " + user.getEmail() + "\n" +
							"Active: " + user.isActive() + "\n" +
							"Password: " + user.getPassword() + "\n" +
							"Ordered Items: ";
					for(String item : user.getOrderedItems()) {
						userDetails += item + " ";
					}
					JOptionPane.showMessageDialog(CustomerManagementScreen.this, userDetails, "Item Details", JOptionPane.INFORMATION_MESSAGE);
				}
				// Update state variables
				currentCursorStart = cursorStart;
				currentCursorEnd = cursorEnd;
				isActivePane = styledPane == activeCustomersPane;
			} catch(BadLocationException ex) {
				throw new RuntimeException(ex);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			return;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			return;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			return;
		}

		@Override
		public void mouseExited(MouseEvent e) {
			return;
		}

	}
	
	public class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == logout) {
				dispose();
				parent.dispose();
			} else if (e.getSource() == reactivate){
				try {
					User user = userManager.getUserFromName(getSelectedName());
					user.setActive(true);
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				writeToDocs();
			} else if (e.getSource() == inactivate) {
				try {
					User user = userManager.getUserFromName(getSelectedName());
					user.setActive(false);
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				writeToDocs();
			}

		}

	}
}