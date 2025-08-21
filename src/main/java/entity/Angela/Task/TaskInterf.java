package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.InfoInterf;
import java.time.LocalDateTime;

/**
 * Interface defining the contract for Task entities.
 * Follows Clean Architecture principles by defining what a Task must provide.
 * Uses interface types to enable dependency inversion.
 */
public interface TaskInterf {
    /**
     * Gets the unique identifier for this task instance.
     * @return The task ID
     */
    String getId();
    
    /**
     * Gets the ID of the template task this was created from.
     * @return The template task ID
     */
    String getTemplateTaskId();
    
    /**
     * Gets the task's basic information.
     * @return The InfoInterf object containing name, description, category
     */
    InfoInterf getInfo();

    /**
     * Gets the task's priority level.
     * @return The priority (HIGH, MEDIUM, LOW) or null if not set
     */
    Task.Priority getPriority();

    /**
     * Gets the task's completion status.
     * @return true if completed, false otherwise
     */
    boolean getStatus();

    /**
     * Gets the completion timestamp.
     * @return The LocalDateTime when completed, or null if not completed
     */
    LocalDateTime getCompletedDateTime();

    /**
     * Gets the task's date range.
     * @return The BeginAndDueDatesInterf object
     */
    BeginAndDueDatesInterf getBeginAndDueDates();

    /**
     * Checks if the task is overdue.
     * @return true if overdue, false otherwise
     */
    boolean isOverDue();

    /**
     * Checks if this is a one-time task.
     * @return true if one-time, false if regular
     */
    boolean isOneTime();

    /**
     * Sets whether this is a one-time task.
     * @param oneTime true for one-time, false for regular
     */
    void setOneTime(boolean oneTime);
}