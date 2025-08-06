package use_case.Angela.task.create;

/**
 * Output boundary for the create task use case.
 * Defines the interface that the Presenter must implement.
 */
public interface CreateTaskOutputBoundary {
    /**
     * Presents successful task creation.
     *
     * @param outputData The output data containing the created task information
     */
    void presentSuccess(CreateTaskOutputData outputData);

    /**
     * Presents an error that occurred during task creation.
     *
     * @param error The error message
     */
    void presentError(String error);
}