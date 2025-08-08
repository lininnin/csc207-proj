package use_case.alex.WellnessLog_related.Moodlabel_related.delete_moodLabel;

/**
 * Output boundary for the DeleteMoodLabel use case.
 * Presenter implements this interface to receive results from the Interactor.
 */
public interface DeleteMoodLabelOutputBoundary {

    /**
     * Called when the delete operation succeeds.
     *
     * @param outputData The output data containing mood label info.
     */
    void prepareSuccessView(DeleteMoodLabelOutputData outputData);

    /**
     * Called when the delete operation fails.
     *
     * @param outputData The output data containing failure reason.
     */
    void prepareFailView(DeleteMoodLabelOutputData outputData);
}

