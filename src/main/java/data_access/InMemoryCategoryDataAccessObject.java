package data_access;

import entity.Category;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import entity.info.Info;
import entity.info.InfoInterf;
import use_case.Angela.category.CategoryGateway;
import use_case.Angela.category.create.CreateCategoryDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryCategoryDataAccessInterface;
import use_case.Angela.category.edit.EditCategoryDataAccessInterface;
import use_case.Angela.today_so_far.CategoryReadDataAccessInterface;
import use_case.Angela.task.overdue.OverdueTasksCategoryDataAccessInterface;
import use_case.Angela.task.edit_available.EditAvailableTaskCategoryDataAccessInterface;
import use_case.Angela.task.create.CreateTaskCategoryDataAccessInterface;
import java.util.*;

/**
 * In-memory implementation of DataAccessInterfaces for category operations.
 * Following the DAO pattern: one concrete implementation implements multiple interfaces.
 * Updated to follow ISP with segregated interfaces for category operations.
 */
public class InMemoryCategoryDataAccessObject implements 
        CategoryGateway,
        CreateCategoryDataAccessInterface,
        DeleteCategoryDataAccessInterface,  // Keep for backward compatibility
        DeleteCategoryCategoryDataAccessInterface,  // New segregated interface
        EditCategoryDataAccessInterface,
        CategoryReadDataAccessInterface,
        OverdueTasksCategoryDataAccessInterface,
        EditAvailableTaskCategoryDataAccessInterface,
        CreateTaskCategoryDataAccessInterface {
    private final Map<String, Category> categories = Collections.synchronizedMap(new HashMap<>());
    private int nextId = 4; // Start at 4 since we have 3 default categories

    public InMemoryCategoryDataAccessObject() {
        // Don't add default categories - let tests control this
    }

    @Override
    public void saveCategory(Category category) {
        categories.put(category.getId(), category);

        // Update nextId if we're saving a category with a numeric ID
        try {
            int id = Integer.parseInt(category.getId());
            if (id >= nextId) {
                nextId = id + 1;
            }
        } catch (NumberFormatException e) {
            // Ignore non-numeric IDs
        }
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories = new ArrayList<>(categories.values());
        
        // Sort categories alphabetically by name (case-insensitive)
        allCategories.sort((a, b) -> {
            String nameA = a.getName();
            String nameB = b.getName();
            if (nameA == null && nameB == null) return 0;
            if (nameA == null) return 1;  // null names go to end
            if (nameB == null) return -1;
            return nameA.compareToIgnoreCase(nameB);
        });
        
        return allCategories;
    }

    @Override
    public Category getCategoryById(String id) {
        return categories.get(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        // Find category by exact name match (case-insensitive)
        return categories.values().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateCategory(Category category) {
        if (categories.containsKey(category.getId())) {
            categories.put(category.getId(), category);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCategory(String categoryId) {
        return categories.remove(categoryId) != null;
    }

    @Override
    public boolean categoryNameExists(String name) {
        return categories.values().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }

    @Override
    public String getNextCategoryId() {
        return String.valueOf(nextId++);
    }


    // ===== CreateCategoryDataAccessInterface methods =====

    @Override
    public void save(Category category) {
        // Validate category ID is not null or empty
        if (category.getId() == null || category.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Category ID is required");
        }
        saveCategory(category); // Delegate to existing method
    }

    @Override
    public boolean existsByName(String name) {
        return categoryNameExists(name); // Delegate to existing method
    }

    @Override
    public int getCategoryCount() {
        return categories.size();
    }

    // ===== DeleteCategoryDataAccessInterface methods =====

    @Override
    public boolean exists(Category category) {
        return categories.containsKey(category.getId());
    }

    @Override
    public boolean deleteCategory(Category category) {
        return deleteCategory(category.getId()); // Delegate to existing method
    }

    @Override
    public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
        // This implementation returns empty list since InMemoryCategoryGateway
        // no longer has direct access to task data (follows SRP and DIP)
        // The actual implementation is in InMemoryTaskGateway
        return new ArrayList<>();
    }

    @Override
    public List<Task> findTodaysTasksByCategory(String categoryId) {
        // This implementation returns empty list since InMemoryCategoryGateway
        // no longer has direct access to task data (follows SRP and DIP)
        // The actual implementation is in InMemoryTaskGateway
        return new ArrayList<>();
    }

    @Override
    public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
        // This implementation returns false since InMemoryCategoryGateway
        // no longer has direct access to task data (follows SRP and DIP)
        // The actual implementation is in InMemoryTaskGateway
        return false;
    }

    @Override
    public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
        // This implementation returns false since InMemoryCategoryGateway
        // no longer has direct access to task data (follows SRP and DIP)
        // The actual implementation is in InMemoryTaskGateway
        return false;
    }

    // ===== EditCategoryDataAccessInterface methods =====

    @Override
    public boolean existsByNameExcluding(String name, String excludeCategoryId) {
        return categories.values().stream()
                .anyMatch(c -> !c.getId().equals(excludeCategoryId) && 
                              c.getName().equalsIgnoreCase(name));
    }
    
    /**
     * Clears all data from this data access object for testing purposes.
     * WARNING: This will delete all categories and should only be used in tests!
     */
    public void clearAllData() {
        categories.clear();
    }
}