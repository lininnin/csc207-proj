package data_access;

import entity.alex.DailyWellnessLog.DailyWellnessLogFactoryInterf;
import entity.alex.DailyWellnessLog.DailyWellnessLogInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogDataAccessInterf;
import use_case.alex.wellness_log_related.todays_wellness_log_module.delete_wellnesslog.DeleteWellnessLogDataAccessInterf;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogDataAccessInterf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory DAO for a single day's wellness log.
 * Implements multiple interfaces for adding, deleting, and editing logs.
 * Fully decoupled from concrete DailyWellnessLog using factory and interface.
 */
public class TodaysWellnessLogDataAccessObject implements
        AddWellnessLogDataAccessInterf,
        DeleteWellnessLogDataAccessInterf,
        EditWellnessLogDataAccessInterf {

    /**
     * The current day's wellness log.
     */
    private DailyWellnessLogInterf dailyWellnessLog;

    /**
     * Factory for creating new DailyWellnessLog instances.
     */
    private final DailyWellnessLogFactoryInterf logFactory;

    /**
     * Constructs the DAO using a provided DailyWellnessLogFactory.
     *
     * @param logFactoryParam A factory that creates DailyWellnessLogInterf instances
     */
    public TodaysWellnessLogDataAccessObject(
            final DailyWellnessLogFactoryInterf logFactoryParam) {
        this.logFactory = logFactoryParam;
        this.dailyWellnessLog = logFactory.create(LocalDate.now());
    }

    /**
     * Saves a new wellness log entry to the current day's log.
     *
     * @param entry The wellness log entry to be added
     */
    @Override
    public void save(final WellnessLogEntryInterf entry) {
        if (!entry.getTime().toLocalDate().equals(dailyWellnessLog.getDate())) {
            throw new IllegalArgumentException("Entry does not belong to today's log.");
        }
        dailyWellnessLog.addEntry(entry);
    }

    /**
     * Removes a wellness log entry from the current day's log.
     *
     * @param entry The entry to be removed
     * @return true if removal succeeded, false otherwise
     */
    @Override
    public boolean remove(final WellnessLogEntryInterf entry) {
        int before = dailyWellnessLog.getEntries().size();
        dailyWellnessLog.removeEntry(entry.getId());
        int after = dailyWellnessLog.getEntries().size();
        return after < before;
    }

    /**
     * Deletes a log entry by ID.
     *
     * @param logId The ID of the entry to delete
     * @return true if an entry was deleted, false otherwise
     */
    @Override
    public boolean deleteById(final String logId) {
        int before = dailyWellnessLog.getEntries().size();
        dailyWellnessLog.removeEntry(logId);
        int after = dailyWellnessLog.getEntries().size();
        return after < before;
    }

    /**
     * Returns a defensive copy of all entries in today's log.
     *
     * @return List of today's wellness log entries
     */
    @Override
    public List<WellnessLogEntryInterf> getTodaysWellnessLogEntries() {
        return new ArrayList<>(dailyWellnessLog.getEntries());
    }

    /**
     * Checks if the entry exists in today's log.
     *
     * @param entry The entry to check
     * @return true if entry exists, false otherwise
     */
    @Override
    public boolean contains(final WellnessLogEntryInterf entry) {
        return dailyWellnessLog.getEntries().stream()
                .anyMatch(e -> e.getId().equals(entry.getId()));
    }

    /**
     * Clears all entries and resets the daily log to a new one.
     */
    @Override
    public void clearAll() {
        this.dailyWellnessLog = logFactory.create(LocalDate.now());
    }

    /**
     * Gets a specific entry by ID.
     *
     * @param logId The ID of the entry
     * @return The matching entry or null if not found
     */
    @Override
    public WellnessLogEntryInterf getById(final String logId) {
        return dailyWellnessLog.getEntries().stream()
                .filter(e -> e.getId().equals(logId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing wellness log entry by ID.
     *
     * @param updatedEntry The new version of the entry
     * @return true if update succeeded, false otherwise
     */
    @Override
    public boolean update(final WellnessLogEntryInterf updatedEntry) {
        boolean removed = deleteById(updatedEntry.getId());
        if (removed) {
            save(updatedEntry);
            return true;
        }
        return false;
    }

    /**
     * Gets the current DailyWellnessLog interface.
     *
     * @return The current DailyWellnessLogInterf
     */
    public DailyWellnessLogInterf getDailyLog() {
        return this.dailyWellnessLog;
    }
}

