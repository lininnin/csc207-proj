package data_access;

import entity.Category;
import use_case.Angela.category.CategoryGateway;
import java.util.*;

/**
 * In-memory implementation of CategoryGateway for quick demos.
 */
public class InMemoryCategoryGateway implements CategoryGateway {
    private final Map<String, Category> categories = new HashMap<>();
    private int nextId = 4; // Start at 4 since we have 3 default categories

    public InMemoryCategoryGateway() {
        // Add some default categories for demo
        saveCategory(new Category("1", "Work", null));
        saveCategory(new Category("2", "Personal", null));
        saveCategory(new Category("3", "Health", null));
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
        return new ArrayList<>(categories.values());
    }

    @Override
    public Category getCategoryById(String id) {
        return categories.get(id);
    }

    @Override
    public Category getCategoryByName(String name) {
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
}