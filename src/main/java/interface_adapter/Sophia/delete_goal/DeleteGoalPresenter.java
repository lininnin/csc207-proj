package interface_adapter.Sophia.delete_goal;

import use_case.goalManage.delete_goal.DeleteGoalOutputBoundary ;
import use_case.goalManage.delete_goal.DeleteGoalOutputData;

public class DeleteGoalPresenter implements DeleteGoalOutputBoundary {
    @Override
    public void prepareConfirmationView(DeleteGoalOutputData outputData) {
        // Show confirmation dialog in UI
        System.out.println(outputData.getMessage()); // Replace with actual UI call
    }

    @Override
    public void prepareSuccessView(DeleteGoalOutputData outputData) {
        // Show success message in UI
        System.out.println(outputData.getMessage()); // Replace with actual UI call
    }

    @Override
    public void prepareFailView(String error) {
        // Show error message in UI
        System.err.println(error); // Replace with actual UI call
    }
}