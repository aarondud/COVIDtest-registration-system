package COVIDbooking;

import com.fasterxml.jackson.databind.node.ObjectNode;
import endpoints.BookingAPIEndpoint;
import java.util.ArrayList;

/**
 * Facade class for the bookings package. Handles all interactions with Bookings.
 */
public class BookingFacade {

    /**
     * Singleton instance of this class.
     */
    private static BookingFacade instance;

    /**
     * The BookingFactory used to construct bookings.
     */
    private static BookingFactory bookingFactory;

    /**
     * The booking API endpoint used for communicating with the web service.
     */
    private static BookingAPIEndpoint bookingEndpoint;

    /**
     * The BookingCollection used for storing all Bookings.
     */
    private static BookingCollection bookingCollection;

    /**
     * Private constructor.
     */
    private BookingFacade() {
        bookingFactory = new ConcreteBookingFactory();
        bookingEndpoint = new BookingAPIEndpoint();
        bookingCollection = new BookingCollection();

        selfPopulateFromAPI();
    }

    /**
     * Singleton instance method.
     *
     * @return The singleton instance of BookingFacade.
     */
    public static BookingFacade getInstance() {
        if (instance == null) {
            instance = new BookingFacade();
        }
        return instance;
    }

    /**
     * Creates a new booking, and adds it to the system. Also pushes it to the API.
     *
     * @param customerId The customer's unique id
     * @param testingSiteId The testing site's unique id
     * @param startTime The starting time of the booking
     * @param needRATKit Whether the user needs a RAT kit
     * @return The created Booking object.
     */
    public Booking createBooking(String customerId, String testingSiteId, String startTime, Boolean needRATKit) {
        Booking newBooking = bookingFactory.createBooking(customerId, testingSiteId, startTime, needRATKit);
        // notifying observers
        BookingEventManager bookingEventManager = new BookingEventManager(newBooking);
        bookingEventManager.notifyObservers("create");

        pushNewBookingToAPI(newBooking);
        bookingCollection.addBooking(newBooking);


        return newBooking;
    }

    /**
     * Pulls all bookings from the API and populates the BookingCollection.
     */
    private void selfPopulateFromAPI() {
        try {
            // retrieve all bookings from the web service
            ObjectNode[] bookingNodes = bookingEndpoint.makeGETRequest();

            // for each booking found, add it to the bookingCollection
            for (ObjectNode bookingNode : bookingNodes) {
                bookingCollection.addBooking(bookingFactory.getBooking(bookingNode));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    /**
     * Pushes a new booking to the API via POST.
     *
     * @param booking The booking to be added to the API.
     */
    private void pushNewBookingToAPI(Booking booking) {
        try {
            bookingEndpoint.makePOSTRequest(booking.toJSON());
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    /**
     * Updates a booking on the API via PATCH.
     *
     * @param booking The booking to be modified.
     */
    private void updateBookingOnAPI(Booking booking) {
        try {
            bookingEndpoint.makePATCHRequestByID(booking.getId(), booking.toJSON());
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    /**
     * Searches for a Booking by ID.
     *
     * @param bookingId The desired booking's ID.
     * @return The booking if it exists, null if it does not exist.
     */
    public Booking getBookingById(String bookingId) {
        return bookingCollection.getBookingById(bookingId);
    }

    /**
     * Searches for a booking by PIN.
     *
     * @param pin The desired booking's PIN.
     * @return The booking if it exists, null if it does not exist.
     */
    public Booking getBookingByPin(String pin) {
        return bookingCollection.getBookingByPin(pin);
    }

    /**
     * Searches for a booking by a particular field/value pair.
     *
     * @param field The field to search by.
     * @param value The desired value of the field.
     * @return The booking if it exists, null if it does not exist.
     */
    public Booking getBookingByField(String field, String value) {
        return bookingCollection.getBookingByAdditionalInfoField(field, value);
    }

    /**
     * Function retrieves a user's active bookings, where active means the test has not yet been administered
     * @param userId user's unique ID
     * @return list of user's active bookings
     */
    public ArrayList<Booking> getActiveBookingsByUserId(String userId) {
        return bookingCollection.getActiveBookingsByUserId(userId);
    }

    /**
     * Updates a booking in the system and on the API.
     *
     * @param booking The booking to be updated.
     */
    public void updateBooking(Booking booking) {
        updateBookingOnAPI(booking);
        // notifying observers
        BookingEventManager bookingEventManager = new BookingEventManager(booking);
        bookingEventManager.notifyObservers("modify");

        bookingCollection.updateBookingById(booking.getId(), booking);
    }

    /**
     * Deletes a booking from the system, and on the API.
     *
     * @param booking The booking to be deleted.
     */
    public void deleteBooking(Booking booking) {
        // notifying observers
        BookingEventManager bookingEventManager = new BookingEventManager(booking);
        bookingEventManager.notifyObservers("delete");

        deleteBookingOnAPI(booking);
        bookingCollection.deleteBookingById(booking.getId());
        
    }

    /**
     * Deletes a booking on the API.
     *
     * @param booking The booking to be deleted.
     */
    private void deleteBookingOnAPI(Booking booking){
        try {
            bookingEndpoint.makeDELETERequestByID(booking.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    /**
     * Fetches a given booking from the API and overwrites the existing booking with that same id locally.
     * Manually syncs a single booking with the web service.
     *
     * @param id The booking's unique ID.
     */
    public void syncBookingWithAPIById(String id) {
        try {
            // fetch the most recent version of the booking from the API
            ObjectNode bookingNode = bookingEndpoint.makeGETRequestByID(id);
            Booking updatedBooking = bookingFactory.getBooking(bookingNode);

            // overwrite the existing booking with the new one fetched from the API
            bookingCollection.deleteBookingById(id);
            bookingCollection.addBooking(updatedBooking);
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling
        }
    }
}
