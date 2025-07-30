package entity.Alex.DailyWellnessLog;

import java.time.LocalDate;

public interface DailyWellnessLogFactoryInterf {

    /**
     * Creates a new DailyWellnessLog instance for the given date.
     *
     * @param date the date for which the log is created
     * @return a new DailyWellnessLog instance
     */
    DailyWellnessLogInterf create(LocalDate date);
}

