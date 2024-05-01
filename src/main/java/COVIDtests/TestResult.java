package COVIDtests;

/**
 * Enum to represent all possible values a test result can take.
 */
public enum TestResult {
    /**
     * Test returned positive test result.
     */
    POSITIVE,
    /**
     * Test returned negative. Patient doesn't have COVID.
     */
    NEGATIVE,
    /**
     * Test has not been performed adequately.
     */
    INVALID,
    /**
     * Test has simply been made for the patient and not yet performed.
     */
    INITIATED,
    /**
     * Test has been performed, outcome not yet produced.
     */
    PENDING
}
