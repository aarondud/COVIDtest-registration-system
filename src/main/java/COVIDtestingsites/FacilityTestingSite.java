package COVIDtestingsites;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class represents a facility testing site's data from FIT3077 API
 */
public class FacilityTestingSite {

    /**
     * testing site's unique ID
     */
    private String id;

    /**
     * testing site's name
     */
    private String name;

    /**
     * description of the testing site
     */
    private String description;

    /**
     * testing site's URL
     */
    private String websiteUrl;

    /**
     * testing site's phone number
     */
    private String phoneNumber;

    /**
     * testing site's address
     */
    private Address address;

    /**
     *  JSON data containing info such as type, open and close times.
     */
    private ObjectNode additionalInfo;

    /**
     * ObjectMapper used for reading and writing JSON
     */
    private ObjectMapper mapper;

    /**
     * Constructor for manual creation of FacilityTestingSite
     * @param name name of the testing facility
     * @param description description of the facility
     * @param websiteUrl URL of the facility
     * @param phoneNumber phone number of the testing facility
     * @param address address of the testing facility
     */
    public FacilityTestingSite(String name, String description, String websiteUrl, String phoneNumber, ObjectNode address) {
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.phoneNumber = phoneNumber;
        this.address = new Address(address.deepCopy()); // TODO need to change to address class
        this.mapper = new ObjectMapper();
        this.additionalInfo = this.mapper.createObjectNode();
        this.additionalInfoSetup();
        this.setWaitTime("5 minutes");
        this.setOpenHours("9:00", "17:00");
    }

    /**
     * Constructor for creation of FacilityTestingSite through JSON data
     * @param testingSiteNode ObjectNode, JSON data of a testing site.
     */
    public FacilityTestingSite(ObjectNode testingSiteNode) {
        this.id = testingSiteNode.get("id").asText();
        this.name = testingSiteNode.get("name").asText();
        this.description = testingSiteNode.get("description").asText();
        this.websiteUrl = testingSiteNode.get("websiteUrl").asText();
        this.phoneNumber = testingSiteNode.get("phoneNumber").asText();
        this.address = new Address(testingSiteNode.get("address").deepCopy());
        this.additionalInfo = testingSiteNode.get("additionalInfo").deepCopy();
        this.mapper = new ObjectMapper();
    }

    /**
     * Gets opening time of a testing facility.
     * @return LocalTime representation of opening time.
     */
    public LocalTime getOpen(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        String open = this.getAdditionalInfo().get("open").asText();
        return LocalTime.parse(open, fmt);
    }

    /**
     * Gets closing time of a testing facility.
     * @return LocalTime representation of closing time.
     */
    public LocalTime getClose(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        String close = this.getAdditionalInfo().get("close").asText();
        return LocalTime.parse(close, fmt);
    }

    /**
     * Gets the current waitingTime for a facility.
     * @return String representation of waiting time.
     */
    public String getWaitTime(){
        return this.getAdditionalInfo().get("waitTime").asText();
    }

    /**
     * Setter for waitTime field in additionalInfo.
     * @param waitTime String containing the waiting of a facility.
     */
    public void setWaitTime(String waitTime){
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("waitTime", waitTime);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Setter for canBook field in additionalInfo.
     * @param ability Boolean, true if the facility can support onsite booking, false otherwise.
     */
    public void setCanBook(Boolean ability) {
        // put method adds if not already present and updates if it is present.
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("canBook", ability);
        this.setAdditionalInfo(additionalInfo);
    }
 
    /**
     * Method to determine whether the facility can handle on site QR code generation.
     * @return Boolean, true if the facility supports onsite booking and false otherwise.
     */
    public Boolean canBook() {
        return this.getAdditionalInfo().get("canBook").booleanValue();
    }
    
    /**
     * Sets open and close hours of a FacilityTestingSite through conversion to LocalTime.
     * @param open String containing opening time of FacilityTestingSite.
     * @param close containing closing time of FacilityTestingSite.
     */
    public void setOpenHours(String open, String close) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        // using LocalTime.toString() so it can be parsed properly later.
        LocalTime openTime = LocalTime.parse(open, fmt);
        LocalTime closeTime = LocalTime.parse(close, fmt);
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("open", openTime.toString());
        additionalInfo.put("close", closeTime.toString());
        this.setAdditionalInfo(additionalInfo);    
    }

