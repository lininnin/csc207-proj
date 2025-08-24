package use_case.alex.event_related.create_event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CreateEventOutputData.
 * Tests validation, trimming behavior, and getter functionality.
 */
class CreateEventOutputDataTest {

    @Test
    @DisplayName("Should create output data with valid parameters")
    void testValidCreation() {
        // Given
        String name = "Team Meeting";
        String description = "Weekly team sync";
        String category = "Work";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals(name, outputData.getName(), "Name should match");
        assertEquals(description, outputData.getDescription(), "Description should match");
        assertEquals(category, outputData.getCategory(), "Category should match");
        assertEquals(useCaseFailed, outputData.getUseCaseFailed(), "Use case failed flag should match");
        assertFalse(outputData.isFailed(), "isFailed should return false when use case succeeded");
    }

    @Test
    @DisplayName("Should create output data with failed use case")
    void testFailedUseCase() {
        // Given
        String name = "Event";
        String description = "Description";
        String category = "Category";
        boolean useCaseFailed = true;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertTrue(outputData.getUseCaseFailed(), "Use case failed flag should be true");
        assertTrue(outputData.isFailed(), "isFailed should return true when use case failed");
    }

    @Test
    @DisplayName("Should throw exception for null name")
    void testNullName() {
        // Given
        String name = null;
        String description = "Description";
        String category = "Category";
        boolean useCaseFailed = false;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CreateEventOutputData(name, description, category, useCaseFailed));
        assertEquals("Event name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for empty name")
    void testEmptyName() {
        // Given
        String name = "";
        String description = "Description";
        String category = "Category";
        boolean useCaseFailed = false;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CreateEventOutputData(name, description, category, useCaseFailed));
        assertEquals("Event name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for whitespace-only name")
    void testWhitespaceOnlyName() {
        // Given
        String name = "   ";
        String description = "Description";
        String category = "Category";
        boolean useCaseFailed = false;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CreateEventOutputData(name, description, category, useCaseFailed));
        assertEquals("Event name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should trim name whitespace")
    void testNameTrimming() {
        // Given
        String name = "  Event Name  ";
        String description = "Description";
        String category = "Category";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("Event Name", outputData.getName(), "Name should be trimmed");
    }

    @Test
    @DisplayName("Should handle null description")
    void testNullDescription() {
        // Given
        String name = "Event";
        String description = null;
        String category = "Category";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("", outputData.getDescription(), "Null description should become empty string");
    }

    @Test
    @DisplayName("Should trim description whitespace")
    void testDescriptionTrimming() {
        // Given
        String name = "Event";
        String description = "  Description with spaces  ";
        String category = "Category";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("Description with spaces", outputData.getDescription(), "Description should be trimmed");
    }

    @Test
    @DisplayName("Should handle empty description")
    void testEmptyDescription() {
        // Given
        String name = "Event";
        String description = "";
        String category = "Category";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("", outputData.getDescription(), "Empty description should remain empty");
    }

    @Test
    @DisplayName("Should handle null category")
    void testNullCategory() {
        // Given
        String name = "Event";
        String description = "Description";
        String category = null;
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("", outputData.getCategory(), "Null category should become empty string");
    }

    @Test
    @DisplayName("Should trim category whitespace")
    void testCategoryTrimming() {
        // Given
        String name = "Event";
        String description = "Description";
        String category = "  Work Category  ";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("Work Category", outputData.getCategory(), "Category should be trimmed");
    }

    @Test
    @DisplayName("Should handle empty category")
    void testEmptyCategory() {
        // Given
        String name = "Event";
        String description = "Description";
        String category = "";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("", outputData.getCategory(), "Empty category should remain empty");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String name = "Event with émojis 😀";
        String description = "Special chars: !@#$%^&*()";
        String category = "Catégory with àccents";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals(name, outputData.getName(), "Name with special chars should be preserved");
        assertEquals(description, outputData.getDescription(), "Description with special chars should be preserved");
        assertEquals(category, outputData.getCategory(), "Category with special chars should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String name = "Very long event name that exceeds normal expectations and contains many characters";
        String description = "A".repeat(200);
        String category = "B".repeat(50);
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals(name, outputData.getName(), "Long name should be preserved");
        assertEquals(description, outputData.getDescription(), "Long description should be preserved");
        assertEquals(category, outputData.getCategory(), "Long category should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only description and category")
    void testWhitespaceOnlyOptionalFields() {
        // Given
        String name = "Event";
        String description = "   ";
        String category = "   ";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("", outputData.getDescription(), "Whitespace-only description should become empty");
        assertEquals("", outputData.getCategory(), "Whitespace-only category should become empty");
    }

    @Test
    @DisplayName("Should test both getter methods for use case failed")
    void testBothFailureGetters() {
        // Test false case
        CreateEventOutputData successData = new CreateEventOutputData("Event", "Desc", "Cat", false);
        assertFalse(successData.getUseCaseFailed(), "getUseCaseFailed should return false");
        assertFalse(successData.isFailed(), "isFailed should return false");

        // Test true case
        CreateEventOutputData failureData = new CreateEventOutputData("Event", "Desc", "Cat", true);
        assertTrue(failureData.getUseCaseFailed(), "getUseCaseFailed should return true");
        assertTrue(failureData.isFailed(), "isFailed should return true");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalName = "Event";
        String originalDescription = "Description";
        String originalCategory = "Category";
        
        CreateEventOutputData outputData = new CreateEventOutputData(originalName, originalDescription, originalCategory, false);

        // Then - Should return same values consistently
        assertEquals(originalName, outputData.getName(), "Name should remain unchanged");
        assertEquals(originalDescription, outputData.getDescription(), "Description should remain unchanged");
        assertEquals(originalCategory, outputData.getCategory(), "Category should remain unchanged");
        assertFalse(outputData.getUseCaseFailed(), "Use case failed flag should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        CreateEventOutputData outputData1 = new CreateEventOutputData("Event1", "Desc1", "Cat1", false);
        CreateEventOutputData outputData2 = new CreateEventOutputData("Event2", "Desc2", "Cat2", true);

        // Then
        assertNotEquals(outputData1.getName(), outputData2.getName(), "Different instances should have different names");
        assertNotEquals(outputData1.getUseCaseFailed(), outputData2.getUseCaseFailed(), "Different instances should have different failure states");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should handle mixed whitespace scenarios")
    void testMixedWhitespaceScenarios() {
        // Given - Mix of tabs, spaces, and newlines
        String name = "\t Event Name \n";
        String description = " \t Description \n ";
        String category = "\n\t Category \t\n";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("Event Name", outputData.getName(), "Mixed whitespace in name should be trimmed");
        assertEquals("Description", outputData.getDescription(), "Mixed whitespace in description should be trimmed");
        assertEquals("Category", outputData.getCategory(), "Mixed whitespace in category should be trimmed");
    }

    @Test
    @DisplayName("Should preserve internal spaces in trimmed strings")
    void testInternalSpacePreservation() {
        // Given
        String name = "  Team Meeting Event  ";
        String description = "  Weekly team sync meeting  ";
        String category = "  Work Category  ";
        boolean useCaseFailed = false;

        // When
        CreateEventOutputData outputData = new CreateEventOutputData(name, description, category, useCaseFailed);

        // Then
        assertEquals("Team Meeting Event", outputData.getName(), "Internal spaces in name should be preserved");
        assertEquals("Weekly team sync meeting", outputData.getDescription(), "Internal spaces in description should be preserved");
        assertEquals("Work Category", outputData.getCategory(), "Internal spaces in category should be preserved");
    }
}