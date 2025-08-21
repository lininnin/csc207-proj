package entity.Angela;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CategoryManager singleton.
 * Tests category management operations and listener notifications.
 */
class CategoryManagerTest {

    private CategoryManager categoryManager;
    private TestCategoryChangeListener testListener;

    @BeforeEach
    void setUp() {
        // Get fresh instance and reset for each test
        categoryManager = CategoryManager.getInstance();
        testListener = new TestCategoryChangeListener();
        categoryManager.addListener(testListener);
        
        // Clear existing categories except defaults to have consistent state
        categoryManager.clearAllCategories();
    }

    @AfterEach
    void tearDown() {
        // Clean up listeners after each test
        categoryManager.removeListener(testListener);
        categoryManager.clearAllCategories();
    }

    @Test
    void testSingletonInstance() {
        CategoryManager instance1 = CategoryManager.getInstance();
        CategoryManager instance2 = CategoryManager.getInstance();
        
        assertSame(instance1, instance2);
        assertEquals(categoryManager, instance1);
    }

    @Test
    void testInitializeDefaultCategories() {
        Set<String> categories = categoryManager.getCategories();
        
        assertFalse(categories.isEmpty());
        assertTrue(categories.contains("Work"));
        assertTrue(categories.contains("Personal"));
        assertTrue(categories.contains("Health"));
    }

    @Test
    void testAddCategory() {
        String newCategory = "Study";
        
        categoryManager.addCategory(newCategory);
        
        Set<String> categories = categoryManager.getCategories();
        assertTrue(categories.contains(newCategory));
        assertEquals(1, testListener.addedCount);
        assertEquals(newCategory, testListener.lastAddedCategory);
    }

    @Test
    void testAddCategoryIgnoresCase() {
        categoryManager.addCategory("study");
        categoryManager.addCategory("STUDY");
        categoryManager.addCategory("Study");
        
        Set<String> categories = categoryManager.getCategories();
        // Should only contain one version due to case insensitive set
        long studyCount = categories.stream()
                .filter(cat -> cat.equalsIgnoreCase("study"))
                .count();
        assertEquals(1, studyCount);
    }

    @Test
    void testAddDuplicateCategoryIgnored() {
        String category = "Duplicate";
        
        categoryManager.addCategory(category);
        categoryManager.addCategory(category);
        
        Set<String> categories = categoryManager.getCategories();
        assertEquals(1, categories.stream()
                .filter(cat -> cat.equals(category))
                .count());
        assertEquals(1, testListener.addedCount);
    }

    @Test
    void testAddNullCategoryIgnored() {
        int initialSize = categoryManager.getCategories().size();
        
        categoryManager.addCategory(null);
        
        assertEquals(initialSize, categoryManager.getCategories().size());
        assertEquals(0, testListener.addedCount);
    }

    @Test
    void testAddEmptyCategoryIgnored() {
        int initialSize = categoryManager.getCategories().size();
        
        categoryManager.addCategory("");
        categoryManager.addCategory("   ");
        
        assertEquals(initialSize, categoryManager.getCategories().size());
        assertEquals(0, testListener.addedCount);
    }

    @Test
    void testRemoveCategory() {
        String category = "ToRemove";
        categoryManager.addCategory(category);
        testListener.reset();
        
        categoryManager.removeCategory(category);
        
        Set<String> categories = categoryManager.getCategories();
        assertFalse(categories.contains(category));
        assertEquals(1, testListener.removedCount);
        assertEquals(category, testListener.lastRemovedCategory);
    }

    @Test
    void testRemoveNonExistentCategory() {
        int initialSize = categoryManager.getCategories().size();
        
        categoryManager.removeCategory("NonExistent");
        
        assertEquals(initialSize, categoryManager.getCategories().size());
        assertEquals(0, testListener.removedCount);
    }

    @Test
    void testRemoveNullCategory() {
        int initialSize = categoryManager.getCategories().size();
        
        categoryManager.removeCategory(null);
        
        assertEquals(initialSize, categoryManager.getCategories().size());
        assertEquals(0, testListener.removedCount);
    }

