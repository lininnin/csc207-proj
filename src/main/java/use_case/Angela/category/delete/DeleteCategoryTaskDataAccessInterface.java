package use_case.Angela.category.delete;

import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;

import java.util.List;

/**
 * Interface for task-specific data access operations in the delete category use case.
 * This interface follows ISP by only containing methods related to updating tasks
 * when their category is deleted.
 */
public interface DeleteCategoryTaskDataAccessInterface {

    /**
     * Finds all available tasks that reference the given category ID.
     * This is needed to update tasks when their category is deleted.
     *
     * @param categoryId The category ID to search for
     * @return List of TaskAvailable that have this category
     */
    List<TaskAvailable> findAvailableTasksByCategory(String categoryId);

    /**
     * Finds all today's tasks that reference the given category ID.
     * This is needed to update tasks when their category is deleted.
     *
     * @param categoryId The category ID to search for
     * @return List of Task that have this category
     */
    List<Task> findTodaysTasksByCategory(String categoryId);

    /**
     * Updates the category field for an available task.
     * Used to clear the category when a category is deleted.
     *
     * @param taskId The ID of the task to update
     * @param newCategoryId The new category ID (empty string for no category)
     * @return true if update succeeded, false otherwise
     */
    boolean updateAvailableTaskCategory(String taskId, String newCategoryId);

    /**
     * Updates the category field for a today's task.
     * Used to clear the category when a category is deleted.
     *
     * @param taskId The ID of the task to update
     * @param newCategoryId The new category ID (empty string for no category)
     * @return true if update succeeded, false otherwise
     */
    boolean updateTodaysTaskCategory(String taskId, String newCategoryId);
}