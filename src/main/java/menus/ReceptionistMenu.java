package menus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import menuitems.CheckBookingStatusMenuItem;
import menuitems.CreateBookingMenuItem;
import menuitems.EditBookingMenuItem;
import menuitems.ProvideRATKitMenuItem;
import users.Receptionist;
import users.UserFacade;
import views.CommandLineView;
import views.View;

/**
 * Class that contains all the menu items to represent the relevant actions a Receptionist can perform.
 */
public class ReceptionistMenu extends Menu {
    /**
     * Constructor to create a menu for a receptionist.
     * Menu will contain actions for creating bookings, checking booking status and providing RAT kits.
     */
    public ReceptionistMenu(View view) {
        super(view);

        this.menuItems.add(new CreateBookingMenuItem(view));
        this.menuItems.add(new CheckBookingStatusMenuItem(view));
        this.menuItems.add(new ProvideRATKitMenuItem(view));
        this.menuItems.add(new EditBookingMenuItem(view));
    }

    
    @Override
    public void runMenu() throws Exception {
        

        // loop until the menu has been exited
        while (true) {
            //display user messages if needed
            displayMessages();

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

    private void displayMessages() {
        Receptionist currentAdmin = (Receptionist) UserFacade.getInstance().getUserByUserName(UserFacade.getInstance().getCurrentUser().getUserName());
        ArrayNode messageList = currentAdmin.getMessageList();
        if (messageList.size() > 0) {
            String customerId;
            String action;
            View view = new CommandLineView();
            for (JsonNode message: messageList){
                customerId = message.get("customerId").asText();
                action = message.get("type").asText();
                view.displayString("Booking for customer with ID " + customerId + " has been " + action + " at your testing site.");
            }

            currentAdmin.emptyMessageList();
            UserFacade.getInstance().updateUser(currentAdmin);
        }


    }
}
