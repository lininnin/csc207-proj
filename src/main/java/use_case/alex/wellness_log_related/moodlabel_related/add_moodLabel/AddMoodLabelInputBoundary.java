package use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel;

/**
 * Input boundary for the AddMoodLabel use case.
 * Defines the method that the controller will call to trigger the use case logic.
 */
public interface AddMoodLabelInputBoundary {
    void execute(AddMoodLabelInputData inputData);
}
