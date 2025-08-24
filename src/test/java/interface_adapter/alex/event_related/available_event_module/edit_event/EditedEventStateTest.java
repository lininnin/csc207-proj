package interface_adapter.alex.event_related.available_event_module.edit_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditedEventStateTest {

    private EditedEventState state;

    @BeforeEach
    void setUp() {
        state = new EditedEventState();
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("", state.getEventId());
        assertEquals("", state.getName());
        assertNull(state.getNameError());
        assertEquals("", state.getCategory());
        assertNull(state.getCategoryError());
        assertEquals("", state.getDescription());
        assertNull(state.getDescriptionError());
        assertFalse(state.isOneTime());
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
    void testNameSetterAndGetter() {
        String name = "Updated Event Name";
        state.setName(name);
        assertEquals(name, state.getName());
    }

    @Test
    void testNameWithNull() {
        state.setName(null);
        assertNull(state.getName());
    }

    @Test
    void testNameWithEmptyString() {
        state.setName("");
        assertEquals("", state.getName());
    }

    @Test
    void testNameWithSpecialCharacters() {
        String specialName = "Événement Spécial & Test!";
        state.setName(specialName);
        assertEquals(specialName, state.getName());
    }

    @Test
    void testNameErrorSetterAndGetter() {
        String nameError = "Name is required";
        state.setNameError(nameError);
        assertEquals(nameError, state.getNameError());
    }

    @Test
    void testNameErrorWithNull() {
        state.setNameError(null);
        assertNull(state.getNameError());
    }

    @Test
    void testNameErrorWithEmptyString() {
        state.setNameError("");
        assertEquals("", state.getNameError());
    }

    @Test
    void testCategorySetterAndGetter() {
        String category = "Work";
        state.setCategory(category);
        assertEquals(category, state.getCategory());
    }

    @Test
    void testCategoryWithNull() {
        state.setCategory(null);
        assertNull(state.getCategory());
    }

    @Test
    void testCategoryWithEmptyString() {
        state.setCategory("");
        assertEquals("", state.getCategory());
    }

    @Test
    void testCategoryWithSpecialCharacters() {
        String specialCategory = "Work-Life_Balance.2024";
        state.setCategory(specialCategory);
        assertEquals(specialCategory, state.getCategory());
    }

    @Test
    void testCategoryErrorSetterAndGetter() {
        String categoryError = "Invalid category selected";
        state.setCategoryError(categoryError);
        assertEquals(categoryError, state.getCategoryError());
    }

    @Test
    void testCategoryErrorWithNull() {
        state.setCategoryError(null);
        assertNull(state.getCategoryError());
    }

    @Test
    void testCategoryErrorWithEmptyString() {
        state.setCategoryError("");
        assertEquals("", state.getCategoryError());
    }

    @Test
    void testDescriptionSetterAndGetter() {
        String description = "This is a detailed description of the event";
        state.setDescription(description);
        assertEquals(description, state.getDescription());
    }

    @Test
    void testDescriptionWithNull() {
        state.setDescription(null);
        assertNull(state.getDescription());
    }

    @Test
    void testDescriptionWithEmptyString() {
        state.setDescription("");
        assertEquals("", state.getDescription());
    }

    @Test
    void testDescriptionWithLongText() {
        String longDescription = "This is a very long description ".repeat(20);
        state.setDescription(longDescription);
        assertEquals(longDescription, state.getDescription());
    }

    @Test
    void testDescriptionWithNewlines() {
        String descriptionWithNewlines = "Line 1\nLine 2\nLine 3";
        state.setDescription(descriptionWithNewlines);
        assertEquals(descriptionWithNewlines, state.getDescription());
    }

    @Test
    void testDescriptionErrorSetterAndGetter() {
        String descriptionError = "Description too long";
        state.setDescriptionError(descriptionError);
        assertEquals(descriptionError, state.getDescriptionError());
    }

    @Test
    void testDescriptionErrorWithNull() {
        state.setDescriptionError(null);
        assertNull(state.getDescriptionError());
    }

    @Test
    void testDescriptionErrorWithEmptyString() {
        state.setDescriptionError("");
        assertEquals("", state.getDescriptionError());
    }

    @Test
    void testOneTimeSetterAndGetter() {
        state.setOneTime(true);
        assertTrue(state.isOneTime());

        state.setOneTime(false);
        assertFalse(state.isOneTime());
    }

    @Test
    void testOneTimeDefaultValue() {
        assertFalse(state.isOneTime());
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
        String complexError = "Edit operation failed: validation errors in name, category, and description fields";
        state.setEditError(complexError);
        assertEquals(complexError, state.getEditError());
    }

    @Test
    void testAllFieldsIndependence() {
        // Set all fields to different values
        state.setEventId("independence-test-id");
        state.setName("Independence Test Event");
        state.setNameError("Name error");
        state.setCategory("Test Category");
        state.setCategoryError("Category error");
        state.setDescription("Test description");
        state.setDescriptionError("Description error");
        state.setOneTime(true);
        state.setEditError("Edit error");

        // Verify they are all independent and correct
        assertEquals("independence-test-id", state.getEventId());
        assertEquals("Independence Test Event", state.getName());
        assertEquals("Name error", state.getNameError());
        assertEquals("Test Category", state.getCategory());
        assertEquals("Category error", state.getCategoryError());
        assertEquals("Test description", state.getDescription());
        assertEquals("Description error", state.getDescriptionError());
        assertTrue(state.isOneTime());
        assertEquals("Edit error", state.getEditError());

        // Change one field and verify others remain unchanged
        state.setEventId("changed-id");
        assertEquals("changed-id", state.getEventId()); // Changed
        assertEquals("Independence Test Event", state.getName()); // Unchanged
        assertEquals("Name error", state.getNameError()); // Unchanged
        assertEquals("Test Category", state.getCategory()); // Unchanged
        assertEquals("Category error", state.getCategoryError()); // Unchanged
        assertEquals("Test description", state.getDescription()); // Unchanged
        assertEquals("Description error", state.getDescriptionError()); // Unchanged
        assertTrue(state.isOneTime()); // Unchanged
        assertEquals("Edit error", state.getEditError()); // Unchanged
    }

    @Test
    void testSuccessfulEditScenario() {
        // Simulate a successful edit
        state.setEventId("success-event-101");
        state.setName("Successfully Edited Event");
        state.setCategory("Work");
        state.setDescription("Successfully updated description");
        state.setOneTime(true);
        // Clear all errors
        state.setNameError(null);
        state.setCategoryError(null);
        state.setDescriptionError(null);
        state.setEditError(null);

        // Verify successful edit state
        assertEquals("success-event-101", state.getEventId());
        assertEquals("Successfully Edited Event", state.getName());
        assertEquals("Work", state.getCategory());
        assertEquals("Successfully updated description", state.getDescription());
        assertTrue(state.isOneTime());
        assertNull(state.getNameError());
        assertNull(state.getCategoryError());
        assertNull(state.getDescriptionError());
        assertNull(state.getEditError());
    }

    @Test
    void testFailedEditWithValidationErrors() {
        // Simulate a failed edit with multiple validation errors
        state.setEventId("failed-event-202");
        state.setName(""); // Invalid name
        state.setNameError("Name cannot be empty");
        state.setCategory("InvalidCategory");
        state.setCategoryError("Category does not exist");
        state.setDescription("Short"); // Potentially invalid
        state.setDescriptionError("Description too short");
        state.setOneTime(false);
        state.setEditError(null); // No general edit error

        // Verify failed edit state with validation errors
        assertEquals("failed-event-202", state.getEventId());
        assertEquals("", state.getName());
        assertEquals("Name cannot be empty", state.getNameError());
        assertEquals("InvalidCategory", state.getCategory());
        assertEquals("Category does not exist", state.getCategoryError());
        assertEquals("Short", state.getDescription());
        assertEquals("Description too short", state.getDescriptionError());
        assertFalse(state.isOneTime());
        assertNull(state.getEditError());
    }

    @Test
    void testFailedEditWithGeneralError() {
        // Simulate a failed edit due to general error (e.g., event not found)
        state.setEventId("failed-event-303");
        state.setName("Some Event Name");
        state.setCategory("Some Category");
        state.setDescription("Some description");
        state.setOneTime(true);
        // Clear field-specific errors
        state.setNameError(null);
        state.setCategoryError(null);
        state.setDescriptionError(null);
        state.setEditError("Edit failed: event not found or access denied");

        // Verify failed edit state with general error
        assertEquals("failed-event-303", state.getEventId());
        assertEquals("Some Event Name", state.getName());
        assertEquals("Some Category", state.getCategory());
        assertEquals("Some description", state.getDescription());
        assertTrue(state.isOneTime());
        assertNull(state.getNameError());
        assertNull(state.getCategoryError());
        assertNull(state.getDescriptionError());
        assertEquals("Edit failed: event not found or access denied", state.getEditError());
    }

    @Test
    void testMultipleStateChanges() {
        // First state
        state.setEventId("event-001");
        state.setName("First Event");
        state.setCategory("Category1");
        assertEquals("event-001", state.getEventId());
        assertEquals("First Event", state.getName());
        assertEquals("Category1", state.getCategory());

        // Second state
        state.setEventId("event-002");
        state.setName("Second Event");
        state.setCategory("Category2");
        state.setDescription("Second description");
        state.setOneTime(true);
        assertEquals("event-002", state.getEventId());
        assertEquals("Second Event", state.getName());
        assertEquals("Category2", state.getCategory());
        assertEquals("Second description", state.getDescription());
        assertTrue(state.isOneTime());

        // Third state with errors
        state.setNameError("Name validation error");
        state.setEditError("General edit error");
        state.setOneTime(false);
        assertEquals("event-002", state.getEventId()); // Previous value retained
        assertEquals("Second Event", state.getName()); // Previous value retained
        assertEquals("Category2", state.getCategory()); // Previous value retained
        assertEquals("Second description", state.getDescription()); // Previous value retained
        assertEquals("Name validation error", state.getNameError()); // Updated
        assertEquals("General edit error", state.getEditError()); // Updated
        assertFalse(state.isOneTime()); // Updated
    }

    @Test
    void testStateWithComplexData() {
        // Set state with complex/realistic data
        String complexId = "event_2024_annual_company_retreat_planning_session_v2";
        String complexName = "Annual Company Retreat 2024: Planning & Coordination Session";
        String complexCategory = "Corporate_Events-Strategic_Planning";
        String complexDescription = "Comprehensive planning session for the 2024 annual company retreat.\n\nTopics to cover:\n- Venue selection\n- Activity planning\n- Budget allocation\n- Timeline coordination";
        String complexNameError = "Event name contains special characters that are not allowed in the system database";
        String complexEditError = "Edit operation failed: Event is currently locked by another user (John Smith) and cannot be modified until released. Please try again in 15 minutes.";

        state.setEventId(complexId);
        state.setName(complexName);
        state.setCategory(complexCategory);
        state.setDescription(complexDescription);
        state.setOneTime(true);
        state.setNameError(complexNameError);
        state.setEditError(complexEditError);

        // Verify complex data is handled correctly
        assertEquals(complexId, state.getEventId());
        assertEquals(complexName, state.getName());
        assertEquals(complexCategory, state.getCategory());
        assertEquals(complexDescription, state.getDescription());
        assertTrue(state.isOneTime());
        assertEquals(complexNameError, state.getNameError());
        assertEquals(complexEditError, state.getEditError());
    }

    @Test
    void testStateConsistencyAfterMultipleOperations() {
        // Perform multiple operations and verify consistency
        for (int i = 0; i < 10; i++) {
            state.setEventId("event-" + i);
            state.setName("Event Name " + i);
            state.setCategory("Category " + (i % 3));
            state.setDescription("Description for event " + i);
            state.setOneTime(i % 2 == 0);
            state.setNameError(i % 3 == 0 ? "Name Error " + i : null);
            state.setCategoryError(i % 4 == 0 ? "Category Error " + i : null);
            state.setDescriptionError(i % 5 == 0 ? "Description Error " + i : null);
            state.setEditError(i % 6 == 0 ? "Edit Error " + i : null);

            // Verify current state
            assertEquals("event-" + i, state.getEventId());
            assertEquals("Event Name " + i, state.getName());
            assertEquals("Category " + (i % 3), state.getCategory());
            assertEquals("Description for event " + i, state.getDescription());
            assertEquals(i % 2 == 0, state.isOneTime());
            assertEquals(i % 3 == 0 ? "Name Error " + i : null, state.getNameError());
            assertEquals(i % 4 == 0 ? "Category Error " + i : null, state.getCategoryError());
            assertEquals(i % 5 == 0 ? "Description Error " + i : null, state.getDescriptionError());
            assertEquals(i % 6 == 0 ? "Edit Error " + i : null, state.getEditError());
        }
    }

    @Test
    void testDefaultInitializationValues() {
        // Test that all fields have proper default values
        EditedEventState freshState = new EditedEventState();
        
        assertEquals("", freshState.getEventId()); // Empty string, not null
        assertEquals("", freshState.getName()); // Empty string, not null
        assertNull(freshState.getNameError()); // null as expected
        assertEquals("", freshState.getCategory()); // Empty string, not null
        assertNull(freshState.getCategoryError()); // null as expected
        assertEquals("", freshState.getDescription()); // Empty string, not null
        assertNull(freshState.getDescriptionError()); // null as expected
        assertFalse(freshState.isOneTime()); // false as expected
        assertNull(freshState.getEditError()); // null as expected
    }

    @Test
    void testFieldsAfterReset() {
        // Set some values
        state.setEventId("reset-test");
        state.setName("Reset Test Event");
        state.setCategory("Reset Category");
        state.setDescription("Reset description");
        state.setOneTime(true);
        state.setNameError("Some error");
        state.setCategoryError("Another error");
        state.setDescriptionError("Description error");
        state.setEditError("Edit error");

        // Reset to default-like values
        state.setEventId("");
        state.setName("");
        state.setCategory("");
        state.setDescription("");
        state.setOneTime(false);
        state.setNameError(null);
        state.setCategoryError(null);
        state.setDescriptionError(null);
        state.setEditError(null);

        // Verify reset state
        assertEquals("", state.getEventId());
        assertEquals("", state.getName());
        assertEquals("", state.getCategory());
        assertEquals("", state.getDescription());
        assertFalse(state.isOneTime());
        assertNull(state.getNameError());
        assertNull(state.getCategoryError());
        assertNull(state.getDescriptionError());
        assertNull(state.getEditError());
    }

    @Test
    void testAllErrorFieldsSimultaneously() {
        // Test having all error fields set at the same time
        state.setEventId("error-test-event");
        state.setName("Error Test Event");
        state.setNameError("Name validation failed");
        state.setCategory("ErrorCategory");
        state.setCategoryError("Category validation failed");
        state.setDescription("Error description");
        state.setDescriptionError("Description validation failed");
        state.setEditError("General edit validation failed");

        assertEquals("error-test-event", state.getEventId());
        assertEquals("Error Test Event", state.getName());
        assertEquals("Name validation failed", state.getNameError());
        assertEquals("ErrorCategory", state.getCategory());
        assertEquals("Category validation failed", state.getCategoryError());
        assertEquals("Error description", state.getDescription());
        assertEquals("Description validation failed", state.getDescriptionError());
        assertEquals("General edit validation failed", state.getEditError());
    }

    @Test
    void testClearingIndividualFields() {
        // Set all fields
        state.setEventId("clear-test");
        state.setName("Clear Test Event");
        state.setNameError("Name error");
        state.setCategory("Clear Category");
        state.setCategoryError("Category error");
        state.setDescription("Clear description");
        state.setDescriptionError("Description error");
        state.setOneTime(true);
        state.setEditError("Edit error");

        // Clear each field individually and verify others remain
        state.setEventId("");
        assertEquals("", state.getEventId());
        assertEquals("Clear Test Event", state.getName());
        assertEquals("Name error", state.getNameError());

        state.setName("");
        assertEquals("", state.getEventId());
        assertEquals("", state.getName());
        assertEquals("Name error", state.getNameError());

        state.setNameError(null);
        assertEquals("", state.getEventId());
        assertEquals("", state.getName());
        assertNull(state.getNameError());
        assertEquals("Clear Category", state.getCategory());

        // Continue clearing other fields...
        state.setCategory("");
        state.setCategoryError(null);
        state.setDescription("");
        state.setDescriptionError(null);
        state.setOneTime(false);
        state.setEditError(null);

        // Verify all are cleared
        assertEquals("", state.getEventId());
        assertEquals("", state.getName());
        assertNull(state.getNameError());
        assertEquals("", state.getCategory());
        assertNull(state.getCategoryError());
        assertEquals("", state.getDescription());
        assertNull(state.getDescriptionError());
        assertFalse(state.isOneTime());
        assertNull(state.getEditError());
    }
}