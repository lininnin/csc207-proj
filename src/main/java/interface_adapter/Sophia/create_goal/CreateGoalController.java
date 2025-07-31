package interface_adapter.Sophia.create_goal;

import entity.Sophia.Goal;
import use_case.create_goal.CreateGoalInputBoundary;
import use_case.create_goal.CreateGoalInputData;

public class CreateGoalController {
    private final CreateGoalInputBoundary createGoalUseCase;

    public CreateGoalController(CreateGoalInputBoundary createGoalUseCase) {
        this.createGoalUseCase = createGoalUseCase;
    }

    public void execute(String name, String description, String category,
                        String targetTask, int frequency, String timePeriod) {
        CreateGoalInputData inputData = new CreateGoalInputData(
                name, description, category, targetTask, frequency,
                Goal.TimePeriod.valueOf(timePeriod.toUpperCase())
        );
        createGoalUseCase.execute(inputData);
    }
}