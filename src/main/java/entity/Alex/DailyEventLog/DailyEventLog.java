package entity.Alex.DailyEventLog;

import entity.Alex.Event.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a daily log of actual events that occurred on a given date.
 * Each entry in this log corresponds to a recorded Event object.
 */
public class DailyEventLog implements DailyEventLogInterf{

    private final String id;
    private final LocalDate date;
    private final List<Event> actualEvents;

    /**
     * Constructs a new DailyEventLog for the specified date.
     *
     * @param date The date this event log corresponds to
     */
    public DailyEventLog(LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.actualEvents = new ArrayList<>();
    }

    /**
     * Adds an event to the actual event list for this day.
     * If the event is null or already exists, it is not added.
     *
     * @param event The Event to add
     */
    public void addEntry(Event event) {
        if (event != null && !actualEvents.contains(event)) {
            actualEvents.add(event);
        }
    }

    /**
     * Removes an event from the actual event list by its Info ID.
     * If the ID is null or no matching event is found, nothing is removed.
     *
     * @param id The ID of the event (from its Info object) to remove
     */
    public void removeEntry(String id) {
        if (id == null) return;

        actualEvents.removeIf(entry -> id.equals(entry.getInfo().getId()));
    }

    // ------------------ Getters ------------------

    /**
     * @return The unique identifier of this daily event log
     */
    public String getId() {
        return id;
    }

    /**
     * @return The date associated with this event log
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return A copy of the list of all events recorded for this day
     */
    public List<Event> getActualEvents() {
        return new ArrayList<>(actualEvents); // defensive copy
    }
}

