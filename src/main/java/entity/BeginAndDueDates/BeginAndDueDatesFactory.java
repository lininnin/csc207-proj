package entity.BeginAndDueDates;

import java.time.LocalDate;

/**
 * Factory class for creating BeginAndDueDates instances.
 * Provides convenient methods for common date range scenarios.
 */
public class BeginAndDueDatesFactory {

    /**
     * Creates a BeginAndDueDates with both dates set.
     *
     * @param beginDate The start date
     * @param dueDate The end date
     * @return A new BeginAndDueDates instance
     * @throws IllegalArgumentException if dates are invalid
     */
    public static BeginAndDueDates create(LocalDate beginDate, LocalDate dueDate) {
        return new BeginAndDueDates(beginDate, dueDate);
    }

    /**
     * Creates a BeginAndDueDates with only begin date (open-ended).
     *
     * @param beginDate The start date
     * @return A new BeginAndDueDates instance with no due date
     */
    public static BeginAndDueDates createOpenEnded(LocalDate beginDate) {
        return new BeginAndDueDates(beginDate, null);
    }

    /**
     * Creates an empty BeginAndDueDates (both dates null).
     * Used for initial task creation before dates are set.
     *
     * @return A new BeginAndDueDates instance with no dates
     */
    public static BeginAndDueDates createEmpty() {
        return new BeginAndDueDates(null, null);
    }

    /**
     * Creates a BeginAndDueDates for today with optional due date.
     *
     * @param daysUntilDue Number of days from today until due (null for no due date)
     * @return A new BeginAndDueDates instance starting today
     */
    public static BeginAndDueDates createStartingToday(Integer daysUntilDue) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = daysUntilDue != null ? today.plusDays(daysUntilDue) : null;
        return new BeginAndDueDates(today, dueDate);
    }

    /**
     * Creates a BeginAndDueDates for a specific duration.
     *
     * @param beginDate The start date
     * @param durationDays Number of days duration
     * @return A new BeginAndDueDates instance with calculated due date
     */
    public static BeginAndDueDates createWithDuration(LocalDate beginDate, int durationDays) {
        if (beginDate == null) {
            throw new IllegalArgumentException("Begin date cannot be null when creating with duration");
        }
        LocalDate dueDate = beginDate.plusDays(durationDays);
        return new BeginAndDueDates(beginDate, dueDate);
    }
}