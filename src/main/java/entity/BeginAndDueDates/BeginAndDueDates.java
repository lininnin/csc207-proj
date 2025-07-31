package entity.BeginAndDueDates;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a date range with begin and due dates for tasks and events.
 *
 * Business Rules:
 * - Begin date is required for Today's items
 * - Due date is optional
 * - Begin date cannot be after due date
 * - Used only for Today's instances, not Available templates
 */
public class BeginAndDueDates {
    private LocalDate beginDate;
    private LocalDate dueDate;

    /**
     * Creates a new date range.
     *
     * @param beginDate The begin date (required for Today's items)
     * @param dueDate The due date (optional)
     * @throws IllegalArgumentException if dates are invalid
     */
    public BeginAndDueDates(LocalDate beginDate, LocalDate dueDate) {
        // Validate date logic
        if (beginDate != null && dueDate != null && beginDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Begin date cannot be after due date");
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
     * @throws IllegalArgumentException if it would be after due date
     */
    public void setBeginDate(LocalDate beginDate) {
        if (beginDate != null && dueDate != null && beginDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Begin date cannot be after due date");
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
     * @param dueDate The new due date
     * @throws IllegalArgumentException if it would be before begin date
     */
    public void setDueDate(LocalDate dueDate) {
        if (beginDate != null && dueDate != null && beginDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Due date cannot be before begin date");
        }
        this.dueDate = dueDate;
    }

    /**
     * Checks if a due date is set.
     *
     * @return true if due date is not null
     */
    public boolean hasDueDate() {
        return dueDate != null;
    }

    /**
     * Checks if the due date has passed (item is overdue).
     * Only applicable if due date is set.
     *
     * @return true if due date is before today
     */
    public boolean isOverdue() {
        if (dueDate == null) {
            return false;
        }
        return dueDate.isBefore(LocalDate.now());
    }

    /**
     * Checks if the due date is today or in the future.
     * Used for daily reset logic.
     *
     * @return true if due date is >= today, false if null or past
     */
    public boolean isDueTodayOrFuture() {
        if (dueDate == null) {
            return false;
        }
        return !dueDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BeginAndDueDates{");
        if (beginDate != null) {
            sb.append("beginDate=").append(beginDate);
        }
        if (dueDate != null) {
            if (beginDate != null) sb.append(", ");
            sb.append("dueDate=").append(dueDate);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeginAndDueDates that = (BeginAndDueDates) o;
        return Objects.equals(beginDate, that.beginDate) &&
                Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginDate, dueDate);
    }
}