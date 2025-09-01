package interface_adapter.alex.event_related.todays_events_module.edit_todays_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditTodaysEventStateTest {

    private EditTodaysEventState state;

    @BeforeEach
    void setUp() {
        state = new EditTodaysEventState();
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("", state.getEventId());
        assertEquals("", state.getDueDate());
        assertNull(state.getDueDateError());
        assertNull(state.getEditError());
    }

    @Test
    void testEventIdSetterAndGetter() {
        String eventId = "test-event-123";
        state.setEventId(eventId);
        assertEquals(eventId, state.getEventId());
    }

    @Test
    void testEventIdWithNull() {
        state.setEventId(null);
        assertNull(state.getEventId());
    }

    @Test
    void testEventIdWithEmptyString() {
        state.setEventId("");
        assertEquals("", state.getEventId());
    }

    @Test
    void testEventIdWithComplexId() {
        String complexId = "event_complex-id@#$%_789";
        state.setEventId(complexId);
        assertEquals(complexId, state.getEventId());
    }

    @Test
    void testDueDateSetterAndGetter() {
        String dueDate = "2024-12-25";
        state.setDueDate(dueDate);
        assertEquals(dueDate, state.getDueDate());
    }

    @Test
    void testDueDateWithNull() {
        state.setDueDate(null);
        assertNull(state.getDueDate());
    }

    @Test
    void testDueDateWithEmptyString() {
        state.setDueDate("");
        assertEquals("", state.getDueDate());
    }

    @Test
    void testDueDateWithInvalidFormat() {
        String invalidDate = "invalid-date-format";
        state.setDueDate(invalidDate);
        assertEquals(invalidDate, state.getDueDate());
    }

    @Test
    void testDueDateWithLeapYear() {
        String leapYearDate = "2024-02-29";
        state.setDueDate(leapYearDate);
        assertEquals(leapYearDate, state.getDueDate());
    }

    @Test
    void testDueDateErrorSetterAndGetter() {
        String errorMessage = "Invalid date format";
        state.setDueDateError(errorMessage);
        assertEquals(errorMessage, state.getDueDateError());
    }

    @Test
    void testDueDateErrorWithNull() {
        state.setDueDateError(null);
        assertNull(state.getDueDateError());
    }

    @Test
    void testDueDateErrorWithEmptyString() {
        state.setDueDateError("");
        assertEquals("", state.getDueDateError());
    }

    @Test
    void testDueDateErrorWithLongMessage() {
        String longError = "This is a very long error message that describes in detail what went wrong with the due date validation and why the date format is invalid and cannot be processed by the system";
        state.setDueDateError(longError);
        assertEquals(longError, state.getDueDateError());
    }

    @Test
    void testEditErrorSetterAndGetter() {
        String editError = "Edit failed: event not found";
        state.setEditError(editError);
        assertEquals(editError, state.getEditError());
    }

    @Test
    void testEditErrorWithNull() {
        state.setEditError(null);
        assertNull(state.getEditError());
    }

    @Test
    void testEditErrorWithEmptyString() {
        state.setEditError("");
        assertEquals("", state.getEditError());
    }

    @Test
    void testEditErrorWithComplexMessage() {
        String complexError = "Edit failed: invalid due date or event not found.";
        state.setEditError(complexError);
        assertEquals(complexError, state.getEditError());
    }

    @Test
    void testAllFieldsIndependence() {
        // Set all fields to different values
        state.setEventId("independence-test-id");
        state.setDueDate("2024-06-15");
        state.setDueDateError("Date error");
        state.setEditError("Edit error");

        // Verify they are all independent and correct
        assertEquals("independence-test-id", state.getEventId());
        assertEquals("2024-06-15", state.getDueDate());
        assertEquals("Date error", state.getDueDateError());
        assertEquals("Edit error", state.getEditError());

        // Change one and verify others remain unchanged
        state.setEventId("changed-id");
        assertEquals("changed-id", state.getEventId()); // Changed
        assertEquals("2024-06-15", state.getDueDate()); // Unchanged
        assertEquals("Date error", state.getDueDateError()); // Unchanged
        assertEquals("Edit error", state.getEditError()); // Unchanged
    }

    @Test
    void testSuccessfulEditScenario() {
        // Simulate a successful edit
        state.setEventId("success-event-101");
        state.setDueDate("2024-08-20");
        state.setDueDateError(null);
        state.setEditError(null);

        // Verify successful edit state
        assertEquals("success-event-101", state.getEventId());
        assertEquals("2024-08-20", state.getDueDate());
        assertNull(state.getDueDateError());
        assertNull(state.getEditError());
    }

    @Test
    void testFailedEditWithDueDateError() {
        // Simulate a failed edit due to date validation
        state.setEventId("failed-event-202");
        state.setDueDate("invalid-date");
        state.setDueDateError("Invalid date format. Please use yyyy-MM-dd format.");
        state.setEditError(null);

        // Verify failed edit state
        assertEquals("failed-event-202", state.getEventId());
        assertEquals("invalid-date", state.getDueDate());
        assertEquals("Invalid date format. Please use yyyy-MM-dd format.", state.getDueDateError());
        assertNull(state.getEditError());
    }

    @Test
    void testFailedEditWithGeneralError() {
        // Simulate a failed edit due to general error
        state.setEventId("failed-event-303");
        state.setDueDate("2024-09-15");
        state.setDueDateError(null);
        state.setEditError("Edit failed: invalid due date or event not found.");

        // Verify failed edit state
        assertEquals("failed-event-303", state.getEventId());
        assertEquals("2024-09-15", state.getDueDate());
        assertNull(state.getDueDateError());
        assertEquals("Edit failed: invalid due date or event not found.", state.getEditError());
    }

    @Test
    void testMultipleStateChanges() {
        // First state
        state.setEventId("event-001");
        state.setDueDate("2024-01-01");
        assertEquals("event-001", state.getEventId());
        assertEquals("2024-01-01", state.getDueDate());

        // Second state
        state.setEventId("event-002");
        state.setDueDate("2024-02-02");
        state.setEditError("Some error");
        assertEquals("event-002", state.getEventId());
        assertEquals("2024-02-02", state.getDueDate());
        assertEquals("Some error", state.getEditError());

        // Third state
        state.setDueDateError("Date validation error");
        state.setEditError(null);
        assertEquals("event-002", state.getEventId()); // Previous value retained
        assertEquals("2024-02-02", state.getDueDate()); // Previous value retained
        assertEquals("Date validation error", state.getDueDateError()); // Updated
        assertNull(state.getEditError()); // Updated to null
    }

    @Test
    void testStateWithComplexData() {
        // Set state with complex/realistic data
        String complexId = "event_2024_conference_ai_ml_workshop_session_3";
        String complexDate = "2024-12-31";
        String complexDateError = "Date must be within the next 6 months. Selected date is too far in the future.";
        String complexEditError = "Failed to edit event: Event is locked by another user or has been deleted. Please refresh and try again.";

        state.setEventId(complexId);
        state.setDueDate(complexDate);
        state.setDueDateError(complexDateError);
        state.setEditError(complexEditError);

        // Verify complex data is handled correctly
        assertEquals(complexId, state.getEventId());
        assertEquals(complexDate, state.getDueDate());
        assertEquals(complexDateError, state.getDueDateError());
        assertEquals(complexEditError, state.getEditError());
    }

    @Test
    void testStateConsistencyAfterMultipleOperations() {
        // Perform multiple operations and verify consistency
        for (int i = 0; i < 10; i++) {
            state.setEventId("event-" + i);
            state.setDueDate("2024-" + String.format("%02d", (i % 12) + 1) + "-01");
            state.setDueDateError(i % 3 == 0 ? "Error " + i : null);
            state.setEditError(i % 4 == 0 ? "Edit error " + i : null);

            // Verify current state
            assertEquals("event-" + i, state.getEventId());
            assertEquals("2024-" + String.format("%02d", (i % 12) + 1) + "-01", state.getDueDate());
            assertEquals(i % 3 == 0 ? "Error " + i : null, state.getDueDateError());
            assertEquals(i % 4 == 0 ? "Edit error " + i : null, state.getEditError());
        }
    }

    @Test
    void testDefaultInitializationValues() {
        // Test that all fields have proper default values
        EditTodaysEventState freshState = new EditTodaysEventState();
        
        assertEquals("", freshState.getEventId()); // Empty string, not null
        assertEquals("", freshState.getDueDate()); // Empty string, not null
        assertNull(freshState.getDueDateError()); // null as expected
        assertNull(freshState.getEditError()); // null as expected
    }

    @Test
    void testFieldsAfterReset() {
        // Set some values
        state.setEventId("reset-test");
        state.setDueDate("2024-05-15");
        state.setDueDateError("Some error");
        state.setEditError("Another error");

        // Reset to default-like values
        state.setEventId("");
        state.setDueDate("");
        state.setDueDateError(null);
        state.setEditError(null);

        // Verify reset state
        assertEquals("", state.getEventId());
        assertEquals("", state.getDueDate());
        assertNull(state.getDueDateError());
        assertNull(state.getEditError());
    }

    @Test
    void testBothErrorFieldsSimultaneously() {
        // Test having both error fields set at the same time
        state.setEventId("error-test-event");
        state.setDueDate("invalid-date");
        state.setDueDateError("Date format is invalid");
        state.setEditError("General edit failure occurred");

        assertEquals("error-test-event", state.getEventId());
        assertEquals("invalid-date", state.getDueDate());
        assertEquals("Date format is invalid", state.getDueDateError());
        assertEquals("General edit failure occurred", state.getEditError());
    }

    @Test
    void testClearingIndividualFields() {
        // Set all fields
        state.setEventId("clear-test");
        state.setDueDate("2024-07-20");
        state.setDueDateError("Date error");
        state.setEditError("Edit error");

        // Clear each field individually and verify others remain
        state.setEventId("");
        assertEquals("", state.getEventId());
        assertEquals("2024-07-20", state.getDueDate());
        assertEquals("Date error", state.getDueDateError());
        assertEquals("Edit error", state.getEditError());

        state.setDueDate("");
        assertEquals("", state.getEventId());
        assertEquals("", state.getDueDate());
        assertEquals("Date error", state.getDueDateError());
        assertEquals("Edit error", state.getEditError());

        state.setDueDateError(null);
        assertEquals("", state.getEventId());
        assertEquals("", state.getDueDate());
        assertNull(state.getDueDateError());
        assertEquals("Edit error", state.getEditError());

        state.setEditError(null);
        assertEquals("", state.getEventId());
        assertEquals("", state.getDueDate());
        assertNull(state.getDueDateError());
        assertNull(state.getEditError());
    }
}