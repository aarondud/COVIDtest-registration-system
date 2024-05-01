package COVIDbooking;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Concrete factory class for the creation of COVID test appointments
 */
public class ConcreteBookingFactory implements BookingFactory {

    /**
     * Constructor for creating of a booking through user input
     * @param customerId customer's unique id
     * @param testingSiteId testing site's unique id
     * @param startTime starting time of appointment
     * @param patientNeedRATKit does the patient need to pick up a RAT kit from a testing site
     */
    @Override
    public Booking createBooking(String customerId, String testingSiteId, String startTime, Boolean patientNeedRATKit) {
        Booking newBooking;

        if (testingSiteId.equals("home")) {
            newBooking = new HomeBooking(customerId, startTime, patientNeedRATKit);
        }
        else {
            newBooking = new FacilityBooking(customerId, startTime, testingSiteId);
        }
        return newBooking;
    }

    /**
     * Constructor for creation of booking through JSON data.
     * @param bookingNode ObjectNode, JSON data of a booking
     */
    @Override
    public Booking getBooking(ObjectNode bookingNode) {
        Booking Booking;

        if (bookingNode.get("testingSite") == null || bookingNode.get("testingSite").isNull()) {
            Booking = new HomeBooking(bookingNode);
        }
        else {
            Booking = new FacilityBooking(bookingNode);
        }
        return Booking;
    }
}
