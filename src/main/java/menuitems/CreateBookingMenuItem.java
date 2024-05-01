package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import COVIDtestingsites.TestingSiteFacade;
import COVIDtests.*;
import users.User;
import users.UserFacade;
import java.time.*;
import java.util.Objects;
import views.View;

/**
 * MenuItem involving a user creating a new booking.
 */
public class CreateBookingMenuItem implements MenuItem {

    private View view;

    /**
     * Constructor.
     */
    public CreateBookingMenuItem(View view) {
        this.view = view;
    }

    /**
     * Gets the description of the menu item, to be printed out to a menu.
     *
     * @return The description of the MenuItem
     */
    @Override
    public String getMenuDescription() {
        return "Create a new booking";
    }

    /**
     * Executes the MenuItem, passing control flow to it, until it finishes execution.
     *
     * @throws Exception If an error occurs while executing the MenuItem.
     */
    @Override
    public void execute() throws Exception {

        // create booking
        Booking booking = createBooking();
        view.displayString("Booking successfully created.");

        // show user their PIN / QR Code / URL
        getBookingAccessors(booking);

        // if home booking, create test object and post to api
        if (Objects.equals(booking.getTestingSiteId(), "home")) {
            createTest(booking);
        }
    }

    /**
     * Function creates a new booking and COVID test
     *
     * @return the created booking
     */
    private Booking createBooking() {
        String customerId = null;
        String testingSiteId = null;
        String startTime = null;
        Boolean needRATKit = null;

        // get customerId
        customerId = getPatientInfo();

        // select testingSiteId
        testingSiteId = getSiteInfo();

        // select appointment time
        startTime = setBookingTime(testingSiteId);

        // if home, does patient need to pick up a RAT kit?
        if (testingSiteId.equals("home")) {
            needRATKit = needRATKit();
        }

        // create booking
        return BookingFacade.getInstance().createBooking(customerId, testingSiteId, startTime, needRATKit);
    }

    /**
     * Menu function to records patient's unique ID from user input
     * @return customerId
     */
    public String getPatientInfo() {

        String[] menuOptions = {"Select booking type", "Booking for yourself", "Booking for other"};
        Integer userSelection = view.createSubMenu(menuOptions);

        if (userSelection.equals(1)) {
            // booking for themselves
            return UserFacade.getInstance().getCurrentUser().getId();

        } else if (userSelection.equals(2)) {
            // booking for someone else
            String customerId = view.promptStringInput("customer ID");

            if (UserFacade.getInstance().checkUserExists(customerId)) {
                return customerId;
            } else {
                view.displayError();
                return getPatientInfo();
            }
        } else {
            // handle invalid cases
            view.displayError();
            return getPatientInfo();
        }
    }

    /**
     * Menu function to select a testing site location, using testingSiteId and user input
     *
     * @return testingSiteId
     */
    public String getSiteInfo() {

        String[] menuOptions = {"Select booking type", "Home test", "Facility test" };
        Integer userSelection = view.createSubMenu(menuOptions);

        // book for home or facility based on user's choice
        if (userSelection.equals(1)) {
            return "home";

        } else if (userSelection.equals(2)) {
            // case where user chooses facility booking
            String testingSiteId = view.promptStringInput("testing site ID");

            // pull testing site information from the API.
            TestingSiteFacade testingSiteFacade = TestingSiteFacade.getInstance();

            if (testingSiteFacade.checkSiteExists(testingSiteId)) {
                return testingSiteId;
            } else {
                view.displayError();
                return getSiteInfo();
            }
        } else {
            view.displayError();
            return getSiteInfo();
        }
    }

