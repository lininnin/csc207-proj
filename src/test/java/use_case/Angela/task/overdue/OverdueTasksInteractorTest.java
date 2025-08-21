package use_case.Angela.task.overdue;

import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryDataAccessObject;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for OverdueTasksInteractor following the template repository pattern.
 */
class OverdueTasksInteractorTest {

    private InMemoryTaskGateway taskGateway;
    private InMemoryCategoryDataAccessObject categoryDataAccess;
    private TestOverdueTasksPresenter testPresenter;
    private OverdueTasksInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskGateway();
        categoryDataAccess = new InMemoryCategoryDataAccessObject();
        testPresenter = new TestOverdueTasksPresenter();
        interactor = new OverdueTasksInteractor(taskGateway, categoryDataAccess, testPresenter);
    }

    @Test
    void testExecuteWithNoTasks() {
        // Execute with no tasks in the system
        OverdueTasksInputData inputData = new OverdueTasksInputData(30);
        interactor.execute(inputData);

        // Verify presenter received empty list
        assertNotNull(testPresenter.lastOutputData);
        assertTrue(testPresenter.lastOutputData.getOverdueTasks().isEmpty());
        assertNull(testPresenter.lastError);
    }

    @Test
    void testExecuteWithOverdueTasks() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category workCategory = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.save(workCategory);

        // Create available tasks and add them to today with past due dates
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

        // Task 1: Due yesterday
        Info info1 = new Info.Builder("Overdue Task 1")
                .description("Should be overdue")
                .category(workCategory.getId())
                .build();
        TaskAvailable template1 = new TaskAvailable(info1);
        taskGateway.saveTaskAvailable(template1);
        taskGateway.addTaskToToday(template1, Task.Priority.HIGH, yesterday);

        // Task 2: Due two days ago
        Info info2 = new Info.Builder("Overdue Task 2")
                .description("Also overdue")
                .category(workCategory.getId())
                .build();
        TaskAvailable template2 = new TaskAvailable(info2);
        taskGateway.saveTaskAvailable(template2);
        taskGateway.addTaskToToday(template2, Task.Priority.MEDIUM, twoDaysAgo);

        // Task 3: Due today (not overdue)
        Info info3 = new Info.Builder("Current Task")
                .description("Due today")
                .category(workCategory.getId())
                .build();
        TaskAvailable template3 = new TaskAvailable(info3);
        taskGateway.saveTaskAvailable(template3);
        taskGateway.addTaskToToday(template3, Task.Priority.LOW, LocalDate.now());

        // Task 4: Due tomorrow (not overdue)
        Info info4 = new Info.Builder("Future Task")
                .description("Due tomorrow")
                .category(workCategory.getId())
                .build();
        TaskAvailable template4 = new TaskAvailable(info4);
        taskGateway.saveTaskAvailable(template4);
        taskGateway.addTaskToToday(template4, null, LocalDate.now().plusDays(1));

        // Execute the interactor
        OverdueTasksInputData inputData = new OverdueTasksInputData(30);
        interactor.execute(inputData);

        // Verify only overdue tasks are returned
        assertNotNull(testPresenter.lastOutputData);
        List<OverdueTasksOutputData.OverdueTaskData> overdueTasks = testPresenter.lastOutputData.getOverdueTasks();
        assertEquals(2, overdueTasks.size());

        // Verify we have the overdue tasks (order may vary)
        boolean foundTask1 = false;
        boolean foundTask2 = false;
        
        for (OverdueTasksOutputData.OverdueTaskData task : overdueTasks) {
            if (task.getTaskName().equals("Overdue Task 1")) {
                foundTask1 = true;
                assertEquals("Should be overdue", task.getTaskDescription());
                assertEquals("Work", task.getCategoryName());
                assertEquals("HIGH", task.getTaskPriority());
            } else if (task.getTaskName().equals("Overdue Task 2")) {
                foundTask2 = true;
                assertEquals("Also overdue", task.getTaskDescription());
                assertEquals("Work", task.getCategoryName());
                assertEquals("MEDIUM", task.getTaskPriority());
            }
        }
        
        assertTrue(foundTask1, "Should find Overdue Task 1");
        assertTrue(foundTask2, "Should find Overdue Task 2");
        assertNull(testPresenter.lastError);
    }

    @Test
    void testExecuteWithCompletedOverdueTasks() {
        // Create an overdue task and mark it as completed
        Info info = new Info.Builder("Completed Overdue Task")
                .description("Should not appear")
                .build();
        TaskAvailable template = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(template);
        
        // Add to today with past due date
        Task completedTask = taskGateway.addTaskToToday(template, Task.Priority.HIGH, LocalDate.now().minusDays(1));
        
        // Mark as completed
        completedTask.markComplete();

        // Execute the interactor
        OverdueTasksInputData inputData = new OverdueTasksInputData(30);
        interactor.execute(inputData);

        // Verify completed tasks are not included
        assertNotNull(testPresenter.lastOutputData);
        assertTrue(testPresenter.lastOutputData.getOverdueTasks().isEmpty());
        assertNull(testPresenter.lastError);
    }

    @Test
    void testExecuteWithNullDueDate() {
        // Create a task with no due date
        Info info = new Info.Builder("No Due Date Task")
                .description("Should not appear")
                .build();
        TaskAvailable template = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(template);
        
        // Add to today with null due date
        taskGateway.addTaskToToday(template, null, null);

        // Execute the interactor
        OverdueTasksInputData inputData = new OverdueTasksInputData(30);
        interactor.execute(inputData);

        // Verify tasks with no due date are not included
        assertNotNull(testPresenter.lastOutputData);
        assertTrue(testPresenter.lastOutputData.getOverdueTasks().isEmpty());
        assertNull(testPresenter.lastError);
    }

    @Test
    void testExecuteWithDeletedCategory() {
        // Create a task with no category (simulating deleted category)
        Info info = new Info.Builder("Task with Deleted Category")
                .description("Category was deleted")
                .category(null) // No category
                .build();
        TaskAvailable template = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(template);
        
        // Add to today with past due date
        taskGateway.addTaskToToday(template, Task.Priority.LOW, LocalDate.now().minusDays(1));

        // Execute the interactor
        OverdueTasksInputData inputData = new OverdueTasksInputData(30);
        interactor.execute(inputData);

        // Verify task is returned with empty category name
        assertNotNull(testPresenter.lastOutputData);
        List<OverdueTasksOutputData.OverdueTaskData> overdueTasks = testPresenter.lastOutputData.getOverdueTasks();
        assertEquals(1, overdueTasks.size());

        OverdueTasksOutputData.OverdueTaskData task = overdueTasks.get(0);
        assertEquals("Task with Deleted Category", task.getTaskName());
        assertEquals("", task.getCategoryName()); // Empty string for missing category
        assertNull(testPresenter.lastError);
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestOverdueTasksPresenter implements OverdueTasksOutputBoundary {
        OverdueTasksOutputData lastOutputData;
        String lastError;

        @Override
        public void presentOverdueTasks(OverdueTasksOutputData outputData) {
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