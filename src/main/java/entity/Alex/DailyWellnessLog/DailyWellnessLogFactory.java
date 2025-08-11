package entity.Alex.DailyWellnessLog;

import java.time.LocalDate;

/**
 * A factory class for creating instances of DailyWellnessLog.
 * This class implements the DailyWellnessLogFactoryInterf interface.
 */
public class DailyWellnessLogFactory implements DailyWellnessLogFactoryInterf {

    /**
     * Creates a new DailyWellnessLogInterf instance for the given date.
     *
     * @param date the date for which the wellness log is created
     * @return a new DailyWellnessLogInterf instance
     */
    @Override
    public DailyWellnessLogInterf create(final LocalDate date) {
        return new DailyWellnessLog(date);
    }
}
