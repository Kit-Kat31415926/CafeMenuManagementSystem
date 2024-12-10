import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class User implements Comparable<User>, Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private boolean isActive;
    private List<String> orderedItems;
	private boolean ascending = true;
    private static final int MAX_ORDER_LIMIT = 10;

    private static boolean asc;
    private static String sortBy = "";
    private static String regex;

    public User(String firstName, String lastName, String email, String userName, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
        this.orderedItems = new ArrayList<>();
    }

    public static boolean isAsc() {
        return asc;
    }

    public static void setAsc(boolean asc) {
        User.asc = asc;
    }

    public static String getSortBy() {
        return sortBy;
    }

    public static void setSortBy(String sortBy) {
        User.sortBy = sortBy;
    }

    public static String getRegex() {
        return regex;
    }

    public static void setRegex(String regex) {
        User.regex = regex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<String> getOrderedItems() {
        return orderedItems;
    }

    public abstract String getRole();  // e.g., "Admin" or "Customer"


    public void orderItems(MenuItem item) throws CustomExceptions.ItemNotAvailableException {
        orderedItems.add(item.getItemID());
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setOrderedItems(List<String> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void cancelItem(MenuItem item) {
        orderedItems.remove(item.getItemID());
    }

    public boolean canPlace() {
        return orderedItems.size() < MAX_ORDER_LIMIT;
    }

    public abstract String getDetails();

    public abstract boolean isAdmin();

    public abstract String toDataString(); // Convert the object to a string for saving to a file

    static Admin fromDataString(String data) {
        // Implement a basic structure, each subclass will have its own logic
        throw new UnsupportedOperationException("fromDataString() must be implemented in the subclass.");
    }
    
    /*
     * Compare method
     * @param o - other menu item to compare with
     */
    @Override
    public int compareTo(User o) {
        if (sortBy.equals("Name")) {
            if (ascending) {
                if (this.getLastName().compareTo(o.getLastName()) == 0) {
                	return this.getFirstName().compareTo(o.getFirstName());
                } else {
                	return this.getLastName().compareTo(o.getLastName());
                }
            } else {
            	if (this.getLastName().compareTo(o.getLastName()) == 0) {
                	return -1 * this.getFirstName().compareTo(o.getFirstName());
                } else {
                	return -1 * this.getLastName().compareTo(o.getLastName());
                }
            }
        } else if (sortBy.equals("Username")) {
            if (ascending) {
                return (this.getUserName().compareTo(o.getUserName()));
            } else {
                return  - (this.getUserName().compareTo(o.getUserName()));
            }
        } else if (sortBy.equals("Email")) {
            if (ascending) {
                return (this.getEmail().compareTo(o.getEmail()));
            } else {
                return - (this.getEmail().compareTo(o.getEmail()));
            }
        }
        return 0;
    }

    /*
     * Gets value to search by
     *  @return String - value to be searched by with regex
     */
    public String getSearchBy() {
        if (sortBy.equals("Name")) {
            return getFirstName() + " " + getLastName();
        } else if (sortBy.equals("Username")) {
            return getUserName();
        } else if (sortBy.equals("Email")) {
            return getEmail();
        }
        return  null;
    }
}
