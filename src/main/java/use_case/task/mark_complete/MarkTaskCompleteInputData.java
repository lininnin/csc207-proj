package use_case.task.mark_complete;

/**
 * Input data for marking task completion status.
 * Even though it's just an ID now, this allows for future extension
 * without changing the interface (e.g., adding completion notes).
 */
public class MarkTaskCompleteInputData {
    private final String taskId;

    public MarkTaskCompleteInputData(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}