package use_case.goalManage.today_goal;

import entity.Sophia.Goal;
import use_case.repository.GoalRepository;
import use_case.goalManage.today_goal.TodayGoalsOutputBoundary.OperationType;

/**
 * Handles both adding to and removing from Today's Goals
 * Single Responsibility: Manages Today's Goals list operations
 */
public class TodayGoalInteractor implements TodayGoalsInputBoundary {
    private final GoalRepository goalRepository;
    private final TodayGoalsOutputBoundary outputBoundary;

    public TodayGoalInteractor(GoalRepository goalRepository,
                               TodayGoalsOutputBoundary outputBoundary) {
        this.goalRepository = goalRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void addToToday(TodayGoalInputData inputData) {
        try {
            Goal goal = goalRepository.findByName(inputData.getGoalName())
                    .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

            if (goalRepository.isInCurrentGoals(goal)) {
                outputBoundary.prepareFailView(
                        OperationType.ADD,
                        "Goal is already in Today's list"
                );
                return;
            }

            goalRepository.addToCurrentGoals(goal);
            outputBoundary.prepareSuccessView(
                    OperationType.ADD,
                    new TodayGoalOutputData(goal.getInfo().getName())
            );

        } catch (Exception e) {
            outputBoundary.prepareFailView(OperationType.ADD, e.getMessage());
        }
    }

    @Override
    public void removeFromToday(TodayGoalInputData inputData) {
        try {
            Goal goal = goalRepository.findByName(inputData.getGoalName())
                    .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

            if (!inputData.isConfirmed()) {
                outputBoundary.prepareConfirmationView(
                        new TodayGoalOutputData(
                                goal.getInfo().getName(),
                                "Delete action is not undoable, continue?"
                        )
                );
                return;
            }

            goalRepository.removeFromCurrentGoals(goal);
            outputBoundary.prepareSuccessView(
                    OperationType.REMOVE,
                    new TodayGoalOutputData(goal.getInfo().getName())
            );

        } catch (Exception e) {
            outputBoundary.prepareFailView(OperationType.REMOVE, e.getMessage());
        }
    }
}