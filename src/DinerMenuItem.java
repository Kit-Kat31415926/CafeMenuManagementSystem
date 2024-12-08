import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class DinerMenuItem extends MenuItem  {
	private static final long serialVersionUID = 1L;

    public DinerMenuItem(String title, String itemID, String description, float price, int count, boolean current) {
        super(title, itemID, description, price, count, current);
    }

    @Override
    public String getMenuType() {
        return "Diner";
    }

    @Override
    public String toDataString() {
        return getMenuType() + ";" + super.toDataString();
    }
}
