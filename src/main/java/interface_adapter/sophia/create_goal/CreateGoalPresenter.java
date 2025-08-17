package interface_adapter.sophia.create_goal;

import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.goalManage.create_goal.CreateGoalOutputBoundary;
import use_case.goalManage.create_goal.CreateGoalOutputData;

/**
 * Presenter class for handling the presentation logic of goal creation results.
 * Implements the CreateGoalOutputBoundary to handle success and error cases.
 */
public class CreateGoalPresenter implements CreateGoalOutputBoundary {
    private TodaySoFarController todaySoFarController;

    /**
     * Sets the TodaySoFarController for refreshing the Today panel after goal creation.
     * @param controller The TodaySoFarController instance to set
     */
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }

    /**
     * Handles successful goal creation by displaying the success message
     * and refreshing the Today So Far panel.
     * @param outputData Contains the success status message and created goal details
     */
    @Override
    public void presentSuccess(CreateGoalOutputData outputData) {
        // Update UI with success message
        System.out.println("Success: " + outputData.getStatusMessage());

        // Refresh Today So Far panel if controller is available
        if (todaySoFarController != null) {
            System.out.println("DEBUG: CreateGoalPresenter calling todaySoFarController.refresh()");
            todaySoFarController.refresh();
        }
        else {
            System.out.println("DEBUG: CreateGoalPresenter - todaySoFarController is null!");
        }
    }

    /**
     * Handles goal creation errors by displaying the error message.
     * @param error The error message to display
     */
    @Override
    public void presentError(String error) {
        // Show error in UI
        System.err.println("Error: " + error);
    }
}
