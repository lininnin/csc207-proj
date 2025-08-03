package use_case.goalManage.delete_goal;

import entity.Sophia.Goal;
import use_case.goalManage.GoalRepository;

/**
 * Handles the business logic for goal deletion
 */
public class DeleteGoalInteractor implements DeleteGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final DeleteGoalOutputBoundary outputBoundary;

    public DeleteGoalInteractor(GoalRepository goalRepository,
                                DeleteGoalOutputBoundary outputBoundary) {
        this.goalRepository = goalRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteGoalInputData inputData) {
        try {
            Goal goal = goalRepository.findByName(inputData.getGoalName())
                    .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

            boolean isCurrentGoal = goalRepository.isInCurrentGoals(goal);

            if (!inputData.isConfirmed()) {
                // Show confirmation if goal is in current goals
                if (isCurrentGoal) {
                    outputBoundary.prepareConfirmationView(
                            new DeleteGoalOutputData(
                                    inputData.getGoalName(),
                                    true,
                                    "This goal is also in Today's list. Deleting will remove it from both lists. Continue?"
                            )
                    );
                }
                return;
            }

            // Proceed with deletion
            goalRepository.deleteByName(inputData.getGoalName());

            outputBoundary.prepareSuccessView(
                    new DeleteGoalOutputData(
                            inputData.getGoalName(),
                            isCurrentGoal,
                            inputData.getGoalName() + " deleted successfully"
                    )
            );

        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to delete goal: " + e.getMessage());
        }
    }
}