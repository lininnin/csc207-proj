package interface_adapter.Sophia.delete_goal;

import use_case.goalManage.delete_goal.DeleteGoalOutputBoundary ;
import use_case.goalManage.delete_goal.DeleteGoalOutputData;

import javax.swing.*;

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

    public void showConfirmationDialog(String goalName, int currentProgress, int targetProgress) {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this goal?\n\n" +
                        "Goal: " + goalName + "\n" +
                        "Progress: " + currentProgress + "/" + targetProgress,
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            // This would trigger the actual deletion through your controller
            onDeletionConfirmed(goalName);
        }
    }

    public void showSuccessMessage(String goalName) {
        JOptionPane.showMessageDialog(
                null,
                "Successfully deleted goal: " + goalName,
                "Deletion Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void showErrorMessage(String error) {
        JOptionPane.showMessageDialog(
                null,
                "Failed to delete goal: " + error,
                "Deletion Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void onDeletionConfirmed(String goalName) {
        // This would be connected to your controller
        System.out.println("Deletion confirmed for: " + goalName);
    }
}
