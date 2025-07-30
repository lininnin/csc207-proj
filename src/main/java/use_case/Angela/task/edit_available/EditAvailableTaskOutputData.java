package use_case.Angela.task.edit_available;

/**
 * Output data for the edit available task use case.
 */
public class EditAvailableTaskOutputData {
    private final String taskId;
    private final String taskName;
    private final String message;

    public EditAvailableTaskOutputData(String taskId, String taskName, String message) {
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