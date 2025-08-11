package entity.Alex.DailyWellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;

import java.time.LocalDate;
import java.util.List;

public interface DailyWellnessLogInterf {

    /**
     * @return The unique identifier of this daily wellness log.
     */
    String getId();

    /**
     * @return The date associated with this log.
     */
    LocalDate getDate();

    /**
     * @return A list of all wellness log entries for this date.
     */
    List<WellnessLogEntryInterf> getEntries();

    /**
     * Adds a wellness log entry to the list for this day.
     * Duplicate entries should not be added.
     *
     * @param entry The WellnessLogEntry to add.
     */
    void addEntry(WellnessLogEntryInterf entry);

    /**
     * Removes a wellness log entry by ID.
     *
     * @param id The ID of the entry to remove.
     */
    void removeEntry(String id);
}

