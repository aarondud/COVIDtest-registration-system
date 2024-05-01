package views;

import COVIDbooking.Booking;
import COVIDtestingsites.FacilityTestingSite;
import java.util.ArrayList;

public interface View {
    Integer createSubMenu(String[] menuItems);

    String promptStringInput(String value);

    Integer promptIntegerInput(String value);

    void displayError();

    void displayString(String message);

    Boolean promptTryAgain();

    void displayBookingDetails(Booking booking);

    void displayTestingSiteDetails(FacilityTestingSite testingSite);

    void displayListOfTestingSiteDetails(ArrayList<FacilityTestingSite> testingSites);

    String readAnswer();

    void waitForEnter(String message);
}
