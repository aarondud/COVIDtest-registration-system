package COVIDbooking;

import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.RandomString;

/**
 * Concrete product class holds home booking COVID tests details
 */
public class HomeBooking extends Booking {

    /**
     * Constructor for home booking through user input
     * @param customerId user's unique id string
     * @param startTime start time of appointment string (local date time format)
     * @param patientNeedRATKit boolean, does the user need to pick up a RAT kit from a testing site
     */
    public HomeBooking(String customerId, String startTime, Boolean patientNeedRATKit) {
        super(customerId, startTime);
        setQRCode();
        setURL();
        setPatientNeedRATKit(patientNeedRATKit);
        setPickedUpKit(false);
    }

    /**
     * Constructor for home booking through JSON data
     * @param bookingNode ObjectNode, JSON data of a booking
     */
    public HomeBooking(ObjectNode bookingNode) {
        super(bookingNode);
    }

    /**
     * Function generates QR code for this booking
     * @return QRCode for home booking
     */
    public String generateQRCode() {
        return RandomString.main(15);
    }

    /**
     * Function generates URL to host home booking
     * @return URL of home booking
     */
    public String generateURL() {
        return RandomString.main(10);
    }

    /**
     * Function sets booking's URL in additionalInfo
     */
    public void setURL() {
        String url = this.generateURL();
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("URL", url);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Function sets booking's QR code in additionalInfo
     */
    public void setQRCode() {
        String qrCode = this.generateQRCode();
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("QRCode", qrCode);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Function records if customer picked up this booking's RAT kit
     * @param pickedUpRATKit true if picked up, false otherwise
     */
    public void setPickedUpKit(Boolean pickedUpRATKit) {
        this.getAdditionalInfo().put("pickedUpRATKit", pickedUpRATKit);
    }

    /**
     * Function records if patient is in need of a RAT kit from a testing site
     * @param patientNeedRATKit true if needed, false otherwise
     */
    public void setPatientNeedRATKit(Boolean patientNeedRATKit) {
        this.getAdditionalInfo().put("patientNeedRATKit", patientNeedRATKit);
    }

    /**
     * Function records outcome of customer picking up their RAT test
     *
     * @return true if customer needs to pick up a RAT kit, false otherwise
     */
    public Boolean customerPicksUpRAT() {

        // if patient doesn't need, or kit has already picked up, return false
        if (getAdditionalInfo().get("pickedUpRATKit").asBoolean() || !getAdditionalInfo().get("patientNeedRATKit").asBoolean() ) {
            return false;
        }
        // otherwise, pick up, return true
        setPickedUpKit(true);
        setPatientNeedRATKit(false);
        return true;
    }

    /**
     * Function returns if patient needs to pick up a RAT kit from a testing site
     * @return true if patient needs a RAT kit from a testing site, false otherwise
     */
    public Boolean needToPickUpRATKit() {
        return this.getAdditionalInfo().get("patientNeedRATKit").asBoolean();
    }

    /**
     * Function returns if patient has picked up a RAT kit from a testing site
     * @return true if picked up, false otherwise
     */
    public Boolean hasPickedUpRATKit() {
        return this.getAdditionalInfo().get("pickedUpRATKit").asBoolean();
    }
}
