package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for all view models in the application.
 * Provides property change support for observer pattern implementation.
 */
public abstract class ViewModel {

    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    /**
     * Fires a property change event to notify observers that the state has changed.
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