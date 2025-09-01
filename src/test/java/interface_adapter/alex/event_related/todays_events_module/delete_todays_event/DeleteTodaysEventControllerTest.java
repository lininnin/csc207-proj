package interface_adapter.alex.event_related.todays_events_module.delete_todays_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventInputBoundary;
import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteTodaysEventControllerTest {

    @Mock
    private DeleteTodaysEventInputBoundary mockInteractor;

    private DeleteTodaysEventController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DeleteTodaysEventController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        // Constructor coverage is achieved by setUp() method
    }

    @Test
    void testDeleteWithValidEventId() {
        // Arrange
        String eventId = "todays-event-123";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteTodaysEventInputData> captor = ArgumentCaptor.forClass(DeleteTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getEventId());
    }

    @Test
    void testDeleteWithEventIdWithWhitespace() {
        // Arrange
        String eventId = "  todays-event-456  ";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteTodaysEventInputData> captor = ArgumentCaptor.forClass(DeleteTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals("todays-event-456", capturedInputData.getEventId()); // Should be trimmed by InputData
    }

    @Test
    void testDeleteWithComplexEventId() {
        // Arrange
        String eventId = "todays-complex-event-id-with-dashes-789";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteTodaysEventInputData> captor = ArgumentCaptor.forClass(DeleteTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getEventId());
    }

    @Test
    void testDeleteCallsInteractorExactlyOnce() {
        // Arrange
        String eventId = "single-call-todays-event";

        // Act
        controller.delete(eventId);

        // Assert
        verify(mockInteractor, times(1)).execute(any(DeleteTodaysEventInputData.class));
        verifyNoMoreInteractions(mockInteractor);
    }

    @Test
    void testMultipleDeleteCallsCreateSeparateInputData() {
        // Arrange
        String eventId1 = "todays-event-001";
        String eventId2 = "todays-event-002";

        // Act
        controller.delete(eventId1);
        controller.delete(eventId2);

        // Assert
        ArgumentCaptor<DeleteTodaysEventInputData> captor = ArgumentCaptor.forClass(DeleteTodaysEventInputData.class);
        verify(mockInteractor, times(2)).execute(captor.capture());

        var capturedInputs = captor.getAllValues();
        assertEquals(2, capturedInputs.size());
        assertEquals(eventId1, capturedInputs.get(0).getEventId());
        assertEquals(eventId2, capturedInputs.get(1).getEventId());
    }

    @Test
    void testDeleteWithNumericEventId() {
        // Arrange
        String eventId = "todays-12345";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteTodaysEventInputData> captor = ArgumentCaptor.forClass(DeleteTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getEventId());
    }

    @Test
    void testDeleteInteractorExceptionPropagates() {
        // Arrange
        String eventId = "exception-todays-event";
        RuntimeException expectedException = new RuntimeException("Interactor failed");
        doThrow(expectedException).when(mockInteractor).execute(any(DeleteTodaysEventInputData.class));

        // Act & Assert
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> {
            controller.delete(eventId);
        });

        assertEquals("Interactor failed", actualException.getMessage());
        assertEquals(expectedException, actualException);
    }

    @Test
    void testDeleteWithSpecialCharactersInEventId() {
        // Arrange
        String eventId = "todays-event_special@#$%";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteTodaysEventInputData> captor = ArgumentCaptor.forClass(DeleteTodaysEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteTodaysEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getEventId());
    }
}