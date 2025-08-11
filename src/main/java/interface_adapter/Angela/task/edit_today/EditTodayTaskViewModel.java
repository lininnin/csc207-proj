package interface_adapter.Angela.task.edit_today;

import interface_adapter.ViewModel;

/**
 * View model for the edit today task use case.
 * Manages the state of the edit today task UI.
 */
public class EditTodayTaskViewModel extends ViewModel<EditTodayTaskState> {
    public static final String EDIT_TODAY_TASK_STATE_PROPERTY = "editTodayTaskState";

    /**
     * Constructs the view model with a default view name.
     */
    public EditTodayTaskViewModel() {
        super("edit today task");
        setState(new EditTodayTaskState());
    }

    /**
     * Fires a property change event for the state.
     */
    @Override
    public void firePropertyChanged() {
        super.firePropertyChanged(EDIT_TODAY_TASK_STATE_PROPERTY);
    }
}