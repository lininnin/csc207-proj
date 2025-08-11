package interface_adapter.Sophia.delete_goal;

import use_case.goalManage.delete_goal.DeleteGoalInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputData;

public class DeleteGoalController {
    private final DeleteGoalInputBoundary deleteGoalUseCase;
    private DeleteGoalPresenter presenter;

    public DeleteGoalController(DeleteGoalInputBoundary deleteGoalUseCase) {
        this.deleteGoalUseCase = deleteGoalUseCase;
    }

    // Inject presenter reference here
    public void setPresenter(DeleteGoalPresenter presenter) {
        this.presenter = presenter;
    }

    public void execute(String goalName) {
        DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, false);
        deleteGoalUseCase.execute(inputData);
    }

    public void executeConfirmedDeletion(String goalName) {
        DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, true);
        deleteGoalUseCase.execute(inputData);
    }
}
