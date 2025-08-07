package use_case.Angela.task.edit_available;

import entity.Angela.Task.TaskAvailable;

/**
 * Data access interface for editing available tasks.
 * Following the DAO pattern from Alex's modules.
 */
public interface EditAvailableTaskDataAccessInterface {
    /**
     * Updates an existing available task with new information.
     *
     * @param taskId The ID of the task to update
     * @param newName The new name for the task
     * @param newDescription The new description for the task
     * @param newCategoryId The new category ID for the task (can be empty)
     * @param isOneTime Whether the task is a one-time task
     * @return true if the update was successful, false otherwise
     */
    boolean updateAvailableTask(String taskId, String newName, String newDescription, 
                                String newCategoryId, boolean isOneTime);

    /**
     * Gets an available task by its ID.
     *
     * @param taskId The task ID
     * @return The TaskAvailable object, or null if not found
     */
    TaskAvailable getTaskAvailableById(String taskId);

    /**
     * Checks if a task with the given name exists in the same category.
     * Used for duplicate checking during edit.
     *
     * @param name The task name to check
     * @param categoryId The category ID to check within
     * @param excludeTaskId The task ID to exclude from the check (the task being edited)
     * @return true if a duplicate exists, false otherwise
     */
    boolean taskExistsWithNameAndCategoryExcluding(String name, String categoryId, String excludeTaskId);
    
    /**
     * Gets all available tasks with their complete information including isOneTime flag.
     * Needed for the view to display the One Time column.
     *
     * @return List of all TaskAvailable objects
     */
    java.util.List<TaskAvailable> getAllAvailableTasksWithDetails();
}