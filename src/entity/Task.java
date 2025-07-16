import java.time.LocalDateTime;

/**
 * Represents a task that can be scheduled, prioritized, and marked as complete.
 * Each task is associated with Info metadata and a time range.
 */
public class Task {
    private final Info info;
    private final BeginAndDueDates beginAndDueDates;
    private boolean isComplete;
    private LocalDateTime completedDateTime;
    private boolean overDue;
    private Priority taskPriority;

    /**
     * Enum for task priority level.
     */

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    /**
     * Constructs a new Task with given metadata, dates, and optional priority.
     *
     * @param info       Metadata for the task
     * @param dates      Time window of the task
     * @param priority   Task priority (defaults to MEDIUM if null)
     */
    public Task(Info info, BeginAndDueDates dates, Priority priority) {
        this.info = info;
        this.beginAndDueDates = dates;
        this.taskPriority = priority != null ? priority : Priority.MEDIUM;
        this.isComplete = false;
        this.completedDateTime = null;
        this.overDue = false;
    }

    /**
     * Marks the task as complete and records the completion time.
     *
     * @param completionTime Time of task completion
     */
    public void completeTask(LocalDateTime completionTime) {
        this.isComplete = true;
        this.completedDateTime = completionTime;
    }

    /**
     * Checks if the task is overdue based on current time and due date.
     *
     * @param currentTime The current time to compare against due date
     */
    public void checkIfOverdue(LocalDateTime currentTime) {
        if (beginAndDueDates.getDueDate() != null &&
                currentTime.isAfter(beginAndDueDates.getDueDate()) &&
                !isComplete) {
            this.overDue = true;
        }
    }

    /** @return Task metadata (Info) */
    public Info getInfo() {
        return info;
    }

    /** @return Task time range */
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    /** @return Whether the task is complete */
    public boolean isComplete() {
        return isComplete;
    }

    /** @return When the task was completed (if any) */
    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    /** @return Whether the task is overdue */
    public boolean isOverDue() {
        return overDue;
    }

    /** @return Task priority */
    public Priority getTaskPriority() {
        return taskPriority;
    }

    /** @param priority The new task priority */
    public void setTaskPriority(Priority priority) {
        this.taskPriority = priority;
    }
}
