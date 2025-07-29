package use_case.create_goal;

import entity.Sophia.Goal;

/**
 * Input data for creating a goal, containing all required fields.
 */
public class CreateGoalInputData {
    private final String name;
    private final String description;
    private final String category;
    private final String targetTask;
    private final int frequency;
    private final Goal.TimePeriod timePeriod;

    public CreateGoalInputData(String name, String description, String category,
                               String targetTask, int frequency, Goal.TimePeriod timePeriod) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.targetTask = targetTask;
        this.frequency = frequency;
        this.timePeriod = timePeriod;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getTargetTask() { return targetTask; }
    public int getFrequency() { return frequency; }
    public Goal.TimePeriod getTimePeriod() { return timePeriod; }
}