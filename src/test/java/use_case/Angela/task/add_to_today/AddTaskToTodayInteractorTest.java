package use_case.Angela.task.add_to_today;

import data_access.InMemoryTaskGateway;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AddTaskToTodayInteractor, including overdue task scenarios.
 */
class AddTaskToTodayInteractorTest {

    private InMemoryTaskGateway taskGateway;
    private TestAddTaskToTodayPresenter testPresenter;
    private AddTaskToTodayInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskGateway();
        testPresenter = new TestAddTaskToTodayPresenter();
        interactor = new AddTaskToTodayInteractor(taskGateway, testPresenter);
    }

    @Test
    void testSuccessfulAddWithFutureDueDate() {
        // Create an available task
        Info info = new Info.Builder("Test Task")
                .description("Test description")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // Create input data with future due date
        LocalDate futureDueDate = LocalDate.now().plusDays(3);
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.HIGH,
                futureDueDate
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Test Task", testPresenter.lastOutputData.getTaskName());
        assertNull(testPresenter.lastError);

        // Verify task was added to today's list
        assertEquals(1, taskGateway.getTodaysTasks().size());
        Task addedTask = taskGateway.getTodaysTasks().get(0);
        assertEquals("Test Task", addedTask.getInfo().getName());
        assertEquals(Task.Priority.HIGH, addedTask.getPriority());
        assertEquals(futureDueDate, addedTask.getDates().getDueDate());
        assertFalse(addedTask.isOverdue());
    }

    @Test
    void testSuccessfulAddWithPastDueDate() {
        // This tests the "Test: Add with Yesterday" functionality
        // Create an available task
        Info info = new Info.Builder("Overdue Test Task")
                .description("Will be overdue")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // Create input data with past due date (yesterday)
        LocalDate yesterday = LocalDate.now().minusDays(1);
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.MEDIUM,
                yesterday
        );

        // Execute the interactor (should allow past dates for testing)
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Overdue Test Task", testPresenter.lastOutputData.getTaskName());
        assertNull(testPresenter.lastError);

        // Verify task was added and is marked as overdue
        assertEquals(1, taskGateway.getTodaysTasks().size());
        Task addedTask = taskGateway.getTodaysTasks().get(0);
        assertEquals("Overdue Test Task", addedTask.getInfo().getName());
        assertEquals(yesterday, addedTask.getDates().getDueDate());
        assertTrue(addedTask.isOverdue());
    }

    @Test
    void testSuccessfulAddWithTodayDueDate() {
        // Create an available task
        Info info = new Info.Builder("Today's Task")
                .description("Due today")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // Create input data with today as due date
        LocalDate today = LocalDate.now();
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.LOW,
                today
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify task is not overdue (due today)
        Task addedTask = taskGateway.getTodaysTasks().get(0);
        assertEquals(today, addedTask.getDates().getDueDate());
        assertFalse(addedTask.isOverdue());
    }

    @Test
    void testAddWithNullDueDate() {
        // Create an available task
        Info info = new Info.Builder("No Due Date Task")
                .description("No due date")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // Create input data with null due date
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                availableTask.getId(),
                null,
                null
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);

        // Verify task has no due date and is not overdue
        Task addedTask = taskGateway.getTodaysTasks().get(0);
        assertNull(addedTask.getDates().getDueDate());
        assertFalse(addedTask.isOverdue());
    }

    @Test
    void testFailureTaskNotFound() {
        // Try to add a non-existent task
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                "non-existent-id",
                Task.Priority.HIGH,
                LocalDate.now().plusDays(1)
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in Available Tasks", testPresenter.lastError);

        // Verify no task was added
        assertTrue(taskGateway.getTodaysTasks().isEmpty());
    }

    @Test
    void testFailureTaskAlreadyInToday() {
        // Create an available task
        Info info = new Info.Builder("Already Added Task")
                .description("Already in today")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // Add it to today's list first
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.MEDIUM,
                LocalDate.now().plusDays(1)
        );
        interactor.execute(inputData);

        // Reset presenter
        testPresenter = new TestAddTaskToTodayPresenter();
        interactor = new AddTaskToTodayInteractor(taskGateway, testPresenter);

        // Try to add the same task again
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task is already in Today's Tasks", testPresenter.lastError);

        // Verify only one task in today's list
        assertEquals(1, taskGateway.getTodaysTasks().size());
    }

    @Test
    void testOneTimeTaskBehavior() {
        // Create a one-time available task using the constructor that takes parameters
        Info info = new Info.Builder("One-Time Task")
                .description("One-time task test")
                .build();
        TaskAvailable oneTimeTask = new TaskAvailable(
                info.getId(), // Use Info's ID
                info,
                null, // plannedDueDate
                true  // isOneTime = true
        );
        taskGateway.saveTaskAvailable(oneTimeTask);

        // Verify task is in available list
        assertEquals(1, taskGateway.getAllAvailableTasks().size());

        // Add to today's list
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                oneTimeTask.getId(),
                Task.Priority.HIGH,
                LocalDate.now().plusDays(1)
        );
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);

        // Note: One-time task removal is not yet implemented in the current version
        // This test just verifies the task is added successfully
        // Future implementation should remove one-time tasks from available list

        // Verify task was added to today's list
        assertEquals(1, taskGateway.getTodaysTasks().size());
        
        // Verify the task in today's list is marked as one-time
        Task addedTask = taskGateway.getTodaysTasks().get(0);
        assertTrue(addedTask.isOneTime());
    }

    @Test
    void testRecurringTaskNotRemovedAfterAdding() {
        // Create a recurring available task using the constructor that takes parameters
        Info info = new Info.Builder("Recurring Task")
                .description("Should remain in available")
                .build();
        TaskAvailable recurringTask = new TaskAvailable(
                info.getId(), // Use Info's ID
                info,
                null, // plannedDueDate
                false // isOneTime = false (recurring)
        );
        taskGateway.saveTaskAvailable(recurringTask);

        // Add to today's list
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                recurringTask.getId(),
                Task.Priority.LOW,
                LocalDate.now().plusDays(2)
        );
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);

        // Verify recurring task remains in available list
        assertEquals(1, taskGateway.getAllAvailableTasks().size());

        // Verify task was added to today's list
        assertEquals(1, taskGateway.getTodaysTasks().size());
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestAddTaskToTodayPresenter implements AddTaskToTodayOutputBoundary {
        AddTaskToTodayOutputData lastOutputData;
        String lastError;

        @Override
        public void presentSuccess(AddTaskToTodayOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }

        @Override
        public void presentError(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }
    }
}