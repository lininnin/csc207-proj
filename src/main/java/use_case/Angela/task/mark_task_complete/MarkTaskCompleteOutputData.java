package use_case.Angela.task.mark_task_complete;

import java.time.LocalDateTime;

/**
 * Output data for successful task completion.
 */
public class MarkTaskCompleteOutputData {
    private final String taskId;
    private final String taskName;
    private final LocalDateTime completionTime;
    private final double completionRate;

    public MarkTaskCompleteOutputData(String taskId, String taskName,
                                      LocalDateTime completionTime, double completionRate) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.completionTime = completionTime;
        this.completionRate = completionRate;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public double getCompletionRate() {
        return completionRate;
    }
}