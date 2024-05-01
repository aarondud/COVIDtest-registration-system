package users;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Concrete factory class for the creation of User types
 */
public class ConcreteUserFactory implements UserFactory {

    /**
     * Function manually creates user type
     * @param userNode ObjectNode, JSON data of a test.
     * @return User
     */
    @Override
    public User createUser(ObjectNode userNode) {
        User newUser = null;

        // TODO: decide how to handle case where multiple are true
        if (userNode.get("isCustomer").booleanValue()) {
            newUser = new Patient(userNode);
        }
        else if (userNode.get("isReceptionist").booleanValue()) {
            newUser = new Receptionist(userNode);
        }
        else if (userNode.get("isHealthcareWorker").booleanValue()) {
            newUser = new HealthcareWorker(userNode);
        }

        return newUser;
    }
}
