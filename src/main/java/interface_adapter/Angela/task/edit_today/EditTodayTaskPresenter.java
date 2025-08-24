package interface_adapter.Angela.task.edit_today;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.Angela.task.edit_today.EditTodayTaskOutputBoundary;
import use_case.Angela.task.edit_today.EditTodayTaskOutputData;

/**
 * Presenter for the edit today task use case.
 * Converts use case output to view model updates.
 */
public class EditTodayTaskPresenter implements EditTodayTaskOutputBoundary {
    private final EditTodayTaskViewModel editTodayTaskViewModel;
    private final TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;
    private TodaySoFarController todaySoFarController;

    /**
     * Constructs the presenter with its view models.
     * 
     * @param editTodayTaskViewModel The view model for edit today task state
     * @param todayTasksViewModel The view model for today's tasks list
     */
    public EditTodayTaskPresenter(EditTodayTaskViewModel editTodayTaskViewModel,
                                  TodayTasksViewModel todayTasksViewModel) {
        this.editTodayTaskViewModel = editTodayTaskViewModel;
        this.todayTasksViewModel = todayTasksViewModel;
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }

    @Override
    public void prepareSuccessView(EditTodayTaskOutputData outputData) {
        
        // Create success state
        EditTodayTaskState editState = new EditTodayTaskState();
        editState.setSuccessMessage(outputData.getMessage());
        editState.setError(null);
        editState.setEditingTaskId(null); // Clear editing state
        editState.setLastEditedTaskId(outputData.getTaskId());
        editTodayTaskViewModel.setState(editState);
        editTodayTaskViewModel.firePropertyChanged();

        // Trigger refresh of today's tasks view
        TodayTasksState todayState = todayTasksViewModel.getState();
        if (todayState == null) {
            todayState = new TodayTasksState();
        }
        todayState.setRefreshNeeded(true);
        todayTasksViewModel.setState(todayState);
        todayTasksViewModel.firePropertyChanged();
        
        // Also refresh overdue tasks if due date was edited
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Last 7 days
        }
        
        // Also refresh Today So Far panel since due dates might affect overdue status
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
        
    }

    @Override
    public void prepareFailView(String error) {
        
        // Create error state
        EditTodayTaskState state = new EditTodayTaskState();
        state.setError(error);
        state.setSuccessMessage(null);
        editTodayTaskViewModel.setState(state);
        editTodayTaskViewModel.firePropertyChanged();
        
        // Don't trigger refresh on error - keep edit dialog open
    }
}