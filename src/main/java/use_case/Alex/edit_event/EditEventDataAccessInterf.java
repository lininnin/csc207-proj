package use_case.Alex.edit_event;

import entity.Info.Info;

/**
 * Interface for data access operations related to editing available events.
 * This interface is used only by the EditAvailableEvent use case.
 */
public interface EditEventDataAccessInterf {

    /**
     * Updates an existing event's information in the event pool.
     *
     * @param updatedEvent The updated Info object.
     * @return true if update is successful, false otherwise.
     */
    boolean update(Info updatedEvent);

    /**
     * Retrieves an Info object by its ID.
     * Useful when EditInteractor receives only an ID but needs the original event.
     *
     * @param id The ID of the Info object.
     * @return The Info object with the given ID, or null if not found.
     */
    Info getEventById(String id);

    /**
     * Checks whether an event with the given ID exists.
     *
     * @param id The ID to check.
     * @return true if the event exists, false otherwise.
     */
    boolean existsById(String id);
}

