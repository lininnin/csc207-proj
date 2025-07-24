package entity;

import java.time.LocalDate;

/**
 * Value object representing a date range with begin and optional due dates.
 * Used by Tasks, Events, and Goals to track their active periods.
 * Immutable to ensure data consistency.
 */
public class BeginAndDueDates {
    private final LocalDate beginDate;
    private final LocalDate dueDate;

    /**
     * Constructs a date range with begin date and optional due date.
     *
     * @param beginDate Start date (required)
     * @param dueDate End date (optional)
     * @throws IllegalArgumentException if beginDate is null or dueDate is before beginDate
     */
    public BeginAndDueDates(LocalDate beginDate, LocalDate dueDate) {
        if (beginDate == null) {
            throw new IllegalArgumentException("Begin date cannot be null");
        }
        if (dueDate != null && dueDate.isBefore(beginDate)) {
            throw new IllegalArgumentException("Due date cannot be before begin date");
        }

        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    /**
     * Factory method for creating a date range starting today.
     *
     * @param dueDate Optional due date
     * @return New BeginAndDueDates instance starting today
     */
    public static BeginAndDueDates startingToday(LocalDate dueDate) {
        return new BeginAndDueDates(LocalDate.now(), dueDate);
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Checks if this date range has a due date.
     *
     * @return true if due date is set, false otherwise
     */
    public boolean hasDueDate() {
        return dueDate != null;
    }

    /**
     * Checks if the due date has passed (if set).
     *
     * @return true if has due date and it's before today, false otherwise
     */
    public boolean isPastDue() {
        return hasDueDate() && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format("BeginAndDueDates[begin=%s, due=%s]",
                beginDate, dueDate != null ? dueDate : "none");
    }
}