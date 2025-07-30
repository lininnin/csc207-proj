package use_case.Angela.task.unmark_complete;

/**
 * Input data for unmarking a completed task.
 */
public class UnmarkTaskCompleteInputData {
    private final String taskId;

    public UnmarkTaskCompleteInputData(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
