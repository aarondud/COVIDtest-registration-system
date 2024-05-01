package endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * Class representing the Photo endpoint of the FIT3077 API, located at https://fit3077.com/photo
 */
public class PhotoAPIEndpoint extends APIEndpoint {

    /**
     * The URL of the content access endpoint.
     */
    private static final String contentAccessSuffix = "/content-access";

    /**
     * The URL of the content upload endpoint.
     */
    private static final String contentUploadSuffix = "/content-upload";

    /**
     * Constructor.
     */
    public PhotoAPIEndpoint() {
        super("/photo");
    }

    /**
     * Makes a GET request to the content access endpoint, specifying the photo ID.
     *
     * @param id The photo's ID.
     * @return An ObjectNode containing information on how to access the photo.
     * @throws Exception If an error occurs while performing the GET request.
     */
    public ObjectNode getContentAccessInfoByID(String id) throws Exception {
        String contentAccessUrl = fullUrl + "/" + id + contentAccessSuffix;

        HttpRequest request = HttpRequest
                .newBuilder(URI.create(contentAccessUrl))
                .setHeader("Authorization", myApiKey)
                .GET()
                .build();

        return makeRequest(request)[0];
    }

    /**
     * Makes a GET request to the content upload endpoint, specifying the photo ID.
     *
     * @param id The photo's ID.
     * @param fileName The photo file's name, i.e. 'test1.jpg'. Only JPEG and PNG formats are supported.
     * @return An ObjectNode containing information on how to upload a photo to the API.
     * @throws Exception If an error occurs while performing the GET request.
     */
    public ObjectNode getContentUploadInfoByID(String id, String fileName) throws Exception {
        String contentUploadUrl = fullUrl + "/" + id + contentUploadSuffix;

        String jsonString = "{" +
                "\"fileName\":\"" + fileName + "\"" +
                "}";

        HttpRequest request = HttpRequest.newBuilder(URI.create(contentUploadUrl))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        return makeRequest(request)[0];
    }

    /**
     * Uploads an image file (corresponding to id) specified by filePath to the API.
     *
     * This code has been adapted from https://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-post-request-using-java
     * last accessed: 29/04/2022
     *
     * @param id The photo's ID.
     * @param filePath The absolute file path on the user's computer to the image file, i.e. 'C:/Users/john/my-photo.jpg'
     * @throws Exception If an error occurs while performing the GET request.
     */
    public void uploadPhoto(String id, String filePath) throws Exception {

        // the filename is everything in the path after the last "/"
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        File chosenFile = new File(filePath);

        // get info on the upload link and required headers
        ObjectNode uploadInfo = getContentUploadInfoByID(id, fileName);

        // retrieve the url for the POST request
        String photoUploadUrl = uploadInfo.get("accessLink").textValue();

        // build the multipart request payload using
        HttpEntity payload = buildPhotoUploadPayload(chosenFile, fileName, uploadInfo);

        // using Apache's HttpClient class to make the multipart request
        // this code has been adapted from: https://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-post-request-using-java
        // last accessed: 29/04/2022

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost photoUploadRequest = new HttpPost(photoUploadUrl);
        photoUploadRequest.setHeader("Authorization", myApiKey);

        photoUploadRequest.setEntity(payload);
        CloseableHttpResponse response = httpClient.execute(photoUploadRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();

        if (statusCode != 200 && statusCode != 201) {
            throw new EndpointException("Error uploading photo.");
        }
    }

    /**
     * Creates a multipart/form-data http request payload using uploadInfo provided by the API's upload-content endpoint.
     *
     * This code has been adapted from https://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-post-request-using-java
     * last accessed: 29/04/2022
     *
     * @param file The file to be uploaded.
     * @param fileName The name of the file.
     * @param uploadInfo ObjectNode containing information required for file upload, as specified by the upload-content endpoint.
     * @return The constructed HttpEntity containing the multipart/form-data payload.
     * @throws FileNotFoundException if the specified File does not exist on the system.
     */
    private HttpEntity buildPhotoUploadPayload(File file, String fileName, ObjectNode uploadInfo) throws FileNotFoundException {
        // this code has been adapted from: https://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-post-request-using-java
        // last accessed: 29/04/2022

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        JsonNode accessInfo = uploadInfo.get("accessInfo");

        // loop through each field in uploadInfo, and add the key/value pairs to the request
        Iterator<Map.Entry<String, JsonNode>> fields = accessInfo.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> nextItem = fields.next();

            String fieldName = nextItem.getKey();
            String fieldValue = nextItem.getValue().textValue();

            builder.addTextBody(fieldName, fieldValue, ContentType.TEXT_PLAIN);
        }

        // add the file's binary content to the multipart request
        builder.addBinaryBody(
                "file",
                new FileInputStream(file),
                ContentType.APPLICATION_OCTET_STREAM,
                fileName);

        return builder.build();
    }
}
