package use_case.repository;

import entity.Angela.DailyLog;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for DailyLog persistence.
 */
public interface DailyLogRepository {
    /**
     * Saves or updates a daily log.
     *
     * @param dailyLog The daily log to save
     */
    void save(DailyLog dailyLog);

    /**
     * Finds a daily log by date.
     *
     * @param date The date to search for
     * @return The daily log if found, null otherwise
     */
    DailyLog findByDate(LocalDate date);

    List<DailyLog> loadBetween(LocalDate from, LocalDate to);
}