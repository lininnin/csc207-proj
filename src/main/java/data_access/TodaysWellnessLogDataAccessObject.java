package data_access;

import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogDataAccessInterf;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogDataAccessInterf;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogDataAccessInterf;

import java.time.LocalDate;
import java.util.List;

/**
 * In-memory DAO for a single day's wellness log.
 * Implements Add, Delete, and Edit use case interfaces.
 */
public class TodaysWellnessLogDataAccessObject implements
        AddWellnessLogDataAccessInterf,
        DeleteWellnessLogDataAccessInterf,
        EditWellnessLogDataAccessInterf {

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
    public boolean deleteById(String logId) {
        int before = dailyLog.getEntries().size();
        dailyLog.removeEntry(logId);
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

    // ✅ 实现 Edit 接口所需方法：

    @Override
    public WellnessLogEntry getById(String logId) {
        return dailyLog.getEntries().stream()
                .filter(e -> e.getId().equals(logId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean update(WellnessLogEntry updatedEntry) {
        // 删除旧的，加入新的
        boolean removed = deleteById(updatedEntry.getId());
        if (removed) {
            save(updatedEntry);
            return true;
        }
        return false;
    }
}




