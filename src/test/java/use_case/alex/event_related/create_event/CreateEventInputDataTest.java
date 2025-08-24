package use_case.alex.event_related.create_event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CreateEventInputData.
 * Tests validation, immutability, and getter functionality.
 */
class CreateEventInputDataTest {

    @Test
    @DisplayName("Should create input data with valid parameters")
    void testValidCreation() {
        // Given
        String id = "event-123";
        String name = "Team Meeting";
        String description = "Weekly team sync";
        String category = "Work";
        LocalDate createdDate = LocalDate.of(2024, 8, 24);

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then
        assertEquals(id, inputData.getId(), "ID should match");
        assertEquals(name, inputData.getName(), "Name should match");
        assertEquals(description, inputData.getDescription(), "Description should match");
        assertEquals(category, inputData.getCategory(), "Category should match");
        assertEquals(createdDate, inputData.getCreatedDate(), "Created date should match");
    }

    @Test
    @DisplayName("Should throw exception for null ID")
    void testNullId() {
        // Given
        String id = null;
        String name = "Event";
        String description = "Description";
        String category = "Category";
        LocalDate createdDate = LocalDate.now();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CreateEventInputData(id, name, description, category, createdDate));
        assertEquals("id cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty ID")
    void testEmptyId() {
        // Given
        String id = "";
        String name = "Event";
        String description = "Description";
        String category = "Category";
        LocalDate createdDate = LocalDate.now();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CreateEventInputData(id, name, description, category, createdDate));
        assertEquals("id cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for whitespace-only ID")
    void testWhitespaceOnlyId() {
        // Given
        String id = "   ";
        String name = "Event";
        String description = "Description";
        String category = "Category";
        LocalDate createdDate = LocalDate.now();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CreateEventInputData(id, name, description, category, createdDate));
        assertEquals("id cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for null created date")
    void testNullCreatedDate() {
        // Given
        String id = "event-123";
        String name = "Event";
        String description = "Description";
        String category = "Category";
        LocalDate createdDate = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CreateEventInputData(id, name, description, category, createdDate));
        assertEquals("createdDate cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept null name")
    void testNullName() {
        // Given
        String id = "event-123";
        String name = null;
        String description = "Description";
        String category = "Category";
        LocalDate createdDate = LocalDate.now();

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then
        assertNull(inputData.getName(), "Name should be null");
        assertEquals(id, inputData.getId(), "ID should be preserved");
    }

    @Test
    @DisplayName("Should accept null description")
    void testNullDescription() {
        // Given
        String id = "event-123";
        String name = "Event";
        String description = null;
        String category = "Category";
        LocalDate createdDate = LocalDate.now();

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then
        assertNull(inputData.getDescription(), "Description should be null");
        assertEquals(name, inputData.getName(), "Name should be preserved");
    }

    @Test
    @DisplayName("Should accept null category")
    void testNullCategory() {
        // Given
        String id = "event-123";
        String name = "Event";
        String description = "Description";
        String category = null;
        LocalDate createdDate = LocalDate.now();

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then
        assertNull(inputData.getCategory(), "Category should be null");
        assertEquals(name, inputData.getName(), "Name should be preserved");
    }

    @Test
    @DisplayName("Should handle empty strings for optional fields")
    void testEmptyStrings() {
        // Given
        String id = "event-123";
        String name = "";
        String description = "";
        String category = "";
        LocalDate createdDate = LocalDate.now();

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then
        assertEquals("", inputData.getName(), "Empty name should be preserved");
        assertEquals("", inputData.getDescription(), "Empty description should be preserved");
        assertEquals("", inputData.getCategory(), "Empty category should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters in fields")
    void testSpecialCharacters() {
        // Given
        String id = "event-123!@#";
        String name = "Event with émojis 😀";
        String description = "Special chars: !@#$%^&*()";
        String category = "Catégory with àccents";
        LocalDate createdDate = LocalDate.now();

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then
        assertEquals(id, inputData.getId(), "ID with special chars should be preserved");
        assertEquals(name, inputData.getName(), "Name with special chars should be preserved");
        assertEquals(description, inputData.getDescription(), "Description with special chars should be preserved");
        assertEquals(category, inputData.getCategory(), "Category with special chars should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String id = "event-" + "x".repeat(100);
        String name = "Very long event name that exceeds normal expectations and contains many characters";
        String description = "A".repeat(200);
        String category = "B".repeat(50);
        LocalDate createdDate = LocalDate.now();

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then
        assertEquals(id, inputData.getId(), "Long ID should be preserved");
        assertEquals(name, inputData.getName(), "Long name should be preserved");
        assertEquals(description, inputData.getDescription(), "Long description should be preserved");
        assertEquals(category, inputData.getCategory(), "Long category should be preserved");
    }

    @Test
    @DisplayName("Should handle various date values")
    void testVariousDateValues() {
        // Test past date
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        CreateEventInputData pastInput = new CreateEventInputData("id1", "Event", "Desc", "Cat", pastDate);
        assertEquals(pastDate, pastInput.getCreatedDate(), "Past date should be preserved");

        // Test future date
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        CreateEventInputData futureInput = new CreateEventInputData("id2", "Event", "Desc", "Cat", futureDate);
        assertEquals(futureDate, futureInput.getCreatedDate(), "Future date should be preserved");

        // Test current date
        LocalDate currentDate = LocalDate.now();
        CreateEventInputData currentInput = new CreateEventInputData("id3", "Event", "Desc", "Cat", currentDate);
        assertEquals(currentDate, currentInput.getCreatedDate(), "Current date should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalId = "event-123";
        String originalName = "Event";
        LocalDate originalDate = LocalDate.of(2024, 8, 24);
        
        CreateEventInputData inputData = new CreateEventInputData(originalId, originalName, "Desc", "Cat", originalDate);

        // When - Try to modify the returned date (should not affect internal state)
        LocalDate returnedDate = inputData.getCreatedDate();
        
        // Then - Original values should remain unchanged
        assertEquals(originalId, inputData.getId(), "ID should remain unchanged");
        assertEquals(originalName, inputData.getName(), "Name should remain unchanged");
        assertEquals(originalDate, inputData.getCreatedDate(), "Date should remain unchanged");
        assertEquals(returnedDate, inputData.getCreatedDate(), "Returned date should equal internal date");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        CreateEventInputData inputData1 = new CreateEventInputData("id1", "Event1", "Desc1", "Cat1", LocalDate.now());
        CreateEventInputData inputData2 = new CreateEventInputData("id2", "Event2", "Desc2", "Cat2", LocalDate.now());

        // Then
        assertNotEquals(inputData1.getId(), inputData2.getId(), "Different instances should have different IDs");
        assertNotEquals(inputData1.getName(), inputData2.getName(), "Different instances should have different names");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact field values")
    void testFieldPreservation() {
        // Given
        String id = "exact-id";
        String name = "Exact Name";
        String description = "Exact Description";
        String category = "Exact Category";
        LocalDate createdDate = LocalDate.of(2024, 8, 24);

        // When
        CreateEventInputData inputData = new CreateEventInputData(id, name, description, category, createdDate);

        // Then - Test that each field is exactly preserved
        assertSame(id, inputData.getId(), "ID reference should be preserved");
        assertSame(name, inputData.getName(), "Name reference should be preserved");
        assertSame(description, inputData.getDescription(), "Description reference should be preserved");
        assertSame(category, inputData.getCategory(), "Category reference should be preserved");
        assertEquals(createdDate, inputData.getCreatedDate(), "Date value should be preserved");
    }
}