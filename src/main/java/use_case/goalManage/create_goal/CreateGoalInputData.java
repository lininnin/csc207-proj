package use_case.goalManage.create_goal;

import entity.Angela.Task.Task;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import java.time.LocalDate;

/**
 * Input data for creating a goal, containing all required fields.
 */
public class CreateGoalInputData {
    private final String goalName;
    private final String goalDescription;
    private final double targetAmount;
    private final double currentAmount;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Goal.TimePeriod timePeriod;  // Kept from original
    private final int frequency;  // Kept from original
    private final Task targetTask;

    public CreateGoalInputData(String goalName, String goalDescription,
                               double targetAmount, double currentAmount,
                               LocalDate startDate, LocalDate endDate,
                               Goal.TimePeriod timePeriod, int frequency, Task targetTask) {
        this.goalName = goalName;
        this.goalDescription = goalDescription;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timePeriod = timePeriod;
        this.frequency = frequency;
        this.targetTask = targetTask;
    }

    // Getters that match goalFactory.create() parameters
    public String getGoalName() { return goalName; }
    public String getGoalDescription() { return goalDescription; }
    public double getTargetAmount() { return targetAmount; }
    public double getCurrentAmount() { return currentAmount; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Task getTargetTask() {
        return targetTask;
    }

    // Additional getters from original
    public Goal.TimePeriod getTimePeriod() { return timePeriod; }
    public int getFrequency() { return frequency; }
}