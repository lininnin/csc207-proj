package use_case.task.add_to_today;

/**
 * Output data for adding task to today's list.
 */
public class AddTaskToTodayOutputData {
    private final String taskId;
    private final String taskName;
    private final boolean success;

    public AddTaskToTodayOutputData(String taskId, String taskName, boolean success) {
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