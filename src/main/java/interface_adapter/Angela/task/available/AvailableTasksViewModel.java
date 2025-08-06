package interface_adapter.Angela.task.available;

import interface_adapter.ViewModel;

/**
 * View model for the available tasks view.
 * This is a placeholder - you'll need to implement fully when creating the view_available_tasks use case.
 */
public class AvailableTasksViewModel extends ViewModel<AvailableTasksState> {
    public static final String AVAILABLE_TASKS_STATE_PROPERTY = "availableTasksState";

    public AvailableTasksViewModel() {
        super("available tasks");
        setState(new AvailableTasksState());
    }
}