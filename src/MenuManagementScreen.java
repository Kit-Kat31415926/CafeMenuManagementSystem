import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/*
 * Creates screen to manage menu
 * @author - Arianna Gonzalez
 */

public class MenuManagementScreen extends JFrame {

	private MenuManager menuManager;
	private User currentUser; // The user who is currently logged in
	private JTextPane backUpMenuPane;
	private JTextPane activeMenuPane;
	private StyledDocument backUpMenuDoc;
	private StyledDocument activeMenuDoc;
	private JComboBox<String> sortOrderComboBox;
	private JComboBox<String> menuItemTypeComboBox;
	private JComboBox<String> searchSortByComboBox;
	private JFrame parent;
	
	private JCheckBox breakfastCheckbox;
	private JCheckBox dinnerCheckbox;
	
	private JTextField searchSortInput;
	
	// Buttons
	private JButton logout;
	private JButton reactivate;
	private JButton inactivate;
	private JButton add;
	private JButton edit;
	private JButton delete;
	private JButton sort;
	private JButton search;

	// variables for MouseClickListener
	private int currentCursorStart;
	private int currentCursorEnd = -1;
	private boolean isActivePane = false;

	public MenuManagementScreen(JFrame parent, User user) {
		// basic set up
		super("Customer Management Dashboard");
		this.currentUser = user;
		this.menuManager = new MenuManager();
		this.parent = parent;

		setSize(960, 600);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// Create meal checkboxes
		JPanel mealTypePanel = new JPanel();
		breakfastCheckbox = new JCheckBox("Breakfast");
		breakfastCheckbox.setSelected(true);
		breakfastCheckbox.addActionListener(new ButtonListener());
		dinnerCheckbox = new JCheckBox("Dinner");
		dinnerCheckbox.setSelected(true);
		dinnerCheckbox.addActionListener(new ButtonListener());
		mealTypePanel.add(breakfastCheckbox);
		mealTypePanel.add(dinnerCheckbox);
		
		//create the name display and logout button at the top
		JPanel topBar = new JPanel();
		logout = new JButton("Logout");
		logout.addActionListener(new ButtonListener());
		JLabel name = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName() + " : " + currentUser.getUserName());
		topBar.add(Box.createRigidArea(new Dimension(200, 0)));
		topBar.add(breakfastCheckbox);
		topBar.add(dinnerCheckbox);
		topBar.add(Box.createRigidArea(new Dimension(300, 0)));
		topBar.add(name);
		topBar.add(logout);
		
		