    /**
     * Menu function to select a valid appointment start time
     *
     * @param testingSiteId the testing site's id
     * @return startTime - date and time of appointment
     */
    public String setBookingTime(String testingSiteId) {

        LocalDateTime appointmentDateTime;

        // enter date time and validate correctness
        do {
            appointmentDateTime = enterDateTime();
        } while (appointmentDateTime == null);

        // check it's a future date time, facility is open, time is available
        boolean inFuture = isAppointmentInFuture(appointmentDateTime);
        boolean isOpen = isFacilityOpen(appointmentDateTime, testingSiteId);
        boolean isAvailable = isTimeAvailable(appointmentDateTime, testingSiteId);

        if (inFuture & isOpen & isAvailable) {
            return appointmentDateTime.toString();
        } else if (!inFuture) {
            view.displayString("Appointment time in past. Try again");
        } else if (!isOpen) {
            view.displayString("Facility not open. Try again");
        } else if (!isAvailable) {
            view.displayString("Time not available. Try again");
        }
        return setBookingTime(testingSiteId);
    }

    /**
     * Checks if desired appointment is in the future
     * @param startTime appointment start time
     * @return true if in the future, false otherwise
     */
    public Boolean isAppointmentInFuture(LocalDateTime startTime) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        int comparison = startTime.compareTo(currentDateTime);

        return comparison >= 0;
    }

    /**
     * Checks if selected testing facility is open at the required time
     * @param startTime appointment start time
     * @param testingSiteId testing facility's unique ID
     * @return true if open, false otherwise
     */
    public boolean isFacilityOpen(LocalDateTime startTime, String testingSiteId) {
        return true;
    }

    /**
     * Checks if appointment time is available at the facility
     * @param startTime desired starting time of appointment
     * @param testingSiteId testing facility's unique ID
     * @return true if available, false otherwise
     */
    public boolean isTimeAvailable(LocalDateTime startTime, String testingSiteId) {
        return true;
    }

    /**
     * Menu item to record if customer needs to pick up a RAT kit from a testing site for their home test
     * @return true if needed, false otherwise
     */
    public Boolean needRATKit() {

        String[] menuOptions = {"Do you need to receive a RAT kit from a testing site", "Yes", "No"};

        Integer userSelection = view.createSubMenu(menuOptions);

        // determine whether user needs a RAT based on user's choice
        if (userSelection.equals(1)) {
            return true;
        }
        else if (userSelection.equals(2)) {
            return false;
        }
        else {
            return needRATKit();
        }
    }

    /**
     * Function records user inputted date time and validates it.
     * @return LocalDateTime specifying the chosen date and time of the booking.
     */
    public LocalDateTime enterDateTime() {
        int year;
        int month;
        int day;
        int hour;
        int minute;
        LocalDateTime appointmentDateTime = null;

        while (appointmentDateTime == null) {
            try {
                year = view.promptIntegerInput("Enter year of appointment (YYYY): ");
                month = view.promptIntegerInput("Enter month of appointment (MM):  ");
                day = view.promptIntegerInput("Enter day of appointment (DD): ");
                hour = view.promptIntegerInput("Enter booking hour (24hr format): ");
                minute = view.promptIntegerInput("Enter booking minute:");

                appointmentDateTime = LocalDateTime.of(year, month, day, hour, minute);
            } catch (Exception e) {
                view.displayString("Invalid date time.");
                while (!view.promptTryAgain()) {
                    return null;
                }
            }
        }
        return appointmentDateTime;
    }

    /**
     * Function retrieves booking accessors: PIN, URL, QR Code
     * @param booking COVID booking
     */
    public void getBookingAccessors(Booking booking) {
        // if home booking - QR Code & URL
        if (Objects.equals(booking.getTestingSiteId(), null)) {
            view.displayString("A QR Code has been sent to you: \n" + booking.getAdditionalInfoField("QRCode") + "\n");
            view.displayString("Your home testing URL is: \n" + booking.getAdditionalInfoField("URL") + "\n");
        }
        // if facility booking - PIN
        else{
            view.displayString("An SMS has been sent to you: \n" + booking.getAdditionalInfoField("PIN"));
        }
    }

    /**
     * Function creates a test object and posts to the API
     * @param booking COVID booking
     */
    public void createTest(Booking booking) {

        User currentUser = UserFacade.getInstance().getCurrentUser();

        TestFacade testFacade = TestFacade.getInstance();
        testFacade.createTest(
                "RAT",
                booking.getCustomerId(),
                booking.getId(),
                currentUser.getId(),
                ""
        );

        view.displayString("Covid Test Successfully Created.");
    }
}
