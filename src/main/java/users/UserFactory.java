package users;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Factory interface for creating User types
 */
public interface UserFactory {
    /**
     * Implemented by concrete factories to create user types using JSON data
     * @param userNode ObjectNode, JSON data of a test.
     * @return User
     */
    User createUser(ObjectNode userNode);
}
