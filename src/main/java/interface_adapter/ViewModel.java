package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for all view models in the application.
 * Provides property change support for observer pattern implementation.
 *
 * @param <T> The type of state this view model manages
 */
public abstract class ViewModel<T> {

    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private T state;

    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    /**
     * Gets the current state.
     *
     * @return The current state
     */
    public T getState() {
        return state;
    }

    /**
     * Sets the state and fires a property change event.
     *
     * @param state The new state
     */
    public void setState(T state) {
        T oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }

    /**
     * Fires a property change event to notify observers that the state has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    /**
     * Fires a property change event with a specific property name.
     *
     * @param propertyName The name of the property that changed
     */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, this.state);
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