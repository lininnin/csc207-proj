package interface_adapter.alex.WellnessLog_related.moodLabel_related;

import interface_adapter.ViewModel;

public class AvailableMoodLabelViewModel extends ViewModel<AvailableMoodLabelState> {

    public static final String MOOD_LABEL_LIST_PROPERTY = "moodLabels";

    public AvailableMoodLabelViewModel() {
        super("AvailableMoodLabelView");
        this.setState(new AvailableMoodLabelState());
    }

    @Override
    public void setState(AvailableMoodLabelState state) {
        super.setState(state);
        firePropertyChanged(MOOD_LABEL_LIST_PROPERTY); // 通知视图刷新
    }

    public void fireLabelListChanged() {
        firePropertyChanged(MOOD_LABEL_LIST_PROPERTY); // 手动通知视图刷新
    }
}

