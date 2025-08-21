package use_case.Angela.task.create;

import entity.Category;

/**
 * Data access interface for category operations required by the create task use case.
 * Follows Interface Segregation Principle.
 */
public interface CreateTaskCategoryDataAccessInterface {
    /**
     * Finds a category by its ID.
     *
     * @param id The category ID
     * @return The category, or null if not found
     */
    Category getCategoryById(String id);
}