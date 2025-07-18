package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a task that can be scheduled, prioritized, and marked as complete.
 * Each task is associated with Info metadata and a time range.
 * This entity follows Clean Architecture principles and is immutable except for
 * completion status which represents a valid state transition.
 */
public class Task {
    private final Info info;
    private final BeginAndDueDates beginAndDueDates;
    private boolean isComplete;
    private LocalDateTime completedDateTime;
    private final Priority taskPriority;

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
     * @param value   input value for priority (defaults to MEDIUM if null)
     * @throws IllegalArgumentException
     *
     */
    public Task(Info info, BeginAndDueDates dates, Object value) {
        this.info = info;
        this.beginAndDueDates = dates;

        if (value == null) {
            this.taskPriority = Priority.MEDIUM;
        }
        else if (value instanceof Priority) {
            this.taskPriority = (Priority) value;
        }
        else if (value instanceof Integer) {
            int index = (Integer) value;
            this.taskPriority = Priority.values()[index];
        }
        else {
            throw new IllegalArgumentException("Invalid priority input");
        }

        this.isComplete = false;
        this.completedDateTime = null;
    }

    /**
     * Marks the task as complete and records the completion time.
     * This is the only state mutation allowed as it represents a valid business transition.
     *
     * @param completionTime Time of task completion
     */
    public void completeTask(LocalDateTime completionTime) {
        if (!this.isComplete) {
            this.isComplete = true;
            this.completedDateTime = completionTime;
        }
    }

    /**
     * Checks if the task is overdue based on current time.
     * A task is overdue if it has a due date, the current date is past the due date,
     * and the task is not complete.
     * Implemented as a method rather than stored attribute to ensure data consistency.
     *
     * @return true if task is past due date and not complete, false otherwise
     */
    public boolean isOverdue() {
        if (beginAndDueDates.getDueDate() == null || isComplete) {
            return false;
        }
        return LocalDate.now().isAfter(beginAndDueDates.getDueDate());
    }

    /**
     * Print task info in neat format.
     * @return get task info
     * */
    public String printTask(){
        return info.getInfo()
                 + "\nBegin Date: " + beginAndDueDates.getBeginDate()
                + "\nDue Date: " + beginAndDueDates.getDueDate() + "\n"
                + (isComplete == true ? "\nCompleted: " + completedDateTime : "");
    }

    /**
     * get the begin and end date for task in object type.
     * @return Task time range
     */
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    /**
     * print the begin and end date for task.
     * @return String of dates
     */
    public String printBeginAndDueDates() {
        return "Begin Date: " + beginAndDueDates.getBeginDate() + "\n"+ "Due Date: " + beginAndDueDates.getDueDate();
    }

    /**
     * return if the task is complete.
     * @return Whether the task is complete
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * the time task was completed.
     * @return When the task was completed (if any)
     */
    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    /**
     * task's priority.
     * @return Task priority
     */
    public Priority getTaskPriority() {
        return taskPriority;
    }

    /**
     * get task category.
     * @return Info category
     */
    public String getCategory() {
        return info.getCategory();
    }

    /*
    * Todo: might consider to put getInfo and printTask (do it later)
    */
    /**
     * get the info in object type.
     * @return Info object
     */
    public Info getInfo(){
        return info;
    }
}
