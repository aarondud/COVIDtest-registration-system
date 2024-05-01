import menus.ConcreteMenuFactory;
import menus.Menu;
import menus.MenuFactory;
import users.User;
import users.UserFacade;
import views.CommandLineView;
import views.View;

/**
 * Main driver class. Runs the program.
 */
public class Main {

    /**
     * Runs the covid test registration system.
     *
     * @param args Arguments to be passed to the system. None are required.
     * @throws Exception If an error occurs running the program
     */
    public static void main(String[] args) throws Exception {

        View view = new CommandLineView();

        view.displayString("Welcome to the COVID test registration system.");

        User currentUser = performLogin(view);

        // create the relevant type of menu for the current user, and pass control flow over to that menu
        MenuFactory menuFactory = new ConcreteMenuFactory();
        Menu menu = menuFactory.createMenu(currentUser.getClass().getSimpleName(), view);

        menu.runMenu();

        view.displayString("Program exited correctly. Thank you.");
    }

    private static User performLogin(View view) {

        User currentUser;

        do {
            String userName = view.promptStringInput("your username");
            String password = view.promptStringInput("your password");

            currentUser = UserFacade.getInstance().login(userName, password);

            if (currentUser == null) {
                view.displayError();
            }
        } while (currentUser == null);

        view.displayString("\nWelcome " + currentUser.getGivenName() + ".\n");

        return currentUser;
    }
}
