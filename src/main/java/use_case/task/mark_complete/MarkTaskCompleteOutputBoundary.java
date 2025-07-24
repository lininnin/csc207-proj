package use_case.task.mark_complete;

/**
 * Output boundary for marking tasks complete/incomplete.
 */
public interface MarkTaskCompleteOutputBoundary {
    /**
     * Presents successful status change.
     *
     * @param outputData The result data
     */
    void presentSuccess(MarkTaskCompleteOutputData outputData);

    /**
     * Presents status change failure.
     *
     * @param error The error message
     */
    void presentError(String error);
}