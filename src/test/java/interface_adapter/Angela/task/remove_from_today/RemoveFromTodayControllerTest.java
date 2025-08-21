package interface_adapter.Angela.task.remove_from_today;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.Angela.task.remove_from_today.RemoveFromTodayInputBoundary;
import use_case.Angela.task.remove_from_today.RemoveFromTodayInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for RemoveFromTodayController.
 */
class RemoveFromTodayControllerTest {

    @Mock
    private RemoveFromTodayInputBoundary mockInteractor;

    private RemoveFromTodayController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new RemoveFromTodayController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithValidTaskId() {
        // Arrange
        String taskId = "task-123";

        // Act
        controller.execute(taskId);

        // Assert
        ArgumentCaptor<RemoveFromTodayInputData> captor = ArgumentCaptor.forClass(RemoveFromTodayInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        RemoveFromTodayInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
    }

    @Test
    void testExecuteWithNullTaskId() {
        // Arrange
        String taskId = null;

        // Act
        controller.execute(taskId);

        // Assert
        ArgumentCaptor<RemoveFromTodayInputData> captor = ArgumentCaptor.forClass(RemoveFromTodayInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        RemoveFromTodayInputData capturedData = captor.getValue();
        assertNull(capturedData.getTaskId());
    }

    @Test
    void testExecuteWithEmptyTaskId() {
        // Arrange
        String taskId = "";

        // Act
        controller.execute(taskId);

        // Assert
        ArgumentCaptor<RemoveFromTodayInputData> captor = ArgumentCaptor.forClass(RemoveFromTodayInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        RemoveFromTodayInputData capturedData = captor.getValue();
        assertEquals("", capturedData.getTaskId());
    }

    @Test
    void testExecuteWithWhitespaceTaskId() {
        // Arrange
        String taskId = "   ";

        // Act
        controller.execute(taskId);

        // Assert
        ArgumentCaptor<RemoveFromTodayInputData> captor = ArgumentCaptor.forClass(RemoveFromTodayInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        RemoveFromTodayInputData capturedData = captor.getValue();
        assertEquals("   ", capturedData.getTaskId());
    }

    @Test
    void testExecuteWithLongTaskId() {
        // Arrange
        String taskId = "task-with-very-long-id-12345678901234567890";

        // Act
        controller.execute(taskId);

        // Assert
        ArgumentCaptor<RemoveFromTodayInputData> captor = ArgumentCaptor.forClass(RemoveFromTodayInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        RemoveFromTodayInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
    }

    @Test
    void testExecuteCallsInteractorOnce() {
        // Arrange
        String taskId = "task-456";

        // Act
        controller.execute(taskId);

        // Assert
        verify(mockInteractor, times(1)).execute(any(RemoveFromTodayInputData.class));
    }

    @Test
    void testMultipleExecuteCalls() {
        // Arrange
        String taskId1 = "task-1";
        String taskId2 = "task-2";
        String taskId3 = "task-3";

        // Act
        controller.execute(taskId1);
        controller.execute(taskId2);
        controller.execute(taskId3);

        // Assert
        verify(mockInteractor, times(3)).execute(any(RemoveFromTodayInputData.class));
    }

    @Test
    void testExecuteWithSpecialCharacters() {
        // Arrange
        String taskId = "task-!@#$%^&*()";

        // Act
        controller.execute(taskId);

        // Assert
        ArgumentCaptor<RemoveFromTodayInputData> captor = ArgumentCaptor.forClass(RemoveFromTodayInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        RemoveFromTodayInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
    }

    @Test
    void testExecuteWithNumericTaskId() {
        // Arrange
        String taskId = "12345";

        // Act
        controller.execute(taskId);

        // Assert
        ArgumentCaptor<RemoveFromTodayInputData> captor = ArgumentCaptor.forClass(RemoveFromTodayInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        RemoveFromTodayInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
    }

    @Test
    void testConstructorWithNullInteractor() {
        RemoveFromTodayController controller = new RemoveFromTodayController(null);
        assertNotNull(controller);
    }
}