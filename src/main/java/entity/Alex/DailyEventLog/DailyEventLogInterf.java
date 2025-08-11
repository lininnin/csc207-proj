package entity.Alex.DailyEventLog;

import entity.Alex.Event.EventInterf;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for DailyEventLog.
 * Represents the abstraction for managing events for a specific day.
 * Now fully decoupled from the concrete Event class using EventInterf.
 */
public interface DailyEventLogInterf {

    /**
     * Adds a new event entry for the day.
     *
     * @param event The EventInterf to add
     */
    void addEntry(EventInterf event);

    /**
     * Removes an event entry by its Info ID.
     *
     * @param id The ID of the event to remove
     */
    void removeEntry(String id);

    /**
     * Gets the unique ID of this log.
     *
     * @return The UUID string
     */
    String getId();

    /**
     * Gets the date this log is associated with.
     *
     * @return The date
     */
    LocalDate getDate();

    /**
     * Gets all recorded events for this day.
     *
     * @return List of events
     */
    List<EventInterf> getActualEvents();
}
