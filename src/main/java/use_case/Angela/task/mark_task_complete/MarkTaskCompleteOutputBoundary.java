package use_case.Angela.task.mark_task_complete;

/**
 * Output boundary for mark task complete use case.
 */
public interface MarkTaskCompleteOutputBoundary {
    void presentSuccess(MarkTaskCompleteOutputData outputData);
    void presentError(String error);
}