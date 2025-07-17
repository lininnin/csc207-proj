package entity;

import java.time.LocalDate;

/**
 * Represents a time range with a begin date and optional due date.
 * This class is immutable to ensure data integrity.
 */
public class BeginAndDueDates {
    private final LocalDate beginDate;
    private final LocalDate dueDate;

    /**
     * Constructs a new time range with validation.
     *
     * @param beginDate The start date (cannot be null)
     * @param dueDate   The end date (can be null for open-ended tasks)
     * @throws IllegalArgumentException if beginDate is null or if dueDate is before beginDate
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
     * @return The begin date
     */
    public LocalDate getBeginDate() {
        return beginDate;
    }

    /**
     * @return The due date (may be null)
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
}
