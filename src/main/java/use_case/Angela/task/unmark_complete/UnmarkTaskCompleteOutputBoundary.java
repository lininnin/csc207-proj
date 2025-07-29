package use_case.Angela.task.unmark_complete;

/**
 * Output boundary for the unmark task complete use case.
 */
public interface UnmarkTaskCompleteOutputBoundary {
    /**
     * Prepares the success view after unmarking task.
     *
     * @param outputData The output data
     */
    void prepareSuccessView(UnmarkTaskCompleteOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);
}