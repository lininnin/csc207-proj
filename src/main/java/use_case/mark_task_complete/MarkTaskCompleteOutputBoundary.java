package use_case.mark_task_complete;

/**
 * Output boundary interface for marking task complete.
 */
public interface MarkTaskCompleteOutputBoundary {
    void presentSuccess(MarkTaskCompleteOutputData outputData);
    void presentError(String error);
}
