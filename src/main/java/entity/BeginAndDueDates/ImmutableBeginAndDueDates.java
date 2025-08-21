package entity.BeginAndDueDates;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable wrapper for BeginAndDueDates entity.
 * Provides immutable alternative to mutable BeginAndDueDates for Clean Architecture compliance.
 * 
 * Strategy: Adapter pattern to maintain backward compatibility with Event module.
 * This allows Task module to use immutable entities while Event module continues using mutable BeginAndDueDates.
 */
public class ImmutableBeginAndDueDates implements BeginAndDueDatesInterf {
    private final LocalDate beginDate;
    private final LocalDate dueDate;

    /**
     * Creates an immutable BeginAndDueDates from existing instance.
     * Used to convert mutable BeginAndDueDates to immutable version.
     *
     * @param dates The BeginAndDueDates instance to wrap
     * @throws IllegalArgumentException if dates is null
     */
    public ImmutableBeginAndDueDates(BeginAndDueDates dates) {
        if (dates == null) {
            throw new IllegalArgumentException("BeginAndDueDates cannot be null");
        }
        
        this.beginDate = dates.getBeginDate();
        this.dueDate = dates.getDueDate();
    }

    /**
     * Creates an immutable BeginAndDueDates with specified dates.
     * Used for creating new immutable instances.
     *
     * @param beginDate The begin date (required for Today's items)
     * @param dueDate The due date (optional)
     * @throws IllegalArgumentException if begin date is after due date
     */
    public ImmutableBeginAndDueDates(LocalDate beginDate, LocalDate dueDate) {
        // Validate date logic
        if (beginDate != null && dueDate != null && beginDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Begin date cannot be after due date");
        }

        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    @Override
    public LocalDate getBeginDate() {
        return beginDate;
    }

    @Override
    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public void setBeginDate(LocalDate beginDate) {
        // No-op for immutable implementation - use withBeginDate() instead
    }

    @Override
    public void setDueDate(LocalDate dueDate) {
        // No-op for immutable implementation - use withDueDate() instead
    }

    @Override
    public boolean hasDueDate() {
        return dueDate != null;
    }

    @Override
    public boolean isOverdue() {
        if (dueDate == null) {
            return false;
        }
        return dueDate.isBefore(LocalDate.now());
    }

    @Override
    public boolean isDueTodayOrFuture() {
        if (dueDate == null) {
            return false;
        }
        return !dueDate.isBefore(LocalDate.now());
    }

    // Immutable update methods (return new instances)

    /**
     * Creates a new ImmutableBeginAndDueDates with updated begin date.
     *
     * @param beginDate The new begin date
     * @return A new ImmutableBeginAndDueDates with updated begin date
     * @throws IllegalArgumentException if begin date would be after due date
     */
    public ImmutableBeginAndDueDates withBeginDate(LocalDate beginDate) {
        if (beginDate != null && this.dueDate != null && beginDate.isAfter(this.dueDate)) {
            throw new IllegalArgumentException("Begin date cannot be after due date");
        }
        return new ImmutableBeginAndDueDates(beginDate, this.dueDate);
    }

    /**
     * Creates a new ImmutableBeginAndDueDates with updated due date.
     *
     * @param dueDate The new due date
     * @return A new ImmutableBeginAndDueDates with updated due date
     * @throws IllegalArgumentException if due date would be before begin date
     */
    public ImmutableBeginAndDueDates withDueDate(LocalDate dueDate) {
        if (this.beginDate != null && dueDate != null && this.beginDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Due date cannot be before begin date");
        }
        return new ImmutableBeginAndDueDates(this.beginDate, dueDate);
    }

    /**
     * Creates a new ImmutableBeginAndDueDates with both dates updated.
     *
     * @param beginDate The new begin date
     * @param dueDate The new due date
     * @return A new ImmutableBeginAndDueDates with updated dates
     * @throws IllegalArgumentException if begin date would be after due date
     */
    public ImmutableBeginAndDueDates withDates(LocalDate beginDate, LocalDate dueDate) {
        if (beginDate != null && dueDate != null && beginDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Begin date cannot be after due date");
        }
        return new ImmutableBeginAndDueDates(beginDate, dueDate);
    }

    /**
     * Converts this immutable BeginAndDueDates to a mutable instance.
     * Used for backward compatibility with Event module.
     *
     * @return A new mutable BeginAndDueDates with the same data
     */
    public BeginAndDueDates toMutableBeginAndDueDates() {
        return new BeginAndDueDates(this.beginDate, this.dueDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableBeginAndDueDates that = (ImmutableBeginAndDueDates) o;
        return Objects.equals(beginDate, that.beginDate) &&
                Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginDate, dueDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImmutableBeginAndDueDates{");
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
}