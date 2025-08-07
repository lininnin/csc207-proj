package entity.Sophia;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a goal that tracks progress on a target task over a specific time period (week or month).
 * Each goal has associated metadata (goalInfo), a date range, frequency requirement, and progress tracking.
 *
 * TODO: Will be implemented in Sophia's story #1 for weekly/monthly goal tracking
 */
public class Goal implements goalInterface{

    private final GoalInfo goalInfo; // GoalInfo containing goal metadata and target task info
    private final BeginAndDueDates beginAndDueDates; // Begin and due dates of this goal
    private final TimePeriod timePeriod; // Time period of the goal (WEEK or MONTH)
    private int frequency; // Required number of task completions per period
    private int currentProgress; // Current number of task completions per period
    private boolean isCompleted; // Whether the goal is currently marked as done
    private LocalDateTime completedDateTime; // Optional completed datetime

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
        if (isCompleted) return;

        currentProgress++;
        if (currentProgress >= frequency) {
            isCompleted = true;
            completedDateTime = LocalDateTime.now();
        }
    }

    public void minusCurrentProgress() {
        if (currentProgress > 0){
            currentProgress--;
        }
    }

    // ------------------ Getters ------------------

    @Override
    public GoalInfo getGoalInfo() {
        return goalInfo;
    }

    @Override
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    @Override
    public int getFrequency() {
        return frequency;
    }

    @Override
    public int getCurrentProgress() {
        return currentProgress;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    @Override
    public Info getInfo() {
        return goalInfo.getInfo();
    }

    @Override
    public Info getTargetTaskInfo() {
        return goalInfo.getTargetTaskInfo();
    }

    public boolean isAvailable() {
        LocalDate today = LocalDate.now();
        return !isCompleted
                && !today.isBefore(beginAndDueDates.getBeginDate())
                && !today.isAfter(beginAndDueDates.getDueDate());
    }


    // ------------------ Setters ------------------

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
        } else if (this.completedDateTime == null) {
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
     * Returns the progress in simple "current/target" format
     * @return String in "currentProgress/frequency" format (e.g., "2/200")
     */
    public String getSimpleProgress() {
        return String.format("%d/%d", currentProgress, frequency);
    }



}


