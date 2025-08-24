package use_case.Angela.category.delete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteCategoryOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class DeleteCategoryOutputDataTest {

    @Test
    @DisplayName("Should create output data with valid parameters")
    void testValidCreation() {
        // Given
        String categoryId = "cat-123";
        String message = "Category deleted successfully";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should match");
        assertEquals(message, outputData.getMessage(), "Message should match");
    }

    @Test
    @DisplayName("Should accept null category ID")
    void testNullCategoryId() {
        // Given
        String categoryId = null;
        String message = "Category deleted successfully";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertNull(outputData.getCategoryId(), "Category ID should be null");
        assertEquals(message, outputData.getMessage(), "Message should be preserved");
    }

    @Test
    @DisplayName("Should accept null message")
    void testNullMessage() {
        // Given
        String categoryId = "cat-123";
        String message = null;

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should be preserved");
        assertNull(outputData.getMessage(), "Message should be null");
    }

    @Test
    @DisplayName("Should accept both null parameters")
    void testBothNull() {
        // Given
        String categoryId = null;
        String message = null;

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertNull(outputData.getCategoryId(), "Category ID should be null");
        assertNull(outputData.getMessage(), "Message should be null");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String categoryId = "";
        String message = "";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals("", outputData.getCategoryId(), "Empty category ID should be preserved");
        assertEquals("", outputData.getMessage(), "Empty message should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String categoryId = "   ";
        String message = "\\t\\n  ";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals("   ", outputData.getCategoryId(), "Whitespace category ID should be preserved");
        assertEquals("\\t\\n  ", outputData.getMessage(), "Whitespace message should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String categoryId = "cat-123!@#$%";
        String message = "Category 'Work & Personal' deleted successfully!";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Special characters in ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Special characters in message should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String categoryId = "类别-123";
        String message = "类别删除成功 😀";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Unicode characters in ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Unicode characters in message should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String categoryId = "category-" + "x".repeat(100);
        String message = "Very long success message that exceeds normal expectations " + "A".repeat(200);

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Long category ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Long message should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric strings")
    void testNumericStrings() {
        // Given
        String categoryId = "123456789";
        String message = "Category 123 deleted";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Numeric ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Message with numbers should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalCategoryId = "cat-123";
        String originalMessage = "Category deleted successfully";
        
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(originalCategoryId, originalMessage);

        // Then
        assertEquals(originalCategoryId, outputData.getCategoryId(), "Category ID should remain unchanged");
        assertEquals(originalMessage, outputData.getMessage(), "Message should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        DeleteCategoryOutputData outputData1 = new DeleteCategoryOutputData("cat-1", "Message 1");
        DeleteCategoryOutputData outputData2 = new DeleteCategoryOutputData("cat-2", "Message 2");

        // Then
        assertNotEquals(outputData1.getCategoryId(), outputData2.getCategoryId(), "Different instances should have different IDs");
        assertNotEquals(outputData1.getMessage(), outputData2.getMessage(), "Different instances should have different messages");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string references")
    void testStringReference() {
        // Given
        String categoryId = "cat-123";
        String message = "Category deleted successfully";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertSame(categoryId, outputData.getCategoryId(), "Should preserve exact ID string reference");
        assertSame(message, outputData.getMessage(), "Should preserve exact message string reference");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String categoryId = "a";
        String message = "b";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals("a", outputData.getCategoryId(), "Single character ID should be preserved");
        assertEquals("b", outputData.getMessage(), "Single character message should be preserved");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData("cat-123", "Deleted successfully");

        // Then - Multiple calls should return same values
        assertEquals(outputData.getCategoryId(), outputData.getCategoryId(), "getCategoryId should be consistent");
        assertEquals(outputData.getMessage(), outputData.getMessage(), "getMessage should be consistent");
    }

    @Test
    @DisplayName("Should handle multiline messages")
    void testMultilineMessage() {
        // Given
        String categoryId = "cat-123";
        String message = "Category deleted successfully.\\nAll associated tasks updated.\\nOperation completed.";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Multiline message should be preserved");
    }

    @Test
    @DisplayName("Should handle success and error messages")
    void testDifferentMessageTypes() {
        // Test success message
        DeleteCategoryOutputData successData = new DeleteCategoryOutputData("cat-1", "Category deleted successfully");
        assertEquals("Category deleted successfully", successData.getMessage());

        // Test error message
        DeleteCategoryOutputData errorData = new DeleteCategoryOutputData("cat-2", "Error: Category could not be deleted");
        assertEquals("Error: Category could not be deleted", errorData.getMessage());
    }

    @Test
    @DisplayName("Should handle mixed case strings")
    void testMixedCaseStrings() {
        // Given
        String categoryId = "CaT-123-MiXeD";
        String message = "CaTeGoRy DeLeTed SuCcEsSfUlLy";

        // When
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Mixed case ID should be preserved exactly");
        assertEquals(message, outputData.getMessage(), "Mixed case message should be preserved exactly");
    }
}