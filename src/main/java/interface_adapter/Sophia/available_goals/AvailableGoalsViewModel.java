package interface_adapter.Sophia.available_goals;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AvailableGoalsViewModel {
    private AvailableGoalsState state = new AvailableGoalsState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public AvailableGoalsState getState() {
        return state;
    }

    public void setState(AvailableGoalsState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}