package use_case.Angela.task.edit_available;

import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryGateway;
import entity.Angela.Task.TaskAvailable;
import entity.Category;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditAvailableTaskInteractor following Clean Architecture principles.
 */
class EditAvailableTaskInteractorTest {

    private InMemoryTaskGateway taskGateway;
    private InMemoryCategoryGateway categoryGateway;
    private TestEditAvailableTaskPresenter testPresenter;
    private EditAvailableTaskInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskGateway();
        categoryGateway = new InMemoryCategoryGateway();
        testPresenter = new TestEditAvailableTaskPresenter();
        interactor = new EditAvailableTaskInteractor(taskGateway, categoryGateway, testPresenter);
    }

    @Test
    void testSuccessfulEdit() {
        // Create a task to edit
        Info info = new Info.Builder("Original Task")
                .description("Original Description")
                .build();
        TaskAvailable task = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Create a category for the edit
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryGateway.save(category);

        // Edit the task
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "Updated Task",
                "Updated Description",
                categoryId,
                true  // isOneTime
        );

        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(taskId, testPresenter.lastOutputData.getTaskId());
        assertEquals("Updated Task", testPresenter.lastOutputData.getTaskName());
        assertNull(testPresenter.lastError);

        // Verify task was updated
        List<Info> tasks = taskGateway.getAllAvailableTasks();
        assertEquals(1, tasks.size());
        Info updatedTask = tasks.get(0);
        assertEquals("Updated Task", updatedTask.getName());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(categoryId, updatedTask.getCategory());
    }

    @Test
    void testEditToEmptyDescription() {
        // Create a task with description
        Info info = new Info.Builder("Task with Desc")
                .description("Has Description")
                .build();
        TaskAvailable task = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Edit to empty description
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "Task with Desc",
                "",  // Empty description
                null,
                false
        );

        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        Info updatedTask = taskGateway.getAllAvailableTasks().get(0);
        // Empty descriptions remain empty
        assertEquals("", updatedTask.getDescription());
    }

    @Test
    void testEditToRemoveCategory() {
        // Create task with category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryGateway.save(category);

        Info info = new Info.Builder("Task with Category")
                .category(categoryId)
                .build();
        TaskAvailable task = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Edit to remove category
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "Task without Category",
                null,
                "",  // Empty category
                false
        );

        interactor.execute(inputData);

        // Verify failure - empty category not allowed
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
    }

    @Test
    void testEditOneTimeFlag() {
        // Create recurring task
        Info info = new Info.Builder("Recurring Task").build();
        TaskAvailable task = new TaskAvailable(info);
        task.setOneTime(false);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Edit to make it one-time
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "One-time Task",
                null,
                null,
                true  // Make it one-time
        );

        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("One-time Task", testPresenter.lastOutputData.getTaskName());
    }

    @Test
    void testFailureTaskNotFound() {
        // Try to edit non-existent task
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                "non-existent-id",
                "Updated Name",
                "Description",
                null,
                false
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task not found in Available Tasks", testPresenter.lastError);
    }

    @Test
    void testFailureEmptyTaskName() {
        // Create a task
        Info info = new Info.Builder("Valid Task").build();
        TaskAvailable task = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Try to edit with empty name
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "",  // Empty name
                "Description",
                null,
                false
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task name cannot be empty", testPresenter.lastError);
    }

    @Test
    void testFailureNameExceedsMaxLength() {
        // Create a task
        Info info = new Info.Builder("Valid Task").build();
        TaskAvailable task = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Try to edit with long name
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "This task name is definitely too long",
                "Description",
                null,
                false
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The name of Task cannot exceed 20 letters", testPresenter.lastError);
    }

    @Test
    void testFailureDescriptionExceedsMaxLength() {
        // Create a task
        Info info = new Info.Builder("Valid Task").build();
        TaskAvailable task = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Try to edit with long description
        String longDescription = "This is a very long description that exceeds the maximum allowed " +
                "length of 100 characters. It should trigger a validation error.";
        
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "Valid Name",
                longDescription,
                null,
                false
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Description cannot exceed 100 characters", testPresenter.lastError);
    }

    @Test
    void testFailureInvalidCategory() {
        // Create a task
        Info info = new Info.Builder("Valid Task").build();
        TaskAvailable task = new TaskAvailable(info);
        taskGateway.saveTaskAvailable(task);
        String taskId = task.getId();

        // Try to edit with invalid category
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                taskId,
                "Valid Name",
                "Description",
                "non-existent-category-id",
                false
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Invalid category selected", testPresenter.lastError);
    }

    @Test
    void testFailureDuplicateName() {
        // Create two tasks
        Info info1 = new Info.Builder("Task One").build();
        TaskAvailable task1 = new TaskAvailable(info1);
        taskGateway.saveTaskAvailable(task1);

        Info info2 = new Info.Builder("Task Two").build();
        TaskAvailable task2 = new TaskAvailable(info2);
        taskGateway.saveTaskAvailable(task2);
        String task2Id = task2.getId();

        // Try to rename task2 to task1's name
        EditAvailableTaskInputData inputData = new EditAvailableTaskInputData(
                task2Id,
                "Task One",  // Duplicate name
                "Description",
                null,
                false
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("A task with this name already exists in the same category", testPresenter.lastError);
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestEditAvailableTaskPresenter implements EditAvailableTaskOutputBoundary {
        EditAvailableTaskOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(EditAvailableTaskOutputData outputData) {
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