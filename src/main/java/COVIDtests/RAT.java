package COVIDtests;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class to represent an individual RAT test's data.
 */
public class RAT extends Test {
    /**
     * Type of test. 
     */
    private String type = "RAT";

    /**
     * Constructor for manual creation of local RAT test.
     * @param patientId String, ID of patient taking test.
     * @param bookingId String, ID of booking that contains the test.
     * @param healthCareWorkerId String, ID of healthCareWorker administering the test.
     * @param status TestStatus, current state of the test.
     * @param notes String, notes related to the test. 
     */
    public RAT(String patientId, String bookingId, String healthCareWorkerId, TestStatus status, String notes){
        super(patientId, bookingId, healthCareWorkerId, status, notes);
    }

    /**
     * Constructor for creation of RAT test through JSON data.
     * @param covidTestNode ObjectNode, JSON data of a test.
     */
    public RAT(ObjectNode covidTestNode) {
        super(covidTestNode);
    }
    
    /**
     * Getter for the string's type.
     * @return String
     */
    public String getType(){
        return this.type;
    }
}
