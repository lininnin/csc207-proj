package use_case.Angela.category;

import entity.Category;
import java.util.List;

/**
 * Gateway interface for category data access operations.
 * Follows the Clean Architecture pattern to decouple use cases from data persistence.
 */
public interface CategoryGateway {

    /**
     * Saves a new category.
     *
     * @param category The category to save
     * @throws IllegalArgumentException if a category with the same name already exists
     */
    void saveCategory(Category category);

    /**
     * Retrieves all categories.
     *
     * @return List of all categories
     */
    List<Category> getAllCategories();

    /**
     * Finds a category by its ID.
     *
     * @param id The category ID
     * @return The category, or null if not found
     */
    Category getCategoryById(String id);

    /**
     * Finds a category by its name.
     *
     * @param name The category name
     * @return The category, or null if not found
     */
    Category getCategoryByName(String name);

    /**
     * Updates an existing category.
     *
     * @param category The category with updated information
     * @return true if updated successfully, false if category not found
     */
    boolean updateCategory(Category category);

    /**
     * Deletes a category by its ID.
     * Also updates all entities using this category to have empty category.
     *
     * @param categoryId The ID of the category to delete
     * @return true if deleted successfully, false if category not found
     */
    boolean deleteCategory(String categoryId);

    /**
     * Checks if a category name already exists.
     *
     * @param name The category name to check
     * @return true if the name exists, false otherwise
     */
    boolean categoryNameExists(String name);

    /**
     * Gets the next available ID for a new category.
     *
     * @return A unique ID string
     */
    String getNextCategoryId();
}