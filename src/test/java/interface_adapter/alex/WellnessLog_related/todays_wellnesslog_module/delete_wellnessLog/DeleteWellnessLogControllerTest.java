package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.wellness_log_related.todays_wellness_log_module.delete_wellnesslog.DeleteWellnessLogInputBoundary;
import use_case.alex.wellness_log_related.todays_wellness_log_module.delete_wellnesslog.DeleteWellnessLogInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteWellnessLogControllerTest {

    @Mock
    private DeleteWellnessLogInputBoundary mockInteractor;

    private DeleteWellnessLogController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DeleteWellnessLogController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        // Constructor coverage is achieved by setUp() method
    }

    @Test
    void testDeleteWithValidLogId() {
        // Arrange
        String logId = "log-123";

        // Act
        controller.delete(logId);

        // Assert
        ArgumentCaptor<DeleteWellnessLogInputData> captor = ArgumentCaptor.forClass(DeleteWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
    }

    @Test
    void testDeleteWithEmptyLogId() {
        // Arrange
        String logId = "";

        // Act
        controller.delete(logId);

        // Assert
        ArgumentCaptor<DeleteWellnessLogInputData> captor = ArgumentCaptor.forClass(DeleteWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
    }

    @Test
    void testDeleteWithNullLogId() {
        // Act
        controller.delete(null);

        // Assert
        ArgumentCaptor<DeleteWellnessLogInputData> captor = ArgumentCaptor.forClass(DeleteWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteWellnessLogInputData capturedInputData = captor.getValue();
        assertNull(capturedInputData.getLogId());
    }

    @Test
    void testDeleteWithUUIDLogId() {
        // Arrange
        String logId = "550e8400-e29b-41d4-a716-446655440000";

        // Act
        controller.delete(logId);

        // Assert
        ArgumentCaptor<DeleteWellnessLogInputData> captor = ArgumentCaptor.forClass(DeleteWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
    }

    @Test
    void testDeleteWithNumericLogId() {
        // Arrange
        String logId = "12345";

        // Act
        controller.delete(logId);

        // Assert
        ArgumentCaptor<DeleteWellnessLogInputData> captor = ArgumentCaptor.forClass(DeleteWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
    }

    @Test
    void testMultipleDeleteCalls() {
        // Arrange
        String logId1 = "log-001";
        String logId2 = "log-002";

        // Act
        controller.delete(logId1);
        controller.delete(logId2);

        // Assert
        ArgumentCaptor<DeleteWellnessLogInputData> captor = ArgumentCaptor.forClass(DeleteWellnessLogInputData.class);
        verify(mockInteractor, times(2)).execute(captor.capture());

        var capturedValues = captor.getAllValues();
        assertEquals(logId1, capturedValues.get(0).getLogId());
        assertEquals(logId2, capturedValues.get(1).getLogId());
    }
}