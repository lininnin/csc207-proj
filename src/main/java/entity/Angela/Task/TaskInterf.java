package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDateTime;

/**
 * Interface defining the contract for Task entities.
 */
public interface TaskInterf {
    /**
     * Gets the task's basic information.
     * @return The Info object containing name, description, category
     */
    Info getInfo();

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
     * @return The BeginAndDueDates object
     */
    BeginAndDueDates getBeginAndDueDates();

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