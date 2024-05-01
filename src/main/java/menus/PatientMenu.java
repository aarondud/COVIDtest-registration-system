package menus;

import menuitems.*;
import views.View;

/**
 * Class that contains all the menu items to represent the relevant actions a Patient/Resident can perform.
 */
public class PatientMenu extends Menu {
    /**
     * Constructor to create a menu for a patient/resident.
     * Menu will contain actions for creating bookings, checking booking status, searching testing sites and performing a home test.
     */
    public PatientMenu(View view) {
        super(view);

        this.menuItems.add(new CheckBookingStatusMenuItem(view));
        this.menuItems.add(new SearchTestingSitesMenuItem(view));
        this.menuItems.add(new CreateBookingMenuItem(view));
        this.menuItems.add(new CustomerHomeTestingMenuItem(view));
        this.menuItems.add(new ProfileMenuItem(view));
    }
}
