package interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel;

import use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel.AddMoodLabelInputBoundary;
import use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel.AddMoodLabelInputData;

/**
 * Controller for the AddMoodLabel use case.
 * Called by the UI to trigger the addition of a new mood label.
 */
public class AddMoodLabelController {

    private final AddMoodLabelInputBoundary interactor;

    public AddMoodLabelController(AddMoodLabelInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Triggers the use case with given user input.
     *
     * @param name Name of the mood label
     * @param type Type of the mood label ("Positive" or "Negative")
     */
    public void addMoodLabel(String name, String type) {
        AddMoodLabelInputData inputData = new AddMoodLabelInputData(name, type);
        interactor.execute(inputData);
    }
}

