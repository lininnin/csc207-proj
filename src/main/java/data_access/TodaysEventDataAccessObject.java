package data_access;

import entity.Alex.DailyEventLog.DailyEventLogFactoryInterf;
import entity.Alex.DailyEventLog.DailyEventLogInterf;
import entity.Alex.Event.Event;
import use_case.alex.event_related.add_event.AddEventDataAccessInterf;
import use_case.alex.event_related.todays_events_module.delete_todays_event.
        DeleteTodaysEventDataAccessInterf;
import use_case.alex.event_related.todays_events_module.edit_todays_event.
        EditTodaysEventDataAccessInterf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for today's events using a DailyEventLogInterf as the internal data store.
 * Now decoupled from the concrete DailyEventLog class using DIP.
 */
public class TodaysEventDataAccessObject implements AddEventDataAccessInterf,
        DeleteTodaysEventDataAccessInterf,
        EditTodaysEventDataAccessInterf {

    /**
     * Internal log for today's events.
     */
    private final DailyEventLogInterf todayLog;

    /**
     * Constructs a new TodaysEventDataAccessObject using the provided factory.
     *
     * @param factory Factory to create the DailyEventLogInterf for today
     */
    public TodaysEventDataAccessObject(final DailyEventLogFactoryInterf factory) {
        this.todayLog = factory.create(LocalDate.now());
    }

    /**
     * Saves an event to the log.
     *
     * @param todaysEvent Event to be added
     */
    @Override
    public void save(final Event todaysEvent) {
        if (todaysEvent == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        todayLog.addEntry(todaysEvent);
    }

    /**
     * Removes the specified event by its ID.
     *
     * @param todaysEvent Event to remove
     * @return true if removed, false otherwise
     */
    @Override
    public boolean remove(final Event todaysEvent) {
        if (todaysEvent == null || todaysEvent.getInfo() == null) {
            return false;
        }
        final String id = todaysEvent.getInfo().getId();
        final int originalSize = todayLog.getActualEvents().size();
        todayLog.removeEntry(id);
        return todayLog.getActualEvents().size() < originalSize;
    }

    /**
     * Returns the full list of today's events.
     *
     * @return list of events
     */
    @Override
    public List<Event> getTodaysEvents() {
        return todayLog.getActualEvents();
    }

    /**
     * Returns events matching the given category.
     *
     * @param category Category to filter by
     * @return filtered list
     */
    @Override
    public List<Event> getEventsByCategory(final String category) {
        List<Event> filtered = new ArrayList<>();
        for (final Event event : todayLog.getActualEvents()) {
            if (event.getInfo() != null
                    && category.equals(event.getInfo().getCategory())) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    /**
     * Returns events matching the given name.
     *
     * @param name Event name
     * @return filtered list
     */
    @Override
    public List<Event> getEventsByName(final String name) {
        List<Event> filtered = new ArrayList<>();
        for (final Event event : todayLog.getActualEvents()) {
            if (event.getInfo() != null
                    && name.equals(event.getInfo().getName())) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    /**
     * Returns the total number of events today.
     *
     * @return count of events
     */
    @Override
    public int getEventCount() {
        return todayLog.getActualEvents().size();
    }

    /**
     * Checks whether the given event exists.
     *
     * @param todaysEvent Event to check
     * @return true if exists
     */
    @Override
    public boolean contains(final Event todaysEvent) {
        return todayLog.getActualEvents().contains(todaysEvent);
    }

    /**
     * Clears all today's events.
     */
    @Override
    public void clearAll() {
        for (final Event e : new ArrayList<>(todayLog.getActualEvents())) {
            todayLog.removeEntry(e.getInfo().getId());
        }
    }

    /**
     * Finds an event by ID.
     *
     * @param id Event ID
     * @return Event if found, null otherwise
     */
    @Override
    public Event getEventById(final String id) {
        for (final Event event : todayLog.getActualEvents()) {
            if (event.getInfo() != null
                    && id.equals(event.getInfo().getId())) {
                return event;
            }
        }
        return null;
    }

    /**
     * Replaces an existing event with an updated one.
     *
     * @param updatedEvent The event with new data
     * @return true if update succeeded
     */
    @Override
    public boolean update(final Event updatedEvent) {
        if (updatedEvent == null || updatedEvent.getInfo() == null) {
            return false;
        }

        final String id = updatedEvent.getInfo().getId();
        List<Event> currentEvents = todayLog.getActualEvents();

        for (int i = 0; i < currentEvents.size(); i++) {
            final Event oldEvent = currentEvents.get(i);
            if (oldEvent.getInfo().getId().equals(id)) {
                todayLog.removeEntry(id);
                todayLog.addEntry(updatedEvent);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an event exists by ID.
     *
     * @param id Event ID
     * @return true if exists
     */
    @Override
    public boolean existsById(final String id) {
        for (final Event event : todayLog.getActualEvents()) {
            if (event.getInfo() != null
                    && id.equals(event.getInfo().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the internal DailyEventLogInterf instance.
     *
     * @return internal log
     */
    public DailyEventLogInterf getDailyEventLog() {
        return todayLog;
    }
}



