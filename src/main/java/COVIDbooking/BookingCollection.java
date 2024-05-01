package COVIDbooking;

import COVIDtests.TestStatus;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class representing a collection of bookings.
 */
public class BookingCollection {
    /**
     * list of COVID test appointments
     */
    private ArrayList<Booking> bookings;

    public BookingCollection() {
        this.bookings = new ArrayList<>();
    }

    /**
     * Constructor BookingCollection
     * Stores lis of all booking nodes in server
     * @param bookingNodes A list of ObjectNodes each containing information on a Booking object.
     */
    public BookingCollection(ObjectNode[] bookingNodes) {
        this.bookings = new ArrayList<Booking>();

        BookingFactory bookingFactory = new ConcreteBookingFactory();

        for (ObjectNode bookingNode : bookingNodes) {
            bookings.add(bookingFactory.getBooking(bookingNode));
        }
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    /**
     * Function finds and returns a booking using a PIN code
     * @param pin booking's unique sms PIN code
     * @return return the booking associated with the PIN, null if it doesn't exist
     */
    public Booking getBookingByPin(String pin) {
        Booking foundBooking = null;

        for (Booking booking : this.bookings) {
            if (Objects.equals(booking.getAdditionalInfoField("PIN"), pin)) {
                foundBooking = booking;
            }
        }
        return foundBooking;
    }

    /**
     * Function returns a booking using a booking ID
     * @param id booking's unique ID
     * @return booking associated with ID, null if it doesn't exist
     */
    public Booking getBookingById(String id) {
        Booking foundBooking = null;

        for (Booking booking : this.bookings) {
            if (Objects.equals(booking.getId(), id)) {
                foundBooking = booking;
            }
        }
        return foundBooking;
    }

    /**
     * Function returns a user's active bookings given a user ID, where active means the test has not yet been administered
     * @param userId user's unique ID
     * @return list of user's bookings
     */
    public ArrayList<Booking> getActiveBookingsByUserId(String userId) {
        ArrayList<Booking> userBookings = new ArrayList<Booking>();

        for (Booking booking : this.bookings) {
            if (Objects.equals(booking.getCustomerId(), userId)) {
                if (Objects.equals(booking.getAdditionalInfoField("status"), TestStatus.INITIATED.toString())) {
                    userBookings.add(booking);
                }
                else if (Objects.equals(booking.getAdditionalInfoField("status"), null)) {
                    userBookings.add(booking);
                }
            }
        }

        return userBookings;
    }

    /**
     * Searches BookingCollection for a Booking that matches the given field and value pair
     * @param field field in Booking's additional info
     * @param value value of field used to identify a booking
     * @return booking that corresponds to field and value, null if it doesn't exist
     */
    public Booking getBookingByAdditionalInfoField(String field, String value) {
        Booking foundBooking = null;

        for (Booking booking : this.bookings) {
            if (booking.getAdditionalInfoField(field) != null && booking.getAdditionalInfoField(field).equalsIgnoreCase(value)) {
                foundBooking = booking;
            }
        }
        return foundBooking;
    }

    public void updateBookingById(String bookingId, Booking booking) {
        // delete the booking if it exists in the list
        deleteBookingById(bookingId);

        // add the new booking to the list
        addBooking(booking);
    }

    public boolean deleteBookingById(String id) {
        Booking oldBooking = getBookingById(id);

        return this.bookings.remove(oldBooking);
    }
}
