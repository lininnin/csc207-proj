package data_access;

import entity.Alex.Event.Event;
import use_case.Alex.add_event.AddEventDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of events that have been added to today.
 * This acts as today's finalized event list for display or tracking.
 */
public class TodaysEventDataAccessObject implements AddEventDataAccessInterface {
    private final List<Event> todaysEvents;

    /**
     * Constructs a new TodaysEventDataAccessObject with an empty list.
     */
    public TodaysEventDataAccessObject() {
        this.todaysEvents = new ArrayList<>();
    }

    /**
     * Saves the given Event object to today's list.
     *
     * @param todaysEvent The Event to save.
     */
    @Override
    public void save(Event todaysEvent) {
        if (todaysEvent == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        todaysEvents.add(todaysEvent);
    }

    /**
     * Removes the given Event from today's list.
     *
     * @param todaysEvent The Event to remove.
     * @return true if removed, false otherwise.
     */
    @Override
    public boolean remove(Event todaysEvent) {
        return todaysEvents.remove(todaysEvent);
    }

    /**
     * @return List of all today's events.
     */
    @Override
    public List<Event> getTodaysEvents() {
        return new ArrayList<>(todaysEvents);
    }

    /**
     * @param category Category to filter.
     * @return Events that match the category.
     */
    @Override
    public List<Event> getEventsByCategory(String category) {
        List<Event> filtered = new ArrayList<>();
        for (Event event : todaysEvents) {
            if (event.getInfo() != null && category.equals(event.getInfo().getCategory())) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    /**
     * @param name Event name to filter.
     * @return Events that match the name.
     */
    @Override
    public List<Event> getEventsByName(String name) {
        List<Event> filtered = new ArrayList<>();
        for (Event event : todaysEvents) {
            if (event.getInfo() != null && name.equals(event.getInfo().getName())) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    /**
     * @return Total count of today's events.
     */
    @Override
    public int getEventCount() {
        return todaysEvents.size();
    }

    /**
     * Checks whether the given Event is present in today's list.
     *
     * @param todaysEvent The Event to check.
     * @return true if present, false otherwise.
     */
    @Override
    public boolean contains(Event todaysEvent) {
        return todaysEvents.contains(todaysEvent);
    }

    /**
     * Clears all events from today's list.
     */
    @Override
    public void clearAll() {
        todaysEvents.clear();
    }
}


