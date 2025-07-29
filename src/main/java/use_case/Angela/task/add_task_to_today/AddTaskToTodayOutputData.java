package use_case.Angela.task.add_task_to_today;

/**
 * Output data for successful add to today operation.
 */
public class AddTaskToTodayOutputData {
    private final String taskName;
    private final String message;

    public AddTaskToTodayOutputData(String taskName, String message) {
        this.taskName = taskName;
        this.message = message;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getMessage() {
        return message;
    }
}