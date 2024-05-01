package COVIDtestingsites;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class stores information of an address
 */
public class Address {
    /**
     * address' latitude
     */
    private Double latitude;

    /**
     * address' longitude
     */
    private Double longitude;

    /**
     * address' unit number
     */
    private String unitNumber;

    /**
     * address' street details (1)
     */
    private String street;

    /**
     * address' street details (2)
     */
    private String street2;

    /**
     * address' suburb
     */
    private String suburb;

    /**
     * address' state
     */
    private String state;

    /**
     * address' postcode
     */
    private String postcode;

    /**
     * address' additional information
     */
    private ObjectNode additionalInfo;

    /**
     * ObjectMapper used for reading and writing JSON
     */
    private ObjectMapper mapper;
    
    /**
     * Constructor for Address.
     * @param address ObjectNode containing address information in JSON format. 
     */
    public Address(ObjectNode address) {
        this.latitude = address.get("latitude").asDouble();
        this.longitude = address.get("longitude").asDouble();
        this.unitNumber = address.get("unitNumber").asText();
        this.street = address.get("street").asText();
        this.street2 = address.get("street2").asText();
        this.suburb = address.get("suburb").asText();
        this.state = address.get("state").asText();
        this.postcode = address.get("postcode").asText();
        this.additionalInfo = address.get("additionalInfo").deepCopy();
        this.mapper = new ObjectMapper();
    }
    
    /**
     * Getter for latitude field.
     * @return Double, latitude
     */
    public Double getLatitude(){
        return this.latitude;
    }

    /**
     * Getter for longitude field
     * @return Double, longitude
     */
    public Double getLongitude(){
        return this.longitude;
    }

    /**
     * Getter for unitNumber
     * @return String, unit number
     */
    public String getUnitNumber() {
        return this.unitNumber;
    }

    /**
     * Getter for street
     * @return String, street's name
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * Getter for street2
     * @return String, street2
     */
    public String getStreet2() {
        return this.street2;
    }

    /**
     * Getter for state
     * @return String, state's name
     */
    public String getState() {
        return this.state;
    }

    /**
     * Getter for postcode
     * @return String, postcode
     */
    public String getPostcode() {
        return this.postcode;
    }

    /**
     * Getter for additionalInfo
     * @return ObjectNode containing additionalInfo (if present) of the address.
     */
    public ObjectNode getAdditionalInfo() {
        return this.additionalInfo.deepCopy();
    }

    /**
     * Getter for suburb
     * @return String, suburb's name
     */
    public String getSuburb() {
        return this.suburb;
    }

    /**
     * Setter for latitude
     * @param latitude Double
     */
    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    /**
     * Setter for longitude
     * @param longitude Double
     */
    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    /**
     * Setter for unitNumber
     * @param unitNumber String
     */
    public void setUnitNumber(String unitNumber){
        this.unitNumber = unitNumber;
    }

    /**
     * setter for street
     * @param street String street's name
     */
    public void setStreet(String street){
        this.street = street;
    }

    /**
     * Setter for street2
     * @param street2 String
     */
    public void setStreet2(String street2){
        this.street2 = street2;
    }

    /**
     * Setter for suburb
     * @param suburb String, suburb's name
     */
    public void setSuburb(String suburb){
        this.suburb = suburb;
    }

    /**
     * Setter for state
     * @param state String, state's name
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * Setter for postcode
     * @param postcode String
     */
    public void setPostcode(String postcode){
        this.postcode = postcode;
    }

    /**
     * Setter for additionalInfo
     * @param additionalInfo ObjectNode containing address' additional info if needed.
     */
    public void setAdditionalInfo(ObjectNode additionalInfo) {
        this.additionalInfo = additionalInfo.deepCopy();
    }

    /**
     * Method that will be used to convert instances back into Json format.
     * @return ObjectNode containing Address information in JSON format.
     */
    public ObjectNode toJson(){
        ObjectNode root = this.mapper.createObjectNode();
        root.put("latitude", this.latitude);
        root.put("longitude", this.latitude);
        root.put("unitNumber", this.unitNumber);
        root.put("street", this.street);
        root.put("street2", this.street2);
        root.put("street2", this.street2);
        root.put("suburb", this.suburb);
        root.put("postcode", this.postcode);
        root.set("additionalInfo", this.additionalInfo);
        return root;
    }



}
