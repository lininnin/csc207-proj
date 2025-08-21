package use_case.Angela.today_so_far;

import entity.Category;

/**
 * Interface for reading category data for the Today So Far use case.
 */
public interface CategoryReadDataAccessInterface {
    /**
     * Finds a category by its ID.
     *
     * @param id The category ID
     * @return The category, or null if not found
     */
    Category getCategoryById(String id);
}