import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;

/*
 * Manages and sorts menu
 * @author - Kaitlyn Chiu
 */
public class MenuManager {
    private ArrayList<MenuItem> menuItems;

    /*
     * Creates new Menu Manager
     */
    public MenuManager() {
        // Loads all menu items
        menuItems = new ArrayList<MenuItem>();
        try {
            File file = new File("cafedata.txt");
            Scanner scan = new Scanner(file);

            // Scans until it reaches the menu data
            while (scan.hasNextLine()) {
                if (scan.nextLine().trim().equals("Menu:")) {
                    break;
                }
            }

            // Scans each line to create menu objects
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                // Check if line is valid menu item
                if (!line.trim().isEmpty()) {
                    String[] menuItemDetails = line.split(";");
                    if (menuItemDetails[0].equals("Diner")) {
                        MenuItem newDiner = new DinerMenuItem(menuItemDetails[1], menuItemDetails[2], menuItemDetails[3],
                                Float.parseFloat(menuItemDetails[4]), Integer.parseInt(menuItemDetails[5]), menuItemDetails[6].equals("true"));
                        menuItems.add(newDiner);
                    } else if (menuItemDetails[0].equals("Pancake")) {
                        MenuItem newPancake = new PancakeMenuItem(menuItemDetails[1], menuItemDetails[2], menuItemDetails[3],
                                Float.parseFloat(menuItemDetails[4]), Integer.parseInt(menuItemDetails[5]), menuItemDetails[6].equals("true"));
                        menuItems.add(newPancake);
                    }
                }
            }
            scan.close();
            //menuItems = (ArrayList<MenuItem>) menuItems.stream().filter(MenuItem::isAvailable).collect(Collectors.toList());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
     * Gets all menu items
     * @return ArrayList - all menu items available
     */
    public ArrayList<MenuItem> getMenu() {
        return menuItems;
    }

    /*
     * Gets MenuItem by title
     * @param title - String representation of item title
     * @return MenuItem - item with given title
     */
    public MenuItem getItemByTitle(String title) {
        title = title.trim();
        for (MenuItem item : menuItems) {
            if (item.getTitle().equals(title)) {
                return item;
            }
        }
        return null;
    }

    public void add(MenuItem menuItem) {
    	this.menuItems.add(menuItem);
        CafeOnlineOrderSystemGUI.writeToDatabase();
    }

    public void remove(MenuItem menuItem) {
    	this.menuItems.remove(menuItem);
        CafeOnlineOrderSystemGUI.writeToDatabase();
    }
    
    public ArrayList<MenuItem> getActiveMenu() {
    	return (ArrayList<MenuItem>) menuItems.stream().filter(MenuItem::isAvailable).collect(Collectors.toList());
    }

    /*
     * Returns string of updated menu after searching/sorting
     * @param breakfast - true if items on pancake menu should be displayed
     * @param dinner - true if items on diner menu should be displayed
     * @param sortOrder - whether to sort ascending or descending
     * @param sortBy - whether to sort by title or price
     * @param regex - regex to match with
     * @return ArrayList - list of all matching items on menu
     */
    public ArrayList<MenuItem> updateMenu(boolean breakfast, boolean dinner, Object sortOrder, Object sortBy, String regex) {
        MenuItem.setAscending(((String)sortOrder).equals("Ascending"));
        MenuItem.setSortBy((String)sortBy);
        ArrayList<MenuItem> updatedMenuItems = new ArrayList<MenuItem>();
        // Add all breakfast and dinner items
        for (MenuItem item : menuItems) {
            if (breakfast && item.getMenuType().equals("Pancake")) {
                updatedMenuItems.add(item);
            }
            if (dinner && item.getMenuType().equals("Diner")) {
                updatedMenuItems.add(item);
            }
        }
        // Sort by chosen order
        if (!((String) sortBy).isEmpty()) {
            // Remove based on regex
            for (int i = 0; i < updatedMenuItems.size(); i++) {
                if (!updatedMenuItems.get(i).getSearchBy().matches(".*" + regex + ".*")) {
                    updatedMenuItems.remove(i);
                    i--;
                }
            }
            if (!((String) sortOrder).isEmpty()) {
                MenuItem.setAscending(sortOrder.equals("Ascending"));
                MenuItem.setSortBy((String) sortBy);
                updatedMenuItems.sort(null);
            }
        }
        return updatedMenuItems;
    }
}