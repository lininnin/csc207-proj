package interface_adapter.Angela.task.overdue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.Angela.task.overdue.OverdueTasksInputBoundary;
import use_case.Angela.task.overdue.OverdueTasksInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OverdueTasksControllerTest {

    private OverdueTasksController controller;
    private OverdueTasksInputBoundary mockInteractor;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(OverdueTasksInputBoundary.class);
        controller = new OverdueTasksController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    void testConstructorWithNullInteractor() {
        // The constructor doesn't validate null, so this test should check runtime behavior
        OverdueTasksController controller = new OverdueTasksController(null);
        assertNotNull(controller);
        // The NPE would occur when execute is called
        assertThrows(NullPointerException.class, () -> controller.execute(1));
    }

    @Test
    void testExecuteWithValidDaysBack() {
        // Arrange
        int daysBack = 7;

        // Act
        controller.execute(daysBack);

        // Assert
        ArgumentCaptor<OverdueTasksInputData> captor = ArgumentCaptor.forClass(OverdueTasksInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());
        
        OverdueTasksInputData capturedInputData = captor.getValue();
        assertEquals(daysBack, capturedInputData.getDaysBack());
    }

    @Test
    void testExecuteWithZeroDaysBack() {
        // Arrange
        int daysBack = 0;

        // Act & Assert - Input validation should throw exception
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, 
            () -> controller.execute(daysBack));
        assertEquals("Days back must be positive", thrown.getMessage());
        
        // Verify interactor was never called
        verify(mockInteractor, never()).execute(any(OverdueTasksInputData.class));
    }

    @Test
    void testExecuteWithNegativeDaysBack() {
        // Arrange
        int daysBack = -5;

        // Act & Assert - Input validation should throw exception
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, 
            () -> controller.execute(daysBack));
        assertEquals("Days back must be positive", thrown.getMessage());
        
        // Verify interactor was never called
        verify(mockInteractor, never()).execute(any(OverdueTasksInputData.class));
    }

    @Test
    void testExecuteWithLargeDaysBack() {
        // Arrange
        int daysBack = 365;

        // Act
        controller.execute(daysBack);

        // Assert
        ArgumentCaptor<OverdueTasksInputData> captor = ArgumentCaptor.forClass(OverdueTasksInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());
        
        OverdueTasksInputData capturedInputData = captor.getValue();
        assertEquals(daysBack, capturedInputData.getDaysBack());
    }

    @Test
    void testExecuteMultipleCalls() {
        // Arrange
        int daysBack1 = 3;
        int daysBack2 = 14;
        int daysBack3 = 30;

        // Act
        controller.execute(daysBack1);
        controller.execute(daysBack2);
        controller.execute(daysBack3);

        // Assert
        verify(mockInteractor, times(3)).execute(any(OverdueTasksInputData.class));
    }

    @Test
    void testExecuteInteractorException() {
        // Arrange
        int daysBack = 7;
        RuntimeException expectedException = new RuntimeException("Interactor error");
        doThrow(expectedException).when(mockInteractor).execute(any(OverdueTasksInputData.class));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> controller.execute(daysBack));
        assertEquals("Interactor error", thrown.getMessage());
        verify(mockInteractor, times(1)).execute(any(OverdueTasksInputData.class));
    }

    @Test
    void testExecuteCallsInteractorOnce() {
        // Arrange
        int daysBack = 1;

        // Act
        controller.execute(daysBack);

        // Assert
        verify(mockInteractor, times(1)).execute(any(OverdueTasksInputData.class));
        verifyNoMoreInteractions(mockInteractor);
    }

    @Test
    void testExecuteInputDataCreation() {
        // Arrange
        int daysBack = 10;

        // Act
        controller.execute(daysBack);

        // Assert
        ArgumentCaptor<OverdueTasksInputData> captor = ArgumentCaptor.forClass(OverdueTasksInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        OverdueTasksInputData inputData = captor.getValue();
        assertNotNull(inputData);
        assertEquals(daysBack, inputData.getDaysBack());
    }
}