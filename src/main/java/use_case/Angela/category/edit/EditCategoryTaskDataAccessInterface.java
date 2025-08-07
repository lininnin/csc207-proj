package use_case.Angela.category.edit;

import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import java.util.List;

/**
 * Interface for task-related data access operations needed by the EditCategory use case.
 * This interface provides methods to update tasks when a category is edited.
 */
public interface EditCategoryTaskDataAccessInterface {
    
    /**
     * Finds all available tasks that have the specified category.
     *
     * @param categoryId The ID of the category
     * @return List of available tasks with this category (never null)
     */
    List<TaskAvailable> findAvailableTasksByCategory(String categoryId);
    
    /**
     * Finds all today's tasks that have the specified category.
     *
     * @param categoryId The ID of the category
     * @return List of today's tasks with this category (never null)
     */
    List<Task> findTodaysTasksByCategory(String categoryId);
    
    /**
     * Updates the category of an available task.
     * This method does not propagate changes to today's tasks.
     *
     * @param taskId The ID of the task to update
     * @param newCategoryId The new category ID (can be empty string)
     * @return true if the update was successful, false otherwise
     */
    boolean updateAvailableTaskCategory(String taskId, String newCategoryId);
    
    /**
     * Updates the category of a today's task.
     *
     * @param taskId The ID of the task to update
     * @param newCategoryId The new category ID (can be empty string)
     * @return true if the update was successful, false otherwise
     */
    boolean updateTodaysTaskCategory(String taskId, String newCategoryId);
}