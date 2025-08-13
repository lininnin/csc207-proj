package use_case.Angela.category.edit;

import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import entity.Category;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditCategoryInteractor with segregated interfaces following SOLID principles.
 * Tests the refactored implementation with separate interfaces for category and task operations.
 */
class EditCategoryWithSegregatedInterfacesTest {

    private TestCategoryDataAccess categoryDataAccess;
    private TestTaskDataAccess taskDataAccess;
    private TestEditCategoryPresenter testPresenter;
    private EditCategoryInteractor interactor;

    @BeforeEach
    void setUp() {
        categoryDataAccess = new TestCategoryDataAccess();
        taskDataAccess = new TestTaskDataAccess();
        testPresenter = new TestEditCategoryPresenter();
        
        interactor = new EditCategoryInteractor(
            categoryDataAccess,
            taskDataAccess,
            testPresenter
        );
    }

    @Test
    void testSuccessfulEditWithNoTasks() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Execute edit
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, "Business");
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals(categoryId, testPresenter.lastOutputData.getCategoryId());
        assertEquals("Work", testPresenter.lastOutputData.getOldName());
        assertEquals("Business", testPresenter.lastOutputData.getNewName());

        // Verify category was updated
        Category updatedCategory = categoryDataAccess.categories.get(categoryId);
        assertEquals("Business", updatedCategory.getName());
    }

    @Test
    void testSuccessfulEditWithTasks() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Add tasks with this category
        Info taskInfo1 = new Info.Builder("Task 1").category(categoryId).build();
        TaskAvailable task1 = new TaskAvailable(taskInfo1);
        taskDataAccess.addAvailableTask(task1);

        Info taskInfo2 = new Info.Builder("Task 2").category(categoryId).build();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        Task todayTask = new Task("task-template-1", taskInfo2, dates, false);
        taskDataAccess.addTodayTask(todayTask);

        // Execute edit
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, "Business");
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals("Business", testPresenter.lastOutputData.getNewName());

        // Verify tasks were found (even though no update is needed since tasks reference category by ID)
        assertTrue(taskDataAccess.findAvailableTasksCalled);
        assertTrue(taskDataAccess.findTodaysTasksCalled);
    }

    @Test
    void testFailureEmptyCategoryName() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Try to edit with empty name
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, "");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);
    }

    @Test
    void testFailureNullCategoryName() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Try to edit with null name
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, null);
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);
    }

    @Test
    void testFailureNameTooLong() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Try to edit with name > 20 characters
        EditCategoryInputData inputData = new EditCategoryInputData(
            categoryId, 
            "ThisNameIsWayTooLongForACategory"
        );
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The name of category cannot exceed 20 letters", testPresenter.lastError);
    }

    @Test
    void testFailureCategoryNotFound() {
        // Try to edit non-existent category
        EditCategoryInputData inputData = new EditCategoryInputData("non-existent", "NewName");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category not found", testPresenter.lastError);
    }

    @Test
    void testFailureNullCategoryId() {
        // Try to edit with null ID
        EditCategoryInputData inputData = new EditCategoryInputData(null, "NewName");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category ID is required", testPresenter.lastError);
    }

    @Test
    void testFailureEmptyCategoryId() {
        // Try to edit with empty ID
        EditCategoryInputData inputData = new EditCategoryInputData("  ", "NewName");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category ID is required", testPresenter.lastError);
    }

    @Test
    void testFailureDuplicateName() {
        // Setup: Create multiple categories
        categoryDataAccess.addCategory(new Category("cat-1", "Work", "#0000FF"));
        categoryDataAccess.addCategory(new Category("cat-2", "Personal", "#00FF00"));

        // Try to rename Work to Personal (duplicate)
        EditCategoryInputData inputData = new EditCategoryInputData("cat-1", "Personal");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The category name already exists", testPresenter.lastError);
    }

    @Test
    void testSuccessRenamingToSameName() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Rename to same name (should succeed)
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, "Work");
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals("Work", testPresenter.lastOutputData.getOldName());
        assertEquals("Work", testPresenter.lastOutputData.getNewName());
    }

    @Test
    void testSuccessCaseInsensitiveDuplicateCheck() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Rename with different case (should succeed as it's the same category)
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, "WORK");
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals("Work", testPresenter.lastOutputData.getOldName());
        assertEquals("WORK", testPresenter.lastOutputData.getNewName());
    }

    @Test
    void testPreservesSpacesInName() {
        // Setup: Create category
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);

        // Rename with spaces (should preserve spaces)
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, "  New Work  ");
        interactor.execute(inputData);

        // Verify success and spaces preserved
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals("  New Work  ", testPresenter.lastOutputData.getNewName());
        
        // Verify category was updated with spaces preserved
        Category updatedCategory = categoryDataAccess.categories.get(categoryId);
        assertEquals("  New Work  ", updatedCategory.getName());
    }

    @Test
    void testInterfaceSegregation() {
        // This test verifies that each interface is called independently
        String categoryId = "cat-1";
        categoryDataAccess.addCategory(new Category(categoryId, "Work", "#0000FF"));

        // Add a task to verify task interface is called
        Info taskInfo = new Info.Builder("Task 1").category(categoryId).build();
        taskDataAccess.addAvailableTask(new TaskAvailable(taskInfo));

        // Track method calls
        categoryDataAccess.resetCallTracking();
        taskDataAccess.resetCallTracking();

        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, "Business");
        interactor.execute(inputData);

        // Verify each interface was called appropriately
        assertTrue(categoryDataAccess.getCategoryByIdCalled, "Should check if category exists");
        assertTrue(categoryDataAccess.existsByNameExcludingCalled, "Should check for duplicate names");
        assertTrue(categoryDataAccess.updateCategoryCalled, "Should update category");
        
        assertTrue(taskDataAccess.findAvailableTasksCalled, "Should find available tasks");
        assertTrue(taskDataAccess.findTodaysTasksCalled, "Should find today's tasks");
    }

    /**
     * Test implementation of EditCategoryDataAccessInterface
     */
    private static class TestCategoryDataAccess implements EditCategoryDataAccessInterface {
        Map<String, Category> categories = new HashMap<>();
        boolean getCategoryByIdCalled = false;
        boolean existsByNameExcludingCalled = false;
        boolean updateCategoryCalled = false;

        void addCategory(Category category) {
            categories.put(category.getId(), category);
        }

        void resetCallTracking() {
            getCategoryByIdCalled = false;
            existsByNameExcludingCalled = false;
            updateCategoryCalled = false;
        }

        @Override
        public Category getCategoryById(String categoryId) {
            getCategoryByIdCalled = true;
            return categories.get(categoryId);
        }

        @Override
        public boolean existsByNameExcluding(String name, String excludeCategoryId) {
            existsByNameExcludingCalled = true;
            return categories.values().stream()
                .anyMatch(c -> !c.getId().equals(excludeCategoryId) && 
                              c.getName().equalsIgnoreCase(name));
        }

        @Override
        public boolean updateCategory(Category category) {
            updateCategoryCalled = true;
            if (categories.containsKey(category.getId())) {
                categories.put(category.getId(), category);
                return true;
            }
            return false;
        }
    }

    /**
     * Test implementation of EditCategoryTaskDataAccessInterface
     */
    private static class TestTaskDataAccess implements EditCategoryTaskDataAccessInterface {
        List<TaskAvailable> availableTasks = new ArrayList<>();
        List<Task> todayTasks = new ArrayList<>();
        boolean findAvailableTasksCalled = false;
        boolean findTodaysTasksCalled = false;

        void addAvailableTask(TaskAvailable task) {
            availableTasks.add(task);
        }

        void addTodayTask(Task task) {
            todayTasks.add(task);
        }

        void resetCallTracking() {
            findAvailableTasksCalled = false;
            findTodaysTasksCalled = false;
        }

        @Override
        public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
            findAvailableTasksCalled = true;
            return availableTasks.stream()
                .filter(t -> categoryId.equals(t.getInfo().getCategory()))
                .toList();
        }

        @Override
        public List<Task> findTodaysTasksByCategory(String categoryId) {
            findTodaysTasksCalled = true;
            return todayTasks.stream()
                .filter(t -> categoryId.equals(t.getInfo().getCategory()))
                .toList();
        }

        @Override
        public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
            // Not needed for edit since tasks reference category by ID
            return true;
        }

        @Override
        public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
            // Not needed for edit since tasks reference category by ID
            return true;
        }
    }

    /**
     * Test presenter implementation
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