package interface_adapter.Alex.edit_todays_event;

import interface_adapter.ViewModel;

public class EditTodaysEventViewModel extends ViewModel<EditTodaysEventState> {

    public static final String EDIT_TODAYS_EVENT_STATE_PROPERTY = "editTodaysEventState";

    public EditTodaysEventViewModel() {
        super("Edited Event View");
        this.setState(new EditTodaysEventState());
    }

    /**
     * Updates the state and notifies listeners.
     */
    public void updateState(EditTodaysEventState newState) {
        this.setState(newState);
        this.firePropertyChanged(EDIT_TODAYS_EVENT_STATE_PROPERTY);
    }
}