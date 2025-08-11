package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import java.util.List;

/**
 * Data access interface for adding tasks to today's list.
 * Follows the DAO pattern where each use case defines its own interface.
 */
public interface AddToTodayDataAccessInterface {
    
    /**
     * Gets an available task by its ID.
     * 
     * @param taskId the ID of the task to retrieve
     * @return the TaskAvailable if found, null otherwise
     */
    TaskAvailable getAvailableTaskById(String taskId);
    
    /**
     * Adds a task to today's list with the specified priority and due date.
     * 
     * @param taskAvailable the available task template to add
     * @param priority the priority for today's task (optional, can be null)
     * @param dueDate the due date for today's task (optional, can be null)
     * @return the created Task instance that was added to today's list
     */
    Task addTaskToToday(TaskAvailable taskAvailable, Task.Priority priority, java.time.LocalDate dueDate);
    
    /**
     * Checks if a task with the given template ID already exists in today's list.
     * 
     * @param templateTaskId the template task ID to check
     * @return true if a task with this template already exists in today's list, false otherwise
     */
    boolean isTaskInTodaysList(String templateTaskId);
    
    /**
     * Checks if a task is in today's list and NOT overdue.
     * This allows re-adding overdue tasks with new due dates.
     * 
     * @param templateTaskId the template task ID to check
     * @return true if task is in today's list and not overdue, false otherwise
     */
    boolean isTaskInTodaysListAndNotOverdue(String templateTaskId);
    
    /**
     * Gets all available tasks with their full details.
     * 
     * @return list of all available tasks as TaskAvailable objects
     */
    List<TaskAvailable> getAllAvailableTasksWithDetails();
}