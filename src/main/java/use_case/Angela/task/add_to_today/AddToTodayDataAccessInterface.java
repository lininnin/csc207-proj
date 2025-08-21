package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import entity.Angela.Task.TaskAvailableInterf;
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
     * @return the TaskAvailableInterf if found, null otherwise
     */
    TaskAvailableInterf getAvailableTaskById(String taskId);
    
    /**
     * Adds a task to today's list with the specified priority and due date.
     * 
     * @param taskAvailable the available task template to add
     * @param priority the priority for today's task (optional, can be null)
     * @param dueDate the due date for today's task (optional, can be null)
     * @return the created TaskInterf instance that was added to today's list
     */
    TaskInterf addTaskToToday(TaskAvailableInterf taskAvailable, Task.Priority priority, java.time.LocalDate dueDate);
    
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
     * @return list of all available tasks as TaskAvailableInterf objects
     */
    List<TaskAvailableInterf> getAllAvailableTasksWithDetails();
    
    /**
     * Checks if an exact duplicate task already exists in today's list.
     * Two tasks are considered exact duplicates if they have the same template ID,
     * priority, and due date.
     * 
     * @param templateTaskId the template task ID to check
     * @param priority the priority to check (can be null)
     * @param dueDate the due date to check (can be null)
     * @return true if an exact duplicate exists, false otherwise
     */
    boolean isExactDuplicateInTodaysList(String templateTaskId, Task.Priority priority, java.time.LocalDate dueDate);
}