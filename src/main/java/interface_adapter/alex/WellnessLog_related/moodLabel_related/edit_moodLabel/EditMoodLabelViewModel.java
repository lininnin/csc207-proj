package interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

import interface_adapter.ViewModel;

public class EditMoodLabelViewModel extends ViewModel<EditMoodLabelState> {

    public static final String EDIT_MOOD_LABEL_STATE_PROPERTY = "editMoodLabelState";

    public EditMoodLabelViewModel() {
        super("Edit Mood Label View");
        this.setState(new EditMoodLabelState());
    }

    /**
     * Updates the state and notifies listeners.
     */
    public void updateState(EditMoodLabelState newState) {
        this.setState(newState);
        this.firePropertyChanged(EDIT_MOOD_LABEL_STATE_PROPERTY);
    }
}

