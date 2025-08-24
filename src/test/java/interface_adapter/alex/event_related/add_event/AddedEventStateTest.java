package interface_adapter.alex.event_related.add_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddedEventStateTest {

    private AddedEventState state;

    @BeforeEach
    void setUp() {
        state = new AddedEventState();
    }

    @Test
    void testDefaultValues() {
        // Assert default values
        assertEquals("", state.getSelectedName());
        assertNull(state.getDueDate());
        assertNotNull(state.getAvailableNames());
        assertTrue(state.getAvailableNames().isEmpty());
        assertNull(state.getErrorMessage());
        assertNull(state.getSuccessMessage());
    }

    // ==================== Selected Name Tests ====================

    @Test
    void testSetAndGetSelectedName() {
        // Act
        state.setSelectedName("Team Meeting");

        // Assert
        assertEquals("Team Meeting", state.getSelectedName());
    }

    @Test
    void testSetSelectedNameWithNull() {
        // Act
        state.setSelectedName(null);

        // Assert
        assertNull(state.getSelectedName());
    }

    @Test
    void testSetSelectedNameWithEmptyString() {
        // Act
        state.setSelectedName("");

        // Assert
        assertEquals("", state.getSelectedName());
    }

    @Test
    void testSetSelectedNameWithWhitespace() {
        // Act
        state.setSelectedName("  Team Meeting  ");

        // Assert
        assertEquals("  Team Meeting  ", state.getSelectedName()); // State doesn't trim
    }

    // ==================== Due Date Tests ====================

    @Test
    void testSetAndGetDueDate() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 15);

        // Act
        state.setDueDate(date);

        // Assert
        assertEquals(date, state.getDueDate());
    }

    @Test
    void testSetDueDateWithNull() {
        // Act
        state.setDueDate(null);

        // Assert
        assertNull(state.getDueDate());
    }

    @Test
    void testSetDueDateWithFutureDate() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusDays(30);

        // Act
        state.setDueDate(futureDate);

        // Assert
        assertEquals(futureDate, state.getDueDate());
    }

    @Test
    void testSetDueDateWithPastDate() {
        // Arrange
        LocalDate pastDate = LocalDate.now().minusDays(30);

        // Act
        state.setDueDate(pastDate);

        // Assert
        assertEquals(pastDate, state.getDueDate());
    }

    @Test
    void testSetDueDateWithToday() {
        // Arrange
        LocalDate today = LocalDate.now();

        // Act
        state.setDueDate(today);

        // Assert
        assertEquals(today, state.getDueDate());
    }

    // ==================== Available Names Tests ====================

    @Test
    void testSetAndGetAvailableNames() {
        // Arrange
        List<String> names = List.of("Event A", "Event B", "Event C");

        // Act
        state.setAvailableNames(names);

        // Assert
        assertEquals(names, state.getAvailableNames());
        assertEquals(3, state.getAvailableNames().size());
    }

    @Test
    void testSetAvailableNamesWithNull() {
        // Act
        state.setAvailableNames(null);

        // Assert
        assertNull(state.getAvailableNames());
    }

    @Test
    void testSetAvailableNamesWithEmptyList() {
        // Arrange
        List<String> emptyList = new ArrayList<>();

        // Act
        state.setAvailableNames(emptyList);

        // Assert
        assertEquals(emptyList, state.getAvailableNames());
        assertTrue(state.getAvailableNames().isEmpty());
    }

    @Test
    void testSetAvailableNamesWithLargeList() {
        // Arrange
        List<String> largeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeList.add("Event " + i);
        }

        // Act
        state.setAvailableNames(largeList);

        // Assert
        assertEquals(largeList, state.getAvailableNames());
        assertEquals(100, state.getAvailableNames().size());
    }

    @Test
    void testSetAvailableNamesWithDuplicates() {
        // Arrange
        List<String> namesWithDuplicates = List.of("Event A", "Event B", "Event A", "Event C");

        // Act
        state.setAvailableNames(namesWithDuplicates);

        // Assert
        assertEquals(namesWithDuplicates, state.getAvailableNames());
        assertEquals(4, state.getAvailableNames().size()); // Duplicates preserved
    }

    // ==================== Error Message Tests ====================

    @Test
    void testSetAndGetErrorMessage() {
        // Act
        state.setErrorMessage("Selection is required");

        // Assert
        assertEquals("Selection is required", state.getErrorMessage());
    }

    @Test
    void testSetErrorMessageWithNull() {
        // Act
        state.setErrorMessage(null);

        // Assert
        assertNull(state.getErrorMessage());
    }

    @Test
    void testSetErrorMessageWithEmptyString() {
        // Act
        state.setErrorMessage("");

        // Assert
        assertEquals("", state.getErrorMessage());
    }

    @Test
    void testSetErrorMessageWithLongText() {
        // Arrange
        String longMessage = "This is a very long error message ".repeat(10);

        // Act
        state.setErrorMessage(longMessage);

        // Assert
        assertEquals(longMessage, state.getErrorMessage());
    }

    // ==================== Success Message Tests ====================

    @Test
    void testSetAndGetSuccessMessage() {
        // Act
        state.setSuccessMessage("Event added successfully");

        // Assert
        assertEquals("Event added successfully", state.getSuccessMessage());
    }

    @Test
    void testSetSuccessMessageWithNull() {
        // Act
        state.setSuccessMessage(null);

        // Assert
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testSetSuccessMessageWithEmptyString() {
        // Act
        state.setSuccessMessage("");

        // Assert
        assertEquals("", state.getSuccessMessage());
    }

    @Test
    void testSetSuccessMessageWithSpecialCharacters() {
        // Act
        state.setSuccessMessage("Event \"Meeting\" added successfully! 🎉");

        // Assert
        assertEquals("Event \"Meeting\" added successfully! 🎉", state.getSuccessMessage());
    }

    // ==================== Complex State Tests ====================

    @Test
    void testCompleteState() {
        // Arrange
        LocalDate dueDate = LocalDate.of(2024, 3, 15);
        List<String> availableNames = List.of("Event 1", "Event 2");

        // Act
        state.setSelectedName("Selected Event");
        state.setDueDate(dueDate);
        state.setAvailableNames(availableNames);
        state.setErrorMessage("Test error");
        state.setSuccessMessage("Test success");

        // Assert
        assertEquals("Selected Event", state.getSelectedName());
        assertEquals(dueDate, state.getDueDate());
        assertEquals(availableNames, state.getAvailableNames());
        assertEquals("Test error", state.getErrorMessage());
        assertEquals("Test success", state.getSuccessMessage());
    }

    @Test
    void testStateTransitions() {
        // Initial state
        assertEquals("", state.getSelectedName());
        assertNull(state.getErrorMessage());
        assertNull(state.getSuccessMessage());

        // Set values
        state.setSelectedName("Event 1");
        state.setErrorMessage("Error 1");
        assertEquals("Event 1", state.getSelectedName());
        assertEquals("Error 1", state.getErrorMessage());
        assertNull(state.getSuccessMessage());

        // Update values
        state.setSelectedName("Event 2");
        state.setErrorMessage(null);
        state.setSuccessMessage("Success");
        assertEquals("Event 2", state.getSelectedName());
        assertNull(state.getErrorMessage());
        assertEquals("Success", state.getSuccessMessage());
    }

    @Test
    void testErrorSuccessMessageToggle() {
        // Set error message
        state.setErrorMessage("Something went wrong");
        state.setSuccessMessage(null);
        assertEquals("Something went wrong", state.getErrorMessage());
        assertNull(state.getSuccessMessage());

        // Set success message, clear error
        state.setErrorMessage(null);
        state.setSuccessMessage("Operation completed");
        assertNull(state.getErrorMessage());
        assertEquals("Operation completed", state.getSuccessMessage());

        // Clear both
        state.setErrorMessage(null);
        state.setSuccessMessage(null);
        assertNull(state.getErrorMessage());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testAvailableNamesModification() {
        // Set initial list
        List<String> initialNames = new ArrayList<>(List.of("Event A", "Event B"));
        state.setAvailableNames(initialNames);

        // Modify the list externally (if mutable reference is kept)
        List<String> retrievedNames = state.getAvailableNames();
        assertEquals(initialNames, retrievedNames);

        // Set new list
        List<String> newNames = List.of("Event X", "Event Y", "Event Z");
        state.setAvailableNames(newNames);
        assertEquals(3, state.getAvailableNames().size());
        assertEquals("Event X", state.getAvailableNames().get(0));
    }

    @Test
    void testUnicodeAndSpecialCharacters() {
        // Test unicode and special characters
        state.setSelectedName("会议 Meeting 🎉");
        state.setErrorMessage("Errör messäge with ümlauts");
        state.setSuccessMessage("Süccess with spëcial chars!");

        assertEquals("会议 Meeting 🎉", state.getSelectedName());
        assertEquals("Errör messäge with ümlauts", state.getErrorMessage());
        assertEquals("Süccess with spëcial chars!", state.getSuccessMessage());
    }

    @Test
    void testBoundaryDates() {
        // Test boundary dates
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;

        state.setDueDate(minDate);
        assertEquals(minDate, state.getDueDate());

        state.setDueDate(maxDate);
        assertEquals(maxDate, state.getDueDate());
    }
}