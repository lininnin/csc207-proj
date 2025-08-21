package entity.alex.DailyEventLog;

import java.time.LocalDate;

/**
 * Default factory for creating DailyEventLog instances.
 * Implements the DailyEventLogFactoryInterf for dependency inversion.
 */
public class DailyEventLogFactory implements DailyEventLogFactoryInterf {

    /**
     * Creates a new DailyEventLog instance for the given date.
     *
     * @param date The date the log corresponds to
     * @return A DailyEventLogInterf instance
     */
    @Override
    public DailyEventLogInterf create(final LocalDate date) {
        return new DailyEventLog(date);
    }
}

