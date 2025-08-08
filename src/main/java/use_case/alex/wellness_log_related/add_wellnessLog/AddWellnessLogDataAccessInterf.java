package use_case.alex.wellness_log_related.add_wellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;

import java.util.List;

public interface AddWellnessLogDataAccessInterf {

    /**
     * Saves the given wellness log entry.
     *
     * @param wellnessLogEntry The wellness log entry to be saved.
     */
    void save(WellnessLogEntry wellnessLogEntry);

    /**
     * Removes the given wellness log entry.
     *
     * @param wellnessLogEntry The wellness log entry to remove.
     * @return true if removed, false otherwise.
     */
    boolean remove(WellnessLogEntry wellnessLogEntry);

    /**
     * Returns today's wellness logs.
     *
     * @return List of today's wellness log entries.
     */
    List<WellnessLogEntry> getTodaysWellnessLogEntries();

    /**
     * Checks whether the given wellness log entry is already recorded.
     *
     * @param wellnessLogEntry Entry to check.
     * @return true if exists, false otherwise.
     */
    boolean contains(WellnessLogEntry wellnessLogEntry);

    /**
     * Clears all stored wellness log entries.
     */
    void clearAll();
}

