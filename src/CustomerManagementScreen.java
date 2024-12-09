import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/*
 * Creates screen to manage customers
 * @author - Arianna Gonzalez
 */
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
    private JButton add;
    private JButton edit;
    private JButton delete;

    private JButton search;
    private JButton sort;
    private JTextField searchInput;
    private JComboBox<String> sortOrderComboBox;
    private JComboBox<String> searchSortByComboBox;

    // variables for MouseClickListener
    private int currentCursorStart;
    private int currentCursorEnd = -1;
    private boolean isActivePane = false;

    /*
     * Creates new customer management screen
     */
    public CustomerManagementScreen(JFrame parent, User user) {
        // basic set up
        super("Customer Management Dashboard");
        this.parent = parent;
        this.currentUser = user;
        this.userManager = CafeOnlineOrderSystemGUI.USER_MANAGER;
        this.activeCustomersPane = new JTextPane();
        this.activeUsersDoc = activeCustomersPane.getStyledDocument();
        this.inactiveCustomersPane = new JTextPane();
        this.inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();

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
        // active customer textPane
        this.activeCustomersPane = new JTextPane();
        activeCustomersPane.setEditable(false);
        activeCustomersPane.setHighlighter(null);
        activeCustomersPane.setCaretColor(new Color(0, 0, 0, 0));
        activeCustomersPane.setPreferredSize(new Dimension(300, 300));
        MouseClickListener activeCustomersPaneListener = new MouseClickListener();
        activeCustomersPane.addMouseListener(activeCustomersPaneListener);

        // inactive customer textPane
        this.inactiveCustomersPane = new JTextPane();
        inactiveCustomersPane.setEditable(false);
        inactiveCustomersPane.setHighlighter(null);
        inactiveCustomersPane.setCaretColor(new Color(0, 0, 0, 0));
        inactiveCustomersPane.setPreferredSize(new Dimension(300, 300));
        MouseClickListener inactiveCustomersPaneListener = new MouseClickListener();
        inactiveCustomersPane.addMouseListener(inactiveCustomersPaneListener);

        // create and write customers to panes
        writeToDocs();

        // create activate and reactivate Buttons
        reactivate = new JButton("Reactivate");
        reactivate.addActionListener(new ButtonListener());
        inactivate = new JButton("Inactivate");
        inactivate.addActionListener(new ButtonListener());

        // create combo Box for management pop-ups
        userTypeComboBox = new JComboBox<String>();
        userTypeComboBox.addItem("Customer");
        userTypeComboBox.addItem("Admin");

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
        sortOrderComboBox.addItem("Descending");
        sortOrderComboBox.setOpaque(false);

        // create label and comboBox for search/sort by
        JLabel searchSortBy = new JLabel("Search/Sort By:");
        searchSortByComboBox = new JComboBox<String>();
        searchSortByComboBox.addItem("First Name");
        searchSortByComboBox.addItem("Last Name");
        searchSortByComboBox.addItem("Username");
        searchSortByComboBox.addItem("Email");
        searchSortByComboBox.setOpaque(false);

        // create label and textField for searching/sorting
        sort = new JButton("Sort");

        searchInput = new JTextField();
        searchInput.setPreferredSize(new Dimension(200, 25));
        search = new JButton("Search");

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

    /*
     * Writes all users to documents
     */
    private void writeToDocs() {
        activeUsersDoc = activeCustomersPane.getStyledDocument();
        try {
            activeUsersDoc.remove(0, activeUsersDoc.getLength());
            inactiveUsersDoc = inactiveCustomersPane.getStyledDocument();
            inactiveUsersDoc.remove(0, inactiveUsersDoc.getLength());

            for (Customer c : userManager.getSortedActiveCustomers()) {
                activeUsersDoc.insertString(0, c.getFirstName() + " " + c.getLastName() + "\n", null);
            }

            for (Customer c : userManager.getSortedInactiveCustomers()) {
                inactiveUsersDoc.insertString(0, c.getFirstName() + " " + c.getLastName() + "\n", null);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException();
        }
    }

    /*
     * Creates new mouse listener to highlight names
     */
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
                    for (String item : user.getOrderedItems()) {
                        userDetails += item + " ";
                    }
                    JOptionPane.showMessageDialog(CustomerManagementScreen.this, userDetails, "Item Details", JOptionPane.INFORMATION_MESSAGE);
                }
                // Update state variables
                currentCursorStart = cursorStart;
                currentCursorEnd = cursorEnd;
                isActivePane = styledPane == activeCustomersPane;
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

    /*
     * Creates class to react to button click
     */
    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logout) {
                dispose();
                parent.dispose();
            } else if (e.getSource() == reactivate) {
                try {
                    if (isActivePane) {
                        JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user to reactivate.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    User user = userManager.getUserFromName(getSelectedName());
                    user.setActive(true);
                    currentCursorEnd = -1;
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                writeToDocs();
            } else if (e.getSource() == inactivate) {
                try {
                    if (!isActivePane) {
                        JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user to inactivate.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    User user = userManager.getUserFromName(getSelectedName());
                    user.setActive(false);
                    currentCursorEnd = -1;
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                writeToDocs();
            } else if (e.getSource() == add) {
                JPanel addUser = new JPanel();
                JTextField firstName = new JTextField();
                firstName.setPreferredSize(new Dimension(150, 25));
                JTextField lastName = new JTextField();
                lastName.setPreferredSize(new Dimension(150, 25));
                JTextField email = new JTextField();
                email.setPreferredSize(new Dimension(150, 25));
                JTextField password = new JTextField();
                password.setPreferredSize(new Dimension(150, 25));
                ButtonGroup status = new ButtonGroup();
                JRadioButton active = new JRadioButton("Active");
                status.add(active);
                JRadioButton inactive = new JRadioButton("Inactive");
                status.add(inactive);

                addUser.add(new JLabel("User Type:"));
                addUser.add(userTypeComboBox);
                addUser.add(new JLabel("First Name:"));
                addUser.add(firstName);
                addUser.add(new JLabel("Last Name:"));
                addUser.add(lastName);
                addUser.add(new JLabel("Email:"));
                addUser.add(email);
                addUser.add(new JLabel("Password:"));
                addUser.add(password);
                addUser.add(new JLabel("Status:"));
                addUser.add(active);
                addUser.add(inactive);

                var success = JOptionPane.showConfirmDialog(null, addUser, "Enter user details", JOptionPane.OK_CANCEL_OPTION);
                int usernameNum = (int) (Math.random() * 9999) % 10000;
                if (success == 0 && userTypeComboBox.getSelectedItem().equals("Customer")) {
                    userManager.addUser(new Customer(firstName.getText(), lastName.getText(), email.getText(),
                            firstName.getText() + String.format("%04d", usernameNum), password.getText(), active.isSelected()));
                    writeToDocs();
                } else if (success == 0 && userTypeComboBox.getSelectedItem().equals("Admin")) {
                    userManager.addUser(new Admin(firstName.getText(), lastName.getText(), email.getText(),
                            firstName.getText() + String.format("%04d", usernameNum), password.getText(), active.isSelected()));
                }
            } else if (e.getSource() == edit) {
                try {
                    User user = userManager.getUserFromName(getSelectedName());
                    currentCursorEnd = -1;
                    JPanel editUser = new JPanel();
                    JTextField firstName = new JTextField();
                    firstName.setText(user.getFirstName());
                    firstName.setPreferredSize(new Dimension(150, 25));
                    JTextField lastName = new JTextField();
                    lastName.setText(user.getLastName());
                    lastName.setPreferredSize(new Dimension(150, 25));
                    JTextField email = new JTextField();
                    email.setText(user.getEmail());
                    email.setPreferredSize(new Dimension(150, 25));
                    JTextField password = new JTextField();
                    password.setText(user.getPassword());
                    password.setPreferredSize(new Dimension(150, 25));
                    ButtonGroup status = new ButtonGroup();
                    JRadioButton active = new JRadioButton("Active");
                    status.add(active);
                    JRadioButton inactive = new JRadioButton("Inactive");
                    status.add(inactive);
                    userTypeComboBox.setSelectedIndex(0);
                    if (user.isActive()) {
                        active.setSelected(true);
                    } else {
                        inactive.setSelected(true);
                    }

                    editUser.add(new JLabel("User Type:"));
                    editUser.add(userTypeComboBox);
                    editUser.add(new JLabel("First Name:"));
                    editUser.add(firstName);
                    editUser.add(new JLabel("Last Name:"));
                    editUser.add(lastName);
                    editUser.add(new JLabel("Email:"));
                    editUser.add(email);
                    editUser.add(new JLabel("Password:"));
                    editUser.add(password);
                    editUser.add(new JLabel("Status:"));
                    editUser.add(active);
                    editUser.add(inactive);

                    var success = JOptionPane.showConfirmDialog(null, editUser, "Update User Details", JOptionPane.OK_CANCEL_OPTION);
                    String userName = user.getUserName();
                    if (success == 0 && userTypeComboBox.getSelectedItem().equals("Customer")) {
                        userManager.remove(user);
                        userManager.addUser(new Customer(firstName.getText(), lastName.getText(), email.getText(),
                                userName, password.getText(), active.isSelected()));
                        writeToDocs();
                    } else if (success == 0 && userTypeComboBox.getSelectedItem().equals("Admin")) {
                        userManager.remove(user);
                        userManager.addUser(new Admin(firstName.getText(), lastName.getText(), email.getText(),
                                userName, password.getText(), active.isSelected()));
                        writeToDocs();
                    }
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == delete) {
                try {
                    User user = userManager.getUserFromName(getSelectedName());
                    var success = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (success == 0) {
                        userManager.remove(user);
                        writeToDocs();
                    }
                    currentCursorEnd = -1;
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(CustomerManagementScreen.this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == sort || e.getSource() == search) {
                sort.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        userManager.sortAll(sortOrderComboBox.getSelectedItem(), searchSortByComboBox.getSelectedItem(), searchInput.getText());
                        writeToDocs();
                    }
                });
            }

        }

    }
}