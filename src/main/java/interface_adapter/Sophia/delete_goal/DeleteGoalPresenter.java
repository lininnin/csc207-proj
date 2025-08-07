// Updated DeleteGoalPresenter.java
package interface_adapter.Sophia.delete_goal;

import interface_adapter.Sophia.available_goals.AvailableGoalsViewModel;
import interface_adapter.Sophia.today_goal.TodayGoalsViewModel;
import use_case.goalManage.delete_goal.DeleteGoalOutputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalOutputData;

import javax.swing.*;

public class DeleteGoalPresenter implements DeleteGoalOutputBoundary {
    private final AvailableGoalsViewModel availableGoalsViewModel;
    private final TodayGoalsViewModel todayGoalsViewModel;

    public DeleteGoalPresenter(AvailableGoalsViewModel availableGoalsViewModel,
                               TodayGoalsViewModel todayGoalsViewModel) {
        this.availableGoalsViewModel = availableGoalsViewModel;
        this.todayGoalsViewModel = todayGoalsViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteGoalOutputData outputData) {
        String goalName = outputData.getGoalName();

        availableGoalsViewModel.removeGoalByName(goalName);
        availableGoalsViewModel.firePropertyChanged();

        todayGoalsViewModel.removeGoalByName(goalName);
        todayGoalsViewModel.firePropertyChanged();

        JOptionPane.showMessageDialog(
                null,
                "Successfully deleted goal: " + goalName,
                "Deletion Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void prepareFailView(String error) {
        JOptionPane.showMessageDialog(
                null,
                "Failed to delete goal: " + error,
                "Deletion Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void prepareConfirmationView(DeleteGoalOutputData outputData) {
        // Optional confirmation dialog (not used in automatic controller call)
        JOptionPane.showConfirmDialog(
                null,
                outputData.getMessage(),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );
    }
}
