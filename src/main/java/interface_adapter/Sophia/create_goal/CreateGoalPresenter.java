package interface_adapter.Sophia.create_goal;

import use_case.goalManage.create_goal.CreateGoalOutputBoundary;
import use_case.goalManage.create_goal.CreateGoalOutputData;

public class CreateGoalPresenter implements CreateGoalOutputBoundary {
    @Override
    public void presentSuccess(CreateGoalOutputData outputData) {
        // Update UI with success message
        System.out.println("Success: " + outputData.getStatusMessage());
    }

    @Override
    public void presentError(String error) {
        // Show error in UI
        System.err.println("Error: " + error);
    }
}