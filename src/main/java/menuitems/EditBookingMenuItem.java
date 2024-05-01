package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import COVIDtestingsites.TestingSiteFacade;
import COVIDtests.TestStatus;
import users.UserFacade;
import views.View;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class EditBookingMenuItem implements MenuItem {
    private View view;

    public EditBookingMenuItem(View view) {
        this.view = view;
    }

    @Override
    public String getMenuDescription() {
        if (UserFacade.getInstance().getCurrentUser().getIsReceptionist()) {
            return "Answer the phone - edit a patient's booking details";
        }
        return "Edit booking details";
    }

    @Override
    public void execute() throws Exception {

        // select booking to edit
        Booking booking = selectBooking();

        // check booking is modifiable
        if (!isBookingModifiable(booking)) {
            return;
        }

        // print booking info
        view.displayBookingDetails(booking);

        // execute edit
        view.displayString(executeEdit(booking));
    }

    /**
     * Function selects a booking by PIN or ID
     *
     * @return booking if it exists, null otherwise
     */
    public Booking selectBooking() {

        // search for booking
        Booking booking;
        do {
            booking = searchBooking();
        } while (booking == null);

        return booking;
    }

    /**
     * Function prompts user to search Booking by PIN or ID
     *
     * @return string PIN or ID
     */
    public String pinOrId() {

        String[] menuOptions = {"Search booking by", "ID", "PIN"};

        Integer userSelection = view.createSubMenu(menuOptions);

        return menuOptions[userSelection];
    }

    /**
     * Function checks if booking is eligible for modification
     *
     * @param booking user's booking
     * @return true if user's booking and booking is scheduled in the future, false otherwise
     */
    public Boolean isBookingModifiable(Booking booking) {

        // cannot edit home tests
        if (Objects.equals(booking.getTestingSiteId(), null)) {
            view.displayString("Cannot edit home bookings.");
            return false;
        }

        // confirm booking is in the future
        if (!isBookingInFuture(booking)) {
            view.displayString("Cannot edit bookings in the past.");
            return false;
        }

        // (if on-site booking adjustment) confirm booking is for the current user
        if (!UserFacade.getInstance().getCurrentUser().getIsReceptionist()) {
            if (!booking.getCustomerId().equals(UserFacade.getInstance().getCurrentUser().getId())) {
                view.displayString("Cannot edit bookings for other users.");
                return false;
            }
        }

        // confirms booking has not been administered
        if (Objects.equals(booking.getAdditionalInfoField("Status"), TestStatus.INITIATED.toString())) {
            return true;
        } else if (Objects.equals(booking.getAdditionalInfoField("Status"), null)) {
            return true;
        } else {
            view.displayString("Cannot edit booking if test has been administered.");
            return false;
        }
    }

    /**
     * Function searches for booking based on user's selection of PIN or ID
     *
     * @return booking attached to PIN or ID, null if none
     */
    public Booking searchBooking() {

        // is user searching bookings by PIN or ID
        String searchSelection = pinOrId();

        // user enters search string
        String searchString = view.promptStringInput(searchSelection);

        if (searchSelection.equals("PIN")) {
            return BookingFacade.getInstance().getBookingByPin(searchString);
        } else if (searchSelection.equals("ID")) {
            return BookingFacade.getInstance().getBookingById(searchString);
        }

        view.displayError();
        return searchBooking();
    }

    /**
     * Function checks if booking is a future appointment
     *
     * @param booking covid booking appointment
     * @return true if booking in the future, false if in past
     */
    public Boolean isBookingInFuture(Booking booking) {

        // get appointment time, convert to LocalDateTime
        String startTime = booking.getStartTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; //"2022-12-12T12:12:00.000Z"
        LocalDateTime ldtStartTime = LocalDateTime.parse(startTime.substring(0, startTime.length() - 1), dateTimeFormatter);

        // if appointment time is in the past, return false, true otherwise
        return ldtStartTime.compareTo(LocalDateTime.now()) >= 0;
    }

    /**
     * Function records user inputted date time and validates it.
     *
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
     * Menu function to select a testing site location, using testingSiteId and user input
     *
     * @return testingSiteId
     */
    public String getSiteInfo() {

        String testingSiteId = view.promptStringInput("testing site ID");

        // pull testing site information from the API.
        TestingSiteFacade testingSiteFacade = TestingSiteFacade.getInstance();

        if (testingSiteFacade.checkSiteExists(testingSiteId)) {
            return testingSiteId;
        } else {
            view.displayError();
            return getSiteInfo();
        }
    }

    /**
     * Function prompts and executes user's edit choice
     *
     * @param booking COVID booking appointment
     */
    public String executeEdit(Booking booking) {

        // select edit option
        String[] menuOptions = {"Editing options", "Time", "Venue", "Cancel/Delete", "Restore previous version"};
        Integer userSelection = view.createSubMenu(menuOptions);

        //1 - time, 2 - venue, 3 - cancel, 4 - restore
        if (userSelection.equals(1)) {
            return editBookingTime(booking);
        } else if (userSelection.equals(2)) {
            return editBookingVenue(booking);
        } else if (userSelection.equals(3)) {
            return cancelBooking(booking);
        } else if (userSelection.equals(4)) {
            return restorePreviousBooking(booking);
        }
        view.displayError();
        return null;
    }

    /**
     * Function edits a booking's start time
     *
     * @param booking user's booking
     * @return confirmation string
     */
    public String editBookingTime(Booking booking) {

        // user enters new appointment time
        LocalDateTime newStartTime = enterDateTime();

        // check time is valid
        if (!isTimeInFuture(newStartTime)){
            view.displayError();
            return editBookingTime(booking);
        }

        // store previous version
        String versions[] = {"timeVersion1", "timeVersion2", "timeVersion3"};
        String override = versions[0];
        for (Integer i = 1; i < versions.length; i++) {
            String restoredStartTime = booking.getAdditionalInfoField(versions[i]);
            if (restoredStartTime.equals("null")) {
                override = versions[i];
                break;
            }
        }

        // set new and previous versions
        booking.setStartTime(newStartTime.toString());
        booking.setAdditionalInfoField(override, booking.getStartTime());

        //capture timestamp of edit
        String editTimestamp = updateTimestampOfEdit(booking);

        // patch
        BookingFacade.getInstance().updateBooking(booking);

        return "Booking time changed at " + editTimestamp;
    }

    /**
     * function edits a booking's venue
     *
     * @param booking user's venue
     * @return confirmation string with timestamp
     */
    public String editBookingVenue(Booking booking) {

        // user selects new venue
        String newSiteInfo;
        do {
            newSiteInfo = getSiteInfo();
        } while (newSiteInfo == null);

        // store previous version
        String versions[] = {"venueVersion1", "venueVersion2", "venueVersion3"};
        String override = versions[0];
        for (Integer i = 1; i < versions.length; i++) {
            String restoredTestingSiteId = booking.getAdditionalInfoField(versions[i]);
            if (restoredTestingSiteId.equals("null")) {
                override = versions[i];
                break;
            }
        }

        // set new and previous versions
        booking.setTestingSite(newSiteInfo);
        booking.setAdditionalInfoField(override, booking.getTestingSiteId());

        // capture timestamp of edit
        String editTimestamp = updateTimestampOfEdit(booking);

        // patch
        BookingFacade.getInstance().updateBooking(booking);

        return "Booking venue changed at " + editTimestamp;
    }

    /**
     * Function cancels a user's booking
     *
     * @param booking user's booking
     * @return confirmation string with timestamp
     */
    public String cancelBooking(Booking booking) {

        // delete booking
        BookingFacade.getInstance().deleteBooking(booking);

        return "Booking deleted at " + LocalDateTime.now().toString();
    }

    /**
     * Function restores a booking's previous details
     *
     * @param booking user's booking
     * @return confirmation string with timestamp
     */
    public String restorePreviousBooking(Booking booking) {

        String typeMenuOptions[] = {"Select restoration field", "Venue", "Start Time"};
        Integer userSelection = view.createSubMenu(typeMenuOptions);


        if (userSelection.equals(1)) {
            // venue

            String venueMenuOptions[] = {"Select version", "venueVersion1", "venueVersion2", "venueVersion3"};
            userSelection = view.createSubMenu(venueMenuOptions);

            String restoredTestingSiteId = booking.getAdditionalInfoField(venueMenuOptions[userSelection]);

            // cannot restore if there were no previous modifications
            if (restoredTestingSiteId.equals("null")) {
                return "Version does not exist.";
            }

            // save current venue under version#, then restore previous
            booking.setAdditionalInfoField(venueMenuOptions[userSelection], booking.getTestingSiteId());
            booking.setTestingSite(restoredTestingSiteId);


        } else if (userSelection.equals((2))) {
            // start time

            String timeMenuOptions[] = {"Select version", "timeVersion1", "timeVersion2", "timeVersion3"};
            userSelection = view.createSubMenu(timeMenuOptions);

            String restoredStartTime = booking.getAdditionalInfoField(timeMenuOptions[userSelection]);

            // cannot restore if there were no previous modifications
            if (restoredStartTime.equals("null")) {
                return "Version does not exist.";
            }

            // save current time under version#, then restore previous
            booking.setAdditionalInfoField(timeMenuOptions[userSelection], booking.getStartTime());
            booking.setStartTime(restoredStartTime);
        }

        //capture timestamp of edit
        String editTimestamp = updateTimestampOfEdit(booking);

        // patch
        BookingFacade.getInstance().updateBooking(booking);

        return "Previous booking details restored at " + editTimestamp;

    }

    /**
     * Functions captures current timestamp and updates booking info
     * @param booking COVID booking appointment
     * @return current timestamp
     */
    public String updateTimestampOfEdit(Booking booking) {
        String timestamp = LocalDateTime.now().toString();
        booking.setUpdatedAt(timestamp);
        return timestamp;
    }

    /**
     * Checks if desired appointment is in the future
     *
     * @param startTime appointment start time
     * @return true if in the future, false otherwise
     */
    public Boolean isTimeInFuture(LocalDateTime startTime) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        int comparison = startTime.compareTo(currentDateTime);

        return comparison >= 0;
    }
}
