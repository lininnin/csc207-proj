package use_case.goalManage.create_goal;

import java.time.LocalDate;

import entity.Angela.Task.Task;
import entity.Sophia.Goal;

/**
 * Input data for creating a goal, containing all required fields.
 * Serves as a container for all parameters needed to create a new goal.
 * This immutable data structure ensures all required goal creation parameters
 * are properly encapsulated and validated before goal creation.
 */
public class CreateGoalInputData {
    private final String goalName;
    private final String goalDescription;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Goal.TimePeriod timePeriod;
    private final int frequency;
    private final Task targetTask;

    /**
     * Constructs a new CreateGoalInputData with all required parameters.
     *
     * @param goalName Name of the goal (must not be null or empty)
     * @param goalDescription Description of the goal (may be empty)
     * @param startDate Start date of goal period (must be before endDate)
     * @param endDate End date of goal period (must be after startDate)
     * @param timePeriod Time period type enum (WEEK or MONTH)
     * @param frequency Target frequency (must be positive)
     * @param targetTask Associated task (may be null)
     */
    public CreateGoalInputData(String goalName, String goalDescription,
                               LocalDate startDate, LocalDate endDate,
                               Goal.TimePeriod timePeriod, int frequency, Task targetTask) {
        this.goalName = goalName;
        this.goalDescription = goalDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timePeriod = timePeriod;
        this.frequency = frequency;
        this.targetTask = targetTask;
    }

    /**
     * Gets the name of the goal.
     * @return The goal name
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Gets the description of the goal.
     * @return The goal description
     */
    public String getGoalDescription() {
        return goalDescription;
    }


    /**
     * Gets the start date of the goal period.
     * @return The start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of the goal period.
     * @return The end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Gets the associated target task.
     * @return The target task (may be null)
     */
    public Task getTargetTask() {
        return targetTask;
    }

    /**
     * Gets the time period type for the goal.
     * @return The time period (WEEK or MONTH)
     */
    public Goal.TimePeriod getTimePeriod() {
        return timePeriod;
    }

    /**
     * Gets the target frequency of completions.
     * @return The frequency value
     */
    public int getFrequency() {
        return frequency;
    }

}
