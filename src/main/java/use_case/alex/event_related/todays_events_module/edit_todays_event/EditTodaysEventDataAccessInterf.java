package use_case.alex.event_related.todays_events_module.edit_todays_event;

import entity.alex.Event.EventInterf;

/**
 * Interface for data access operations related to editing available events.
 * This interface is used only by the EditAvailableEvent use case.
 * Now fully abstracted to depend on EventInterf instead of concrete Event class.
 */
public interface EditTodaysEventDataAccessInterf {

    /**
     * Updates an existing event's information in the event pool.
     *
     * @param updatedEvent The updated EventInterf object.
     * @return true if update is successful, false otherwise.
     */
    boolean update(EventInterf updatedEvent);

    /**
     * Retrieves an EventInterf object by its ID.
     * Useful when EditInteractor receives only an ID but needs the original event.
     *
     * @param id The ID of the EventInterf object.
     * @return The EventInterf with the given ID, or null if not found.
     */
    EventInterf getEventById(String id);

    /**
     * Checks whether an event with the given ID exists.
     *
     * @param id The ID to check.
     * @return true if the event exists, false otherwise.
     */
    boolean existsById(String id);
}

