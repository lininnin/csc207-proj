package use_case.Alex.WellnessLog_related.Moodlabel_related.edit_moodLabel;

/**
 * Output boundary for the Edit Mood Label use case.
 * Defines methods to present the results of the use case.
 */
public interface EditMoodLabelOutputBoundary {

    /**
     * Called when the edit mood label use case is successful.
     *
     * @param outputData Data to be passed to the presenter/view.
     */
    void prepareSuccessView(EditMoodLabelOutputData outputData);

    /**
     * Called when the edit mood label use case fails (e.g. invalid input, label not found).
     *
     * @param outputData Data containing failure info.
     */
    void prepareFailView(EditMoodLabelOutputData outputData);
}

