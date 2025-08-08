package interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel.EditMoodLabelOutputBoundary;
import use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel.EditMoodLabelOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the EditMoodLabel use case.
 * Updates the edited MoodLabel instance and notifies the views.
 */
public class EditMoodLabelPresenter implements EditMoodLabelOutputBoundary {

    private final EditMoodLabelViewModel editMoodLabelViewModel;
    private final AvailableMoodLabelViewModel availableMoodLabelViewModel;

    public EditMoodLabelPresenter(EditMoodLabelViewModel editMoodLabelViewModel,
                                  AvailableMoodLabelViewModel availableMoodLabelViewModel) {
        this.editMoodLabelViewModel = editMoodLabelViewModel;
        this.availableMoodLabelViewModel = availableMoodLabelViewModel;
    }

    @Override
    public void prepareSuccessView(EditMoodLabelOutputData outputData) {
        // ✅ 更新 EditMoodLabelViewModel 状态（用于关闭弹窗或展示成功）
        EditMoodLabelState newState = new EditMoodLabelState();
        newState.setName(outputData.getName());
        newState.setType(outputData.getType());
        editMoodLabelViewModel.updateState(newState);

        // ✅ 更新 AvailableMoodLabelViewModel 中的标签列表
        AvailableMoodLabelState oldState = availableMoodLabelViewModel.getState();
        List<MoodLabelEntry> currentLabels = new ArrayList<>(oldState.getMoodLabels());

        for (MoodLabelEntry entry : currentLabels) {
            if (entry.getName().equals(outputData.getName())) {
                entry.setType(outputData.getType());
                break;
            }
        }

        AvailableMoodLabelState newStateForView = new AvailableMoodLabelState();
        newStateForView.setMoodLabels(currentLabels);
        availableMoodLabelViewModel.setState(newStateForView);
        availableMoodLabelViewModel.firePropertyChanged(AvailableMoodLabelViewModel.MOOD_LABEL_LIST_PROPERTY);
    }

    @Override
    public void prepareFailView(EditMoodLabelOutputData outputData) {
        EditMoodLabelState newState = new EditMoodLabelState();
        newState.setName(outputData.getName());
        newState.setType(outputData.getType());
        newState.setEditError("Edit failed: input may be invalid or label not found.");

        // ✅ 通知 ViewModel 触发界面反馈
        editMoodLabelViewModel.updateState(newState);
    }
}

