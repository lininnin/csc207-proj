package use_case.Alex.create_event;

import entity.Info.Info;

import java.util.List;

/**
 * Interface for the Create Event data access object.
 * Defines methods to persist and query available events.
 */
public interface CreateEventDataAccessInterface {

    /**
     * Saves the given Info object to the event pool.
     *
     * @param eventInfo The Info object representing an event to be saved.
     */
    void save(Info eventInfo);

    /**
     * Removes the given event from the pool.
     *
     * @param eventInfo The Info object to remove.
     * @return true if removed, false otherwise
     */
    boolean remove(Info eventInfo);

    /**
     * @return List of all available event Info
     */
    List<Info> getAllEvents();

    /**
     * @param category Category to filter
     * @return Events that match the category
     */
    List<Info> getEventsByCategory(String category);

    /**
     * @param name Event name to filter
     * @return Events that match the name
     */
    List<Info> getEventsByName(String name);

    /**
     * @return Total count of available events
     */
    int getEventCount();

    /**
     * Checks whether the given Info is present.
     *
     * @param eventInfo Info to check
     * @return true if present, false otherwise
     */
    boolean contains(Info eventInfo);

    /**
     * Clears all available events.
     */
    void clearAll();
}

