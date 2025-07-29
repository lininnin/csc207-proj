package use_case.Angela.task.unmark_complete;

/**
 * Output data for the unmark task complete use case.
 */
public class UnmarkTaskCompleteOutputData {
    private final String taskId;
    private final String taskName;
    private final int originalPosition;

    public UnmarkTaskCompleteOutputData(String taskId, String taskName, int originalPosition) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.originalPosition = originalPosition;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }
}