package interface_adapter.Alex.Event_related.available_event_module.edit_event;

import interface_adapter.ViewModel;

public class EditedEventViewModel extends ViewModel<EditedEventState> {

    public static final String EDITED_EVENT_STATE_PROPERTY = "editedEventState";

    public EditedEventViewModel() {
        super("Edited Event View");
        this.setState(new EditedEventState());
    }

    /**
     * Updates the state and notifies listeners.
     */
    public void updateState(EditedEventState newState) {
        this.setState(newState);
        this.firePropertyChanged(EDITED_EVENT_STATE_PROPERTY);
    }
}

