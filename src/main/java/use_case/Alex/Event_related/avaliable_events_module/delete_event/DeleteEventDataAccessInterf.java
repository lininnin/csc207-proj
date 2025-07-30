package use_case.Alex.Event_related.avaliable_events_module.delete_event;

import entity.Info.Info;

/**
 * Interface for data access operations related to deleting available events.
 * This interface is used only by the DeleteAvailableEvent use case.
 */
public interface DeleteEventDataAccessInterf {

    /**
     * Removes the given event from the pool.
     *
     * @param eventInfo The Info object to remove.
     * @return true if removed successfully, false otherwise.
     */
    boolean remove(Info eventInfo);

    /**
     * Checks whether the given event is present in the event pool.
     *
     * @param eventInfo The Info object to check.
     * @return true if present, false otherwise.
     */
    boolean contains(Info eventInfo);

    /**
     * Retrieves an Info object by its ID.
     * Useful when DeleteInteractor only receives an ID but needs the full Info.
     *
     * @param id The ID of the Info object.
     * @return The Info object with the given ID, or null if not found.
     */
    Info getEventById(String id);
}