		// create the labels for the customer panes
		JPanel inactive = new JPanel();
		JPanel active = new JPanel();
		inactive.setLayout(new GridBagLayout());
		active.setLayout(new GridBagLayout());

		
		JLabel inactiveLabel = new JLabel("Backup (Off-season) Menu:");
		inactiveLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));
		JLabel activeLabel = new JLabel("Active Menu:");
		activeLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));	

		// set up the customer textPanes
		// active customer textPane
		this.activeMenuPane = new JTextPane();
		activeMenuPane.setEditable(false);
		activeMenuPane.setPreferredSize(new Dimension(300, 300));
        activeMenuPane.setHighlighter(null);
        activeMenuPane.setCaretColor(new Color(0, 0, 0, 0));
		MouseClickListener activeCustomersPaneListener = new MouseClickListener();
		activeMenuPane.addMouseListener(activeCustomersPaneListener);

		// inactive customer textPane
		this.backUpMenuPane = new JTextPane();
		backUpMenuPane.setEditable(false);
		backUpMenuPane.setPreferredSize(new Dimension(300, 300));
        backUpMenuPane.setHighlighter(null);
        backUpMenuPane.setCaretColor(new Color(0, 0, 0, 0));
		MouseClickListener inactiveCustomersPaneListener = new MouseClickListener();
		backUpMenuPane.addMouseListener(inactiveCustomersPaneListener);

		// create and write customers to panes
		writeToDocs(menuManager.getMenu());

		// create activate and reactivate Buttons
		reactivate = new JButton("Reactivate");
		reactivate.addActionListener(new ButtonListener());
		inactivate = new JButton("Inactivate");
		inactivate.addActionListener(new ButtonListener());

		// create combo Box for management pop-ups
		menuItemTypeComboBox = new JComboBox<String>();
		menuItemTypeComboBox.addItem("Dinner");
		menuItemTypeComboBox.addItem("Breakfast");

		// create management buttons
		add = new JButton("Add");
		add.addActionListener(new ButtonListener());
		edit = new JButton("Edit");
		edit.addActionListener(new ButtonListener());
		delete = new JButton("Delete");
		delete.addActionListener(new ButtonListener());

		// add management buttons to panel
		JPanel managementButtons = new JPanel();
		managementButtons.add(add);
		managementButtons.add(edit);
		managementButtons.add(delete);

		//create label and comboBox for sort order
		JLabel sortOrder = new JLabel("Sort Order:");
		sortOrderComboBox = new JComboBox<String>();
		sortOrderComboBox.addItem("Ascending");
		sortOrderComboBox.addItem("Decending");
		sortOrderComboBox.setOpaque(false);

		// create label and comboBox for search/sort by
		JLabel searchSortBy = new JLabel("Search/Sort By:");
		searchSortByComboBox = new JComboBox<String>();
		searchSortByComboBox.addItem("Title");
		searchSortByComboBox.addItem("Description");
		searchSortByComboBox.addItem("ItemID");
		searchSortByComboBox.addItem("Price");
		searchSortByComboBox.setOpaque(false);

		// create label and textField for searching/sorting
		sort = new JButton("Sort");
		sort.addActionListener(new ButtonListener());
		searchSortInput = new JTextField();
		searchSortInput.setPreferredSize(new Dimension(200, 25));
		search = new JButton("Search");
		search.addActionListener(new ButtonListener());

		// adding search/sort components to a panel
		JPanel searchSortPanel = new JPanel();
		searchSortPanel.add(sortOrder);
		searchSortPanel.add(sortOrderComboBox);
		searchSortPanel.add(searchSortBy);
		searchSortPanel.add(searchSortByComboBox);
		searchSortPanel.add(sort);
		searchSortPanel.add(searchSortInput);
		searchSortPanel.add(search);

		inactive.setLayout(new BoxLayout(inactive, BoxLayout.Y_AXIS));
        inactive.add(inactiveLabel);
        inactive.add(backUpMenuPane);
        inactive.add(reactivate);

		active.setLayout(new BoxLayout(active, BoxLayout.Y_AXIS));
		active.add(activeLabel);
		active.add(activeMenuPane);
		active.add(inactivate);

		JPanel customersPanel = new JPanel();
		customersPanel.add(inactive);
		customersPanel.add(active);

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(topBar);
		this.add(customersPanel);
		this.add(managementButtons);
		this.add(searchSortPanel);


		// TODO: fix GUI
		setVisible(true);
	}

	public void resetAllDocStyling() {
		Style cartnostyle = activeMenuDoc.addStyle("nostyle", null);
		Style menunostyle = backUpMenuDoc.addStyle("nostyle", null);
		StyleConstants.setBackground(cartnostyle, Color.white);
		StyleConstants.setBackground(menunostyle, Color.white);
		activeMenuDoc.setCharacterAttributes(0, activeMenuDoc.getLength(), cartnostyle, false);
		backUpMenuDoc.setCharacterAttributes(0, backUpMenuDoc.getLength(), menunostyle, false);
	}

	public String getSelectedName() {
		try {
			String line = null;
			if (isActivePane) {
				line = activeMenuDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
			} else {
				line = backUpMenuDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
			}
			return line;
		} catch (BadLocationException ex) {
			System.out.println("Could not get customer name");
		}
		return null;
	}
	
	private void writeToDocs(ArrayList<MenuItem> items) {
		activeMenuDoc = activeMenuPane.getStyledDocument();
		backUpMenuDoc = backUpMenuPane.getStyledDocument();
		try {
			activeMenuDoc.remove(0, activeMenuDoc.getLength());
			backUpMenuDoc.remove(0, backUpMenuDoc.getLength());
			for (MenuItem m : items) {
				if ((breakfastCheckbox.isSelected() && m instanceof PancakeMenuItem) || (dinnerCheckbox.isSelected() && m instanceof DinerMenuItem)){
					if (m.isAvailable()) {
						activeMenuDoc.insertString(0, String.format("%-50s $%.2f",m.getTitle(), m.getPrice()) + "\n", null);
					} else if (!m.isAvailable()){
						backUpMenuDoc.insertString(0, String.format("%-50s $%.2f",m.getTitle(), m.getPrice()) + "\n", null);
					}
				}
			} 
		} catch (BadLocationException e) {}
	}
	
	public String getSelectedItemTitle() {
		try {
			String line = null;
			if (isActivePane) {
				line = activeMenuDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
			} else {
				line = backUpMenuDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
			}
			return line.substring(0, line.indexOf("  "));
		} catch (BadLocationException ex) {
			System.out.println("Could not get title of selected item");
		}
		return null;
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
					MenuItem menuItem = menuManager.getItemByTitle(getSelectedItemTitle());
					String menuDetails = "Title: " + menuItem.getTitle() + "\n" +
							"Description: " + menuItem.getDescription() + "\n" +
							"Item ID: " + menuItem.getItemID() + "\n" +
							"Price: $" + String.format("%.2f", menuItem.getPrice()) + "\n" +
							"Count: " + menuItem.getCount() + "\n";
					JOptionPane.showMessageDialog(MenuManagementScreen.this, menuDetails, "Item Details", JOptionPane.INFORMATION_MESSAGE);					
				}
				// Update state variables
				currentCursorStart = cursorStart;
				currentCursorEnd = cursorEnd;
				isActivePane = styledPane == activeMenuPane;
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
					MenuItem menuItem = menuManager.getItemByTitle(getSelectedItemTitle());
					menuItem.setAvailable(true);
					currentCursorEnd = -1;
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(MenuManagementScreen.this, "Please select a menu item.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				writeToDocs(menuManager.getMenu());
			} else if (e.getSource() == inactivate) {
				try {
					MenuItem menuItem = menuManager.getItemByTitle(getSelectedItemTitle());
					menuItem.setAvailable(false);
					currentCursorEnd = -1;
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(MenuManagementScreen.this, "Please select a menu item.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				writeToDocs(menuManager.getMenu());
			} else if (e.getSource() == add) {
				JPanel addMenuItem = new JPanel();
				JTextField title = new JTextField();
				title.setPreferredSize(new Dimension(150, 25));
				JTextField description = new JTextField();
				description.setPreferredSize(new Dimension(150, 25));
				JTextField itemID = new JTextField();
				itemID.setPreferredSize(new Dimension(150, 25));
				JTextField price = new JTextField();
				price.setPreferredSize(new Dimension(150, 25));
				JTextField count = new JTextField();
				count.setPreferredSize(new Dimension(150, 25));
				ButtonGroup status = new ButtonGroup();
				JRadioButton current = new JRadioButton("current");
				status.add(current);
				JRadioButton offSeason = new JRadioButton("Off-season");
				status.add(offSeason);

				addMenuItem.add(new JLabel("Menu Type:"));
				addMenuItem.add(menuItemTypeComboBox);
				addMenuItem.add(new JLabel("Title:"));
				addMenuItem.add(title);
				addMenuItem.add(new JLabel("Description:"));
				addMenuItem.add(description);
				addMenuItem.add(new JLabel("Item ID:"));
				addMenuItem.add(itemID);
				addMenuItem.add(new JLabel("Price:"));
				addMenuItem.add(price);
				addMenuItem.add(new JLabel("Count:"));
				addMenuItem.add(count);
				addMenuItem.add(new JLabel("Status:"));
				addMenuItem.add(current);
				addMenuItem.add(offSeason);

				var success = JOptionPane.showConfirmDialog(null, addMenuItem, "Enter menu item details", JOptionPane.OK_CANCEL_OPTION);
				if (success == 0 && menuItemTypeComboBox.getSelectedItem().equals("Dinner")) {
					menuManager.add(new DinerMenuItem(title.getText(), itemID.getText(), description.getText(), 
							Float.valueOf(price.getText()), Integer.valueOf(count.getText()), current.isSelected()));
					writeToDocs(menuManager.getMenu());
				} else if (success == 0 && menuItemTypeComboBox.getSelectedItem().equals("Breakfast")) {
					menuManager.add(new PancakeMenuItem(title.getText(), itemID.getText(), description.getText(), 
							Float.valueOf(price.getText()), Integer.valueOf(count.getText()), current.isSelected()));
				}
			} else if (e.getSource() == edit) {
				try {
					MenuItem menuItem = menuManager.getItemByTitle(getSelectedItemTitle());
					currentCursorEnd = -1;
					JPanel editMenuItem = new JPanel();
					JTextField title = new JTextField();
					title.setText(menuItem.getTitle());
					title.setPreferredSize(new Dimension(150, 25));
					JTextField description = new JTextField();
					description.setText(menuItem.getDescription());
					description.setPreferredSize(new Dimension(150, 25));
					JTextField itemID = new JTextField();
					itemID.setText(menuItem.getItemID());
					itemID.setPreferredSize(new Dimension(150, 25));
					JTextField price = new JTextField();
					price.setText("" + (menuItem.getPrice()));
					price.setPreferredSize(new Dimension(150, 25));
					JTextField count = new JTextField();
					count.setText("" + menuItem.getCount());
					count.setPreferredSize(new Dimension(150, 25));
					ButtonGroup status = new ButtonGroup();
					JRadioButton current = new JRadioButton("current");
					status.add(current);
					JRadioButton offSeason = new JRadioButton("Off-season");
					status.add(offSeason);
					if(menuItem.isAvailable()) {
						current.setSelected(true);
					} else {
						offSeason.setSelected(true);
					}
					if(menuItem instanceof DinerMenuItem) {
						menuItemTypeComboBox.setSelectedIndex(0);
					} else {
						menuItemTypeComboBox.setSelectedIndex(1);
					}

					editMenuItem.add(new JLabel("Menu Type:"));
					editMenuItem.add(menuItemTypeComboBox);
					editMenuItem.add(new JLabel("Title:"));
					editMenuItem.add(title);
					editMenuItem.add(new JLabel("Description:"));
					editMenuItem.add(description);
					editMenuItem.add(new JLabel("Item ID:"));
					editMenuItem.add(itemID);
					editMenuItem.add(new JLabel("Price:"));
					editMenuItem.add(price);
					editMenuItem.add(new JLabel("Count:"));
					editMenuItem.add(count);
					editMenuItem.add(new JLabel("Status:"));
					editMenuItem.add(current);
					editMenuItem.add(offSeason);

					var success = JOptionPane.showConfirmDialog(null, editMenuItem, "Update Menu Item Details", JOptionPane.OK_CANCEL_OPTION);
					if (success == 0 && menuItemTypeComboBox.getSelectedItem().equals("Dinner")) {
						menuManager.remove(menuItem);
						menuManager.add(new DinerMenuItem(title.getText(), itemID.getText(), description.getText(), 
								Float.valueOf(price.getText()), Integer.valueOf(count.getText()), current.isSelected()));
						writeToDocs(menuManager.getMenu());
					} else if (success == 0 && menuItemTypeComboBox.getSelectedItem().equals("Breakfast")) {
						menuManager.remove(menuItem);
						menuManager.add(new PancakeMenuItem(title.getText(), itemID.getText(), description.getText(), 
								Float.valueOf(price.getText()), Integer.valueOf(count.getText()), current.isSelected()));
						writeToDocs(menuManager.getMenu());
					} 
					resetAllDocStyling();
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(MenuManagementScreen.this, "Please select a menu item.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else if (e.getSource() == delete) {
				try {
					MenuItem menuItem = menuManager.getItemByTitle(getSelectedItemTitle());
					var success = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this menu item?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
					if (success == 0) {
						menuManager.remove(menuItem);
						writeToDocs(menuManager.getMenu());
					}
					currentCursorEnd = -1;
					resetAllDocStyling();
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(MenuManagementScreen.this, "Please select a menu item.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else if (e.getSource() == breakfastCheckbox || e.getSource() == dinnerCheckbox) {
				writeToDocs(menuManager.getMenu());
			} else if (e.getSource() == search || e.getSource() == sort) {
				System.out.println(sortOrderComboBox.getSelectedItem().toString());
            	MenuItem.setAscending(!sortOrderComboBox.getSelectedItem().toString().equals("Ascending"));
				MenuItem.setSortBy((String)(searchSortByComboBox.getSelectedItem()));
				ArrayList<MenuItem> result = (ArrayList<MenuItem>)menuManager.getMenu().stream()
						.filter(m -> searchSortInput.getText().length() <= 0 || (m.getSearchBy()).contains(searchSortInput.getText()))
						.sorted()
						.collect(Collectors.toList());
				writeToDocs(result);
			}

		}

	}
}
