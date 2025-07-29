package use_case.Angela.task.create_task;

/**
 * Output boundary for the create task use case.
 */
public interface CreateTaskOutputBoundary {
    /**
     * Presents successful task creation.
     *
     * @param outputData The output data containing task details
     */
    void presentSuccess(CreateTaskOutputData outputData);

    /**
     * Presents an error during task creation.
     *
     * @param error The error message
     */
    void presentError(String error);
}