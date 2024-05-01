package menus;

import menuitems.EnterInterviewDetailsMenuItem;
import menuitems.HomeTestingMenuItem;
import menuitems.ProvideRATKitMenuItem;
import views.View;

/**
 * Class that contains all the menu items to represent the relevant actions a Healthcare worker can perform.
 */
public class HealthCareWorkerMenu extends Menu {
    /**
     * Constructor to create a menu for a health care worker.
     * Menu will contain actions for on-site testing interviews, providing RAT kits, and conducting a home test for a patient.
     */
    public HealthCareWorkerMenu(View view) {
        super(view);

        this.menuItems.add(new EnterInterviewDetailsMenuItem(view));
        this.menuItems.add(new ProvideRATKitMenuItem(view));
        this.menuItems.add(new HomeTestingMenuItem(view));
    }

}
