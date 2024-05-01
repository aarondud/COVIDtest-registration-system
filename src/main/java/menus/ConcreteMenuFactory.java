package menus;

import views.View;

/**
 * Class which implements MenuFactory, it is responsible for the creation of specific user menus as per 
 * the factory method design pattern.
 */
public class ConcreteMenuFactory implements MenuFactory {

    /**
    Method to decide which specific menu to be created as per client's request. 
     * @param menuType String, indicating which type of menu the client wants to create.
     * @return newMenu Menu, a specific user menu.
    */
    @Override
    public Menu createMenu(String menuType, View view) {
        Menu newMenu = null;

        if (menuType.equalsIgnoreCase("RECEPTIONIST")) {
            newMenu = new ReceptionistMenu(view);
        }
        else if (menuType.equalsIgnoreCase("PATIENT")) {
            newMenu = new PatientMenu(view);
        }
        else if (menuType.equalsIgnoreCase("HEALTHCAREWORKER")) {
            newMenu = new HealthCareWorkerMenu(view);
        }

        return newMenu;
    }
}
