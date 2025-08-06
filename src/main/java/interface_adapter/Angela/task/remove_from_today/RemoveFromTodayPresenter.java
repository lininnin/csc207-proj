package interface_adapter.Angela.task.remove_from_today;

import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import use_case.Angela.task.remove_from_today.RemoveFromTodayOutputBoundary;
import use_case.Angela.task.remove_from_today.RemoveFromTodayOutputData;

/**
 * Presenter for the remove from today use case.
 */
public class RemoveFromTodayPresenter implements RemoveFromTodayOutputBoundary {
    private final TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;

    public RemoveFromTodayPresenter(TodayTasksViewModel todayTasksViewModel) {
        this.todayTasksViewModel = todayTasksViewModel;
    }

    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }
    
    @Override
    public void prepareSuccessView(RemoveFromTodayOutputData outputData) {
        TodayTasksState state = todayTasksViewModel.getState();
        state.setSuccessMessage(outputData.getMessage());
        state.setRefreshNeeded(true);
        todayTasksViewModel.setState(state);
        todayTasksViewModel.firePropertyChanged();
        
        // Also refresh overdue tasks if controller is available
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Refresh with 7 days
        }
    }

    @Override
    public void prepareFailView(String error) {
        TodayTasksState state = todayTasksViewModel.getState();
        state.setError(error);
        todayTasksViewModel.setState(state);
        todayTasksViewModel.firePropertyChanged();
    }
}