package interface_adapter.Sophia.today_goal;

import use_case.goalManage.today_goal.TodayGoalsOutputBoundary;
import use_case.goalManage.today_goal.TodayGoalOutputData;
import use_case.goalManage.today_goal.TodayGoalsOutputBoundary.OperationType;

/**
 * Formats output for the UI
 */
public class TodayGoalPresenter implements TodayGoalsOutputBoundary {
    @Override
    public void prepareSuccessView(OperationType operation, TodayGoalOutputData outputData) {
        String message = operation == OperationType.ADD
                ? "Added to Today's Goals: " + outputData.getGoalName()
                : "Removed from Today's Goals: " + outputData.getGoalName();

        // Update UI (e.g., show toast, refresh lists)
        System.out.println("SUCCESS: " + message); // Replace with actual UI call
    }

    @Override
    public void prepareConfirmationView(TodayGoalOutputData outputData) {
        // Show confirmation dialog in UI
        System.out.println("CONFIRM: " + outputData.getMessage()); // Replace with UI dialog
    }

    @Override
    public void prepareFailView(OperationType operation, String error) {
        // Show error in UI
        System.err.println("ERROR (" + operation + "): " + error); // Replace with UI alert
    }
}