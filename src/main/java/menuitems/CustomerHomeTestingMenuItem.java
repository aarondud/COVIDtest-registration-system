package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import com.fasterxml.jackson.databind.JsonNode;
import endpoints.PhotoAPIEndpoint;
import utils.RandomString;
import java.util.Objects;
import com.fasterxml.jackson.databind.node.ObjectNode;
import views.View;

/**
 * MenuItem involving a Patient signing on for a home booking appointment, and entering their RAT test result and
 * photo into the system.
 */
public class CustomerHomeTestingMenuItem implements MenuItem{

    private View view;

    /**
     * Constructor.
     */
    public CustomerHomeTestingMenuItem(View view) {
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

        // get booking by URL
        Booking booking = getBookingByURL();
        String bookingId = booking.getId();
        BookingFacade bookingFacade = BookingFacade.getInstance();

        view.waitForEnter("Please join your interviewer on video call, and press enter once you have.");

        // 2. Generate a random code to verify both parties are connected. 
        String code = RandomString.main(6);
        view.displayString("Tell interviewer the following code: " + code);
            // maybe change this to a setter.
        ObjectNode additionalInfo = booking.getAdditionalInfo();
        additionalInfo.put("code", code);
        booking.setAdditionalInfo(additionalInfo);

        // patch to API
        bookingFacade.updateBooking(booking);

        view.waitForEnter("Please perform your test, and press enter once you are ready to upload your result.");

        PhotoAPIEndpoint photoEndpoint = new PhotoAPIEndpoint();

        // ask the user for the path to a file, and attempt to upload the file at that path to the web service.
        do {
            try {
                // get path information
                view.displayString("Only JPEG and PNG formats are supported.");
                String filePath = view.promptStringInput("the path to your image (i.e. 'C:/Users/john/test.jpg')");

                // create a new photo entry via the API
                ObjectNode photoInfo = photoEndpoint.makePOSTRequest("{\"additionalInfo\": {}}");
                String photoId = photoInfo.get("id").textValue();

                // attempt to upload the photo via the specified path
                photoEndpoint.uploadPhoto(photoId, filePath);

                // if there are no exceptions, then upload was successful
                view.displayString("Upload successful. Thank you.\n");
                break;
            } catch (Exception e) {
                view.displayString("An error occurred.\n" + e.getMessage());
            }
        } while (!view.promptTryAgain());

        view.waitForEnter("Please wait until your supervisor has entered comments before continuing and then press enter.");

        // ask the user if they tested positive.
        String testedPositive;
        do {
            testedPositive = view.promptStringInput("if you tested positive [y/n]");
        } while (!Objects.equals(testedPositive, "y") && !Objects.equals(testedPositive, "n"));

        // if they tested negative, patients do not get to see comments
        if (Objects.equals(testedPositive, "n")) {
            return;
        }

        // sync the booking with the api
        bookingFacade.syncBookingWithAPIById(bookingId);

        // if they did test positive, they get to see comments
        view.displayString("Supervisor comments:");

        Booking updatedBooking = bookingFacade.getBookingById(bookingId);

        JsonNode commentNode = updatedBooking.getAdditionalInfo().get("comments");

        for (int i=0; i<commentNode.size(); i++) {
            String comment = commentNode.get(i).textValue();
            view.displayString(comment);
        }

        view.displayString("Please isolate and perform tests every alternate day until the test is negative.");
    }

    /**
     * Function returns home booking given a URL
     * @return home booking if URL valid, null otherwise
     */
    public Booking getBookingByURL() {
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
