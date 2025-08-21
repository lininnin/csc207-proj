package data_access.in_memory_repo;

import entity.alex.Event.Event;
import use_case.repository.EventRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the EventRepository for testing and development.
 * Stores both all events and today's events in memory using ArrayLists.
 */
public class InMemoryEventRepository implements EventRepository {

    /** List of all available events. */
    private final List<Event> allEvents = new ArrayList<>();

    /** List of events scheduled for today. */
    private final List<Event> todayEvents = new ArrayList<>();

    /**
     * Returns a list of all available events.
     *
     * @return A copy of the list containing all events.
     */
    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(allEvents);
    }

    /**
     * Returns a list of events scheduled for today.
     *
     * @return A copy of the list containing today's events.
     */
    @Override
    public List<Event> getTodayEvents() {
        return new ArrayList<>(todayEvents);
    }

    /**
     * Saves the given event into the list of all events.
     *
     * @param event The event to be saved.
     */
    @Override
    public void save(final Event event) {
        allEvents.add(event);
    }

    /**
     * Adds the given event to today's event list.
     * Used for testing or manual simulation.
     *
     * @param event The event to be added to today.
     */
    public void addToToday(final Event event) {
        todayEvents.add(event);
    }
}
