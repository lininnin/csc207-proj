package interface_adapter.Angela.task.edit_available;

import interface_adapter.ViewModel;

/**
 * View model for editing available tasks.
 */
public class EditAvailableTaskViewModel extends ViewModel<EditAvailableTaskState> {
    public static final String EDIT_AVAILABLE_TASK_STATE_PROPERTY = "editAvailableTaskState";

    public EditAvailableTaskViewModel() {
        super("edit available task");
        // Initialize with empty state
        setState(new EditAvailableTaskState());
    }

    @Override
    public void setState(EditAvailableTaskState state) {
        System.out.println("DEBUG: EditAvailableTaskViewModel.setState - " + state);
        super.setState(state);
    }

    public void firePropertyChanged() {
        EditAvailableTaskState currentState = getState();
        System.out.println("DEBUG: EditAvailableTaskViewModel.firePropertyChanged - state: " + currentState);
        // Fire property change with the actual state object as the new value
        super.firePropertyChanged(EDIT_AVAILABLE_TASK_STATE_PROPERTY);
    }
}