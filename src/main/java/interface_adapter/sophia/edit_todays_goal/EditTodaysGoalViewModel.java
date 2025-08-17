package interface_adapter.sophia.edit_todays_goal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * ViewModel for managing the presentation state of today's goal editing.
 */
public class EditTodaysGoalViewModel {
    private List<String> goalNames;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Sets the list of available goal names and notifies listeners of the change.
     * @param goalNames The new list of goal names
     */
    public void setGoalNames(List<String> goalNames) {
        final List<String> oldValue = this.goalNames;
        this.goalNames = goalNames;
        support.firePropertyChange("goalNames", oldValue, goalNames);
    }

    /**
     * Gets the current list of goal names.
     * @return The list of goal names
     */
    public List<String> getGoalNames() {
        return goalNames;
    }

    /**
     * Adds a property change listener.
     * @param listener The listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
