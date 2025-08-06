package use_case.Angela.task.delete;

/**
 * Input data for the delete task use case.
 */
public class DeleteTaskInputData {
    private final String taskId;
    private final boolean isFromAvailable;

    /**
     * Creates input data for deleting a task.
     *
     * @param taskId The ID of the task to delete
     * @param isFromAvailable Whether deletion is initiated from Available Tasks view
     */
    public DeleteTaskInputData(String taskId, boolean isFromAvailable) {
        this.taskId = taskId;
        this.isFromAvailable = isFromAvailable;
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean isFromAvailable() {
        return isFromAvailable;
    }
}