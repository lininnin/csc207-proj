package interface_adapter.Angela.task.delete;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import use_case.Angela.task.delete.DeleteTaskOutputBoundary;
import use_case.Angela.task.delete.DeleteTaskOutputData;

/**
 * Presenter for the delete task use case.
 */
public class DeleteTaskPresenter implements DeleteTaskOutputBoundary {
    private final AvailableTasksViewModel availableTasksViewModel;
    private final DeleteTaskViewModel deleteTaskViewModel;

    public DeleteTaskPresenter(AvailableTasksViewModel availableTasksViewModel,
                               DeleteTaskViewModel deleteTaskViewModel) {
        this.availableTasksViewModel = availableTasksViewModel;
        this.deleteTaskViewModel = deleteTaskViewModel;
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