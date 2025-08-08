package data_access;

import entity.Alex.DailyWellnessLog.DailyWellnessLogFactoryInterf;
import entity.Alex.DailyWellnessLog.DailyWellnessLogInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import use_case.alex.WellnessLog_related.add_wellnessLog.AddWellnessLogDataAccessInterf;
import use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogDataAccessInterf;
import use_case.alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogDataAccessInterf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory DAO for a single day's wellness log.
 * Now fully decoupled from concrete DailyWellnessLog using DIP and factory injection.
 */
public class TodaysWellnessLogDataAccessObject implements
        AddWellnessLogDataAccessInterf,
        DeleteWellnessLogDataAccessInterf,
        EditWellnessLogDataAccessInterf {

    private DailyWellnessLogInterf dailyWellnessLog;
    private final DailyWellnessLogFactoryInterf logFactory;

    /**
     * Constructs the DAO using a provided DailyWellnessLogFactory.
     *
     * @param logFactory A factory that creates DailyWellnessLogInterf instances
     */
    public TodaysWellnessLogDataAccessObject(DailyWellnessLogFactoryInterf logFactory) {
        this.logFactory = logFactory;
        this.dailyWellnessLog = logFactory.create(LocalDate.now());
    }

    @Override
    public void save(WellnessLogEntry entry) {
        if (!entry.getTime().toLocalDate().equals(dailyWellnessLog.getDate())) {
            throw new IllegalArgumentException("Entry does not belong to today's log.");
        }
        dailyWellnessLog.addEntry(entry);
    }

    @Override
    public boolean remove(WellnessLogEntry entry) {
        int before = dailyWellnessLog.getEntries().size();
        dailyWellnessLog.removeEntry(entry.getId());
        int after = dailyWellnessLog.getEntries().size();
        return after < before;
    }

    @Override
    public boolean deleteById(String logId) {
        int before = dailyWellnessLog.getEntries().size();
        dailyWellnessLog.removeEntry(logId);
        int after = dailyWellnessLog.getEntries().size();
        return after < before;
    }

    @Override
    public List<WellnessLogEntry> getTodaysWellnessLogEntries() {
        return new ArrayList<>(dailyWellnessLog.getEntries()); // ✅ defensive copy
    }

    @Override
    public boolean contains(WellnessLogEntry entry) {
        return dailyWellnessLog.getEntries().stream()
                .anyMatch(e -> e.getId().equals(entry.getId()));
    }

    @Override
    public void clearAll() {
        this.dailyWellnessLog = logFactory.create(LocalDate.now());
    }

    @Override
    public WellnessLogEntry getById(String logId) {
        return dailyWellnessLog.getEntries().stream()
                .filter(e -> e.getId().equals(logId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean update(WellnessLogEntry updatedEntry) {
        boolean removed = deleteById(updatedEntry.getId());
        if (removed) {
            save(updatedEntry);
            return true;
        }
        return false;
    }

    /**
     * @return The current DailyWellnessLog (interface) for inspection or testing.
     */
    public DailyWellnessLogInterf getDailyLog() {
        return this.dailyWellnessLog;
    }
}
