package entity.Angela;

import java.util.*;

/**
 * Manages categories for tasks, events, and goals.
 * Implements singleton pattern to ensure consistent category management across the application.
 */
public class CategoryManager {
    private static CategoryManager instance;
    private final Set<String> categories;
    private final List<CategoryChangeListener> listeners;

    /**
     * Interface for listening to category changes.
     */
    public interface CategoryChangeListener {
        void onCategoryAdded(String category);
        void onCategoryRemoved(String category);
        void onCategoriesLoaded(Set<String> categories);
    }

    private CategoryManager() {
        this.categories = new TreeSet<>(String.CASE_INSENSITIVE_ORDER); // Alphabetical order
        this.listeners = new ArrayList<>();
        initializeDefaultCategories();
    }

    /**
     * Gets the singleton instance of CategoryManager.
     *
     * @return The CategoryManager instance
     */
    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    /**
     * Initializes default categories as per design requirements.
     */
    private void initializeDefaultCategories() {
        categories.add("Work");
        categories.add("Personal");
        categories.add("Health");
        categories.add("Study");
    }

    /**
     * Adds a new category.
     *
     * @param category The category name to add
     * @return true if category was added, false if it already exists
     * @throws IllegalArgumentException if category is null or empty
     */
    public boolean addCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        String trimmedCategory = category.trim();
        boolean added = categories.add(trimmedCategory);

        if (added) {
            notifyCategoryAdded(trimmedCategory);
        }

        return added;
    }

    /**
     * Removes a category.
     * WARNING: This will leave tasks/events/goals with empty category field.
     *
     * @param category The category to remove
     * @return true if category was removed, false if it didn't exist
     */
    public boolean removeCategory(String category) {
        if (category == null) {
            return false;
        }

        boolean removed = categories.remove(category);

        if (removed) {
            notifyCategoryRemoved(category);
        }

        return removed;
    }

    /**
     * Edits an existing category name.
     * This will update all tasks/events/goals using the old category name.
     *
     * @param oldCategory The current category name
     * @param newCategory The new category name
     * @return true if category was edited, false if old category doesn't exist or new already exists
     */
    public boolean editCategory(String oldCategory, String newCategory) {
        if (oldCategory == null || newCategory == null || newCategory.trim().isEmpty()) {
            return false;
        }

        String trimmedNew = newCategory.trim();

        // Check if old category exists
        if (!categories.contains(oldCategory)) {
            return false;
        }

        // Check if new category already exists (case-insensitive)
        if (!oldCategory.equalsIgnoreCase(trimmedNew) && categories.contains(trimmedNew)) {
            return false;
        }

        // Remove old and add new
        // First, find and remove the exact entry (considering case-insensitive matching)
        String actualOldCategory = null;
        for (String cat : categories) {
            if (cat.equalsIgnoreCase(oldCategory)) {
                actualOldCategory = cat;
                break;
            }
        }
        
        if (actualOldCategory != null) {
            categories.remove(actualOldCategory);
        }
        categories.add(trimmedNew);

        // Notify listeners about the change
        notifyCategoryRemoved(oldCategory);
        notifyCategoryAdded(trimmedNew);

        return true;
    }

    /**
     * Checks if a category exists.
     *
     * @param category The category to check
     * @return true if category exists (case-insensitive)
     */
    public boolean hasCategory(String category) {
        return category != null && categories.contains(category);
    }

    /**
     * Gets all categories in alphabetical order.
     *
     * @return An unmodifiable sorted set of categories
     */
    public Set<String> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    /**
     * Gets all categories as a sorted list.
     *
     * @return A sorted list of categories
     */
    public List<String> getCategoriesList() {
        return new ArrayList<>(categories);
    }

    /**
     * Loads categories from a collection (used when loading from file).
     *
     * @param loadedCategories The categories to load
     */
    public void loadCategories(Collection<String> loadedCategories) {
        categories.clear();
        if (loadedCategories != null) {
            categories.addAll(loadedCategories);
        }

        // Ensure default categories exist
        initializeDefaultCategories();

        notifyCategoriesLoaded();
    }

    /**
     * Adds a category change listener.
     *
     * @param listener The listener to add
     */
    public void addCategoryChangeListener(CategoryChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a category change listener.
     *
     * @param listener The listener to remove
     */
    public void removeCategoryChangeListener(CategoryChangeListener listener) {
        listeners.remove(listener);
    }

    // Notification methods
    private void notifyCategoryAdded(String category) {
        for (CategoryChangeListener listener : listeners) {
            listener.onCategoryAdded(category);
        }
    }

    private void notifyCategoryRemoved(String category) {
        for (CategoryChangeListener listener : listeners) {
            listener.onCategoryRemoved(category);
        }
    }

    private void notifyCategoriesLoaded() {
        for (CategoryChangeListener listener : listeners) {
            listener.onCategoriesLoaded(categories);
        }
    }

    /**
     * Resets the CategoryManager instance (mainly for testing).
     */
    public static synchronized void resetInstance() {
        instance = null;
    }
}