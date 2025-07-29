package use_case.goal_delete;

import entity.Sophia.Goal;
import interface_adapter.GoalRepository;

public class DeleteGoalInteractor implements DeleteGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final DeleteGoalOutputBoundary presenter;

    public DeleteGoalInteractor(GoalRepository goalRepository, DeleteGoalOutputBoundary presenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteGoalInputData inputData) {
        Goal goal = goalRepository.getGoalByName(inputData.getGoalName());
        if (goal == null) {
            presenter.presentError("Goal not found: " + inputData.getGoalName());
            return;
        }

        boolean wasInCurrent = goalRepository.getCurrentGoals().contains(goal);
        goalRepository.deleteGoal(inputData.getGoalName());

        presenter.presentSuccess(new DeleteGoalOutputData(
                inputData.getGoalName(),
                wasInCurrent
        ));
    }
}