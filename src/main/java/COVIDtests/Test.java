package COVIDtests;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract Class representing test data from FIT3077 API.
 */
public abstract class Test {
    /**
     * Unique ID of a test.
     */
    private String id;

    /**
     * Unique ID of a patient
     */
    private String patientId;

    /**
     * Unique ID of a healthcare worker.
     */
    private String healthCareWorkerId;

    /**
     * associated bookings unique ID
     */
    private String bookingId ;

    /**
     * Outcome of the covid test.
     */
    private TestResult result;

    /**
     * status of the COVID test
     */
    private TestStatus status;
    /**
     * Additional notes about the test.
     */
    private String notes;
    /**
     * Additional info about the test (if needed).
     */
    private JsonNode additionalInfo;
    // used for patch commands
    /**
     * Date results of the test are produced and published.
     */
    private Instant dateOfResults;
    /**
     * Date the covid test is performed.
     */
    private Instant datePerformed;
    /**
     * Mapper is required for conversion of data to ObjectNodes.
     */
    private ObjectMapper mapper;

    /**
     * Constructor for manual creation of local Test.
     * @param patientId String, ID of patient taking test.
     * @param bookingId String, ID of booking that contains the test.
     * @param healthCareWorkerId String, ID of healthCareWorker administering the test.
     * @param status TestStatus, current state of the test.
     * @param notes String, notes related to the test. 
     */
    public Test(String patientId, String bookingId,String healthCareWorkerId, TestStatus status, String notes) {
        this.patientId = patientId;
        this.bookingId = bookingId;
        this.healthCareWorkerId = healthCareWorkerId;
        this.result = TestResult.INITIATED;
        this.status = status;
        this.mapper = new ObjectMapper();
        this.additionalInfo = this.mapper.createObjectNode();
    }

    /**
     * Constructor for creation through JSON data.
     * @param covidTestNode ObjectNode, JSON data of a test.
     */
    public Test(ObjectNode covidTestNode) {
        this.id = covidTestNode.get("id").asText();
        this.patientId = covidTestNode.get("patient").get("id").asText();
        this.healthCareWorkerId = covidTestNode.get("administerer").get("id").asText();
        this.bookingId = covidTestNode.get("booking").get("id").asText();
        this.result = TestResult.valueOf(covidTestNode.get("result").asText());
        this.status = TestStatus.valueOf(covidTestNode.get("status").asText());
        this.notes = covidTestNode.get("notes").asText();
        this.additionalInfo = covidTestNode.get("additionalInfo").deepCopy();
        this.mapper = new ObjectMapper();
    }

    /**
     * Getter of ID.
     * @return String, ID of test.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for notes.
     * @return String, notes for the test.
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Getter for results.
     * @return TestResult, outcome of test.
     */
    public TestResult getResult() {
        return this.result;
    }

    /**
     * Getter for additionalInfo.
     * @return ObjectNode, contains additional information about test.
     */
    public ObjectNode getAdditionalInfo() {
        return this.additionalInfo.deepCopy();
    }

    /**
     * Setter for notes.
     * @param notes String
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Method to update test completion. 
     * @param datePerformed Instant
     */
    public void performTest(Instant datePerformed) {
        this.datePerformed = datePerformed; 
        this.status = TestStatus.COMPLETED;
    }

    /**
     * Method to upload the results of a test after completion.
     * @param result TestResult
     * @param dateOfResults Instant
     */
    public void uploadResult(TestResult result, Instant dateOfResults) {
        this.result = result;
        this.dateOfResults = dateOfResults;
    }

    /**
     * Method to create a String with JSON formatting for posting purposes. 
     * @return String
     */
    public String toJsonPost() {
        return "{" +
                "\"type\":\"" + this.getType() + "\"," +
                "\"patientId\":\"" + this.patientId + "\"," +
                "\"administererId\":\"" + this.healthCareWorkerId + "\"," +
                "\"bookingId\":\"" + this.bookingId + "\"," +
                "\"result\":\"" + this.result.toString() + "\"," +
                "\"status\":\"" + this.status.toString() + "\"," +
                "\"notes\":\"" + this.notes + "\"," +
                "\"additionalInfo\": " + additionalInfoJson() +
            "}";
    }

    /**
     * Method to turn the additionalInfo field of a test into a String with JSON formatting.
     * @return String
     */
    private String additionalInfoJson() {
        StringBuilder sb = new StringBuilder("{");

        // loop through each entry in the additionalInfo ObjectNode, and for each, add an entry
        Iterator<Map.Entry<String, JsonNode>> fields = this.additionalInfo.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> nextItem = fields.next();

            String fieldName = nextItem.getKey();
            String fieldValue = nextItem.getValue().textValue();

            String entry = "\"" + fieldName + "\":\"" + fieldValue + "\",";

            sb.append(entry);
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Method to create a String with JSON formatting for patching purposes. 
     * @return String
     */
    public String toJsonPatch() {

        // we need to add some more fields on the end, so we can use the existing POST string, and remove
        // the final '}' character, and continue adding fields.
        String jsonString = toJsonPost();
        jsonString = jsonString.substring(0, jsonString.length() - 1);

        jsonString += ",";
        jsonString += "\"datePerformed\":\"" + this.datePerformed.toString() + "\",";
        jsonString += "\"dateOfResults\":\"" + this.dateOfResults.toString() + "\",";
        jsonString += "}";

        return jsonString;
    }

    /**
     * Method that will need to be implemented by child classes to return their type.
     * @return String
     */
    abstract String getType();
}
