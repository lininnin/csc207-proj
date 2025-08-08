package interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel;

import use_case.alex.WellnessLog_related.Moodlabel_related.delete_moodLabel.DeleteMoodLabelInputBoundary;
import use_case.alex.WellnessLog_related.Moodlabel_related.delete_moodLabel.DeleteMoodLabelInputData;

/**
 * Controller for the DeleteMoodLabel use case.
 * Invoked by the View (e.g., AvailableMoodLabelView) when a user triggers a delete action.
 */
public class DeleteMoodLabelController {

    private final DeleteMoodLabelInputBoundary interactor;

    /**
     * Constructs the controller with a given interactor (use case).
     *
     * @param interactor The input boundary for the delete mood label use case.
     */
    public DeleteMoodLabelController(DeleteMoodLabelInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the delete use case with the given mood label name.
     *
     * @param moodLabelName The name of the mood label to delete.
     */
    public void delete(String moodLabelName) {
        DeleteMoodLabelInputData inputData = new DeleteMoodLabelInputData(moodLabelName);
        interactor.execute(inputData);
    }
}

