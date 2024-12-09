import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements User, Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private boolean isActive;
    private List<String> orderedItems;
    private static final int MAX_ORDER_LIMIT = 10;

    public Customer(String firstName, String lastName, String email, String userName, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
        this.orderedItems = new ArrayList<>();
    }
	// xxx your codes
    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public List<String> getOrderedItems() {
        return orderedItems;
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public void orderItems(MenuItem item) throws CustomExceptions.ItemNotAvailableException {
        orderedItems.add(item.getItemID());
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void setOrderedItems(List<String> orderedItems) {
        this.orderedItems = orderedItems;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void cancelItem(MenuItem item) {
        orderedItems.remove(item.getItemID());
    }

    @Override
    public boolean canPlace() {
        return orderedItems.size() < MAX_ORDER_LIMIT;
    }

    @Override
    public String getDetails() {
        // TODO: Return details
        return "[User details here]";
    }

    @Override
    public String toDataString() {
        // TODO: Return data as string
        return "[User data]";
    }

    /*
     * Compares by username
     * @param User - user to be compared with
     * @return - integer difference
     */
    @Override
    public int compareTo(User o) {
        return this.getUserName().compareTo(o.getUserName());
    }

}
