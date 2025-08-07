package interface_adapter.Angela.task.add_to_today;

import interface_adapter.ViewModel;

/**
 * View model for adding tasks to today.
 */
public class AddTaskToTodayViewModel extends ViewModel<AddTaskToTodayState> {
    public static final String ADD_TO_TODAY_STATE_PROPERTY = "addToTodayState";

    public AddTaskToTodayViewModel() {
        super("add to today");
        setState(new AddTaskToTodayState());
    }

    public void firePropertyChanged() {
        super.firePropertyChanged(ADD_TO_TODAY_STATE_PROPERTY);
    }
}