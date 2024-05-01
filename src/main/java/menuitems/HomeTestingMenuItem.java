package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import views.View;
import java.util.ArrayList;
import java.util.Objects;

/**
 * MenuItem involving a healthcare worker joining a home testing video call.
 */
public class HomeTestingMenuItem implements MenuItem {

    private View view;

    /**
     * Constructor.
     */
    public HomeTestingMenuItem(View view) {
        this.view = view;
    }

    /**
     * Gets the description of the menu item, to be printed out to a menu.
     *
     * @return The description of the MenuItem
     */
    @Override
    public String getMenuDescription() {
        return "Perform home testing";
    }

    /**
     * Executes the MenuItem, passing control flow to it, until it finishes execution.
     *
     * @throws Exception If an error occurs while executing the MenuItem.
     */
    @Override
    public void execute() throws Exception {

        // 1. enter URL to start Home Testing
        Booking booking = getBookingByURL();
        String bookingId = booking.getId();
        BookingFacade bookingFacade = BookingFacade.getInstance();

        // 2. Enter customer code
        String code = null;
        do{
            // fetch the latest version of the booking from the API
            bookingFacade.syncBookingWithAPIById(bookingId);
            booking = bookingFacade.getBookingById(bookingId);
            code = view.promptStringInput("code");
        } while(!code.equals(booking.getAdditionalInfo().get("code").textValue()));

        view.displayString("Both users are online! Begin testing procedures. \n");

        // check if the expert would like to add comments

        String[] menuOptions = {"Would you like to enter comments?", "Yes", "No"};
        Integer userSelection = view.createSubMenu(menuOptions);

        // if the expert does not want to add any comments
        if (Objects.equals(userSelection, 2)) {
            view.displayString("Thank you.");
            return;
        }

        // begin the comment adding loop
        view.displayString("To exit, enter EXIT");
        ArrayList<String> comments = new ArrayList<>();

        String comment;
        while (true) {
            comment = view.promptStringInput("comment");

            if (Objects.equals(comment, "EXIT")) {
               break;
            }

            comments.add(comment);
        }

        // add these comments to the additionalInfo field of the booking
        ObjectNode additionalInfo = booking.getAdditionalInfo();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode commentsNode = mapper.createArrayNode();

        for (int i=0; i<comments.size(); i++) {
            commentsNode.add(comments.get(i));
        }

        additionalInfo.set("comments", commentsNode);
        booking.setAdditionalInfo(additionalInfo);

        // patch these comments to the booking object in the API
        BookingFacade.getInstance().updateBooking(booking);
    }

    /**
     * Function returns home booking given a URL
     * @return home booking if URL valid, null otherwise
     */
    private Booking getBookingByURL() {
        String url = view.promptStringInput("URL");

        Booking booking =  BookingFacade.getInstance().getBookingByField("URL", url);

        if (Objects.equals(booking, null)) {
            view.displayError();
            return getBookingByURL();
        } else {
            return booking;
        }
    }
}
