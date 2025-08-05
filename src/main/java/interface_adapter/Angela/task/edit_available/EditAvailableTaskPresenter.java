package interface_adapter.Angela.task.edit_available;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import use_case.Angela.task.edit_available.EditAvailableTaskOutputBoundary;
import use_case.Angela.task.edit_available.EditAvailableTaskOutputData;

/**
 * Presenter for the edit available task use case.
 * Converts use case output to view model updates.
 */
public class EditAvailableTaskPresenter implements EditAvailableTaskOutputBoundary {
    private final EditAvailableTaskViewModel editAvailableTaskViewModel;
    private final AvailableTasksViewModel availableTasksViewModel;

    public EditAvailableTaskPresenter(EditAvailableTaskViewModel editAvailableTaskViewModel,
                                      AvailableTasksViewModel availableTasksViewModel) {
        this.editAvailableTaskViewModel = editAvailableTaskViewModel;
        this.availableTasksViewModel = availableTasksViewModel;
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
}