package use_case.alex.event_related.todays_events_module.delete_todays_event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteTodaysEventOutputData.
 * Tests both success and failure constructors and their behaviors.
 */
class DeleteTodaysEventOutputDataTest {

    @Test
    @DisplayName("Should create output data for successful deletion")
    void testSuccessfulDeletion() {
        // Given
        String eventId = "event-123";
        String eventName = "Team Meeting";

        // When
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        // Then
        assertEquals(eventId, outputData.getEventId(), "Event ID should match");
        assertEquals(eventName, outputData.getEventName(), "Event name should match");
        assertTrue(outputData.isDeletionSuccess(), "Deletion should be marked as successful");
        assertNull(outputData.getErrorMessage(), "Error message should be null for successful deletion");
    }

    @Test
    @DisplayName("Should create output data for failed deletion")
    void testFailedDeletion() {
        // Given
        String eventId = "event-123";
        String errorMessage = "Event not found";

        // When
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, errorMessage, false);

        // Then
        assertEquals(eventId, outputData.getEventId(), "Event ID should match");
        assertNull(outputData.getEventName(), "Event name should be null for failed deletion");
        assertFalse(outputData.isDeletionSuccess(), "Deletion should be marked as failed");
        assertEquals(errorMessage, outputData.getErrorMessage(), "Error message should match");
    }

    @Test
    @DisplayName("Should handle null event ID in successful deletion")
    void testSuccessfulDeletionWithNullEventId() {
        // Given
        String eventId = null;
        String eventName = "Team Meeting";

        // When
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        // Then
        assertNull(outputData.getEventId(), "Event ID should be null");
        assertEquals(eventName, outputData.getEventName(), "Event name should be preserved");
        assertTrue(outputData.isDeletionSuccess(), "Deletion should be marked as successful");
        assertNull(outputData.getErrorMessage(), "Error message should be null");
    }

    @Test
    @DisplayName("Should handle null event name in successful deletion")
    void testSuccessfulDeletionWithNullEventName() {
        // Given
        String eventId = "event-123";
        String eventName = null;

        // When
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        // Then
        assertEquals(eventId, outputData.getEventId(), "Event ID should be preserved");
        assertNull(outputData.getEventName(), "Event name should be null");
        assertTrue(outputData.isDeletionSuccess(), "Deletion should be marked as successful");
        assertNull(outputData.getErrorMessage(), "Error message should be null");
    }

    @Test
    @DisplayName("Should handle null event ID in failed deletion")
    void testFailedDeletionWithNullEventId() {
        // Given
        String eventId = null;
        String errorMessage = "Event not found";

        // When
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, errorMessage, false);

        // Then
        assertNull(outputData.getEventId(), "Event ID should be null");
        assertNull(outputData.getEventName(), "Event name should be null");
        assertFalse(outputData.isDeletionSuccess(), "Deletion should be marked as failed");
        assertEquals(errorMessage, outputData.getErrorMessage(), "Error message should be preserved");
    }

    @Test
    @DisplayName("Should handle null error message in failed deletion")
    void testFailedDeletionWithNullErrorMessage() {
        // Given
        String eventId = "event-123";
        String errorMessage = null;

        // When
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, errorMessage, false);

        // Then
        assertEquals(eventId, outputData.getEventId(), "Event ID should be preserved");
        assertNull(outputData.getEventName(), "Event name should be null");
        assertFalse(outputData.isDeletionSuccess(), "Deletion should be marked as failed");
        assertNull(outputData.getErrorMessage(), "Error message should be null");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Test successful deletion with empty strings
        DeleteTodaysEventOutputData successData = new DeleteTodaysEventOutputData("", "");
        assertEquals("", successData.getEventId(), "Empty event ID should be preserved");
        assertEquals("", successData.getEventName(), "Empty event name should be preserved");
        assertTrue(successData.isDeletionSuccess(), "Should be marked as successful");

        // Test failed deletion with empty strings
        DeleteTodaysEventOutputData failureData = new DeleteTodaysEventOutputData("", "", false);
        assertEquals("", failureData.getEventId(), "Empty event ID should be preserved");
        assertEquals("", failureData.getErrorMessage(), "Empty error message should be preserved");
        assertFalse(failureData.isDeletionSuccess(), "Should be marked as failed");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Test successful deletion
        String eventId = "event-123!@#";
        String eventName = "Meeting with émojis 😀";
        DeleteTodaysEventOutputData successData = new DeleteTodaysEventOutputData(eventId, eventName);
        
        assertEquals(eventId, successData.getEventId(), "Special characters in event ID should be preserved");
        assertEquals(eventName, successData.getEventName(), "Special characters in event name should be preserved");

        // Test failed deletion
        String errorMessage = "Error with special chars: !@#$%^&*()";
        DeleteTodaysEventOutputData failureData = new DeleteTodaysEventOutputData(eventId, errorMessage, false);
        
        assertEquals(errorMessage, failureData.getErrorMessage(), "Special characters in error message should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Test with long strings
        String longEventId = "event-" + "x".repeat(100);
        String longEventName = "Very long event name " + "A".repeat(200);
        String longErrorMessage = "Very long error message " + "B".repeat(200);

        // Test successful deletion
        DeleteTodaysEventOutputData successData = new DeleteTodaysEventOutputData(longEventId, longEventName);
        assertEquals(longEventId, successData.getEventId(), "Long event ID should be preserved");
        assertEquals(longEventName, successData.getEventName(), "Long event name should be preserved");

        // Test failed deletion
        DeleteTodaysEventOutputData failureData = new DeleteTodaysEventOutputData(longEventId, longErrorMessage, false);
        assertEquals(longErrorMessage, failureData.getErrorMessage(), "Long error message should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Test successful case
        String eventId = "event-123";
        String eventName = "Meeting";
        DeleteTodaysEventOutputData successData = new DeleteTodaysEventOutputData(eventId, eventName);

        assertEquals(eventId, successData.getEventId(), "Event ID should remain unchanged");
        assertEquals(eventName, successData.getEventName(), "Event name should remain unchanged");
        assertTrue(successData.isDeletionSuccess(), "Success flag should remain unchanged");
        assertNull(successData.getErrorMessage(), "Error message should remain null");

        // Test failed case
        String errorMessage = "Error occurred";
        DeleteTodaysEventOutputData failureData = new DeleteTodaysEventOutputData(eventId, errorMessage, false);

        assertEquals(eventId, failureData.getEventId(), "Event ID should remain unchanged");
        assertNull(failureData.getEventName(), "Event name should remain null");
        assertFalse(failureData.isDeletionSuccess(), "Success flag should remain unchanged");
        assertEquals(errorMessage, failureData.getErrorMessage(), "Error message should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Create different instances
        DeleteTodaysEventOutputData successData = new DeleteTodaysEventOutputData("event-1", "Event 1");
        DeleteTodaysEventOutputData failureData = new DeleteTodaysEventOutputData("event-2", "Error message", false);

        // Test they are distinct
        assertNotSame(successData, failureData, "Should be different object instances");
        assertNotEquals(successData.getEventId(), failureData.getEventId(), "Should have different event IDs");
        assertNotEquals(successData.isDeletionSuccess(), failureData.isDeletionSuccess(), "Should have different success states");
    }

    @Test
    @DisplayName("Should handle whitespace in strings")
    void testWhitespaceHandling() {
        // Test with whitespace (should be preserved, not trimmed like input data)
        String eventIdWithSpaces = "  event-123  ";
        String eventNameWithSpaces = "  Team Meeting  ";
        String errorMessageWithSpaces = "  Error occurred  ";

        // Test successful deletion
        DeleteTodaysEventOutputData successData = new DeleteTodaysEventOutputData(eventIdWithSpaces, eventNameWithSpaces);
        assertEquals(eventIdWithSpaces, successData.getEventId(), "Whitespace in event ID should be preserved");
        assertEquals(eventNameWithSpaces, successData.getEventName(), "Whitespace in event name should be preserved");

        // Test failed deletion
        DeleteTodaysEventOutputData failureData = new DeleteTodaysEventOutputData(eventIdWithSpaces, errorMessageWithSpaces, false);
        assertEquals(errorMessageWithSpaces, failureData.getErrorMessage(), "Whitespace in error message should be preserved");
    }

    @Test
    @DisplayName("Should handle constructor parameter validation")
    void testConstructorParameters() {
        // Both constructors should accept any string values including null
        assertDoesNotThrow(() -> new DeleteTodaysEventOutputData(null, null),
                "Success constructor should accept null parameters");
        
        assertDoesNotThrow(() -> new DeleteTodaysEventOutputData(null, null, false),
                "Failure constructor should accept null parameters");

        assertDoesNotThrow(() -> new DeleteTodaysEventOutputData("", ""),
                "Success constructor should accept empty parameters");

        assertDoesNotThrow(() -> new DeleteTodaysEventOutputData("", "", false),
                "Failure constructor should accept empty parameters");
    }

    @Test
    @DisplayName("Should test both success and failure states comprehensively")
    void testSuccessAndFailureStates() {
        // Success state
        DeleteTodaysEventOutputData successData = new DeleteTodaysEventOutputData("event-1", "Event Name");
        assertTrue(successData.isDeletionSuccess(), "Success case should return true for isDeletionSuccess");
        assertNotNull(successData.getEventName(), "Success case should have event name");
        assertNull(successData.getErrorMessage(), "Success case should have no error message");

        // Failure state
        DeleteTodaysEventOutputData failureData = new DeleteTodaysEventOutputData("event-2", "Error", false);
        assertFalse(failureData.isDeletionSuccess(), "Failure case should return false for isDeletionSuccess");
        assertNull(failureData.getEventName(), "Failure case should have null event name");
        assertNotNull(failureData.getErrorMessage(), "Failure case should have error message");
    }
}