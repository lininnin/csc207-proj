package use_case.task.create;

/**
 * Output boundary for task creation use case.
 * Defines how the use case communicates results back to the presenter.
 */
public interface CreateTaskOutputBoundary {
    /**
     * Presents successful task creation.
     *
     * @param outputData The result data
     */
    void presentSuccess(CreateTaskOutputData outputData);

    /**
     * Presents task creation failure.
     *
     * @param error The error message
     */
    void presentError(String error);
}