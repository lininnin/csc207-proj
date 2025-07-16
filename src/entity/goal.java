import java.time.LocalDate;

/**
 * Represents a goal that tracks progress on a target task over a specific time period (week or month).
 * Each goal has associated metadata (info), a date range, frequency requirements, and current progress tracking.
 */
public class Goal {
    private final Info info;
    private final BeginAndDueDates beginAndDueDates;
    private final Task targetTask;
    private final int frequency;
    private int currentProgress;

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
     */
    public Goal(Info info, BeginAndDueDates dates, Task targetTask, TimePeriod timePeriod, int frequency) {
        this.info = info;
        this.beginAndDueDates = dates;
        this.targetTask = targetTask;
        this.timePeriod = timePeriod;
        this.frequency = frequency;
        this.currentProgress = 0;
    }

    /**
     * Increments the current progress counter by 1.
     */
    public void incrementProgress() {
        currentProgress++;
    }

    /**
     * Checks whether the goal has been achieved based on current progress and required frequency.
     *
     * @return true if current progress is greater than or equal to frequency; false otherwise
     */
    public boolean isGoalAchieved() {
        return currentProgress >= frequency;
    }

    /**
     * Returns a string showing the current progress toward the goal.
     *
     * @return A string like "Progress: 2/3"
     */
    public String getGoalStatus() {
        return "Progress: " + currentProgress + "/" + frequency;
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
     * @return A BeginAndDueDates object representing the goal's active period
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
     * Returns the current number of task completions toward this goal.
     *
     * @return The current progress count
     */
    public int getCurrentProgress() {
        return currentProgress;
    }
}

