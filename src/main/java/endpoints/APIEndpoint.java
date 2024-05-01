package endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Abstract class representing an endpoint on the FIT3077 API (https://fit3077.com)
 */
public abstract class APIEndpoint {

    /**
     * Private API key for the API. Required to authorize any requests.
     */
    protected static final String myApiKey = System.getenv().get("API_KEY");

    /**
     * The root URL for the FIT3077 API.
     */
    private static final String rootUrl = "https://fit3077.com/api/v2";

    /**
     * The full URL for a given specialised endpoint (i.e. user, booking). Comprised of the root URL with a suffix.
     */
    protected String fullUrl;

    /**
     * The HttpClient used for making HTTP requests to the API.
     */
    private HttpClient client;

    /**
     * Constructor.
     *
     * @param relativePath The path of the endpoint relative to the root URL. i.e. '/user' or '/booking'.
     */
    public APIEndpoint(String relativePath) {
        this.fullUrl = rootUrl + relativePath;
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Makes a GET request to the API at this endpoint.
     *
     * @param params Parameters to be passed in the GET request. If none, are required, an empty ArrayList should be passed.
     * @return A list of ObjectNodes, one for each object returned by the API.
     * @throws Exception If an error occurs while performing the GET request.
     */
    public ObjectNode[] makeGETRequest(ArrayList<String> params) throws Exception {
        String urlWithParams = fullUrl + createParamString(params);

        HttpRequest request = HttpRequest
                .newBuilder(URI.create(urlWithParams))
                .setHeader("Authorization", myApiKey)
                .GET()
                .build();

        return makeRequest(request);
    }

    public ObjectNode[] makeGETRequest() throws Exception {
        return makeGETRequest(new ArrayList<>());
    }

    /**
     * Makes a POST request to the API at this endpoint.
     *
     * @param jsonString JSON payload to be passed in the POST API request.
     * @return ObjectNode representing the generated object as a result of the API request.
     * @throws Exception If an error occurs when making the POST request.
     */
    public ObjectNode makePOSTRequest(String jsonString) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(fullUrl))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        return makeRequest(request)[0];
    }

    /**
     * Makes a GET request to the API at this endpoint, specifying the ID.
     *
     * @param id The ID of the object to be found at this endpoint.
     * @param params Parameters to be passed in the GET request. If none, are required, an empty ArrayList should be passed.
     * @return ObjectNode representing the object found by the API request.
     * @throws Exception If an error occurs while performing the GET request.
     */
    public ObjectNode makeGETRequestByID(String id, ArrayList<String> params) throws Exception {
        String urlWithID = fullUrl + "/" + id;
        String urlWithParams = urlWithID + createParamString(params);

        HttpRequest request = HttpRequest
                .newBuilder(URI.create(urlWithParams))
                .setHeader("Authorization", myApiKey)
                .GET()
                .build();

        return makeRequest(request)[0];
    }

    public ObjectNode makeGETRequestByID(String id) throws Exception {
        return makeGETRequestByID(id, new ArrayList<>());
    }

    /**
     * Makes a PATCH request to the API at this endpoint, specifying the ID.
     *
     * @param id The ID of the object to be modified at this endpoint.
     * @param jsonString JSON payload to be passed in the PATCH API request.
     * @return ObjectNode representing the modified object as a result of the API request.
     * @throws Exception If an error occurs while performing the PATCH request.
     */
    public ObjectNode makePATCHRequestByID(String id, String jsonString) throws Exception {
        String urlWithID = fullUrl + "/" + id;

        HttpRequest request = HttpRequest.newBuilder(URI.create(urlWithID))
                .setHeader("Authorization", myApiKey)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
                .header("Content-Type", "application/json")
                .build();

        return makeRequest(request)[0];
    }

    /**
     * Makes a DELETE request to the API at this endpoint, specifying the ID.
     *
     * @param id The ID of the object to be deleted at this endpoint.
     * @throws Exception If an error occurs while performing the DELETE request.
     */
    public void makeDELETERequestByID(String id) throws Exception {
        String urlWithID = fullUrl + "/" + id;

        HttpRequest request = HttpRequest
                .newBuilder(URI.create(urlWithID))
                .setHeader("Authorization", myApiKey)
                .DELETE()
                .build();

        makeRequest(request);
    }

    /**
     * Executes a given HttpRequest, and returns any objects send back from the API.
     *
     * @param request The HttpRequest to be executed.
     * @return A list of ObjectNodes representing the objects returned from the API.
     * @throws Exception If an error occurs while making the API request.
     */
    protected ObjectNode[] makeRequest(HttpRequest request) throws Exception {

        this.client = HttpClient.newHttpClient();

        // send the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // parse the response into ObjectNodes, and return a list of them
        ObjectNode[] jsonNodes = new ObjectNode[1];

        int statusCode = response.statusCode();

        // if the request was successful but nothing was returned
        if ((statusCode == 200 || statusCode == 201) && Objects.equals(response.body(), "")) {
            return null;
        }

        try {
            // handle case where multiple objects are returned
            jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        } catch (MismatchedInputException e) {
            // handle case where only one object is returned
            jsonNodes[0] = new ObjectMapper().readValue(response.body(), ObjectNode.class);
        }

        // if an invalid request was made, throw an exception
        if (statusCode != 200 && statusCode != 201) {
            try {
                throw new EndpointException(jsonNodes[0].get("message").get(0).textValue());
            }
            catch (Exception e) {
                throw new EndpointException(jsonNodes[0].get("message").textValue());
            }
        }

        return jsonNodes;
    }

    /**
     * Creates a parameter string for an API request.
     * Returns an empty string if no parameters are provided.
     *
     * @param params A list of all parameters to be converted into a string
     * @return The formatted string to be appended to an HTTP request url
     */
    protected String createParamString(ArrayList<String> params) {
        if (params.size() == 0) {
            return "";
        }

        StringBuilder paramString = new StringBuilder("?");

        // loop through parameter list and add each to the string
        for (int i = 0; i < params.size(); i++) {
            paramString.append("fields=").append(params.get(i));

            // avoid a final '&' at the end of the string
            if (i != params.size() - 1) {
                paramString.append("&");
            }
        }

        return paramString.toString();
    }
}
