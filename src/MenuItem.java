import java.awt.*;
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

    private static boolean ascending;
    private static String sortBy;

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

    public static void setAscending(boolean ascending) {
        MenuItem.ascending = ascending;
    }

    public static void setSortBy(String sortBy) {
        MenuItem.sortBy = sortBy;
    }

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
	