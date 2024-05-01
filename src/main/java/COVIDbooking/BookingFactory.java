package COVIDbooking;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Factory interface for creating COVID booking types
 */
public interface BookingFactory {
    /**
     * Method to be implemented by concrete factories to create specific types of bookings manually
     * @param customerId customer's unique id
     * @param testingSiteId testing site's unique id
     * @param startTime starting time of appointment
     * @param patientNeedRATKit does the patient need to pick up a RAT kit from a testing site
     * @return Booking
     */
    Booking createBooking(String customerId, String testingSiteId, String startTime, Boolean patientNeedRATKit);

    /**
     * Method to be implemented by concrete factories to create specific types of bookings using JSON data
     * @param bookingNode ObjectNode, JSON data of a booking
     * @return Booking
     */
    Booking getBooking(ObjectNode bookingNode);
}
