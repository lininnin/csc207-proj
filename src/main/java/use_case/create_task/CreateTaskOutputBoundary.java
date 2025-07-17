package use_case.create_task;

/**
 * Output boundary interface for task creation.
 */
public interface CreateTaskOutputBoundary {
    void presentSuccess(CreateTaskOutputData outputData);
    void presentError(String error);
}
