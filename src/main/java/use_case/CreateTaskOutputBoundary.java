package use_case;

/**
 * Output boundary interface for task creation.
 */
public interface CreateTaskOutputBoundary {
    void presentSuccess(CreateTaskOutputData outputData);
    void presentError(String error);
}
