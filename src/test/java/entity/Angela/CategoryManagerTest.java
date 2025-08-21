package entity.Angela;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_utils.TestDataResetUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test for CategoryManager singleton.
 * Tests category management, singleton pattern, and listener functionality.
 */
class CategoryManagerTest {

    private CategoryManager categoryManager;
    private TestCategoryChangeListener testListener;

    private static class TestCategoryChangeListener implements CategoryManager.CategoryChangeListener {
        private final List<String> addedCategories = new ArrayList<>();
        private final List<String> removedCategories = new ArrayList<>();
        private Set<String> loadedCategories = null;

        @Override
        public void onCategoryAdded(String category) {
            addedCategories.add(category);
        }

        @Override
        public void onCategoryRemoved(String category) {
            removedCategories.add(category);
        }

        @Override
        public void onCategoriesLoaded(Set<String> categories) {
            loadedCategories = new HashSet<>(categories);
        }

        // Helper methods for testing
        List<String> getAddedCategories() { return addedCategories; }
        List<String> getRemovedCategories() { return removedCategories; }
        Set<String> getLoadedCategories() { return loadedCategories; }
        void reset() { 
            addedCategories.clear(); 
            removedCategories.clear(); 
            loadedCategories = null; 
        }
    }

    @BeforeEach
    void setUp() {
        // Reset all shared singleton data for test isolation
        TestDataResetUtil.resetAllSharedData();
        
        // Reset singleton for clean test state
        CategoryManager.resetInstance();
        categoryManager = CategoryManager.getInstance();
        testListener = new TestCategoryChangeListener();
    }

    @AfterEach
    void tearDown() {
        // Clean up after tests
        if (testListener != null && categoryManager != null) {
            categoryManager.removeCategoryChangeListener(testListener);
        }
        CategoryManager.resetInstance();
    }

    @Test
    void testSingletonPattern() {
        CategoryManager instance1 = CategoryManager.getInstance();
        CategoryManager instance2 = CategoryManager.getInstance();
        
        assertSame(instance1, instance2);
        assertSame(categoryManager, instance1);
    }

    @Test
    void testDefaultCategories() {
        Set<String> categories = categoryManager.getCategories();
        
        assertTrue(categories.contains("Work"));
        assertTrue(categories.contains("Personal"));
        assertTrue(categories.contains("Health"));
        assertTrue(categories.contains("Study"));
        assertTrue(categories.size() >= 4);
    }

    @Test
    void testAddCategory() {
        String newCategory = "Entertainment";
        boolean added = categoryManager.addCategory(newCategory);
        
        assertTrue(added);
        assertTrue(categoryManager.hasCategory(newCategory));
        assertTrue(categoryManager.getCategories().contains(newCategory));
    }

    @Test
    void testAddCategoryDuplicate() {
        String category = "Finance";
        
        assertTrue(categoryManager.addCategory(category));
        assertFalse(categoryManager.addCategory(category)); // Duplicate should return false
        
        assertEquals(1, categoryManager.getCategoriesList().stream()
                .mapToInt(c -> c.equals(category) ? 1 : 0).sum());
    }

    @Test
    void testAddCategoryIgnoresCase() {
        categoryManager.addCategory("study");
        categoryManager.addCategory("STUDY");
        categoryManager.addCategory("Study");
        
        Set<String> categories = categoryManager.getCategories();
        long studyCount = categories.stream()
                .filter(cat -> cat.equalsIgnoreCase("study"))
                .count();
        assertEquals(1, studyCount);
    }

