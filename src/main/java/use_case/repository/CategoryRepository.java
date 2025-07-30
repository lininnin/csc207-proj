package use_case.repository;

import java.util.Set;

/**
 * Repository interface for category persistence.
 * Manages loading and saving of category data.
 */
public interface CategoryRepository {
    /**
     * Loads all categories from storage.
     *
     * @return Set of category names
     */
    Set<String> loadCategories();

    /**
     * Saves all categories to storage.
     *
     * @param categories Set of category names to save
     */
    void saveCategories(Set<String> categories);

    /**
     * Adds a new category to storage.
     *
     * @param category The category to add
     * @return true if added successfully, false if already exists
     */
    boolean addCategory(String category);

    /**
     * Removes a category from storage.
     *
     * @param category The category to remove
     * @return true if removed successfully, false if not found
     */
    boolean removeCategory(String category);

    /**
     * Updates a category name in storage.
     *
     * @param oldCategory The current category name
     * @param newCategory The new category name
     * @return true if updated successfully, false if old not found or new already exists
     */
    boolean updateCategory(String oldCategory, String newCategory);
}