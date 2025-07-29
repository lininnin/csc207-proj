package use_case.Angela.task.remove_from_today;

/**
 * Input data for removing a task from today.
 */
public class RemoveTaskFromTodayInputData {
    private final String taskId;

    public RemoveTaskFromTodayInputData(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}