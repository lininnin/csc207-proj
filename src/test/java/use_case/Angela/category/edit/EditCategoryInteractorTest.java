package use_case.Angela.category.edit;

import data_access.InMemoryCategoryDataAccessObject;
import data_access.InMemoryTaskDataAccessObject;
import entity.Category;
import entity.CategoryFactory;
import entity.CommonCategoryFactory;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditCategoryInteractor following Clean Architecture principles.
 */
class EditCategoryInteractorTest {

    private TestEditCategoryDataAccess categoryDataAccess;
    private TestEditCategoryTaskDataAccess taskDataAccess;
    private TestEditCategoryPresenter testPresenter;
    private EditCategoryInteractor interactor;

    @BeforeEach
    void setUp() {
        categoryDataAccess = new TestEditCategoryDataAccess();
        taskDataAccess = new TestEditCategoryTaskDataAccess();
        testPresenter = new TestEditCategoryPresenter();
        interactor = new EditCategoryInteractor(categoryDataAccess, taskDataAccess, testPresenter, new CommonCategoryFactory());
    }

    @Test
    void testSuccessfulEdit() {
        // Create a category to edit
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Edit the category
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "Updated Work"
        );

        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(categoryId, testPresenter.lastOutputData.getCategoryId());
        assertEquals("Updated Work", testPresenter.lastOutputData.getNewName());
        assertNull(testPresenter.lastError);

        // Verify category was updated
        Category updatedCategory = categoryDataAccess.getCategoryById(categoryId);
        assertEquals("Updated Work", updatedCategory.getName());
    }

    @Test
    void testEditNameOnly() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Original", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Edit only the name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "New Name"
        );

        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("New Name", testPresenter.lastOutputData.getNewName());
    }

    @Test
    void testFailureCategoryNotFound() {
        // Try to edit non-existent category
        EditCategoryInputData inputData = new EditCategoryInputData(
                "non-existent-id",
                "Updated Name"
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category not found", testPresenter.lastError);
    }

    @Test
    void testFailureEmptyCategoryName() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Try to edit with empty name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                ""
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);

        // Verify category was not changed
        Category unchangedCategory = categoryDataAccess.getCategoryById(categoryId);
        assertEquals("Work", unchangedCategory.getName());
    }

    @Test
    void testFailureNullCategoryName() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Try to edit with null name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                null
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);
    }

    @Test
    void testFailureDuplicateName() {
        // Create two categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        
        categoryDataAccess.addCategory(new Category(categoryId1, "Work", "#0000FF"));
        categoryDataAccess.addCategory(new Category(categoryId2, "Personal", "#00FF00"));

        // Try to rename Personal to Work (duplicate)
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId2,
                "Work"  // Duplicate name
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The category name already exists", testPresenter.lastError);

        // Verify category was not changed
        Category unchangedCategory = categoryDataAccess.getCategoryById(categoryId2);
        assertEquals("Personal", unchangedCategory.getName());
    }

    @Test
    void testFailureNullCategoryId() {
        // Try to edit with null ID
        EditCategoryInputData inputData = new EditCategoryInputData(
                null,
                "Updated Name"
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category ID is required", testPresenter.lastError);
    }

    @Test
    void testFailureEmptyCategoryId() {
        // Try to edit with empty ID
        EditCategoryInputData inputData = new EditCategoryInputData(
                "",
                "Updated Name"
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category ID is required", testPresenter.lastError);
    }

    @Test
    void testEditToSameName() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Edit to same name (should succeed)
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "Work"  // Same name
        );

        interactor.execute(inputData);

        // Verify success (editing to same name is allowed)
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Work", testPresenter.lastOutputData.getNewName());
    }

    @Test
    void testCaseInsensitiveDuplicateCheck() {
        // Create two categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        
        categoryDataAccess.addCategory(new Category(categoryId1, "Work", "#0000FF"));
        categoryDataAccess.addCategory(new Category(categoryId2, "Personal", "#00FF00"));

        // Try to rename Personal to WORK (different case)
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId2,
                "WORK"  // Different case of existing name
        );

        interactor.execute(inputData);

        // Verify failure (case-insensitive duplicate check)
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The category name already exists", testPresenter.lastError);
    }

    @Test
    void testNameTrimming() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Original", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Edit with whitespace around name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "  Updated Name  "
        );

        interactor.execute(inputData);

        // Verify success - name is NOT trimmed
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("  Updated Name  ", testPresenter.lastOutputData.getNewName());

        Category updatedCategory = categoryDataAccess.getCategoryById(categoryId);
        assertEquals("  Updated Name  ", updatedCategory.getName());
    }

    @Test
    void testEditMultipleCategories() {
        // Create multiple categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        String categoryId3 = UUID.randomUUID().toString();
        
        categoryDataAccess.addCategory(new Category(categoryId1, "Work", "#0000FF"));
        categoryDataAccess.addCategory(new Category(categoryId2, "Personal", "#00FF00"));
        categoryDataAccess.addCategory(new Category(categoryId3, "Urgent", "#FF0000"));

        // Edit first category
        EditCategoryInputData inputData1 = new EditCategoryInputData(
                categoryId1,
                "Business"
        );
        interactor.execute(inputData1);

        // Edit second category
        testPresenter = new TestEditCategoryPresenter();
        interactor = new EditCategoryInteractor(categoryDataAccess, taskDataAccess, testPresenter, new CommonCategoryFactory());
        
        EditCategoryInputData inputData2 = new EditCategoryInputData(
                categoryId2,
                "Home"
        );
        interactor.execute(inputData2);

        // Verify both edits succeeded
        Category cat1 = categoryDataAccess.getCategoryById(categoryId1);
        Category cat2 = categoryDataAccess.getCategoryById(categoryId2);
        Category cat3 = categoryDataAccess.getCategoryById(categoryId3);

        assertEquals("Business", cat1.getName());
        assertEquals("Home", cat2.getName());
        assertEquals("Urgent", cat3.getName());  // Unchanged
    }

    /**
     * Test data access implementation for categories.
     */
    private static class TestEditCategoryDataAccess implements EditCategoryDataAccessInterface {
        private Map<String, Category> categories = new HashMap<>();

        public void addCategory(Category category) {
            categories.put(category.getId(), category);
        }

        @Override
        public Category getCategoryById(String categoryId) {
            return categories.get(categoryId);
        }

        @Override
        public boolean exists(Category category) {
            return categories.containsKey(category.getId());
        }

        @Override
        public boolean existsByNameExcluding(String name, String excludeCategoryId) {
            return categories.values().stream()
                    .filter(cat -> !cat.getId().equals(excludeCategoryId))
                    .anyMatch(cat -> cat.getName().equalsIgnoreCase(name));
        }

        @Override
        public boolean updateCategory(Category updatedCategory) {
            if (categories.containsKey(updatedCategory.getId())) {
                categories.put(updatedCategory.getId(), updatedCategory);
                return true;
            }
            return false;
        }

        @Override
        public List<Category> getAllCategories() {
            return new ArrayList<>(categories.values());
        }
    }

    /**
     * Test data access implementation for tasks.
     */
    private static class TestEditCategoryTaskDataAccess implements EditCategoryTaskDataAccessInterface {
        @Override
        public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
            return new ArrayList<>();
        }

        @Override
        public List<Task> findTodaysTasksByCategory(String categoryId) {
            return new ArrayList<>();
        }

        @Override
        public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
            return true;
        }

        @Override
        public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
            return true;
        }
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestEditCategoryPresenter implements EditCategoryOutputBoundary {
        EditCategoryOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(EditCategoryOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }
    }
}