package use_case.Angela.view_history;

import entity.Angela.TodaySoFarSnapshot;
import java.time.LocalDate;
import java.util.List;

/**
 * Data access interface for the view history use case.
 * Provides methods to store and retrieve historical "Today So Far" snapshots.
 */
public interface ViewHistoryDataAccessInterface {
    
    /**
     * Saves a snapshot of today's data.
     * @param snapshot The snapshot to save
     * @return true if saved successfully, false otherwise
     */
    boolean saveSnapshot(TodaySoFarSnapshot snapshot);
    
    /**
     * Retrieves a snapshot for a specific date.
     * @param date The date to retrieve
     * @return The snapshot if it exists, null otherwise
     */
    TodaySoFarSnapshot getSnapshot(LocalDate date);
    
    /**
     * Gets a list of all dates that have snapshots.
     * @return List of dates with available history, sorted by date descending
     */
    List<LocalDate> getAvailableDates();
    
    /**
     * Checks if a snapshot exists for a given date.
     * @param date The date to check
     * @return true if snapshot exists, false otherwise
     */
    boolean hasSnapshot(LocalDate date);
    
    /**
     * Deletes old snapshots beyond a certain number of days.
     * @param daysToKeep Number of days of history to keep
     * @return Number of snapshots deleted
     */
    int cleanupOldSnapshots(int daysToKeep);
}