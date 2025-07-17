package data_access;

import entity.DailyLog;
import use_case.DailyLogRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of DailyLogRepository for demo purposes.
 */
public class InMemoryDailyLogRepository implements DailyLogRepository {
    private final Map<LocalDate, DailyLog> dailyLogs = new HashMap<>();

    @Override
    public void save(DailyLog dailyLog) {
        dailyLogs.put(dailyLog.getDate(), dailyLog);
    }

    @Override
    public DailyLog findByDate(LocalDate date) {
        return dailyLogs.get(date);
    }
}
