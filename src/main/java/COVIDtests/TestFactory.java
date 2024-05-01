package COVIDtests;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Interface for the factory method, will be implemented by concrete test factories.
 */
public interface TestFactory {
    /**
     * Method to be implemented by concrete factories to create a specific type of testing manually.
     * @param type the type of the test
     * @param patientId String, ID of patient taking test.
     * @param bookingId String, ID of booking that contains the test.
     * @param healthCareWorkerId String, ID of healthCareWorker administering the test.
     * @param status TestStatus, current state of the test.
     * @param notes String, notes related to the test. 
     * @return the created Test
     */
    Test createTest(String type, String patientId, String bookingId, String healthCareWorkerId, TestStatus status, String notes);

    /**
     * Method to be implemented by concrete factories to create a specific type of test using JSON data.
     * @param testNode ObjectNode, JSON data of a test.
     * @return the created Test
     */
    Test getTest(ObjectNode testNode);
}
