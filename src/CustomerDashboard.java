import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/*
 * Creates dashboard for user to order items
 * @author - Kaitlyn Chiu
 */
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

    private Style cartfont;
    private Style billfont;
    private Style menufont;

    private ArrayList<MenuItem> itemsInCart;

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

    private final float TAX_PERCENT = 0.08f;

    /*
     * Create inner mouse-clicked class to highlight the text when clicking on JTextPane
     */
    class HighlightListener implements MouseListener {
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
                if (selectedMenu && styledPane == menuPane && position >= currentCursorStart && position <= currentCursorEnd) {
                    MenuItem menuItem = menuManager.getItemByTitle(getSelectedItemTitle());
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
            } catch (BadLocationException ex) {
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

    ;

    /*
     * Create inner button listener class to perform actions
     */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == addToCartButton) {
                    if (!selectedMenu || menuManager.getItemByTitle(getSelectedItemTitle()) == null) {
                        JOptionPane.showMessageDialog(CustomerDashboard.this, "Cannot add to cart: No item selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        addItemToCart(menuManager.getItemByTitle(getSelectedItemTitle()));
                    }
                } else if (e.getSource() == orderButton) {
                    currentUser.setOrderedItems(itemsInCart.stream().map(MenuItem::getItemID).collect(Collectors.toList()));
                    CafeOnlineOrderSystemGUI.writeToDatabase();
                    updateBill();
                } else if (e.getSource() == cancelButton) {
                    cancelItem();
                } else if (e.getSource() == sortButton || e.getSource() == searchButton || e.getSource() == dinnerCheckbox || e.getSource() == breakfastCheckbox) {
                    sortMenu();
                }
            } catch (Exception ex) {
                System.out.println("Something went wrong: " + ex.getMessage());
            }
        }
    }

    /*
     * Create new customer dashboard
     */
    public CustomerDashboard(JFrame parent, User currentUser) {
        super("Customer Dashboard");
        this.currentUser = currentUser;
        userManager = CafeOnlineOrderSystemGUI.USER_MANAGER;
        menuManager = CafeOnlineOrderSystemGUI.MENU_MANAGER;

        // Create frame
        setSize(1220, 700);
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
        shoppinggbc.fill = GridBagConstraints.VERTICAL;
        shoppinggbc.insets = new Insets(0, 0, 0, 0);

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

        shoppingPanel.add(mealTypePanel, shoppinggbc);

        // Create shopping cart
        shoppingPanel.add(new JLabel("Cart:"), shoppinggbc);
        cartDoc = new DefaultStyledDocument();
        cartPane = new JTextPane(cartDoc);
        cartPane.addMouseListener(new HighlightListener());
        // Make cursor invisible
        cartPane.setCaretColor(new Color(0, 0, 0, 0));
        // Set to uneditable
        cartPane.setEditable(false);
        cartPane.setHighlighter(null);
        // Highlight line when clicked
        cartPane.addMouseListener(new HighlightListener());
        cartPane.setPreferredSize(new Dimension(500, 175));
        itemsInCart = (ArrayList<MenuItem>) currentUser.getOrderedItems().stream()
                .map(title -> menuManager.getItemByTitle(title))
                .collect(Collectors.toList());
        try {
            itemsInCart = new ArrayList<>();
            cartDoc.insertString(0, formatMenu(itemsInCart), null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        // Set font
        cartfont = cartDoc.addStyle("font", null);
        StyleConstants.setFontFamily(cartfont, Font.MONOSPACED);
        cartDoc.setCharacterAttributes(0, cartDoc.getLength(), cartfont, false);

        shoppingPanel.add(cartPane, shoppinggbc);

        // Create bill
        shoppingPanel.add(new JLabel("Bill:"), shoppinggbc);
        billDoc = new DefaultStyledDocument();
        billPane = new JTextPane(billDoc);
        // Make cursor invisible
        billPane.setCaretColor(new Color(0, 0, 0, 0));
        // Set to uneditable
        billPane.setEditable(false);
        billPane.setHighlighter(null);
        billPane.setPreferredSize(new Dimension(500, 250));
        // Set font
        billfont = billDoc.addStyle("font", null);
        StyleConstants.setFontFamily(billfont, Font.MONOSPACED);
        billDoc.setCharacterAttributes(0, billDoc.getLength(), billfont, false);

        shoppingPanel.add(billPane, shoppinggbc);

        // Create tip buttons
        tipPanel = new JPanel();
        noTipButton = new JRadioButton("No Tip");
        noTipButton.setSelected(true);
        tenPercentButton = new JRadioButton("10% Tip");
        fifteenPercentButton = new JRadioButton("15% Tip");
        twentyPercentButton = new JRadioButton("20% Tip");
        
        noTipButton.addActionListener(e -> updateBill());
        tenPercentButton.addActionListener(e -> updateBill());
        fifteenPercentButton.addActionListener(e -> updateBill());
        twentyPercentButton.addActionListener(e -> updateBill());
        
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
        orderButton.addActionListener(new ButtonListener());
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ButtonListener());
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
        logoutPanel.add(new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName() + " : " + currentUser.getUserName()));
        JButton logout = new JButton("Logout");
        logoutPanel.add(logout);
        logout.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
		        if (currentUser.isAdmin()) {
		        	parent.dispose();
		        }
				CustomerDashboard.this.dispose();
			}
		});

        menuPanel.add(logoutPanel, menugbc);

        menuPanel.add(new JLabel("Cafe Menu:"), menugbc);
        menuDoc = new DefaultStyledDocument();
        menuPane = new JTextPane(menuDoc);
        // Make cursor invisible
        menuPane.setCaretColor(new Color(0, 0, 0, 0));
        // Set to uneditable
        menuPane.setEditable(false);
        menuPane.setHighlighter(null);
        // Highlight line when clicked
        menuPane.addMouseListener(new HighlightListener());
        menuPane.setPreferredSize(new Dimension(500, 475));
        // List all menu items in document
        try {
            menuDoc.insertString(0, formatMenu(menuManager.getActiveMenu()), null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        // Set font
        menufont = menuDoc.addStyle("font", null);
        StyleConstants.setFontFamily(menufont, Font.MONOSPACED);
        menuDoc.setCharacterAttributes(0, menuDoc.getLength(), menufont, false);

        menuPanel.add(menuPane, menugbc);

        // Create add to cart button
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ButtonListener());

        menuPanel.add(Box.createRigidArea(new Dimension(10, 10)), menugbc);
        menuPanel.add(addToCartButton, menugbc);

        docPanel.add(shoppingPanel);
        docPanel.add(menuPanel);

        // Create sort and search options
        JPanel searchPanel = new JPanel();

        JLabel sortOrderLabel = new JLabel("Sort Order: ");
        sortOrderChoice = new JComboBox<String>(new String[]{"", "Ascending", "Descending"});

        JLabel sortByLabel = new JLabel("Search/Sort By: ");
        sortByChoice = new JComboBox<String>(new String[]{"", "Title", "Price", "ItemID", "Description"});

        sortButton = new JButton("Sort");
        sortButton.addActionListener(new ButtonListener());

        searchInput = new JTextField();
        searchInput.setPreferredSize(new Dimension(200, 25));
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ButtonListener());

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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        mainContentPanel.add(docPanel, gbc);
        mainContentPanel.add(searchPanel, gbc);

        add(mainContentPanel);

        setVisible(true);
    }

    /*
     * Resets styling of all documents
     */
    public void resetAllDocStyling() {
        Style cartnostyle = cartDoc.addStyle("nostyle", null);
        Style menunostyle = menuDoc.addStyle("nostyle", null);
        StyleConstants.setBackground(cartnostyle, Color.white);
        StyleConstants.setBackground(menunostyle, Color.white);
        cartDoc.setCharacterAttributes(0, cartDoc.getLength(), cartnostyle, false);
        menuDoc.setCharacterAttributes(0, menuDoc.getLength(), menunostyle, false);
    }

    /*
     * Formats ArrayList of menu items
     * @param menuItems - items to be formatted
     * @return String - formatted menu
     */
    public String formatMenu(ArrayList<MenuItem> menuItems) {
        return menuItems.stream()
                .map((item) -> item.getTitle() +
                        " ".repeat(67 - item.getTitle().length() - String.format("%.2f", item.getPrice()).length())
                        + "$" + String.format("%.2f", item.getPrice()))
                .collect(Collectors.joining("\n"));
    }

    /*
     * Gets selected item's title
     * @return String - title of item selected/highlighted by user
     */
    public String getSelectedItemTitle() {
        try {
            String line = null;
            if (selectedMenu) {
                line = menuDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
            } else {
                line = cartDoc.getText(currentCursorStart, currentCursorEnd - currentCursorStart);
            }
            return line.substring(0, line.indexOf("  "));
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    /*
     * Adds given item to cart
     * @param menuItem - item to be added
     */
    public void addItemToCart(MenuItem menuItem) {
        // Edge case: Too many items
        if (itemsInCart.size() == 10) {
            JOptionPane.showMessageDialog(this, "Can't add any more items to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        itemsInCart.add(menuItem);
        displayCart();
    }

    /*
     * Cancels highlighted item in user's cart
     */
    public void cancelItem() {
        // Edge case: no item selected or menu item selected instead
		if ((currentCursorStart == 0 && currentCursorEnd == 0) || selectedMenu) {
			JOptionPane.showMessageDialog(this, "Can't add to cart: No item selected.", "Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        try {
            String cartString = cartDoc.getText(0, cartDoc.getLength());
            int index = 0;
            int position = 0;
            while (position != currentCursorStart) {
                index++;
                position = cartString.indexOf("\n", position + 1);
            }
            itemsInCart.remove(index);
            displayCart();
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Re-displays cart with styling
     */
    public void displayCart() {
        try {
            cartDoc.remove(0, cartDoc.getLength());
            cartDoc.insertString(0, formatMenu(itemsInCart), cartfont);
            updateBill();
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Updates bill with items in cart
     * Displays tax, tip, and totals
     */
    public void updateBill() {
        // Update user's items ordered
        currentUser.setOrderedItems(itemsInCart.stream().map(MenuItem::getItemID).collect(Collectors.toList()));
        // Update bill and set right align with spacing
        String bill = itemsInCart.stream()
                .map(item -> " ".repeat(62 - item.getTitle().length() - String.format("%.2f", item.getPrice()).length()) +
                        (((int) item.getPrice()) / 10 == 0 ? " " : "  ") +
                        item.getTitle() +
                        ": " +
                        (((int) item.getPrice()) / 10 == 0 ? "  " : " ") +
                        "$" +
                        String.format("%.2f", item.getPrice()))
                .collect(Collectors.joining("\n"));
        float subtotal = itemsInCart.stream().map(MenuItem::getPrice).reduce(0f, Float::sum);
        float tax = subtotal * TAX_PERCENT;
        float tipPercect = (float) (noTipButton.isSelected() ? 0 : (tenPercentButton.isSelected() ? 0.1 : (fifteenPercentButton.isSelected() ? 0.15 : 0.2)));
        float tip = subtotal * tipPercect;
        bill += "\nSubtotal: $" + String.format("%.2f", subtotal) +
                "\nTax: $" + String.format("%.2f", tax) +
                "\nTip: $" + String.format("%.2f", tip) +
                "\nTotal: $" + String.format("%.2f", subtotal + tax + tip);
        try {
            billDoc.remove(0, billDoc.getLength());
            billDoc.insertString(0, bill, billfont);

            Style right = billDoc.addStyle("right", null);
            StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
            billDoc.setParagraphAttributes(0, billDoc.getLength(), right, false);

            Style bold = billDoc.addStyle("bold", null);
            StyleConstants.setBold(bold, true);
            billDoc.setCharacterAttributes(billDoc.getText(0, billDoc.getLength()).lastIndexOf("\n"), billDoc.getLength(), bold, false);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    /*
     * Sorts menu using user options
     */
    public void sortMenu() {
        try {
            // Reset state variables
            currentCursorStart = 0;
            currentCursorEnd = 0;
            selectedMenu = false;
            menuDoc.remove(0, menuDoc.getLength());
            menuDoc.insertString(0, formatMenu(menuManager.updateMenu(breakfastCheckbox.isSelected(), dinnerCheckbox.isSelected(), sortOrderChoice.getSelectedItem(), sortByChoice.getSelectedItem(), searchInput.getText())), menufont);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}