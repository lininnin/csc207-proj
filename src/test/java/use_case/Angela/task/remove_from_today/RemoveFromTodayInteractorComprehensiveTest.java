package use_case.Angela.task.remove_from_today;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test class for RemoveFromTodayInteractor to boost coverage.
 * Tests all edge cases, error conditions, and boundary cases.
 */
class RemoveFromTodayInteractorComprehensiveTest {

    private TestRemoveFromTodayDataAccess dataAccess;
    private TestRemoveFromTodayPresenter presenter;
    private RemoveFromTodayInteractor interactor;
    
    private Task testTask1;
    private Task testTask2;
    private Task testTask3;

    @BeforeEach
    void setUp() {
        dataAccess = new TestRemoveFromTodayDataAccess();
        presenter = new TestRemoveFromTodayPresenter();
        interactor = new RemoveFromTodayInteractor(dataAccess, presenter);
        
        // Create test tasks
        Info info1 = new Info.Builder("Test Task 1").description("First task").build();
        Info info2 = new Info.Builder("Test Task 2").description("Second task").build();
        Info info3 = new Info.Builder("Test Task 3").description("Third task").build();
        
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        
        testTask1 = new Task("template1", info1, dates, false);
        testTask1.setPriority(Task.Priority.HIGH);
        testTask2 = new Task("template2", info2, dates, false);
        testTask2.setPriority(Task.Priority.MEDIUM);
        testTask3 = new Task("template3", info3, dates, false);
        testTask3.setPriority(Task.Priority.LOW);
    }

    @Test
    void testSuccessfulRemovalOfSingleTask() {
        // Setup - Add task to data access
        dataAccess.addTask(testTask1);
        dataAccess.setRemoveSuccess(true);
        
        // Execute using the actual task ID
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(testTask1.getId());
        interactor.execute(inputData);
        
        // Verify success
        assertNotNull(presenter.lastSuccessData);
        assertNull(presenter.lastError);
        assertEquals(testTask1.getId(), presenter.lastSuccessData.getTaskId());
        assertEquals("Test Task 1", presenter.lastSuccessData.getTaskName());
        assertEquals("Task 'Test Task 1' removed from Today's list", presenter.lastSuccessData.getMessage());
    }

    @Test
    void testSuccessfulRemovalOfMultipleTasks() {
        // Setup multiple tasks
        dataAccess.addTask(testTask1);
        dataAccess.addTask(testTask2);
        dataAccess.addTask(testTask3);
        dataAccess.setRemoveSuccess(true);
        
        // Remove first task
        RemoveFromTodayInputData inputData1 = new RemoveFromTodayInputData(testTask1.getId());
        interactor.execute(inputData1);
        
        assertNotNull(presenter.lastSuccessData);
        assertEquals("Test Task 1", presenter.lastSuccessData.getTaskName());
        
        // Reset presenter
        presenter = new TestRemoveFromTodayPresenter();
        interactor = new RemoveFromTodayInteractor(dataAccess, presenter);
        
        // Remove second task
        RemoveFromTodayInputData inputData2 = new RemoveFromTodayInputData(testTask2.getId());
        interactor.execute(inputData2);
        
        assertNotNull(presenter.lastSuccessData);
        assertEquals("Test Task 2", presenter.lastSuccessData.getTaskName());
        
        // Reset presenter
        presenter = new TestRemoveFromTodayPresenter();
        interactor = new RemoveFromTodayInteractor(dataAccess, presenter);
        
        // Remove third task
        RemoveFromTodayInputData inputData3 = new RemoveFromTodayInputData(testTask3.getId());
        interactor.execute(inputData3);
        
        assertNotNull(presenter.lastSuccessData);
        assertEquals("Test Task 3", presenter.lastSuccessData.getTaskName());
    }

    @Test
    void testFailureTaskNotFound() {
        // Try to remove non-existent task
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData("non-existent");
        interactor.execute(inputData);
        
        // Verify failure
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task not found in Today's list", presenter.lastError);
    }

    @Test
    void testFailureNullTaskId() {
        // Try to remove with null task ID
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(null);
        interactor.execute(inputData);
        
        // Verify failure
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task not found in Today's list", presenter.lastError);
    }

