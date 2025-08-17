package interface_adapter.sophia.today_goal;

import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.goalManage.today_goal.TodayGoalOutputBoundary;
import use_case.goalManage.today_goal.TodayGoalOutputData;

/**
 * Presenter for handling the presentation logic of today's goals operations.
 */
public class TodayGoalPresenter implements TodayGoalOutputBoundary {
    private final TodayGoalsViewModel todayGoalsViewModel;

    private TodaySoFarController todaySoFarController;

    /**
     * Constructs a TodayGoalPresenter with the specified ViewModel.
     * @param todayGoalsViewModel The ViewModel for today's goals
     */
    public TodayGoalPresenter(TodayGoalsViewModel todayGoalsViewModel) {
        this.todayGoalsViewModel = todayGoalsViewModel;
    }

    /**
     * Sets the TodaySoFarController for refreshing the Today panel.
     * @param controller The TodaySoFarController instance to set
     */
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }

    /**
     * Prepares the success view after a successful operation.
     * @param outputData Contains the updated list of today's goals
     */
    @Override
    public void prepareSuccessView(TodayGoalOutputData outputData) {
        final TodaysGoalsState currentState = todayGoalsViewModel.getState();
        currentState.setTodayGoals(outputData.getTodayGoals());
        todayGoalsViewModel.setState(currentState);
        todayGoalsViewModel.firePropertyChanged();

        // Refresh Today So Far panel if controller is available
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
    }

    /**
     * Prepares the failure view when an operation fails.
     * @param error The error message to display
     */
    @Override
    public void prepareFailView(String error) {
        System.err.println("Operation failed: " + error);
    }
}
