package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a goal that tracks progress on a target task over a specific time period (week or month).
 * It tracks completion dates toward achieving the goal frequency within the goal's date range.
 * Todo: If task not found, simulate user creating new task (dont think it should be part of this)
 * Todo: edit info
 */
public class Goal {
    private Info info;
    private BeginAndDueDates beginAndDueDates;
    private final Task targetTask;
    private TimePeriod timePeriod;
    private int frequency;
    private List<LocalDate> completionDates;

    /**
     * Enum representing whether the goal is tracked weekly or monthly.
     */
    public enum TimePeriod {
        WEEK, MONTH
    }

    /**
     * Constructs a new Goal with given parameters.
     *
     * @param info       Metadata about the goal (name, description, category)
     * @param dates      Begin and due dates for the goal
     * @param targetTask The task to track progress against
     * @param timePeriod The period for the goal (WEEK or MONTH)
     * @param frequency  Required number of completions within the time period
     * @throws IllegalArgumentException if any required parameter is null or frequency is negative
     */
    public Goal(Info info, BeginAndDueDates dates, Task targetTask, TimePeriod timePeriod, int frequency) {
        if (info == null || dates == null || timePeriod == null) {
            throw new IllegalArgumentException("Goal parameters cannot be null");
        }
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative");
        }
        this.info = info;
        this.beginAndDueDates = dates;
        this.targetTask = targetTask;
        targetTask.getManageTask().addGoal(this);
        this.timePeriod = timePeriod;
        this.frequency = frequency;
        this.completionDates = new ArrayList<>();
    }

    /**
     * Records a completion date toward this goal.
     *
     * @param completionDate The date when the task was completed
     */
    public void recordCompletion(LocalDate completionDate) {
        if (completionDate != null) {
            completionDates.add(completionDate);
        }
    }

    /**
     * Returns the number of completion dates within the active goal date range.
     *
     * @return Number of completions counted within goal period
     */
    public int getCurrentProgress() {
        return (int) completionDates.stream()
                .filter(date -> !date.isBefore(beginAndDueDates.getBeginDate()) &&
                        (beginAndDueDates.getDueDate() == null || !date.isAfter(beginAndDueDates.getDueDate())))
                .count();
    }

    /**
     * Returns true if the goal has been achieved based on current progress and frequency.
     *
     * @return true if current progress is greater than or equal to frequency; false otherwise
     */
    public boolean isGoalAchieved() {
        if (this.targetTask.isComplete()) {
            return true;
        } else {
            return getCurrentProgress() >= frequency;
        }
    }

    /**
     * Returns a string showing the current progress toward the goal.
     *
     * @return A string like "Progress: 2/3"
     */
    public int getGoalStatus() {
        return getCurrentProgress();
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
     * Returns the date range during which this goal is active.
     *
     * @return Begin and due dates of this goal
     */
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    /**
     * Returns the task being tracked by this goal.
     *
     * @return The target Task
     */
    public Task getTargetTask() {
        return targetTask;
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
     *
     * @return List of completion dates
     */
    public List<LocalDate> getCompletionDates() {
        return new ArrayList<>(completionDates);
    }

    /**
     * Resets the progress by clearing all recorded completion dates.
     */
    public void resetProgress() {
        completionDates.clear();
    }

   public List<LocalDate> getCompletions(){
        return new ArrayList<>(completionDates);
   }

}
