package entity.alex.DailyEventLog;

import java.time.LocalDate;

/**
 * Factory interface for creating DailyEventLog instances.
 */
public interface DailyEventLogFactoryInterf {

    /**
     * Creates a new DailyEventLog instance for the given date.
     *
     * @param date The date the log corresponds to
     * @return A new DailyEventLogInterf instance
     */
    DailyEventLogInterf create(LocalDate date);
}

