package interface_adapter.Angela.task.edit_available;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayState;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
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
    private AddTaskToTodayViewModel addTaskToTodayViewModel;
    private TodaySoFarController todaySoFarController;

    public EditAvailableTaskPresenter(EditAvailableTaskViewModel editAvailableTaskViewModel,
                                      AvailableTasksViewModel availableTasksViewModel) {
        this.editAvailableTaskViewModel = editAvailableTaskViewModel;
        this.availableTasksViewModel = availableTasksViewModel;
    }

    public void setTodayTasksViewModel(TodayTasksViewModel todayTasksViewModel) {
        this.todayTasksViewModel = todayTasksViewModel;
    }
    
    public void setAddTaskToTodayViewModel(AddTaskToTodayViewModel addTaskToTodayViewModel) {
        this.addTaskToTodayViewModel = addTaskToTodayViewModel;
    }

    @Override
    public void prepareSuccessView(EditAvailableTaskOutputData outputData) {
        
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
        }
        
        // Also refresh overdue tasks if controller is available
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Refresh with 7 days
        }
        
        // CRITICAL: Also refresh Add to Today dropdown when task names change
        if (addTaskToTodayViewModel != null) {
            AddTaskToTodayState addToTodayState = addTaskToTodayViewModel.getState();
            if (addToTodayState == null) {
                addToTodayState = new AddTaskToTodayState();
            }
            addToTodayState.setRefreshNeeded(true);
            addTaskToTodayViewModel.setState(addToTodayState);
            addTaskToTodayViewModel.firePropertyChanged();
        }
        
        // Also refresh Today So Far panel when tasks are edited (especially category changes)
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
    }

    @Override
    public void prepareFailView(String error) {
        
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
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }
}