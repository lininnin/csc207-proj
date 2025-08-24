package use_case.Angela.category.delete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteCategoryInputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class DeleteCategoryInputDataTest {

    @Test
    @DisplayName("Should create input data with valid category ID")
    void testValidCreation() {
        // Given
        String categoryId = "cat-123";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Category ID should match");
    }

    @Test
    @DisplayName("Should accept null category ID")
    void testNullCategoryId() {
        // Given
        String categoryId = null;

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertNull(inputData.getCategoryId(), "Category ID should be null");
    }

    @Test
    @DisplayName("Should accept empty category ID")
    void testEmptyCategoryId() {
        // Given
        String categoryId = "";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals("", inputData.getCategoryId(), "Empty category ID should be preserved");
    }

    @Test
    @DisplayName("Should accept whitespace-only category ID")
    void testWhitespaceOnlyCategoryId() {
        // Given
        String categoryId = "   ";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals("   ", inputData.getCategoryId(), "Whitespace category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle category ID with special characters")
    void testSpecialCharacters() {
        // Given
        String categoryId = "cat-123!@#$%";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Special characters should be preserved");
    }

    @Test
    @DisplayName("Should handle long category ID")
    void testLongCategoryId() {
        // Given
        String categoryId = "category-" + "x".repeat(100);

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Long category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle category ID with unicode characters")
    void testUnicodeCategoryId() {
        // Given
        String categoryId = "类别-123-😀";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Unicode characters should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric category ID")
    void testNumericCategoryId() {
        // Given
        String categoryId = "123456789";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Numeric category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle single character category ID")
    void testSingleCharacterCategoryId() {
        // Given
        String categoryId = "a";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals("a", inputData.getCategoryId(), "Single character should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalCategoryId = "cat-123";
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(originalCategoryId);

        // Then
        assertEquals(originalCategoryId, inputData.getCategoryId(), "Category ID should remain unchanged");
        
        // Multiple calls should return same value
        assertEquals(inputData.getCategoryId(), inputData.getCategoryId(), "Multiple getter calls should return same value");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        DeleteCategoryInputData inputData1 = new DeleteCategoryInputData("cat-1");
        DeleteCategoryInputData inputData2 = new DeleteCategoryInputData("cat-2");

        // Then
        assertNotEquals(inputData1.getCategoryId(), inputData2.getCategoryId(), "Different instances should have different category IDs");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string reference")
    void testStringReference() {
        // Given
        String categoryId = "cat-123";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertSame(categoryId, inputData.getCategoryId(), "Should preserve exact string reference");
    }

    @Test
    @DisplayName("Should handle category ID with leading/trailing spaces")
    void testCategoryIdWithSpaces() {
        // Given
        String categoryId = "  cat-123  ";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Leading/trailing spaces should be preserved");
    }

    @Test
    @DisplayName("Should handle mixed case category ID")
    void testMixedCaseCategoryId() {
        // Given
        String categoryId = "CaT-123-MiXeD";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Mixed case should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle category ID with newlines and tabs")
    void testCategoryIdWithNewlinesAndTabs() {
        // Given
        String categoryId = "cat\\n123\\ttab";

        // When
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Newlines and tabs should be preserved");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat-123");

        // Then - Multiple calls should return same values
        assertEquals(inputData.getCategoryId(), inputData.getCategoryId(), "getCategoryId should be consistent");
    }
}