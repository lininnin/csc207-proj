package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import java.time.LocalDate;

/**
 * Input data for adding a task to today.
 */
public class AddTaskToTodayInputData {
    private final String taskId;
    private final Task.Priority priority;
    private final LocalDate dueDate;

    public AddTaskToTodayInputData(String taskId, Task.Priority priority, LocalDate dueDate) {
        this.taskId = taskId;
        this.priority = priority;
        this.dueDate = dueDate;
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
}