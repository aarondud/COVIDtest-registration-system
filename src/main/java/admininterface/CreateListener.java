package admininterface;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import COVIDbooking.Booking;
import users.Receptionist;
import users.UserFacade;

/**
 * Concrete class which will be responsible for notifying observers of a create booking event. 
 */
public class CreateListener implements BookingEventListener {

    /**
     * Mapper for the creation of ObjectNodes.
     */
    private ObjectMapper mapper;

    public CreateListener() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Method to observers about the create booking event that took place.
     */
    @Override
    public void update(Receptionist admin, Booking booking){
        // create a new message for create
        ObjectNode message = this.mapper.createObjectNode();
        message.put("type", "created");
        message.put("customerId", booking.getCustomerId());
        // adding the message to the admin's messageList
        admin.addMessage(message);

        // updating the API
        UserFacade.getInstance().updateUser(admin);
        
    }
    
}
