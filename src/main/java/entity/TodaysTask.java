package entity;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Represents an active task in Today's Tasks with additional properties.
 * References an AvailableTask template and adds active properties like priority and completion status.
 * Follows Single Responsibility Principle - only responsible for active task state.
 */
public class TodaysTask {
    /**
     * Task priority levels.
     */
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    private final AvailableTask availableTask;
    private final Priority priority;
    private final BeginAndDueDates beginAndDueDates;
    private boolean isCompleted;
    private LocalDateTime completedDateTime;

    /**
     * Constructs a new TodaysTask from an available task template.
     *
     * @param availableTask The task template this is based on
     * @param priority Task priority (null defaults to MEDIUM)
     * @param beginAndDueDates Date range for this task instance
     * @throws IllegalArgumentException if availableTask or dates are null
     */
    public TodaysTask(AvailableTask availableTask, Priority priority, BeginAndDueDates beginAndDueDates) {
        if (availableTask == null) {
            throw new IllegalArgumentException("Available task cannot be null");
        }
        if (beginAndDueDates == null) {
            throw new IllegalArgumentException("Begin and due dates cannot be null");
        }

        this.availableTask = availableTask;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.beginAndDueDates = beginAndDueDates;
        this.isCompleted = false;
        this.completedDateTime = null;
    }

    /**
     * Marks this task as completed at the current time.
     *
     * @throws IllegalStateException if task is already completed
     */
    public void markComplete() {
        if (isCompleted) {
            throw new IllegalStateException("Task is already completed");
        }
        this.isCompleted = true;
        this.completedDateTime = LocalDateTime.now();
    }

    /**
     * Unmarks this task as completed.
     *
     * @throws IllegalStateException if task is not completed
     */
    public void markIncomplete() {
        if (!isCompleted) {
            throw new IllegalStateException("Task is not completed");
        }
        this.isCompleted = false;
        this.completedDateTime = null;
    }

    /**
     * Checks if this task is overdue.
     * A task is overdue if it has a due date that has passed and is not completed.
     *
     * @return true if overdue, false otherwise
     */
    public boolean isOverdue() {
        return !isCompleted && beginAndDueDates.isPastDue();
    }

    // Getters
    public AvailableTask getAvailableTask() {
        return availableTask;
    }

    public Info getInfo() {
        return availableTask.getInfo();
    }

    public Priority getPriority() {
        return priority;
    }

    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    /**
     * Creates a new TodaysTask with updated priority.
     *
     * @param newPriority The new priority
     * @return New TodaysTask instance with updated priority
     */
    public TodaysTask withPriority(Priority newPriority) {
        TodaysTask updated = new TodaysTask(availableTask, newPriority, beginAndDueDates);
        if (this.isCompleted) {
            updated.isCompleted = true;
            updated.completedDateTime = this.completedDateTime;
        }
        return updated;
    }

    /**
     * Creates a new TodaysTask with updated due date.
     *
     * @param newDueDate The new due date
     * @return New TodaysTask instance with updated due date
     */
    public TodaysTask withDueDate(LocalDate newDueDate) {
        BeginAndDueDates newDates = new BeginAndDueDates(beginAndDueDates.getBeginDate(), newDueDate);
        TodaysTask updated = new TodaysTask(availableTask, priority, newDates);
        if (this.isCompleted) {
            updated.isCompleted = true;
            updated.completedDateTime = this.completedDateTime;
        }
        return updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodaysTask that = (TodaysTask) o;
        return availableTask.equals(that.availableTask);
    }

    @Override
    public int hashCode() {
        return availableTask.hashCode();
    }

    @Override
    public String toString() {
        return String.format("TodaysTask[%s, priority=%s, completed=%s, overdue=%s]",
                getInfo().getName(), priority, isCompleted, isOverdue());
    }
}