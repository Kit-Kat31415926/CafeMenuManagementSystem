import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * Creates a menu item
 * @author - Kaitlyn Chiu
 */
public abstract class MenuItem implements Comparable<MenuItem>, Serializable {
    private String title;
    private String itemID;
    private String description;
    private float price;
    private int count;
    private boolean available;
    private boolean current;

    private static boolean ascending;
    private static String sortBy;

    private ArrayList<MenuItem> menuItems = new ArrayList<>(); // Store menu items

    /*
     * Creates new menu item
     * @param title - title of item
     * @param itemID - ID of item
     * @param price - cost of item
     * @param count - amount of item available
     * @param current - if item is in-season
     */
    public MenuItem(String title, String itemID, String description, float price, int count, boolean current) {
        this.title = title;
        this.itemID = itemID;
        this.description = description;
        this.price = price;
        this.count = count;
        this.current = current;
        this.available = count > 0 && current;
    }

    /*
     * Gets title of item
     * @return String - title of item
     */
    public String getTitle() {
        return title;
    }

    /*
     * Gets ID of item
     * @return String - ID of item
     */
    public String getItemID() {
        return itemID;
    }

    /*
     * Gets description of item
     * @return String - description of item
     */
    public String getDescription() {
        return description;
    }

    /*
     * Gets price of item
     * @return float - title of price
     */
    public float getPrice() {
        return price;
    }

    /*
     * Gets count of item
     * @return int - how many of item are left
     */
    public int getCount() {
        return count;
    }

    /*
     * Sets count of item
     * @param count - new count of item
     */
    public void setCount(int count) {
        this.count = count;
    }

    /*
     * Gets availability of item
     * @return boolean - availability of item
     */
    public boolean isAvailable() {
        return available;
    }

    /*
     * Sets availability of item
     * @param available - boolean true if available false otherwise
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /*
     * Gets currency of item
     * @return boolean - true if item is current false otherwise
     */
    public boolean isCurrent() {
        return current;
    }

    /*
     * Sets currency of item
     * @param current - true if item is current, false otherwise
     */
    public void setCurrent(boolean current) {
        this.current = current;
    }

    /*
     * Gets string representation of item
     * @return String - string representation of item
     */
    public String toDataString() {
        return getTitle() + ";" + getItemID() + ";" + getDescription() + ";" + getPrice() + ";" + getCount() + ";" + isAvailable();
    }

    /*
     * Adds new item to menu
     * @param item - new item to add
     */
    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    /*
     * Remove item from menu
     * @param itemID - ID of item to remove
     */
    public void removeItem(String itemID) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).getItemID().equals(itemID)) {
                menuItems.remove(i);
                break;
            }
        }
    }

//    public Iterator<MenuItem> createIterator() {
//        return menuItems.iterator();
//    }

    /*
     * Gets type of menu
     * @return String - type of menu
     */
    public abstract String getMenuType();

    /*
     * Sets sort by
     * @param ascending - true if sort by ascending false otherwise
     */
    public static void setAscending(boolean ascending) {
        MenuItem.ascending = ascending;
    }

    /*
     * Sets what to sort by
     * @param sortBy - sorts by title, price, etc
     */
    public static void setSortBy(String sortBy) {
        MenuItem.sortBy = sortBy;
    }

    /*
     * Compare method
     * @param o - other menu item to compare with
     */
    public int compareTo(MenuItem o) {
        if (sortBy.equals("Title")) {
            if (ascending) {
                return this.getTitle().compareTo(o.getTitle());
            } else {
                return  - this.getTitle().compareTo(o.getTitle());
            }
        } else if (sortBy.equals("Price")) {
            if (ascending) {
                return (int) (this.getPrice() - o.getPrice());
            } else {
                return (int) (o.getPrice() - this.getPrice());
            }
        }
        return 0;
    }
}
	