    @Test
    void testFailureEmptyTaskId() {
        // Try to remove with empty task ID
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData("");
        interactor.execute(inputData);
        
        // Verify failure
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task not found in Today's list", presenter.lastError);
    }

    @Test
    void testFailureWhitespaceTaskId() {
        // Try to remove with whitespace-only task ID
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData("   ");
        interactor.execute(inputData);
        
        // Verify failure
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task not found in Today's list", presenter.lastError);
    }

    @Test
    void testFailureDataAccessRemovalFails() {
        // Setup - Add task but make removal fail
        dataAccess.addTask(testTask1);
        dataAccess.setRemoveSuccess(false);
        
        // Execute using the actual task ID
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(testTask1.getId());
        interactor.execute(inputData);
        
        // Verify failure - task found but removal failed
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to remove task from Today's list", presenter.lastError);
    }

    @Test
    void testFailureDataAccessThrowsException() {
        // Setup - Make data access throw exception
        dataAccess.addTask(testTask1);
        dataAccess.setShouldThrowException(true);
        
        // Execute - should not crash, should handle gracefully
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(testTask1.getId());
        
        // This should throw an exception that's not caught by the interactor
        // In a production system, this would be handled by exception handling
        assertThrows(RuntimeException.class, () -> interactor.execute(inputData));
    }

    @Test
    void testTaskWithEmptyName() {
        // Create task with minimal valid name (Info.Builder requires non-null, non-empty name)
        Info minimalNameInfo = new Info.Builder("T").build();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        Task taskWithMinimalName = new Task("template", minimalNameInfo, dates, false);
        taskWithMinimalName.setPriority(Task.Priority.HIGH);
        
        dataAccess.addTask(taskWithMinimalName);
        dataAccess.setRemoveSuccess(true);
        
        // Execute
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskWithMinimalName.getId());
        interactor.execute(inputData);
        
