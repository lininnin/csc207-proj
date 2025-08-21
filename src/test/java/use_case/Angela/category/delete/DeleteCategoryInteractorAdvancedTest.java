package use_case.Angela.category.delete;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Category;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced comprehensive test class for DeleteCategoryInteractor.
 * Tests complex scenarios, edge cases, error conditions, and concurrent scenarios.
 */
class DeleteCategoryInteractorAdvancedTest {

    private TestDeleteCategoryDataAccess categoryDataAccess;
    private TestDeleteCategoryTaskDataAccess taskDataAccess;
    private TestDeleteCategoryEventDataAccess eventDataAccess;
    private TestDeleteCategoryPresenter presenter;
    private DeleteCategoryInteractor interactor;

    @BeforeEach
    void setUp() {
        categoryDataAccess = new TestDeleteCategoryDataAccess();
        taskDataAccess = new TestDeleteCategoryTaskDataAccess();
        eventDataAccess = new TestDeleteCategoryEventDataAccess();
        presenter = new TestDeleteCategoryPresenter();
        interactor = new DeleteCategoryInteractor(categoryDataAccess, taskDataAccess, eventDataAccess, presenter);
    }

    @Test
    void testSuccessfulDeleteCategoryWithNoReferences() {
        // Setup category with no tasks or events
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5); // More than minimum
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals("cat1", presenter.lastSuccessData.getCategoryId());
        assertTrue(presenter.lastSuccessData.getMessage().contains("deleted successfully"));
    }

    @Test
    void testSuccessfulDeleteCategoryWithTaskReferences() {
        // Setup category with tasks
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5); // More than minimum
        
        // Add tasks that reference this category
        taskDataAccess.addTaskReference("task1", "cat1");
        taskDataAccess.addTaskReference("task2", "cat1");
        taskDataAccess.addTaskReference("task3", "cat1");
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(3, taskDataAccess.getUpdatedTasksCount());
        assertTrue(categoryDataAccess.isDeleted("cat1"));
    }

    @Test
    void testSuccessfulDeleteCategoryWithEventReferences() {
        // Setup category with events
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5); // More than minimum
        
        // Add events that reference this category
        eventDataAccess.addEventReference("event1", "cat1");
        eventDataAccess.addEventReference("event2", "cat1");
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(2, eventDataAccess.getUpdatedEventsCount());
        assertTrue(categoryDataAccess.isDeleted("cat1"));
    }

    @Test
    void testSuccessfulDeleteCategoryWithBothTaskAndEventReferences() {
        // Setup category with both tasks and events
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5); // More than minimum
        
        // Add tasks
        taskDataAccess.addTaskReference("task1", "cat1");
        taskDataAccess.addTaskReference("task2", "cat1");
        
        // Add events
        eventDataAccess.addEventReference("event1", "cat1");
        eventDataAccess.addEventReference("event2", "cat1");
        eventDataAccess.addEventReference("event3", "cat1");
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(2, taskDataAccess.getUpdatedTasksCount());
        assertEquals(3, eventDataAccess.getUpdatedEventsCount());
        assertTrue(categoryDataAccess.isDeleted("cat1"));
    }

    @Test
    void testFailureCategoryNotFound() {
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("non-existent");
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Category not found", presenter.lastError);
    }

    @Test
    void testFailureNullCategoryId() {
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(null);
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Category not found", presenter.lastError);
    }

    @Test
    void testFailureEmptyCategoryId() {
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("");
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Category not found", presenter.lastError);
    }

    @Test
    void testFailureWhitespaceCategoryId() {
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("   ");
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Category not found", presenter.lastError);
    }

    @Test
    void testFailureTooFewCategoriesRemaining() {
        // Setup exactly 3 categories (minimum required)
        categoryDataAccess.addCategory(new Category("cat1", "Work", "#FF0000"));
        categoryDataAccess.addCategory(new Category("cat2", "Personal", "#00FF00"));
        categoryDataAccess.addCategory(new Category("cat3", "Urgent", "#0000FF"));
        categoryDataAccess.setCategoryCount(3);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Cannot delete category: minimum 3 categories required", presenter.lastError);
    }

    @Test
    void testSuccessDeleteWhenMoreThan3CategoriesRemain() {
        // Setup 4 categories (can delete one and still have 3)
        categoryDataAccess.addCategory(new Category("cat1", "Work", "#FF0000"));
        categoryDataAccess.addCategory(new Category("cat2", "Personal", "#00FF00"));
        categoryDataAccess.addCategory(new Category("cat3", "Urgent", "#0000FF"));
        categoryDataAccess.addCategory(new Category("cat4", "Extra", "#FFFF00"));
        categoryDataAccess.setCategoryCount(4);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertTrue(categoryDataAccess.isDeleted("cat1"));
    }

    @Test
    void testFailureCategoryDeletionFails() {
        // Setup category but make deletion fail
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5); // Sufficient categories
        categoryDataAccess.setDeleteSuccess(false);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to delete category", presenter.lastError);
    }

    @Test
    void testFailureTaskUpdateFails() {
        // Setup category with tasks, but make task updates fail
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        taskDataAccess.addTaskReference("task1", "cat1");
        taskDataAccess.setUpdateSuccess(false);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to delete category", presenter.lastError);
    }

    @Test
    void testFailureEventUpdateFails() {
        // Setup category with events, but make event updates fail
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        eventDataAccess.addEventReference("event1", "cat1");
        eventDataAccess.setUpdateSuccess(false);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to delete category", presenter.lastError);
    }

    @Test
    void testPartialFailureTaskUpdateFailsButEventUpdateSucceeds() {
        // Setup category with both tasks and events
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        // Add references
        taskDataAccess.addTaskReference("task1", "cat1");
        eventDataAccess.addEventReference("event1", "cat1");
        
        // Make task update fail but event update succeed
        taskDataAccess.setUpdateSuccess(false);
        eventDataAccess.setUpdateSuccess(true);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        // Should fail overall due to task update failure
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to delete category", presenter.lastError);
        
        // Category should not be deleted
        assertFalse(categoryDataAccess.isDeleted("cat1"));
    }

    @Test
    void testMassiveNumberOfReferences() {
        // Test with large number of task and event references
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        // Add many task references
        for (int i = 1; i <= 1000; i++) {
            taskDataAccess.addTaskReference("task" + i, "cat1");
        }
        
        // Add many event references
        for (int i = 1; i <= 1000; i++) {
            eventDataAccess.addEventReference("event" + i, "cat1");
        }
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(1000, taskDataAccess.getUpdatedTasksCount());
        assertEquals(1000, eventDataAccess.getUpdatedEventsCount());
        assertTrue(categoryDataAccess.isDeleted("cat1"));
    }

    @Test
    void testDeleteCategoryWithSpecialCharacters() {
        Category category = new Category("cat-special", "Work@#$%^&*()", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat-special");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals("cat-special", presenter.lastSuccessData.getCategoryId());
    }

    @Test
    void testDeleteCategoryWithUnicodeCharacters() {
        Category category = new Category("cat-unicode", "カテゴリ 📁 ✨", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat-unicode");
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals("cat-unicode", presenter.lastSuccessData.getCategoryId());
    }

    @Test
    void testExceptionHandlingInCategoryDataAccess() {
        categoryDataAccess.setShouldThrowException(true);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        
        assertThrows(RuntimeException.class, () -> interactor.execute(inputData));
    }

    @Test
    void testExceptionHandlingInTaskDataAccess() {
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        taskDataAccess.setShouldThrowException(true);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        
        assertThrows(RuntimeException.class, () -> interactor.execute(inputData));
    }

    @Test
    void testExceptionHandlingInEventDataAccess() {
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        
        eventDataAccess.setShouldThrowException(true);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        
        assertThrows(RuntimeException.class, () -> interactor.execute(inputData));
    }

    @Test
    void testConcurrentModificationScenario() {
        // Category exists when checked but disappears during deletion
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.setCategoryCount(5);
        categoryDataAccess.setRemoveCategoryBeforeDelete(true);
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        // Should fail at deletion stage
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to delete category", presenter.lastError);
    }

    @Test
    void testBoundaryExactly3Categories() {
        // Test edge case where we have exactly 3 categories
        categoryDataAccess.addCategory(new Category("cat1", "Work", "#FF0000"));
        categoryDataAccess.addCategory(new Category("cat2", "Personal", "#00FF00"));
        categoryDataAccess.addCategory(new Category("cat3", "Urgent", "#0000FF"));
        categoryDataAccess.setCategoryCount(3); // Exactly at minimum
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        // Should fail - cannot go below 3
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Cannot delete category: minimum 3 categories required", presenter.lastError);
    }

    @Test
    void testBoundary4CategoriesCanDelete() {
        // Test edge case where we have exactly 4 categories (can delete one)
        categoryDataAccess.addCategory(new Category("cat1", "Work", "#FF0000"));
        categoryDataAccess.setCategoryCount(4); // One more than minimum
        
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat1");
        interactor.execute(inputData);
        
        // Should succeed - will leave exactly 3
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    /**
     * Test implementations of data access interfaces.
     */
    private static class TestDeleteCategoryDataAccess implements DeleteCategoryCategoryDataAccessInterface {
        private Map<String, Category> categories = new HashMap<>();
        private Map<String, Boolean> deletedCategories = new HashMap<>();
        private int categoryCount = 5; // Default to more than minimum
        private boolean deleteSuccess = true;
        private boolean shouldThrowException = false;
        private boolean removeCategoryBeforeDelete = false;

        void addCategory(Category category) {
            categories.put(category.getId(), category);
        }

        void setCategoryCount(int count) {
            this.categoryCount = count;
        }

        void setDeleteSuccess(boolean success) {
            this.deleteSuccess = success;
        }

        void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        void setRemoveCategoryBeforeDelete(boolean remove) {
            this.removeCategoryBeforeDelete = remove;
        }

        boolean isDeleted(String categoryId) {
            return deletedCategories.getOrDefault(categoryId, false);
        }

        @Override
        public Category getCategoryById(String id) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            return categories.get(id);
        }

        @Override
        public boolean exists(Category category) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            return categories.containsKey(category.getId());
        }

        @Override
        public boolean deleteCategory(Category category) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }

            String categoryId = category.getId();
            if (removeCategoryBeforeDelete) {
                categories.remove(categoryId);
                return false; // Simulate concurrent deletion
            }

            if (deleteSuccess) {
                deletedCategories.put(categoryId, true);
                return true;
            }
            return false;
        }

        @Override
        public int getCategoryCount() {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            return categoryCount;
        }
    }

    private static class TestDeleteCategoryTaskDataAccess implements DeleteCategoryTaskDataAccessInterface {
        private List<String> taskReferences = new ArrayList<>();
        private boolean updateSuccess = true;
        private boolean shouldThrowException = false;
        private int updatedTasksCount = 0;

        void addTaskReference(String taskId, String categoryId) {
            taskReferences.add(taskId + ":" + categoryId);
        }

        void setUpdateSuccess(boolean success) {
            this.updateSuccess = success;
        }

        void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        int getUpdatedTasksCount() {
            return updatedTasksCount;
        }

        @Override
        public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
            return new ArrayList<>(); // Return empty list for testing
        }

        @Override
        public List<Task> findTodaysTasksByCategory(String categoryId) {
            return new ArrayList<>(); // Return empty list for testing
        }

        @Override
        public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
            return updateSuccess;
        }

        @Override
        public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
            return updateSuccess;
        }

        @Override
        public List<TaskAvailable> findAvailableTasksWithEmptyCategory() {
            return new ArrayList<>(); // Return empty list for testing
        }

        @Override
        public List<Task> findTodaysTasksWithEmptyCategory() {
            return new ArrayList<>(); // Return empty list for testing
        }

        public boolean updateTasksCategoryToNull(String categoryId) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }

            if (updateSuccess) {
                // Count how many tasks reference this category
                updatedTasksCount = (int) taskReferences.stream()
                        .filter(ref -> ref.endsWith(":" + categoryId))
                        .count();
                return true;
            }
            return false;
        }
    }

    private static class TestDeleteCategoryEventDataAccess implements DeleteCategoryEventDataAccessInterface {
        private List<String> eventReferences = new ArrayList<>();
        private boolean updateSuccess = true;
        private boolean shouldThrowException = false;
        private int updatedEventsCount = 0;

        void addEventReference(String eventId, String categoryId) {
            eventReferences.add(eventId + ":" + categoryId);
        }

        void setUpdateSuccess(boolean success) {
            this.updateSuccess = success;
        }

        void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        int getUpdatedEventsCount() {
            return updatedEventsCount;
        }

        @Override
        public List<Info> findAvailableEventsByCategory(String categoryId) {
            return new ArrayList<>();
        }

        @Override
        public List<Info> findTodaysEventsByCategory(String categoryId) {
            return new ArrayList<>();
        }

        @Override
        public boolean clearAvailableEventCategory(String eventId) {
            return updateSuccess;
        }

        @Override
        public boolean clearTodaysEventCategory(String eventId) {
            return updateSuccess;
        }

        @Override
        public List<Info> findAvailableEventsWithEmptyCategory() {
            return new ArrayList<>();
        }

        @Override
        public List<Info> findTodaysEventsWithEmptyCategory() {
            return new ArrayList<>();
        }

        public boolean updateEventsCategoryToNull(String categoryId) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }

            if (updateSuccess) {
                // Count how many events reference this category
                updatedEventsCount = (int) eventReferences.stream()
                        .filter(ref -> ref.endsWith(":" + categoryId))
                        .count();
                return true;
            }
            return false;
        }
    }

    private static class TestDeleteCategoryPresenter implements DeleteCategoryOutputBoundary {
        DeleteCategoryOutputData lastSuccessData;
        String lastError;

        @Override
        public void prepareSuccessView(DeleteCategoryOutputData outputData) {
            this.lastSuccessData = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastError = error;
            this.lastSuccessData = null;
        }
    }
}