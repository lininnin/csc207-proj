package interface_adapter.Angela.task.edit_available;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditAvailableTaskState.
 */
class EditAvailableTaskStateTest {

    private EditAvailableTaskState state;

    @BeforeEach
    void setUp() {
        state = new EditAvailableTaskState();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(state);
        assertNull(state.getEditingTaskId());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testCopyConstructor() {
        // Arrange
        state.setEditingTaskId("task-123");
        state.setError("Test error");
        state.setSuccessMessage("Test success");

        // Act
        EditAvailableTaskState copiedState = new EditAvailableTaskState(state);

        // Assert
        assertEquals(state.getEditingTaskId(), copiedState.getEditingTaskId());
        assertEquals(state.getError(), copiedState.getError());
        assertEquals(state.getSuccessMessage(), copiedState.getSuccessMessage());
        
        // Verify they are independent objects
        state.setEditingTaskId("different-task");
        assertNotEquals(state.getEditingTaskId(), copiedState.getEditingTaskId());
    }

    @Test
    void testSetAndGetEditingTaskId() {
        // Test setting and getting a valid task ID
        String taskId = "task-456";
        state.setEditingTaskId(taskId);
        assertEquals(taskId, state.getEditingTaskId());

        // Test setting null
        state.setEditingTaskId(null);
        assertNull(state.getEditingTaskId());

        // Test setting empty string
        state.setEditingTaskId("");
        assertEquals("", state.getEditingTaskId());
    }

    @Test
    void testSetAndGetError() {
        // Test setting and getting a valid error message
        String error = "Task name is required";
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
        String successMessage = "Task updated successfully";
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
        state.setEditingTaskId("task-123");

        // Act
        state.clearMessages();

        // Assert
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
        assertEquals("task-123", state.getEditingTaskId()); // Should not be cleared
    }

    @Test
    void testToString() {
        // Test with all null values
        String expected = "EditAvailableTaskState{editingTaskId='null', error='null', successMessage='null'}";
        assertEquals(expected, state.toString());

        // Test with all values set
        state.setEditingTaskId("task-123");
        state.setError("Test error");
        state.setSuccessMessage("Test success");
        
        String expectedWithValues = "EditAvailableTaskState{editingTaskId='task-123', error='Test error', successMessage='Test success'}";
        assertEquals(expectedWithValues, state.toString());
    }

    @Test
    void testStateTransitions() {
        // Test typical state transitions during editing
        
        // Start editing
        state.setEditingTaskId("task-123");
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());

        // Show error
        state.setError("Name is required");
        assertEquals("Name is required", state.getError());
        assertNull(state.getSuccessMessage());

        // Clear error and show success
        state.setError(null);
        state.setSuccessMessage("Task updated successfully");
        assertNull(state.getError());
        assertEquals("Task updated successfully", state.getSuccessMessage());

        // Clear editing state
        state.setEditingTaskId(null);
        state.clearMessages();
        assertNull(state.getEditingTaskId());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testCopyConstructorWithNullValues() {
        // Act
        EditAvailableTaskState copiedState = new EditAvailableTaskState(state);

        // Assert
        assertNull(copiedState.getEditingTaskId());
        assertNull(copiedState.getError());
        assertNull(copiedState.getSuccessMessage());
    }

    @Test
    void testMultipleOperations() {
        // Test setting values multiple times
        state.setEditingTaskId("task-1");
        state.setEditingTaskId("task-2");
        state.setEditingTaskId("task-3");
        assertEquals("task-3", state.getEditingTaskId());

        state.setError("error-1");
        state.setError("error-2");
        assertEquals("error-2", state.getError());

        state.setSuccessMessage("success-1");
        state.setSuccessMessage("success-2");
        assertEquals("success-2", state.getSuccessMessage());
    }
}