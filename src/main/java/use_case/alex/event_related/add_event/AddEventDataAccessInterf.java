package use_case.alex.event_related.add_event;

import entity.alex.Event.EventInterf;

import java.util.List;

/**
 * Interface for the Create Event data access object.
 * Defines methods to persist and query available events.
 * Now fully abstracted to depend on EventInterf instead of concrete Event class.
 */
public interface AddEventDataAccessInterf {

    /**
     * Saves the given event to the event pool.
     *
     * @param todaysEvent The EventInterf representing an event to be saved.
     */
    void save(EventInterf todaysEvent);

    /**
     * Removes the given event from the pool.
     *
     * @param todaysEvent The EventInterf to remove.
     * @return true if removed, false otherwise
     */
    boolean remove(EventInterf todaysEvent);

    /**
     * Gets all available events.
     *
     * @return List of all available events.
     */
    List<EventInterf> getTodaysEvents();

    /**
     * Gets events matching a specific category.
     *
     * @param category Category to filter.
     * @return Events that match the category.
     */
    List<EventInterf> getEventsByCategory(String category);

    /**
     * Gets events matching a specific name.
     *
     * @param name Event name to filter.
     * @return Events that match the name.
     */
    List<EventInterf> getEventsByName(String name);

    /**
     * Gets total count of available events.
     *
     * @return Total event count.
     */
    int getEventCount();

    /**
     * Checks whether the given event is present.
     *
     * @param todaysEvent The EventInterf to check.
     * @return true if present, false otherwise.
     */
    boolean contains(EventInterf todaysEvent);

    /**
     * Clears all available events.
     */
    void clearAll();
}

