package COVIDtests;

import endpoints.TestAPIEndpoint;

public class TestFacade {

    private static TestFacade instance;

    private static TestFactory testFactory;

    private static TestAPIEndpoint testEndpoint;

    private TestFacade() {
        testFactory = new ConcreteTestFactory();
        testEndpoint = new TestAPIEndpoint();
    }

    public static TestFacade getInstance() {
        if (instance == null) {
            instance = new TestFacade();
        }
        return instance;
    }

    public Test createTest(String type, String patientId, String bookingId, String healthCareWorkerId, String notes) {
        Test newTest = testFactory.createTest(type, patientId, bookingId, healthCareWorkerId, TestStatus.INITIATED, notes);
        pushNewTestToAPI(newTest);
        return newTest;
    }

    private void pushNewTestToAPI(Test newTest) {
        try {
            testEndpoint.makePOSTRequest(newTest.toJsonPost());
        } catch (Exception e) {
            System.out.println(e.getMessage());  //FIXME: improve error handling
        }
    }
}
