import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Manages all users
 * @author - Arianna Gonzalez
 */
public class UserManager {

    private ArrayList<User> users;

    /*
     * Creates new user manager
     */
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
                if (!temp.trim().isEmpty()) {
                    String[] userDetails = temp.split(";");
                    if (userDetails[0].equals("Customer")) { // create a customer
                        Customer newCustomer = new Customer(userDetails[1], userDetails[2], userDetails[3],
                                userDetails[4], userDetails[5], userDetails[6].equals("true"));
                        if (userDetails.length > 7) { // if there are ordered items
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
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
	
	/*
	 * Gets all customers 
	 * @return ArrayList - all inactive customers
	 */
	public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        for (User u : users) {
            if (!u.isAdmin()) {
                customers.add((Customer) u);
            }
        }
        return customers;
	}

    /*
     * Gets all customers in sorted order
     * @return ArrayList - all inactive customers
     */
    public ArrayList<Customer> getSortedCustomers() {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        for (User u : users) {
            if (!u.isAdmin()) {
                customers.add((Customer) u);
            }
        }
        customers.sort(null);
        // Sort by regex
        ArrayList<Customer> result = new ArrayList<Customer>();
        for (Customer u : customers) {
            if (u.getSearchBy().matches(".*" + User.getRegex() + ".*")) {
                result.add(u);
            }
        }
        return result;
    }

    /*
     * Checks if login information is valid and user is active
     * @param userName - username of user logging in
     * @param password - password of user logging in
     * @return boolean - true if correct log in false otherwise
     */
    public boolean correctLogin(String userName, String password) {
        for (User u : users) {
            if (u.getUserName().equals(userName) && u.getPassword().equals(password) && u.isActive()) {
                return true;
            }
        }
        return false;
    }

    /*
     * Checks if someone is user with username
     * @param userName - username of user to check
     * @return boolean - true if is user false otherwise
     */
    public boolean isUser(String userName) {
        for (User u : users) {
            if (u.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Returns all users
     */
    public ArrayList<User> getAllUsers() {
        return users;
    }

    /*
     * Gets user based on username
     * @param userName - gets user based on username
     * @return User - user with given username
     */
    public User getUser(String userName) {
        for (User u : users) {
            if (u.getUserName().equals(userName)) {
                return u;
            }
        }
        return null;
    }

	/*
	 * Gets user based on their name
	 * @param name - given name to find user with
	 * @return User - user based on name
	 */
	public User getUserFromName(String name) {
		for (User u: users) {
			if (name.contains(u.getFirstName()) && name.contains(u.getLastName())) {
				return u;
			}
		}
		return null;
	}

    /*
     * Adds new user to list of current users and updates database
     * @param u - user to be added
     */
    public void addUser(User u) {
        users.add(u);
        CafeOnlineOrderSystemGUI.writeToDatabase();
    }

	/*
	 * Removes user and updates database
	 * @param user - user to be removed
	 */
	public void remove(User user) {
		this.users.remove(user);
        CafeOnlineOrderSystemGUI.writeToDatabase();
	}

	/*
	 * Sets sorting algorithm for each user
	 * @param sortBy - sorts ascending or descending
	 * @param sea
	 */
	public void sortAll(Object sortAsc, Object sortBy, String regex) {
		User.setAsc(((String) sortAsc).equals("Ascending"));
		User.setSortBy((String) sortBy);
		User.setRegex(regex);
	}
}
