package use_case.Angela.task.create;

import data_access.InMemoryTaskDataAccessObject;
import data_access.InMemoryCategoryGateway;
import entity.info.Info;
import entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreateTaskInteractor following Clean Architecture principles.
 */
class CreateTaskInteractorTest {

    private InMemoryTaskDataAccessObject taskGateway;
    private InMemoryCategoryGateway categoryGateway;
    private TestCreateTaskPresenter testPresenter;
    private CreateTaskInteractor interactor;

    @BeforeEach
    void setUp() {
        taskGateway = new InMemoryTaskDataAccessObject();
        categoryGateway = new InMemoryCategoryGateway();
        testPresenter = new TestCreateTaskPresenter();
        interactor = new CreateTaskInteractor(taskGateway, categoryGateway, testPresenter);
    }

    @Test
    void testSuccessfulTaskCreation() {
        // Create a category first
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryGateway.save(category);

        // Create input data for a new task
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Study Java",
                "Review Clean Architecture",
                categoryId,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Study Java", testPresenter.lastOutputData.getTaskName());
        assertNull(testPresenter.lastError);

        // Verify task was saved
        List<Info> tasks = taskGateway.getAllAvailableTasks();
        assertEquals(1, tasks.size());
        Info savedTask = tasks.get(0);
        assertEquals("Study Java", savedTask.getName());
        assertEquals("Review Clean Architecture", savedTask.getDescription());
        assertEquals(categoryId, savedTask.getCategory());
    }

    @Test
    void testCreateOneTimeTask() {
        // Create input data for a one-time task
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Doctor Appointment",
                "Annual checkup",
                null,
                true
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Doctor Appointment", testPresenter.lastOutputData.getTaskName());

        // Verify task was created (we can't check isOneTime on Info)
        assertEquals(1, taskGateway.getAllAvailableTasks().size());
    }

    @Test
    void testCreateTaskWithEmptyDescription() {
        // Create task with no description
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Quick Task",
                "",
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        Info savedTask = taskGateway.getAllAvailableTasks().get(0);
        // Empty descriptions are not set on Info, so they become null
        assertNull(savedTask.getDescription());
    }

    @Test
    void testCreateTaskWithNullDescription() {
        // Create task with null description
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Another Task",
                null,
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        Info savedTask = taskGateway.getAllAvailableTasks().get(0);
        assertNull(savedTask.getDescription());
    }

    @Test
    void testFailureDuplicateTaskName() {
        // Create first task
        CreateTaskInputData inputData1 = new CreateTaskInputData(
                "Duplicate Task",
                "First instance",
                null,
                false
        );
        interactor.execute(inputData1);

        // Reset presenter
        testPresenter = new TestCreateTaskPresenter();
        interactor = new CreateTaskInteractor(taskGateway, categoryGateway, testPresenter);

        // Try to create task with same name and category (case-insensitive check)
        CreateTaskInputData inputData2 = new CreateTaskInputData(
                "duplicate task",  // Different case
                "Second instance",
                null,  // Same category (null)
                false
        );
        interactor.execute(inputData2);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("A task with this name and category already exists", testPresenter.lastError);

        // Verify only one task exists
        assertEquals(1, taskGateway.getAllAvailableTasks().size());
    }

    @Test
    void testFailureNameExceedsMaxLength() {
        // Create task with name exceeding 20 characters
        CreateTaskInputData inputData = new CreateTaskInputData(
                "This task name is definitely too long",
                "Description",
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The name of Task cannot exceed 20 letters", testPresenter.lastError);

        // Verify no task was created
        assertTrue(taskGateway.getAllAvailableTasks().isEmpty());
    }

    @Test
    void testFailureDescriptionExceedsMaxLength() {
        // Create task with description exceeding 100 characters
        String longDescription = "This is a very long description that exceeds the maximum allowed " +
                "length of 100 characters. It should trigger a validation error.";
        
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Valid Name",
                longDescription,
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Description cannot exceed 100 characters", testPresenter.lastError);

        // Verify no task was created
        assertTrue(taskGateway.getAllAvailableTasks().isEmpty());
    }

    @Test
    void testFailureEmptyTaskName() {
        // Try to create task with empty name
        CreateTaskInputData inputData = new CreateTaskInputData(
                "",
                "Description",
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task name cannot be empty", testPresenter.lastError);

        // Verify no task was created
        assertTrue(taskGateway.getAllAvailableTasks().isEmpty());
    }

    @Test
    void testFailureNullTaskName() {
        // Try to create task with null name
        CreateTaskInputData inputData = new CreateTaskInputData(
                null,
                "Description",
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Task name cannot be empty", testPresenter.lastError);

        // Verify no task was created
        assertTrue(taskGateway.getAllAvailableTasks().isEmpty());
    }

    @Test
    void testBoundaryExactly20Characters() {
        // Test boundary case - exactly 20 characters
        CreateTaskInputData inputData = new CreateTaskInputData(
                "12345678901234567890",  // Exactly 20 characters
                "Description",
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals(1, taskGateway.getAllAvailableTasks().size());
    }

    @Test
    void testBoundaryExactly100CharacterDescription() {
        // Test boundary case - exactly 100 characters in description
        String description = "1234567890".repeat(10);  // Exactly 100 characters
        
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Task Name",
                description,
                null,
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals(1, taskGateway.getAllAvailableTasks().size());
    }

    @Test
    void testCreateTaskWithInvalidCategory() {
        // Try to create task with non-existent category
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Task with Bad Cat",
                "Description",
                "non-existent-category-id",
                false
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Invalid category selected", testPresenter.lastError);

        // Verify no task was created
        assertTrue(taskGateway.getAllAvailableTasks().isEmpty());
    }

    @Test
    void testCreateTasksWithSameNameDifferentCategories() {
        // Create two categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        Category category1 = new Category(categoryId1, "Work", "#0000FF");
        Category category2 = new Category(categoryId2, "Personal", "#00FF00");
        categoryGateway.save(category1);
        categoryGateway.save(category2);

        // Create first task with category1
        CreateTaskInputData inputData1 = new CreateTaskInputData(
                "Same Name",
                "First task",
                categoryId1,
                false
        );
        interactor.execute(inputData1);

        // Reset presenter
        testPresenter = new TestCreateTaskPresenter();
        interactor = new CreateTaskInteractor(taskGateway, categoryGateway, testPresenter);

        // Create second task with same name but different category
        CreateTaskInputData inputData2 = new CreateTaskInputData(
                "Same Name",
                "Second task",
                categoryId2,
                false
        );
        interactor.execute(inputData2);

        // Both should succeed since they have different categories
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertEquals(2, taskGateway.getAllAvailableTasks().size());
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestCreateTaskPresenter implements CreateTaskOutputBoundary {
        CreateTaskOutputData lastOutputData;
        String lastError;

        @Override
        public void presentSuccess(CreateTaskOutputData outputData) {
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