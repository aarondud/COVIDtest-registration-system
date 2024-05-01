package COVIDtests;

/**
 * Enum to represent all possible statuses a Test can take.
 */
public enum TestStatus {
    /**
     * Test has been made.
     */
    INITIATED,
    /**
     * Test has been undertaken, results yet to be published.
     */
    PROCESSED,
    /**
     * Test has been undertaken, results published.
     */
    COMPLETED
}
