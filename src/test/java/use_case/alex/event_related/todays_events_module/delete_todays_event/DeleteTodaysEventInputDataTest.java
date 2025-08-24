package use_case.alex.event_related.todays_events_module.delete_todays_event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteTodaysEventInputData.
 * Tests validation, trimming behavior, and immutability.
 */
class DeleteTodaysEventInputDataTest {

    @Test
    @DisplayName("Should create input data with valid event ID")
    void testValidCreation() {
        // Given
        String eventId = "event-123";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(eventId, inputData.getEventId(), "Event ID should match");
    }

    @Test
    @DisplayName("Should throw exception for null event ID")
    void testNullEventId() {
        // Given
        String eventId = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new DeleteTodaysEventInputData(eventId));
        assertEquals("eventId cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty event ID")
    void testEmptyEventId() {
        // Given
        String eventId = "";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new DeleteTodaysEventInputData(eventId));
        assertEquals("eventId cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for whitespace-only event ID")
    void testWhitespaceOnlyEventId() {
        // Given
        String eventId = "   ";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new DeleteTodaysEventInputData(eventId));
        assertEquals("eventId cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should trim event ID whitespace")
    void testEventIdTrimming() {
        // Given
        String eventId = "  event-123  ";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals("event-123", inputData.getEventId(), "Event ID should be trimmed");
    }

    @Test
    @DisplayName("Should handle event ID with special characters")
    void testSpecialCharacters() {
        // Given
        String eventId = "event-123!@#$%^&*()";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(eventId, inputData.getEventId(), "Event ID with special characters should be preserved");
    }

    @Test
    @DisplayName("Should handle long event IDs")
    void testLongEventId() {
        // Given
        String eventId = "very-long-event-id-" + "x".repeat(100);

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(eventId, inputData.getEventId(), "Long event ID should be preserved");
    }

    @Test
    @DisplayName("Should handle event ID with unicode characters")
    void testUnicodeEventId() {
        // Given
        String eventId = "event-123-émojis-😀-测试";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(eventId, inputData.getEventId(), "Event ID with unicode should be preserved");
    }

    @Test
    @DisplayName("Should handle event ID with numbers only")
    void testNumericEventId() {
        // Given
        String eventId = "123456789";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(eventId, inputData.getEventId(), "Numeric event ID should be preserved");
    }

    @Test
    @DisplayName("Should handle event ID with mixed case")
    void testMixedCaseEventId() {
        // Given
        String eventId = "Event-123-ABC-xyz";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(eventId, inputData.getEventId(), "Mixed case event ID should be preserved");
    }

    @Test
    @DisplayName("Should handle event ID with internal spaces")
    void testEventIdWithInternalSpaces() {
        // Given
        String eventId = "  event 123 with spaces  ";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals("event 123 with spaces", inputData.getEventId(), "Internal spaces should be preserved after trimming");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalEventId = "event-123";
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(originalEventId);

        // Then
        assertEquals(originalEventId, inputData.getEventId(), "Event ID should remain unchanged");
        
        // Multiple calls should return same value
        assertEquals(inputData.getEventId(), inputData.getEventId(), "Multiple getter calls should return same value");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        DeleteTodaysEventInputData inputData1 = new DeleteTodaysEventInputData("event-1");
        DeleteTodaysEventInputData inputData2 = new DeleteTodaysEventInputData("event-2");

        // Then
        assertNotEquals(inputData1.getEventId(), inputData2.getEventId(), "Different instances should have different event IDs");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should handle tab and newline characters")
    void testTabAndNewlineCharacters() {
        // Given
        String eventId = "\t\nevent-123\t\n";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals("event-123", inputData.getEventId(), "Tab and newline characters should be trimmed");
    }

    @Test
    @DisplayName("Should handle single character event ID")
    void testSingleCharacterEventId() {
        // Given
        String eventId = "a";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(eventId, inputData.getEventId(), "Single character event ID should be preserved");
    }

    @Test
    @DisplayName("Should preserve exact trimmed reference")
    void testTrimmedReference() {
        // Given
        String eventId = "  event-123  ";
        String expectedTrimmed = "event-123";

        // When
        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);

        // Then
        assertEquals(expectedTrimmed, inputData.getEventId(), "Should return trimmed version");
        assertNotSame(eventId, inputData.getEventId(), "Should not be same reference as original (since trimmed)");
    }
}