package interface_adapter.Sophia.available_goals;

import entity.Sophia.Goal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

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

    public void removeGoalByName(String goalName) {
        List<Goal> currentGoals = new ArrayList<>(state.getAvailableGoals());
        currentGoals.removeIf(goal -> goal.getGoalInfo().getInfo().getName().equals(goalName));
        setState(new AvailableGoalsState());
    }
}