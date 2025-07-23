package interface_adapter.Alex.delete_event;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for DeleteAvailableEvent use case.
 * Holds the current DeletedEventState and notifies listeners (typically Views) on state change.
 */
public class DeletedEventViewModel {

    public static final String DELETE_EVENT_STATE_PROPERTY = "deletedEventState";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private DeletedEventState state = new DeletedEventState();

    /**
     * Returns the current state.
     */
    public DeletedEventState getState() {
        return state;
    }

    /**
     * Updates the state and notifies listeners.
     */
    public void setState(DeletedEventState newState) {
        DeletedEventState oldState = this.state;
        this.state = newState;
        support.firePropertyChange(DELETE_EVENT_STATE_PROPERTY, oldState, newState);
    }

    /**
     * Adds a PropertyChangeListener to listen to DELETE_EVENT_STATE_PROPERTY changes.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(DELETE_EVENT_STATE_PROPERTY, listener);
    }

    /**
     * Removes a PropertyChangeListener.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(DELETE_EVENT_STATE_PROPERTY, listener);
    }
}

