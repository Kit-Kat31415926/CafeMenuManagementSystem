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

	public CustomerManagementScreen(JFrame parent, User user) {
		this.currentUser = user;


	}
	
	// TODO: this is copied from the midterm, still need to make more changes for it to work here
	/*
	private Customer makeCustomer(Scanner fileInput, String temp ) {
		User newUser = new User();  // create a new user object
		Boolean b =   temp.equals ("active")   ;
		newUser.setActive(b); // set the first thing read in to firstName
		newUser.setFirstName( fileInput.nextLine() ); // set the first thing read in to firstName
		newUser.setLastName(fileInput.nextLine());  // files reads line by line, so do .nextLine
		newUser.setEmail(fileInput.nextLine());
		newUser.setPassword(fileInput.nextLine());
		newUser.setLibraryCardNum(fileInput.nextLine());

		// if there is next line, it is checkout book isbn
		while (fileInput.hasNextLine()) {
			try {
				String tempLine = fileInput.nextLine(); // first thing we read from txt
				if (tempLine.trim().length() == 0) {   // break when no more lines to read
					break;
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		// System.out.println ( newUser );
		return newUser ;
	}
	
	public ArrayList<Customer> loadCustomers(String filename) {
		try {
			File file = new File(filename);
			Scanner scnr = new Scanner(file);
			
			// scans until it reaches the user data
			while (scnr.hasNextLine()) {
				if (scnr.nextLine().trim().equals("Users:")) {
					break;
				}
			}
			
			while (scnr.hasNextLine()) {   
				String temp = fileInput.nextLine();
				if (temp.trim().length() > 0 ) {  
					Customer newCustomer = makeCustomer( fileInput, temp );
					m_users.add ( newUser );
				}
			}
			fileInput.close();
		} catch (IOException ex ) {
			System.out.println(ex.getMessage());
		}
		return m_users;
	}
	*/

}
