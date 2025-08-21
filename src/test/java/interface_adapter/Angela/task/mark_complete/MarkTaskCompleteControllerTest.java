package interface_adapter.Angela.task.mark_complete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.task.mark_complete.MarkTaskCompleteInputBoundary;

import static org.mockito.Mockito.*;

class MarkTaskCompleteControllerTest {

    private MarkTaskCompleteInputBoundary mockInteractor;
    private MarkTaskCompleteController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(MarkTaskCompleteInputBoundary.class);
        controller = new MarkTaskCompleteController(mockInteractor);
    }

    @Test
    void testExecute_markAsCompleteTrue_callsInteractor() {
        String taskId = "task123";
        boolean markAsComplete = true;

        controller.execute(taskId, markAsComplete);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isMarkAsComplete() == true
        ));
    }

    @Test
    void testExecute_markAsCompleteFalse_callsInteractor() {
        String taskId = "task123";
        boolean markAsComplete = false;

        controller.execute(taskId, markAsComplete);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isMarkAsComplete() == false
        ));
    }

    @Test
    void testExecute_nullTaskId_callsInteractor() {
        String taskId = null;
        boolean markAsComplete = true;

        controller.execute(taskId, markAsComplete);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId() == null &&
                inputData.isMarkAsComplete() == true
        ));
    }

    @Test
    void testExecute_emptyTaskId_callsInteractor() {
        String taskId = "";
        boolean markAsComplete = false;

        controller.execute(taskId, markAsComplete);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isMarkAsComplete() == false
        ));
    }

    @Test
    void testExecute_longTaskId_callsInteractor() {
        String taskId = "very-long-task-id-123456789-abcdef";
        boolean markAsComplete = true;

        controller.execute(taskId, markAsComplete);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isMarkAsComplete() == true
        ));
    }

    @Test
    void testExecute_specialCharactersInTaskId_callsInteractor() {
        String taskId = "task@#$%123";
        boolean markAsComplete = false;

        controller.execute(taskId, markAsComplete);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isMarkAsComplete() == false
        ));
    }

    @Test
    void testExecute_multipleCallsWithDifferentValues_callsInteractor() {
        String taskId1 = "task1";
        String taskId2 = "task2";

        controller.execute(taskId1, true);
        controller.execute(taskId2, false);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId1) &&
                inputData.isMarkAsComplete() == true
        ));
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId2) &&
                inputData.isMarkAsComplete() == false
        ));
    }

    @Test
    void testExecute_sameTaskIdDifferentCompletionStates_callsInteractor() {
        String taskId = "task123";

        // Mark as complete
        controller.execute(taskId, true);
        
        // Mark as incomplete
        controller.execute(taskId, false);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isMarkAsComplete() == true
        ));
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.isMarkAsComplete() == false
        ));
    }
}