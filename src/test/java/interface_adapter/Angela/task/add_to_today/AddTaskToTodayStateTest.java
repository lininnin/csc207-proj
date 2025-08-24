package interface_adapter.Angela.task.add_to_today;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddTaskToTodayStateTest {

    private AddTaskToTodayState state;

    @BeforeEach
    void setUp() {
        state = new AddTaskToTodayState();
    }

    @Test
    void testDefaultConstructor() {
        // Assert
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testCopyConstructor() {
        // Arrange
        AddTaskToTodayState original = new AddTaskToTodayState();
        original.setError("Test error");
        original.setSuccessMessage("Test success");
        original.setRefreshNeeded(true);

        // Act
        AddTaskToTodayState copy = new AddTaskToTodayState(original);

        // Assert
        assertEquals("Test error", copy.getError());
        assertEquals("Test success", copy.getSuccessMessage());
        assertTrue(copy.isRefreshNeeded());
        
        // Verify they are different instances
        assertNotSame(original, copy);
    }

    @Test
    void testCopyConstructorWithNullValues() {
        // Arrange
        AddTaskToTodayState original = new AddTaskToTodayState();
        // original already has null values by default

        // Act
        AddTaskToTodayState copy = new AddTaskToTodayState(original);

        // Assert
        assertNull(copy.getError());
        assertNull(copy.getSuccessMessage());
        assertFalse(copy.isRefreshNeeded());
    }

    @Test
    void testSetAndGetError() {
        // Test with null
        state.setError(null);
        assertNull(state.getError());

        // Test with empty string
        state.setError("");
        assertEquals("", state.getError());

        // Test with normal error message
        String errorMessage = "Task not found";
        state.setError(errorMessage);
        assertEquals(errorMessage, state.getError());

        // Test with long error message
        String longError = "A very long error message that might occur during the add to today process";
        state.setError(longError);
        assertEquals(longError, state.getError());
    }

    @Test
    void testSetAndGetSuccessMessage() {
        // Test with null
        state.setSuccessMessage(null);
        assertNull(state.getSuccessMessage());

        // Test with empty string
        state.setSuccessMessage("");
        assertEquals("", state.getSuccessMessage());

        // Test with normal success message
        String successMessage = "Task added successfully";
        state.setSuccessMessage(successMessage);
        assertEquals(successMessage, state.getSuccessMessage());

        // Test with long success message
        String longSuccess = "Task has been successfully added to today's schedule with all dependencies resolved";
        state.setSuccessMessage(longSuccess);
        assertEquals(longSuccess, state.getSuccessMessage());
    }

    @Test
    void testSetAndGetRefreshNeeded() {
        // Test default value
        assertFalse(state.isRefreshNeeded());

        // Test setting to true
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());

        // Test setting to false
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testClearMessages() {
        // Arrange - Set some messages
        state.setError("Some error");
        state.setSuccessMessage("Some success");
        state.setRefreshNeeded(true); // This should not be affected by clearMessages

        // Act
        state.clearMessages();

        // Assert
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        assertTrue(state.isRefreshNeeded()); // Should remain unchanged
    }

    @Test
    void testClearMessagesWithNullValues() {
        // Arrange - Messages are already null by default
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());

        // Act
        state.clearMessages();

        // Assert - Should still be null
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testCompleteStateUpdate() {
        // Act
        state.setError("Connection failed");
        state.setSuccessMessage("Operation completed");
        state.setRefreshNeeded(true);

        // Assert
        assertEquals("Connection failed", state.getError());
        assertEquals("Operation completed", state.getSuccessMessage());
        assertTrue(state.isRefreshNeeded());
    }

    @Test
    void testMultipleUpdatesToSameProperty() {
        // Test error updates
        state.setError("First error");
        assertEquals("First error", state.getError());

        state.setError("Second error");
        assertEquals("Second error", state.getError());

        state.setError(null);
        assertNull(state.getError());

        // Test success message updates
        state.setSuccessMessage("First success");
        assertEquals("First success", state.getSuccessMessage());

        state.setSuccessMessage("Second success");
        assertEquals("Second success", state.getSuccessMessage());

        // Test refresh needed updates
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());

        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testStateTransitions() {
        // Initial state
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        assertFalse(state.isRefreshNeeded());

        // Error state
        state.setError("Something went wrong");
        state.setRefreshNeeded(true);
        assertEquals("Something went wrong", state.getError());
        assertNull(state.getSuccessMessage());
        assertTrue(state.isRefreshNeeded());

        // Clear and set success
        state.clearMessages();
        state.setSuccessMessage("Task added successfully");
        assertNull(state.getError());
        assertEquals("Task added successfully", state.getSuccessMessage());
        assertTrue(state.isRefreshNeeded()); // Should remain true

        // Reset everything
        state.clearMessages();
        state.setRefreshNeeded(false);
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testCopyConstructorIndependence() {
        // Arrange
        AddTaskToTodayState original = new AddTaskToTodayState();
        original.setError("Original error");
        original.setSuccessMessage("Original success");
        original.setRefreshNeeded(true);

        // Act
        AddTaskToTodayState copy = new AddTaskToTodayState(original);
        
        // Modify original after copying
        original.setError("Modified error");
        original.setSuccessMessage("Modified success");
        original.setRefreshNeeded(false);

        // Assert - Copy should remain unchanged
        assertEquals("Original error", copy.getError());
        assertEquals("Original success", copy.getSuccessMessage());
        assertTrue(copy.isRefreshNeeded());
        
        // Original should have new values
        assertEquals("Modified error", original.getError());
        assertEquals("Modified success", original.getSuccessMessage());
        assertFalse(original.isRefreshNeeded());
    }

    @Test
    void testEmptyStringHandling() {
        // Test empty strings are handled properly (not converted to null)
        state.setError("");
        state.setSuccessMessage("");

        assertEquals("", state.getError());
        assertEquals("", state.getSuccessMessage());

        // Clear messages should set them to null, not empty string
        state.clearMessages();
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }
}