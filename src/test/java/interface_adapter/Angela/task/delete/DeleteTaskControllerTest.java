package interface_adapter.Angela.task.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.task.delete.DeleteTaskInputBoundary;

import static org.mockito.Mockito.*;

class DeleteTaskControllerTest {

    private DeleteTaskInputBoundary mockInteractor;
    private DeleteTaskController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(DeleteTaskInputBoundary.class);
        controller = new DeleteTaskController(mockInteractor);
    }

    @Test
    void testExecute_fromAvailableTrue_callsInteractor() {
        String taskId = "task123";
        boolean isFromAvailable = true;

        controller.execute(taskId, isFromAvailable);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == true
        ));
    }

    @Test
    void testExecute_fromAvailableFalse_callsInteractor() {
        String taskId = "task123";
        boolean isFromAvailable = false;

        controller.execute(taskId, isFromAvailable);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == false
        ));
    }

    @Test
    void testExecute_nullTaskId_callsInteractor() {
        String taskId = null;
        boolean isFromAvailable = true;

        controller.execute(taskId, isFromAvailable);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId() == null &&
                inputData.isFromAvailable() == true
        ));
    }

    @Test
    void testExecute_emptyTaskId_callsInteractor() {
        String taskId = "";
        boolean isFromAvailable = false;

        controller.execute(taskId, isFromAvailable);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == false
        ));
    }

    @Test
    void testExecute_longTaskId_callsInteractor() {
        String taskId = "very-long-task-id-123456789-abcdef";
        boolean isFromAvailable = true;

        controller.execute(taskId, isFromAvailable);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == true
        ));
    }

    @Test
    void testExecute_specialCharactersInTaskId_callsInteractor() {
        String taskId = "task@#$%123";
        boolean isFromAvailable = false;

        controller.execute(taskId, isFromAvailable);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == false
        ));
    }

    @Test
    void testConfirmDeleteFromBoth_validTaskId_callsInteractor() {
        String taskId = "task123";

        controller.confirmDeleteFromBoth(taskId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == false
        ));
    }

    @Test
    void testConfirmDeleteFromBoth_nullTaskId_callsInteractor() {
        String taskId = null;

        controller.confirmDeleteFromBoth(taskId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId() == null &&
                inputData.isFromAvailable() == false
        ));
    }

    @Test
    void testConfirmDeleteFromBoth_emptyTaskId_callsInteractor() {
        String taskId = "";

        controller.confirmDeleteFromBoth(taskId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == false
        ));
    }

    @Test
    void testConfirmDeleteFromBoth_longTaskId_callsInteractor() {
        String taskId = "very-long-task-id-123456789-abcdef";

        controller.confirmDeleteFromBoth(taskId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == false
        ));
    }

    @Test
    void testConfirmDeleteFromBoth_specialCharactersInTaskId_callsInteractor() {
        String taskId = "task@#$%123";

        controller.confirmDeleteFromBoth(taskId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isFromAvailable() == false
        ));
    }
}