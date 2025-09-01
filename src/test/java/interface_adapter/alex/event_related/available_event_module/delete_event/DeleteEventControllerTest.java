package interface_adapter.alex.event_related.available_event_module.delete_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.avaliable_events_module.delete_event.DeleteEventInputBoundary;
import use_case.alex.event_related.avaliable_events_module.delete_event.DeleteEventInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteEventControllerTest {

    @Mock
    private DeleteEventInputBoundary mockInteractor;

    private DeleteEventController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DeleteEventController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        // Constructor coverage is achieved by setUp() method
    }

    @Test
    void testDeleteWithValidEventId() {
        // Arrange
        String eventId = "event-123";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteEventInputData> captor = ArgumentCaptor.forClass(DeleteEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getEventId());
    }

    @Test
    void testDeleteWithEventIdWithWhitespace() {
        // Arrange
        String eventId = "  event-456  ";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteEventInputData> captor = ArgumentCaptor.forClass(DeleteEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteEventInputData capturedInputData = captor.getValue();
        assertEquals("event-456", capturedInputData.getEventId()); // Should be trimmed by InputData
    }

    @Test
    void testDeleteWithComplexEventId() {
        // Arrange
        String eventId = "complex-event-id-with-dashes-123";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteEventInputData> captor = ArgumentCaptor.forClass(DeleteEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getEventId());
    }

    @Test
    void testDeleteCallsInteractorExactlyOnce() {
        // Arrange
        String eventId = "single-call-test";

        // Act
        controller.delete(eventId);

        // Assert
        verify(mockInteractor, times(1)).execute(any(DeleteEventInputData.class));
        verifyNoMoreInteractions(mockInteractor);
    }

    @Test
    void testMultipleDeleteCallsCreateSeparateInputData() {
        // Arrange
        String eventId1 = "event-001";
        String eventId2 = "event-002";

        // Act
        controller.delete(eventId1);
        controller.delete(eventId2);

        // Assert
        ArgumentCaptor<DeleteEventInputData> captor = ArgumentCaptor.forClass(DeleteEventInputData.class);
        verify(mockInteractor, times(2)).execute(captor.capture());

        var capturedInputs = captor.getAllValues();
        assertEquals(2, capturedInputs.size());
        assertEquals(eventId1, capturedInputs.get(0).getEventId());
        assertEquals(eventId2, capturedInputs.get(1).getEventId());
    }

    @Test
    void testDeleteWithNumericEventId() {
        // Arrange
        String eventId = "12345";

        // Act
        controller.delete(eventId);

        // Assert
        ArgumentCaptor<DeleteEventInputData> captor = ArgumentCaptor.forClass(DeleteEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        DeleteEventInputData capturedInputData = captor.getValue();
        assertEquals(eventId, capturedInputData.getEventId());
    }

    @Test
    void testDeleteInteractorExceptionPropagates() {
        // Arrange
        String eventId = "exception-event";
        RuntimeException expectedException = new RuntimeException("Interactor failed");
        doThrow(expectedException).when(mockInteractor).execute(any(DeleteEventInputData.class));

        // Act & Assert
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> {
            controller.delete(eventId);
        });

        assertEquals("Interactor failed", actualException.getMessage());
        assertEquals(expectedException, actualException);
    }
}