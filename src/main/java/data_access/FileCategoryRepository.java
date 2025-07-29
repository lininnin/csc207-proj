package data_access;

import entity.Angela.CategoryManager;
import use_case.repository.CategoryRepository;

import org.json.JSONArray;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * File-based implementation of CategoryRepository using org.json.
 */
public class FileCategoryRepository implements CategoryRepository {
    private static final String CATEGORIES_FILE = "data/categories.json";
    private static final Path DATA_DIR = Paths.get("data");
    private final CategoryManager categoryManager;

    public FileCategoryRepository() {
        this.categoryManager = CategoryManager.getInstance();

        ensureDataDirectoryExists();
        initializeCategories();
    }

    private void ensureDataDirectoryExists() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory", e);
        }
    }

    private void initializeCategories() {
        // Load categories from file and sync with CategoryManager
        Set<String> loadedCategories = loadCategories();
        categoryManager.loadCategories(loadedCategories);

        // Add listener to save changes
        categoryManager.addCategoryChangeListener(new CategoryManager.CategoryChangeListener() {
            @Override
            public void onCategoryAdded(String category) {
                saveCategories(categoryManager.getCategories());
            }

            @Override
            public void onCategoryRemoved(String category) {
                saveCategories(categoryManager.getCategories());
            }

            @Override
            public void onCategoriesLoaded(Set<String> categories) {
                saveCategories(categories);
            }
        });
    }

    @Override
    public Set<String> loadCategories() {
        File file = new File(getCategoriesFilePath());
        Set<String> categories = new TreeSet<>();

        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONArray array = new JSONArray(content);

                for (int i = 0; i < array.length(); i++) {
                    String category = array.getString(i);
                    if (!category.isEmpty()) {
                        categories.add(category);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading categories: " + e.getMessage());
            }
        }

        // Add default categories if empty
        if (categories.isEmpty()) {
            categories.addAll(Arrays.asList("Work", "Personal", "Health", "Study"));
        }

        return categories;
    }

    @Override
    public void saveCategories(Set<String> categories) {
        try {
            JSONArray array = new JSONArray(categories);

            try (FileWriter writer = new FileWriter(getCategoriesFilePath())) {
                writer.write(array.toString(2)); // Pretty print
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save categories", e);
        }
    }

    @Override
    public boolean addCategory(String category) {
        boolean added = categoryManager.addCategory(category);
        if (added) {
            saveCategories(categoryManager.getCategories());
        }
        return added;
    }

    @Override
    public boolean removeCategory(String category) {
        boolean removed = categoryManager.removeCategory(category);
        if (removed) {
            saveCategories(categoryManager.getCategories());
        }
        return removed;
    }

    @Override
    public boolean updateCategory(String oldCategory, String newCategory) {
        boolean updated = categoryManager.editCategory(oldCategory, newCategory);
        if (updated) {
            saveCategories(categoryManager.getCategories());
        }
        return updated;
    }

    protected String getCategoriesFilePath() {
        return CATEGORIES_FILE;
    }
}