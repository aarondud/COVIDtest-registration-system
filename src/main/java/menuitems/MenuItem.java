package menuitems;

/**
 * Interface representing an item on a Menu.
 *
 * @see menus.Menu
 */
public interface MenuItem {
    /**
     * Gets the description of the menu item, to be printed out to a menu.
     *
     * @return The description of the MenuItem
     */
    String getMenuDescription();

    /**
     * Executes the MenuItem, passing control flow to it, until it finishes execution.
     *
     * @throws Exception If an error occurs while executing the MenuItem.
     */
    void execute() throws Exception;
}
