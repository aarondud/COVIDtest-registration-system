package COVIDtests;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class to represent an individual PCR test's data.
 */
public class PCR extends Test {
    /**
     * Type of test.
     */
    private String type = "PCR";

    /**
     * Constructor for manual creation of local PCR test.
     * @param patientId String, ID of patient taking test.
     * @param bookingId String, ID of booking that contains the test.
     * @param healthCareWorkerId String, ID of healthCareWorker administering the test.
     * @param status TestStatus, current state of the test.
     * @param notes String, notes related to the test. 
     */
    public PCR(String patientId, String bookingId, String healthCareWorkerId, TestStatus status, String notes){
        super(patientId, bookingId, healthCareWorkerId, status, notes);
    }

    /**
     * Constructor for creation of PCR test through JSON data.
     * @param covidTestNode ObjectNode, JSON data of a test.
     */
    public PCR(ObjectNode covidTestNode) {
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
