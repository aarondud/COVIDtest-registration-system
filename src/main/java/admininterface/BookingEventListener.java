package admininterface;

import COVIDbooking.Booking;
import users.Receptionist;

/**
 * Interface for listeners relating to booking changes.
 */
public interface BookingEventListener {
    /**
     * Method to alert an observer of an event
     * @param admin Receptionist who is an observer
     * @param booking booking which contained the change.
     */
    void update(Receptionist admin, Booking booking);
}
