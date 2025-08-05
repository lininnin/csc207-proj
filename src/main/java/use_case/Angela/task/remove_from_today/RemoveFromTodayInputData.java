package use_case.Angela.task.remove_from_today;

/**
 * Input data for the remove from today use case.
 */
public class RemoveFromTodayInputData {
    private final String taskId;

    public RemoveFromTodayInputData(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}