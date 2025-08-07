package use_case.goalManage.available_goals;

import entity.Sophia.Goal;
import data_access.GoalRepository;

import java.util.List;

public class AvailableGoalsInteractor implements AvailableGoalsInputBoundary {
    private final GoalRepository goalRepository;
    private final AvailableGoalsOutputBoundary presenter;

    public AvailableGoalsInteractor(GoalRepository goalRepository,
                                    AvailableGoalsOutputBoundary presenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        List<Goal> availableGoals = goalRepository.findAvailableGoals();
        AvailableGoalsOutputData outputData = new AvailableGoalsOutputData(availableGoals);
        presenter.presentAvailableGoals(outputData);
    }
}