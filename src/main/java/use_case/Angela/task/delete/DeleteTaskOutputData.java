package use_case.Angela.task.delete;

/**
 * Output data for the delete task use case.
 */
public class DeleteTaskOutputData {
    private final String taskId;
    private final String message;
    private final boolean deletedFromBoth;

    public DeleteTaskOutputData(String taskId, String message, boolean deletedFromBoth) {
        this.taskId = taskId;
        this.message = message;
        this.deletedFromBoth = deletedFromBoth;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isDeletedFromBoth() {
        return deletedFromBoth;
    }
}