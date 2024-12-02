Café Online Order System Using Java GUI

Application Name: The Best Café Co. – Café Menu Management System

Student Names (ID’s): Kaitlyn Chiu (016967652), Arianna Gonzalez (017157114)

Introduction:
The Café Menu Management System is a Java-based application designed to manage cafe operations
efficiently. The system provides a graphical user interface (GUI) for users to interact with the cafe's
functionalities. It allows admins to manage menu and user accounts, while customers can order and
cancel items. User and Menu data are stored in a text file “cafeData.txt” and the application also
supports different types of users, including admins and customers each with specific roles and access
levels.

Key Features:
- User Management:
  - Two kinds of users: customers and administrators
  - Admins can activate or deactivate user accounts.
- Menu Management:
  - Two kinds of menu: diner and pancake
  - Admins can add/update/remove Items from menu.
- Order and Cancel items: Customers can order and cancel items with at most 10 items per order.
- Query: The details of the selected menu items/customers can be displayed by customers/admins with double clicks.
- Search and Sort: Search and sort functionality for menu items and users.
- User Registration: Users can sign up by providing their first name, last name, email, and 
password. There is an option for users to sign up either as a Customer or an Admin.
- User Login: Registered users can log in with their UserName (firstname of the user + 
randomly generated 4-digit number) and password.
- User Persistence: User and Menu data is stored in a text file (cafeData.txt) for persistence. 
The files must be updated immediately for each of the added/deleted user accounts and
menu items.

Project Structure
Below is the recommended project structure. You can add more supportive files.
- src/: All the Java source files should be here. Below are the few main java files
  - CafeOnlineOrderSystemGUI.java: Main class to launch the GUI application.
  - MenuItem.java: Represents menu in the cafe. Below are its two subclasses.
    - PancakeMenuItem.java: Represents pancake menu in the cafe.
    - DinerMenuItem.java: Represents diner menu in the cafe.
  - MenuManager.java: Manages item-related operations.
  - UserManager.java: Manages user-related operations.
  - Admin.java: Defines the Admin user and their functionalities.
  - Customer.java: Defines the Customer and their functionalities.
  - cafe.java: Manages the café database operations. You must use singleton pattern design for the café database.
- Resources/: contains Image files (backdrop1.jpg and logo.jpg)
- bin/: Directory for compiled classes.
- README.md: This file.
- cafeData.txt: You must use this file to store/update the user and menu data.
  - User data format:
    - User Type; first name; last name; email; user name; password; is active;
    ordered item 1; ordered item 2; … at most 10 items
    - Examples:
      - Customer;John;Smith;jsmith@cafe.com;john4157;Password1!;true;
      - Admin;Albert;Tsao;chung-wen.tsao@cafe.com;albert2825;Password1!;true;2001
  - Menu Item format
    - menu type; title; item id; description, price; count; in-season
    - A menu item is available only if the remaining count &gt; 0 and in-season is true
    - Examples:
      - Diner;Pasta;1006;Classic pasta with a choice of marinara or Alfredo sauce.;10.99;3;true
      - Pancake;Regular Pancake Breakfast;2002;Regular pancakes served with a choice of 
butter, syrup, or fruit topping.;4.99;30;true

GUI Overview
The application should use Java Swing to create a user-friendly graphical interface. The main GUI consists of:
1. Welcome Screen: The entry point of the application, provides options to log in or sign up.
2. Login Screen: Allows users to log in using their username and password.
3. Signup Screen: Enables new users to register by providing their details.
4. Admin Dashboard: For Admins to log in as a customer or manage menu items and user
   accounts.
5. Customer Dashboard: For Customers to place order and cancel items, view cafe information.
6. Customer Management (by administrators only): 
   - Activate/Deactivate Accounts: Admins can enable or disable user accounts.
   - View User Details: Admins can view details of all users.
   - Search Users: Admins can search for users by matching certain fields of their
      information with regular expressions
   - Sort Users: Admins can view customers in different orders by different fields of their
      information like name or username.
   - Add/Remove/edit Users: Admins can add new users or edit or remove existing ones.
7. Menu Management (by administrators only):
   - Activate/Deactivate Items: Admins can partition menu items to two groups of
      current season and off seasons.
   - View Item Details: Admins can view details of all menu items.
   - Search Items: Admins can search for menu items by matching certain fields of their
      information with regular expressions
   - Sort Items: Admins can sort menu items in different orders by fields of menu item
      information like title, description, ItemID or price. Implementation must use
      interface Comparator.
   - Add/Remove/edit Items: Admins can add new items and edit/remove existing ones.
8. Order and cancel Items (by customers)
   - Order Items: Users can order items, with at most ten items at a time.
   - Cancel Items: Customers can remove items in their cart that were placed earlier.
   - View Item Details: Customers can view details of all the available items.
   - Search Items: Customers can search for menu items by matching certain fields of
      their information with regular expressions
   - Sort Items: Customers can sort menu items in different orders by fields of menu
      item information like title, description, ItemID or price. Implementation must use
      interface Comparator.
   - Note that admins can login in as customers to place food orders.
