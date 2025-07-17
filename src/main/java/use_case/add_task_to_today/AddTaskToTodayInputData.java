package use_case.add_task_to_today;

/**
 * Input data for adding task to today.
 */
public class AddTaskToTodayInputData {
    private final String taskId;

    public AddTaskToTodayInputData(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }
}
