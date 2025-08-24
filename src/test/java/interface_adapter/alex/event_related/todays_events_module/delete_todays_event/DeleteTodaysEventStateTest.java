package interface_adapter.alex.event_related.todays_events_module.delete_todays_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTodaysEventStateTest {

    private DeleteTodaysEventState state;

    @BeforeEach
    void setUp() {
        state = new DeleteTodaysEventState();
    }

    @Test
    void testDefaultConstructor() {
        assertNull(state.getDeletedEventId());
        assertNull(state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully());
        assertNull(state.getDeleteError());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String eventId = "param-event-123";
        String eventName = "Parametrized Event";
        boolean success = true;
        String error = null;

        // Act
        DeleteTodaysEventState paramState = new DeleteTodaysEventState(eventId, eventName, success, error);

        // Assert
        assertEquals(eventId, paramState.getDeletedEventId());
        assertEquals(eventName, paramState.getDeletedEventName());
        assertTrue(paramState.isDeletedSuccessfully());
        assertNull(paramState.getDeleteError());
    }

    @Test
    void testParameterizedConstructorWithError() {
        // Arrange
        String eventId = "error-event-456";
        String eventName = "Error Event";
        boolean success = false;
        String error = "Failed to delete event";

        // Act
        DeleteTodaysEventState paramState = new DeleteTodaysEventState(eventId, eventName, success, error);

        // Assert
        assertEquals(eventId, paramState.getDeletedEventId());
        assertEquals(eventName, paramState.getDeletedEventName());
        assertFalse(paramState.isDeletedSuccessfully());
        assertEquals(error, paramState.getDeleteError());
    }

    @Test
    void testDeletedEventIdSetterAndGetter() {
        String eventId = "todays-event-789";
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
        state.setDeletedEventId("  todays-event-whitespace  ");
        assertEquals("  todays-event-whitespace  ", state.getDeletedEventId());
    }

    @Test
    void testDeletedEventNameSetterAndGetter() {
        String eventName = "Today's Meeting with Team";
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
        String specialName = "Today's Event @#$%^&*()_+";
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
        String errorMessage = "Today's event not found in database";
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
        String longError = "A very long error message that describes in detail what went wrong during the today's event deletion process and why the operation could not be completed successfully due to various system constraints and validation failures in the today's events module";
        state.setDeleteError(longError);
        assertEquals(longError, state.getDeleteError());
    }

    @Test
    void testAllFieldsIndependence() {
        // Set all fields to different values
        state.setDeletedEventId("independence-test-id");
        state.setDeletedEventName("Independence Test Event");
        state.setDeletedSuccessfully(true);
        state.setDeleteError("Independence Test Error");

        // Verify they are all independent and correct
        assertEquals("independence-test-id", state.getDeletedEventId());
        assertEquals("Independence Test Event", state.getDeletedEventName());
        assertTrue(state.isDeletedSuccessfully());
        assertEquals("Independence Test Error", state.getDeleteError());

        // Change one and verify others remain unchanged
        state.setDeletedSuccessfully(false);
        assertEquals("independence-test-id", state.getDeletedEventId()); // Unchanged
        assertEquals("Independence Test Event", state.getDeletedEventName()); // Unchanged
        assertFalse(state.isDeletedSuccessfully()); // Changed
        assertEquals("Independence Test Error", state.getDeleteError()); // Unchanged
    }

    @Test
    void testSuccessfulDeletionScenario() {
        // Simulate a successful today's event deletion
        state.setDeletedEventId("success-todays-event-101");
        state.setDeletedEventName("Today's Workshop on Testing");
        state.setDeletedSuccessfully(true);
        state.setDeleteError(null);

        // Verify successful deletion state
        assertEquals("success-todays-event-101", state.getDeletedEventId());
        assertEquals("Today's Workshop on Testing", state.getDeletedEventName());
        assertTrue(state.isDeletedSuccessfully());
        assertNull(state.getDeleteError());
    }

    @Test
    void testFailedDeletionScenario() {
        // Simulate a failed today's event deletion
        state.setDeletedEventId("failed-todays-event-202");
        state.setDeletedEventName("Failed Today's Event");
        state.setDeletedSuccessfully(false);
        state.setDeleteError("Today's event is currently being used and cannot be deleted");

        // Verify failed deletion state
        assertEquals("failed-todays-event-202", state.getDeletedEventId());
        assertEquals("Failed Today's Event", state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully());
        assertEquals("Today's event is currently being used and cannot be deleted", state.getDeleteError());
    }

    @Test
    void testMultipleStateChanges() {
        // First state
        state.setDeletedEventId("todays-event-001");
        state.setDeletedSuccessfully(true);
        assertEquals("todays-event-001", state.getDeletedEventId());
        assertTrue(state.isDeletedSuccessfully());

        // Second state
        state.setDeletedEventId("todays-event-002");
        state.setDeletedSuccessfully(false);
        state.setDeleteError("Permission denied");
        assertEquals("todays-event-002", state.getDeletedEventId());
        assertFalse(state.isDeletedSuccessfully());
        assertEquals("Permission denied", state.getDeleteError());

        // Third state
        state.setDeletedEventName("Updated Today's Event Name");
        state.setDeleteError(null);
        assertEquals("todays-event-002", state.getDeletedEventId()); // Previous value retained
        assertEquals("Updated Today's Event Name", state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully()); // Previous value retained
        assertNull(state.getDeleteError()); // Updated to null
    }

    @Test
    void testStateWithComplexData() {
        // Set state with complex/realistic data
        String complexId = "todays_evt_2023_conference_ai_ml_workshop_session_3";
        String complexName = "Today's AI/ML Workshop: Session 3 - Advanced Neural Networks & Deep Learning";
        String complexError = "Failed to delete today's event: Event has 15 registered participants and 3 associated materials. Please remove dependencies before deletion.";

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
            state.setDeletedEventId("todays-event-" + i);
            state.setDeletedEventName("Today's Event " + i);
            state.setDeletedSuccessfully(i % 2 == 0); // Even numbers succeed
            state.setDeleteError(i % 2 == 0 ? null : "Error " + i);

            // Verify current state
            assertEquals("todays-event-" + i, state.getDeletedEventId());
            assertEquals("Today's Event " + i, state.getDeletedEventName());
            assertEquals(i % 2 == 0, state.isDeletedSuccessfully());
            assertEquals(i % 2 == 0 ? null : "Error " + i, state.getDeleteError());
        }
    }

    @Test
    void testToString() {
        // Arrange
        state.setDeletedEventId("toString-event-123");
        state.setDeletedEventName("ToString Test Event");
        state.setDeletedSuccessfully(true);
        state.setDeleteError(null);

        // Act
        String result = state.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("toString-event-123"));
        assertTrue(result.contains("ToString Test Event"));
        assertTrue(result.contains("true"));
        assertTrue(result.contains("DeleteTodaysEventState"));
    }

    @Test
    void testToStringWithNullValues() {
        // Act
        String result = state.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("null"));
        assertTrue(result.contains("false"));
        assertTrue(result.contains("DeleteTodaysEventState"));
    }
}