package view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for all ViewModels in the application.
 * Handles property change notifications for the observer pattern.
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Convenience method to notify listeners of a state change.
     * This fires a property change event for the "state" property.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this);
    }
}