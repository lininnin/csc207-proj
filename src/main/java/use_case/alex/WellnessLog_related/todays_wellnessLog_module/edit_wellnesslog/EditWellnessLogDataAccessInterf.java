package use_case.alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog;

import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;

import java.util.List;

/**
 * Data access interface for editing a wellness log entry.
 * Defines the methods required to retrieve, update, and query today's log entries.
 */
public interface EditWellnessLogDataAccessInterf {

    /**
     * Retrieves the wellness log entry with the given ID.
     *
     * @param logId The unique identifier of the wellness log.
     * @return The WellnessLogEntry if found, otherwise null.
     */
    WellnessLogEntryInterf getById(String logId);

    /**
     * Updates the given wellness log entry in the data store.
     *
     * @param updatedEntry The updated wellness log entry.
     * @return true if the update was successful, false otherwise.
     */
    boolean update(WellnessLogEntryInterf updatedEntry);

    /**
     * Retrieves all wellness log entries for today.
     *
     * @return List of today's WellnessLogEntry objects.
     */
    List<WellnessLogEntryInterf> getTodaysWellnessLogEntries();
}


