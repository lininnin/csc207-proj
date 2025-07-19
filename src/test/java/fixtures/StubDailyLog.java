package fixtures;

import entity.DailyLog;
import entity.DailyTaskSummary;

import java.time.LocalDate;

public class StubDailyLog extends DailyLog {
    /**
     * Constructs a new DailyLog for the specified date.
     *
     * @param date The date for this daily log
     */
    public StubDailyLog(LocalDate date) {
        super(date);
    }

    @Override
    public LocalDate getDate() {
        return LocalDate.now(); // fixed or today's date
    }

    public DailyTaskSummary getDailyTaskSummary(LocalDate date) {
        return new DailyTaskSummary(date);
    }
}
