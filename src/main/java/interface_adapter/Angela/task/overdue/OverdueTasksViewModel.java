package interface_adapter.Angela.task.overdue;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View model for overdue tasks.
 */
public class OverdueTasksViewModel extends ViewModel {
    public static final String OVERDUE_TASKS_STATE_PROPERTY = "overdueTasks";
    
    private OverdueTasksState state = new OverdueTasksState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    public OverdueTasksViewModel() {
        super("overdueTasks");
    }
    
    public OverdueTasksState getState() {
        return state;
    }
    
    public void setState(OverdueTasksState state) {
        this.state = state;
    }
    
    @Override
    public void firePropertyChanged() {
        support.firePropertyChange(OVERDUE_TASKS_STATE_PROPERTY, null, state);
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}