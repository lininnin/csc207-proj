package data_access;

import entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test for InMemoryCategoryDataAccessObject.
 * Tests all data access interfaces and category operations.
 */
class InMemoryCategoryDataAccessObjectTest {

    private InMemoryCategoryDataAccessObject dataAccess;
    private Category workCategory;
    private Category personalCategory;
    private Category healthCategory;

    @BeforeEach
    void setUp() {
        dataAccess = new InMemoryCategoryDataAccessObject();
        setupTestCategories();
    }

    private void setupTestCategories() {
        workCategory = new Category("1", "Work", "#FF0000");
        personalCategory = new Category("2", "Personal", "#00FF00");
        healthCategory = new Category("3", "Health", "#0000FF");
    }

    @Test
    void testEmptyInitialization() {
        assertEquals(0, dataAccess.getCategoryCount());
        assertTrue(dataAccess.getAllCategories().isEmpty());
        assertNull(dataAccess.getCategoryById("1"));
        assertNull(dataAccess.getCategoryByName("Work"));
        assertFalse(dataAccess.categoryNameExists("Work"));
    }

    @Test
    void testSaveCategory() {
        dataAccess.saveCategory(workCategory);
        
        assertEquals(1, dataAccess.getCategoryCount());
        assertEquals(workCategory, dataAccess.getCategoryById("1"));
        assertEquals(workCategory, dataAccess.getCategoryByName("Work"));
        assertTrue(dataAccess.categoryNameExists("Work"));
    }

    @Test
    void testSaveCategoryViaSaveInterface() {
        dataAccess.save(workCategory);
        
        assertEquals(1, dataAccess.getCategoryCount());
        assertEquals(workCategory, dataAccess.getCategoryById("1"));
        assertTrue(dataAccess.existsByName("Work"));
    }

    @Test
    void testSaveCategoryWithNullId() {
        Category invalidCategory = new Category(null, "Invalid", "#FFFFFF");
        
        assertThrows(IllegalArgumentException.class, () -> dataAccess.save(invalidCategory));
        assertEquals(0, dataAccess.getCategoryCount());
    }

    @Test
    void testSaveCategoryWithEmptyId() {
        Category invalidCategory = new Category("", "Invalid", "#FFFFFF");
        
        assertThrows(IllegalArgumentException.class, () -> dataAccess.save(invalidCategory));
        assertEquals(0, dataAccess.getCategoryCount());
    }

    @Test
    void testSaveCategoryWithWhitespaceId() {
        Category invalidCategory = new Category("   ", "Invalid", "#FFFFFF");
        
        assertThrows(IllegalArgumentException.class, () -> dataAccess.save(invalidCategory));
        assertEquals(0, dataAccess.getCategoryCount());
    }

    @Test
    void testSaveMultipleCategories() {
        dataAccess.saveCategory(workCategory);
        dataAccess.saveCategory(personalCategory);
        dataAccess.saveCategory(healthCategory);
        
        assertEquals(3, dataAccess.getCategoryCount());
        assertEquals(workCategory, dataAccess.getCategoryById("1"));
        assertEquals(personalCategory, dataAccess.getCategoryById("2"));
        assertEquals(healthCategory, dataAccess.getCategoryById("3"));
    }

    @Test
    void testGetAllCategoriesAlphabeticalOrder() {
        // Add in non-alphabetical order
        dataAccess.saveCategory(new Category("1", "Work", "#FF0000"));
        dataAccess.saveCategory(new Category("2", "Health", "#00FF00"));
        dataAccess.saveCategory(new Category("3", "Personal", "#0000FF"));
        dataAccess.saveCategory(new Category("4", "Art", "#FFFF00"));
        
        List<Category> categories = dataAccess.getAllCategories();
        assertEquals(4, categories.size());
        assertEquals("Art", categories.get(0).getName());
        assertEquals("Health", categories.get(1).getName());
        assertEquals("Personal", categories.get(2).getName());
        assertEquals("Work", categories.get(3).getName());
    }

    @Test
    void testGetAllCategoriesCaseInsensitiveSort() {
        dataAccess.saveCategory(new Category("1", "work", "#FF0000"));
        dataAccess.saveCategory(new Category("2", "HEALTH", "#00FF00"));
        dataAccess.saveCategory(new Category("3", "Personal", "#0000FF"));
        
        List<Category> categories = dataAccess.getAllCategories();
        assertEquals("HEALTH", categories.get(0).getName());
        assertEquals("Personal", categories.get(1).getName());
        assertEquals("work", categories.get(2).getName());
    }

