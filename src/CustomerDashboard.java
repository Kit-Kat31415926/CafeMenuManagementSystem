import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDashboard extends JFrame {

	private UserManager userManager;
	private MenuManager menuManager;
	private User currentUser; // The user who is currently logged in

	private JTextPane cartPane;
	private JTextPane billPane;
	private JTextPane menuPane;
	private StyledDocument cartDoc;
	private StyledDocument billDoc;
	private StyledDocument menuDoc;

	private JCheckBox breakfastCheckbox;
	private JCheckBox dinnerCheckbox;

	private JPanel tipPanel;
	private ButtonGroup tipGroup;
	private JRadioButton noTipButton;
	private JRadioButton tenPercentButton;
	private JRadioButton fifteenPercentButton;
	private JRadioButton twentyPercentButton;

	private JButton orderButton;
	private JButton cancelButton;
	private JButton addToCartButton;

	private JComboBox<String> sortOrderChoice;
	private JComboBox<String> sortByChoice;
	private JButton sortButton;
	private JTextField searchInput;
	private JButton searchButton;

	private int currentCursorStart;
	private int currentCursorEnd;
	private boolean selectedMenu = false;

	// Create inner mouse-clicked class to highlight the text when clicking on JTextPane
	class MouseClicked implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				// Reset style of all panes
				Style cartnostyle = cartDoc.addStyle("nostyle", null);
				Style menunostyle = menuDoc.addStyle("nostyle", null);
				StyleConstants.setBackground(cartnostyle, Color.white);
				StyleConstants.setBackground(menunostyle, Color.white);
				StyledDocument cartStyledDoc = cartPane.getStyledDocument();
				StyledDocument menuStyledDoc = menuPane.getStyledDocument();
				cartDoc.setCharacterAttributes(0, cartStyledDoc.getLength(), cartnostyle, false);
				menuDoc.setCharacterAttributes(0, menuStyledDoc.getLength(), menunostyle, false);

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
				if (selectedMenu && styledPane == menuPane && position >= currentCursorStart && position <= currentCursorEnd) {
					MenuItem menuItem = menuManager.getItemByTitle(styledDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart));
					String menuDetails = "Title: " + menuItem.getTitle() + "\n" +
							"Description: " + menuItem.getDescription() + "\n" +
							"Item ID: " + menuItem.getItemID() + "\n" +
							"Price: $" + String.format("%.2f", menuItem.getPrice()) + "\n" +
							"Count: " + menuItem.getCount() + "\n";
					JOptionPane.showMessageDialog(CustomerDashboard.this, menuDetails, "Item Details", JOptionPane.INFORMATION_MESSAGE);
				}
				// Update state variables
				currentCursorStart = cursorStart;
				currentCursorEnd = cursorEnd;
				selectedMenu = styledPane == menuPane;
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
	};

	public CustomerDashboard(JFrame parent, User currentUser) {
		super("Customer Dashboard");
		this.currentUser = currentUser;
		userManager = new UserManager();
		menuManager = new MenuManager();

		// Create frame
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		// Create all docs
		JPanel docPanel = new JPanel();

		// Create doc for user shopping
		JPanel shoppingPanel = new JPanel();
		shoppingPanel.setLayout(new GridBagLayout());
		GridBagConstraints shoppinggbc = new GridBagConstraints();
		shoppinggbc.gridwidth = GridBagConstraints.REMAINDER;
		shoppinggbc.fill =      GridBagConstraints.VERTICAL;
		shoppinggbc.insets = new Insets(0, 0, 0, 0);

		// Create meal checkboxes
		JPanel mealTypePanel = new JPanel();
		breakfastCheckbox = new JCheckBox("Breakfast");
		dinnerCheckbox = new JCheckBox("Dinner");
		mealTypePanel.add(breakfastCheckbox);
		mealTypePanel.add(dinnerCheckbox);

		shoppingPanel.add(mealTypePanel, shoppinggbc);

		// Create shopping cart
		shoppingPanel.add(new JLabel("Cart:"), shoppinggbc);
		cartDoc = new DefaultStyledDocument();
		cartPane = new JTextPane(cartDoc);
		cartPane.addMouseListener(new MouseClicked());
		// Make cursor invisible
		cartPane.setCaretColor(new Color(0,0,0,0));
		// Set to uneditable
		cartPane.setEditable(false);
		// Highlight line when clicked
		cartPane.addMouseListener(new MouseClicked());
		cartPane.setPreferredSize(new Dimension(375,125));
        try {
            cartDoc.insertString(0,"FOOD", null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        shoppingPanel.add(cartPane, shoppinggbc);

		// Create bill
		shoppingPanel.add(new JLabel("Bill:"), shoppinggbc);
		billDoc = new DefaultStyledDocument();
		billPane = new JTextPane(billDoc);
		billPane.addMouseListener(new MouseClicked());
		// Make cursor invisible
		billPane.setCaretColor(new Color(0,0,0,0));
		// Set to uneditable
		billPane.setEditable(false);
		billPane.setPreferredSize(new Dimension(375,125));

		shoppingPanel.add(billPane, shoppinggbc);

		// Create tip buttons
		tipPanel = new JPanel();
		noTipButton = new JRadioButton("No Tip");
		tenPercentButton = new JRadioButton("10% Tip");
		fifteenPercentButton = new JRadioButton("15% Tip");
		twentyPercentButton = new JRadioButton("20% Tip");

		tipGroup = new ButtonGroup();
		tipGroup.add(noTipButton);
		tipGroup.add(tenPercentButton);
		tipGroup.add(fifteenPercentButton);
		tipGroup.add(twentyPercentButton);

		tipPanel.add(noTipButton);
		tipPanel.add(tenPercentButton);
		tipPanel.add(fifteenPercentButton);
		tipPanel.add(twentyPercentButton);

		shoppingPanel.add(tipPanel, shoppinggbc);

		// Create order buttons
		JPanel orderButtonPanel = new JPanel();
		orderButton = new JButton("Order");
		cancelButton = new JButton("Cancel");
		orderButtonPanel.add(orderButton);
		orderButtonPanel.add(cancelButton);

		shoppingPanel.add(orderButtonPanel, shoppinggbc);

		// Create panel for showing all menu items
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints menugbc = new GridBagConstraints();
		menugbc.fill = GridBagConstraints.VERTICAL;
		menugbc.gridwidth = GridBagConstraints.REMAINDER;
		menugbc.insets = new Insets(0, 0, 0, 0);

		// Create logout button
		JPanel logoutPanel = new JPanel();
		logoutPanel.add(new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName() + ":" + currentUser.getUserName()));
		logoutPanel.add(new JButton("Logout") {{
			addActionListener(e -> CustomerDashboard.this.dispose());
		}});

		menuPanel.add(logoutPanel, menugbc);

		menuPanel.add(new JLabel("Cafe Menu:"), menugbc);
		menuDoc = new DefaultStyledDocument();
		menuPane = new JTextPane(menuDoc);
		menuPane.setHighlighter(null);
		// Make cursor invisible
		menuPane.setCaretColor(new Color(0,0,0,0));
		// Set to uneditable
		menuPane.setEditable(false);
		// Highlight line when clicked
		menuPane.addMouseListener(new MouseClicked());
		menuPane.setPreferredSize(new Dimension(375,300));
		// List all menu items in document
		try {
            menuDoc.insertString(0, menuManager.getMenu().stream().map(MenuItem::getTitle).collect(Collectors.joining("\n")), null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        menuPanel.add(menuPane, menugbc);

		// Create add to cart button
		addToCartButton = new JButton("Add to Cart");

		menuPanel.add(Box.createRigidArea(new Dimension(10, 10)), menugbc);
		menuPanel.add(addToCartButton, menugbc);

		docPanel.add(shoppingPanel);
		docPanel.add(menuPanel);

		// Create sort and search options
		JPanel searchPanel = new JPanel();

		JLabel sortOrderLabel = new JLabel("Sort Order: ");
		sortOrderChoice = new JComboBox<String>(new String[] {"Ascending", "Descending"});

		JLabel sortByLabel = new JLabel("Search/Sort By: ");
		sortByChoice = new JComboBox<String>(new String[] {"Title", "Price"});

		sortButton = new JButton("Sort");

		searchInput = new JTextField();
		searchInput.setPreferredSize(new Dimension(200, 25));
		searchButton = new JButton("Search");

		searchPanel.add(sortOrderLabel);
		searchPanel.add(sortOrderChoice);
		searchPanel.add(sortByLabel);
		searchPanel.add(sortByChoice);
		searchPanel.add(sortButton);
		searchPanel.add(searchInput);
		searchPanel.add(searchButton);

		// Set layout of main panel
		JPanel mainContentPanel = new JPanel();
		mainContentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill =      GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0,0,0,0);

		mainContentPanel.add(docPanel, gbc);
		mainContentPanel.add(searchPanel, gbc);

		add(mainContentPanel);

		setVisible(true);
	}
}
