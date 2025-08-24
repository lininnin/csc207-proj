package interface_adapter.alex.event_related.available_event_module.delete_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeletedEventStateTest {

    private DeletedEventState state;

    @BeforeEach
    void setUp() {
        state = new DeletedEventState();
    }

    @Test
    void testDefaultConstructor() {
        assertNull(state.getDeletedEventId());
        assertNull(state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully());
        assertNull(state.getDeleteError());
    }

    @Test
    void testDeletedEventIdSetterAndGetter() {
        String eventId = "event-123";
        state.setDeletedEventId(eventId);
        assertEquals(eventId, state.getDeletedEventId());
    }

    @Test
    void testDeletedEventIdWithNull() {
        state.setDeletedEventId(null);
        assertNull(state.getDeletedEventId());
    }

    @Test
    void testDeletedEventIdWithEmptyString() {
        state.setDeletedEventId("");
        assertEquals("", state.getDeletedEventId());
    }

    @Test
    void testDeletedEventIdWithWhitespace() {
        state.setDeletedEventId("  event-456  ");
        assertEquals("  event-456  ", state.getDeletedEventId());
    }

    @Test
    void testDeletedEventNameSetterAndGetter() {
        String eventName = "Meeting with Team";
        state.setDeletedEventName(eventName);
        assertEquals(eventName, state.getDeletedEventName());
    }

    @Test
    void testDeletedEventNameWithNull() {
        state.setDeletedEventName(null);
        assertNull(state.getDeletedEventName());
    }

    @Test
    void testDeletedEventNameWithEmptyString() {
        state.setDeletedEventName("");
        assertEquals("", state.getDeletedEventName());
    }

    @Test
    void testDeletedEventNameWithSpecialCharacters() {
        String specialName = "Event @#$%^&*()_+";
        state.setDeletedEventName(specialName);
        assertEquals(specialName, state.getDeletedEventName());
    }

    @Test
    void testIsDeletedSuccessfullySetterAndGetter() {
        // Test setting to true
        state.setDeletedSuccessfully(true);
        assertTrue(state.isDeletedSuccessfully());

        // Test setting to false
        state.setDeletedSuccessfully(false);
        assertFalse(state.isDeletedSuccessfully());
    }

    @Test
    void testDeleteErrorSetterAndGetter() {
        String errorMessage = "Event not found in database";
        state.setDeleteError(errorMessage);
        assertEquals(errorMessage, state.getDeleteError());
    }

    @Test
    void testDeleteErrorWithNull() {
        state.setDeleteError(null);
        assertNull(state.getDeleteError());
    }

    @Test
    void testDeleteErrorWithEmptyString() {
        state.setDeleteError("");
        assertEquals("", state.getDeleteError());
    }

    @Test
    void testDeleteErrorWithLongMessage() {
        String longError = "A very long error message that describes in detail what went wrong during the deletion process and why the operation could not be completed successfully due to various system constraints and validation failures";
        state.setDeleteError(longError);
        assertEquals(longError, state.getDeleteError());
    }

    @Test
    void testAllFieldsIndependence() {
        // Set all fields to different values
        state.setDeletedEventId("test-id");
        state.setDeletedEventName("Test Event");
        state.setDeletedSuccessfully(true);
        state.setDeleteError("Test Error");

        // Verify they are all independent and correct
        assertEquals("test-id", state.getDeletedEventId());
        assertEquals("Test Event", state.getDeletedEventName());
        assertTrue(state.isDeletedSuccessfully());
        assertEquals("Test Error", state.getDeleteError());

        // Change one and verify others remain unchanged
        state.setDeletedSuccessfully(false);
        assertEquals("test-id", state.getDeletedEventId()); // Unchanged
        assertEquals("Test Event", state.getDeletedEventName()); // Unchanged
        assertFalse(state.isDeletedSuccessfully()); // Changed
        assertEquals("Test Error", state.getDeleteError()); // Unchanged
    }

    @Test
    void testSuccessfulDeletionScenario() {
        // Simulate a successful deletion
        state.setDeletedEventId("success-event-789");
        state.setDeletedEventName("Workshop on Testing");
        state.setDeletedSuccessfully(true);
        state.setDeleteError(null);

        // Verify successful deletion state
        assertEquals("success-event-789", state.getDeletedEventId());
        assertEquals("Workshop on Testing", state.getDeletedEventName());
        assertTrue(state.isDeletedSuccessfully());
        assertNull(state.getDeleteError());
    }

    @Test
    void testFailedDeletionScenario() {
        // Simulate a failed deletion
        state.setDeletedEventId("failed-event-456");
        state.setDeletedEventName(null); // Usually null for failed deletions
        state.setDeletedSuccessfully(false);
        state.setDeleteError("Event is currently being used and cannot be deleted");

        // Verify failed deletion state
        assertEquals("failed-event-456", state.getDeletedEventId());
        assertNull(state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully());
        assertEquals("Event is currently being used and cannot be deleted", state.getDeleteError());
    }

    @Test
    void testMultipleStateChanges() {
        // First state
        state.setDeletedEventId("event-001");
        state.setDeletedSuccessfully(true);
        assertEquals("event-001", state.getDeletedEventId());
        assertTrue(state.isDeletedSuccessfully());

        // Second state
        state.setDeletedEventId("event-002");
        state.setDeletedSuccessfully(false);
        state.setDeleteError("Permission denied");
        assertEquals("event-002", state.getDeletedEventId());
        assertFalse(state.isDeletedSuccessfully());
        assertEquals("Permission denied", state.getDeleteError());

        // Third state
        state.setDeletedEventName("Updated Event Name");
        state.setDeleteError(null);
        assertEquals("event-002", state.getDeletedEventId()); // Previous value retained
        assertEquals("Updated Event Name", state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully()); // Previous value retained
        assertNull(state.getDeleteError()); // Updated to null
    }

    @Test
    void testStateWithComplexData() {
        // Set state with complex/realistic data
        String complexId = "evt_2023_conference_ai_ml_workshop_session_3";
        String complexName = "AI/ML Workshop: Session 3 - Advanced Neural Networks & Deep Learning";
        String complexError = "Failed to delete event: Event has 15 registered participants and 3 associated materials. Please remove dependencies before deletion.";

        state.setDeletedEventId(complexId);
        state.setDeletedEventName(complexName);
        state.setDeletedSuccessfully(false);
        state.setDeleteError(complexError);

        // Verify complex data is handled correctly
        assertEquals(complexId, state.getDeletedEventId());
        assertEquals(complexName, state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully());
        assertEquals(complexError, state.getDeleteError());
    }

    @Test
    void testStateConsistencyAfterMultipleOperations() {
        // Perform multiple operations and verify consistency
        for (int i = 0; i < 10; i++) {
            state.setDeletedEventId("event-" + i);
            state.setDeletedEventName("Event " + i);
            state.setDeletedSuccessfully(i % 2 == 0); // Even numbers succeed
            state.setDeleteError(i % 2 == 0 ? null : "Error " + i);

            // Verify current state
            assertEquals("event-" + i, state.getDeletedEventId());
            assertEquals("Event " + i, state.getDeletedEventName());
            assertEquals(i % 2 == 0, state.isDeletedSuccessfully());
            assertEquals(i % 2 == 0 ? null : "Error " + i, state.getDeleteError());
        }
    }
}