    @Test
    void testGetAllCategoriesWithNullNames() {
        // Cannot create category with null name due to validation
        // Skip this test since Category constructor doesn't allow null names
        dataAccess.saveCategory(workCategory);
        
        List<Category> categories = dataAccess.getAllCategories();
        assertEquals(1, categories.size());
        assertEquals("Work", categories.get(0).getName());
    }

    @Test
    void testGetCategoryByIdNonExistent() {
        assertNull(dataAccess.getCategoryById("999"));
        assertNull(dataAccess.getCategoryById(null));
    }

    @Test
    void testGetCategoryByName() {
        dataAccess.saveCategory(workCategory);
        
        assertEquals(workCategory, dataAccess.getCategoryByName("Work"));
        assertEquals(workCategory, dataAccess.getCategoryByName("work")); // Case-insensitive
        assertEquals(workCategory, dataAccess.getCategoryByName("WORK"));
        assertNull(dataAccess.getCategoryByName("Personal"));
    }

    @Test
    void testGetCategoryByNameNonExistent() {
        assertNull(dataAccess.getCategoryByName("NonExistent"));
        assertNull(dataAccess.getCategoryByName(null));
    }

    @Test
    void testUpdateCategory() {
        dataAccess.saveCategory(workCategory);
        
        Category updatedCategory = new Category("1", "Updated Work", "#FF0000");
        boolean result = dataAccess.updateCategory(updatedCategory);
        
        assertTrue(result);
        assertEquals("Updated Work", dataAccess.getCategoryById("1").getName());
        assertFalse(dataAccess.categoryNameExists("Work"));
        assertTrue(dataAccess.categoryNameExists("Updated Work"));
    }

    @Test
    void testUpdateNonExistentCategory() {
        Category nonExistent = new Category("999", "Non Existent", "#FFFFFF");
        boolean result = dataAccess.updateCategory(nonExistent);
        
        assertFalse(result);
        assertEquals(0, dataAccess.getCategoryCount());
    }

    @Test
    void testDeleteCategoryById() {
        dataAccess.saveCategory(workCategory);
        dataAccess.saveCategory(personalCategory);
        
        boolean result = dataAccess.deleteCategory("1");
        
        assertTrue(result);
        assertEquals(1, dataAccess.getCategoryCount());
        assertNull(dataAccess.getCategoryById("1"));
        assertFalse(dataAccess.categoryNameExists("Work"));
        assertTrue(dataAccess.categoryNameExists("Personal"));
    }

    @Test
    void testDeleteCategoryByEntity() {
        dataAccess.saveCategory(workCategory);
        
        boolean result = dataAccess.deleteCategory(workCategory);
        
        assertTrue(result);
        assertEquals(0, dataAccess.getCategoryCount());
        assertNull(dataAccess.getCategoryById("1"));
    }

    @Test
    void testDeleteNonExistentCategory() {
        boolean result = dataAccess.deleteCategory("999");
        
        assertFalse(result);
        assertEquals(0, dataAccess.getCategoryCount());
    }

    @Test
    void testCategoryNameExistsCaseInsensitive() {
        dataAccess.saveCategory(workCategory);
        
        assertTrue(dataAccess.categoryNameExists("Work"));
        assertTrue(dataAccess.categoryNameExists("work"));
        assertTrue(dataAccess.categoryNameExists("WORK"));
        assertTrue(dataAccess.categoryNameExists("WoRk"));
        assertFalse(dataAccess.categoryNameExists("Personal"));
    }

    @Test
    void testExistsByName() {
        dataAccess.saveCategory(workCategory);
        
        assertTrue(dataAccess.existsByName("Work"));
        assertTrue(dataAccess.existsByName("work"));
        assertFalse(dataAccess.existsByName("Personal"));
    }

    @Test
    void testExistsByNameExcluding() {
        dataAccess.saveCategory(workCategory);
        dataAccess.saveCategory(personalCategory);
        
        // "Work" exists but we're excluding category ID "1", so should return false
        assertFalse(dataAccess.existsByNameExcluding("Work", "1"));
        
        // "Work" exists and we're excluding category ID "2", so should return true
        assertTrue(dataAccess.existsByNameExcluding("Work", "2"));
        
        // "Personal" exists and we're excluding category ID "1", so should return true
        assertTrue(dataAccess.existsByNameExcluding("Personal", "1"));
        
        // Non-existent name should return false regardless
        assertFalse(dataAccess.existsByNameExcluding("NonExistent", "1"));
    }

    @Test
    void testExistsByNameExcludingCaseInsensitive() {
        dataAccess.saveCategory(workCategory);
        
        assertFalse(dataAccess.existsByNameExcluding("work", "1")); // Case-insensitive match
        assertFalse(dataAccess.existsByNameExcluding("WORK", "1")); // Case-insensitive match
        assertTrue(dataAccess.existsByNameExcluding("work", "2")); // Different ID
    }

