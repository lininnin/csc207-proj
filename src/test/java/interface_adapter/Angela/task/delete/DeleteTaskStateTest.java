package interface_adapter.Angela.task.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTaskStateTest {

    private DeleteTaskState state;

    @BeforeEach
    void setUp() {
        state = new DeleteTaskState();
    }

    @Test
    void testConstructor() {
        // Assert all fields are initialized to their default values
        assertNull(state.getLastDeletedTaskId());
        assertNull(state.getSuccessMessage());
        assertNull(state.getError());
        assertFalse(state.isShowWarningDialog());
        assertNull(state.getPendingDeleteTaskId());
        assertNull(state.getPendingDeleteTaskName());
    }

    @Test
    void testSetAndGetLastDeletedTaskId() {
        // Test with null
        state.setLastDeletedTaskId(null);
        assertNull(state.getLastDeletedTaskId());

        // Test with empty string
        state.setLastDeletedTaskId("");
        assertEquals("", state.getLastDeletedTaskId());

        // Test with normal task ID
        String taskId = "task-123";
        state.setLastDeletedTaskId(taskId);
        assertEquals(taskId, state.getLastDeletedTaskId());

        // Test with UUID-like ID
        String uuidId = "550e8400-e29b-41d4-a716-446655440000";
        state.setLastDeletedTaskId(uuidId);
        assertEquals(uuidId, state.getLastDeletedTaskId());
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
        String successMessage = "Task deleted successfully";
        state.setSuccessMessage(successMessage);
        assertEquals(successMessage, state.getSuccessMessage());

        // Test with long success message
        String longMessage = "The task has been successfully removed from your list and all associated data has been cleared";
        state.setSuccessMessage(longMessage);
        assertEquals(longMessage, state.getSuccessMessage());
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

        // Test with detailed error message
        String detailedError = "Cannot delete task: Task is currently being used in an active goal";
        state.setError(detailedError);
        assertEquals(detailedError, state.getError());
    }

    @Test
    void testSetAndGetShowWarningDialog() {
        // Test default value
        assertFalse(state.isShowWarningDialog());

        // Test setting to true
        state.setShowWarningDialog(true);
        assertTrue(state.isShowWarningDialog());

        // Test setting to false
        state.setShowWarningDialog(false);
        assertFalse(state.isShowWarningDialog());
    }

    @Test
    void testSetAndGetPendingDeleteTaskId() {
        // Test with null
        state.setPendingDeleteTaskId(null);
        assertNull(state.getPendingDeleteTaskId());

        // Test with empty string
        state.setPendingDeleteTaskId("");
        assertEquals("", state.getPendingDeleteTaskId());

        // Test with normal task ID
        String taskId = "pending-task-456";
        state.setPendingDeleteTaskId(taskId);
        assertEquals(taskId, state.getPendingDeleteTaskId());

        // Test with complex ID
        String complexId = "user-123-task-789-2023";
        state.setPendingDeleteTaskId(complexId);
        assertEquals(complexId, state.getPendingDeleteTaskId());
    }

    @Test
    void testSetAndGetPendingDeleteTaskName() {
        // Test with null
        state.setPendingDeleteTaskName(null);
        assertNull(state.getPendingDeleteTaskName());

        // Test with empty string
        state.setPendingDeleteTaskName("");
        assertEquals("", state.getPendingDeleteTaskName());

        // Test with normal task name
        String taskName = "Complete project proposal";
        state.setPendingDeleteTaskName(taskName);
        assertEquals(taskName, state.getPendingDeleteTaskName());

        // Test with long task name
        String longName = "Review and finalize the quarterly business analysis report including market trends and financial projections";
        state.setPendingDeleteTaskName(longName);
        assertEquals(longName, state.getPendingDeleteTaskName());
    }

    @Test
    void testCompleteStateUpdate() {
        // Arrange and Act
        state.setLastDeletedTaskId("deleted-123");
        state.setSuccessMessage("Task successfully deleted");
        state.setError(null);
        state.setShowWarningDialog(false);
        state.setPendingDeleteTaskId(null);
        state.setPendingDeleteTaskName(null);

        // Assert
        assertEquals("deleted-123", state.getLastDeletedTaskId());
        assertEquals("Task successfully deleted", state.getSuccessMessage());
        assertNull(state.getError());
        assertFalse(state.isShowWarningDialog());
        assertNull(state.getPendingDeleteTaskId());
        assertNull(state.getPendingDeleteTaskName());
    }

    @Test
    void testPendingDeleteScenario() {
        // Arrange - Setup a pending delete scenario
        String pendingId = "task-to-delete";
        String pendingName = "Important Task";

        // Act
        state.setPendingDeleteTaskId(pendingId);
        state.setPendingDeleteTaskName(pendingName);
        state.setShowWarningDialog(true);
        state.setError(null);
        state.setSuccessMessage(null);

        // Assert
        assertEquals(pendingId, state.getPendingDeleteTaskId());
        assertEquals(pendingName, state.getPendingDeleteTaskName());
        assertTrue(state.isShowWarningDialog());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testErrorScenario() {
        // Arrange - Setup an error scenario
        String errorMessage = "Cannot delete: Task has dependencies";

        // Act
        state.setError(errorMessage);
        state.setSuccessMessage(null);
        state.setShowWarningDialog(false);
        state.setLastDeletedTaskId(null);

        // Assert
        assertEquals(errorMessage, state.getError());
        assertNull(state.getSuccessMessage());
        assertFalse(state.isShowWarningDialog());
        assertNull(state.getLastDeletedTaskId());
    }

    @Test
    void testSuccessfulDeleteScenario() {
        // Arrange - Setup a successful delete scenario
        String deletedTaskId = "successfully-deleted-task";
        String successMessage = "Task removed from your list";

        // Act
        state.setLastDeletedTaskId(deletedTaskId);
        state.setSuccessMessage(successMessage);
        state.setError(null);
        state.setShowWarningDialog(false);
        state.setPendingDeleteTaskId(null);
        state.setPendingDeleteTaskName(null);

        // Assert
        assertEquals(deletedTaskId, state.getLastDeletedTaskId());
        assertEquals(successMessage, state.getSuccessMessage());
        assertNull(state.getError());
        assertFalse(state.isShowWarningDialog());
        assertNull(state.getPendingDeleteTaskId());
        assertNull(state.getPendingDeleteTaskName());
    }

    @Test
    void testMultipleUpdatesToSameProperty() {
        // Test lastDeletedTaskId updates
        state.setLastDeletedTaskId("first-task");
        assertEquals("first-task", state.getLastDeletedTaskId());

        state.setLastDeletedTaskId("second-task");
        assertEquals("second-task", state.getLastDeletedTaskId());

        state.setLastDeletedTaskId(null);
        assertNull(state.getLastDeletedTaskId());

        // Test showWarningDialog updates
        state.setShowWarningDialog(true);
        assertTrue(state.isShowWarningDialog());

        state.setShowWarningDialog(false);
        assertFalse(state.isShowWarningDialog());

        state.setShowWarningDialog(true);
        assertTrue(state.isShowWarningDialog());
    }

    @Test
    void testStateTransitions() {
        // Initial state
        assertNull(state.getLastDeletedTaskId());
        assertNull(state.getSuccessMessage());
        assertNull(state.getError());
        assertFalse(state.isShowWarningDialog());
        assertNull(state.getPendingDeleteTaskId());
        assertNull(state.getPendingDeleteTaskName());

        // Transition to pending delete
        state.setPendingDeleteTaskId("task-123");
        state.setPendingDeleteTaskName("Test Task");
        state.setShowWarningDialog(true);

        assertEquals("task-123", state.getPendingDeleteTaskId());
        assertEquals("Test Task", state.getPendingDeleteTaskName());
        assertTrue(state.isShowWarningDialog());

        // Transition to error state
        state.setError("Deletion failed");
        state.setShowWarningDialog(false);
        state.setPendingDeleteTaskId(null);
        state.setPendingDeleteTaskName(null);

        assertEquals("Deletion failed", state.getError());
        assertFalse(state.isShowWarningDialog());
        assertNull(state.getPendingDeleteTaskId());
        assertNull(state.getPendingDeleteTaskName());

        // Transition to success state
        state.setError(null);
        state.setLastDeletedTaskId("task-123");
        state.setSuccessMessage("Task deleted successfully");

        assertNull(state.getError());
        assertEquals("task-123", state.getLastDeletedTaskId());
        assertEquals("Task deleted successfully", state.getSuccessMessage());
    }

    @Test
    void testEmptyStringHandling() {
        // Test that empty strings are preserved (not converted to null)
        state.setLastDeletedTaskId("");
        state.setSuccessMessage("");
        state.setError("");
        state.setPendingDeleteTaskId("");
        state.setPendingDeleteTaskName("");

        assertEquals("", state.getLastDeletedTaskId());
        assertEquals("", state.getSuccessMessage());
        assertEquals("", state.getError());
        assertEquals("", state.getPendingDeleteTaskId());
        assertEquals("", state.getPendingDeleteTaskName());
    }

    @Test
    void testBooleanToggling() {
        // Test multiple toggles of showWarningDialog
        boolean[] testValues = {true, false, true, false, true};
        
        for (boolean value : testValues) {
            state.setShowWarningDialog(value);
            assertEquals(value, state.isShowWarningDialog());
        }
    }
}