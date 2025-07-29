package use_case.Angela.task.edit_todays;

import entity.Task;
import java.time.LocalDate;

/**
 * Input data for editing a today's task.
 */
public class EditTodaysTaskInputData {
    private final String taskId;
    private final Task.Priority priority;
    private final LocalDate dueDate;
    private final Boolean isComplete;

    public EditTodaysTaskInputData(String taskId, Task.Priority priority,
                                   LocalDate dueDate, Boolean isComplete) {
        this.taskId = taskId;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
    }

    public String getTaskId() {
        return taskId;
    }

    public Task.Priority getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }
}