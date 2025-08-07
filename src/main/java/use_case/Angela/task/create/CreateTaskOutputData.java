package use_case.Angela.task.create;

/**
 * Output data for the create task use case.
 * Contains the result of creating a new task.
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