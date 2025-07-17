package use_case.add_task_to_today;

/**
 * Output data for adding task to today.
 */
public class AddTaskToTodayOutputData {
    private final String taskName;
    private final String message;

    public AddTaskToTodayOutputData(String taskName, String message) {
        this.taskName = taskName;
        this.message = message;
    }

    public String getTaskName() { return taskName; }
    public String getMessage() { return message; }
}
