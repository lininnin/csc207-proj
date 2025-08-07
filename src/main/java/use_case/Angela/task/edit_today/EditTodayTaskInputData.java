package use_case.Angela.task.edit_today;

import entity.Angela.Task.Task;
import java.time.LocalDate;

/**
 * Input data for the Edit Today Task use case.
 * Contains the task ID and the new values for priority and due date.
 */
public class EditTodayTaskInputData {
    private final String taskId;
    private final Task.Priority priority;
    private final LocalDate dueDate;

    /**
     * Creates input data for editing a today's task.
     * 
     * @param taskId The ID of the task to edit
     * @param priority The new priority (can be null to clear priority)
     * @param dueDate The new due date (can be null to clear due date)
     */
    public EditTodayTaskInputData(String taskId, Task.Priority priority, LocalDate dueDate) {
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