package interface_adapter.Angela.task.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.task.create.CreateTaskInputBoundary;

import static org.mockito.Mockito.*;

class CreateTaskControllerTest {

    private CreateTaskInputBoundary mockInteractor;
    private CreateTaskController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(CreateTaskInputBoundary.class);
        controller = new CreateTaskController(mockInteractor);
    }

    @Test
    void testExecute_validParameters_callsInteractor() {
        String taskName = "Complete project";
        String description = "Finish the task management feature";
        String categoryId = "cat123";
        boolean isOneTime = true;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_nullTaskName_callsInteractor() {
        String taskName = null;
        String description = "Description";
        String categoryId = "cat123";
        boolean isOneTime = false;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName() == null &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_emptyTaskName_callsInteractor() {
        String taskName = "";
        String description = "Description";
        String categoryId = "cat123";
        boolean isOneTime = false;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_nullDescription_callsInteractor() {
        String taskName = "Task";
        String description = null;
        String categoryId = "cat123";
        boolean isOneTime = true;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription() == null &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_emptyDescription_callsInteractor() {
        String taskName = "Task";
        String description = "";
        String categoryId = "cat123";
        boolean isOneTime = true;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_nullCategoryId_callsInteractor() {
        String taskName = "Task";
        String description = "Description";
        String categoryId = null;
        boolean isOneTime = false;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId() == null &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_emptyCategoryId_callsInteractor() {
        String taskName = "Task";
        String description = "Description";
        String categoryId = "";
        boolean isOneTime = false;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_oneTimeTrue_callsInteractor() {
        String taskName = "One-time task";
        String description = "This is a one-time task";
        String categoryId = "cat123";
        boolean isOneTime = true;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == true
        ));
    }

    @Test
    void testExecute_oneTimeFalse_callsInteractor() {
        String taskName = "Recurring task";
        String description = "This is a recurring task";
        String categoryId = "cat123";
        boolean isOneTime = false;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == false
        ));
    }

    @Test
    void testExecute_specialCharacters_callsInteractor() {
        String taskName = "Task@#$%";
        String description = "Description with special chars!@#";
        String categoryId = "cat@123";
        boolean isOneTime = true;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }

    @Test
    void testExecute_longStrings_callsInteractor() {
        String taskName = "Very long task name that exceeds normal length";
        String description = "Very long description that exceeds normal length and contains many words and details";
        String categoryId = "very-long-category-id-123456789";
        boolean isOneTime = false;

        controller.execute(taskName, description, categoryId, isOneTime);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskName().equals(taskName) &&
                inputData.getDescription().equals(description) &&
                inputData.getCategoryId().equals(categoryId) &&
                inputData.isOneTime() == isOneTime
        ));
    }
}