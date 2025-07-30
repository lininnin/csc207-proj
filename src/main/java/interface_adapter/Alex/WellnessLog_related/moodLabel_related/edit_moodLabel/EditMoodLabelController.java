package interface_adapter.Alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

import use_case.Alex.WellnessLog_related.Moodlabel_related.edit_moodLabel.EditMoodLabelInputBoundary;
import use_case.Alex.WellnessLog_related.Moodlabel_related.edit_moodLabel.EditMoodLabelInputData;

/**
 * Controller for handling edit mood label requests from the UI.
 */
public class EditMoodLabelController {

    private final EditMoodLabelInputBoundary interactor;

    public EditMoodLabelController(EditMoodLabelInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Called by the UI when the user submits an edit request for a mood label.
     *
     * @param originalName The original name of the mood label to be edited
     * @param newName      The new name for the mood label
     * @param newType      The new type for the mood label ("Positive" or "Negative")
     */
    public void execute(String originalName, String newName, String newType) {
        EditMoodLabelInputData inputData = new EditMoodLabelInputData(originalName, newName, newType);
        interactor.execute(inputData);
    }
}

