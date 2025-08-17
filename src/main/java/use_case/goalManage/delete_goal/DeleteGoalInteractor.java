package use_case.goalManage.delete_goal;

import entity.Sophia.Goal;
import data_access.GoalRepository;

import java.util.Optional;

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
                    this.outputBoundary.prepareConfirmationView(
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

            this.outputBoundary.prepareSuccessView(
                    new DeleteGoalOutputData(
                            inputData.getGoalName(),
                            isCurrentGoal,
                            inputData.getGoalName() + " deleted successfully"
                    )
            );

        } catch (Exception e) {
            this.outputBoundary.prepareFailView("Failed to delete goal: " + e.getMessage());
        }
    }

    public void execute(String goalName) {
        try {
            // Check if goal exists
            Optional<Goal> goalOptional = goalRepository.findByName(goalName);

            if (!goalOptional.isPresent()) {
                this.outputBoundary.prepareFailView("Goal not found: " + goalName);
                return;
            }

            Goal goal = goalOptional.get();
            boolean wasInCurrentGoals = goalRepository.isInCurrentGoals(goal);

            // Prepare confirmation data
            DeleteGoalOutputData confirmationData = new DeleteGoalOutputData(
                    goalName,
                    wasInCurrentGoals,
                    "Are you sure you want to delete '" + goalName + "'?" +
                            (wasInCurrentGoals ? "\nThis goal is currently active!" : "")
            );

            // Show confirmation
            this.outputBoundary.prepareConfirmationView(confirmationData);

        } catch (Exception e) {
            this.outputBoundary.prepareFailView("Error preparing deletion: " + e.getMessage());
        }
    }

    public void executeConfirmedDeletion(String goalName) {
        try {
            // Check if goal exists first
            Optional<Goal> goalOptional = goalRepository.findByName(goalName);

            if (!goalOptional.isPresent()) {
                this.outputBoundary.prepareFailView("Goal no longer exists: " + goalName);
                return;
            }

            Goal goal = goalOptional.get();
            boolean wasInCurrentGoals = goalRepository.isInCurrentGoals(goal);

            // Perform deletion
            goalRepository.deleteByName(goalName);

            // Prepare success data
            DeleteGoalOutputData successData = new DeleteGoalOutputData(
                    goalName,
                    wasInCurrentGoals,
                    "Successfully deleted goal: " + goalName +
                            (wasInCurrentGoals ? " (removed from current goals)" : "")
            );

            this.outputBoundary.prepareSuccessView(successData);

        } catch (Exception e) {
            this.outputBoundary.prepareFailView("Error deleting goal: " + e.getMessage());
        }
    }
}