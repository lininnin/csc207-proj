package use_case.Angela.task.delete;

import data_access.InMemoryTaskGateway;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DeleteTaskInteractor following Clean Architecture principles.
 */
class DeleteTaskInteractorTest {

    private InMemoryTaskGateway taskGateway;
    private TestDeleteTaskPresenter testPresenter;
    private DeleteTaskInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskGateway();
        testPresenter = new TestDeleteTaskPresenter();
        interactor = new DeleteTaskInteractor(taskGateway, testPresenter);
    }

    @Test
    void testSuccessfulDeleteFromAvailableOnly() {
        // Create an available task (not in today's list)
        Info info = new Info.Builder("Task to Delete")
                .description("Will be deleted")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        String taskId = availableTask.getId();

        // Create input data - deleting from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, true);

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(taskId, testPresenter.lastOutputData.getTaskId());
        assertTrue(testPresenter.lastOutputData.getMessage().contains("deleted"));
        assertFalse(testPresenter.lastOutputData.isDeletedFromBoth());
        assertNull(testPresenter.lastError);

        // Verify task was deleted from available tasks
        assertNull(taskGateway.getAvailableTaskById(taskId));
        assertTrue(taskGateway.getAllAvailableTasks().isEmpty());
    }

    @Test
    void testSuccessfulDeleteFromBothLists() {
        // Create an available task and add it to today's list
        Info info = new Info.Builder("Task in Both Lists")
                .description("In available and today")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        String templateId = availableTask.getId();
        
        // Add to today's list
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, LocalDate.now().plusDays(1));

        // Create input data with template ID - deleting from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData(templateId, true);

        // Execute the interactor
        interactor.execute(inputData);

        // When task exists in both lists, should show warning dialog
        assertNotNull(testPresenter.lastWarningTaskId);
        assertEquals(templateId, testPresenter.lastWarningTaskId);
        assertNull(testPresenter.lastError);

        // Tasks should still exist (warning shown, not deleted yet)
        assertNotNull(taskGateway.getAvailableTaskById(templateId));
        assertFalse(taskGateway.getAllAvailableTasks().isEmpty());
        assertFalse(taskGateway.getTodaysTasks().isEmpty());
    }

    @Test
    void testDeleteOneTimeTask() {
        // Create a one-time task
        Info info = new Info.Builder("One-Time Event")
                .description("Should be deletable")
                .build();
        TaskAvailable oneTimeTask = new TaskAvailable(
                info.getId(),
                info,
                null,
                true  // isOneTime = true
        );
        taskGateway.saveTaskAvailable(oneTimeTask);
        String taskId = oneTimeTask.getId();

        // Delete the one-time task from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, true);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(taskId, testPresenter.lastOutputData.getTaskId());
        assertNull(testPresenter.lastError);

        // Verify task was deleted
        assertNull(taskGateway.getAvailableTaskById(taskId));
    }

    @Test
    void testDeleteCompletedTask() {
        // Create a task and mark it complete
        Info info = new Info.Builder("Completed Task")
                .description("Already done")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        String templateId = availableTask.getId();
        
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.MEDIUM, LocalDate.now());
        todayTask.markComplete();

        // Delete the completed task from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData(templateId, true);
        interactor.execute(inputData);

        // When task exists in both lists, should show warning dialog (even if completed)
        assertNotNull(testPresenter.lastWarningTaskId);
        assertEquals(templateId, testPresenter.lastWarningTaskId);
        assertNull(testPresenter.lastError);

        // Tasks should still exist (warning shown, not deleted yet)
        assertNotNull(taskGateway.getAvailableTaskById(templateId));
        assertFalse(taskGateway.getTodaysTasks().isEmpty());
    }

    @Test
    void testDeleteOverdueTask() {
        // Create an overdue task
        Info info = new Info.Builder("Overdue Task")
                .description("Past due date")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        String templateId = availableTask.getId();
        
        // Add with past due date
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, yesterday);
        assertTrue(todayTask.isOverdue());

        // Delete the overdue task from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData(templateId, true);
        interactor.execute(inputData);

        // When task exists in both lists, should show warning dialog (even if overdue)
        assertNotNull(testPresenter.lastWarningTaskId);
        assertEquals(templateId, testPresenter.lastWarningTaskId);
        assertNull(testPresenter.lastError);

        // Tasks should still exist (warning shown, not deleted yet)
        assertNotNull(taskGateway.getAvailableTaskById(templateId));
        assertFalse(taskGateway.getTodaysTasks().isEmpty());
    }

    @Test
    void testFailureTaskNotFound() {
        // Try to delete a non-existent task from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData("non-existent-id", true);

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in Available Tasks", testPresenter.lastError);
    }

    @Test
    void testDeleteMultipleTasks() {
        // Create multiple tasks
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        TaskAvailable task3 = new TaskAvailable(info3);
        
        taskGateway.saveTaskAvailable(task1);
        taskGateway.saveTaskAvailable(task2);
        taskGateway.saveTaskAvailable(task3);
        
        // Add task2 to today's list
        taskGateway.addTaskToToday(task2, Task.Priority.LOW, null);
        
        assertEquals(3, taskGateway.getAllAvailableTasks().size());
        assertEquals(1, taskGateway.getTodaysTasks().size());

        // Delete task2 from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData(task2.getId(), true);
        interactor.execute(inputData);

        // Should show warning dialog since task2 is in both lists
        assertNotNull(testPresenter.lastWarningTaskId);
        assertEquals(task2.getId(), testPresenter.lastWarningTaskId);
        
        // All tasks should still exist (warning shown, not deleted yet)
        assertEquals(3, taskGateway.getAllAvailableTasks().size());
        assertEquals(1, taskGateway.getTodaysTasks().size());
        assertNotNull(taskGateway.getAvailableTaskById(task1.getId()));
        assertNotNull(taskGateway.getAvailableTaskById(task2.getId()));
        assertNotNull(taskGateway.getAvailableTaskById(task3.getId()));
    }

    @Test
    void testDeleteTaskWithNullFields() {
        // Create task with minimal information
        Info info = new Info.Builder("Minimal Task")
                .description(null)
                .category(null)
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        String taskId = availableTask.getId();

        // Delete the task from available view
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, true);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        // Check message contains "deleted" but may not contain task name
        assertTrue(testPresenter.lastOutputData.getMessage().contains("deleted") || 
                  testPresenter.lastOutputData.getMessage().contains("Deleted"));
        assertNull(testPresenter.lastError);

        // Verify task was deleted
        assertNull(taskGateway.getAvailableTaskById(taskId));
    }

    @Test
    void testDeleteFromTodayView() {
        // Create task in both lists
        Info info = new Info.Builder("Delete from Today")
                .description("Testing deletion from today view")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        String templateId = availableTask.getId();
        
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, LocalDate.now());

        // When deleting from today's view with isFromAvailable=false,
        // it actually deletes completely (both from today and available)
        DeleteTaskInputData inputData = new DeleteTaskInputData(templateId, false);
        interactor.execute(inputData);

        // Should succeed in deleting from both lists
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(templateId, testPresenter.lastOutputData.getTaskId());
        assertTrue(testPresenter.lastOutputData.getMessage().contains("deleted"));
        assertTrue(testPresenter.lastOutputData.isDeletedFromBoth());
        assertNull(testPresenter.lastError);
        
        // Both tasks should be deleted
        assertNull(taskGateway.getAvailableTaskById(templateId));
        assertTrue(taskGateway.getTodaysTasks().isEmpty());
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestDeleteTaskPresenter implements DeleteTaskOutputBoundary {
        DeleteTaskOutputData lastOutputData;
        String lastError;
        String lastWarningTaskId;
        String lastWarningTaskName;

        @Override
        public void prepareSuccessView(DeleteTaskOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }

        @Override
        public void showDeleteFromBothWarning(String taskId, String taskName) {
            this.lastWarningTaskId = taskId;
            this.lastWarningTaskName = taskName;
        }
    }
}