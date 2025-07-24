package use_case.task.mark_complete;

import java.time.LocalDateTime;

/**
 * Output data for task completion status changes.
 */
public class MarkTaskCompleteOutputData {
    private final String taskId;
    private final String taskName;
    private final boolean isCompleted;
    private final LocalDateTime completedDateTime;

    public MarkTaskCompleteOutputData(String taskId, String taskName,
                                      boolean isCompleted, LocalDateTime completedDateTime) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.isCompleted = isCompleted;
        this.completedDateTime = completedDateTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }
}