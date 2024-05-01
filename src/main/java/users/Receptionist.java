package users;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents receptionist user type
 */
public class Receptionist extends User {

    /**
     * Constructor for manual creation of Receptionist
     * @param givenName user's given name
     * @param familyName user's family name
     * @param userName user's username
     * @param phoneNumber user's password
     * @param isCustomer is the user a customer
     * @param isAdmin is the user a receptionist
     * @param isHealthcareWorker is the user a healthcare worker
     * @param additionalInfo additional information about the user
     */
    public Receptionist(String givenName, String familyName, String userName, String phoneNumber, Boolean isCustomer, Boolean isAdmin, Boolean isHealthcareWorker, JsonNode additionalInfo) {
        super(givenName, familyName, userName, phoneNumber, isCustomer, isAdmin, isHealthcareWorker, additionalInfo);
    }

    /**
     * Constructor creates Patient through JSON data
     * @param userNode ObjectNode, JSON data of a user
     */
    public Receptionist(ObjectNode userNode) {
        super(userNode);
    }

    
    /**
     * Method to test a testingSiteId as a workplace of a receptionist.
     * @param testingSiteId is the testingSiteId of the testing site the receptionist works at.
     */
    public void setWorkplace(String testingSiteId) {
        ObjectNode additionalInfo = this.getAdditionalInfo().put("testingSiteId", testingSiteId);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Method to retrieve the testingSiteId a receptionist works at.
     * @return testingSiteId of the testing site the receptionist works at.
     */
    public String getWorkplace(){
        return this.getAdditionalInfo().get("testingSiteId").asText();
    }

    /**
     * Gets all the updates relating to bookings at the receptionist's workplace that the worker
     * has not already seen.
     * @return array of updates
     */
    public ArrayNode getMessageList() {
        return this.getAdditionalInfo().withArray("messageList").deepCopy();
    }

    /**
     * Adds an update regarding a booking at the receptionist's workplace.
     * @param message message to be shown
     */
    public void addMessage(ObjectNode message){
        ObjectNode additionalInfo = this.getAdditionalInfo();
        // messageList in additionalInfo is an ArrayNode
        ArrayNode messageList = additionalInfo.withArray("messageList");
        messageList.add(message); // as it is not a deepcopy, additionalInfo will now contain the new message
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Empties the list of updates that a receptionist has not seen regarding a booking at the receptionist's workplace.
     */
    public void emptyMessageList() {
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.putArray("messageList");
        this.setAdditionalInfo(additionalInfo);
    }
}
