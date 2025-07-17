package use_case;

import entity.DailyLog;
import java.time.LocalDate;

/**
 * Repository interface for DailyLog persistence.
 */
public class DailyLogRepository {
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
}
