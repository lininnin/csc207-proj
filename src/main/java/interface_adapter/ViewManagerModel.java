package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Model for managing which view is currently active.
 * Uses the observer pattern to notify when the active view changes.
 */
public class ViewManagerModel {

    private String activeViewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Gets the name of the currently active view.
     *
     * @return The active view name
     */
    public String getActiveView() {
        return activeViewName;
    }

    /**
     * Sets the active view and notifies observers.
     *
     * @param viewName The name of the view to activate
     */
    public void setActiveView(String viewName) {
        String oldView = this.activeViewName;
        this.activeViewName = viewName;
        support.firePropertyChange("view", oldView, viewName);
    }

    /**
     * Fires a property change event.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this);
    }

    /**
     * Adds a property change listener.
     *
     * @param listener The listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     *
     * @param listener The listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}