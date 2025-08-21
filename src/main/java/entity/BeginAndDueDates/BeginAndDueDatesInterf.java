package entity.BeginAndDueDates;

import java.time.LocalDate;

/**
 * Interface for BeginAndDueDates entity.
 * Defines operations for managing begin and due dates.
 */
public interface BeginAndDueDatesInterf {

    /**
     * @return The begin date
     */
    LocalDate getBeginDate();

    /**
     * Sets the begin date.
     * @deprecated Use immutable update methods instead
     * @param beginDate The new begin date
     * @throws IllegalArgumentException if it would be after due date
     */
    @Deprecated
    void setBeginDate(LocalDate beginDate);

    /**
     * @return The due date, or null if not set
     */
    LocalDate getDueDate();

    /**
     * Sets the due date.
     * @deprecated Use immutable update methods instead
     * @param dueDate The new due date
     * @throws IllegalArgumentException if it would be before begin date
     */
    @Deprecated
    void setDueDate(LocalDate dueDate);

    /**
     * @return true if due date is not null
     */
    boolean hasDueDate();

    /**
     * @return true if due date is before today
     */
    boolean isOverdue();

    /**
     * @return true if due date is >= today, false if null or past
     */
    boolean isDueTodayOrFuture();
}

