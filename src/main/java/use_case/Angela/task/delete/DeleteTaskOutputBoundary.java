package use_case.Angela.task.delete;

/**
 * Output boundary for the delete task use case.
 */
public interface DeleteTaskOutputBoundary {
    /**
     * Presents successful task deletion.
     *
     * @param outputData The output data containing deletion details
     */
    void prepareSuccessView(DeleteTaskOutputData outputData);

    /**
     * Presents an error that occurred during deletion.
     *
     * @param error The error message
     */
    void prepareFailView(String error);

    /**
     * Shows warning dialog when task exists in both lists.
     *
     * @param taskId The task ID
     * @param taskName The task name
     */
    void showDeleteFromBothWarning(String taskId, String taskName);
}