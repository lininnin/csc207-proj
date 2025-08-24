package use_case.Angela.category.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CreateCategoryInputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class CreateCategoryInputDataTest {

    @Test
    @DisplayName("Should create input data with valid category name")
    void testValidCreation() {
        // Given
        String categoryName = "Work";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Category name should match");
    }

    @Test
    @DisplayName("Should accept null category name")
    void testNullCategoryName() {
        // Given
        String categoryName = null;

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertNull(inputData.getCategoryName(), "Category name should be null");
    }

    @Test
    @DisplayName("Should accept empty category name")
    void testEmptyCategoryName() {
        // Given
        String categoryName = "";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals("", inputData.getCategoryName(), "Empty category name should be preserved");
    }

    @Test
    @DisplayName("Should accept whitespace-only category name")
    void testWhitespaceOnlyCategoryName() {
        // Given
        String categoryName = "   ";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals("   ", inputData.getCategoryName(), "Whitespace category name should be preserved");
    }

    @Test
    @DisplayName("Should handle category name with special characters")
    void testSpecialCharacters() {
        // Given
        String categoryName = "Work & Personal!@#";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Special characters should be preserved");
    }

    @Test
    @DisplayName("Should handle long category names")
    void testLongCategoryName() {
        // Given
        String categoryName = "Very long category name that exceeds normal expectations and contains many words";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Long category name should be preserved");
    }

    @Test
    @DisplayName("Should handle category name with unicode characters")
    void testUnicodeCategoryName() {
        // Given
        String categoryName = "工作 Work 😀 测试";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Unicode characters should be preserved");
    }

    @Test
    @DisplayName("Should handle category name with numbers")
    void testCategoryNameWithNumbers() {
        // Given
        String categoryName = "Project123";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Numbers in category name should be preserved");
    }

    @Test
    @DisplayName("Should handle single character category name")
    void testSingleCharacterCategoryName() {
        // Given
        String categoryName = "A";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Single character should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalCategoryName = "Work";
        CreateCategoryInputData inputData = new CreateCategoryInputData(originalCategoryName);

        // Then
        assertEquals(originalCategoryName, inputData.getCategoryName(), "Category name should remain unchanged");
        
        // Multiple calls should return same value
        assertEquals(inputData.getCategoryName(), inputData.getCategoryName(), "Multiple getter calls should return same value");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        CreateCategoryInputData inputData1 = new CreateCategoryInputData("Work");
        CreateCategoryInputData inputData2 = new CreateCategoryInputData("Personal");

        // Then
        assertNotEquals(inputData1.getCategoryName(), inputData2.getCategoryName(), "Different instances should have different category names");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string reference")
    void testStringReference() {
        // Given
        String categoryName = "Work";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertSame(categoryName, inputData.getCategoryName(), "Should preserve exact string reference");
    }

    @Test
    @DisplayName("Should handle category names with leading/trailing spaces")
    void testCategoryNameWithSpaces() {
        // Given
        String categoryName = "  Work  ";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Leading/trailing spaces should be preserved");
    }

    @Test
    @DisplayName("Should handle category names with internal spaces")
    void testCategoryNameWithInternalSpaces() {
        // Given
        String categoryName = "Work and Personal";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Internal spaces should be preserved");
    }

    @Test
    @DisplayName("Should handle mixed case category names")
    void testMixedCaseCategoryName() {
        // Given
        String categoryName = "WoRk AnD pErSoNaL";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Mixed case should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle category names with newlines and tabs")
    void testCategoryNameWithNewlinesAndTabs() {
        // Given
        String categoryName = "Work\nand\tPersonal";

        // When
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);

        // Then
        assertEquals(categoryName, inputData.getCategoryName(), "Newlines and tabs should be preserved");
    }
}