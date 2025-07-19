package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a goal that tracks progress on a target task over a specific time period (week or month).
 * Each goal has associated metadata (info), a date range, frequency requirements, and current progress tracking.
 *
 * TODO: Will be implemented in Sophia's story #1 for weekly/monthly goal tracking
 */
public class Goal {
    private final Info info;
    private final BeginAndDueDates beginAndDueDates;
    private final Task targetTask;
    private final TimePeriod timePeriod;
    private final int frequency;
    private final List<LocalDate> completionDates;
    private boolean isComplete;

    /**
     * Enum representing whether the goal is tracked weekly or monthly.
     */
    public enum TimePeriod {
        WEEK, MONTH
    }

    /**
     * Constructs a new Goal with the given metadata, time range, target task, time period, and frequency.
     *
     * @param info           Metadata about the goal (name, description, category)
     * @param dates          Begin and due dates for the goal
     * @param targetTask     The task to track progress against
     * @param timePeriod     The period for the goal (WEEK or MONTH)
     * @param frequency      The required number of task completions within the time period
     * @throws IllegalArgumentException if any required parameter is null or frequency is negative
     */
    public Goal(Info info, BeginAndDueDates dates, Task targetTask, TimePeriod timePeriod, int frequency) {
        if (info == null || dates == null || targetTask == null || timePeriod == null) {
            throw new IllegalArgumentException("Goal parameters cannot be null");
        }
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative");
        }
        this.info = info;
        this.beginAndDueDates = dates;
        this.targetTask = targetTask;
        this.timePeriod = timePeriod;
        this.frequency = frequency;
        this.completionDates = new ArrayList<>();
        this.isComplete = false;
    }

    /**
     * Records a task completion on the specified date.
     *
     * @param completionDate The date when the task was completed
     */
    public void recordCompletion(LocalDate completionDate) {
        if (completionDate != null) {
            completionDates.add(completionDate);
            if (completionDates.size() == frequency){
                this.isComplete = true;
            }
        }
    }

    /**
     * Calculates the current progress based on completions within the goal's date range.
     *
     * @return The number of completions within the active period
     */
    public int getCurrentProgress() {
        return (int) completionDates.stream()
                .filter(date -> !date.isBefore(beginAndDueDates.getBeginDate()) &&
                        (beginAndDueDates.getDueDate() == null || !date.isAfter(beginAndDueDates.getDueDate())))
                .count();
    }

    /**
     * Checks whether the goal has been achieved based on current progress and required frequency.
     *
     * @return true if current progress is greater than or equal to frequency; false otherwise
     */
    public boolean isGoalAchieved() {
        return getCurrentProgress() >= frequency;
    }

    /**
     * Returns a string showing the current progress toward the goal.
     *
     * @return A string like "Progress: 2/3"
     */
    public String getGoalStatus() {
        return "Progress: " + getCurrentProgress() + "/" + frequency;
    }

    /**
     * Returns the date range during which this goal is active.
     *
     * @return A BeginAndDueDates object representing the goal's active period
     */
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;

        return "Begin Date: " + beginAndDueDates.getBeginDate() + "\n"+ "Due Date: " + beginAndDueDates.getDueDate();
    }

    /**
     * Returns the time period type of the goal (week or month).
     *
     * @return The TimePeriod enum value
     */
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    /**
     * Returns the required number of task completions to achieve the goal.
     *
     * @return The frequency requirement
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Returns a defensive copy of the completion dates.
     * Todo maybe find a way to print better
     * @return List of completion dates
     */
    public List<LocalDate> getCompletionDates() {
        return new ArrayList<>(completionDates);
    }
    /**
     * Returns the metadata (info) associated with this goal.
     *
     * @return The Info object
     */
    public Info getInfo() {
        return info;
    }
    
    
    /**
     * Returns the task being tracked by this goal.
     *
     * @return The target Task
     */
    public Task getTargetTask() {
        return targetTask;
}