package use_case.Angela.task.mark_complete;

/**
 * Output boundary for marking a task complete.
 */
public interface MarkTaskCompleteOutputBoundary {
    /**
     * Prepares the success view.
     *
     * @param outputData The output data
     */
    void presentSuccess(MarkTaskCompleteOutputData outputData);

    /**
     * Prepares the error view.
     *
     * @param error The error message
     */
    void presentError(String error);
}