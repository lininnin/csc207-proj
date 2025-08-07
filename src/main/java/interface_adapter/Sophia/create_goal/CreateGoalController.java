package interface_adapter.Sophia.create_goal;

import use_case.goalManage.create_goal.CreateGoalInputBoundary;
import use_case.goalManage.create_goal.CreateGoalInputData;
import entity.Sophia.Goal;
import java.time.LocalDate;

public class CreateGoalController {
    private final CreateGoalInputBoundary createGoalUseCase;

    public CreateGoalController(CreateGoalInputBoundary createGoalUseCase) {
        this.createGoalUseCase = createGoalUseCase;
    }

    /**
     * Executes goal creation with all required parameters
     * @param goalName Name of the goal
     * @param goalDescription Description of the goal
     * @param targetAmount Target amount for the goal
     * @param currentAmount Current progress amount
     * @param startDate Start date of the goal period
     * @param endDate End date of the goal period
     * @param timePeriod Time period type (WEEK or MONTH)
     * @param frequency Target frequency of completions
     */
    public void execute(String goalName, String goalDescription,
                        double targetAmount, double currentAmount,
                        LocalDate startDate, LocalDate endDate,
                        Goal.TimePeriod timePeriod, int frequency) {
        CreateGoalInputData inputData = new CreateGoalInputData(
                goalName,
                goalDescription,
                targetAmount,
                currentAmount,
                startDate,
                endDate,
                timePeriod,
                frequency
        );
        createGoalUseCase.execute(inputData);
    }

    /**
     * Simplified version with default currentAmount (0.0)
     */
    public void execute(String goalName, String goalDescription,
                        double targetAmount, LocalDate startDate,
                        LocalDate endDate, Goal.TimePeriod timePeriod,
                        int frequency) {
        execute(goalName, goalDescription, targetAmount, 0.0,
                startDate, endDate, timePeriod, frequency);
    }
}