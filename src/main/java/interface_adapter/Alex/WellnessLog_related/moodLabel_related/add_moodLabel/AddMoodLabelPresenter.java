package interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel;

import entity.Alex.MoodLabel.MoodLabel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelOutputBoundary;
import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelOutputData;
import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelDataAccessInterf;

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
        List<MoodLabel> updatedLabels = dataAccess.getAllLabels(); // 获取最新列表
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
        AddMoodLabelState state = addMoodLabelViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        addMoodLabelViewModel.setState(state);
    }
}