        // Should work with minimal name
        assertNotNull(presenter.lastSuccessData);
        assertTrue(presenter.lastSuccessData.getMessage().contains("Task 'T'"));
    }

    @Test
    void testTaskWithVeryLongName() {
        // Create task with very long name
        String longName = "A".repeat(100); // Very long name
        Info longNameInfo = new Info.Builder(longName).build();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        Task taskWithLongName = new Task("template", longNameInfo, dates, false);
        taskWithLongName.setPriority(Task.Priority.LOW);
        
        dataAccess.addTask(taskWithLongName);
        dataAccess.setRemoveSuccess(true);
        
        // Execute
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskWithLongName.getId());
        interactor.execute(inputData);
        
        // Should work with long name
        assertNotNull(presenter.lastSuccessData);
        assertEquals(longName, presenter.lastSuccessData.getTaskName());
        assertTrue(presenter.lastSuccessData.getMessage().contains(longName));
    }

    @Test
    void testTaskWithSpecialCharactersInName() {
        // Create task with special characters
        String specialName = "Task @#$%^&*()_+-={}[]|\\:;\"'<>?,./";
        Info specialNameInfo = new Info.Builder(specialName).build();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        Task taskWithSpecialName = new Task("template", specialNameInfo, dates, false);
        taskWithSpecialName.setPriority(Task.Priority.MEDIUM);
        
        dataAccess.addTask(taskWithSpecialName);
        dataAccess.setRemoveSuccess(true);
        
        // Execute
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskWithSpecialName.getId());
        interactor.execute(inputData);
        
        // Should work with special characters
        assertNotNull(presenter.lastSuccessData);
        assertEquals(specialName, presenter.lastSuccessData.getTaskName());
        assertTrue(presenter.lastSuccessData.getMessage().contains(specialName));
    }

    @Test
    void testTaskWithUnicodeCharactersInName() {
        // Create task with Unicode characters
        String unicodeName = "タスク 任务 🔥 📝 ✅";
        Info unicodeNameInfo = new Info.Builder(unicodeName).build();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        Task taskWithUnicodeName = new Task("template", unicodeNameInfo, dates, false);
        taskWithUnicodeName.setPriority(Task.Priority.HIGH);
        
        dataAccess.addTask(taskWithUnicodeName);
        dataAccess.setRemoveSuccess(true);
        
        // Execute
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskWithUnicodeName.getId());
        interactor.execute(inputData);
        
        // Should work with Unicode characters
        assertNotNull(presenter.lastSuccessData);
        assertEquals(unicodeName, presenter.lastSuccessData.getTaskName());
        assertTrue(presenter.lastSuccessData.getMessage().contains(unicodeName));
    }

    @Test
    void testRemoveTaskTwice() {
        // Setup
        dataAccess.addTask(testTask1);
        dataAccess.setRemoveSuccess(true);
        
        // First removal - should succeed
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(testTask1.getId());
        interactor.execute(inputData);
        
        assertNotNull(presenter.lastSuccessData);
        
        // Remove task from data access to simulate it being gone
        dataAccess.removeTask(testTask1.getId());
        
        // Reset presenter
        presenter = new TestRemoveFromTodayPresenter();
        interactor = new RemoveFromTodayInteractor(dataAccess, presenter);
        
        // Second removal - should fail (task not found)
        interactor.execute(inputData);
        
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Task not found in Today's list", presenter.lastError);
    }

    @Test
    void testConcurrentRemovalScenario() {
        // Setup
        dataAccess.addTask(testTask1);
        dataAccess.setRemoveSuccess(true);
        
        // Simulate concurrent access - task exists when checked, but removal fails
        // This tests the edge case where task exists during getTodayTaskById but fails during removeFromTodaysList
        dataAccess.setRemoveAfterCheck(true);
        
        // Execute
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(testTask1.getId());
        interactor.execute(inputData);
        
        // Should fail at removal stage
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to remove task from Today's list", presenter.lastError);
    }

    @Test
    void testBoundaryTaskIdFormats() {
        // Test various ID formats that might be valid
        String[] testIds = {
            "1",
            "task-123",
            "TASK_ABC",
            "task.with.dots",
            "task@domain.com",
            "123e4567-e89b-12d3-a456-426614174000", // UUID format
            "very-long-task-id-that-might-exist-in-the-system-somewhere"
        };
        
        for (String testId : testIds) {
            // Setup fresh presenter for each test
            presenter = new TestRemoveFromTodayPresenter();
            interactor = new RemoveFromTodayInteractor(dataAccess, presenter);
            
            // Execute with non-existent ID (should all fail the same way)
            RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(testId);
            interactor.execute(inputData);
            
            // All should fail with task not found
            assertNull(presenter.lastSuccessData);
            assertNotNull(presenter.lastError);
            assertEquals("Task not found in Today's list", presenter.lastError);
        }
    }

    /**
     * Test implementation of RemoveFromTodayDataAccessInterface.
     */
    private static class TestRemoveFromTodayDataAccess implements RemoveFromTodayDataAccessInterface {
        private List<Task> tasks = new ArrayList<>();
        private boolean removeSuccess = true;
        private boolean shouldThrowException = false;
        private boolean removeAfterCheck = false;
        
        void addTask(Task task) {
            tasks.add(task);
        }
        
        void removeTask(String taskId) {
            tasks.removeIf(task -> task.getId().equals(taskId));
        }
        
        void setRemoveSuccess(boolean success) {
            this.removeSuccess = success;
        }
        
        void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }
        
        void setRemoveAfterCheck(boolean removeAfterCheck) {
            this.removeAfterCheck = removeAfterCheck;
        }
        
        @Override
        public Task getTodayTaskById(String taskId) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            
            if (taskId == null || taskId.trim().isEmpty()) {
                return null;
            }
            
            return tasks.stream()
                    .filter(task -> task.getId().equals(taskId))
                    .findFirst()
                    .orElse(null);
        }
        
        @Override
        public boolean removeFromTodaysList(String taskId) {
            if (shouldThrowException) {
                throw new RuntimeException("Database connection failed");
            }
            
            if (removeAfterCheck) {
                // Simulate failure during removal (task exists but removal fails)
                // Don't actually remove the task from our list
                return false;
            }
            
            if (removeSuccess) {
                removeTask(taskId);
                return true;
            }
            
            return false;
        }
    }

    /**
     * Test implementation of RemoveFromTodayOutputBoundary.
     */
    private static class TestRemoveFromTodayPresenter implements RemoveFromTodayOutputBoundary {
        RemoveFromTodayOutputData lastSuccessData;
        String lastError;
        
        @Override
        public void prepareSuccessView(RemoveFromTodayOutputData outputData) {
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