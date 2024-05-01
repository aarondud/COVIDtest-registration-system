package COVIDbooking;

import COVIDtests.TestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Abstract class holds COVID booking details
 */
public abstract class Booking {

    /**
     * booking's unique ID string
     */
    private String id;
    /**
     * patient registered for COVID test
     */
    private String customerId;
    /**
     * ID of testing site hosting the appointment
     */
    protected String testingSiteId;
    /**
     * starting date time of the booking
     */
    private String startTime;

    /**
     * notes for the booking
     */
    private String notes;
    /**
     * timestamp of booking update
     */
    private String updatedAt;
    /**
     * booking's additional information
     */
    private ObjectNode additionalInfo;
    /**
     * ObjectMapper used for reading and writing JSON
     */
    private ObjectMapper mapper;

    private String status;

    /**
     * Constructor for booking creation through JSON data
     * @param bookingNode ObjectNode, JSON data of a booking
     */
    public Booking(ObjectNode bookingNode) {
        this.id = bookingNode.get("id").asText();
        this.customerId = bookingNode.get("customer").get("id").asText();

        if (bookingNode.get("testingSite") != null && !bookingNode.get("testingSite").isNull()) {
            this.testingSiteId = bookingNode.get("testingSite").get("id").asText();
        } else {
            this.testingSiteId = null;
        }
        this.startTime = bookingNode.get("startTime").asText();
        this.notes = bookingNode.get("notes").asText();
        this.additionalInfo = bookingNode.get("additionalInfo").deepCopy();
        this.updatedAt = bookingNode.get("updatedAt").asText();
        this.mapper = new ObjectMapper();
    }

    /**
     * Constructor for booking creation through user input
     * @param customerId user's unique id string
     * @param startTime  start time of appointment string (date time format)
     */
    public Booking(String customerId, String startTime) {
        this.customerId = customerId;
        this.startTime = startTime;
        this.mapper = new ObjectMapper();
        this.updatedAt = startTime;
        this.additionalInfo = this.mapper.createObjectNode();
        this.status = TestStatus.INITIATED.toString();
    }

    /**
     * Returns booking's unique id
     * @return id booking's unique id string
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns booking's customer id
     * @return customer id
     */
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * Returns booking's testing side id
     * @return testingSiteId
     */
    public String getTestingSiteId() {
        return this.testingSiteId;
    }

    /**
     * Returns booking's appointment time
     * @return startTime appointment starting time (date time format)
     */
    public String getStartTime() {
        return this.startTime;
    }

    /**
     * Returns booking's notes
     * @return booking's notes
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Returns booking's additional information
     *
     * @return additionalInfo booking's additional information
     */
    public ObjectNode getAdditionalInfo() {
        return this.additionalInfo;
    }

    /**
     * Sets notes for a booking
     * @param notes booking's notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Converts JSON data of a booking to a string
     * @return booking's JSON data represented as a string
     */
    public String toJSON() {
        ObjectNode node = this.mapper.createObjectNode();
        node.put("customerId", getCustomerId());
        node.put("testingSiteId", getTestingSiteId());
        node.put("startTime", getStartTime());
        node.put("notes", getNotes());
        node.put("updatedAt", getUpdatedAT());
        node.set("additionalInfo", this.additionalInfo.deepCopy() );
        return node.toString();
    }

    /**
     * Function sets booking's additional information
     * @param additionalInfo booking's additional information
     */
    public void setAdditionalInfo(ObjectNode additionalInfo) {
        this.additionalInfo = additionalInfo.deepCopy();
    }

    /**
     * Function returns a field value from Booking's additional info
     * @param field attribute in additional info
     * @return value of selected field, null if n/a
     */
    public String getAdditionalInfoField(String field) {
        try {
            this.getAdditionalInfo().get(field).asText();
        }
        catch (Exception e) {
           return null;
        }
        return this.getAdditionalInfo().get(field).asText();
    }

    /**
     * Function sets a bookings updated timestamp
     * @param updatedTimestamp timestamp of booking updated
     */
    public void setUpdatedAt(String updatedTimestamp) {
        this.updatedAt = updatedTimestamp;
    }

    /**
     * Function gets booking's update timestamp
     * @return booking's update timestamp
     */
    public String getUpdatedAT(){
        return this.updatedAt;
    }

    /**
     * Functions sets bookings testing site
     * @param testingSiteId covid testing facilities ID
     */
    public void setTestingSite(String testingSiteId) {
        this.testingSiteId = testingSiteId;
    }

    /**
     * Function sets bookings start time
     * @param startTime starting date and time of appointment
     */
    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public void setAdditionalInfoField(String field, String value) {
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put(field, value);
        //TODO will put override here ^^
        this.setAdditionalInfo(additionalInfo);
    }
}
