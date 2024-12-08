import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class MenuItem implements Comparable<MenuItem>, Serializable {
    private String title;
    private String itemID;
    private String description;
    private float price;
    private int count;
    private boolean available;
    private boolean current;

    private ArrayList<MenuItem> menuItems = new ArrayList<>(); // Store menu items

    public MenuItem(String title, String itemID, String description, float price, int count, boolean current) {
        this.title = title;
        this.itemID = itemID;
        this.description = description;
        this.price = price;
        this.count = count;
        this.current = current;
        this.available = count > 0 && current;
    }

    public String getTitle() {
        return title;
    }

    public String getItemID() {
        return itemID;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String toDataString() {
        return getTitle() + ";" + getItemID() + ";" + getDescription() + ";" + getPrice() + ";" + getCount() + ";" + isAvailable();
    }

    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    public void removeItem(String itemID) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).getItemID().equals(itemID)) {
                menuItems.remove(i);
                break;
            }
        }
    }

    public Iterator<MenuItem> createIterator() {
        return menuItems.iterator();
    }

    public abstract String getMenuType();

    public int compareTo(MenuItem o) {
        return this.getTitle().compareTo(o.getTitle());
    }

}
	