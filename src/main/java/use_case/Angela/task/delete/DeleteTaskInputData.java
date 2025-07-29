package use_case.Angela.task.delete;

/**
 * Input data for deleting a task.
 */
public class DeleteTaskInputData {
    private final String taskId;
    private final boolean isFromAvailable;

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