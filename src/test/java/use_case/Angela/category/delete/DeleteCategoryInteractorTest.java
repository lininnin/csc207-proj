package use_case.Angela.category.delete;

import data_access.InMemoryCategoryGateway;
import data_access.InMemoryTaskGateway;
import entity.Angela.Task.TaskAvailable;
import entity.Category;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DeleteCategoryInteractor following Clean Architecture principles.
 */
class DeleteCategoryInteractorTest {

    private InMemoryCategoryGateway categoryGateway;
    private InMemoryTaskGateway taskGateway;
    private TestDeleteCategoryPresenter testPresenter;
    private DeleteCategoryInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskGateway();
        categoryGateway = new InMemoryCategoryGateway(taskGateway);  // Wire them together
        testPresenter = new TestDeleteCategoryPresenter();
        interactor = new DeleteCategoryInteractor(categoryGateway, testPresenter);
    }

    @Test
    void testSuccessfulDeleteWithoutTasks() {
        // Create enough categories to allow deletion (minimum 3 required)
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Personal", "#00FF00"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Urgent", "#FF0000"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Extra", "#FFFF00"));
        
        // Create a category to delete
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryGateway.save(category);

        // Delete the category
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(categoryId, testPresenter.lastOutputData.getCategoryId());
        // Category name not in output data
        assertNull(testPresenter.lastError);

        // Verify category was deleted
        assertNull(categoryGateway.getCategoryById(categoryId));
    }

    @Test
    void testSuccessfulDeleteWithTasks() {
        // Create enough categories to allow deletion (minimum 3 required)
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Personal", "#00FF00"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Urgent", "#FF0000"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Extra", "#FFFF00"));
        
        // Create a category to delete
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryGateway.save(category);

        // Create tasks with this category
        Info info1 = new Info.Builder("Task 1")
                .category(categoryId)
                .build();
        TaskAvailable task1 = new TaskAvailable(info1);
        taskGateway.saveTaskAvailable(task1);

        Info info2 = new Info.Builder("Task 2")
                .category(categoryId)
                .build();
        TaskAvailable task2 = new TaskAvailable(info2);
        taskGateway.saveTaskAvailable(task2);

        // Delete the category
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertTrue(testPresenter.lastOutputData.getMessage() != null);

        // Verify category was deleted
        assertNull(categoryGateway.getCategoryById(categoryId));

        // Verify tasks have empty category
        List<Info> tasks = taskGateway.getAllAvailableTasks();
        assertEquals(2, tasks.size());
        for (Info task : tasks) {
            assertTrue(task.getCategory() == null || task.getCategory().isEmpty());
        }
    }

    @Test
    void testDeleteWhenMinimumCategoriesReached() {
        // Create exactly 3 categories (minimum required)
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        String categoryId3 = UUID.randomUUID().toString();
        
        categoryGateway.save(new Category(categoryId1, "Work", "#0000FF"));
        categoryGateway.save(new Category(categoryId2, "Personal", "#00FF00"));
        categoryGateway.save(new Category(categoryId3, "Urgent", "#FF0000"));

        // Try to delete one category when at minimum
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId1);
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Cannot delete category: minimum 3 categories required", testPresenter.lastError);

        // Verify all categories still exist
        assertEquals(3, categoryGateway.getAllCategories().size());
    }

    @Test
    void testDeleteWhenMoreThanMinimumCategories() {
        // Create 4 categories (more than minimum)
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        String categoryId3 = UUID.randomUUID().toString();
        String categoryId4 = UUID.randomUUID().toString();
        
        categoryGateway.save(new Category(categoryId1, "Work", "#0000FF"));
        categoryGateway.save(new Category(categoryId2, "Personal", "#00FF00"));
        categoryGateway.save(new Category(categoryId3, "Urgent", "#FF0000"));
        categoryGateway.save(new Category(categoryId4, "Extra", "#FFFF00"));

        // Delete one category (should succeed)
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId4);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        // Category name not in output data

        // Verify correct number of categories remain
        assertEquals(3, categoryGateway.getAllCategories().size());
    }

    @Test
    void testFailureCategoryNotFound() {
        // Create some categories
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Work", "#0000FF"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Personal", "#00FF00"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Urgent", "#FF0000"));

        // Try to delete non-existent category
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("non-existent-id");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category not found", testPresenter.lastError);
    }

    @Test
    void testFailureNullCategoryId() {
        // Try to delete with null ID
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(null);
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category not found", testPresenter.lastError);  // Since null ID won't find a category
    }

    @Test
    void testFailureEmptyCategoryId() {
        // Try to delete with empty ID
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category not found", testPresenter.lastError);  // Since empty ID won't find a category
    }

    @Test
    void testDeleteOnlyAffectsTargetCategory() {
        // Create multiple categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        String categoryId3 = UUID.randomUUID().toString();
        String categoryId4 = UUID.randomUUID().toString();
        
        categoryGateway.save(new Category(categoryId1, "Work", "#0000FF"));
        categoryGateway.save(new Category(categoryId2, "Personal", "#00FF00"));
        categoryGateway.save(new Category(categoryId3, "Urgent", "#FF0000"));
        categoryGateway.save(new Category(categoryId4, "Extra", "#FFFF00"));

        // Create tasks with different categories
        Info info1 = new Info.Builder("Work Task")
                .category(categoryId1)
                .build();
        taskGateway.saveTaskAvailable(new TaskAvailable(info1));

        Info info2 = new Info.Builder("Personal Task")
                .category(categoryId2)
                .build();
        taskGateway.saveTaskAvailable(new TaskAvailable(info2));

        // Delete only the Work category
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId1);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertTrue(testPresenter.lastOutputData.getMessage() != null);

        // Verify only Work task has empty category
        List<Info> tasks = taskGateway.getAllAvailableTasks();
        Info workTask = tasks.stream()
                .filter(t -> t.getName().equals("Work Task"))
                .findFirst().orElse(null);
        Info personalTask = tasks.stream()
                .filter(t -> t.getName().equals("Personal Task"))
                .findFirst().orElse(null);

        assertNotNull(workTask);
        assertNotNull(personalTask);
        assertTrue(workTask.getCategory() == null || workTask.getCategory().isEmpty());
        assertEquals(categoryId2, personalTask.getCategory());
    }

    @Test
    void testDeleteCategoryWithMixedTasks() {
        // Create categories
        String categoryId = UUID.randomUUID().toString();
        categoryGateway.save(new Category(categoryId, "Work", "#0000FF"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Personal", "#00FF00"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Urgent", "#FF0000"));
        categoryGateway.save(new Category(UUID.randomUUID().toString(), "Extra", "#FFFF00"));

        // Create tasks - some with category, some without
        Info info1 = new Info.Builder("Task with category")
                .category(categoryId)
                .build();
        taskGateway.saveTaskAvailable(new TaskAvailable(info1));

        Info info2 = new Info.Builder("Task without category")
                .build();
        taskGateway.saveTaskAvailable(new TaskAvailable(info2));

        Info info3 = new Info.Builder("Another task with category")
                .category(categoryId)
                .build();
        taskGateway.saveTaskAvailable(new TaskAvailable(info3));

        // Delete the category
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertTrue(testPresenter.lastOutputData.getMessage() != null);

        // Verify tasks were updated - the ones with the deleted category should have null category
        List<Info> tasks = taskGateway.getAllAvailableTasks();
        
        // Find the specific tasks we created in this test
        Info taskWithCategory1 = tasks.stream()
                .filter(t -> t.getName().equals("Task with category"))
                .findFirst().orElse(null);
        Info taskWithoutCategory = tasks.stream()
                .filter(t -> t.getName().equals("Task without category"))
                .findFirst().orElse(null);
        Info taskWithCategory2 = tasks.stream()
                .filter(t -> t.getName().equals("Another task with category"))
                .findFirst().orElse(null);
        
        assertNotNull(taskWithCategory1);
        assertNotNull(taskWithoutCategory);
        assertNotNull(taskWithCategory2);
        
        // Verify categories were updated correctly
        assertTrue(taskWithCategory1.getCategory() == null || taskWithCategory1.getCategory().isEmpty());
        assertTrue(taskWithoutCategory.getCategory() == null || taskWithoutCategory.getCategory().isEmpty());
        assertTrue(taskWithCategory2.getCategory() == null || taskWithCategory2.getCategory().isEmpty());
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestDeleteCategoryPresenter implements DeleteCategoryOutputBoundary {
        DeleteCategoryOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(DeleteCategoryOutputData outputData) {
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