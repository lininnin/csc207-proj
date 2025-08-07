package use_case.Angela.task.mark_complete;

/**
 * Input data for marking a task complete or incomplete.
 */
public class MarkTaskCompleteInputData {
    private final String taskId;
    private final boolean markAsComplete;

    public MarkTaskCompleteInputData(String taskId, boolean markAsComplete) {
        this.taskId = taskId;
        this.markAsComplete = markAsComplete;
    }

    public String getTaskId() {
        return taskId;
    }
    
    public boolean isMarkAsComplete() {
        return markAsComplete;
    }
}