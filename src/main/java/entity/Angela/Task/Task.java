package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a task entity with all required fields per design.
 * Tasks are created with basic info, then enhanced with priority/dates when added to Today.
 */
public class Task implements TaskInterf {
    private final Info info;
    private Priority priority;
    private boolean isCompleted;
    private LocalDateTime completedDateTime;
    private final BeginAndDueDates beginAndDueDates;
    private boolean overDue;
    private boolean oneTime; // Added per design requirement

    /**
     * Priority levels for tasks.
     */
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    /**
     * Creates a new task with basic information.
     * Priority is not set at creation per design.
     *
     * @param info Basic task information
     * @param beginAndDueDates Task date range (set when added to Today)
     */
    public Task(Info info, BeginAndDueDates beginAndDueDates) {
        if (info == null) {
            throw new IllegalArgumentException("Task info cannot be null");
        }
        if (beginAndDueDates == null) {
            throw new IllegalArgumentException("BeginAndDueDates cannot be null");
        }

        this.info = info;
        this.beginAndDueDates = beginAndDueDates;
        this.isCompleted = false;
        this.completedDateTime = null;
        this.priority = null; // Not set until added to Today
        this.overDue = false;
        this.oneTime = false; // Default to regular (not one-time)

        updateOverdueStatus();
    }

    /**
     * Gets the task's basic information.
     *
     * @return The task info
     */
    public Info getInfo() {
        return info;
    }

    /**
     * Gets the task's priority.
     *
     * @return The priority, or null if not in Today's tasks
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the task's priority (only when added to Today).
     *
     * @param priority The priority level
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Checks if the task is completed.
     *
     * @return true if completed, false otherwise
     */
    public boolean isComplete() {
        return isCompleted;
    }

    /**
     * Gets the task's completion status.
     *
     * @return true if completed, false otherwise
     */
    public boolean getStatus() {
        return isCompleted;
    }

    /**
     * Updates the task's completion status.
     *
     * @param status The new completion status
     */
    public void editStatus(boolean status) {
        this.isCompleted = status;
        if (!status) {
            this.completedDateTime = null;
        }
        updateOverdueStatus();
    }

    /**
     * Marks the task as completed with timestamp.
     *
     * @param completionTime The time of completion
     */
    public void completeTask(LocalDateTime completionTime) {
        this.isCompleted = true;
        this.completedDateTime = completionTime;
        this.overDue = false; // Completed tasks are not overdue
    }

    /**
     * Gets the completion timestamp.
     *
     * @return The completion time, or null if not completed
     */
    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    /**
     * Sets the completion timestamp (for loading from storage).
     *
     * @param completedDateTime The completion time
     */
    public void setCompletedDateTime(LocalDateTime completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    /**
     * Gets the task's date range.
     *
     * @return The begin and due dates
     */
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    /**
     * Checks if the task is overdue.
     * A task is overdue if it has a due date that's passed and is not completed.
     *
     * @return true if overdue, false otherwise
     */
    public boolean isOverDue() {
        updateOverdueStatus();
        return overDue;
    }

    /**
     * Updates the overdue status based on current date.
     */
    private void updateOverdueStatus() {
        if (isCompleted) {
            overDue = false;
            return;
        }

        LocalDate dueDate = beginAndDueDates.getDueDate();
        if (dueDate != null) {
            overDue = LocalDate.now().isAfter(dueDate);
        } else {
            overDue = false;
        }
    }

    /**
     * Checks if this is a one-time task.
     * One-time tasks are removed from Available after the day ends.
     *
     * @return true if one-time task, false if regular
     */
    public boolean isOneTime() {
        return oneTime;
    }

    /**
     * Sets whether this is a one-time task.
     *
     * @param oneTime true for one-time task, false for regular
     */
    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    /**
     * Creates a copy of this task for today with priority and dates.
     * Used when adding from Available to Today.
     *
     * @param priority The priority for today
     * @param beginDate The begin date (usually today)
     * @param dueDate The optional due date
     * @return A new task instance configured for today
     */
    public Task copyForToday(Priority priority, LocalDate beginDate, LocalDate dueDate) {
        BeginAndDueDates newDates = new BeginAndDueDates(beginDate, dueDate);
        Task todayTask = new Task(this.info, newDates);
        todayTask.setPriority(priority);
        todayTask.setOneTime(this.oneTime);
        return todayTask;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task{");
        sb.append("name='").append(info.getName()).append("'");
        sb.append(", category='").append(info.getCategory()).append("'");
        if (priority != null) {
            sb.append(", priority=").append(priority);
        }
        sb.append(", completed=").append(isCompleted);
        if (beginAndDueDates.getDueDate() != null) {
            sb.append(", dueDate=").append(beginAndDueDates.getDueDate());
        }
        sb.append(", oneTime=").append(oneTime);
        sb.append(", overdue=").append(overDue);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return info.getId().equals(task.info.getId());
    }

    @Override
    public int hashCode() {
        return info.getId().hashCode();
    }
}