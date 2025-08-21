package use_case.Angela.task.edit_today;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import java.time.LocalDate;

/**
 * Data access interface for editing today's tasks.
 * This interface defines the methods needed by the EditTodayTask use case.
 * Only allows editing of priority and due date for Today's Tasks.
 */
public interface EditTodayTaskDataAccessInterface {
    /**
     * Gets a task from today's list by its ID.
     *
     * @param taskId The ID of the task
     * @return The task if found, null otherwise
     */
    TaskInterf getTodayTaskById(String taskId);

    /**
     * Updates the priority and due date of a task in today's list.
     * Other fields (name, description, category) cannot be modified through this interface.
     *
     * @param taskId The ID of the task to update
     * @param priority The new priority (can be null to clear priority)
     * @param dueDate The new due date (can be null to clear due date)
     * @return true if the update was successful, false if task not found
     * @throws IllegalArgumentException if due date is before today
     */
    boolean updateTodayTaskPriorityAndDueDate(String taskId, Task.Priority priority, LocalDate dueDate);

    /**
     * Validates that a due date is not before today.
     *
     * @param dueDate The date to validate
     * @return true if the date is valid (today or future), false otherwise
     */
    boolean isValidDueDate(LocalDate dueDate);
}