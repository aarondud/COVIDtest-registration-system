package COVIDbooking;

import java.util.ArrayList;
import admininterface.BookingEventListener;
import admininterface.CreateListener;
import admininterface.DeleteListener;
import admininterface.ModifyListener;
import users.Receptionist;
import users.UserFacade;

/**
 * Class responsible for calling the specific listener to notify observers when a booking event takes place.
 */
public class BookingEventManager {
    /**
     * List of observers based on the observer patten.
     */
    private ArrayList<Receptionist> admins;
    /**
     * Booking which is the subject of an event.
     */
    private Booking booking;

    /**
     * Constructor for a BookingEventManager
     * @param booking booking that the event has applied to.
     */
    public BookingEventManager(Booking booking) {
        String testingSiteId = booking.getTestingSiteId();
        this.admins = new ArrayList<>();
        if (testingSiteId != null){
            this.populateAdmins(testingSiteId);
        }
        this.booking = booking;
    }

    /**
     * Method to notify observers through the listener based on the event
     * @param event Booking event that took place.
     */
    public void notifyObservers(String event) {
        // Event can be of "create", "modify", and "delete"
        BookingEventListener listener = null;
        if (event.equals("create")) {
             listener = new CreateListener();
        } 
        else if (event.equals("modify")) {
             listener = new ModifyListener();
        } 
        else if(event.equals("delete")) {
            listener = new DeleteListener();
            
        }
        callListener(listener, this.admins);
    }

    private void populateAdmins(String testingSiteId) {
        // need to check that the booking does have a testing site, i.e. not home booking
        // fill admins list with admins/healthcareWorkers at booking's site.
        ArrayList<Receptionist> healthcareWorkers= UserFacade.getInstance().getReceptionists();

        for (Receptionist admin : healthcareWorkers) {
            if (testingSiteId.equals(admin.getWorkplace())) {
                    this.admins.add(admin);
                }
            }
    }

    /**
     * Method to use call the listener's update method for each observer.
     * @param listener the appropriate listener for the event.
     * @param admins list of observers.
     */
    private void callListener(BookingEventListener listener, ArrayList<Receptionist> admins) {
        if (!admins.isEmpty()){
            for (Receptionist admin: admins) {
                listener.update(admin, booking);
            }
        }
    }

}
