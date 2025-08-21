package interface_adapter.Angela.task.edit_today;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditTodayTaskState.
 */
class EditTodayTaskStateTest {

    private EditTodayTaskState state;

    @BeforeEach
    void setUp() {
        state = new EditTodayTaskState();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(state);
        assertNull(state.getEditingTaskId());
        assertNull(state.getLastEditedTaskId());
        assertNull(state.getSuccessMessage());
        assertNull(state.getError());
        assertFalse(state.isShowDialog());
    }

    @Test
    void testCopyConstructor() {
        // Arrange
        state.setEditingTaskId("task-123");
        state.setLastEditedTaskId("task-456");
        state.setSuccessMessage("Success message");
        state.setError("Error message");
        state.setShowDialog(true);

        // Act
        EditTodayTaskState copiedState = new EditTodayTaskState(state);

        // Assert
        assertEquals(state.getEditingTaskId(), copiedState.getEditingTaskId());
        assertEquals(state.getLastEditedTaskId(), copiedState.getLastEditedTaskId());
        assertEquals(state.getSuccessMessage(), copiedState.getSuccessMessage());
        assertEquals(state.getError(), copiedState.getError());
        assertEquals(state.isShowDialog(), copiedState.isShowDialog());
        
        // Verify they are independent objects
        state.setEditingTaskId("different-task");
        assertNotEquals(state.getEditingTaskId(), copiedState.getEditingTaskId());
    }

    @Test
    void testSetAndGetEditingTaskId() {
        // Test setting and getting a valid task ID
        String taskId = "editing-task-123";
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
    void testSetAndGetLastEditedTaskId() {
        // Test setting and getting a valid task ID
        String taskId = "last-edited-456";
        state.setLastEditedTaskId(taskId);
        assertEquals(taskId, state.getLastEditedTaskId());

        // Test setting null
        state.setLastEditedTaskId(null);
        assertNull(state.getLastEditedTaskId());

        // Test setting empty string
        state.setLastEditedTaskId("");
        assertEquals("", state.getLastEditedTaskId());
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
    void testSetAndGetError() {
        // Test setting and getting a valid error message
        String error = "Task not found";
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
    void testSetAndGetShowDialog() {
        // Test setting to true
        state.setShowDialog(true);
        assertTrue(state.isShowDialog());

        // Test setting to false
        state.setShowDialog(false);
        assertFalse(state.isShowDialog());
    }

    @Test
    void testStateTransitions() {
        // Test typical state transitions during editing
        
        // Start editing - show dialog
        state.setEditingTaskId("task-123");
        state.setShowDialog(true);
        assertEquals("task-123", state.getEditingTaskId());
        assertTrue(state.isShowDialog());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());

        // Show error during editing
        state.setError("Priority is required");
        assertEquals("Priority is required", state.getError());
        assertNull(state.getSuccessMessage());
        assertTrue(state.isShowDialog()); // Dialog should still be shown

        // Clear error and show success
        state.setError(null);
        state.setSuccessMessage("Task updated successfully");
        state.setLastEditedTaskId("task-123");
        assertNull(state.getError());
        assertEquals("Task updated successfully", state.getSuccessMessage());
        assertEquals("task-123", state.getLastEditedTaskId());

        // Close dialog after success
        state.setShowDialog(false);
        state.setEditingTaskId(null);
        assertFalse(state.isShowDialog());
        assertNull(state.getEditingTaskId());
    }

    @Test
    void testCopyConstructorWithNullValues() {
        // Act
        EditTodayTaskState copiedState = new EditTodayTaskState(state);

        // Assert
        assertNull(copiedState.getEditingTaskId());
        assertNull(copiedState.getLastEditedTaskId());
        assertNull(copiedState.getSuccessMessage());
        assertNull(copiedState.getError());
        assertFalse(copiedState.isShowDialog());
    }

    @Test
    void testMultipleOperations() {
        // Test setting values multiple times
        state.setEditingTaskId("task-1");
        state.setEditingTaskId("task-2");
        state.setEditingTaskId("task-3");
        assertEquals("task-3", state.getEditingTaskId());

        state.setLastEditedTaskId("last-1");
        state.setLastEditedTaskId("last-2");
        assertEquals("last-2", state.getLastEditedTaskId());

        state.setError("error-1");
        state.setError("error-2");
        assertEquals("error-2", state.getError());

        state.setSuccessMessage("success-1");
        state.setSuccessMessage("success-2");
        assertEquals("success-2", state.getSuccessMessage());

        state.setShowDialog(true);
        state.setShowDialog(false);
        state.setShowDialog(true);
        assertTrue(state.isShowDialog());
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
    void testAllFieldsIndependentInCopy() {
        // Arrange
        state.setEditingTaskId("original-editing");
        state.setLastEditedTaskId("original-last-edited");
        state.setSuccessMessage("original-success");
        state.setError("original-error");
        state.setShowDialog(true);

        EditTodayTaskState copiedState = new EditTodayTaskState(state);

        // Act - modify original state
        state.setEditingTaskId("modified-editing");
        state.setLastEditedTaskId("modified-last-edited");
        state.setSuccessMessage("modified-success");
        state.setError("modified-error");
        state.setShowDialog(false);

        // Assert - copied state should remain unchanged
        assertEquals("original-editing", copiedState.getEditingTaskId());
        assertEquals("original-last-edited", copiedState.getLastEditedTaskId());
        assertEquals("original-success", copiedState.getSuccessMessage());
        assertEquals("original-error", copiedState.getError());
        assertTrue(copiedState.isShowDialog());
    }
}