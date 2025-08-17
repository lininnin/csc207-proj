package interface_adapter.sophia.available_goals;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import entity.Sophia.Goal;

/**
 * ViewModel class for managing the presentation and state of available goals.
 * Provides property change support for view updates and maintains the current state
 * of available goals in the application.
 */
public class AvailableGoalsViewModel {
    private AvailableGoalsState state = new AvailableGoalsState();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Gets the current state of available goals.
     * @return Current AvailableGoalsState containing the list of goals
     */
    public AvailableGoalsState getState() {
        return state;
    }

    /**
     * Updates the current state of available goals.
     * @param state New AvailableGoalsState to set
     */
    public void setState(AvailableGoalsState state) {
        this.state = state;
    }

    /**
     * Notifies all registered listeners that the state has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /**
     * Adds a property change listener to be notified of state changes.
     * @param listener The PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a goal from the available goals list by name.
     * Creates a new state instance after removal.
     * @param goalName Name of the goal to remove
     */
    public void removeGoalByName(String goalName) {
        final List<Goal> currentGoals = new ArrayList<>(state.getAvailableGoals());
        currentGoals.removeIf(goal -> goal.getGoalInfo().getInfo().getName().equals(goalName));
        setState(new AvailableGoalsState());
    }
}
