package interface_adapter.Angela.task.delete;

import interface_adapter.ViewModel;

/**
 * View model for delete task operations.
 */
public class DeleteTaskViewModel extends ViewModel<DeleteTaskState> {
    public static final String DELETE_TASK_STATE_PROPERTY = "deleteTaskState";

    public DeleteTaskViewModel() {
        super("delete task");
        setState(new DeleteTaskState());
    }

    public void updateState(DeleteTaskState newState) {
        setState(newState);
        firePropertyChanged(DELETE_TASK_STATE_PROPERTY);
    }
}