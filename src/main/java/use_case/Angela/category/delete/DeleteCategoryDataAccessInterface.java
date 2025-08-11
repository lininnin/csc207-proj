package use_case.Angela.category.delete;

import entity.Category;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;

import java.util.List;

/**
 * Interface for data access operations related to deleting categories.
 * This interface handles both category deletion and the required task updates
 * when a category is deleted (tasks must have their category field cleared).
 */
public interface DeleteCategoryDataAccessInterface {

    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId The ID of the category to find
     * @return The Category if found, null otherwise
     */
    Category getCategoryById(String categoryId);

    /**
     * Checks whether the given category exists.
     *
     * @param category The Category to check
     * @return true if present, false otherwise
     */
    boolean exists(Category category);

    /**
     * Removes the given category from the data store.
     *
     * @param category The Category to remove
     * @return true if removed successfully, false otherwise
     */
    boolean deleteCategory(Category category);

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
    
    /**
     * Gets the total number of categories in the system.
     * Used to enforce minimum category requirements.
     *
     * @return The total number of categories
     */
    int getCategoryCount();
}