package interface_adapter.alex.event_related.todays_events_module.edit_todays_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventInputBoundary;
import use_case.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditTodaysEventControllerTest {

    @Mock
    private EditTodaysEventInputBoundary mockInteractor;

    private EditTodaysEventController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EditTodaysEventController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithValidIdAndDate() {
        // Arrange
        String eventId = "test-event-123";
        String dueDate = "2023-12-25";

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithComplexEventId() {
        // Arrange
        String eventId = "complex-event-id-with-dashes-456";
        String dueDate = "2024-01-15";

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithEmptyId() {
        // Arrange
        String eventId = "";
        String dueDate = "2023-12-31";

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithNullId() {
        // Arrange
        String eventId = null;
        String dueDate = "2023-06-15";

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertNull(capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithEmptyDueDate() {
        // Arrange
        String eventId = "event-789";
        String dueDate = "";

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithNullDueDate() {
        // Arrange
        String eventId = "event-101";
        String dueDate = null;

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertNull(capturedInputData.getDueDate());
    }

    @Test
    void testExecuteCallsInteractorExactlyOnce() {
        // Arrange
        String eventId = "single-call-event";
        String dueDate = "2024-03-20";

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        verify(mockInteractor, times(1)).execute(any(EditTodaysEventInputData.class));
        verifyNoMoreInteractions(mockInteractor);
    }

    @Test
    void testMultipleExecuteCallsCreateSeparateInputData() {
        // Arrange
        String eventId1 = "event-001";
        String dueDate1 = "2024-01-01";
        String eventId2 = "event-002";
        String dueDate2 = "2024-02-02";

        // Act
        controller.execute(eventId1, dueDate1);
        controller.execute(eventId2, dueDate2);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(2)).execute(captor.capture());

        var capturedInputs = captor.getAllValues();
        assertEquals(2, capturedInputs.size());
        assertEquals(eventId1, capturedInputs.get(0).getId());
        assertEquals(dueDate1, capturedInputs.get(0).getDueDate());
        assertEquals(eventId2, capturedInputs.get(1).getId());
        assertEquals(dueDate2, capturedInputs.get(1).getDueDate());
    }

    @Test
    void testExecuteInteractorExceptionPropagates() {
        // Arrange
        String eventId = "exception-event";
        String dueDate = "2024-12-31";
        RuntimeException expectedException = new RuntimeException("Interactor failed");
        doThrow(expectedException).when(mockInteractor).execute(any(EditTodaysEventInputData.class));

        // Act & Assert
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> {
            controller.execute(eventId, dueDate);
        });

        assertEquals("Interactor failed", actualException.getMessage());
        assertEquals(expectedException, actualException);
    }

    @Test
    void testExecuteWithSpecialCharactersInEventId() {
        // Arrange
        String eventId = "event_special@#$%^&*()";
        String dueDate = "2024-07-04";

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithInvalidDateFormat() {
        // Arrange
        String eventId = "event-invalid-date";
        String dueDate = "invalid-date-format";

        // Act
        controller.execute(eventId, dueDate);

        // Assert - Controller should pass through the invalid date (validation happens in use case layer)
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithLeapYearDate() {
        // Arrange
        String eventId = "leap-year-event";
        String dueDate = "2024-02-29"; // Leap year date

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithPastDate() {
        // Arrange
        String eventId = "past-date-event";
        String dueDate = "2020-01-01"; // Past date

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }

    @Test
    void testExecuteWithFutureDate() {
        // Arrange
        String eventId = "future-date-event";
        String dueDate = "2030-12-31"; // Future date

        // Act
        controller.execute(eventId, dueDate);

        // Assert
        ArgumentCaptor<EditTodaysEventInputData> captor = ArgumentCaptor.forClass(EditTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getId());
        assertEquals(dueDate, capturedInputData.getDueDate());
    }
}