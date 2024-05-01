package COVIDtestingsites;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Objects;

/**
 * Class holds list of facility testing sites
 */
public class FacilityTestingSiteCollection  {
    /**
     * list of facility testing sites
     */
    private ArrayList<FacilityTestingSite> facilities;

    public FacilityTestingSiteCollection() {
        this.facilities = new ArrayList<>();
    }
    
    /**
     * Searches FacilityTestingSiteCollection and prints facility info of facilities in the input suburb.
     * @param name name of suburb as String
     * @return the list of facilities that are located in that suburb
     */
    public ArrayList<FacilityTestingSite> searchSuburbName(String name) {
        ArrayList<FacilityTestingSite> foundSites = new ArrayList<>();

        for (FacilityTestingSite facility : facilities) {
            if (facility.getAddress().getSuburb().equals(name)) {
                foundSites.add(facility);
            }
        }
        return foundSites;
    }

    /**
     * Searches FacilityTestingSiteCollection and prints facility info of Facility with input ID.
     * @param id facility's unique ID as String
     * @return the facility site that matches the id. null if none match.
     */
    public FacilityTestingSite searchId(String id) {
        FacilityTestingSite foundSite = null;

        for (FacilityTestingSite facility : facilities) {
            if (facility.getId().equals(id)) {
                foundSite = facility;
            }
        }
        return foundSite;
    }

    /**
     * Searches FacilityTestingSiteCollection and prints Facilities that match the input type.
     * @param type type of facility as String
     * @return the list of facilities that match the type
     */
    public ArrayList<FacilityTestingSite> searchType(String type) {
        ArrayList<FacilityTestingSite> foundSites = new ArrayList<>();

        for (FacilityTestingSite facility : facilities) {
            ObjectNode additionalInfo = facility.getAdditionalInfo();
            if (additionalInfo.get(type) != null && additionalInfo.get(type).asBoolean()) {
                foundSites.add(facility);
            }
        }
        return foundSites;
    }

    /**
     * Searches FacilityTestingSiteCollection for a given site by ID
     * @param testingSiteId facility's unique ID
     * @return true if site exists, false otherwise
     */
    public Boolean checkSiteExists(String testingSiteId){

        for (FacilityTestingSite testingSite : this.facilities) {
            if (Objects.equals(testingSite.getId(), testingSiteId)) {
                return true;
            }
        }
        return false;
    }

    public void addTestingSite(FacilityTestingSite testingSite) {
        this.facilities.add(testingSite);
    }
}
