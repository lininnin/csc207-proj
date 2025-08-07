package use_case.Angela.category.create;

import entity.Category;

import java.util.List;

/**
 * Interface for the Create Category data access object.
 * Defines methods to persist and query categories for the CreateCategory use case.
 */
public interface CreateCategoryDataAccessInterface {

    /**
     * Saves the given Category to the data store.
     *
     * @param category The Category to save
     */
    void save(Category category);

    /**
     * Checks if a category with the given name already exists (case-insensitive).
     *
     * @param name The category name to check
     * @return true if a category with this name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * @return List of all available categories
     */
    List<Category> getAllCategories();

    /**
     * @return Total count of categories
     */
    int getCategoryCount();
}