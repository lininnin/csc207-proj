package use_case.Angela.task.edit_today;

import data_access.InMemoryTaskDataAccessObject;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditTodayTaskInteractor following Clean Architecture principles.
 */
class EditTodayTaskInteractorTest {

    private InMemoryTaskDataAccessObject taskGateway;
    private TestEditTodayTaskPresenter testPresenter;
    private EditTodayTaskInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskDataAccessObject();
        testPresenter = new TestEditTodayTaskPresenter();
        interactor = new EditTodayTaskInteractor(taskGateway, testPresenter);
    }

    @Test
    void testSuccessfulEditPriority() {
        // Create task in today's list
        Info info = new Info.Builder("Edit Priority Task")
                .description("Test priority change")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.LOW, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        // Edit priority
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                Task.Priority.HIGH,
                LocalDate.now().plusDays(1)
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(taskId, testPresenter.lastOutputData.getTaskId());
        assertEquals("Edit Priority Task", testPresenter.lastOutputData.getTaskName());
        assertNull(testPresenter.lastError);

        // Verify priority was updated
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertEquals(Task.Priority.HIGH, updatedTask.getPriority());
    }

    @Test
    void testSuccessfulEditDueDate() {
        // Create task in today's list
        Info info = new Info.Builder("Edit Due Date Task")
                .description("Test due date change")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        LocalDate originalDueDate = LocalDate.now().plusDays(3);
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.MEDIUM, originalDueDate);
        String taskId = todayTask.getId();

        // Edit due date
        LocalDate newDueDate = LocalDate.now().plusDays(7);
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                Task.Priority.MEDIUM,
                newDueDate
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify due date was updated
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertEquals(newDueDate, updatedTask.getDates().getDueDate());
        assertFalse(updatedTask.isOverdue());
    }

    @Test
    void testEditToOverdueDate() {
        // Create task in today's list
        Info info = new Info.Builder("Make Overdue Task")
                .description("Will be overdue")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, null, LocalDate.now().plusDays(2));
        String taskId = todayTask.getId();
        assertFalse(todayTask.isOverdue());

        // Try to edit to past due date (should fail - business rule prevents setting past dates)
        LocalDate yesterday = LocalDate.now().minusDays(1);
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                Task.Priority.HIGH,
                yesterday
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure - cannot set due date to past
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Due date cannot be before today", testPresenter.lastError);

        // Task should remain unchanged
        Task unchangedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertEquals(LocalDate.now().plusDays(2), unchangedTask.getDates().getDueDate());
        assertFalse(unchangedTask.isOverdue());
    }

    @Test
    void testEditFromOverdueToFutureDate() {
        // Create an overdue task
        Info info = new Info.Builder("Fix Overdue Task")
                .description("No longer overdue")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        LocalDate pastDate = LocalDate.now().minusDays(2);
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.LOW, pastDate);
        String taskId = todayTask.getId();
        assertTrue(todayTask.isOverdue());

        // Edit to future date
        LocalDate futureDate = LocalDate.now().plusDays(3);
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                Task.Priority.MEDIUM,
                futureDate
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify task is no longer overdue
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertEquals(futureDate, updatedTask.getDates().getDueDate());
        assertFalse(updatedTask.isOverdue());
    }

    @Test
    void testEditToNullDueDate() {
        // Create task with due date
        Info info = new Info.Builder("Remove Due Date Task")
                .description("Will have no due date")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        // Remove due date
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                Task.Priority.HIGH,
                null
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify due date was removed
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertNull(updatedTask.getDates().getDueDate());
        assertFalse(updatedTask.isOverdue());
    }

    @Test
    void testEditToNullPriority() {
        // Create task with priority
        Info info = new Info.Builder("Remove Priority Task")
                .description("Will have no priority")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, LocalDate.now());
        String taskId = todayTask.getId();

        // Remove priority
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                null,
                LocalDate.now()
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify priority was removed
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertNull(updatedTask.getPriority());
    }

    @Test
    void testEditBothPriorityAndDueDate() {
        // Create task
        Info info = new Info.Builder("Edit Both Fields")
                .description("Change priority and due date")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.LOW, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        // Edit both fields
        LocalDate newDueDate = LocalDate.now().plusDays(5);
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                Task.Priority.HIGH,
                newDueDate
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify both fields were updated
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertEquals(Task.Priority.HIGH, updatedTask.getPriority());
        assertEquals(newDueDate, updatedTask.getDates().getDueDate());
    }

    @Test
    void testEditCompletedTask() {
        // Create completed task
        Info info = new Info.Builder("Completed Task")
                .description("Already done")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, Task.Priority.MEDIUM, LocalDate.now());
        todayTask.markComplete();
        String taskId = todayTask.getId();

        // Edit completed task
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                Task.Priority.HIGH,
                LocalDate.now().plusDays(2)
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success - completed tasks can be edited
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify task remains completed but fields are updated
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertTrue(updatedTask.isCompleted());
        assertEquals(Task.Priority.HIGH, updatedTask.getPriority());
    }

    @Test
    void testFailureTaskNotFound() {
        // Try to edit non-existent task
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                "non-existent-id",
                Task.Priority.HIGH,
                LocalDate.now()
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in today's list", testPresenter.lastError);
    }

    @Test
    void testNoChangesEdit() {
        // Create task
        Info info = new Info.Builder("No Changes Task")
                .description("Nothing will change")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        Task.Priority originalPriority = Task.Priority.MEDIUM;
        LocalDate originalDueDate = LocalDate.now().plusDays(3);
        Task todayTask = (Task) taskGateway.addTaskToToday(availableTask, originalPriority, originalDueDate);
        String taskId = todayTask.getId();

        // Edit with same values
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(
                taskId,
                originalPriority,
                originalDueDate
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success (idempotent operation)
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify nothing changed
        Task updatedTask = (Task) taskGateway.getTodayTaskById(taskId);
        assertEquals(originalPriority, updatedTask.getPriority());
        assertEquals(originalDueDate, updatedTask.getDates().getDueDate());
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestEditTodayTaskPresenter implements EditTodayTaskOutputBoundary {
        EditTodayTaskOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(EditTodayTaskOutputData outputData) {
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