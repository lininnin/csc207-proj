package use_case.goalManage.delete_goal;

import java.util.Optional;

import data_access.GoalRepository;
import entity.Sophia.Goal;

/**
 * Interactor implementation for goal deletion.
 * Handles the business logic of deleting goals.
 */
public class DeleteGoalInteractor implements DeleteGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final DeleteGoalOutputBoundary outputBoundary;

    /**
     * Constructs a DeleteGoalInteractor with dependencies.
     * @param goalRepository Repository for goal data access
     * @param outputBoundary Presenter for handling output
     */
    public DeleteGoalInteractor(GoalRepository goalRepository,
                                DeleteGoalOutputBoundary outputBoundary) {
        this.goalRepository = goalRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteGoalInputData inputData) {
        try {
            final Goal goal = goalRepository.findByName(inputData.getGoalName())
                    .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

            final boolean isCurrentGoal = goalRepository.isInCurrentGoals(goal);

            if (!inputData.isConfirmed() && isCurrentGoal) {
                this.outputBoundary.prepareConfirmationView(
                        new DeleteGoalOutputData(
                                inputData.getGoalName(),
                                true,
                                "This goal is also in Today's list. Deleting will "
                                        + "remove it from both lists. Continue?"
                        )
                );
            }
            else if (inputData.isConfirmed()) {
                goalRepository.deleteByName(inputData.getGoalName());
                this.outputBoundary.prepareSuccessView(
                        new DeleteGoalOutputData(
                                inputData.getGoalName(),
                                isCurrentGoal,
                                inputData.getGoalName() + " deleted successfully"
                        )
                );
            }
        }
        catch (IllegalArgumentException ex) {
            this.outputBoundary.prepareFailView("Failed to delete goal: " + ex.getMessage());
        }
    }

    /**
     * Prepares a goal deletion confirmation.
     * @param goalName Name of the goal to delete
     */
    public void execute(String goalName) {
        try {
            final Optional<Goal> goalOptional = goalRepository.findByName(goalName);

            if (goalOptional.isPresent()) {
                final Goal goal = goalOptional.get();
                final boolean wasInCurrentGoals = goalRepository.isInCurrentGoals(goal);

                // Build confirmation message without inline condition
                String confirmationMessage = "Are you sure you want to delete '" + goalName + "'?";
                if (wasInCurrentGoals) {
                    confirmationMessage += "\nThis goal is currently active!";
                }

                final DeleteGoalOutputData confirmationData = new DeleteGoalOutputData(
                        goalName,
                        wasInCurrentGoals,
                        confirmationMessage
                );

                this.outputBoundary.prepareConfirmationView(confirmationData);
            }
            else {
                this.outputBoundary.prepareFailView("Goal not found: " + goalName);
            }
        }
        catch (IllegalArgumentException ex) {
            this.outputBoundary.prepareFailView("Error preparing deletion: " + ex.getMessage());
        }
    }

    /**
     * Executes a confirmed goal deletion.
     * @param goalName Name of the goal to delete
     */
    public void executeConfirmedDeletion(String goalName) {
        try {
            final Optional<Goal> goalOptional = goalRepository.findByName(goalName);

            if (goalOptional.isPresent()) {
                final Goal goal = goalOptional.get();
                final boolean wasInCurrentGoals = goalRepository.isInCurrentGoals(goal);

                goalRepository.deleteByName(goalName);

                // Build success message without inline condition
                String successMessage = "Successfully deleted goal: " + goalName;
                if (wasInCurrentGoals) {
                    successMessage += " (removed from current goals)";
                }

                final DeleteGoalOutputData successData = new DeleteGoalOutputData(
                        goalName,
                        wasInCurrentGoals,
                        successMessage
                );

                this.outputBoundary.prepareSuccessView(successData);
            }
            else {
                this.outputBoundary.prepareFailView("Goal no longer exists: " + goalName);
            }
        }
        catch (IllegalArgumentException ex) {
            this.outputBoundary.prepareFailView("Error deleting goal: " + ex.getMessage());
        }
    }
}
