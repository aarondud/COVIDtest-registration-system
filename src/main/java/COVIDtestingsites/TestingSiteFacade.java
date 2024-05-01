package COVIDtestingsites;

import com.fasterxml.jackson.databind.node.ObjectNode;
import endpoints.TestingSiteAPIEndpoint;
import java.util.ArrayList;

public class TestingSiteFacade {

    private static TestingSiteFacade instance;

    private static TestingSiteAPIEndpoint testingSiteEndpoint;

    private static FacilityTestingSiteCollection testingSiteCollection;

    private TestingSiteFacade() {
        testingSiteEndpoint = new TestingSiteAPIEndpoint();
        testingSiteCollection = new FacilityTestingSiteCollection();

        selfPopulateFromAPI();
    }

    public static TestingSiteFacade getInstance() {
        if (instance == null) {
            instance = new TestingSiteFacade();
        }
        return instance;
    }

    public ArrayList<FacilityTestingSite> getFacilitiesBySuburb(String suburbName) {
        return testingSiteCollection.searchSuburbName(suburbName);
    }

    public ArrayList<FacilityTestingSite> getFacilitiesByType(String type) {
        return testingSiteCollection.searchType(type);
    }

    public FacilityTestingSite getFacilityById(String id) {
        return testingSiteCollection.searchId(id);
    }

    private void selfPopulateFromAPI() {
        try {
            // pull all testing sites from the API
            ObjectNode[] testingSiteNodes = testingSiteEndpoint.makeGETRequest();

            // for each testing site, add it to the testingSiteCollection
            for (ObjectNode testingSiteNode: testingSiteNodes) {
                testingSiteCollection.addTestingSite(new FacilityTestingSite(testingSiteNode));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling
        }
    }

    public boolean checkSiteExists(String testingSiteId) {
        return testingSiteCollection.checkSiteExists(testingSiteId);
    }
}
