package interface_adapter.sophia.today_goal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import entity.Sophia.Goal;
import interface_adapter.ViewModel;

/**
 * ViewModel for managing the state and properties of today's goals view.
 */
public class TodayGoalsViewModel extends ViewModel {
    private TodaysGoalsState state = new TodaysGoalsState();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Constructs a TodayGoalsViewModel with default state.
     */
    public TodayGoalsViewModel() {
        super("today goals");
    }

    /**
     * Gets the current state.
     * @return The current TodaysGoalsState
     */
    public TodaysGoalsState getState() {
        return state;
    }

    /**
     * Notifies all registered listeners of state changes.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    /**
     * Adds a property change listener.
     * @param listener The listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Sets the current state and notifies listeners.
     * @param state The new state to set
     */
    public void setState(TodaysGoalsState state) {
        this.state = state;
        firePropertyChanged();
    }

    /**
     * Removes a goal by name from today's list.
     * @param goalName The name of the goal to remove
     */
    public void removeGoalByName(String goalName) {
        final List<Goal> currentTodayGoals = new ArrayList<>(state.getTodayGoals());
        currentTodayGoals.removeIf(goal -> goal.getGoalInfo().getInfo().getName().equals(goalName));
        setState(new TodaysGoalsState(currentTodayGoals));
    }
}
