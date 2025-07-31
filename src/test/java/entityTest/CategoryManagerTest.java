//package entityTest;
//
//import entity.Angela.CategoryManager;
//import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicReference;
//
///**
// * Unit tests for CategoryManager entity.
// */
//class CategoryManagerTest {
//
//    @BeforeEach
//    void setUp() {
//        // Reset singleton before each test
//        CategoryManager.resetInstance();
//    }
//
//    @AfterEach
//    void tearDown() {
//        CategoryManager.resetInstance();
//    }
//
//    @Test
//    @DisplayName("Should initialize with default categories")
//    void testDefaultCategories() {
//        CategoryManager manager = CategoryManager.getInstance();
//        Set<String> categories = manager.getCategories();
//
//        assertEquals(4, categories.size());
//        assertTrue(categories.contains("Work"));
//        assertTrue(categories.contains("Personal"));
//        assertTrue(categories.contains("Health"));
//        assertTrue(categories.contains("Study"));
//    }
//
//    @Test
//    @DisplayName("Should maintain singleton instance")
//    void testSingletonInstance() {
//        CategoryManager instance1 = CategoryManager.getInstance();
//        CategoryManager instance2 = CategoryManager.getInstance();
//
//        assertSame(instance1, instance2);
//    }
//
//    @Test
//    @DisplayName("Should add new category successfully")
//    void testAddCategory() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertTrue(manager.addCategory("Fitness"));
//        assertTrue(manager.hasCategory("Fitness"));
//        assertEquals(5, manager.getCategories().size());
//    }
//
//    @Test
//    @DisplayName("Should not add duplicate category")
//    void testAddDuplicateCategory() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertTrue(manager.addCategory("Shopping"));
//        assertFalse(manager.addCategory("Shopping"));
//        assertFalse(manager.addCategory("SHOPPING")); // Case insensitive
//        assertEquals(5, manager.getCategories().size());
//    }
//
//    @Test
//    @DisplayName("Should reject null or empty categories")
//    void testAddInvalidCategory() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertThrows(IllegalArgumentException.class, () -> manager.addCategory(null));
//        assertThrows(IllegalArgumentException.class, () -> manager.addCategory(""));
//        assertThrows(IllegalArgumentException.class, () -> manager.addCategory("   "));
//    }
//
//    @Test
//    @DisplayName("Should remove existing category")
//    void testRemoveCategory() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertTrue(manager.removeCategory("Work"));
//        assertFalse(manager.hasCategory("Work"));
//        assertEquals(3, manager.getCategories().size());
//    }
//
//    @Test
//    @DisplayName("Should handle removing non-existent category")
//    void testRemoveNonExistentCategory() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertFalse(manager.removeCategory("NonExistent"));
//        assertFalse(manager.removeCategory(null));
//        assertEquals(4, manager.getCategories().size());
//    }
//
//    @Test
//    @DisplayName("Should edit category name successfully")
//    void testEditCategory() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertTrue(manager.editCategory("Work", "Office"));
//        assertTrue(manager.hasCategory("Office"));
//        assertFalse(manager.hasCategory("Work"));
//        assertEquals(4, manager.getCategories().size());
//    }
//
//    @Test
//    @DisplayName("Should not edit to existing category name")
//    void testEditToExistingCategory() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertFalse(manager.editCategory("Work", "Personal"));
//        assertTrue(manager.hasCategory("Work"));
//        assertTrue(manager.hasCategory("Personal"));
//    }
//
//    @Test
//    @DisplayName("Should handle invalid edit operations")
//    void testInvalidEdit() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        assertFalse(manager.editCategory("NonExistent", "NewName"));
//        assertFalse(manager.editCategory(null, "NewName"));
//        assertFalse(manager.editCategory("Work", null));
//        assertFalse(manager.editCategory("Work", ""));
//    }
//
//    @Test
//    @DisplayName("Should maintain alphabetical order")
//    void testAlphabeticalOrder() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        manager.addCategory("Zebra");
//        manager.addCategory("Apple");
//        manager.addCategory("Banana");
//
//        String[] orderedCategories = manager.getCategoriesList().toArray(new String[0]);
//
//        // Verify alphabetical order
//        for (int i = 1; i < orderedCategories.length; i++) {
//            assertTrue(orderedCategories[i-1].compareToIgnoreCase(orderedCategories[i]) < 0);
//        }
//    }
//
//    @Test
//    @DisplayName("Should notify listeners on category addition")
//    void testAddCategoryNotification() {
//        CategoryManager manager = CategoryManager.getInstance();
//        AtomicBoolean notified = new AtomicBoolean(false);
//        AtomicReference<String> addedCategory = new AtomicReference<>();
//
//        manager.addCategoryChangeListener(new CategoryManager.CategoryChangeListener() {
//            @Override
//            public void onCategoryAdded(String category) {
//                notified.set(true);
//                addedCategory.set(category);
//            }
//
//            @Override
//            public void onCategoryRemoved(String category) {}
//
//            @Override
//            public void onCategoriesLoaded(Set<String> categories) {}
//        });
//
//        manager.addCategory("TestCategory");
//
//        assertTrue(notified.get());
//        assertEquals("TestCategory", addedCategory.get());
//    }
//
//    @Test
//    @DisplayName("Should notify listeners on category removal")
//    void testRemoveCategoryNotification() {
//        CategoryManager manager = CategoryManager.getInstance();
//        AtomicBoolean notified = new AtomicBoolean(false);
//        AtomicReference<String> removedCategory = new AtomicReference<>();
//
//        manager.addCategoryChangeListener(new CategoryManager.CategoryChangeListener() {
//            @Override
//            public void onCategoryAdded(String category) {}
//
//            @Override
//            public void onCategoryRemoved(String category) {
//                notified.set(true);
//                removedCategory.set(category);
//            }
//
//            @Override
//            public void onCategoriesLoaded(Set<String> categories) {}
//        });
//
//        manager.removeCategory("Work");
//
//        assertTrue(notified.get());
//        assertEquals("Work", removedCategory.get());
//    }
//
//    @Test
//    @DisplayName("Should load categories and notify listeners")
//    void testLoadCategories() {
//        CategoryManager manager = CategoryManager.getInstance();
//        AtomicBoolean notified = new AtomicBoolean(false);
//
//        manager.addCategoryChangeListener(new CategoryManager.CategoryChangeListener() {
//            @Override
//            public void onCategoryAdded(String category) {}
//
//            @Override
//            public void onCategoryRemoved(String category) {}
//
//            @Override
//            public void onCategoriesLoaded(Set<String> categories) {
//                notified.set(true);
//            }
//        });
//
//        manager.loadCategories(Set.of("Custom1", "Custom2"));
//
//        assertTrue(notified.get());
//        // Should have custom categories plus defaults
//        assertTrue(manager.hasCategory("Custom1"));
//        assertTrue(manager.hasCategory("Custom2"));
//        assertTrue(manager.hasCategory("Work")); // Default still exists
//    }
//
//    @Test
//    @DisplayName("Should handle case-insensitive operations")
//    void testCaseInsensitive() {
//        CategoryManager manager = CategoryManager.getInstance();
//
//        manager.addCategory("UPPERCASE");
//
//        assertTrue(manager.hasCategory("uppercase"));
//        assertTrue(manager.hasCategory("UPPERCASE"));
//        assertTrue(manager.hasCategory("UpperCase"));
//    }
//}