    @Test
    void testAddCategoryWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            categoryManager.addCategory(null));
    }

    @Test
    void testAddCategoryWithEmptyStringThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            categoryManager.addCategory(""));
        assertThrows(IllegalArgumentException.class, () -> 
            categoryManager.addCategory("   "));
    }

    @Test
    void testAddCategoryTrimsWhitespace() {
        String categoryWithSpaces = "  Gaming  ";
        boolean added = categoryManager.addCategory(categoryWithSpaces);
        
        assertTrue(added);
        assertTrue(categoryManager.hasCategory("Gaming"));
        assertFalse(categoryManager.hasCategory(categoryWithSpaces));
    }

    @Test
    void testRemoveCategory() {
        String category = "Work";
        assertTrue(categoryManager.hasCategory(category));
        
        boolean removed = categoryManager.removeCategory(category);
        
        assertTrue(removed);
        assertFalse(categoryManager.hasCategory(category));
    }

    @Test
    void testRemoveNonExistentCategory() {
        String nonExistent = "NonExistent";
        assertFalse(categoryManager.hasCategory(nonExistent));
        
        boolean removed = categoryManager.removeCategory(nonExistent);
        
        assertFalse(removed);
    }

    @Test
    void testRemoveNullCategory() {
        boolean removed = categoryManager.removeCategory(null);
        
        assertFalse(removed);
    }

    @Test
    void testEditCategory() {
        String oldCategory = "Work";
        String newCategory = "Business";
        
        assertTrue(categoryManager.hasCategory(oldCategory));
        assertFalse(categoryManager.hasCategory(newCategory));
        
        boolean edited = categoryManager.editCategory(oldCategory, newCategory);
        
        assertTrue(edited);
        assertFalse(categoryManager.hasCategory(oldCategory));
        assertTrue(categoryManager.hasCategory(newCategory));
    }

    @Test
    void testEditCategoryNonExistent() {
        String nonExistent = "NonExistent";
        String newCategory = "NewCategory";
        
        boolean edited = categoryManager.editCategory(nonExistent, newCategory);
        
        assertFalse(edited);
        assertFalse(categoryManager.hasCategory(newCategory));
    }

    @Test
    void testEditCategoryToExistingName() {
        String category1 = "Work";
        String category2 = "Personal";
        
        assertTrue(categoryManager.hasCategory(category1));
        assertTrue(categoryManager.hasCategory(category2));
        
        boolean edited = categoryManager.editCategory(category1, category2);
        
        assertFalse(edited);
        assertTrue(categoryManager.hasCategory(category1));
        assertTrue(categoryManager.hasCategory(category2));
    }

    @Test
    void testEditCategorySameName() {
        String category = "Work";
        
        boolean edited = categoryManager.editCategory(category, category);
        
        assertTrue(edited); // Should allow same name
        assertTrue(categoryManager.hasCategory(category));
    }

    @Test
    void testEditCategorySameNameDifferentCase() {
        String category = "Work";
        String sameNameDifferentCase = "work";
        
        boolean edited = categoryManager.editCategory(category, sameNameDifferentCase);
        
        assertTrue(edited);
        assertFalse(categoryManager.hasCategory("Work"));
        assertTrue(categoryManager.hasCategory("work"));
    }

    @Test
    void testEditCategoryWithNullParameters() {
        assertFalse(categoryManager.editCategory(null, "NewName"));
        assertFalse(categoryManager.editCategory("OldName", null));
        assertFalse(categoryManager.editCategory("OldName", ""));
        assertFalse(categoryManager.editCategory("OldName", "   "));
    }

    @Test
    void testHasCategory() {
        assertTrue(categoryManager.hasCategory("Work"));
        assertFalse(categoryManager.hasCategory("NonExistent"));
        assertFalse(categoryManager.hasCategory(null));
    }

    @Test
    void testHasCategoryIgnoresCase() {
        assertTrue(categoryManager.hasCategory("work"));
        assertTrue(categoryManager.hasCategory("WORK"));
        assertTrue(categoryManager.hasCategory("WoRk"));
    }

    @Test
    void testGetCategoriesIsUnmodifiable() {
        Set<String> categories = categoryManager.getCategories();
        
        assertThrows(UnsupportedOperationException.class, () -> 
            categories.add("ShouldNotWork"));
        assertThrows(UnsupportedOperationException.class, () -> 
            categories.remove("Work"));
        assertThrows(UnsupportedOperationException.class, () -> 
            categories.clear());
    }

    @Test
    void testGetCategoriesListIsModifiable() {
        List<String> categoriesList = categoryManager.getCategoriesList();
        int originalSize = categoriesList.size();
        
        // Modifying the returned list should not affect the original
        categoriesList.add("TestCategory");
        categoriesList.remove("Work");
        
        assertEquals(originalSize, categoryManager.getCategories().size());
        assertTrue(categoryManager.hasCategory("Work"));
        assertFalse(categoryManager.hasCategory("TestCategory"));
    }

    @Test
    void testCategoriesAreSorted() {
        categoryManager.addCategory("Zebra");
        categoryManager.addCategory("Apple");
        categoryManager.addCategory("Banana");
        
        List<String> categoriesList = categoryManager.getCategoriesList();
        
        // Should be sorted alphabetically
        List<String> sortedList = new ArrayList<>(categoriesList);
        sortedList.sort(String.CASE_INSENSITIVE_ORDER);
        
        assertEquals(sortedList, categoriesList);
    }

    @Test
    void testLoadCategories() {
        Collection<String> newCategories = Arrays.asList("Music", "Art", "Travel");
        
        categoryManager.loadCategories(newCategories);
        
        assertTrue(categoryManager.hasCategory("Music"));
        assertTrue(categoryManager.hasCategory("Art"));
        assertTrue(categoryManager.hasCategory("Travel"));
        
        // Default categories should still be present
        assertTrue(categoryManager.hasCategory("Work"));
        assertTrue(categoryManager.hasCategory("Personal"));
        assertTrue(categoryManager.hasCategory("Health"));
        assertTrue(categoryManager.hasCategory("Study"));
    }

    @Test
    void testLoadCategoriesWithNull() {
        int originalSize = categoryManager.getCategories().size();
        
        categoryManager.loadCategories(null);
        
        // Should have at least the default categories
        assertTrue(categoryManager.hasCategory("Work"));
        assertTrue(categoryManager.hasCategory("Personal"));
        assertTrue(categoryManager.hasCategory("Health"));
        assertTrue(categoryManager.hasCategory("Study"));
    }

    @Test
    void testLoadCategoriesReplacesExisting() {
        categoryManager.addCategory("Custom1");
        categoryManager.addCategory("Custom2");
        
        assertTrue(categoryManager.hasCategory("Custom1"));
        assertTrue(categoryManager.hasCategory("Custom2"));
        
        Collection<String> newCategories = Arrays.asList("Replacement1", "Replacement2");
        categoryManager.loadCategories(newCategories);
        
        assertTrue(categoryManager.hasCategory("Replacement1"));
        assertTrue(categoryManager.hasCategory("Replacement2"));
        
        // Default categories should still be present
        assertTrue(categoryManager.hasCategory("Work"));
        assertTrue(categoryManager.hasCategory("Personal"));
        assertTrue(categoryManager.hasCategory("Health"));
        assertTrue(categoryManager.hasCategory("Study"));
    }

    @Test
    void testAddCategoryChangeListener() {
        categoryManager.addCategoryChangeListener(testListener);
        
        categoryManager.addCategory("TestCategory");
        
        assertTrue(testListener.getAddedCategories().contains("TestCategory"));
    }

    @Test
    void testRemoveCategoryChangeListener() {
        categoryManager.addCategoryChangeListener(testListener);
        categoryManager.addCategory("TestCategory1");
        categoryManager.removeCategoryChangeListener(testListener);
        categoryManager.addCategory("TestCategory2");
        
        assertTrue(testListener.getAddedCategories().contains("TestCategory1"));
        assertFalse(testListener.getAddedCategories().contains("TestCategory2"));
    }

    @Test
    void testCategoryChangeListenerNotifications() {
        categoryManager.addCategoryChangeListener(testListener);
        
        // Test add notification
        categoryManager.addCategory("NewCategory");
        assertEquals(1, testListener.getAddedCategories().size());
        assertEquals("NewCategory", testListener.getAddedCategories().get(0));
        
        // Test remove notification
        categoryManager.removeCategory("NewCategory");
        assertEquals(1, testListener.getRemovedCategories().size());
        assertEquals("NewCategory", testListener.getRemovedCategories().get(0));
    }

    @Test
    void testEditCategoryChangeListenerNotifications() {
        categoryManager.addCategoryChangeListener(testListener);
        
        categoryManager.editCategory("Work", "Business");
        
        assertTrue(testListener.getRemovedCategories().contains("Work"));
        assertTrue(testListener.getAddedCategories().contains("Business"));
    }

    @Test
    void testLoadCategoriesListenerNotification() {
        categoryManager.addCategoryChangeListener(testListener);
        
        Collection<String> newCategories = Arrays.asList("Music", "Art");
        categoryManager.loadCategories(newCategories);
        
        assertNotNull(testListener.getLoadedCategories());
        assertTrue(testListener.getLoadedCategories().contains("Music"));
        assertTrue(testListener.getLoadedCategories().contains("Art"));
    }

    @Test
    void testAddNullListener() {
        // Should not throw exception
        categoryManager.addCategoryChangeListener(null);
        
        // Should still work normally
        categoryManager.addCategory("TestCategory");
    }

    @Test
    void testAddDuplicateListener() {
        categoryManager.addCategoryChangeListener(testListener);
        categoryManager.addCategoryChangeListener(testListener); // Add same listener twice
        
        categoryManager.addCategory("TestCategory");
        
        // Should only be notified once
        assertEquals(1, testListener.getAddedCategories().size());
    }

    @Test
    void testRemoveNonExistentListener() {
        TestCategoryChangeListener otherListener = new TestCategoryChangeListener();
        
        // Should not throw exception
        categoryManager.removeCategoryChangeListener(otherListener);
        categoryManager.removeCategoryChangeListener(null);
    }

    @Test
    void testResetInstance() {
        CategoryManager instance1 = CategoryManager.getInstance();
        instance1.addCategory("TestCategory");
        
        CategoryManager.resetInstance();
        CategoryManager instance2 = CategoryManager.getInstance();
        
        assertNotSame(instance1, instance2);
        assertFalse(instance2.hasCategory("TestCategory")); // Should not have custom category
        assertTrue(instance2.hasCategory("Work")); // Should have defaults
    }

    @Test
    void testCompleteWorkflow() {
        // Add listener
        categoryManager.addCategoryChangeListener(testListener);
        
        // Add categories
        assertTrue(categoryManager.addCategory("Entertainment"));
        assertTrue(categoryManager.addCategory("Finance"));
        
        // Edit category
        assertTrue(categoryManager.editCategory("Work", "Professional"));
        
        // Remove category
        assertTrue(categoryManager.removeCategory("Personal"));
        
        // Load new categories
        categoryManager.loadCategories(Arrays.asList("Music", "Sports"));
        
        // Verify final state
        // Note: loadCategories replaces existing custom categories
        assertFalse(categoryManager.hasCategory("Entertainment")); // Replaced by loadCategories
        assertFalse(categoryManager.hasCategory("Finance")); // Replaced by loadCategories
        assertFalse(categoryManager.hasCategory("Professional")); // Replaced by loadCategories
        assertFalse(categoryManager.hasCategory("Work")); // Was edited to Professional, then replaced
        assertTrue(categoryManager.hasCategory("Music")); // From loadCategories
        assertTrue(categoryManager.hasCategory("Sports")); // From loadCategories
        
        // Default categories should still be present
        assertTrue(categoryManager.hasCategory("Personal"));
        assertTrue(categoryManager.hasCategory("Health"));
        assertTrue(categoryManager.hasCategory("Study"));
        
        // Verify listener notifications
        assertTrue(testListener.getAddedCategories().contains("Entertainment"));
        assertTrue(testListener.getAddedCategories().contains("Finance"));
        assertTrue(testListener.getAddedCategories().contains("Professional"));
        assertTrue(testListener.getRemovedCategories().contains("Work"));
        assertTrue(testListener.getRemovedCategories().contains("Personal"));
        assertNotNull(testListener.getLoadedCategories());
    }
}