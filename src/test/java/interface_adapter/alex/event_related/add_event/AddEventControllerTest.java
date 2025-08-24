package interface_adapter.alex.event_related.add_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.alex.event_related.add_event.AddEventInputBoundary;
import use_case.alex.event_related.add_event.AddEventInputData;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddEventControllerTest {

    private AddEventInputBoundary mockInteractor;
    private AddEventController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(AddEventInputBoundary.class);
        controller = new AddEventController(mockInteractor);
    }

    @Test
    void testConstructor() {
        // Verify constructor properly assigns the interactor
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithValidData() {
        // Arrange
        String selectedName = "Team Meeting";
        LocalDate dueDate = LocalDate.of(2024, 1, 15);

        // Act
        controller.execute(selectedName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        AddEventInputData inputData = captor.getValue();
        assertEquals(selectedName, inputData.getSelectedName());
        assertEquals(dueDate, inputData.getDueDate());
    }

    @Test
    void testExecuteWithNullValues() {
        // Arrange
        String selectedName = null;
        LocalDate dueDate = null;

        // Act
        controller.execute(selectedName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        AddEventInputData inputData = captor.getValue();
        assertNull(inputData.getSelectedName());
        assertNull(inputData.getDueDate());
    }

    @Test
    void testExecuteWithEmptyName() {
        // Arrange
        String selectedName = "";
        LocalDate dueDate = LocalDate.now();

        // Act
        controller.execute(selectedName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        AddEventInputData inputData = captor.getValue();
        assertEquals("", inputData.getSelectedName());
        assertEquals(dueDate, inputData.getDueDate());
    }

    @Test
    void testExecuteWithNullDueDate() {
        // Arrange
        String selectedName = "Team Meeting";
        LocalDate dueDate = null;

        // Act
        controller.execute(selectedName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        AddEventInputData inputData = captor.getValue();
        assertEquals(selectedName, inputData.getSelectedName());
        assertNull(inputData.getDueDate());
    }

    @Test
    void testExecuteWithSpecialCharactersInName() {
        // Arrange
        String selectedName = "Meeting & Review @2024";
        LocalDate dueDate = LocalDate.of(2024, 12, 31);

        // Act
        controller.execute(selectedName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        AddEventInputData inputData = captor.getValue();
        assertEquals(selectedName, inputData.getSelectedName());
        assertEquals(dueDate, inputData.getDueDate());
    }

    @Test
    void testExecuteMultipleCalls() {
        // Arrange
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalDate date2 = LocalDate.of(2024, 2, 1);

        // Act
        controller.execute("Event 1", date1);
        controller.execute("Event 2", date2);

        // Assert
        verify(mockInteractor, times(2)).execute(any(AddEventInputData.class));
    }

    @Test
    void testExecuteWithLongEventName() {
        // Arrange
        String longName = "Very Long Event Name ".repeat(10);
        LocalDate dueDate = LocalDate.now();

        // Act
        controller.execute(longName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        AddEventInputData inputData = captor.getValue();
        assertEquals(longName, inputData.getSelectedName());
        assertEquals(dueDate, inputData.getDueDate());
    }

    @Test
    void testExecuteWithWhitespaceInName() {
        // Arrange
        String selectedName = "  Team Meeting  ";
        LocalDate dueDate = LocalDate.of(2024, 6, 15);

        // Act
        controller.execute(selectedName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        AddEventInputData inputData = captor.getValue();
        assertEquals(selectedName, inputData.getSelectedName()); // Controller doesn't trim
        assertEquals(dueDate, inputData.getDueDate());
    }

    @Test
    void testExecutePropagatesException() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Interactor failed");
        doThrow(expectedException).when(mockInteractor).execute(any(AddEventInputData.class));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
            controller.execute("Test Event", LocalDate.now()));

        assertEquals("Interactor failed", thrown.getMessage());
        verify(mockInteractor, times(1)).execute(any(AddEventInputData.class));
    }

    @Test
    void testExecutePreservesInputDataIntegrity() {
        // Arrange
        String selectedName = "Important Event";
        LocalDate dueDate = LocalDate.of(2024, 3, 20);

        // Act
        controller.execute(selectedName, dueDate);

        // Assert - Verify that the exact values are passed through without modification
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor).execute(captor.capture());

        AddEventInputData captured = captor.getValue();
        assertAll(
            () -> assertEquals(selectedName, captured.getSelectedName()),
            () -> assertEquals(dueDate, captured.getDueDate())
        );
    }

    @Test
    void testExecuteWithFutureDueDate() {
        // Arrange
        String selectedName = "Future Event";
        LocalDate futureDate = LocalDate.now().plusDays(30);

        // Act
        controller.execute(selectedName, futureDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor).execute(captor.capture());

        AddEventInputData captured = captor.getValue();
        assertEquals(selectedName, captured.getSelectedName());
        assertEquals(futureDate, captured.getDueDate());
    }

    @Test
    void testExecuteWithPastDueDate() {
        // Arrange
        String selectedName = "Past Event";
        LocalDate pastDate = LocalDate.now().minusDays(30);

        // Act
        controller.execute(selectedName, pastDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor).execute(captor.capture());

        AddEventInputData captured = captor.getValue();
        assertEquals(selectedName, captured.getSelectedName());
        assertEquals(pastDate, captured.getDueDate());
    }

    @Test
    void testExecuteWithTodayDueDate() {
        // Arrange
        String selectedName = "Today Event";
        LocalDate today = LocalDate.now();

        // Act
        controller.execute(selectedName, today);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor).execute(captor.capture());

        AddEventInputData captured = captor.getValue();
        assertEquals(selectedName, captured.getSelectedName());
        assertEquals(today, captured.getDueDate());
    }

    @Test
    void testExecuteWithUnicodeCharacters() {
        // Arrange
        String selectedName = "会议 Meeting 🎉";
        LocalDate dueDate = LocalDate.of(2024, 5, 10);

        // Act
        controller.execute(selectedName, dueDate);

        // Assert
        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor).execute(captor.capture());

        AddEventInputData captured = captor.getValue();
        assertEquals(selectedName, captured.getSelectedName());
        assertEquals(dueDate, captured.getDueDate());
    }

    @Test
    void testExecuteSequentialCalls() {
        // Arrange
        String[] names = {"Event A", "Event B", "Event C"};
        LocalDate[] dates = {
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 2, 1),
            LocalDate.of(2024, 3, 1)
        };

        // Act
        for (int i = 0; i < names.length; i++) {
            controller.execute(names[i], dates[i]);
        }

        // Assert
        verify(mockInteractor, times(3)).execute(any(AddEventInputData.class));

        ArgumentCaptor<AddEventInputData> captor = ArgumentCaptor.forClass(AddEventInputData.class);
        verify(mockInteractor, times(3)).execute(captor.capture());

        // Verify last call
        AddEventInputData lastCall = captor.getValue();
        assertEquals("Event C", lastCall.getSelectedName());
        assertEquals(LocalDate.of(2024, 3, 1), lastCall.getDueDate());
    }
}