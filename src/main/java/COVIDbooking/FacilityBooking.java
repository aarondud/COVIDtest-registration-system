package COVIDbooking;

import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.RandomString;

/**
 * Concrete product class facility booking holds COVID test information
 */
public class FacilityBooking extends Booking {

    /**
     * Constructor for facility booking through user input
     * @param customerId user's unique id string
     * @param startTime start time of appointment string (local date time format)
     * @param testingSiteId testing site location's unique ID
     */
    public FacilityBooking(String customerId, String startTime, String testingSiteId) {
        super(customerId, startTime);
        this.testingSiteId = testingSiteId;
        setPIN();
        initiateVenueVersions();
        initiateTimeVersions();
    }

    /**
     * Constructor for booking creation through JSON data
     * @param bookingNode ObjectNode, JSON data of a booking
     */
    public FacilityBooking(ObjectNode bookingNode) {
        super(bookingNode);
    }

    /**
     * Function generates a PIN associated with the booking
     * @return PIN
     */
    public String generatePIN() {
        return RandomString.main(4);
    }

    /**
     * Function sets booking's PIN in additionalInfo
     */
    public void setPIN() {
        String pin = this.generatePIN();
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("PIN", pin);
        this.setAdditionalInfo(additionalInfo);
    }

    public void initiateVenueVersions() {
        Integer noVersions = 3;

        // set current as version 1
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("venueVersion1", getTestingSiteId());

        // set remaining versions as null
        for (Integer i=2; i <= noVersions; i ++) {
            additionalInfo.put("venueVersion" + i.toString(), "null");
        }
        this.setAdditionalInfo(additionalInfo);
    }

    public void initiateTimeVersions() {
        Integer noVersions = 3;

        // set current as version 1
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("timeVersion1", getStartTime());

        // set remaining versions as null
        for (Integer i=2; i <= noVersions; i ++) {
            additionalInfo.put("timeVersion" + i.toString(), "null");
        }
        this.setAdditionalInfo(additionalInfo);
    }
}

