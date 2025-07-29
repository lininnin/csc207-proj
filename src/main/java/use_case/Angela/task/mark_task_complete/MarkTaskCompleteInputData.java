package use_case.Angela.task.mark_task_complete;

/**
 * Input data for marking a task complete.
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