package interface_adapter.Angela.task.create;

import interface_adapter.ViewModel;

/**
 * View model for the create task view.
 * Manages the state and notifies observers of changes.
 */
public class CreateTaskViewModel extends ViewModel<CreateTaskState> {
    public static final String TITLE_LABEL = "Create New Task";
    public static final String TASK_NAME_LABEL = "Task Name:";
    public static final String DESCRIPTION_LABEL = "Description:";
    public static final String CATEGORY_LABEL = "Category:";
    public static final String ONE_TIME_LABEL = "One-Time Task";
    public static final String CREATE_BUTTON_LABEL = "Create";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public static final String CREATE_TASK_STATE_PROPERTY = "createTaskState";

    public CreateTaskViewModel() {
        super("create task");
        setState(new CreateTaskState());
    }

    /**
     * Updates the state and notifies listeners.
     */
    public void updateState(CreateTaskState newState) {
        setState(newState);
        firePropertyChanged(CREATE_TASK_STATE_PROPERTY);
    }
}