package interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel;

import interface_adapter.ViewModel;

public class AddMoodLabelViewModel extends ViewModel<AddMoodLabelState> {

    public static final String ADD_MOOD_LABEL_PROPERTY = "addMoodLabel";

    public AddMoodLabelViewModel() {
        super("AddMoodLabelView");
        this.setState(new AddMoodLabelState());
    }
}

