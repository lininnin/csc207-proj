package use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel;

/**
 * Output boundary for the AddMoodLabel use case.
 * Presenter implements this interface to receive success or failure results.
 */
public interface AddMoodLabelOutputBoundary {

    /**
     * Called when the mood label is successfully added.
     *
     * @param outputData The result data of the successfully added mood label.
     */
    void prepareSuccessView(AddMoodLabelOutputData outputData);

    /**
     * Called when the mood label addition fails (e.g., invalid input or duplicate).
     *
     * @param errorMessage Description of the failure.
     */
    void prepareFailView(String errorMessage);
}

