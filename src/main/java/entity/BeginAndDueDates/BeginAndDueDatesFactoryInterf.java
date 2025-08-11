package entity.BeginAndDueDates;

import java.time.LocalDate;

/**
 * Interface for BeginAndDueDatesFactory.
 * Defines methods to create BeginAndDueDatesInterf instances for various scenarios.
 */
public interface BeginAndDueDatesFactoryInterf {

    /**
     * Creates a BeginAndDueDates with both dates set.
     *
     * @param beginDate The start date
     * @param dueDate The end date
     * @return A new BeginAndDueDates instance
     * @throws IllegalArgumentException if dates are invalid
     */
    BeginAndDueDatesInterf create(LocalDate beginDate, LocalDate dueDate);

    /**
     * Creates a BeginAndDueDates with only begin date (open-ended).
     *
     * @param beginDate The start date
     * @return A new BeginAndDueDates instance with no due date
     */
    BeginAndDueDatesInterf createOpenEnded(LocalDate beginDate);

    /**
     * Creates an empty BeginAndDueDates (both dates null).
     * Used for initial task creation before dates are set.
     *
     * @return A new BeginAndDueDates instance with no dates
     */
    BeginAndDueDatesInterf createEmpty();

    /**
     * Creates a BeginAndDueDates for today with optional due date.
     *
     * @param daysUntilDue Number of days from today until due (null for no due date)
     * @return A new BeginAndDueDates instance starting today
     */
    BeginAndDueDatesInterf createStartingToday(Integer daysUntilDue);

    /**
     * Creates a BeginAndDueDates for a specific duration.
     *
     * @param beginDate The start date
     * @param durationDays Number of days duration
     * @return A new BeginAndDueDates instance with calculated due date
     */
    BeginAndDueDatesInterf createWithDuration(LocalDate beginDate, int durationDays);
}

