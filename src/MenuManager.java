import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Scanner;

public class MenuManager {
    private ArrayList<MenuItem> menuItems;

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
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
     * Gets all menu items
     */
    public ArrayList<MenuItem> getMenu() {
        return menuItems;
    }

    /*
     * Gets MenuItem by title
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
}
