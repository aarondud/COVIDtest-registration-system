package menus;

import views.View;

/** Interface that concrete menu factories will implement as per the factory method design pattern */
public interface MenuFactory {
    /**
     * Method to decide which specific menu to be created as per client's request. 
     * @param menuType String, indicating which type of menu the client wants to create.
     * @return Menu
     */
    Menu createMenu(String menuType, View view);
}
