package data_access.in_memory_repo;

import entity.Angela.DailyLog;
import use_case.repository.DailyLogRepository;

import java.time.LocalDate;
import java.util.*;

/**
 * In-memory implementation of DailyLogRepository.
 * For production, swap with a database-backed implementation.
 */
public class InMemoryDailyLogRepository implements DailyLogRepository {
    // Use a map to store logs by date
    private final Map<LocalDate, DailyLog> logMap = new HashMap<>();

    @Override
    public void save(DailyLog dailyLog) {
        if (dailyLog == null || dailyLog.getDate() == null)
            throw new IllegalArgumentException("DailyLog or date cannot be null");
        logMap.put(dailyLog.getDate(), dailyLog);
    }

    @Override
    public DailyLog findByDate(LocalDate date) {
        return logMap.get(date);
    }

    @Override
    public List<DailyLog> loadBetween(LocalDate from, LocalDate to) {
        List<DailyLog> result = new ArrayList<>();
        for (Map.Entry<LocalDate, DailyLog> entry : logMap.entrySet()) {
            LocalDate date = entry.getKey();
            if ((date.isEqual(from) || date.isAfter(from)) &&
                    (date.isEqual(to) || date.isBefore(to))) {
                result.add(entry.getValue());
            }
        }
        result.sort(Comparator.comparing(DailyLog::getDate));
        return result;
    }
}
