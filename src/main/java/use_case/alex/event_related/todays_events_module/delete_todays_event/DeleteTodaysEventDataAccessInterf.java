package use_case.alex.event_related.todays_events_module.delete_todays_event;

import entity.alex.Event.EventInterf;

/**
 * Interface for data access operations related to deleting available events.
 * This interface is used only by the DeleteAvailableEvent use case.
 * Now fully abstracted to depend on EventInterf instead of concrete Event class.
 */
public interface DeleteTodaysEventDataAccessInterf {

    /**
     * Removes the given event from the pool.
     *
     * @param event The EventInterf to remove.
     * @return true if removed successfully, false otherwise.
     */
    boolean remove(EventInterf event);

    /**
     * Checks whether the given event is present in the event pool.
     *
     * @param event The EventInterf to check.
     * @return true if present, false otherwise.
     */
    boolean contains(EventInterf event);

    /**
     * Retrieves an EventInterf object by its ID.
     * Useful when DeleteInteractor only receives an ID but needs the full event.
     *
     * @param id The ID of the EventInterf object.
     * @return The EventInterf with the given ID, or null if not found.
     */
    EventInterf getEventById(String id);
}