    @Test
    void testClearAllCategories() {
        categoryManager.addCategory("Test1");
        categoryManager.addCategory("Test2");
        assertFalse(categoryManager.getCategories().isEmpty());
        
        categoryManager.clearAllCategories();
        
        assertTrue(categoryManager.getCategories().isEmpty());
    }

    @Test
    void testCategoriesSortedAlphabetically() {
        categoryManager.addCategory("Zebra");
        categoryManager.addCategory("Apple");
        categoryManager.addCategory("Banana");
        
        Set<String> categories = categoryManager.getCategories();
        String[] categoryArray = categories.toArray(new String[0]);
        
        // TreeSet with case insensitive comparator should maintain alphabetical order
        assertTrue(categoryArray[0].compareToIgnoreCase(categoryArray[1]) <= 0);
        assertTrue(categoryArray[1].compareToIgnoreCase(categoryArray[2]) <= 0);
    }

    @Test
    void testMultipleListeners() {
        TestCategoryChangeListener listener2 = new TestCategoryChangeListener();
        categoryManager.addListener(listener2);
        
        String category = "TestCategory";
        categoryManager.addCategory(category);
        
        assertEquals(1, testListener.addedCount);
        assertEquals(1, listener2.addedCount);
        assertEquals(category, testListener.lastAddedCategory);
        assertEquals(category, listener2.lastAddedCategory);
        
        categoryManager.removeListener(listener2);
    }

    @Test
    void testRemoveListener() {
        categoryManager.removeListener(testListener);
        
        categoryManager.addCategory("TestCategory");
        
        assertEquals(0, testListener.addedCount);
    }

    @Test
    void testGetCategoriesReturnsUnmodifiableCopy() {
        Set<String> categories = categoryManager.getCategories();
        
        assertThrows(UnsupportedOperationException.class, () -> 
                categories.add("ShouldFail"));
    }

    @Test
    void testLoadCategories() {
        Set<String> categoriesToLoad = Set.of("LoadTest1", "LoadTest2", "LoadTest3");
        testListener.reset();
        
        categoryManager.loadCategories(categoriesToLoad);
        
        Set<String> loadedCategories = categoryManager.getCategories();
        assertTrue(loadedCategories.containsAll(categoriesToLoad));
        assertEquals(1, testListener.loadedCount);
        assertEquals(categoriesToLoad.size(), testListener.lastLoadedCategories.size());
    }

    @Test
    void testLoadCategoriesReplacesExisting() {
        categoryManager.addCategory("ExistingCategory");
        Set<String> newCategories = Set.of("NewCategory1", "NewCategory2");
        
        categoryManager.loadCategories(newCategories);
        
        Set<String> categories = categoryManager.getCategories();
        assertEquals(newCategories.size(), categories.size());
        assertTrue(categories.containsAll(newCategories));
        assertFalse(categories.contains("ExistingCategory"));
    }

    @Test
    void testLoadNullCategories() {
        categoryManager.addCategory("ExistingCategory");
        int initialSize = categoryManager.getCategories().size();
        
        categoryManager.loadCategories(null);
        
        assertEquals(initialSize, categoryManager.getCategories().size());
        assertEquals(0, testListener.loadedCount);
    }

    /**
     * Test implementation of CategoryChangeListener for testing purposes.
     */
    private static class TestCategoryChangeListener implements CategoryManager.CategoryChangeListener {
        int addedCount = 0;
        int removedCount = 0;
        int loadedCount = 0;
        String lastAddedCategory = null;
        String lastRemovedCategory = null;
        Set<String> lastLoadedCategories = null;

        @Override
        public void onCategoryAdded(String category) {
            addedCount++;
            lastAddedCategory = category;
        }

        @Override
        public void onCategoryRemoved(String category) {
            removedCount++;
            lastRemovedCategory = category;
        }

        @Override
        public void onCategoriesLoaded(Set<String> categories) {
            loadedCount++;
            lastLoadedCategories = categories;
        }

        void reset() {
            addedCount = 0;
            removedCount = 0;
            loadedCount = 0;
            lastAddedCategory = null;
            lastRemovedCategory = null;
            lastLoadedCategories = null;
        }
    }
}