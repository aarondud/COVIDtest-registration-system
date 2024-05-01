package menus;

import menuitems.MenuItem;
import views.View;
import java.util.ArrayList;

/**
 * Abstract Class which specific user menus will inherit from. Will be used to display user actions related to the user.
 */
public abstract class Menu {
    /**
     * The list of options for this menu
     */
    protected ArrayList<MenuItem> menuItems;

    /**
     * The command line that will be printed to and read from
     */
    protected View view;

    /**
     * Constructor.
     */
    public Menu(View view) {
        this.view = view;
        this.menuItems = new ArrayList<>();
    }

    /**
     * Creates an array of MenuItem's descriptions, with an exit option as the final element
     */
    protected String[] createMenuPrompts() {
        String[] menuPrompts = new String[menuItems.size() + 2];

        // set the title of the menu to be "Main Menu"
        menuPrompts[0] = "Main Menu";

        // print out the description of each of this menu's options
        for (int i = 0; i < menuItems.size(); i++) {
            menuPrompts[i+1] = menuItems.get(i).getMenuDescription();
        }

        // the exit option is the final option
        menuPrompts[menuItems.size() + 1] = "exit/go back";

        return menuPrompts;
    }

    /**
     * Runs the menu. Prints all menu options, and then executes user selection
     * repeatedly until the exit option is chosen.
     * @throws Exception if an error occurs while running the menu
     */
    public void runMenu() throws Exception {
        // loop until the menu has been exited
        while (true) {

            // print all available options to the menu
            String[] menuPrompts = createMenuPrompts();

            // read the choice from the user
            int choice = view.createSubMenu(menuPrompts) - 1;

            // if the 'exit' option is chosen, which is always the last in the menu
            if (choice == menuItems.size()) {
                break;
            }

            runMenuItem(choice);
        }
    }

    // allows for additional actions to be taken after running a given menu item
    protected void runMenuItem(int choice) throws Exception {
        // execute the chosen option
        menuItems.get(choice).execute();
    }
}
