package use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel;

/**
 * Input boundary for the Edit Mood Label use case.
 * Defines the method required to execute the use case with input data.
 */
public interface EditMoodLabelInputBoundary {

    /**
     * Executes the edit mood label use case with the provided input data.
     *
     * @param inputData The data needed to perform the edit operation.
     */
    void execute(EditMoodLabelInputData inputData);
}

