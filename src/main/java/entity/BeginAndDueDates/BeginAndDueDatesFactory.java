package entity.BeginAndDueDates;

import java.time.LocalDate;

/**
 * Factory class for creating BeginAndDueDates instances.
 * Provides convenient methods for common date range scenarios.
 * Supports both mutable and immutable entity creation.
 */
public class BeginAndDueDatesFactory implements BeginAndDueDatesFactoryInterf {

    /**
     * Creates a BeginAndDueDates with both dates set.
     *
     * @param beginDate The start date
     * @param dueDate The end date
     * @return A new BeginAndDueDates instance
     * @throws IllegalArgumentException if dates are invalid
     */
    public BeginAndDueDatesInterf create(LocalDate beginDate, LocalDate dueDate) {
        return new BeginAndDueDates(beginDate, dueDate);
    }

    /**
     * Creates a BeginAndDueDates with only begin date (open-ended).
     *
     * @param beginDate The start date
     * @return A new BeginAndDueDates instance with no due date
     */
    public BeginAndDueDatesInterf createOpenEnded(LocalDate beginDate) {
        return new BeginAndDueDates(beginDate, null);
    }

    /**
     * Creates an empty BeginAndDueDates (both dates null).
     * Used for initial task creation before dates are set.
     *
     * @return A new BeginAndDueDates instance with no dates
     */
    public BeginAndDueDatesInterf createEmpty() {
        return new BeginAndDueDates(null, null);
    }

    /**
     * Creates a BeginAndDueDates for today with optional due date.
     *
     * @param daysUntilDue Number of days from today until due (null for no due date)
     * @return A new BeginAndDueDates instance starting today
     */
    public BeginAndDueDatesInterf createStartingToday(Integer daysUntilDue) {
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
    public BeginAndDueDatesInterf createWithDuration(LocalDate beginDate, int durationDays) {
        if (beginDate == null) {
            throw new IllegalArgumentException("Begin date cannot be null when creating with duration");
        }
        LocalDate dueDate = beginDate.plusDays(durationDays);
        return new BeginAndDueDates(beginDate, dueDate);
    }
    
    // Factory methods for immutable entity creation
    
    /**
     * Creates an immutable BeginAndDueDates with both dates set.
     *
     * @param beginDate The start date
     * @param dueDate The end date
     * @return A new ImmutableBeginAndDueDates instance
     * @throws IllegalArgumentException if dates are invalid
     */
    public ImmutableBeginAndDueDates createImmutable(LocalDate beginDate, LocalDate dueDate) {
        return new ImmutableBeginAndDueDates(beginDate, dueDate);
    }

    /**
     * Creates an immutable BeginAndDueDates with only begin date (open-ended).
     *
     * @param beginDate The start date
     * @return A new ImmutableBeginAndDueDates instance with no due date
     */
    public ImmutableBeginAndDueDates createImmutableOpenEnded(LocalDate beginDate) {
        return new ImmutableBeginAndDueDates(beginDate, null);
    }

    /**
     * Creates an empty immutable BeginAndDueDates (both dates null).
     * Used for initial task creation before dates are set.
     *
     * @return A new ImmutableBeginAndDueDates instance with no dates
     */
    public ImmutableBeginAndDueDates createImmutableEmpty() {
        return new ImmutableBeginAndDueDates(null, null);
    }

    /**
     * Creates an immutable BeginAndDueDates for today with optional due date.
     *
     * @param daysUntilDue Number of days from today until due (null for no due date)
     * @return A new ImmutableBeginAndDueDates instance starting today
     */
    public ImmutableBeginAndDueDates createImmutableStartingToday(Integer daysUntilDue) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = daysUntilDue != null ? today.plusDays(daysUntilDue) : null;
        return new ImmutableBeginAndDueDates(today, dueDate);
    }

    /**
     * Creates an immutable BeginAndDueDates for a specific duration.
     *
     * @param beginDate The start date
     * @param durationDays Number of days duration
     * @return A new ImmutableBeginAndDueDates instance with calculated due date
     */
    public ImmutableBeginAndDueDates createImmutableWithDuration(LocalDate beginDate, int durationDays) {
        if (beginDate == null) {
            throw new IllegalArgumentException("Begin date cannot be null when creating with duration");
        }
        LocalDate dueDate = beginDate.plusDays(durationDays);
        return new ImmutableBeginAndDueDates(beginDate, dueDate);
    }
    
    /**
     * Creates an immutable BeginAndDueDates from existing instance.
     *
     * @param dates The BeginAndDueDates instance to convert
     * @return ImmutableBeginAndDueDates object
     */
    public ImmutableBeginAndDueDates createImmutable(BeginAndDueDates dates) {
        if (dates == null) {
            throw new IllegalArgumentException("BeginAndDueDates cannot be null");
        }
        return new ImmutableBeginAndDueDates(dates);
    }
}