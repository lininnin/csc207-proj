package interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import use_case.alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelOutputBoundary;
import use_case.alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelOutputData;
import use_case.alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelDataAccessInterf;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Presenter for the AddMoodLabel use case.
 * Updates the AddMoodLabelViewModel and AvailableMoodLabelViewModel based on success or failure.
 */
public class AddMoodLabelPresenter implements AddMoodLabelOutputBoundary {

    private final AddMoodLabelViewModel addMoodLabelViewModel;
    private final AvailableMoodLabelViewModel availableMoodLabelViewModel;
    private final AddMoodLabelDataAccessInterf dataAccess;

    public AddMoodLabelPresenter(AddMoodLabelViewModel addMoodLabelViewModel,
                                 AvailableMoodLabelViewModel availableMoodLabelViewModel,
                                 AddMoodLabelDataAccessInterf dataAccess) {
        this.addMoodLabelViewModel = addMoodLabelViewModel;
        this.availableMoodLabelViewModel = availableMoodLabelViewModel;
        this.dataAccess = dataAccess;
    }

    @Override
    public void prepareSuccessView(AddMoodLabelOutputData outputData) {
        // ✅ 更新 Add 模块提示
        AddMoodLabelState state = addMoodLabelViewModel.getState();
        state.setErrorMessage(null);
        state.setSuccessMessage("Mood label \"" + outputData.getMoodName() + "\" added successfully.");
        state.setMoodName(outputData.getMoodName());
        state.setType(outputData.getMoodType());
        addMoodLabelViewModel.setState(state);

        // ✅ 刷新 Available Mood Label 模块
        List<MoodLabelInterf> updatedLabels = dataAccess.getAllLabels(); // 获取最新列表
        List<AvailableMoodLabelState.MoodLabelEntry> entries = updatedLabels.stream()
                .map(label -> new AvailableMoodLabelState.MoodLabelEntry(
                        label.getName(),
                        label.getType().name() // 将 enum 转为字符串
                ))
                .collect(Collectors.toList());


        AvailableMoodLabelState availableState = new AvailableMoodLabelState();
        availableState.setMoodLabels(entries);
        availableMoodLabelViewModel.setState(availableState);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // 更新错误提示
        AddMoodLabelState state = addMoodLabelViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        addMoodLabelViewModel.setState(state);

        // ✅ 清理 isNew 且 name 为空的 entry（防止 UI 中出现空白标签）
        AvailableMoodLabelState availableState = availableMoodLabelViewModel.getState();
        List<AvailableMoodLabelState.MoodLabelEntry> entries = availableState.getMoodLabels();
        entries.removeIf(entry -> entry.isNew() && entry.getName().trim().isEmpty());

        availableMoodLabelViewModel.fireLabelListChanged();
    }

}



