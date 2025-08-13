package use_case.Angela.category.delete;

import entity.Category;

/**
 * Interface for category-specific data access operations in the delete category use case.
 * This interface follows ISP by only containing methods related to category operations.
 * Separate from task/event update operations.
 */
public interface DeleteCategoryCategoryDataAccessInterface {

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
     * Gets the total number of categories in the system.
     * Used to enforce minimum category requirements.
     *
     * @return The total number of categories
     */
    int getCategoryCount();
}