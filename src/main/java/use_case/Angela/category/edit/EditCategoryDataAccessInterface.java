package use_case.Angela.category.edit;

import entity.Category;

import java.util.List;

/**
 * Interface for data access operations related to editing categories.
 * This interface is used only by the EditCategory use case.
 */
public interface EditCategoryDataAccessInterface {

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
     * Checks if a category with the given name already exists (case-insensitive).
     * Used for validation during editing to prevent duplicate names.
     *
     * @param name The category name to check
     * @param excludeCategoryId The ID of the category being edited (to exclude from duplicate check)
     * @return true if a category with this name exists, false otherwise
     */
    boolean existsByNameExcluding(String name, String excludeCategoryId);

    /**
     * Updates an existing category in the data store.
     * The category is identified by its ID and its properties are updated.
     *
     * @param updatedCategory The Category object with updated properties
     * @return true if update succeeded, false otherwise
     */
    boolean updateCategory(Category updatedCategory);

    /**
     * @return List of all available categories
     */
    List<Category> getAllCategories();
}