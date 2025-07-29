package entity.BeginAndDueDates;

import java.time.LocalDate;

import java.time.LocalDate;

/**
 * Represents a time period with a begin date and optional due date.
 * Used by tasks, events, and goals to track their active periods.
 */
public class BeginAndDueDates {
    private LocalDate beginDate;
    private LocalDate dueDate;

    /**
     * Constructs a new BeginAndDueDates with the given dates.
     *
     * @param beginDate The start date (required)
     * @param dueDate The due date (optional)
     * @throws IllegalArgumentException if beginDate is null
     */
    public BeginAndDueDates(LocalDate beginDate, LocalDate dueDate) {
        if (beginDate == null) {
            throw new IllegalArgumentException("Begin date cannot be null");
        }
        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    /**
     * Gets the begin date.
     *
     * @return The begin date
     */
    public LocalDate getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the begin date.
     *
     * @param beginDate The new begin date
     * @throws IllegalArgumentException if beginDate is null
     */
    public void setBeginDate(LocalDate beginDate) {
        if (beginDate == null) {
            throw new IllegalArgumentException("Begin date cannot be null");
        }
        this.beginDate = beginDate;
    }

    /**
     * Gets the due date.
     *
     * @return The due date, or null if not set
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date.
     *
     * @param dueDate The new due date (can be null to clear)
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Checks if this period has a due date.
     *
     * @return true if due date is set, false otherwise
     */
    public boolean hasDueDate() {
        return dueDate != null;
    }

    /**
     * Checks if the due date has passed.
     *
     * @return true if due date exists and is before today, false otherwise
     */
    public boolean isPastDue() {
        return dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    /**
     * Gets the number of days until the due date.
     *
     * @return Days until due (negative if overdue), or 0 if no due date
     */
    public int getDaysUntilDue() {
        if (dueDate == null) {
            return 0;
        }
        return (int) LocalDate.now().until(dueDate).getDays();
    }
}