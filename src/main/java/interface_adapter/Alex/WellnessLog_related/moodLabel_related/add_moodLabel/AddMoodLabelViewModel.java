package interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel;

import interface_adapter.ViewModel;

/**
 * ViewModel for the AddMoodLabel use case.
 * Holds the state and UI labels for the add mood label form.
 */
public class AddMoodLabelViewModel extends ViewModel<AddMoodLabelState> {

    public static final String ADD_MOOD_LABEL_PROPERTY = "addMoodLabelState";

    // ✅ UI 文本标签常量（供视图引用）
    public static final String TITLE_LABEL = "Add New Mood Label";
    public static final String MOOD_NAME_LABEL = "Mood Name:";
    public static final String TYPE_LABEL = "Type (Positive / Negative):";
    public static final String ADD_BUTTON_LABEL = "Save";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public AddMoodLabelViewModel() {
        super("AddMoodLabelView");
        this.setState(new AddMoodLabelState());
    }

    @Override
    public void setState(AddMoodLabelState state) {
        super.setState(state);
        this.firePropertyChanged(ADD_MOOD_LABEL_PROPERTY); // 通知 UI 监听者更新
    }
}


