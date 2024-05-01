package endpoints;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URI;
import java.net.http.HttpRequest;

/**
 * Class representing the User endpoint of the FIT3077 API, located at https://fit3077.com/user
 */
public class UserAPIEndpoint extends APIEndpoint {

    /**
     * The URL of the login endpoint.
     */
    private String loginUrl;

    /**
     * The URL of the token verification endpoint.
     */
    private String verifyTokenUrl;

    /**
     * Constructor.
     */
    public UserAPIEndpoint() {
        super("/user");

        this.loginUrl = fullUrl + "/login";
        this.verifyTokenUrl = fullUrl + "/verify-token";
    }

    /**
     * Makes a PUT request to the API at this endpoint, specifying the user's ID.
     *
     * @param id The user's ID.
     * @param jsonString JSON payload to be passed in the PUT API request.
     * @return ObjectNode representing the modified object as a result of the API request.
     * @throws Exception If an error occurs while performing the PUT request.
     */
    public ObjectNode makePUTRequestByID(String id, String jsonString) throws Exception {
        String urlWithID = fullUrl + "/" + id;

        HttpRequest request = HttpRequest.newBuilder(URI.create(urlWithID))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        return makeRequest(request)[0];
    }

    /**
     * Validates a user's username and password.
     *
     * @param userName The user's username
     * @param password The user's password
     * @param withJwt Whether a JWT should be requested from the login.
     * @return The JWT, if it was requested. Returns null if it was not requested.
     * @throws Exception If an error occurs while making the login request, including if the user credentials are invalid.
     */
    public String login(String userName, String password, boolean withJwt) throws Exception {

        // format the login JSON payload, add parameters
        String jsonString = "{" +
                "\"userName\":\"" + userName + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";

        String urlWithParams = loginUrl + "?jwt=" + withJwt;

        // execute the request
        HttpRequest request = HttpRequest.newBuilder(URI.create(urlWithParams))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        ObjectNode response = makeRequest(request)[0];

        String jwt = null;

        if (withJwt) {
           jwt = response.get("jwt").textValue();
        }

        return jwt;
    }

    /**
     * Verifies that a given JWT is valid by checking it with the API.
     *
     * @param jwt The JWT to be checked.
     * @throws Exception If an error occurs while making the token verification request.
     */
    public void verifyToken(String jwt) throws Exception {

        String jsonString = "{" +
                "\"jwt\":\"" + jwt + "\"" +
                "}";

        HttpRequest request = HttpRequest.newBuilder(URI.create(verifyTokenUrl))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        makeRequest(request);
    }
}
