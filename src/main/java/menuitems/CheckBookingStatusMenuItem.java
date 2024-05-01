package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import views.View;
import java.util.Objects;

/**
 * MenuItem involving a user checking the status of an existing booking.
 */
public class CheckBookingStatusMenuItem implements MenuItem {

    private View view;

    /**
     * Constructor.
     */
    public CheckBookingStatusMenuItem(View view) {
        this.view = view;
    }

    /**
     * Gets the description of the menu item, to be printed out to a menu.
     * @return The description of the MenuItem
     */
    @Override
    public String getMenuDescription() {
        return "Check the status of a booking";
    }

    /**
     * Executes the MenuItem, passing control flow to it, until it finishes execution.
     *
     * @throws Exception If an error occurs while executing the MenuItem.
     */
    @Override
    public void execute() throws Exception {

        // get booking
        Booking booking = getBookingByPIN();

        // show booking status
        view.displayBookingDetails(booking);
    }

    /**
     * Function returns facility booking given a sms PIN
     * @return facility booking if valid PIN, null otherwise
     */
    public Booking getBookingByPIN() {

        String bookingPin = view.promptStringInput("PIN");

        Booking booking = BookingFacade.getInstance().getBookingByPin(bookingPin);

        if (Objects.equals(booking, null)) {
            view.displayError();
            return getBookingByPIN();
        }
        else {
            return booking;
        }
    }
}
