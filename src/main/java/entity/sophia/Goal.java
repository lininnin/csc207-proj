package entity.Sophia;

import java.time.LocalDate;
import java.time.LocalDateTime;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

/**
 * Represents a goal that tracks progress on a target task over a specific time period (week or month).
 * Each goal has associated metadata (goalInfo), a date range, frequency requirement, and progress tracking.
 */
public class Goal implements GoalInterface {

    private final GoalInfo goalInfo;
    private final BeginAndDueDates beginAndDueDates;
    private final TimePeriod timePeriod;
    private int frequency;
    private int currentProgress;
    private boolean isCompleted;
    private LocalDateTime completedDateTime;
    /**
     * Constructs a new Goal with the given information.
     *
     * @param goalInfo        Goal metadata and target task info; must not be null
     * @param dates           Begin and due dates; must not be null
     * @param timePeriod      Time period of the goal (WEEK or MONTH); must not be null
     * @param frequency       Required task completion frequency; must not be negative
     * @throws IllegalArgumentException if any argument is invalid
     */

    public Goal(GoalInfo goalInfo, BeginAndDueDates dates, TimePeriod timePeriod, int frequency) {
        if (goalInfo == null) {
            throw new IllegalArgumentException("GoalInfo cannot be null.");
        }
        if (dates == null) {
            throw new IllegalArgumentException("BeginAndDueDates cannot be null.");
        }
        if (timePeriod == null) {
            throw new IllegalArgumentException("TimePeriod cannot be null.");
        }
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative.");
        }

        this.goalInfo = goalInfo;
        this.beginAndDueDates = dates;
        this.timePeriod = timePeriod;
        this.frequency = frequency;
        this.currentProgress = 0;
        this.isCompleted = false;
        this.completedDateTime = null;
    }

    /**
     * Records a task completion and updates the progress.
     * If the goal is achieved, marks it as completed and records the time.
     */
    public void recordCompletion() {
        if (!isCompleted) {
            currentProgress++;
            if (currentProgress >= frequency) {
                isCompleted = true;
                completedDateTime = LocalDateTime.now();
            }
        }
    }

    /**
     * Decrements the current progress by one, but not below zero.
     * This method can be used to undo a recorded completion.
     */
    public void minusCurrentProgress() {
        if (currentProgress > 0) {
            currentProgress--;
        }
    }

    /**
     * Gets the GoalInfo object associated with this goal.
     *
     * @return The GoalInfo object.
     */
    @Override
    public GoalInfo getGoalInfo() {
        return goalInfo;
    }

    /**
     * Gets the BeginAndDueDates object for this goal.
     *
     * @return The BeginAndDueDates object.
     */
    @Override
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    /**
     * Gets the time period (WEEK or MONTH) of this goal.
     *
     * @return The TimePeriod of the goal.
     */
    @Override
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    /**
     * Gets the required number of task completions (frequency) for this goal.
     *
     * @return The frequency as an int.
     */
    @Override
    public int getFrequency() {
        return frequency;
    }

    /**
     * Gets the current progress of the goal.
     *
     * @return The current progress as an int.
     */
    @Override
    public int getCurrentProgress() {
        return currentProgress;
    }

    /**
     * Checks if the goal is completed.
     *
     * @return {@code true} if the goal is completed, {@code false} otherwise.
     */
    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Gets the date and time when the goal was completed.
     *
     * @return The LocalDateTime of completion, or null if not yet completed.
     */
    @Override
    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    /**
     * Gets the general Info object from the GoalInfo.
     *
     * @return The Info object.
     */
    @Override
    public Info getInfo() {
        return goalInfo.getInfo();
    }

    /**
     * Gets the Info object for the target task from the GoalInfo.
     *
     * @return The Info object for the target task.
     */
    @Override
    public Info getTargetTaskInfo() {
        return goalInfo.getTargetTaskInfo();
    }

    /**
     * Checks if the goal is currently available for completion.
     * A goal is considered available if it's not completed and the current date
     * is within its begin and due dates.
     *
     * @return {@code true} if the goal is available, {@code false} otherwise.
     */
    public boolean isAvailable() {
        final LocalDate today = LocalDate.now();
        return !isCompleted
                && !today.isBefore(beginAndDueDates.getBeginDate())
                && !today.isAfter(beginAndDueDates.getDueDate());
    }

    /**
     * Sets the frequency of required task completions.
     *
     * @param frequency The new frequency value; must not be negative.
     * @throws IllegalArgumentException if frequency is negative.
     */
    public void setFrequency(int frequency) {
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative.");
        }
        this.frequency = frequency;
    }

    /**
     * Sets the current progress of this goal.
     *
     * @param currentProgress The new current progress; must not be negative.
     * @throws IllegalArgumentException if currentProgress is negative.
     */
    public void setCurrentProgress(int currentProgress) {
        if (currentProgress < 0) {
            throw new IllegalArgumentException("Current progress cannot be negative.");
        }
        this.currentProgress = currentProgress;
    }

    /**
     * Manually marks this goal as completed or not.
     *
     * @param completed Whether the goal is completed.
     */
    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
        if (!completed) {
            this.completedDateTime = null;
        }
        else if (this.completedDateTime == null) {
            this.completedDateTime = LocalDateTime.now();
        }
    }

    /**
     * Manually sets the completed date and time.
     *
     * @param completedDateTime The date and time when the goal was completed; can be null.
     */
    public void setCompletedDateTime(LocalDateTime completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    /**
     * Provides a string representation of the goal, including its name, time period, progress, and date range.
     *
     * @return A formatted string representation of the goal.
     */
    @Override
    public String toString() {
        return String.format("%s - %s (Progress: %d/%d, %s to %s)",
                goalInfo.getInfo().getName(),
                timePeriod.toString(),
                currentProgress,
                frequency,
                beginAndDueDates.getBeginDate(),
                beginAndDueDates.getDueDate());
    }

    /**
     * Returns the progress in simple "current/target" format.
     *
     * @return A string in "currentProgress/frequency" format (e.g., "2/200").
     */
    public String getSimpleProgress() {
        return String.format("%d/%d", currentProgress, frequency);
    }
}
