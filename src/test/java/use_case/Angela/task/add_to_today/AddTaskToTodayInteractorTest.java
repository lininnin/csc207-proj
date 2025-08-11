package use_case.Angela.task.add_to_today;

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

    @Test
    void testIsTestingOverdueAllowsReAddingTaskWithPastDate() {
        // Create an available task
        Info info = new Info.Builder("Test Task")
                .description("Test description")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // First add the task normally with a future date
        LocalDate futureDate = LocalDate.now().plusDays(3);
        AddTaskToTodayInputData normalInput = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.HIGH,
                futureDate
        );
        interactor.execute(normalInput);
        
        // Verify task was added
        assertEquals(1, taskGateway.getTodaysTasks().size());
        Task firstTask = taskGateway.getTodaysTasks().get(0);
        assertFalse(firstTask.isOverdue());
        
        // Reset presenter for next test
        testPresenter = new TestAddTaskToTodayPresenter();
        interactor = new AddTaskToTodayInteractor(taskGateway, testPresenter);
        
        // Now try to add the same task with yesterday's date using isTestingOverdue flag
        LocalDate yesterday = LocalDate.now().minusDays(1);
        AddTaskToTodayInputData testingInput = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.MEDIUM,
                yesterday,
                true  // isTestingOverdue = true
        );
        interactor.execute(testingInput);
        
        // Verify success (no error)
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        
        // The task should be updated or re-added with overdue status
        List<Task> todaysTasks = taskGateway.getTodaysTasks();
        assertTrue(todaysTasks.size() >= 1);
        
        // Find the task with the overdue date
        boolean foundOverdueTask = todaysTasks.stream()
                .anyMatch(task -> task.getDates().getDueDate() != null &&
                                task.getDates().getDueDate().equals(yesterday) &&
                                task.isOverdue());
        assertTrue(foundOverdueTask, "Should have an overdue task with yesterday's date");
    }

    @Test
    void testNormalAddFailsWhenTaskAlreadyInTodayAndNotOverdue() {
        // Create an available task
        Info info = new Info.Builder("Already Added Task")
                .description("Already in today")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // Add it to today's list with a future date
        LocalDate futureDate = LocalDate.now().plusDays(2);
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.MEDIUM,
                futureDate
        );
        interactor.execute(inputData);
        
        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals(1, taskGateway.getTodaysTasks().size());
        
        // Reset presenter
        testPresenter = new TestAddTaskToTodayPresenter();
        interactor = new AddTaskToTodayInteractor(taskGateway, testPresenter);

        // Try to add the same task again without isTestingOverdue flag
        AddTaskToTodayInputData duplicateInput = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.HIGH,
                LocalDate.now().plusDays(5)
        );
        interactor.execute(duplicateInput);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task is already in Today's Tasks", testPresenter.lastError);
        
        // Verify still only one task in today's list
        assertEquals(1, taskGateway.getTodaysTasks().size());
    }

    @Test
    void testCanReAddOverdueTaskWithNewDueDate() {
        // Create an available task
        Info info = new Info.Builder("Overdue Task")
                .description("Will become overdue")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // First add the task with a past date (making it overdue)
        LocalDate pastDate = LocalDate.now().minusDays(3);
        AddTaskToTodayInputData overdueInput = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.LOW,
                pastDate,
                true  // Use testing flag to allow past date
        );
        interactor.execute(overdueInput);
        
        // Verify task was added and is overdue
        assertEquals(1, taskGateway.getTodaysTasks().size());
        Task overdueTask = taskGateway.getTodaysTasks().get(0);
        assertTrue(overdueTask.isOverdue());
        assertEquals(pastDate, overdueTask.getDates().getDueDate());
        
        // Reset presenter
        testPresenter = new TestAddTaskToTodayPresenter();
        interactor = new AddTaskToTodayInteractor(taskGateway, testPresenter);
        
        // Now try to add the same task again with a new future date
        // This should work because the existing task is overdue
        LocalDate newFutureDate = LocalDate.now().plusDays(7);
        AddTaskToTodayInputData newInput = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.HIGH,
                newFutureDate
        );
        interactor.execute(newInput);
        
        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        
        // Should now have 2 instances of the task (one overdue, one not)
        assertEquals(2, taskGateway.getTodaysTasks().size());
        
        // Verify we have both overdue and non-overdue versions
        List<Task> tasks = taskGateway.getTodaysTasks();
        boolean hasOverdueVersion = tasks.stream().anyMatch(Task::isOverdue);
        boolean hasNonOverdueVersion = tasks.stream().anyMatch(t -> !t.isOverdue());
        assertTrue(hasOverdueVersion, "Should still have the overdue version");
        assertTrue(hasNonOverdueVersion, "Should have the new non-overdue version");
    }

    @Test
    void testIsTaskInTodaysListAndNotOverdueMethod() {
        // Create an available task
        Info info = new Info.Builder("Test Task")
                .description("For testing method")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);
        
        // Initially, task should not be in today's list
        assertFalse(taskGateway.isTaskInTodaysListAndNotOverdue(availableTask.getId()));
        
        // Add task with future date (not overdue)
        LocalDate futureDate = LocalDate.now().plusDays(2);
        Task todayTask = taskGateway.addTaskToToday(availableTask, Task.Priority.MEDIUM, futureDate);
        
        // Now it should be in today's list and not overdue
        assertTrue(taskGateway.isTaskInTodaysListAndNotOverdue(availableTask.getId()));
        
        // Manually update the task to be overdue by changing its due date
        // Create a new task with past date to simulate overdue
        taskGateway.getTodaysTasks().clear();
        LocalDate pastDate = LocalDate.now().minusDays(1);
        Task overdueTask = taskGateway.addTaskToToday(availableTask, Task.Priority.HIGH, pastDate);
        
        // Now it should be in today's list but overdue, so method should return false
        assertFalse(taskGateway.isTaskInTodaysListAndNotOverdue(availableTask.getId()));
    }

    @Test
    void testIsTestingOverdueFlagBypassesValidation() {
        // Create an available task
        Info info = new Info.Builder("Bypass Test")
                .description("Testing validation bypass")
                .build();
        TaskAvailable availableTask = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(availableTask);

        // Add task normally first
        LocalDate normalDate = LocalDate.now().plusDays(1);
        AddTaskToTodayInputData normalInput = new AddTaskToTodayInputData(
                availableTask.getId(),
                Task.Priority.LOW,
                normalDate
        );
        interactor.execute(normalInput);
        
        // Verify task was added
        assertEquals(1, taskGateway.getTodaysTasks().size());
        
        // Reset presenter
        testPresenter = new TestAddTaskToTodayPresenter();
        interactor = new AddTaskToTodayInteractor(taskGateway, testPresenter);
        
        // Try to add again with testing flag - should succeed despite already being present
        LocalDate testDate = LocalDate.now().minusDays(2);
        AddTaskToTodayInputData testInput = new AddTaskToTodayInputData(
                availableTask.getId(),
                null,  // No priority
                testDate,
                true  // isTestingOverdue = true bypasses validation
        );
        interactor.execute(testInput);
        
        // Should succeed even though task is already in today's list
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        
        // Should have added another instance
        assertEquals(2, taskGateway.getTodaysTasks().size());
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