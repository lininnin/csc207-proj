package use_case.Angela.task.remove_from_today;

/**
 * Output data for the remove from today use case.
 */
public class RemoveFromTodayOutputData {
    private final String taskId;
    private final String taskName;
    private final String message;

    public RemoveFromTodayOutputData(String taskId, String taskName, String message) {
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