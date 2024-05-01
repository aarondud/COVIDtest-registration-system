package users;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents Healthcare worker user type
 */
public class HealthcareWorker extends User {

    /**
     * Constructor for manual creation of HealthcareWorker
     * @param givenName user's given name
     * @param familyName user's family name
     * @param userName user's username
     * @param phoneNumber user's password
     * @param isCustomer is the user a customer
     * @param isAdmin is the user a receptionist
     * @param isHealthcareWorker is the user a healthcare worker
     * @param additionalInfo additional information about the user
     */
    public HealthcareWorker(String givenName, String familyName, String userName, String phoneNumber, Boolean isCustomer, Boolean isAdmin, Boolean isHealthcareWorker, JsonNode additionalInfo) {
        super(givenName, familyName, userName, phoneNumber, isCustomer, isAdmin, isHealthcareWorker, additionalInfo);
    }

    /**
     * Constructor creates HealthCareWorker through JSON data
     * @param userNode ObjectNode, JSON data of a user
     */
    public HealthcareWorker(ObjectNode userNode) {
        super(userNode);
    }
}
