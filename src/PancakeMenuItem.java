/*
 * Creates new item on pancake menu
 * @author - Kaitlyn Chiu
 */
public class PancakeMenuItem extends MenuItem {
    private static final long serialVersionUID = 1L;

    /*
     * Creates new pancake menu item
     * @param title - title of item
     * @param itemID - ID of item
     * @param price - cost of item
     * @param count - amount of item available
     * @param current - if item is in-season
     */
    public PancakeMenuItem(String title, String itemID, String description, float price, int count, boolean current) {
        super(title, itemID, description, price, count, current);
    }

    /*
     * Gets menu type
     * @return String - menu type
     */
    @Override
    public String getMenuType() {
        return "Pancake";
    }

    /*
     * Returns menu item as string to store in txt file
     * @return String - formatted menu item
     */
    @Override
    public String toDataString() {
        return getMenuType() + ";" + super.toDataString();
    }
}
