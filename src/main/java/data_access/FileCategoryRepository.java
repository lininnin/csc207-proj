package data_access;

import entity.Angela.CategoryManager;
import use_case.repository.CategoryRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * File-based implementation of CategoryRepository.
 * Stores categories in categories.json and syncs with CategoryManager.
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
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean inArray = false;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (line.equals("[")) {
                        inArray = true;
                        continue;
                    } else if (line.equals("]")) {
                        break;
                    }

                    if (inArray && line.startsWith("\"")) {
                        String category = line
                                .replace("\"", "")
                                .replace(",", "")
                                .trim();
                        if (!category.isEmpty()) {
                            categories.add(category);
                        }
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(getCategoriesFilePath()))) {
            writer.println("[");

            List<String> categoryList = new ArrayList<>(categories);
            for (int i = 0; i < categoryList.size(); i++) {
                writer.print("  \"" + escapeJson(categoryList.get(i)) + "\"");
                if (i < categoryList.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }

            writer.println("]");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save categories", e);
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
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

    /**
     * Gets the file path for testing purposes.
     * Can be overridden in tests.
     *
     * @return The categories file path
     */
    protected String getCategoriesFilePath() {
        return CATEGORIES_FILE;
    }
}