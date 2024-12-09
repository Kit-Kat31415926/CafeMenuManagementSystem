import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private static final long serialVersionUID = 1L;


    public Customer(String firstName, String lastName, String email, String userName, String password, boolean isActive) {
        super(firstName, lastName, email, userName, password, isActive);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public boolean isAdmin() {
        return "Admin".equals(getRole());
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
        if (User.getSortBy().equals("First Name")) {
            if (User.isAsc()) {
                return this.getFirstName().compareTo(o.getFirstName());
            } else {
                return - this.getFirstName().compareTo(o.getFirstName());
            }
        } else if (User.getSortBy().equals("Last Name")) {
            if (User.isAsc()) {
                return this.getLastName().compareTo(o.getLastName());
            } else {
                return - this.getLastName().compareTo(o.getLastName());
            }
        } else if (User.getSortBy().equals("Username")) {
            if (User.isAsc()) {
                return this.getUserName().compareTo(o.getUserName());
            } else {
                return - this.getUserName().compareTo(o.getUserName());
            }
        } else if (User.getSortBy().equals("Email")) {
            if (User.isAsc()) {
                return this.getEmail().compareTo(o.getEmail());
            } else {
                return -this.getEmail().compareTo(o.getEmail());
            }
        }
        return 0;
    }

}
