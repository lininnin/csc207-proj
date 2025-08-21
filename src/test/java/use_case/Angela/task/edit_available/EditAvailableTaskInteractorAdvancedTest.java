package use_case.Angela.task.edit_available;

import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced comprehensive test class for EditAvailableTaskInteractor.
 * Tests advanced edge cases, error conditions, boundary cases, and concurrent scenarios.
 */
class EditAvailableTaskInteractorAdvancedTest {

    private TestEditAvailableTaskDataAccess dataAccess;
    private TestEditCategoryDataAccess categoryDataAccess;
    private TestEditAvailableTaskPresenter presenter;
    private EditAvailableTaskInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestEditAvailableTaskDataAccess();
        categoryDataAccess = new TestEditCategoryDataAccess();
        presenter = new TestEditAvailableTaskPresenter();
        interactor = new EditAvailableTaskInteractor(dataAccess, categoryDataAccess, presenter);
    }

    @Test
    void testSuccessfulEditAllFields() {
        // Setup existing task and category
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        Category category = new Category("cat1", "Work", "#FF0000");
        categoryDataAccess.addCategory(category);
        
        // Execute edit
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", "cat1", true
        );
        interactor.execute(inputData);
        
        // Verify success
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals("task1", presenter.lastSuccessData.getTaskId());
        assertEquals("Updated Task", presenter.lastSuccessData.getTaskName());
        assertEquals("Task updated successfully", presenter.lastSuccessData.getMessage());
    }

    @Test
    void testFailureNullTaskName() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", null, "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task name cannot be empty", presenter.lastError);
    }

    @Test
    void testFailureEmptyTaskName() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "", "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task name cannot be empty", presenter.lastError);
    }

    @Test
    void testFailureWhitespaceOnlyTaskName() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "   ", "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task name cannot be empty", presenter.lastError);
    }

    @Test
    void testFailureTaskNameExceeds20Characters() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        String longName = "ThisIsAVeryLongTaskNameThatExceeds20Characters";
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", longName, "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("The name of Task cannot exceed 20 letters", presenter.lastError);
    }

    @Test
    void testSuccessTaskNameExactly20Characters() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        String exactName = "12345678901234567890"; // exactly 20 chars
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", exactName, "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(exactName, presenter.lastSuccessData.getTaskName());
    }

    @Test
    void testFailureDescriptionExceeds100Characters() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        String longDescription = "A".repeat(101); // 101 characters
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", longDescription, null, false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Description cannot exceed 100 characters", presenter.lastError);
    }

    @Test
    void testSuccessDescriptionExactly100Characters() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        String exactDescription = "A".repeat(100); // exactly 100 characters
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", exactDescription, null, false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testSuccessNullDescription() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", null, null, false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testSuccessEmptyDescription() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "", null, false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testFailureTaskNotFound() {
        // Don't add any task to data access
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "non-existent", "Updated Task", "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task not found in Available Tasks", presenter.lastError);
    }

    @Test
    void testFailureDuplicateNameInSameCategory() {
        // Setup two tasks in same category
        TaskAvailable existingTask1 = createTestTask("task1", "Task A", "Description A", false);
        TaskAvailable existingTask2 = createTestTask("task2", "Task B", "Description B", false);
        dataAccess.addTask(existingTask1);
        dataAccess.addTask(existingTask2);
        
        // Set up duplicate check to return true
        dataAccess.setDuplicateNameExists(true);
        
        // Try to rename task2 to same name as task1 in same category
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task2", "Task A", "Updated description", "cat1", false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("A task with this name already exists in the same category", presenter.lastError);
    }

    @Test
    void testSuccessEditToSameName() {
        // Task should be able to "edit" to its current name
        TaskAvailable existingTask = createTestTask("task1", "Existing Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        dataAccess.setDuplicateNameExists(false); // No duplicate since we're editing the same task
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Existing Task", "Updated description", null, true
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testFailureInvalidCategory() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", "non-existent-category", false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Invalid category selected", presenter.lastError);
    }

    @Test
    void testSuccessValidCategory() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        Category category = new Category("valid-cat", "Valid Category", "#00FF00");
        categoryDataAccess.addCategory(category);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", "valid-cat", false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testSuccessNullCategory() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testSuccessEmptyCategory() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", "", false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testFailureDataAccessUpdateFails() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        dataAccess.setUpdateSuccess(false);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to update task", presenter.lastError);
    }

    @Test
    void testToggleOneTimeFlag() {
        // Test toggling from false to true
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", null, true
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        
        // Reset presenter
        presenter = new TestEditAvailableTaskPresenter();
        interactor = new EditAvailableTaskInteractor(dataAccess, categoryDataAccess, presenter);
        
        // Now test toggling back to false
        TaskAvailable updatedTask = createTestTask("task1", "Updated Task", "Updated description", true);
        dataAccess.clearTasks();
        dataAccess.addTask(updatedTask);
        
        EditAvailableTaskInputData inputData2 = new EditAvailableTaskInputData(
                "task1", "Final Task", "Final description", null, false
        );
        interactor.execute(inputData2);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
    }

    @Test
    void testSpecialCharactersInTaskName() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        String specialName = "@#$%^&*()_+-={}[]|";
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", specialName, "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(specialName, presenter.lastSuccessData.getTaskName());
    }

    @Test
    void testUnicodeCharactersInTaskName() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        
        String unicodeName = "タスク 📝 ✅";
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", unicodeName, "Updated description", null, false
        );
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(unicodeName, presenter.lastSuccessData.getTaskName());
    }

    @Test
    void testExceptionHandlingInDataAccess() {
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        dataAccess.setShouldThrowException(true);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", null, false
        );
        
        // Should throw exception and not be caught by interactor
        assertThrows(RuntimeException.class, () -> interactor.execute(inputData));
    }

    @Test
    void testConcurrentModificationScenario() {
        // Task exists when checked but disappears during update
        TaskAvailable existingTask = createTestTask("task1", "Original Task", "Original desc", false);
        dataAccess.addTask(existingTask);
        dataAccess.setRemoveTaskBeforeUpdate(true);
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "task1", "Updated Task", "Updated description", null, false
        );
        interactor.execute(inputData);
        
        // Should fail at update stage
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to update task", presenter.lastError);
    }

    private TaskAvailable createTestTask(String id, String name, String description, boolean isOneTime) {
        Info info = new Info.Builder(name).description(description).build();
        return new TaskAvailable(id, info, null, isOneTime);
    }

    /**
     * Test implementation of EditAvailableTaskDataAccessInterface.
     */
    private static class TestEditAvailableTaskDataAccess implements EditAvailableTaskDataAccessInterface {
        private Map<String, TaskAvailable> tasks = new HashMap<>();
        private boolean duplicateNameExists = false;
        private boolean updateSuccess = true;
        private boolean shouldThrowException = false;
        private boolean removeTaskBeforeUpdate = false;

        void addTask(TaskAvailable task) {
            tasks.put(task.getId(), task);
        }

        void clearTasks() {
            tasks.clear();
        }

        void setDuplicateNameExists(boolean exists) {
            this.duplicateNameExists = exists;
        }

        void setUpdateSuccess(boolean success) {
            this.updateSuccess = success;
        }

        void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        void setRemoveTaskBeforeUpdate(boolean remove) {
            this.removeTaskBeforeUpdate = remove;
        }

        @Override
        public TaskAvailable getTaskAvailableById(String taskId) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            return tasks.get(taskId);
        }

        @Override
        public boolean taskExistsWithNameAndCategoryExcluding(String name, String categoryId, String excludeTaskId) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            return duplicateNameExists;
        }

        @Override
        public boolean updateAvailableTask(String taskId, String name, String description, String categoryId, boolean isOneTime) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }

            if (removeTaskBeforeUpdate) {
                // Simulate concurrent removal
                tasks.remove(taskId);
                return false;
            }

            return updateSuccess;
        }
    }

    /**
     * Test implementation of EditAvailableTaskCategoryDataAccessInterface.
     */
    private static class TestEditCategoryDataAccess implements EditAvailableTaskCategoryDataAccessInterface {
        private Map<String, Category> categories = new HashMap<>();

        void addCategory(Category category) {
            categories.put(category.getId(), category);
        }

        @Override
        public Category getCategoryById(String id) {
            return categories.get(id);
        }
    }

    /**
     * Test implementation of EditAvailableTaskOutputBoundary.
     */
    private static class TestEditAvailableTaskPresenter implements EditAvailableTaskOutputBoundary {
        EditAvailableTaskOutputData lastSuccessData;
        String lastError;

        @Override
        public void prepareSuccessView(EditAvailableTaskOutputData outputData) {
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