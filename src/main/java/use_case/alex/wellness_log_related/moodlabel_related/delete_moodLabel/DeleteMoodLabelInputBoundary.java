package use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel;

/**
 * Input boundary for the DeleteMoodLabel use case.
 * Defines the input method that the controller can call.
 */
public interface DeleteMoodLabelInputBoundary {

    /**
     * Executes the delete use case with the provided input data.
     *
     * @param inputData The input data containing mood label name.
     */
    void execute(DeleteMoodLabelInputData inputData);
}

