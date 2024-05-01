package users;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents patient/customer user type
 */
public class Patient extends User {

    /**
     * Constructor for manual creation of Patient
     * @param givenName user's given name
     * @param familyName user's family name
     * @param userName user's username
     * @param phoneNumber user's password
     * @param isCustomer is the user a customer
     * @param isAdmin is the user a receptionist
     * @param isHealthcareWorker is the user a healthcare worker
     * @param additionalInfo additional information about the user
     */
    public Patient(String givenName, String familyName, String userName, String phoneNumber, Boolean isCustomer, Boolean isAdmin, Boolean isHealthcareWorker, JsonNode additionalInfo) {
        super(givenName, familyName, userName, phoneNumber, isCustomer, isAdmin, isHealthcareWorker, additionalInfo);
    }

    /**
     * Constructor creates Patient through JSON data
     * @param userNode ObjectNode, JSON data of a user
     */
    public Patient(ObjectNode userNode) {
        super(userNode);
    }
}
