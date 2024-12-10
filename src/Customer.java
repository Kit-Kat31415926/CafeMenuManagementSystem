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

}
