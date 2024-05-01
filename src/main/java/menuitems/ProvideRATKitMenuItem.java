package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import COVIDbooking.HomeBooking;
import views.View;
import java.util.Objects;

/**
 * MenuItem involving a receptionist providing a RAT kit to a user and logging it in the system.
 */
public class ProvideRATKitMenuItem implements MenuItem {

    private final View view;
    /**
     * Constructor.
     */
    public ProvideRATKitMenuItem(View view) {
        this.view = view;
    }

    /**
     * Gets the description of the menu item, to be printed out to a menu.
     *
     * @return The description of the MenuItem
     */
    @Override
    public String getMenuDescription() {
        return "Provide RAT kit for home testing";
    }

    /**
     * Executes the MenuItem, passing control flow to it, until it finishes execution.
     *
     * @throws Exception If an error occurs while executing the MenuItem.
     */
    @Override
    public void execute() throws Exception {

        // get home booking using QR code
        Booking booking = getHomeBooking();

        // give RAT kit
        provideRATKit((HomeBooking) booking);
    }

    /**
     * Function returns home booking given a qrCode
     * @return home booking if qrCode valid, null otherwise
     */
    public Booking getHomeBooking () {

        String qrCode = view.promptStringInput("QR code");
        Booking booking = BookingFacade.getInstance().getBookingByField("QRCode", qrCode);

        if (Objects.equals(booking, null)) {
            view.displayError();
            return getHomeBooking();
        } else {
            return booking;
        }
    }

    /**
     * Function provides customer with a home testing RAT Kit. Records in booking it's been collected
     * @param booking home booking RAT kit is required for
     */
    public void provideRATKit(HomeBooking booking) {
        if (booking.customerPicksUpRAT()) {
            view.displayString("RAT kit successfully provided to customer.");
        }
        else {
            view.displayString("RAT kit unsuccessfully provided. Customer has either already picked up, or elected to not pick up. ");
        }
    }
}
