package use_case.Angela.task.mark_complete;

import data_access.InMemoryTaskGateway;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MarkTaskCompleteInteractor following Clean Architecture principles.
 */
class MarkTaskCompleteInteractorTest {

    private InMemoryTaskGateway taskGateway;
    private TestMarkTaskCompletePresenter testPresenter;
    private MarkTaskCompleteInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskGateway();
        testPresenter = new TestMarkTaskCompletePresenter();
        interactor = new MarkTaskCompleteInteractor(taskGateway, testPresenter);
    }

    @Test
    void testSuccessfulMarkComplete() {
        // Create an available task and add it to today
        Info info = new Info.Builder("Test Task")
                .description("Test description")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        // Create input data to mark complete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, true);

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(taskId, testPresenter.lastOutputData.getTaskId());
        assertEquals("Test Task", testPresenter.lastOutputData.getTaskName());
        assertNotNull(testPresenter.lastOutputData.getCompletionTime());
        assertNull(testPresenter.lastError);

        // Verify task is marked complete in storage
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    void testSuccessfulMarkIncomplete() {
        // Create an available task and add it to today
        Info info = new Info.Builder("Completed Task")
                .description("Already done")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.MEDIUM, null);
        todayTask.markComplete();  // Mark it complete first
        String taskId = todayTask.getId();

        // Create input data to mark incomplete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, false);

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(taskId, testPresenter.lastOutputData.getTaskId());
        // Output data doesn't have isCompleted, check the actual task instead
        assertEquals("Completed Task", testPresenter.lastOutputData.getTaskName());
        assertNull(testPresenter.lastError);

        // Verify task is marked incomplete in storage
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertFalse(updatedTask.isCompleted());
    }

    @Test
    void testToggleCompletion() {
        // Create an available task and add it to today
        Info info = new Info.Builder("Toggle Task")
                .description("Test toggling")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, null, null);
        String taskId = todayTask.getId();

        // Initially should be incomplete
        assertFalse(todayTask.isCompleted());

        // Mark complete
        MarkTaskCompleteInputData inputData1 = new MarkTaskCompleteInputData(taskId, true);
        interactor.execute(inputData1);
        
        Task updatedTask1 = (Task) taskGateway.getTodayTaskById(taskId);
        assertTrue(updatedTask1.isCompleted());

        // Mark incomplete again
        MarkTaskCompleteInputData inputData2 = new MarkTaskCompleteInputData(taskId, false);
        interactor.execute(inputData2);
        
        Task updatedTask2 = (Task) taskGateway.getTodayTaskById(taskId);
        assertFalse(updatedTask2.isCompleted());
    }

    @Test
    void testMarkCompleteOverdueTask() {
        // Create an overdue task
        Info info = new Info.Builder("Overdue Task")
                .description("Should have been done yesterday")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        // Add with past due date
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, yesterday);
        String taskId = todayTask.getId();

        // Verify it's overdue
        assertTrue(todayTask.isOverdue());

        // Mark complete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, true);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastOutputData.getCompletionTime());
        
        // Completed overdue tasks should still be marked complete
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertTrue(updatedTask.isCompleted());
        // Completed tasks are never overdue
        assertFalse(updatedTask.isOverdue());
    }

    @Test
    void testFailureTaskNotFound() {
        // Try to mark a non-existent task
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("non-existent-id", true);

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in Today's Tasks", testPresenter.lastError);
    }

    @Test
    void testMarkCompleteAlreadyCompletedTask() {
        // Create a completed task
        Info info = new Info.Builder("Already Complete")
                .description("Done task")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.LOW, LocalDate.now());
        todayTask.markComplete();
        String taskId = todayTask.getId();

        // Try to mark complete again (should succeed but no change)
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, true);
        interactor.execute(inputData);

        // Verify success (idempotent operation)
        assertNotNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastOutputData.getCompletionTime());
        assertNull(testPresenter.lastError);

        // Task should still be completed
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    void testMarkIncompleteAlreadyIncompleteTask() {
        // Create an incomplete task
        Info info = new Info.Builder("Already Incomplete")
                .description("Not done")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, null, null);
        String taskId = todayTask.getId();

        // Try to mark incomplete again (should succeed but no change)
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, false);
        interactor.execute(inputData);

        // Verify success (idempotent operation)
        assertNotNull(testPresenter.lastOutputData);
        // Output data doesn't have isCompleted, check the actual task instead
        assertNull(testPresenter.lastError);

        // Task should still be incomplete
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertFalse(updatedTask.isCompleted());
    }

    @Test
    void testMarkCompleteWithNullPriority() {
        // Create task with no priority
        Info info = new Info.Builder("No Priority Task")
                .description("Task without priority")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, null, null);
        String taskId = todayTask.getId();

        // Mark complete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, true);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastOutputData.getCompletionTime());
        assertNull(testPresenter.lastError);
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestMarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {
        MarkTaskCompleteOutputData lastOutputData;
        String lastError;
        String lastMessage;

        @Override
        public void presentSuccess(MarkTaskCompleteOutputData outputData, String message) {
            this.lastOutputData = outputData;
            this.lastMessage = message;
            this.lastError = null;
        }

        @Override
        public void presentError(String error) {
            this.lastError = error;
            this.lastOutputData = null;
            this.lastMessage = null;
        }
    }
}