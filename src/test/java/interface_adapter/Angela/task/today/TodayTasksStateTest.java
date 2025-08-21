package interface_adapter.Angela.task.today;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TodayTasksState.
 */
class TodayTasksStateTest {

    private TodayTasksState state;

    @BeforeEach
    void setUp() {
        state = new TodayTasksState();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(state);
        assertFalse(state.isRefreshNeeded());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testCopyConstructor() {
        // Arrange
        state.setRefreshNeeded(true);
        state.setError("Test error");
        state.setSuccessMessage("Test success");

        // Act
        TodayTasksState copiedState = new TodayTasksState(state);

        // Assert
        assertEquals(state.isRefreshNeeded(), copiedState.isRefreshNeeded());
        assertEquals(state.getError(), copiedState.getError());
        assertEquals(state.getSuccessMessage(), copiedState.getSuccessMessage());
        
        // Verify they are independent objects
        state.setRefreshNeeded(false);
        assertTrue(copiedState.isRefreshNeeded());
    }

    @Test
    void testSetAndGetRefreshNeeded() {
        // Test setting to true
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());

        // Test setting to false
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testSetAndGetError() {
        // Test setting and getting a valid error message
        String error = "Something went wrong";
        state.setError(error);
        assertEquals(error, state.getError());

        // Test setting null
        state.setError(null);
        assertNull(state.getError());

        // Test setting empty string
        state.setError("");
        assertEquals("", state.getError());
    }

    @Test
    void testSetAndGetSuccessMessage() {
        // Test setting and getting a valid success message
        String successMessage = "Operation completed successfully";
        state.setSuccessMessage(successMessage);
        assertEquals(successMessage, state.getSuccessMessage());

        // Test setting null
        state.setSuccessMessage(null);
        assertNull(state.getSuccessMessage());

        // Test setting empty string
        state.setSuccessMessage("");
        assertEquals("", state.getSuccessMessage());
    }

    @Test
    void testClearMessages() {
        // Arrange
        state.setError("Some error");
        state.setSuccessMessage("Some success");

        // Act
        state.clearMessages();

        // Assert
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        // refreshNeeded should not be affected
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testClearMessagesWithRefreshNeeded() {
        // Arrange
        state.setRefreshNeeded(true);
        state.setError("Some error");
        state.setSuccessMessage("Some success");

        // Act
        state.clearMessages();

        // Assert
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        assertTrue(state.isRefreshNeeded()); // Should remain unchanged
    }

    @Test
    void testClearMessagesWhenAlreadyNull() {
        // Act
        state.clearMessages();

        // Assert
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testMultipleOperations() {
        // Test setting values multiple times
        state.setRefreshNeeded(true);
        state.setRefreshNeeded(false);
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());

        state.setError("error-1");
        state.setError("error-2");
        assertEquals("error-2", state.getError());

        state.setSuccessMessage("success-1");
        state.setSuccessMessage("success-2");
        assertEquals("success-2", state.getSuccessMessage());
    }

    @Test
    void testErrorAndSuccessMessageSimultaneously() {
        // Test that both error and success message can be set (though UI should only show one)
        state.setError("Some error");
        state.setSuccessMessage("Some success");
        
        assertEquals("Some error", state.getError());
        assertEquals("Some success", state.getSuccessMessage());
        
        // Clear error while keeping success
        state.setError(null);
        assertNull(state.getError());
        assertEquals("Some success", state.getSuccessMessage());
        
        // Clear success while setting error
        state.setSuccessMessage(null);
        state.setError("New error");
        assertEquals("New error", state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testCopyConstructorWithNullValues() {
        // Act
        TodayTasksState copiedState = new TodayTasksState(state);

        // Assert
        assertFalse(copiedState.isRefreshNeeded());
        assertNull(copiedState.getError());
        assertNull(copiedState.getSuccessMessage());
    }

    @Test
    void testStateIndependenceInCopy() {
        // Arrange
        state.setRefreshNeeded(true);
        state.setError("original-error");
        state.setSuccessMessage("original-success");

        TodayTasksState copiedState = new TodayTasksState(state);

        // Act - modify original state
        state.setRefreshNeeded(false);
        state.setError("modified-error");
        state.setSuccessMessage("modified-success");

        // Assert - copied state should remain unchanged
        assertTrue(copiedState.isRefreshNeeded());
        assertEquals("original-error", copiedState.getError());
        assertEquals("original-success", copiedState.getSuccessMessage());
    }

    @Test
    void testLongMessages() {
        // Test with long error message
        String longError = "This is a very long error message ".repeat(10);
        state.setError(longError);
        assertEquals(longError, state.getError());

        // Test with long success message
        String longSuccess = "This is a very long success message ".repeat(10);
        state.setSuccessMessage(longSuccess);
        assertEquals(longSuccess, state.getSuccessMessage());
    }

    @Test
    void testSpecialCharacters() {
        // Test with special characters in error
        String specialError = "Error: !@#$%^&*()_+{}|:<>?[]\\;'\",./<>?";
        state.setError(specialError);
        assertEquals(specialError, state.getError());

        // Test with special characters in success
        String specialSuccess = "Success: 你好 emoji 🎉 αβγ";
        state.setSuccessMessage(specialSuccess);
        assertEquals(specialSuccess, state.getSuccessMessage());
    }

    @Test
    void testWhitespaceMessages() {
        // Test with whitespace-only messages
        state.setError("   ");
        assertEquals("   ", state.getError());

        state.setSuccessMessage("\t\n\r");
        assertEquals("\t\n\r", state.getSuccessMessage());
    }

    @Test
    void testTypicalStateTransitions() {
        // Test typical state transitions during UI operations
        
        // Initially clean state
        assertFalse(state.isRefreshNeeded());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        
        // Show error
        state.setError("Validation failed");
        assertEquals("Validation failed", state.getError());
        
        // Clear error and show success
        state.clearMessages();
        state.setSuccessMessage("Task completed");
        assertNull(state.getError());
        assertEquals("Task completed", state.getSuccessMessage());
        
        // Data changes, refresh needed
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        // After refresh, no longer needed
        state.setRefreshNeeded(false);
        state.clearMessages();
        assertFalse(state.isRefreshNeeded());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testToString() {
        // Test that toString doesn't throw exceptions (defensive programming)
        assertDoesNotThrow(() -> state.toString());
        
        state.setRefreshNeeded(true);
        state.setError("Test error");
        state.setSuccessMessage("Test success");
        assertDoesNotThrow(() -> state.toString());
    }
}