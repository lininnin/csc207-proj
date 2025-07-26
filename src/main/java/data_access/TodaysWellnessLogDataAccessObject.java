package data_access;

import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogDataAccessInterf;

import java.time.LocalDate;
import java.util.List;

/**
 * In-memory DAO for a single day's wellness log.
 * Implements AddWellnessLogDataAccessInterf.
 */
public class TodaysWellnessLogDataAccessObject implements AddWellnessLogDataAccessInterf {

    private DailyWellnessLog dailyLog;

    public TodaysWellnessLogDataAccessObject() {
        this.dailyLog = new DailyWellnessLog(LocalDate.now());
    }

    @Override
    public void save(WellnessLogEntry entry) {
        if (!entry.getTime().toLocalDate().equals(dailyLog.getDate())) {
            throw new IllegalArgumentException("Entry does not belong to today's log.");
        }
        dailyLog.addEntry(entry);
    }

    @Override
    public boolean remove(WellnessLogEntry entry) {
        int before = dailyLog.getEntries().size();
        dailyLog.removeEntry(entry.getId());
        int after = dailyLog.getEntries().size();
        return after < before;
    }

    @Override
    public List<WellnessLogEntry> getTodaysWellnessLogEntries() {
        return dailyLog.getEntries();
    }

    @Override
    public boolean contains(WellnessLogEntry entry) {
        return dailyLog.getEntries().stream()
                .anyMatch(e -> e.getId().equals(entry.getId()));
    }

    @Override
    public void clearAll() {
        this.dailyLog = new DailyWellnessLog(LocalDate.now());
    }
}


