package entity.Alex.EventAvailable;

import entity.info.Info;
import entity.info.InfoInterf;

import java.util.List;

/**
 * Interface representing a pool of available events.
 * Defines operations for adding, removing, querying, and managing events
 * that can be scheduled into a user's day.
 */
public interface EventAvailableInterf {

    /**
     * Adds the specified event info to the available event pool.
     *
     * @param info the {@link Info} object to add
     * @throws IllegalArgumentException if {@code info} is null
     */
    void addEvent(InfoInterf info);

    /**
     * Removes the specified event info from the available event pool.
     *
     * @param info the {@link Info} object to remove
     * @return {@code true} if the event was present and removed,
     *         {@code false} otherwise
     */
    boolean removeEvent(InfoInterf info);

    /**
     * Returns a list of all currently available events.
     *
     * @return a new {@link List} containing all {@link Info} objects
     */
    List<InfoInterf> getEventAvailable();

    /**
     * Returns a list of available events that match the given category.
     *
     * @param category the category to filter by
     * @return a list of {@link Info} objects in the specified category
     */
    List<InfoInterf> getEventsByCategory(String category);

    /**
     * Returns a list of available events that match the given name.
     *
     * @param name the name to filter by
     * @return a list of {@link Info} objects with the specified name
     */
    List<InfoInterf> getEventsByName(String name);

    /**
     * Returns the total number of available events.
     *
     * @return the count of available {@link Info} objects
     */
    int getEventCount();

    /**
     * Checks whether the specified event info exists in the pool.
     *
     * @param info the {@link Info} object to check
     * @return {@code true} if the event exists, {@code false} otherwise
     */
    boolean contains(InfoInterf info);

    /**
     * Removes all events from the available event pool.
     */
    void clearAll();
}


