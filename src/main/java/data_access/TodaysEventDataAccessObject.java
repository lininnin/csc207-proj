package data_access;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.Event.Event;
import use_case.Alex.Event_related.add_event.AddEventDataAccessInterf;
import use_case.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventDataAccessInterf;
import use_case.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventDataAccessInterf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for today's events using a single DailyEventLog as the internal data store.
 */
public class TodaysEventDataAccessObject implements AddEventDataAccessInterf,
        DeleteTodaysEventDataAccessInterf,
        EditTodaysEventDataAccessInterf {

    private final DailyEventLog todayLog;

    /**
     * Constructs a new TodaysEventDataAccessObject using today's date.
     */
    public TodaysEventDataAccessObject() {
        this.todayLog = new DailyEventLog(LocalDate.now());
    }

    /**
     * Saves an event to today's log.
     */
    @Override
    public void save(Event todaysEvent) {
        if (todaysEvent == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        todayLog.addEntry(todaysEvent);
    }

    /**
     * Removes the event from today's log by ID match.
     */
    @Override
    public boolean remove(Event todaysEvent) {
        if (todaysEvent == null || todaysEvent.getInfo() == null) {
            return false;
        }
        String id = todaysEvent.getInfo().getId();
        int originalSize = todayLog.getActualEvents().size();
        todayLog.removeEntry(id);
        return todayLog.getActualEvents().size() < originalSize;
    }

    @Override
    public List<Event> getTodaysEvents() {
        return todayLog.getActualEvents();  // already returns a copy
    }

    @Override
    public List<Event> getEventsByCategory(String category) {
        List<Event> filtered = new ArrayList<>();
        for (Event event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && category.equals(event.getInfo().getCategory())) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    @Override
    public List<Event> getEventsByName(String name) {
        List<Event> filtered = new ArrayList<>();
        for (Event event : todayLog.getActualEvents()) {
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
    public boolean contains(Event todaysEvent) {
        return todayLog.getActualEvents().contains(todaysEvent);
    }

    @Override
    public void clearAll() {
        for (Event e : new ArrayList<>(todayLog.getActualEvents())) {
            todayLog.removeEntry(e.getInfo().getId());
        }
    }

    @Override
    public Event getEventById(String id) {
        for (Event event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && id.equals(event.getInfo().getId())) {
                return event;
            }
        }
        return null;
    }

    @Override
    public boolean update(Event updatedEvent) {
        if (updatedEvent == null || updatedEvent.getInfo() == null) return false;

        String id = updatedEvent.getInfo().getId();
        List<Event> currentEvents = todayLog.getActualEvents();

        for (int i = 0; i < currentEvents.size(); i++) {
            Event oldEvent = currentEvents.get(i);
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
        for (Event event : todayLog.getActualEvents()) {
            if (event.getInfo() != null && id.equals(event.getInfo().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return The internal DailyEventLog instance.
     */
    public DailyEventLog getDailyEventLog() {
        return todayLog;
    }
}

