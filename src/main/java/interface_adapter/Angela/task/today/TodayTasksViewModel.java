package interface_adapter.Angela.task.today;

import interface_adapter.ViewModel;

/**
 * View model for today's tasks.
 */
public class TodayTasksViewModel extends ViewModel<TodayTasksState> {
    public static final String TODAY_TASKS_STATE_PROPERTY = "todayTasksState";

    public TodayTasksViewModel() {
        super("today tasks");
        setState(new TodayTasksState());
    }

    public void firePropertyChanged() {
        super.firePropertyChanged(TODAY_TASKS_STATE_PROPERTY);
    }
}