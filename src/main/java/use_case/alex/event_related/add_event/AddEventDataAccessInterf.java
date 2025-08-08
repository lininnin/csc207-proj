package use_case.alex.event_related.add_event;

import entity.Alex.Event.Event;

import java.util.List;

/**
 * Interface for the Create Event data access object.
 * Defines methods to persist and query available events.
 */
public interface AddEventDataAccessInterf {

    /**
     * Saves the given Event object to the event pool.
     *
     * @param todaysEvent The Event object representing an event to be saved.
     */
    void save(Event todaysEvent);

    /**
     * Removes the given event from the pool.
     *
     * @param todaysEvent The Event object to remove.
     * @return true if removed, false otherwise
     */
    boolean remove(Event todaysEvent);

    /**
     * Gets all available events.
     *
     * @return List of all available event objects.
     */
    List<Event> getTodaysEvents();

    /**
     * Gets events matching a specific category.
     *
     * @param category Category to filter.
     * @return Events that match the category.
     */
    List<Event> getEventsByCategory(String category);

    /**
     * Gets events matching a specific name.
     *
     * @param name Event name to filter.
     * @return Events that match the name.
     */
    List<Event> getEventsByName(String name);

    /**
     * Gets total count of available events.
     *
     * @return Total event count.
     */
    int getEventCount();

    /**
     * Checks whether the given Event is present.
     *
     * @param todaysEvent The Event to check.
     * @return true if present, false otherwise.
     */
    boolean contains(Event todaysEvent);

    /**
     * Clears all available events.
     */
    void clearAll();
}
