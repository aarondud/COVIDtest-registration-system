package users;

import com.fasterxml.jackson.databind.node.ObjectNode;
import endpoints.UserAPIEndpoint;
import java.util.ArrayList;
import java.util.Objects;

public class UserFacade {

    private static UserFacade instance;

    private static UserFactory userFactory;

    private static UserAPIEndpoint userEndpoint;

    private static UserList userList;

    private UserFacade() {
        userFactory = new ConcreteUserFactory();
        userEndpoint = new UserAPIEndpoint();
        userList = new UserList();  //TODO: add constructor to userList

        selfPopulateFromAPI();
    }

    public static UserFacade getInstance() {
        if (instance == null) {
            instance = new UserFacade();
        }
        return instance;
    }

    private void selfPopulateFromAPI() {
        try {
            // retrieve all bookings from the web service
            ObjectNode[] userNodes = userEndpoint.makeGETRequest();

            // for each booking found, add it to the bookingCollection
            for (ObjectNode userNode : userNodes) {
                userList.addUser(userFactory.createUser(userNode));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    private void pushNewUserToAPI(User user) {
        try {
            userEndpoint.makePOSTRequest(user.toJson());
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    private void updateUserOnAPI(User user) {
        try {
            userEndpoint.makePATCHRequestByID(user.getId(), user.toJson());
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    private void deleteUserOnAPIById(String id) {
        try {
            userEndpoint.makeDELETERequestByID(id);
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
    }

    public User getUserByUserName(String userName) {
        return userList.getUserByUserName(userName);
    }

    public boolean checkUserExists(String id) {
        return userList.checkUserExists(id);
    }

    public void updateUser(User user) {
        updateUserOnAPI(user);
        userList.updateUser(user);
    }

    public boolean deleteUserById(String id) {
        deleteUserOnAPIById(id);
        return userList.deleteUserById(id);
    }

    public User getCurrentUser(){
        return Auth.getInstance().getCurrentUser();
    }

    public User login(String userName, String password) {

        String tempJwt;
        try {
            tempJwt = userEndpoint.login(userName, password, true);
        } catch (Exception e) {
            return null;
        }

        Auth.getInstance().setJwt(tempJwt);

        User currentUser = getUserByUserName(userName);

        if (!Objects.equals(currentUser, null)) {
            Auth.getInstance().setLoggedInUser(currentUser);
            return currentUser;
        }

        return currentUser;
    }


    /**
     * Method to get all HealthcareWorkers from API in an ArrayList.
     * @return ArrayList<HealthcareWorker>
     */
    public ArrayList<HealthcareWorker> getHealthcareWorkers() {
        ArrayList<HealthcareWorker> tempUserList =  new ArrayList<>();
        try {
            // retrieve all bookings from the web service
            ObjectNode[] userNodes = userEndpoint.makeGETRequest();

            // for each booking found, add it to the bookingCollection
            for (ObjectNode userNode : userNodes) {
                if (!userNode.get("isCustomer").asBoolean() && !userNode.get("isReceptionist").asBoolean()) {
                    userList.addUser(new HealthcareWorker(userNode));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
        return tempUserList;
    }

    /**
     * Method to get all Receptionists from API in an ArrayList.
     * @return ArrayList<Receptionist>
     */
    public ArrayList<Receptionist> getReceptionists() {
        ArrayList<Receptionist> tempUserList =  new ArrayList<>();
        try {
            // retrieve all bookings from the web service
            ObjectNode[] userNodes = userEndpoint.makeGETRequest();

            // for each booking found, add it to the bookingCollection
            for (ObjectNode userNode : userNodes) {
                if (!userNode.get("isCustomer").asBoolean() && userNode.get("isReceptionist").asBoolean()) {
                    tempUserList.add(new Receptionist(userNode));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); //FIXME: improve error handling here
        }
        return tempUserList;
    }


    /**
     * Checks whether the current user of the system is authenticated by
     * verifying the current JWT.
     *
     * @return true if the JWT is valid, false otherwise.
     */
    public boolean isAuthenticated() {
        try {
            userEndpoint.verifyToken(Auth.getInstance().getJwt());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
