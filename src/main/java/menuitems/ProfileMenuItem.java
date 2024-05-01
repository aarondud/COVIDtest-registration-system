package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import users.UserFacade;
import views.View;
import java.util.ArrayList;
import java.util.Objects;

public class ProfileMenuItem implements MenuItem {
    /**
     * View used for I/O with user
     */
    private View view;

    /**
     * Constructor
     */
    public ProfileMenuItem(View view){
        this.view = view;
    }

    @Override
    public String getMenuDescription() {
        return "See profile details";
    }

    @Override
    public void execute() throws Exception {
        profileMenuOptions();
    }

    private void profileMenuOptions() throws Exception {
        String[] menuOptions = {"Select option", "Current active bookings", "Edit booking details"};
        Integer userSelection = view.createSubMenu(menuOptions);

        if (userSelection.equals(1)){
            ArrayList<Booking> activeBookings = getActiveBookings();

            if (Objects.equals(activeBookings.size(), 0)){
                view.displayString("No active bookings. ");
                return;
            }

            for (Booking activeBooking : activeBookings) {
                view.displayBookingDetails(activeBooking);
            }
        }
        else if (userSelection.equals(2)){
            EditBookingMenuItem editBookingMenuItem = new EditBookingMenuItem(view);
            editBookingMenuItem.execute();
        }
    }

    /**
     * Function retrieves a user's active bookings
     * @return list of user's active bookings
     */
    private ArrayList<Booking> getActiveBookings() {
        return BookingFacade.getInstance().getActiveBookingsByUserId(UserFacade.getInstance().getCurrentUser().getId());
    }
}







