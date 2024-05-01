package admininterface;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import COVIDbooking.Booking;
import users.Receptionist;
import users.UserFacade;

/**
 * Concrete class which will be responsible for notifying observers of a modify booking event. 
 */
public class ModifyListener implements BookingEventListener {

    /**
     * Mapper for the creation of ObjectNodes.
     */
    private ObjectMapper mapper;

    public ModifyListener() {
        this.mapper = new ObjectMapper();
    }

     /**
    * Concrete class which will notify observers of a delete booking event. 
    */
    @Override
    public void update(Receptionist admin, Booking booking) {
        // create a new message for modify
        ObjectNode message = this.mapper.createObjectNode();
        message.put("type", "modified");
        message.put("customerId", booking.getCustomerId());
        // adding the message to the admin's messageList
        admin.addMessage(message);

        // updating the API
        UserFacade.getInstance().updateUser(admin);
        
    }
}
