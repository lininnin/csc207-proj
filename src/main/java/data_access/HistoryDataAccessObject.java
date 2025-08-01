package data_access;

import entity.Angela.DailyLog;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory DAO for managing DailyLogs by date.
 * Each date corresponds to one DailyLog.
 */
public class HistoryDataAccessObject {

    // Mapping from date to DailyLog
    private final Map<LocalDate, DailyLog> dailyLogs = new HashMap<>();

    /**
     * Retrieves the DailyLog for the given date, if it exists.
     * Otherwise, creates a new one, stores it, and returns it.
     *
     * @param date The date to retrieve or create a DailyLog for
     * @return The existing or newly created DailyLog
     */
    public DailyLog getOrCreateDailyLog(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }

        return dailyLogs.computeIfAbsent(date, DailyLog::new);
    }

    /**
     * Stores or replaces the DailyLog for the given date.
     *
     * @param log The DailyLog to store
     */
    public void saveDailyLog(DailyLog log) {
        if (log == null || log.getDate() == null) {
            throw new IllegalArgumentException("Cannot save null DailyLog or DailyLog with null date.");
        }

        dailyLogs.put(log.getDate(), log);
    }

    /**
     * Returns whether a DailyLog exists for the given date.
     *
     * @param date The date to check
     * @return true if a DailyLog exists, false otherwise
     */
    public boolean hasLogForDate(LocalDate date) {
        return dailyLogs.containsKey(date);
    }

    /**
     * Deletes the DailyLog associated with the given date, if it exists.
     *
     * @param date The date to delete the log for
     * @return true if a log was deleted, false if none existed
     */
    public boolean deleteDailyLog(LocalDate date) {
        return dailyLogs.remove(date) != null;
    }

    /**
     * Returns all stored DailyLogs.
     *
     * @return Map of all daily logs keyed by date
     */
    public Map<LocalDate, DailyLog> getAllLogs() {
        return new HashMap<>(dailyLogs);
    }
}

