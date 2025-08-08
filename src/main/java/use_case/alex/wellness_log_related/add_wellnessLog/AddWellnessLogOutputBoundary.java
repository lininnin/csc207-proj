package use_case.alex.wellness_log_related.add_wellnessLog;

/**
 * Output boundary for the AddWellnessLog use case.
 * The presenter implements this interface to handle success or failure.
 */
public interface AddWellnessLogOutputBoundary {

    /**
     * Called when the log is successfully added.
     *
     * @param outputData the result data
     */
    void prepareSuccessView(AddWellnessLogOutputData outputData);

    /**
     * Called when the log addition fails (e.g. invalid input).
     *
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);
}

