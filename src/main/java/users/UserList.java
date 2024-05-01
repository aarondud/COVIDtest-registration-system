package users;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class stores a list of Users
 */
public class UserList {
    /**
     * list of users
     */
    private ArrayList<User> users;

    public UserList() {
        this.users = new ArrayList<>();
    }

    /**
     * Constructor for UserList. Stores a list of users
     * @param userNodes JSON data of list of users
     */
    public UserList(ObjectNode[] userNodes) {
        this.users = new ArrayList<User>();

        UserFactory userFactory = new ConcreteUserFactory();

        for (ObjectNode userNode : userNodes) {
            users.add(userFactory.createUser(userNode));
        }
    }

    public void addUser(User newUser) {
        this.users.add(newUser);
    }

    /**
     * Searches the UserList for a given user by userName.
     * @param userName the username of the user.
     * @return The found user. If no user is found, returns null.
     */
    public User getUserByUserName(String userName) {
        User foundUser = null;

        for (User user : this.users) {
            if (Objects.equals(user.getUserName(), userName)) {
                foundUser = user;
            }
        }
        return foundUser;
    }

    /**
     * Searches the UserList for a given user by ID
     * @param id user's unique ID
     * @return true if user exists, false otherwise
     */
    public Boolean checkUserExists(String id) {

        for (User user : this.users) {
            if (Objects.equals(user.getId(), id)) {
                return true;
            }
        }
        return false;
    }

    public void updateUser(User user) {
        // delete the old user, if there is one, and then add the new user
        deleteUserById(user.getId());
        addUser(user);
    }

    public boolean deleteUserById(String id) {
        User oldUser = getUserById(id);
        return users.remove(oldUser);
    }

    private User getUserById(String id) {
        User foundUser = null;

        for (User user : this.users) {
            if (Objects.equals(user.getId(), id)) {
                foundUser = user;
            }
        }
        return foundUser;
    }
}
