package use_case.Angela.task.edit_today;

import entity.Angela.Task.Task;
import java.time.LocalDate;

/**
 * Output data for the Edit Today Task use case.
 * Contains the updated task information and a success message.
 */
public class EditTodayTaskOutputData {
    private final String taskId;
    private final String taskName;
    private final Task.Priority priority;
    private final LocalDate dueDate;
    private final String message;

    /**
     * Creates output data for a successful today's task edit.
     * 
     * @param taskId The ID of the edited task
     * @param taskName The name of the task (for display purposes)
     * @param priority The updated priority
     * @param dueDate The updated due date
     * @param message Success message
     */
    public EditTodayTaskOutputData(String taskId, String taskName, 
                                   Task.Priority priority, LocalDate dueDate, String message) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.priority = priority;
        this.dueDate = dueDate;
        this.message = message;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Task.Priority getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getMessage() {
        return message;
    }
}