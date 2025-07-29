package interface_adapter.Sophia.delete_goal;

import use_case.goalManage.delete_goal.DeleteGoalInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputData;

public class DeleteGoalController {
    private final DeleteGoalInputBoundary deleteGoalUseCase;

    public DeleteGoalController(DeleteGoalInputBoundary deleteGoalUseCase) {
        this.deleteGoalUseCase = deleteGoalUseCase;
    }

    public void execute(String goalName, boolean confirmed) {
        DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, confirmed);
        deleteGoalUseCase.execute(inputData);
    }
}