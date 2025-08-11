package use_case.goalManage.today_goal;

import data_access.GoalRepository;
import entity.Sophia.Goal;
import java.util.List;
import java.util.Optional;

public class TodayGoalInteractor implements TodayGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final TodayGoalOutputBoundary outputBoundary;

    public TodayGoalInteractor(GoalRepository goalRepository,
                               TodayGoalOutputBoundary outputBoundary) {
        this.goalRepository = goalRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute() {
        try {
            List<Goal> todayGoals = goalRepository.getTodayGoals();
            TodayGoalOutputData outputData = new TodayGoalOutputData(todayGoals);
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            outputBoundary.prepareFailView("Error loading today's goals: " + e.getMessage());
        }
    }

    @Override
    public void addToToday(TodayGoalInputData inputData) {
        try {
            String goalName = inputData.getGoalName();

            // Check if goal already in today's goals to avoid duplicates
            List<Goal> todayGoals = goalRepository.getTodayGoals();
            boolean alreadyAdded = todayGoals.stream()
                    .anyMatch(goal -> goal.getGoalInfo().getInfo().getName().equals(goalName));

            if (!alreadyAdded) {
                goalRepository.addGoalToToday(goalName);
            }
            execute(); // Refresh the view
        } catch (Exception e) {
            outputBoundary.prepareFailView("Error adding goal: " + e.getMessage());
        }
    }

    @Override
    public void removeFromToday(TodayGoalInputData inputData) {
        try {
            goalRepository.removeGoalFromToday(inputData.getGoalName());
            execute(); // Refresh the view
        } catch (Exception e) {
            outputBoundary.prepareFailView("Error removing goal: " + e.getMessage());
        }
    }

    @Override
    public void updateProgress(TodayGoalInputData inputData) {
        try {
            Optional<Goal> optGoal = goalRepository.findByName(inputData.getGoalName());
            if (optGoal.isEmpty()) {
                throw new Exception("Goal not found");
            }
            Goal goal = optGoal.get();

            double newAmount = inputData.getNewAmount();
            if (newAmount % 1 == 0) {
                goal.setCurrentProgress((int) newAmount);
            } else {
                goal.setCurrentProgress((int) Math.round(newAmount));
            }

            goalRepository.save(goal);
            execute(); // Refresh the view
        } catch (Exception e) {
            outputBoundary.prepareFailView("Error updating progress: " + e.getMessage());
        }
    }
}
