package entity.alex.DailyEventLog;

import entity.alex.Event.EventInterf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a daily log of actual events that occurred on a given date.
 * Each entry in this log corresponds to a recorded Event object.
 */
public class DailyEventLog implements DailyEventLogInterf {

    /**
     * The unique identifier for this daily event log.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final String id;

    /**
     * The date that this daily event log is associated with.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final LocalDate date;

    /**
     * The list of actual events recorded for the given date.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final List<EventInterf> actualEvents;

    /**
     * Constructs a new DailyEventLog for the specified date.
     *
     * @param dateParam the date this event log corresponds to
     */
    public DailyEventLog(final LocalDate dateParam) {
        this.id = UUID.randomUUID().toString();
        this.date = dateParam;
        this.actualEvents = new ArrayList<>();
    }

    /**
     * Adds an event to the actual event list for this day.
     * If the event is null or already exists, it is not added.
     *
     * @param event the Event to add
     */
    public void addEntry(final EventInterf event) {
        if (event != null && !actualEvents.contains(event)) {
            actualEvents.add(event);
        }
    }

    /**
     * Removes an event from the actual event list by its Info ID.
     * If the ID is null or no matching event is found, nothing is removed.
     *
     * @param idParam the ID of the event (from its Info object) to remove
     */
    public void removeEntry(final String idParam) {
        if (idParam == null) {
            return;
        }
        actualEvents.removeIf(entry -> idParam.equals(entry.getInfo().getId()));
    }

    /**
     * Returns the unique identifier of this daily event log.
     *
     * @return the ID string of the event log
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date associated with this event log.
     *
     * @return the date of the log
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns a copy of the list of all events recorded for this day.
     *
     * @return a new List containing the actual events
     */
    public List<EventInterf> getActualEvents() {
        return new ArrayList<>(actualEvents);
    }
}
