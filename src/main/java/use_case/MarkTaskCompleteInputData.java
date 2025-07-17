package use_case;

/**
 * Input data for marking task complete.
 */
public class MarkTaskCompleteInputData {
    private final String taskId;

    public MarkTaskCompleteInputData(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }
}
