package interface_adapter.Sophia.edit_todays_goal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class EditTodaysGoalViewModel {
    private List<String> goalNames;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setGoalNames(List<String> goalNames) {
        List<String> oldValue = this.goalNames;
        this.goalNames = goalNames;
        support.firePropertyChange("goalNames", oldValue, goalNames);
    }

    public List<String> getGoalNames() {
        return goalNames;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}