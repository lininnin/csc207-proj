package use_case.Alex.Event_related.todays_events_module.delete_todays_event;

import entity.Alex.Event.Event;

/**
 * Interface for data access operations related to deleting available events.
 * This interface is used only by the DeleteAvailableEvent use case.
 */
public interface DeleteTodaysEventDataAccessInterf {

    /**
     * Removes the given event from the pool.
     *
     * @param event The Event object to remove.
     * @return true if removed successfully, false otherwise.
     */
    boolean remove(Event event);

    /**
     * Checks whether the given event is present in the event pool.
     *
     * @param event The Event object to check.
     * @return true if present, false otherwise.
     */
    boolean contains(Event event);

    /**
     * Retrieves an Info object by its ID.
     * Useful when DeleteInteractor only receives an ID but needs the full Info.
     *
     * @param id The ID of the Info object.
     * @return The Info object with the given ID, or null if not found.
     */
    Event getEventById(String id);
}


