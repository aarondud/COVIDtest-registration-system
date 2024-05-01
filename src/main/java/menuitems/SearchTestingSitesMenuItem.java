package menuitems;

import java.util.ArrayList;
import java.util.Objects;
import COVIDtestingsites.FacilityTestingSite;
import COVIDtestingsites.TestingSiteFacade;
import views.View;

/**
 * MenuItem involving a user searching for existing testing sites.
 */
public class SearchTestingSitesMenuItem implements MenuItem {

    /**
     * The collection of all FacilityTestingSites.
     */
    private TestingSiteFacade testingSiteFacade;

    private final View view;

    /**
     * Constructor.
     */
    public SearchTestingSitesMenuItem(View view) {
        this.view = view;
        this.testingSiteFacade = TestingSiteFacade.getInstance();
    }

    /**
     * Gets the description of the menu item, to be printed out to a menu.
     *
     * @return The description of the MenuItem
     */
    @Override
    public String getMenuDescription() {
        return "Search for testing sites";
    }

    /**
     * Executes the MenuItem, passing control flow to it, until it finishes execution.
     *
     * @throws Exception If an error occurs while executing the MenuItem.
     */
    @Override
    public void execute() throws Exception {

        // search testing site
        ArrayList<FacilityTestingSite> testingSites = searchTestingSite();

        if (!Objects.equals(testingSites, null)) {
            // print details
            printListTestingSiteInfo(testingSites);
        }
    }

    private ArrayList<FacilityTestingSite> searchTestingSite() {
        String[] menuOptions = {"Search Testing Sites by", "ID", "Suburb", "Type"};
        Integer userSelection = view.createSubMenu(menuOptions);

        if (userSelection.equals(1)) {
             return searchById();
        } else if (userSelection.equals(2)) {
             return searchBySuburb();
        } else if (userSelection.equals(3)) {
            return searchByType();
        } else {
            view.displayError();
            return searchTestingSite();
        }
    }


    /**
     * Searches for a given testing site by ID number. Prompts the user to enter an ID number, and prints
     * info on that testing site, if it exists.
     */
    private ArrayList<FacilityTestingSite> searchById() {
        String testingSiteId = view.promptStringInput("Testing Site ID");
        FacilityTestingSite testingSite = this.testingSiteFacade.getFacilityById(testingSiteId);

        if (Objects.equals(testingSite, null)) {
            view.displayString("No testing site found with that ID.");
            return null;
        }

        ArrayList<FacilityTestingSite> testingSites = new ArrayList<>();
        testingSites.add(testingSite);

        return testingSites;
    }

    /**
     * Searches for testing sites located in a given suburb. Prompts the user to enter a suburb name (case-sensitive),
     * and prints info on all testing sites in that suburb.
     */
    private ArrayList<FacilityTestingSite> searchBySuburb() {
        String suburb = view.promptStringInput("Suburb");
        ArrayList<FacilityTestingSite> testingSites = this.testingSiteFacade.getFacilitiesBySuburb(suburb);

        if (testingSites.size() == 0) {
            view.displayString("No testing sites found in that suburb.");
            return null;
        }

        return testingSites;
    }

    /**
     * Searches for testing sites of a given type. Prompts the user to choose a facility type,
     * and prints info on all testing sites with that type.
     */
    private ArrayList<FacilityTestingSite> searchByType() {
        ArrayList<FacilityTestingSite> testingSites = new ArrayList<>();

        String[] menuOptions = {"Enter type of testing site", "driveThrough", "walk-in", "gp", "clinic", "hospital"};
        Integer option = view.createSubMenu(menuOptions);

        testingSites = this.testingSiteFacade.getFacilitiesByType(menuOptions[option]);

        if (testingSites.size() == 0) {
            view.displayString("No testing sites found of that type.");
            return null;
        } else {
            return testingSites;
        }
    }

    /**
     * Prints out testing site information to the user.
     * @param testingSite the testing site to be printed.
     */
    private void printTestingSiteInfo(FacilityTestingSite testingSite) {
        view.displayTestingSiteDetails(testingSite);
    }

    private void printListTestingSiteInfo(ArrayList<FacilityTestingSite> testingSites) {
        view.displayString(testingSites.size() + " testing site(s) found: ");
        view.displayListOfTestingSiteDetails(testingSites);
    }
}
