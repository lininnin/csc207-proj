package interface_adapter.Angela.task.delete;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import use_case.Angela.task.delete.DeleteTaskOutputBoundary;
import use_case.Angela.task.delete.DeleteTaskOutputData;

/**
 * Presenter for the delete task use case.
 */
public class DeleteTaskPresenter implements DeleteTaskOutputBoundary {
    private final AvailableTasksViewModel availableTasksViewModel;
    private final DeleteTaskViewModel deleteTaskViewModel;
    private TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;

    public DeleteTaskPresenter(AvailableTasksViewModel availableTasksViewModel,
                               DeleteTaskViewModel deleteTaskViewModel) {
        this.availableTasksViewModel = availableTasksViewModel;
        this.deleteTaskViewModel = deleteTaskViewModel;
    }

    public void setTodayTasksViewModel(TodayTasksViewModel todayTasksViewModel) {
        this.todayTasksViewModel = todayTasksViewModel;
        System.out.println("DEBUG: DeleteTaskPresenter - TodayTasksViewModel set: " + (todayTasksViewModel != null));
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }

    @Override
    public void prepareSuccessView(DeleteTaskOutputData outputData) {
        // Update view models
        DeleteTaskState state = new DeleteTaskState();
        state.setLastDeletedTaskId(outputData.getTaskId());
        state.setSuccessMessage(outputData.getMessage());
        deleteTaskViewModel.setState(state);
        deleteTaskViewModel.firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);

        // Trigger refresh of available tasks
        AvailableTasksState availableState = availableTasksViewModel.getState();
        availableState.setRefreshNeeded(true);
        availableTasksViewModel.setState(availableState);
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
            System.out.println("DEBUG: Triggered Today's Tasks refresh after delete");
        }
        
        // Also refresh overdue tasks if controller is available
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Refresh with 7 days
        }
    }

    @Override
    public void prepareFailView(String error) {
        DeleteTaskState state = new DeleteTaskState();
        state.setError(error);
        deleteTaskViewModel.setState(state);
        deleteTaskViewModel.firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Override
    public void showDeleteFromBothWarning(String taskId, String taskName) {
        // In a real implementation, this would trigger a dialog in the view
        // For now, we'll set a flag in the view model
        DeleteTaskState state = new DeleteTaskState();
        state.setPendingDeleteTaskId(taskId);
        state.setPendingDeleteTaskName(taskName);
        state.setShowWarningDialog(true);
        deleteTaskViewModel.setState(state);
        deleteTaskViewModel.firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }
}