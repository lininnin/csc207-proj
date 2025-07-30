package use_case.Angela.task.create;

/**
 * Output boundary for the create task use case.
 */
public interface CreateTaskOutputBoundary {
    /**
     * Prepares the success view with the created task data.
     *
     * @param outputData The output data
     */
    void presentSuccess(CreateTaskOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void presentError(String error);
}