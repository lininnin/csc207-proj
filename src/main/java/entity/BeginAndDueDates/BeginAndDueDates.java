package entity.BeginAndDueDates;

import java.time.LocalDate;

/**
 * Represents a time range with a begin date and optional due date.
 * Made mutable to support updating dates when adding tasks to Today.
 */
public class BeginAndDueDates {
    private LocalDate beginDate;
    private LocalDate dueDate;

    /**
     * Constructs a new time range with validation.
     *
     * @param beginDate The start date (can be null initially)
     * @param dueDate   The end date (can be null for open-ended tasks)
     * @throws IllegalArgumentException if dueDate is before beginDate
     */
    public BeginAndDueDates(LocalDate beginDate, LocalDate dueDate) {
        if (beginDate != null && dueDate != null && dueDate.isBefore(beginDate)) {
            throw new IllegalArgumentException("Due date cannot be before begin date");
        }
        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    /**
     * @return The starting date
     */
    public LocalDate getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the begin date.
     *
     * @param beginDate The begin date to set
     * @throws IllegalArgumentException if beginDate is after current dueDate
     */
    public void setBeginDate(LocalDate beginDate) {
        if (beginDate != null && dueDate != null && dueDate.isBefore(beginDate)) {
            throw new IllegalArgumentException("Due date cannot be before begin date");
        }
        this.beginDate = beginDate;
    }

    /**
     * @return The due date (might be null)
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date.
     *
     * @param dueDate The due date to set (can be null)
     * @throws IllegalArgumentException if dueDate is before current beginDate
     */
    public void setDueDate(LocalDate dueDate) {
        if (beginDate != null && dueDate != null && dueDate.isBefore(beginDate)) {
            throw new IllegalArgumentException("Due date cannot be before begin date");
        }
        this.dueDate = dueDate;
    }

    /**
     * Checks if the task has a due date.
     *
     * @return true if due date is set, false otherwise
     */
    public boolean hasDueDate() {
        return dueDate != null;
    }

    /**
     * Checks if the dates are valid (begin date before or equal to due date).
     *
     * @return true if valid or if either date is null, false if invalid
     */
    public boolean isValid() {
        if (beginDate == null || dueDate == null) {
            return true; // Can't validate if dates not set
        }
        return !beginDate.isAfter(dueDate);
    }

    /**
     * Checks if a given date falls within the begin and due date range.
     *
     * @param date The date to check
     * @return true if date is within range, false otherwise
     */
    public boolean isDateInRange(LocalDate date) {
        if (date == null) {
            return false;
        }

        if (beginDate != null && date.isBefore(beginDate)) {
            return false;
        }

        if (dueDate != null && date.isAfter(dueDate)) {
            return false;
        }

        return true;
    }
}