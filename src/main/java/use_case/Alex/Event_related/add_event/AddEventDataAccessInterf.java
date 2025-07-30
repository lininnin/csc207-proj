package use_case.Alex.Event_related.add_event;

import entity.Alex.Event.Event;

import java.util.List;

/**
 * Interface for the Create Event data access object.
 * Defines methods to persist and query available events.
 */
public interface AddEventDataAccessInterf {

    /**
     * Saves the given Info object to the event pool.
     *
     * @param eventInfo The Info object representing an event to be saved.
     */
    void save(Event todaysEvent);

    /**
     * Removes the given event from the pool.
     *
     * @param eventInfo The Info object to remove.
     * @return true if removed, false otherwise
     */
    boolean remove(Event todaysEvent);

    /**
     * @return List of all available event Info
     */
    List<Event> getTodaysEvents();

    /**
     * @param category Category to filter
     * @return Events that match the category
     */
    List<Event> getEventsByCategory(String category);

    /**
     * @param name Event name to filter
     * @return Events that match the name
     */
    List<Event> getEventsByName(String name);

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
    boolean contains(Event todaysEvent);

    /**
     * Clears all available events.
     */
    void clearAll();
}