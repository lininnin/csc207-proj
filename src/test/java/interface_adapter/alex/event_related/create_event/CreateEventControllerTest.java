package interface_adapter.alex.event_related.create_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.alex.event_related.create_event.CreateEventInputBoundary;
import use_case.alex.event_related.create_event.CreateEventInputData;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEventControllerTest {

    private CreateEventInputBoundary mockInteractor;
    private CreateEventController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(CreateEventInputBoundary.class);
        controller = new CreateEventController(mockInteractor);
    }

    @Test
    void testConstructor() {
        // Verify constructor properly assigns the interactor
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithValidData() {
        // Arrange
        String id = "event-123";
        String name = "Team Meeting";
        String description = "Weekly standup meeting";
        String category = "Work";
        LocalDate createdDate = LocalDate.of(2024, 1, 15);

        // Act
        controller.execute(id, name, description, category, createdDate);

        // Assert
        ArgumentCaptor<CreateEventInputData> captor = ArgumentCaptor.forClass(CreateEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        CreateEventInputData inputData = captor.getValue();
        assertEquals(id, inputData.getId());
        assertEquals(name, inputData.getName());
        assertEquals(description, inputData.getDescription());
        assertEquals(category, inputData.getCategory());
        assertEquals(createdDate, inputData.getCreatedDate());
    }

    @Test
    void testExecuteWithNullValues() {
        // Arrange
        String id = null;
        String name = null;
        String description = null;
        String category = null;
        LocalDate createdDate = null;

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
            controller.execute(id, name, description, category, createdDate));

        assertEquals("id cannot be null or empty", thrown.getMessage());
        verify(mockInteractor, never()).execute(any(CreateEventInputData.class));
    }

    @Test
    void testExecuteWithEmptyStrings() {
        // Arrange
        String id = "";
        String name = "";
        String description = "";
        String category = "";
        LocalDate createdDate = LocalDate.now();

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
            controller.execute(id, name, description, category, createdDate));

        assertEquals("id cannot be null or empty", thrown.getMessage());
        verify(mockInteractor, never()).execute(any(CreateEventInputData.class));
    }

    @Test
    void testExecuteWithSpecialCharacters() {
        // Arrange
        String id = "event@123";
        String name = "Meeting & Review";
        String description = "Review & analyze project status (Q1)";
        String category = "Work/Project";
        LocalDate createdDate = LocalDate.of(2024, 12, 31);

        // Act
        controller.execute(id, name, description, category, createdDate);

        // Assert
        ArgumentCaptor<CreateEventInputData> captor = ArgumentCaptor.forClass(CreateEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        CreateEventInputData inputData = captor.getValue();
        assertEquals(id, inputData.getId());
        assertEquals(name, inputData.getName());
        assertEquals(description, inputData.getDescription());
        assertEquals(category, inputData.getCategory());
        assertEquals(createdDate, inputData.getCreatedDate());
    }

    @Test
    void testExecuteMultipleCalls() {
        // Arrange
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalDate date2 = LocalDate.of(2024, 2, 1);

        // Act
        controller.execute("id1", "Event 1", "Description 1", "Category 1", date1);
        controller.execute("id2", "Event 2", "Description 2", "Category 2", date2);

        // Assert
        verify(mockInteractor, times(2)).execute(any(CreateEventInputData.class));
    }

    @Test
    void testExecuteWithLongStrings() {
        // Arrange
        String id = "a".repeat(100);
        String name = "b".repeat(200);
        String description = "c".repeat(300);
        String category = "d".repeat(50);
        LocalDate createdDate = LocalDate.now();

        // Act
        controller.execute(id, name, description, category, createdDate);

        // Assert
        ArgumentCaptor<CreateEventInputData> captor = ArgumentCaptor.forClass(CreateEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        CreateEventInputData inputData = captor.getValue();
        assertEquals(id, inputData.getId());
        assertEquals(name, inputData.getName());
        assertEquals(description, inputData.getDescription());
        assertEquals(category, inputData.getCategory());
        assertEquals(createdDate, inputData.getCreatedDate());
    }

    @Test
    void testExecuteWithWhitespace() {
        // Arrange
        String id = "  event-123  ";
        String name = "\t\nTeam Meeting\n\t";
        String description = "  Weekly standup  ";
        String category = "\nWork\n";
        LocalDate createdDate = LocalDate.of(2024, 6, 15);

        // Act
        controller.execute(id, name, description, category, createdDate);

        // Assert
        ArgumentCaptor<CreateEventInputData> captor = ArgumentCaptor.forClass(CreateEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        CreateEventInputData inputData = captor.getValue();
        assertEquals(id, inputData.getId());
        assertEquals(name, inputData.getName());
        assertEquals(description, inputData.getDescription());
        assertEquals(category, inputData.getCategory());
        assertEquals(createdDate, inputData.getCreatedDate());
    }

    @Test
    void testExecutePropagatesException() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Interactor failed");
        doThrow(expectedException).when(mockInteractor).execute(any(CreateEventInputData.class));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
            controller.execute("id", "name", "desc", "category", LocalDate.now()));

        assertEquals("Interactor failed", thrown.getMessage());
        verify(mockInteractor, times(1)).execute(any(CreateEventInputData.class));
    }

    @Test
    void testExecutePreservesInputDataIntegrity() {
        // Arrange
        String id = "event-456";
        String name = "Important Meeting";
        String description = "Critical business discussion";
        String category = "High Priority";
        LocalDate createdDate = LocalDate.of(2024, 3, 20);

        // Act
        controller.execute(id, name, description, category, createdDate);

        // Assert - Verify that the exact values are passed through without modification
        ArgumentCaptor<CreateEventInputData> captor = ArgumentCaptor.forClass(CreateEventInputData.class);
        verify(mockInteractor).execute(captor.capture());

        CreateEventInputData captured = captor.getValue();
        assertAll(
            () -> assertEquals(id, captured.getId()),
            () -> assertEquals(name, captured.getName()),
            () -> assertEquals(description, captured.getDescription()),
            () -> assertEquals(category, captured.getCategory()),
            () -> assertEquals(createdDate, captured.getCreatedDate())
        );
    }
}