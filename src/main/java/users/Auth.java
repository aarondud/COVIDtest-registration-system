package users;

/**
 * Singleton class storing the current authentication state of the application.
 */
public class Auth {

    /**
     * Current singleton instance of the Auth class.
     */
    private static Auth instance = null;

    /**
     * The currently logged-in user of the system.
     */
    private static User currentUser;

    /**
     * The stored JWT for the currently logged-in user.
     */
    private static String jwt;


    /**
     * Singleton constructor Auth
     */
    private Auth() {
        currentUser = null;
        jwt = null;
    }

    /**
     * Static method controls access to the singleton instance Auth
     * @return an instance of Auth.
     */
    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }
        return instance;
    }

    public void setLoggedInUser(User loggedInUser) {
        currentUser = loggedInUser;
    }

    public void setJwt(String tempJwt) {
        jwt = tempJwt;
    }

    /**
     * Function logs current user out of system
     */
    public void logout() {
        currentUser = null;
        jwt = null;
    }

    /**
     * Function returns current logged in user
     * @return currentUser
     */
    public User getCurrentUser() {
        //FIXME: this is a privacy leak. may need to create 'public User(User user)' constructor in User
        // if we decide to fix this
        return currentUser;
    }

    public String getJwt(){
        return this.jwt;
    }
}
