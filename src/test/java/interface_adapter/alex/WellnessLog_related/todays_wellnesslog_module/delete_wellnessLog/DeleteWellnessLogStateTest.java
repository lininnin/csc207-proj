package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteWellnessLogStateTest {

    private DeleteWellnessLogState state;

    @BeforeEach
    void setUp() {
        state = new DeleteWellnessLogState();
    }

    @Test
    void testConstructorInitializesWithDefaults() {
        assertEquals("", state.getDeletedLogId());
        assertEquals("", state.getDeleteError());
    }

    @Test
    void testDeletedLogIdSetterAndGetter() {
        String logId = "wellness-log-123";
        state.setDeletedLogId(logId);
        assertEquals(logId, state.getDeletedLogId());
    }

    @Test
    void testDeleteErrorSetterAndGetter() {
        String error = "Failed to delete wellness log entry";
        state.setDeleteError(error);
        assertEquals(error, state.getDeleteError());
    }

    @Test
    void testSetDeletedLogIdWithNull() {
        state.setDeletedLogId(null);
        assertNull(state.getDeletedLogId());
    }

    @Test
    void testSetDeleteErrorWithNull() {
        state.setDeleteError(null);
        assertNull(state.getDeleteError());
    }

    @Test
    void testSetDeletedLogIdWithEmpty() {
        state.setDeletedLogId("");
        assertEquals("", state.getDeletedLogId());
    }

    @Test
    void testSetDeleteErrorWithEmpty() {
        state.setDeleteError("");
        assertEquals("", state.getDeleteError());
    }

    @Test
    void testFieldsIndependence() {
        // Set both fields to different values
        state.setDeletedLogId("log-456");
        state.setDeleteError("Some error occurred");

        // Verify they are independent
        assertEquals("log-456", state.getDeletedLogId());
        assertEquals("Some error occurred", state.getDeleteError());

        // Change one and verify the other remains unchanged
        state.setDeletedLogId("log-789");
        assertEquals("log-789", state.getDeletedLogId());
        assertEquals("Some error occurred", state.getDeleteError());
    }

    @Test
    void testSetDeletedLogIdWithUUID() {
        String uuidLogId = "550e8400-e29b-41d4-a716-446655440000";
        state.setDeletedLogId(uuidLogId);
        assertEquals(uuidLogId, state.getDeletedLogId());
    }

    @Test
    void testSetDeleteErrorWithLongMessage() {
        String longError = "This is a very long error message that describes in detail what went wrong during the deletion process of the wellness log entry";
        state.setDeleteError(longError);
        assertEquals(longError, state.getDeleteError());
    }

    @Test
    void testMultipleSetOperations() {
        // First set
        state.setDeletedLogId("log-001");
        state.setDeleteError("Error 1");
        assertEquals("log-001", state.getDeletedLogId());
        assertEquals("Error 1", state.getDeleteError());

        // Second set
        state.setDeletedLogId("log-002");
        state.setDeleteError("Error 2");
        assertEquals("log-002", state.getDeletedLogId());
        assertEquals("Error 2", state.getDeleteError());

        // Third set with null and empty
        state.setDeletedLogId(null);
        state.setDeleteError("");
        assertNull(state.getDeletedLogId());
        assertEquals("", state.getDeleteError());
    }
}