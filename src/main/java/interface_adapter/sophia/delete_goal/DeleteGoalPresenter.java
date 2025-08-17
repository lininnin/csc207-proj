package interface_adapter.sophia.delete_goal;

import javax.swing.JOptionPane;

import interface_adapter.sophia.available_goals.AvailableGoalsViewModel;
import interface_adapter.sophia.today_goal.TodayGoalsViewModel;
import use_case.goalManage.delete_goal.DeleteGoalOutputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalOutputData;

/**
 * Presenter for handling the presentation logic of goal deletion results.
 * Updates views and shows appropriate dialogs based on deletion outcomes.
 */
public class DeleteGoalPresenter implements DeleteGoalOutputBoundary {
    private final AvailableGoalsViewModel availableGoalsViewModel;
    private final TodayGoalsViewModel todayGoalsViewModel;

    /**
     * Constructs a DeleteGoalPresenter with the specified view models.
     * @param availableGoalsViewModel ViewModel for available goals
     * @param todayGoalsViewModel ViewModel for today's goals
     */
    public DeleteGoalPresenter(AvailableGoalsViewModel availableGoalsViewModel,
                               TodayGoalsViewModel todayGoalsViewModel) {
        this.availableGoalsViewModel = availableGoalsViewModel;
        this.todayGoalsViewModel = todayGoalsViewModel;
    }

    /**
     * Prepares the success view after goal deletion.
     * @param outputData Contains details of the deleted goal
     */
    @Override
    public void prepareSuccessView(DeleteGoalOutputData outputData) {
        final String goalName = outputData.getGoalName();

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

    /**
     * Prepares the failure view when goal deletion fails.
     * @param error The error message to display
     */
    @Override
    public void prepareFailView(String error) {
        JOptionPane.showMessageDialog(
                null,
                "Failed to delete goal: " + error,
                "Deletion Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Prepares the confirmation view before goal deletion.
     * @param outputData Contains the confirmation message
     */
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
