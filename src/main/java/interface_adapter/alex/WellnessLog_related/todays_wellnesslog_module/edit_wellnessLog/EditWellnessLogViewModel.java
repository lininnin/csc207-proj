package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;

/**
 * ViewModel for editing a wellness log entry.
 * Extends the generic ViewModel class and uses EditWellnessLogState as state.
 */
public class EditWellnessLogViewModel extends ViewModel<EditWellnessLogState> {

    public static final String EDIT_WELLNESS_LOG_PROPERTY = "editWellnessLog";

    public EditWellnessLogViewModel() {
        super(EDIT_WELLNESS_LOG_PROPERTY);
        this.setState(new EditWellnessLogState());
    }

    /**
     * Notifies all listeners that the state has changed.
     */
    public void fireStateChanged() {
        firePropertyChanged(EDIT_WELLNESS_LOG_PROPERTY);
    }

    /**
     * Registers a listener for changes to the edit wellness log property.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
    }
}

