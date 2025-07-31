package interface_adapter.Sophia.edit_todays_goal;

import use_case.goalManage.edit_todays_goal.EditTodaysGoalOutputBoundary;
import use_case.goalManage.edit_todays_goal.EditTodaysGoalOutputData;

public class EditTodaysGoalPresenter implements EditTodaysGoalOutputBoundary {
    @Override
    public void prepareSuccessView(EditTodaysGoalOutputData outputData) {
        System.out.println("SUCCESS: " + outputData.getMessage()); // Replace with UI update
    }

    @Override
    public void prepareConfirmationView(EditTodaysGoalOutputData outputData) {
        System.out.println("CONFIRM: " + outputData.getMessage()); // Replace with UI dialog
    }

    @Override
    public void prepareFailView(String error) {
        System.err.println("ERROR: " + error); // Replace with UI alert
    }
}