import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {
	private static final long serialVersionUID = 1L;

    // Constructor to initialize a Admin object 
    public Admin(String firstName, String lastName, String email, String userName, String password, boolean isActive) {
        super(firstName, lastName, email, userName, password, isActive);
    }

    @Override
    public String getRole() {
        return "Admin";
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
        return getRole() + ";" + getFirstName() + ";" + getLastName() + ";" + getEmail() + ";" + getUserName() + ";" + getPassword() + ";" + isActive() + ";" + getOrderedItems().toString();
    }
}