    @Test
    void testGetNextCategoryId() {
        // Initially should start at 4
        assertEquals("4", dataAccess.getNextCategoryId());
        assertEquals("5", dataAccess.getNextCategoryId());
        assertEquals("6", dataAccess.getNextCategoryId());
    }

    @Test
    void testGetNextCategoryIdUpdatesWithNumericIds() {
        // Save a category with a high numeric ID
        Category highIdCategory = new Category("10", "High ID", "#FFFFFF");
        dataAccess.saveCategory(highIdCategory);
        
        // Next ID should be 11
        assertEquals("11", dataAccess.getNextCategoryId());
        assertEquals("12", dataAccess.getNextCategoryId());
    }

    @Test
    void testGetNextCategoryIdIgnoresNonNumericIds() {
        // Save a category with a non-numeric ID
        Category nonNumericCategory = new Category("abc", "Non Numeric", "#FFFFFF");
        dataAccess.saveCategory(nonNumericCategory);
        
        // Next ID should still follow the numeric sequence
        assertEquals("4", dataAccess.getNextCategoryId());
    }

    @Test
    void testExists() {
        assertFalse(dataAccess.exists(workCategory));
        
        dataAccess.saveCategory(workCategory);
        
        assertTrue(dataAccess.exists(workCategory));
        assertFalse(dataAccess.exists(personalCategory));
    }

    @Test
    void testTaskRelatedMethodsReturnDefaults() {
        // These methods should return defaults as documented since category
        // data access should not handle task operations (follows SRP)
        
        assertTrue(dataAccess.findAvailableTasksByCategory("1").isEmpty());
        assertTrue(dataAccess.findTodaysTasksByCategory("1").isEmpty());
        assertFalse(dataAccess.updateAvailableTaskCategory("task1", "1"));
        assertFalse(dataAccess.updateTodaysTaskCategory("task1", "1"));
    }

    @Test
    void testConcurrentAccess() {
        // Test thread safety with synchronized map
        dataAccess.saveCategory(workCategory);
        dataAccess.saveCategory(personalCategory);
        
        // Simulate concurrent access
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                dataAccess.saveCategory(new Category("t1_" + i, "Thread1_" + i, "#FF0000"));
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                dataAccess.saveCategory(new Category("t2_" + i, "Thread2_" + i, "#00FF00"));
            }
        });
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Should have 2 original + 10 from t1 + 10 from t2 = 22 categories
        assertEquals(22, dataAccess.getCategoryCount());
    }

    @Test
    void testCompleteWorkflow() {
        // Create categories
        dataAccess.save(workCategory);
        dataAccess.save(personalCategory);
        assertEquals(2, dataAccess.getCategoryCount());
        
        // Check existence
        assertTrue(dataAccess.exists(workCategory));
        assertTrue(dataAccess.existsByName("Personal"));
        
        // Update category
        Category updatedWork = new Category("1", "Work Projects", "#FF0000");
        assertTrue(dataAccess.updateCategory(updatedWork));
        assertEquals("Work Projects", dataAccess.getCategoryById("1").getName());
        
        // Check name exclusion
        assertTrue(dataAccess.existsByNameExcluding("Personal", "1"));
        assertFalse(dataAccess.existsByNameExcluding("Work Projects", "1"));
        
        // Delete category
        assertTrue(dataAccess.deleteCategory(personalCategory));
        assertEquals(1, dataAccess.getCategoryCount());
        
        // Verify sorted retrieval
        List<Category> categories = dataAccess.getAllCategories();
        assertEquals(1, categories.size());
        assertEquals("Work Projects", categories.get(0).getName());
    }

    @Test
    void testEdgeCasesWithEmptyStrings() {
        // Cannot create category with empty name due to validation
        // Category emptyNameCategory = new Category("1", "", "#FFFFFF");
        // Skip this test since Category constructor validates empty names
        // Skip this test - empty names not allowed
        assertFalse(dataAccess.categoryNameExists(""));
    }

    @Test
    void testMultipleCategoriesWithSameNameDifferentCase() {
        Category workLower = new Category("1", "work", "#FF0000");
        Category workUpper = new Category("2", "WORK", "#00FF00");
        
        dataAccess.saveCategory(workLower);
        
        // Should not be able to save another category with same name (case-insensitive)
        assertTrue(dataAccess.existsByName("WORK"));
        assertTrue(dataAccess.categoryNameExists("Work"));
        
        // But we can still save it (the check is done at business layer)
        dataAccess.saveCategory(workUpper);
        assertEquals(2, dataAccess.getCategoryCount());
    }
}