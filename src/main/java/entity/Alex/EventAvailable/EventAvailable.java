package entity.Alex.EventAvailable;

import entity.info.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of events available to be added to a specific day.
 * This serves as a pool of events that users can choose from when planning their day.
 *
 * TODO: Will be used in Sophia's story #2 for filtering and organizing events by category/priority
 */
public class EventAvailable implements EventAvailableInterf {
    private final List<Info> eventInfoAvailable;

    /**
     * Constructs a new EventAvailable with an empty list of events.
     */
    public EventAvailable() {
        this.eventInfoAvailable = new ArrayList<>();
    }

    /**
     * Adds an event to the available events pool.
     *
     * @param info The Info to add
     * @throws IllegalArgumentException if info is null
     */
    public void addEvent(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        eventInfoAvailable.add(info);
    }

    /**
     * Removes an event from the available events pool.
     *
     * @param info The Info to remove
     * @return true if the Info was removed, false if it wasn't in the list
     */
    public boolean removeEvent(Info info) {
        return eventInfoAvailable.remove(info);
    }

    /**
     * Returns all available events.
     *
     * @return A copy of the list of available Info
     */
    public List<Info> getEventAvailable() {
        return new ArrayList<>(eventInfoAvailable);
    }

    /**
     * Returns events filtered by category.
     *
     * @param category The category to filter by
     * @return List of Info in the specified category
     */
    public List<Info> getEventsByCategory(String category) {
        List<Info> filtered = new ArrayList<>();
        for (Info info : eventInfoAvailable) {
            if (category.equals(info.getCategory())) {
                filtered.add(info);
            }
        }
        return filtered;
    }

    /**
     * Returns events filtered by name.
     *
     * @param name The name to filter by
     * @return List of Info with the specified name
     */
    public List<Info> getEventsByName(String name) {
        List<Info> filtered = new ArrayList<>();
        for (Info info : eventInfoAvailable) {
            if (name.equals(info.getName())) {
                filtered.add(info);
            }
        }
        return filtered;
    }

    /**
     * Returns the number of available events.
     *
     * @return The size of the info list
     */
    public int getEventCount() {
        return eventInfoAvailable.size();
    }

    /**
     * Checks if a specific Info is in the available event list.
     *
     * @param info The Info to check
     * @return true if the Info is available, false otherwise
     */
    public boolean contains(Info info) {
        return eventInfoAvailable.contains(info);
    }

    /**
     * Clears all Info from the available event list.
     */
    public void clearAll() {
        eventInfoAvailable.clear();
    }
}

