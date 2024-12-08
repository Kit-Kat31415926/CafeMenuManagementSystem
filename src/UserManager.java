import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManager {

	private ArrayList<User> users; 

	public UserManager() {
		users = new ArrayList<User>();
		try {
			File file = new File("cafeData.txt");
			Scanner scnr = new Scanner(file);

			// scans until it reaches the user data
			while (scnr.hasNextLine()) {
				if (scnr.nextLine().trim().equals("Users:")) {
					break;
				}
			}

			// scans each line to create users 
			while (scnr.hasNextLine()) {   
				String temp = scnr.nextLine();
				if (temp.trim().length() > 0 ) {  
					String[] userDetails = temp.split(";");
					if (userDetails[0].equals("Customer")) { // create a customer
						Customer newCustomer = new Customer(userDetails[1], userDetails[2], userDetails[3], 
															userDetails[4], userDetails[5], userDetails[6].equals("true"));
						if(userDetails.length > 6) { // if there are ordered items 
							ArrayList<String> order = new ArrayList<String>();
							for (int i = 7; i < userDetails.length; i++) {
								order.add(userDetails[i]);
							}
							newCustomer.setOrderedItems(order);
						}
						users.add(newCustomer);
					} else if (userDetails[0].equals("Admin")) { // create an admin
						users.add(new Admin(userDetails[1], userDetails[2], userDetails[3], 
												   userDetails[4], userDetails[5], userDetails[6].equals("true")));
					}
				}
			}
			scnr.close();
		} catch (IOException ex ) {
			System.out.println(ex.getMessage());
		}
	}
	
	public ArrayList<Customer> getActiveCustomers() {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		for (User u: users) {
			if (!u.isAdmin() && u.isActive()) {
				customers.add((Customer)u);
			}
		}
		return customers;
	}
	
	public ArrayList<Customer> getInactiveCustomers() {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		for (User u: users) {
			if (!u.isAdmin() && !u.isActive()) {
				customers.add((Customer)u);
			}
		}
		return customers;
	}
	
	public boolean correctLogin(String userName, String password) {
		for (User u: users) {
			if (u.getUserName().equals(userName) && u.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isUser(String userName) {
		for (User u: users) {
			if (u.getUserName().equals(userName)) {
				return true;
			}
		}
		return false;
	}
	
	public User getUser(String userName) {
		for (User u: users) {
			if (u.getUserName().equals(userName)) {
				return u;
			}
		}
		return null;
	}
}
