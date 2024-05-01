package COVIDtests;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class to decouple and abstract creations of tests.
 */
public class ConcreteTestFactory implements TestFactory { 

    /**
     * Method to create a specific type of testing manually. 
     * @param patientId String, ID of patient taking test.
     * @param bookingId String, ID of booking that contains the test.
     * @param healthCareWorkerId String, ID of healthCareWorker administering the test.
     * @param status TestStatus, current state of the test.
     * @param notes String, notes related to the test. 
     * @return Test
     */
    @Override
    public Test createTest(String type, String patientId, String bookingId, String healthCareWorkerId, TestStatus status, String notes){
        Test newTest = null;

        if (type.equals("RAT")) {
            newTest = new RAT(patientId, bookingId, healthCareWorkerId, status, notes);
        }
        else {
            newTest = new PCR(patientId, bookingId, healthCareWorkerId, status, notes);
        }

        return newTest;
    }

    /**
     * Method to create a specific type of test using JSON data.
     * @param testNode ObjectNode, JSON data of a test.
     * @return Test
     */
    @Override
    public Test getTest(ObjectNode testNode) {
        Test newTest = null;
        if (testNode.get("type").asText().equals("RAT")) {
            newTest = new RAT(testNode);
        }
        else {
            newTest = new PCR(testNode);
        }
        return newTest;
    }
}
