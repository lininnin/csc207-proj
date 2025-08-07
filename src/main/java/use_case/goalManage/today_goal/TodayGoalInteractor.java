package use_case.goalManage.today_goal;

import data_access.GoalRepository;
import entity.Sophia.Goal;
import java.util.List;

public class TodayGoalInteractor implements TodayGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final TodayGoalOutputBoundary presenter;

    public TodayGoalInteractor(GoalRepository goalRepository,
                               TodayGoalOutputBoundary presenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
    }

    @Override
    public void addToToday(TodayGoalInputData inputData) {
        try {
            goalRepository.addGoalToToday(inputData.getGoalName());
            List<Goal> updatedGoals = goalRepository.getTodayGoals();
            presenter.prepareSuccessView(new TodayGoalOutputData(updatedGoals));
        } catch (Exception e) {
            presenter.prepareFailView("Failed to add goal to today: " + e.getMessage());
        }
    }

    @Override
    public void removeFromToday(TodayGoalInputData inputData) {
        try {
            if (inputData.isConfirmed()) {
                goalRepository.removeGoalFromToday(inputData.getGoalName());
                List<Goal> updatedGoals = goalRepository.getTodayGoals();
                presenter.prepareSuccessView(new TodayGoalOutputData(updatedGoals));
            }
        } catch (Exception e) {
            presenter.prepareFailView("Failed to remove goal from today: " + e.getMessage());
        }
    }

    @Override
    public void execute() {
        try {
            List<Goal> currentGoals = goalRepository.getTodayGoals();
            presenter.prepareSuccessView(new TodayGoalOutputData(currentGoals));
        } catch (Exception e) {
            presenter.prepareFailView("Failed to load today's goals: " + e.getMessage());
        }
    }
}