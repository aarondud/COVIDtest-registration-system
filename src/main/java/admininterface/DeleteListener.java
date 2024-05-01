package admininterface;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import COVIDbooking.Booking;
import users.Receptionist;
import users.UserFacade;

/**
 * Concrete class which will be responsible for notifying observers of a delete booking event. 
 */
public class DeleteListener implements BookingEventListener {

    /**
     * Mapper for the creation of ObjectNodes.
     */
    private ObjectMapper mapper;

    public DeleteListener() {
        this.mapper = new ObjectMapper();
    }

    /**
    * Concrete class which will notify observers of a delete booking event. 
    */
    @Override
    public void update(Receptionist admin, Booking booking) {
        // create a new message for delete
        ObjectNode message = this.mapper.createObjectNode();
        message.put("type", "deleted");
        message.put("customerId", booking.getCustomerId());
        // adding the message to the admin's messageList
        admin.addMessage(message);

        // updating the API
        UserFacade.getInstance().updateUser(admin);
    }
}
