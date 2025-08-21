package entity.alex.EventAvailable;

import entity.info.InfoInterf;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of events available to be added to a specific day.
 * This serves as a pool of events that users can choose from when planning their day.
 *
 * <p>Used in Sophia's story #2 for filtering and organizing events by category/priority.</p>
 */
public class EventAvailable implements EventAvailableInterf {

    /**
     * The list of available event info objects.
     */
    private final List<InfoInterf> eventInfoAvailable;

    /**
     * Constructs a new EventAvailable with an empty list of events.
     */
    public EventAvailable() {
        this.eventInfoAvailable = new ArrayList<>();
    }

    /**
     * Adds an event to the available events pool.
     *
     * @param info the Info to add
     * @throws IllegalArgumentException if info is null
     */
    public void addEvent(final InfoInterf info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null.");
        }
        eventInfoAvailable.add(info);
    }

    /**
     * Removes an event from the available events pool.
     *
     * @param info the Info to remove
     * @return true if the Info was removed, false if it wasn't in the list
     */
    public boolean removeEvent(final InfoInterf info) {
        return eventInfoAvailable.remove(info);
    }

    /**
     * Returns all available events.
     *
     * @return a copy of the list of available Info
     */
    public List<InfoInterf> getEventAvailable() {
        return new ArrayList<>(eventInfoAvailable);
    }

    /**
     * Returns events filtered by category.
     *
     * @param category the category to filter by
     * @return list of Info in the specified category
     */
    public List<InfoInterf> getEventsByCategory(final String category) {
        List<InfoInterf> filtered = new ArrayList<>();
        for (InfoInterf info : eventInfoAvailable) {
            if (category.equals(info.getCategory())) {
                filtered.add(info);
            }
        }
        return filtered;
    }

    /**
     * Returns events filtered by name.
     *
     * @param name the name to filter by
     * @return list of Info with the specified name
     */
    public List<InfoInterf> getEventsByName(final String name) {
        List<InfoInterf> filtered = new ArrayList<>();
        for (InfoInterf info : eventInfoAvailable) {
            if (name.equals(info.getName())) {
                filtered.add(info);
            }
        }
        return filtered;
    }

    /**
     * Returns the number of available events.
     *
     * @return the size of the info list
     */
    public int getEventCount() {
        return eventInfoAvailable.size();
    }

    /**
     * Checks if a specific Info is in the available event list.
     *
     * @param info the Info to check
     * @return true if the Info is available, false otherwise
     */
    public boolean contains(final InfoInterf info) {
        return eventInfoAvailable.contains(info);
    }

    /**
     * Clears all Info from the available event list.
     */
    public void clearAll() {
        eventInfoAvailable.clear();
    }
}


