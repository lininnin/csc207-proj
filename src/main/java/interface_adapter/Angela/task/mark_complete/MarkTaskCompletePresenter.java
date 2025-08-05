package interface_adapter.Angela.task.mark_complete;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import use_case.Angela.task.mark_complete.MarkTaskCompleteOutputBoundary;
import use_case.Angela.task.mark_complete.MarkTaskCompleteOutputData;

/**
 * Presenter for the mark task complete use case.
 * Updates the today tasks view model to reflect completion status changes.
 */
public class MarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {
    private final TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;

    public MarkTaskCompletePresenter(TodayTasksViewModel todayTasksViewModel) {
        this.todayTasksViewModel = todayTasksViewModel;
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }

    @Override
    public void presentSuccess(MarkTaskCompleteOutputData outputData, String message) {
        // Update the today tasks view model to trigger a refresh
        TodayTasksState currentState = todayTasksViewModel.getState();
        if (currentState == null) {
            currentState = new TodayTasksState();
        }
        
        // Set the success message
        currentState.setSuccessMessage(message);
        currentState.setError(null);
        
        // Trigger a refresh of the today's tasks view
        currentState.setRefreshNeeded(true);
        
        todayTasksViewModel.setState(currentState);
        todayTasksViewModel.firePropertyChanged();
        
        // Also refresh overdue tasks if the controller is set
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Last 7 days
        }
        
        System.out.println("DEBUG: MarkTaskCompletePresenter - Success: " + message);
    }

    @Override
    public void presentError(String error) {
        TodayTasksState currentState = todayTasksViewModel.getState();
        if (currentState == null) {
            currentState = new TodayTasksState();
        }
        
        currentState.setError(error);
        currentState.setSuccessMessage(null);
        
        todayTasksViewModel.setState(currentState);
        todayTasksViewModel.firePropertyChanged();
        
        System.out.println("DEBUG: MarkTaskCompletePresenter - Error: " + error);
    }
}