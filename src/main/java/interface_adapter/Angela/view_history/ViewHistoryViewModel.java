package interface_adapter.Angela.view_history;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View model for the history view.
 * Manages the state and notifies observers of changes.
 */
public class ViewHistoryViewModel {
    private final PropertyChangeSupport support;
    private ViewHistoryState state;
    
    public ViewHistoryViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.state = new ViewHistoryState();
    }
    
    public void setState(ViewHistoryState state) {
        ViewHistoryState oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }
    
    public ViewHistoryState getState() {
        return state;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
    
    /**
     * Fires a property change event for UI updates.
     * @param propertyName The name of the property that changed
     */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }
}