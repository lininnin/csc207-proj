package use_case.task.create;

/**
 * Output data for task creation results.
 * Contains the created task ID and any business-level information.
 */
public class CreateTaskOutputData {
    private final String taskId;
    private final String taskName;
    private final boolean success;

    public CreateTaskOutputData(String taskId, String taskName, boolean success) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.success = success;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isSuccess() {
        return success;
    }
}