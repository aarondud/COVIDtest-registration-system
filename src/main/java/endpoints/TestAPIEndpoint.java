package endpoints;

/**
 * Class representing the Test endpoint of the FIT3077 API, located at https://fit3077.com/test
 */
public class TestAPIEndpoint extends APIEndpoint {

    /**
     * Constructor.
     */
    public TestAPIEndpoint() {
        super("/covid-test");
    }
}
