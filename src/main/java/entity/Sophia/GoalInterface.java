package entity.Sophia;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDateTime;

/**
 * Interface representing a goal with its associated properties and progress tracking.
 * Provides methods to access goal information, time periods, completion status, and progress metrics.
 */
public interface GoalInterface {
    /**
     * Enum representing the possible time periods for a goal.
     */
    public enum TimePeriod {
        /** Goal spanning a week */
        WEEK,
        /** Goal spanning a month */
        MONTH
    }

    /**
     * Retrieves the goal information.
     * @return GoalInfo object containing goal details
     */
    GoalInfo getGoalInfo();

    /**
     * Gets the beginning and due dates for the goal.
     * @return BeginAndDueDates object containing the time frame
     */
    BeginAndDueDates getBeginAndDueDates();

    /**
     * Gets the time period type for the goal.
     * @return TimePeriod enum value (WEEK or MONTH)
     */
    TimePeriod getTimePeriod();

    /**
     * Gets the current progress towards the goal.
     * @return integer representing current progress
     */
    int getCurrentProgress();

    /**
     * Checks if the goal has been completed.
     * @return true if the goal is completed, false otherwise
     */
    boolean isCompleted();

    /**
     * Retrieves general information about the goal.
     * @return Info object containing goal metadata
     */
    Info getInfo();

    /**
     * Gets information about the target task associated with the goal.
     * @return Info object about the target task
     */
    Info getTargetTaskInfo();

    /**
     * Gets the frequency at which the goal should be worked on.
     * @return integer representing the frequency
     */
    int getFrequency();

    /**
     * Gets the date and time when the goal was completed.
     * @return LocalDateTime of completion, or null if not completed
     */
    LocalDateTime getCompletedDateTime();

    /**
     * Gets a simplified progress representation as a string.
     * @return String representation of progress (e.g., "3/5")
     */
    String getSimpleProgress();
}