package use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog;

import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;

import java.util.List;

/**
 * Data access interface for deleting a wellness log entry.
 * Also supports retrieving the current day's entries for view refreshing.
 */
public interface DeleteWellnessLogDataAccessInterf {

    /**
     * Deletes the wellness log entry with the given ID.
     *
     * @param logId The unique identifier of the log to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    boolean deleteById(String logId);

    /**
     * Returns the updated list of today's wellness log entries.
     * Used for refreshing the view after deletion.
     *
     * @return list of WellnessLogEntry for today
     */
    List<WellnessLogEntryInterf> getTodaysWellnessLogEntries();
}


