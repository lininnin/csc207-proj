package data_access.in_memory_repo;

import entity.Angela.DailyLog;
import use_case.repository.DailyLogRepository;

import java.time.LocalDate;
import java.util.*;

public class InMemoryDailyLogRepo implements DailyLogRepository {
    private final Map<LocalDate, DailyLog> logs = new HashMap<>();

    @Override
    public void save(DailyLog log) {
        logs.put(log.getDate(), log);
    }

    @Override
    public DailyLog findByDate(LocalDate date) {
        return logs.get(date);
    }

    @Override
    public List<DailyLog> loadBetween(LocalDate from, LocalDate to) {
        List<DailyLog> result = new ArrayList<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            DailyLog log = logs.get(d);
            if (log != null) result.add(log);
        }
        return result;
    }
}
