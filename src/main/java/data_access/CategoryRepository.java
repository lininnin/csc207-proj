package data_access;

import entity.Category;
import use_case.Angela.category.CategoryGateway;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JSON-based implementation of CategoryGateway.
 * Persists categories to a categories.json file.
 */
public class CategoryRepository implements CategoryGateway {
    private static final String CATEGORIES_FILE = "categories.json";
    private final List<Category> categories;

    public CategoryRepository() {
        this.categories = new ArrayList<>();
        loadCategories();
    }

    @Override
    public void saveCategory(Category category) {
        if (categoryNameExists(category.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + category.getName());
        }
        categories.add(category);
        saveToFile();
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    @Override
    public Category getCategoryById(String id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateCategory(Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(category.getId())) {
                categories.set(i, category);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteCategory(String categoryId) {
        boolean removed = categories.removeIf(c -> c.getId().equals(categoryId));
        if (removed) {
            saveToFile();
            // Note: In a real implementation, we would also update all tasks/events/goals
            // that use this category to have empty category
        }
        return removed;
    }

    @Override
    public boolean categoryNameExists(String name) {
        return categories.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }

    @Override
    public String getNextCategoryId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Loads categories from the JSON file.
     */
    private void loadCategories() {
        File file = new File(CATEGORIES_FILE);
        if (!file.exists()) {
            // Initialize with some default categories
            initializeDefaultCategories();
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(CATEGORIES_FILE)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Category category = new Category(
                        json.getString("id"),
                        json.getString("name"),
                        json.optString("color", "#808080")
                );
                categories.add(category);
            }
        } catch (IOException e) {
            System.err.println("Error loading categories: " + e.getMessage());
            initializeDefaultCategories();
        }
    }

    /**
     * Saves categories to the JSON file.
     */
    private void saveToFile() {
        JSONArray jsonArray = new JSONArray();

        for (Category category : categories) {
            JSONObject json = new JSONObject();
            json.put("id", category.getId());
            json.put("name", category.getName());
            json.put("color", category.getColor());
            jsonArray.put(json);
        }

        try (FileWriter writer = new FileWriter(CATEGORIES_FILE)) {
            writer.write(jsonArray.toString(2));
        } catch (IOException e) {
            System.err.println("Error saving categories: " + e.getMessage());
        }
    }

    /**
     * Initializes default categories for new users.
     */
    private void initializeDefaultCategories() {
        categories.add(new Category(getNextCategoryId(), "Work", "#4169E1"));
        categories.add(new Category(getNextCategoryId(), "Personal", "#32CD32"));
        categories.add(new Category(getNextCategoryId(), "Academic", "#FF6347"));
        categories.add(new Category(getNextCategoryId(), "Health", "#FFD700"));
        saveToFile();
    }
}