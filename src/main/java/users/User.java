package users;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Abstract class User represents a human actor in the COVID Registration System
 */
public abstract class User {

    /**
     * user's unique ID
     */
    private String id;

    /**
     * user's given name
     */
    private String givenName;

    /**
     * user's family name
     */
    private String familyName;

    /**
     * user's username
     */
    private String userName;

    /**
     * user's password
     */
    private String password;

    /**
     * user's phone number
     */
    private String phoneNumber;

    /**
     * True if user is type customer
     */
    private Boolean isCustomer;

    /**
     * True if user is type receptionist
     */
    private Boolean isReceptionist;

    /**
     * True if user is type healthcare worker
     */
    private Boolean isHealthcareWorker;

    /**
     * additional information of user
     */
    private JsonNode additionalInfo;

    /**
     * Constructor for manual creation of new users
     * @param givenName user's given name
     * @param familyName user's family name
     * @param userName user's username
     * @param phoneNumber user's password
     * @param isCustomer is the user a customer
     * @param isReceptionist is the user a receptionist
     * @param isHealthcareWorker is the user a healthcare worker
     * @param additionalInfo additional information about the user
     */
    public User(String givenName, String familyName, String userName, String phoneNumber, Boolean isCustomer, Boolean isReceptionist, Boolean isHealthcareWorker, JsonNode additionalInfo) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.userName = userName;
        this.password = this.userName;
        this.phoneNumber = phoneNumber;
        this.isCustomer = isCustomer;
        this.isReceptionist = isReceptionist;
        this.isHealthcareWorker = isHealthcareWorker;
        this.additionalInfo = additionalInfo;
    }

    /**
     * Constructor for creation of user through JSON data
     * @param userNode ObjectNode, JSON data of a user
     */
    public User(ObjectNode userNode) {
        this.id = userNode.get("id").textValue();
        this.givenName = userNode.get("givenName").textValue();
        this.familyName = userNode.get("familyName").textValue();
        this.userName = userNode.get("userName").textValue();
        this.phoneNumber = userNode.get("phoneNumber").textValue();
        this.isCustomer = userNode.get("isCustomer").booleanValue();
        this.isReceptionist = userNode.get("isReceptionist").booleanValue();
        this.isHealthcareWorker = userNode.get("isHealthcareWorker").booleanValue();
        this.additionalInfo = userNode.get("additionalInfo").deepCopy();
    }

    /**
     * Function returns user's unique ID
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * function returns user's given name
     * @return givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * function returns user's family name
     * @return familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Function returns user's username
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Function returns user's password
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Function returns user's phone number
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Function returns if user is a customer
     * @return true if customer, false otherwise
     */
    public Boolean getIsCustomer() {
        return isCustomer;
    }

    /**
     * Function returns if user is a receptionist
     * @return true if receptionist, false otherwise
     */
    public Boolean getIsReceptionist() {
        return isReceptionist;
    }

    /**
     * Function returns if user is a healthcare worker
     * @return true if healthcare worker, false otherwise
     */
    public Boolean getIsHealthcareWorker() {
        return isHealthcareWorker;
    }

    public ObjectNode getAdditionalInfo() {
        return this.additionalInfo.deepCopy();
    }

    public void setAdditionalInfo(ObjectNode additionalInfo) {
        this.additionalInfo = additionalInfo.deepCopy();
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("givenName", this.givenName);
        root.put("familyName", this.familyName);
        root.put("password", this.userName);
        root.put("isCustomer", this.isCustomer);
        root.put("isReceptionist", this.isReceptionist);
        root.put("isHealthcareWorker", this.isHealthcareWorker);
        root.set("additionalInfo", this.additionalInfo);
        
        return root.toString();
    }
}


