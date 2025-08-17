package interface_adapter.sophia.delete_goal;

import use_case.goalManage.delete_goal.DeleteGoalInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputData;

/**
 * Controller for handling goal deletion operations.
 * Orchestrates the deletion process between the view and use case layers.
 */
public class DeleteGoalController {
    private final DeleteGoalInputBoundary deleteGoalUseCase;
    private DeleteGoalPresenter presenter;

    /**
     * Constructs a DeleteGoalController with the specified use case interactor.
     * @param deleteGoalUseCase The goal deletion use case interactor
     */
    public DeleteGoalController(DeleteGoalInputBoundary deleteGoalUseCase) {
        this.deleteGoalUseCase = deleteGoalUseCase;
    }

    /**
     * Sets the presenter for handling deletion results.
     * @param presenter The DeleteGoalPresenter to set
     */
    public void setPresenter(DeleteGoalPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Initiates goal deletion.
     * @param goalName Name of the goal to delete
     */
    public void execute(String goalName) {
        final DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, false);
        deleteGoalUseCase.execute(inputData);
    }

    /**
     * Executes confirmed goal deletion.
     * @param goalName Name of the goal to delete
     */
    public void executeConfirmedDeletion(String goalName) {
        final DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, true);
        deleteGoalUseCase.execute(inputData);
    }
}
