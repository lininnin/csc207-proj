package interface_adapter.sophia.create_goal;

import java.time.LocalDate;

import entity.Angela.Task.Task;
import entity.Sophia.Goal;
import use_case.goalManage.create_goal.CreateGoalInputBoundary;
import use_case.goalManage.create_goal.CreateGoalInputData;

/**
 * Controller for handling goal creation in the system.
 * Orchestrates the creation of new goals by converting user input
 * into the appropriate format for the use case.
 */
public class CreateGoalController {
    private final CreateGoalInputBoundary createGoalUseCase;

    /**
     * Constructs a CreateGoalController with the specified use case interactor.
     * @param createGoalUseCase The goal creation use case interactor
     */
    public CreateGoalController(CreateGoalInputBoundary createGoalUseCase) {
        this.createGoalUseCase = createGoalUseCase;
    }

    /**
     * Executes goal creation with all required parameters.
     * @param goalName Name of the goal
     * @param goalDescription Description of the goal
     * @param targetAmount Target amount for the goal
     * @param currentAmount Current progress amount
     * @param startDate Start date of the goal period
     * @param endDate End date of the goal period
     * @param timePeriod Time period type (WEEK or MONTH)
     * @param frequency Target frequency of completions
     * @param targetTask The target task associated with the goal
     */
    public void execute(String goalName, String goalDescription,
                        double targetAmount, double currentAmount,
                        LocalDate startDate, LocalDate endDate,
                        Goal.TimePeriod timePeriod, int frequency, Task targetTask) {
        final CreateGoalInputData inputData = new CreateGoalInputData(
                goalName,
                goalDescription,
                targetAmount,
                currentAmount,
                startDate,
                endDate,
                timePeriod,
                frequency,
                targetTask
        );
        createGoalUseCase.execute(inputData);
    }
}