    /**
     * Setter for the driveThrough field in additionalInfo. 
     * @param val Boolean, true if the facility can handle drive-through testing, false otherwise.
     */
    public void setDriveThrough(Boolean val) {
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("driveThrough", val);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Setter for the walk-in field in additionalInfo. 
     * @param val Boolean, true if the facility can handle walk-in testing, false otherwise.
     */
    public void setWalkIn(Boolean val) {
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("walk-in", val);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Setter for the clinic field in additionalInfo. 
     * @param val Boolean, true if the facility can handle walk-in testing, false otherwise.
     */
    public void setClinic(Boolean val) {
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("clinic", val);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Setter for the gp field in additionalInfo.
     * @param val Boolean, true if the facility is a type of GP, false otherwise.
     */
    public void setGP(Boolean val) {
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("gp", val);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Setter for the hospital field in additionalInfo.
     * @param val Boolean, true if the facility is a type of hospital, false otherwise.
     */
    public void setHospital(Boolean val) {
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.put("hospital", val);
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Method to determine whether an instance of FacilityTestingType can handle drive through testing.
     * @return val Boolean, true if the facility handles drive through testing, false otherwise.
     */
    public Boolean isDriveThrough() {
        return this.getAdditionalInfo().get("driveThrough").asBoolean();
    }

    /**
     * Method to determine whether an instance of FacilityTestingType can handle walk-in testing.
     * @return val Boolean, true if the facility handles walk-in testing, false otherwise.
     */
    public Boolean isWalkIn() {
        return this.getAdditionalInfo().get("walk-in").asBoolean();
    }

    /**
     * Method to determine whether an instance of FacilityTestingType is a type of clinic.
     * @return val Boolean, true if the facility is a type of clinic, false otherwise.
     */
    public Boolean isClinic() {
        return this.getAdditionalInfo().get("clinic").asBoolean();
    }
    
    /**
     * Method to determine whether an instance of FacilityTestingType is a type of GP.
     * @return val Boolean, true if the facility is a type of GP, false otherwise.
     */
    public Boolean isGP() {
        return this.getAdditionalInfo().get("gp").asBoolean();
    }

    /**
     * Method to determine whether an instance of FacilityTestingType is a type of hospital.
     * @return val Boolean, true if the facility is a type of hospital, false otherwise.
     */
    public Boolean isHospital() {
        return this.getAdditionalInfo().get("hospital").asBoolean();
    }

    /**
     * Method to add all the required fields in additionalInfo when creating an instance of FacilityTestingSite, initially null
     */
    public void additionalInfoSetup(){
        ObjectNode additionalInfo = this.getAdditionalInfo();
        additionalInfo.putNull("waitTime");
        additionalInfo.putNull("open");
        additionalInfo.putNull("close");
        additionalInfo.putNull("hospital");
        additionalInfo.putNull("clinic");
        additionalInfo.putNull("gp");
        additionalInfo.putNull("walk-in");
        additionalInfo.putNull("driveThrough");
        additionalInfo.putNull("canBook");
        this.setAdditionalInfo(additionalInfo);
    }

    /**
     * Getter for ID
     * @return String, testing site's ID
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for name
     * @return String, testing site's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for description
     * @return String, description of testing site
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for websiteURL
     * @return String, URL of testing site's website.
     */
    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    /**
     * Getter for phone number
     * @return String, phone number of testing site
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Getter for address
     * @return Address, address of testing site.
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * Getter for additionalInfo
     * @return ObjectNode, Additional info of testing sites if needed
     */
    public ObjectNode getAdditionalInfo() {
        return this.additionalInfo.deepCopy();
    }

    /**
     * Setter for additionalInfo
     * @param additionalInfo Object Node
     */
    public void setAdditionalInfo(ObjectNode additionalInfo) {
        this.additionalInfo = additionalInfo.deepCopy();
    }

    /**
     * Setter for name
     * @param name String, testing site's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for description
     * @param description String, testing site's new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter for websiteURL
     * @param websiteUrl String, testing Site's new website URL
     */
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * Setter for phone number
     * @param phoneNumber String, new phone number of testing site
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = websiteUrl;
    }

    /**
     * Setter for address
     * @param address Address, new address of testing site.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Method that will be used to convert instances back into Json format for post and patch calls.
     * @return ObjectNode containing TestingSite information in JSON format.
     */
    public ObjectNode toJson() {
        ObjectNode root = this.mapper.createObjectNode();

        root.put("name", this.name);
        root.put("description", this.description);
        root.put("websiteUrl", this.websiteUrl);
        root.put("phoneNumber", this.phoneNumber);
        // uses set since address is already ObjectNode
        root.set("address", this.address.toJson());
        root.set("additionalInfo", this.additionalInfo);
        return root;
    }
}
