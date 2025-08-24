package use_case.Angela.category.edit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for EditCategoryOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class EditCategoryOutputDataTest {

    @Test
    @DisplayName("Should create output data with valid parameters")
    void testValidCreation() {
        // Given
        String categoryId = "cat-123";
        String oldName = "Work";
        String newName = "Updated Work";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should match");
        assertEquals(oldName, outputData.getOldName(), "Old name should match");
        assertEquals(newName, outputData.getNewName(), "New name should match");
    }

    @Test
    @DisplayName("Should accept null category ID")
    void testNullCategoryId() {
        // Given
        String categoryId = null;
        String oldName = "Work";
        String newName = "Updated Work";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertNull(outputData.getCategoryId(), "Category ID should be null");
        assertEquals(oldName, outputData.getOldName(), "Old name should be preserved");
        assertEquals(newName, outputData.getNewName(), "New name should be preserved");
    }

    @Test
    @DisplayName("Should accept null old name")
    void testNullOldName() {
        // Given
        String categoryId = "cat-123";
        String oldName = null;
        String newName = "Updated Work";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should be preserved");
        assertNull(outputData.getOldName(), "Old name should be null");
        assertEquals(newName, outputData.getNewName(), "New name should be preserved");
    }

    @Test
    @DisplayName("Should accept null new name")
    void testNullNewName() {
        // Given
        String categoryId = "cat-123";
        String oldName = "Work";
        String newName = null;

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should be preserved");
        assertEquals(oldName, outputData.getOldName(), "Old name should be preserved");
        assertNull(outputData.getNewName(), "New name should be null");
    }

    @Test
    @DisplayName("Should accept all null parameters")
    void testAllNull() {
        // Given
        String categoryId = null;
        String oldName = null;
        String newName = null;

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertNull(outputData.getCategoryId(), "Category ID should be null");
        assertNull(outputData.getOldName(), "Old name should be null");
        assertNull(outputData.getNewName(), "New name should be null");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String categoryId = "";
        String oldName = "";
        String newName = "";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals("", outputData.getCategoryId(), "Empty category ID should be preserved");
        assertEquals("", outputData.getOldName(), "Empty old name should be preserved");
        assertEquals("", outputData.getNewName(), "Empty new name should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String categoryId = "   ";
        String oldName = "\\t\\n  ";
        String newName = "  \\r\\n";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals("   ", outputData.getCategoryId(), "Whitespace category ID should be preserved");
        assertEquals("\\t\\n  ", outputData.getOldName(), "Whitespace old name should be preserved");
        assertEquals("  \\r\\n", outputData.getNewName(), "Whitespace new name should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String categoryId = "cat-123!@#$%";
        String oldName = "Work & Personal!@#";
        String newName = "Updated Work & Personal$%^";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Special characters in ID should be preserved");
        assertEquals(oldName, outputData.getOldName(), "Special characters in old name should be preserved");
        assertEquals(newName, outputData.getNewName(), "Special characters in new name should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String categoryId = "类别-123";
        String oldName = "工作";
        String newName = "更新的工作 😀";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Unicode characters in ID should be preserved");
        assertEquals(oldName, outputData.getOldName(), "Unicode characters in old name should be preserved");
        assertEquals(newName, outputData.getNewName(), "Unicode characters in new name should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String categoryId = "category-" + "x".repeat(100);
        String oldName = "Old category name " + "A".repeat(150);
        String newName = "New category name " + "B".repeat(200);

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Long category ID should be preserved");
        assertEquals(oldName, outputData.getOldName(), "Long old name should be preserved");
        assertEquals(newName, outputData.getNewName(), "Long new name should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric strings")
    void testNumericStrings() {
        // Given
        String categoryId = "123456789";
        String oldName = "Category123";
        String newName = "UpdatedCategory456";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Numeric ID should be preserved");
        assertEquals(oldName, outputData.getOldName(), "Alphanumeric old name should be preserved");
        assertEquals(newName, outputData.getNewName(), "Alphanumeric new name should be preserved");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String categoryId = "a";
        String oldName = "b";
        String newName = "c";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals("a", outputData.getCategoryId(), "Single character ID should be preserved");
        assertEquals("b", outputData.getOldName(), "Single character old name should be preserved");
        assertEquals("c", outputData.getNewName(), "Single character new name should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalCategoryId = "cat-123";
        String originalOldName = "Work";
        String originalNewName = "Updated Work";
        
        EditCategoryOutputData outputData = new EditCategoryOutputData(originalCategoryId, originalOldName, originalNewName);

        // Then
        assertEquals(originalCategoryId, outputData.getCategoryId(), "Category ID should remain unchanged");
        assertEquals(originalOldName, outputData.getOldName(), "Old name should remain unchanged");
        assertEquals(originalNewName, outputData.getNewName(), "New name should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        EditCategoryOutputData outputData1 = new EditCategoryOutputData("cat-1", "Work", "Updated Work");
        EditCategoryOutputData outputData2 = new EditCategoryOutputData("cat-2", "Personal", "Updated Personal");

        // Then
        assertNotEquals(outputData1.getCategoryId(), outputData2.getCategoryId(), "Different instances should have different IDs");
        assertNotEquals(outputData1.getOldName(), outputData2.getOldName(), "Different instances should have different old names");
        assertNotEquals(outputData1.getNewName(), outputData2.getNewName(), "Different instances should have different new names");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string references")
    void testStringReference() {
        // Given
        String categoryId = "cat-123";
        String oldName = "Work";
        String newName = "Updated Work";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertSame(categoryId, outputData.getCategoryId(), "Should preserve exact ID string reference");
        assertSame(oldName, outputData.getOldName(), "Should preserve exact old name string reference");
        assertSame(newName, outputData.getNewName(), "Should preserve exact new name string reference");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat-123", "Work", "Updated Work");

        // Then - Multiple calls should return same values
        assertEquals(outputData.getCategoryId(), outputData.getCategoryId(), "getCategoryId should be consistent");
        assertEquals(outputData.getOldName(), outputData.getOldName(), "getOldName should be consistent");
        assertEquals(outputData.getNewName(), outputData.getNewName(), "getNewName should be consistent");
    }

    @Test
    @DisplayName("Should handle mixed case strings")
    void testMixedCaseStrings() {
        // Given
        String categoryId = "CaT-123-MiXeD";
        String oldName = "WoRk";
        String newName = "UpDaTeD wOrK";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Mixed case ID should be preserved exactly");
        assertEquals(oldName, outputData.getOldName(), "Mixed case old name should be preserved exactly");
        assertEquals(newName, outputData.getNewName(), "Mixed case new name should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle same old and new names")
    void testSameOldAndNewNames() {
        // Given
        String categoryId = "cat-123";
        String sameName = "Work";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, sameName, sameName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should be preserved");
        assertEquals(sameName, outputData.getOldName(), "Old name should be preserved");
        assertEquals(sameName, outputData.getNewName(), "New name should be preserved");
        assertSame(outputData.getOldName(), outputData.getNewName(), "Old and new names should reference same string");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test with null ID, empty old name, whitespace new name
        EditCategoryOutputData combo1 = new EditCategoryOutputData(null, "", "   ");
        assertNull(combo1.getCategoryId());
        assertEquals("", combo1.getOldName());
        assertEquals("   ", combo1.getNewName());

        // Test with empty ID, null old name, single char new name
        EditCategoryOutputData combo2 = new EditCategoryOutputData("", null, "A");
        assertEquals("", combo2.getCategoryId());
        assertNull(combo2.getOldName());
        assertEquals("A", combo2.getNewName());
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces")
    void testLeadingAndTrailingSpaces() {
        // Given
        String categoryId = "  cat-123  ";
        String oldName = "  Work  ";
        String newName = "  Updated Work  ";

        // When
        EditCategoryOutputData outputData = new EditCategoryOutputData(categoryId, oldName, newName);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Leading/trailing spaces in ID should be preserved");
        assertEquals(oldName, outputData.getOldName(), "Leading/trailing spaces in old name should be preserved");
        assertEquals(newName, outputData.getNewName(), "Leading/trailing spaces in new name should be preserved");
    }
}