package interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel;

import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.DeleteMoodLabelOutputBoundary;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.DeleteMoodLabelOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the DeleteMoodLabel use case.
 * Converts output data into view model state and notifies the view via the ViewModel.
 */
public class DeleteMoodLabelPresenter implements DeleteMoodLabelOutputBoundary {

    private final DeleteMoodLabelViewModel deleteViewModel;
    private final AvailableMoodLabelViewModel availableViewModel;

    public DeleteMoodLabelPresenter(DeleteMoodLabelViewModel deleteViewModel,
                                    AvailableMoodLabelViewModel availableViewModel) {
        this.deleteViewModel = deleteViewModel;
        this.availableViewModel = availableViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteMoodLabelOutputData outputData) {
        // ✅ 更新删除结果 ViewModel
        DeleteMoodLabelState newState = new DeleteMoodLabelState();
        newState.setDeletedMoodLabelName(outputData.getMoodLabelName());
        newState.setDeletedSuccessfully(true);
        newState.setDeleteError(null);
        deleteViewModel.setState(newState);
        deleteViewModel.firePropertyChanged(DeleteMoodLabelViewModel.DELETE_MOOD_LABEL_STATE_PROPERTY);

        // ✅ 更新 AvailableMoodLabelViewModel
        AvailableMoodLabelState currentState = availableViewModel.getState();
        List<MoodLabelEntry> updatedList = new ArrayList<>(currentState.getMoodLabels());
        updatedList.removeIf(label -> label.getName().equals(outputData.getMoodLabelName()));
        currentState.setMoodLabels(updatedList);
        availableViewModel.setState(currentState);
        availableViewModel.firePropertyChanged(AvailableMoodLabelViewModel.MOOD_LABEL_LIST_PROPERTY );
    }

    @Override
    public void prepareFailView(DeleteMoodLabelOutputData outputData) {
        DeleteMoodLabelState newState = new DeleteMoodLabelState();
        newState.setDeletedMoodLabelName(outputData.getMoodLabelName());
        newState.setDeletedSuccessfully(false);
        newState.setDeleteError(outputData.getErrorMessage());
        deleteViewModel.setState(newState);
        deleteViewModel.firePropertyChanged(DeleteMoodLabelViewModel.DELETE_MOOD_LABEL_STATE_PROPERTY);
    }
}

