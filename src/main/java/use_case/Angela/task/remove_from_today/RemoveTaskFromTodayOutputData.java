package use_case.Angela.task.remove_from_today;

/**
 * Output data for the remove task from today use case.
 */
public class RemoveTaskFromTodayOutputData {
    private final String removedTaskId;
    private final String taskName;
    private final String message;

    public RemoveTaskFromTodayOutputData(String removedTaskId, String taskName, String message) {
        this.removedTaskId = removedTaskId;
        this.taskName = taskName;
        this.message = message;
    }

    public String getRemovedTaskId() {
        return removedTaskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getMessage() {
        return message;
    }
}