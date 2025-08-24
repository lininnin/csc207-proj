package interface_adapter.sophia.edit_todays_goal;

import use_case.goalManage.edit_todays_goal.EditTodaysGoalOutputBoundary;
import use_case.goalManage.edit_todays_goal.EditTodaysGoalOutputData;

/**
 * Presenter for handling the presentation logic of today's goal editing results.
 */
public class EditTodaysGoalPresenter implements EditTodaysGoalOutputBoundary {
    /**
     * Prepares the success view after successful goal editing.
     * @param outputData Contains the success message and updated goal details
     */
    @Override
    public void prepareSuccessView(EditTodaysGoalOutputData outputData) {
    }

    /**
     * Prepares the confirmation view before editing a goal.
     * @param outputData Contains the confirmation message
     */
    @Override
    public void prepareConfirmationView(EditTodaysGoalOutputData outputData) {
    }

    /**
     * Prepares the failure view when goal editing fails.
     * @param error The error message to display
     */
    @Override
    public void prepareFailView(String error) {
        System.err.println("ERROR: " + error);
    }
}
