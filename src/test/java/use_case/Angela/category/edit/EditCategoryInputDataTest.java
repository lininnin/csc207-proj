package use_case.Angela.category.edit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for EditCategoryInputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class EditCategoryInputDataTest {

    @Test
    @DisplayName("Should create input data with valid parameters")
    void testValidCreation() {
        // Given
        String categoryId = "cat-123";
        String newCategoryName = "Updated Work";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Category ID should match");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "New category name should match");
    }

    @Test
    @DisplayName("Should accept null category ID")
    void testNullCategoryId() {
        // Given
        String categoryId = null;
        String newCategoryName = "Work";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertNull(inputData.getCategoryId(), "Category ID should be null");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "New category name should be preserved");
    }

    @Test
    @DisplayName("Should accept null new category name")
    void testNullNewCategoryName() {
        // Given
        String categoryId = "cat-123";
        String newCategoryName = null;

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Category ID should be preserved");
        assertNull(inputData.getNewCategoryName(), "New category name should be null");
    }

    @Test
    @DisplayName("Should accept both null parameters")
    void testBothNull() {
        // Given
        String categoryId = null;
        String newCategoryName = null;

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertNull(inputData.getCategoryId(), "Category ID should be null");
        assertNull(inputData.getNewCategoryName(), "New category name should be null");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String categoryId = "";
        String newCategoryName = "";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals("", inputData.getCategoryId(), "Empty category ID should be preserved");
        assertEquals("", inputData.getNewCategoryName(), "Empty new category name should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String categoryId = "   ";
        String newCategoryName = "\\t\\n  ";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals("   ", inputData.getCategoryId(), "Whitespace category ID should be preserved");
        assertEquals("\\t\\n  ", inputData.getNewCategoryName(), "Whitespace new category name should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String categoryId = "cat-123!@#$%";
        String newCategoryName = "Work & Personal!@#";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Special characters in ID should be preserved");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "Special characters in name should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String categoryId = "类别-123";
        String newCategoryName = "工作 和 个人 😀";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Unicode characters in ID should be preserved");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "Unicode characters in name should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String categoryId = "category-" + "x".repeat(100);
        String newCategoryName = "Very long category name that exceeds normal expectations " + "A".repeat(200);

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Long category ID should be preserved");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "Long new category name should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric strings")
    void testNumericStrings() {
        // Given
        String categoryId = "123456789";
        String newCategoryName = "Category123";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Numeric ID should be preserved");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "Alphanumeric name should be preserved");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String categoryId = "a";
        String newCategoryName = "b";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals("a", inputData.getCategoryId(), "Single character ID should be preserved");
        assertEquals("b", inputData.getNewCategoryName(), "Single character name should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalCategoryId = "cat-123";
        String originalNewCategoryName = "Updated Work";
        
        EditCategoryInputData inputData = new EditCategoryInputData(originalCategoryId, originalNewCategoryName);

        // Then
        assertEquals(originalCategoryId, inputData.getCategoryId(), "Category ID should remain unchanged");
        assertEquals(originalNewCategoryName, inputData.getNewCategoryName(), "New category name should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        EditCategoryInputData inputData1 = new EditCategoryInputData("cat-1", "Work");
        EditCategoryInputData inputData2 = new EditCategoryInputData("cat-2", "Personal");

        // Then
        assertNotEquals(inputData1.getCategoryId(), inputData2.getCategoryId(), "Different instances should have different IDs");
        assertNotEquals(inputData1.getNewCategoryName(), inputData2.getNewCategoryName(), "Different instances should have different names");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string references")
    void testStringReference() {
        // Given
        String categoryId = "cat-123";
        String newCategoryName = "Updated Work";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertSame(categoryId, inputData.getCategoryId(), "Should preserve exact ID string reference");
        assertSame(newCategoryName, inputData.getNewCategoryName(), "Should preserve exact name string reference");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        EditCategoryInputData inputData = new EditCategoryInputData("cat-123", "Updated Work");

        // Then - Multiple calls should return same values
        assertEquals(inputData.getCategoryId(), inputData.getCategoryId(), "getCategoryId should be consistent");
        assertEquals(inputData.getNewCategoryName(), inputData.getNewCategoryName(), "getNewCategoryName should be consistent");
    }

    @Test
    @DisplayName("Should handle mixed case strings")
    void testMixedCaseStrings() {
        // Given
        String categoryId = "CaT-123-MiXeD";
        String newCategoryName = "WoRk AnD pErSoNaL";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Mixed case ID should be preserved exactly");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "Mixed case name should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle strings with newlines and tabs")
    void testStringsWithNewlinesAndTabs() {
        // Given
        String categoryId = "cat\\n123\\ttab";
        String newCategoryName = "Work\\nand\\tPersonal";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "ID with newlines and tabs should be preserved");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "Name with newlines and tabs should be preserved");
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces")
    void testLeadingAndTrailingSpaces() {
        // Given
        String categoryId = "  cat-123  ";
        String newCategoryName = "  Updated Work  ";

        // When
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newCategoryName);

        // Then
        assertEquals(categoryId, inputData.getCategoryId(), "Leading/trailing spaces in ID should be preserved");
        assertEquals(newCategoryName, inputData.getNewCategoryName(), "Leading/trailing spaces in name should be preserved");
    }
}