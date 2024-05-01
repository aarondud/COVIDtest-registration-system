package endpoints;

/**
 * Class representing an Exception raised as the result of a bad request to the API (i.e. status code 400, 401).
 */
public class EndpointException extends Exception {

    /**
     * Constructor.
     *
     * @param message the message returned by the EndpointException.
     */
    public EndpointException(String message) {
        super(message);
    }
}
