package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/*
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
    private Goal goal = null;
    private final List<Goal> goals = new ArrayList<>();
    private boolean isRegular = false;

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
    public Task(Info info, BeginAndDueDates dates, Priority priority, Boolean regular) {
        this.info = info;
        this.beginAndDueDates = dates;
        this.taskPriority = priority != null ? priority : Priority.MEDIUM;
        this.isComplete = false;
        this.completedDateTime = null;
        this.isRegular = regular;
    }

    /**
     * Marks the task as complete and records the completion time.
     * This is the only state mutation allowed as it represents a valid business transition.
     *
     * @param completionTime Time of task completion
     * Todo: if it's a regular task i think it should just update on log/history?
     */
    public void completeTask(LocalDateTime completionTime) {
        if (!this.isComplete && !isRegular) {
            this.isComplete = true;
            this.completedDateTime = completionTime;
        }
        else{

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

    /** @return Task priority */
    public Priority getTaskPriority() {
        return taskPriority;
    }



/*
    // Listener interface lives INSIDE entity layer
    public interface TaskCompleteListener {
        void onTaskCompleted(Task task, LocalDateTime completionTime);
    }

    private final List<TaskCompleteListener> listeners = new ArrayList<>();

    public void addTaskCompleteListener(TaskCompleteListener listener) {
        listeners.add(listener);
    }

    public void removeTaskCompleteListener(TaskCompleteListener listener) {
        listeners.remove(listener);
    }

    public void setIsRegular(boolean regular) {
        this.isRegular = regular;
    }

    private void notifyListeners() {
        for (TaskCompleteListener listener : listeners) {
            listener.onTaskCompleted(this, this.completedDateTime);
        }
    }*/


}