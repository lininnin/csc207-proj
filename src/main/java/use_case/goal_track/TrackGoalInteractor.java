package use_case.goal_track;

import entity.Sophia.Goal;
import interface_adapter.GoalRepository;

public class TrackGoalInteractor implements TrackGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final TrackGoalOutputBoundary presenter;

    public TrackGoalInteractor(GoalRepository goalRepository, TrackGoalOutputBoundary presenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(TrackGoalInputData inputData) {
        Goal goal = goalRepository.getGoalByName(inputData.getGoalName());
        if (goal == null) {
            presenter.presentError("Goal not found: " + inputData.getGoalName());
            return;
        }

        if (inputData.isIncrement()) {
            goal.recordCompletion();
        } else {
            goal.minusCurrentProgress();
        }

        goalRepository.saveGoal(goal);
        presenter.presentSuccess(new TrackGoalOutputData(
                goal.getInfo().getName(),
                goal.getCurrentProgress(),
                goal.getFrequency(),
                goal.isCompleted()
        ));
    }
}