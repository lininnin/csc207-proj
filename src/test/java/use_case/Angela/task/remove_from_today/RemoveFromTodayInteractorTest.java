package use_case.Angela.task.remove_from_today;

import data_access.InMemoryTaskGateway;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RemoveFromTodayInteractor following Clean Architecture principles.
 */
class RemoveFromTodayInteractorTest {

    private InMemoryTaskGateway taskGateway;
    private TestRemoveFromTodayPresenter testPresenter;
    private RemoveFromTodayInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskGateway();
        testPresenter = new TestRemoveFromTodayPresenter();
        interactor = new RemoveFromTodayInteractor(taskGateway, testPresenter);
    }

    @Test
    void testSuccessfulRemove() {
        // Create an available task and add it to today
        Info info = new Info.Builder("Test Task")
                .description("Test description")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        // Remove from today
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(taskId, testPresenter.lastOutputData.getTaskId());
        assertEquals("Test Task", testPresenter.lastOutputData.getTaskName());
        assertNull(testPresenter.lastError);

        // Verify task was removed from today
        List<Task> todaysTasks = taskGateway.getTodaysTasks();
        assertTrue(todaysTasks.isEmpty());

        // Verify task still exists in available
        List<Info> availableTasks = taskGateway.getAllAvailableTasks();
        assertEquals(1, availableTasks.size());
    }

    @Test
    void testRemoveCompletedTask() {
        // Create a completed task
        Info info = new Info.Builder("Completed Task")
                .description("Already done")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.MEDIUM, null);
        todayTask.markComplete();
        String taskId = todayTask.getId();

        // Remove from today
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Completed Task", testPresenter.lastOutputData.getTaskName());

        // Verify task was removed
        assertTrue(taskGateway.getTodaysTasks().isEmpty());
    }

    @Test
    void testRemoveOverdueTask() {
        // Create an overdue task
        Info info = new Info.Builder("Overdue Task")
                .description("Past due")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, yesterday);
        String taskId = todayTask.getId();
        assertTrue(todayTask.isOverdue());

        // Remove from today
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Overdue Task", testPresenter.lastOutputData.getTaskName());

        // Verify task was removed
        assertTrue(taskGateway.getTodaysTasks().isEmpty());
    }

    @Test
    void testRemoveOneTimeTask() {
        // Create a one-time task
        Info info = new Info.Builder("One-time Task")
                .description("Only once")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        availableTask.setOneTime(true);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = taskGateway.addTaskToToday(availableTask, null, null);
        String taskId = todayTask.getId();

        // Remove from today
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);

        // Verify task was removed from today
        assertTrue(taskGateway.getTodaysTasks().isEmpty());

        // One-time task should still exist in available (until midnight)
        assertEquals(1, taskGateway.getAllAvailableTasks().size());
    }

    @Test
    void testRemoveTaskWithNullPriority() {
        // Create task without priority
        Info info = new Info.Builder("No Priority Task")
                .description("No priority set")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = taskGateway.addTaskToToday(availableTask, null, null);
        String taskId = todayTask.getId();

        // Remove from today
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(taskId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertTrue(taskGateway.getTodaysTasks().isEmpty());
    }

    @Test
    void testFailureTaskNotFound() {
        // Try to remove non-existent task
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData("non-existent-id");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in Today's list", testPresenter.lastError);
    }

    @Test
    void testFailureNullTaskId() {
        // Try to remove with null ID
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(null);
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in Today's list", testPresenter.lastError);
    }

    @Test
    void testFailureEmptyTaskId() {
        // Try to remove with empty ID
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData("");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in Today's list", testPresenter.lastError);
    }

    @Test
    void testRemoveMultipleTasks() {
        // Create multiple tasks
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        
        TaskAvailable available1 = new TaskAvailable(info1);
        TaskAvailable available2 = new TaskAvailable(info2);
        TaskAvailable available3 = new TaskAvailable(info3);
        
        taskGateway.saveTaskAvailable(available1);
        taskGateway.saveTaskAvailable(available2);
        taskGateway.saveTaskAvailable(available3);
        
        Task today1 = taskGateway.addTaskToToday(available1, Task.Priority.HIGH, null);
        Task today2 = taskGateway.addTaskToToday(available2, Task.Priority.MEDIUM, null);
        Task today3 = taskGateway.addTaskToToday(available3, Task.Priority.LOW, null);
        
        // Remove the middle task
        RemoveFromTodayInputData inputData = new RemoveFromTodayInputData(today2.getId());
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Task 2", testPresenter.lastOutputData.getTaskName());

        // Verify only one task was removed
        List<Task> remainingTasks = taskGateway.getTodaysTasks();
        assertEquals(2, remainingTasks.size());
        
        // Verify the correct tasks remain
        boolean hasTask1 = remainingTasks.stream().anyMatch(t -> t.getInfo().getName().equals("Task 1"));
        boolean hasTask3 = remainingTasks.stream().anyMatch(t -> t.getInfo().getName().equals("Task 3"));
        assertTrue(hasTask1);
        assertTrue(hasTask3);
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestRemoveFromTodayPresenter implements RemoveFromTodayOutputBoundary {
        RemoveFromTodayOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(RemoveFromTodayOutputData outputData) {
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