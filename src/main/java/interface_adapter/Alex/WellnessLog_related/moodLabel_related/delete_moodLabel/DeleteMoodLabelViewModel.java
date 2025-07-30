package interface_adapter.Alex.WellnessLog_related.moodLabel_related.delete_moodLabel;

import interface_adapter.ViewModel;

/**
 * ViewModel for tracking the state of a deleted mood label.
 * Used by the view to observe and respond to changes in deletion state.
 */
public class DeleteMoodLabelViewModel extends ViewModel<DeleteMoodLabelState> {

    public static final String DELETE_MOOD_LABEL_STATE_PROPERTY = "deleteMoodLabelState";

    public DeleteMoodLabelViewModel() {
        super("delete mood label view");
        this.setState(new DeleteMoodLabelState());
    }

    public void updateState(DeleteMoodLabelState newState) {
        this.setState(newState);
        this.firePropertyChanged(DELETE_MOOD_LABEL_STATE_PROPERTY);
    }
}

