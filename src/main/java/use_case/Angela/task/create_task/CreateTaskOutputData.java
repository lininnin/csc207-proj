package use_case.create_task;

/**
 * Output data for successful task creation.
 */
public class CreateTaskOutputData {
    private final String taskId;
    private final String taskName;
    private final String message;

    public CreateTaskOutputData(String taskId, String taskName, String message) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.message = message;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getMessage() {
        return message;
    }
}