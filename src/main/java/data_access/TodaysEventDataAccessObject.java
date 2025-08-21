package data_access;

import entity.Alex.DailyEventLog.DailyEventLogFactoryInterf;
import entity.Alex.DailyEventLog.DailyEventLogInterf;

import entity.Alex.Event.EventInterf;
import entity.Alex.Event.Event;

import entity.info.Info;

import use_case.alex.event_related.add_event.AddEventDataAccessInterf;
import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventDataAccessInterf;
import use_case.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventDataAccessInterf;
import use_case.Angela.category.delete.DeleteCategoryEventDataAccessInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for today's events using a DailyEventLogInterf as the internal data store.
 * Fully decoupled from concrete Event/DailyEventLog classes using DIP.
 */
public class TodaysEventDataAccessObject implements AddEventDataAccessInterf,
        DeleteTodaysEventDataAccessInterf,
        EditTodaysEventDataAccessInterf,
        DeleteCategoryEventDataAccessInterface {

    private final DailyEventLogInterf todayLog;

    /**
     * Constructs a new TodaysEventDataAccessObject using the provided factory.
     *
     * @param factory Factory to create the DailyEventLogInterf for today
     */
    public TodaysEventDataAccessObject(DailyEventLogFactoryInterf factory) {
        this.todayLog = factory.create(LocalDate.now());
    }

    @Override
    public void save(EventInterf todaysEvent) {
        if (todaysEvent == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        todayLog.addEntry(todaysEvent);
    }

    @Override
    public boolean remove(EventInterf todaysEvent) {
        if (todaysEvent == null || todaysEvent.getInfo() == null) {
            return false;
        }
        String id = todaysEvent.getInfo().getId();
        int originalSize = todayLog.getActualEvents().size();
        todayLog.removeEntry(id);
        return todayLog.getActualEvents().size() < originalSize;
    }

    @Override
    public List<EventInterf> getTodaysEvents() {
        return todayLog.getActualEvents();
    }

    @Override
    public List<EventInterf> getEventsByCategory(String category) {
        List<EventInterf> filtered = new ArrayList<>();
        for (EventInterf event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && category.equals(event.getInfo().getCategory())) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    @Override
    public List<EventInterf> getEventsByName(String name) {
        List<EventInterf> filtered = new ArrayList<>();
        for (EventInterf event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && name.equals(event.getInfo().getName())) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    @Override
    public int getEventCount() {
        return todayLog.getActualEvents().size();
    }

    @Override
    public boolean contains(EventInterf todaysEvent) {
        return todayLog.getActualEvents().contains(todaysEvent);
    }

    @Override
    public void clearAll() {
        for (EventInterf e : new ArrayList<>(todayLog.getActualEvents())) {
            todayLog.removeEntry(e.getInfo().getId());
        }
    }

    @Override
    public EventInterf getEventById(String id) {
        for (EventInterf event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && id.equals(event.getInfo().getId())) {
                return event;
            }
        }
        return null;
    }

    @Override
    public boolean update(EventInterf updatedEvent) {
        if (updatedEvent == null || updatedEvent.getInfo() == null) return false;

        String id = updatedEvent.getInfo().getId();
        List<EventInterf> currentEvents = todayLog.getActualEvents();

        for (int i = 0; i < currentEvents.size(); i++) {
            EventInterf oldEvent = currentEvents.get(i);
            if (oldEvent.getInfo().getId().equals(id)) {
                todayLog.removeEntry(id);
                todayLog.addEntry(updatedEvent);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsById(String id) {
        for (EventInterf event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && id.equals(event.getInfo().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return The internal DailyEventLogInterf instance.
     */
    public DailyEventLogInterf getDailyEventLog() {
        return todayLog;
    }

    // ===== DeleteCategoryEventDataAccessInterface methods =====

    @Override
    public List<Info> findAvailableEventsByCategory(String categoryId) {
        // Today's events DAO doesn't manage available events
        // This is handled by EventAvailableDataAccessObject
        return new ArrayList<>();
    }

    @Override
    public List<Info> findTodaysEventsByCategory(String categoryId) {
        List<Info> result = new ArrayList<>();
        for (EventInterf event : todayLog.getActualEvents()) {
            if (event.getInfo() != null &&
                    event.getInfo().getCategory() != null &&
                    event.getInfo().getCategory().equals(categoryId)) {
                // Cast to Info since InfoInterf is implemented by Info
                if (event.getInfo() instanceof Info) {
                    result.add((Info) event.getInfo());
                }
            }
        }
        return result;
    }

    @Override
    public boolean clearAvailableEventCategory(String eventId) {
        // Today's events DAO doesn't manage available events
        // This is handled by EventAvailableDataAccessObject
        return false;
    }

    @Override
    public boolean clearTodaysEventCategory(String eventId) {
        for (EventInterf event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && event.getInfo().getId().equals(eventId)) {
                event.getInfo().setCategory(null);  // Clear the category
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Info> findAvailableEventsWithEmptyCategory() {
        // Today's events DAO doesn't manage available events
        // This will be handled by EventAvailableDataAccessObject
        return new ArrayList<>();
    }

    @Override
    public List<Info> findTodaysEventsWithEmptyCategory() {
        List<Info> result = new ArrayList<>();
        for (EventInterf event : todayLog.getActualEvents()) {
            if (event.getInfo() != null) {
                String category = event.getInfo().getCategory();
                if (category == null || category.trim().isEmpty()) {
                    // Cast to Info since InfoInterf is implemented by Info
                    if (event.getInfo() instanceof Info) {
                        result.add((Info) event.getInfo());
                    }
                }
            }
        }
        return result;
    }

    /**
     * Clears all data from this data access object for testing purposes.
     * WARNING: This will delete all events and should only be used in tests!
     */
    public void clearAllData() {
        if (todayLog != null) {
            // Clear today's events - need to check the DailyEventLog API
            // todayLog.clearAllEvents(); // TODO: Add clear method to DailyEventLogInterf
        }
    }

}
