package use_case.Angela.task.delete;

import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;

import java.util.List;

/**
 * Interface for data access operations related to deleting tasks.
 * This interface handles deletion from both Available Tasks (templates) and Today's Tasks (instances).
 * 
 * According to the use case specification:
 * - Deleting from Available removes the task template completely from the system
 * - If task exists in Today's list, user gets warning and both are deleted on confirmation
 */
public interface DeleteTaskDataAccessInterface {

    /**
     * Retrieves a task template by its ID.
     *
     * @param taskId The ID of the task template to find
     * @return The TaskAvailableInterf if found, null otherwise
     */
    TaskAvailableInterf getTaskAvailableById(String taskId);

    /**
     * Retrieves a today's task by its ID.
     *
     * @param taskId The ID of the today's task to find
     * @return The TaskInterf if found, null otherwise
     */
    TaskInterf getTodaysTaskById(String taskId);

    /**
     * Checks whether the given task template exists in Available Tasks.
     *
     * @param taskAvailable The TaskAvailableInterf to check
     * @return true if present, false otherwise
     */
    boolean existsInAvailable(TaskAvailableInterf taskAvailable);

    /**
     * Checks whether the given task instance exists in Today's Tasks.
     *
     * @param task The TaskInterf to check
     * @return true if present, false otherwise
     */
    boolean existsInToday(TaskInterf task);

    /**
     * Checks if a task template exists in Today's Tasks by its template ID.
     * This is used to determine if we need to show the warning about deleting from both lists.
     *
     * @param templateTaskId The ID of the task template
     * @return true if any today's task references this template, false otherwise
     */
    boolean templateExistsInToday(String templateTaskId);

    /**
     * Removes the given task template from Available Tasks.
     * This is a permanent deletion of the template.
     *
     * @param taskAvailable The TaskAvailableInterf to remove
     * @return true if removed successfully, false otherwise
     */
    boolean deleteFromAvailable(TaskAvailableInterf taskAvailable);

    /**
     * Removes all today's task instances that reference the given template ID.
     * This is used when deleting a template that exists in Today's list.
     *
     * @param templateTaskId The template task ID
     * @return true if all instances were removed successfully, false otherwise
     */
    boolean deleteAllTodaysTasksWithTemplate(String templateTaskId);

    /**
     * Completely removes a task from both Available and Today's lists.
     * This is used when user confirms deletion of a task that exists in both lists.
     *
     * @param templateTaskId The template task ID to remove completely
     * @return true if removed from both lists successfully, false otherwise
     */
    boolean deleteTaskCompletely(String templateTaskId);

    /**
     * Gets all today's tasks that reference a specific template.
     * Used to provide information in the deletion warning.
     *
     * @param templateTaskId The template task ID
     * @return List of TaskInterf instances that reference this template
     */
    List<TaskInterf> getTodaysTasksByTemplate(String templateTaskId);

    /**
     * @return List of all available task templates
     */
    List<TaskAvailableInterf> getAllAvailableTaskTemplates();

    /**
     * @return List of all today's tasks
     */
    List<TaskInterf> getAllTodaysTasks();

    /**
     * Checks if the given task is a target task for any goal.
     * This is used to warn the user before deleting a task that is referenced by goals.
     *
     * @param taskId The ID of the task to check
     * @return List of goal names that reference this task as target, empty if none
     */
    List<String> getGoalNamesTargetingTask(String taskId);
}