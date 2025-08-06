package interface_adapter.Angela.task.edit_available;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import use_case.Angela.task.edit_available.EditAvailableTaskOutputBoundary;
import use_case.Angela.task.edit_available.EditAvailableTaskOutputData;

/**
 * Presenter for the edit available task use case.
 * Converts use case output to view model updates.
 */
public class EditAvailableTaskPresenter implements EditAvailableTaskOutputBoundary {
    private final EditAvailableTaskViewModel editAvailableTaskViewModel;
    private final AvailableTasksViewModel availableTasksViewModel;
    private TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;

    public EditAvailableTaskPresenter(EditAvailableTaskViewModel editAvailableTaskViewModel,
                                      AvailableTasksViewModel availableTasksViewModel) {
        this.editAvailableTaskViewModel = editAvailableTaskViewModel;
        this.availableTasksViewModel = availableTasksViewModel;
    }

    public void setTodayTasksViewModel(TodayTasksViewModel todayTasksViewModel) {
        this.todayTasksViewModel = todayTasksViewModel;
        System.out.println("DEBUG: EditAvailableTaskPresenter - TodayTasksViewModel set: " + (todayTasksViewModel != null));
    }

    @Override
    public void prepareSuccessView(EditAvailableTaskOutputData outputData) {
        System.out.println("DEBUG: EditAvailableTaskPresenter.prepareSuccessView - message: " + outputData.getMessage());
        
        // Create a new state object (don't assume one exists)
        EditAvailableTaskState editState = new EditAvailableTaskState();
        editState.setSuccessMessage(outputData.getMessage());
        editState.setError(null);
        editState.setEditingTaskId(null); // Clear editing state
        editAvailableTaskViewModel.setState(editState);
        editAvailableTaskViewModel.firePropertyChanged();

        // Trigger refresh of available tasks view
        AvailableTasksState tasksState = availableTasksViewModel.getState();
        if (tasksState == null) {
            tasksState = new AvailableTasksState();
        }
        tasksState.setRefreshNeeded(true);
        availableTasksViewModel.setState(tasksState);
        availableTasksViewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Also trigger refresh of Today's Tasks if todayTasksViewModel is available
        if (todayTasksViewModel != null) {
            TodayTasksState todayState = todayTasksViewModel.getState();
            if (todayState == null) {
                todayState = new TodayTasksState();
            }
            todayState.setRefreshNeeded(true);
            todayTasksViewModel.setState(todayState);
            todayTasksViewModel.firePropertyChanged();
            System.out.println("DEBUG: Triggered Today's Tasks refresh after edit");
        }
        
        // Also refresh overdue tasks if controller is available
        if (overdueTasksController != null) {
            System.out.println("DEBUG [EditAvailableTaskPresenter]: Refreshing overdue tasks after edit");
            overdueTasksController.execute(7); // Refresh with 7 days
        }
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println("DEBUG: EditAvailableTaskPresenter.prepareFailView - error: " + error);
        
        // Create a new state object (don't assume one exists)
        EditAvailableTaskState state = new EditAvailableTaskState();
        state.setError(error);
        state.setSuccessMessage(null);
        editAvailableTaskViewModel.setState(state);
        editAvailableTaskViewModel.firePropertyChanged();
        
        // Don't trigger refresh on error - keep edit mode
        // The view should stay in edit mode so user can fix the error
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }
}