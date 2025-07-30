package use_case.Angela.task.delete;

/**
 * Output boundary for the delete task use case.
 */
public interface DeleteTaskOutputBoundary {
    /**
     * Prepares the success view after task deletion.
     *
     * @param outputData The output data
     */
    void prepareSuccessView(DeleteTaskOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);

    /**
     * Shows a warning dialog when task exists in both Available and Today's lists.
     *
     * @param taskId The task ID
     * @param taskName The task name for display
     */
    void showDeleteFromBothWarning(String taskId, String taskName);
}
