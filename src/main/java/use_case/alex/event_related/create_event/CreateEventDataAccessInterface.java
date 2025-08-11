package use_case.alex.event_related.create_event;

import entity.info.InfoInterf;
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
    void save(InfoInterf eventInfo);

    /**
     * Removes the given event from the pool.
     *
     * @param eventInfo The Info object to remove.
     * @return true if removed, false otherwise
     */
    boolean remove(InfoInterf eventInfo);

    /**
     * @return List of all available event Info
     */
    List<InfoInterf> getAllEvents();

    /**
     * @param category Category to filter
     * @return Events that match the category
     */
    List<InfoInterf> getEventsByCategory(String category);

    /**
     * @param name Event name to filter
     * @return Events that match the name
     */
    List<InfoInterf> getEventsByName(String name);

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
    boolean contains(InfoInterf eventInfo);

    /**
     * Clears all available events.
     */
    void clearAll();
}
