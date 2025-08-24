package use_case.Angela.category.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CreateCategoryOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class CreateCategoryOutputDataTest {

    @Test
    @DisplayName("Should create output data with all valid parameters")
    void testValidCreation() {
        // Given
        String categoryId = "cat-123";
        String categoryName = "Work";
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Category ID should match");
        assertEquals(categoryName, outputData.getCategoryName(), "Category name should match");
        assertTrue(outputData.shouldCloseDialog(), "Should close dialog");
    }

    @Test
    @DisplayName("Should handle closeDialog false")
    void testCloseDialogFalse() {
        // Given
        String categoryId = "cat-123";
        String categoryName = "Work";
        boolean closeDialog = false;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertFalse(outputData.shouldCloseDialog(), "Should not close dialog");
        assertEquals(categoryId, outputData.getCategoryId(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null category ID")
    void testNullCategoryId() {
        // Given
        String categoryId = null;
        String categoryName = "Work";
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertNull(outputData.getCategoryId(), "Category ID should be null");
        assertEquals(categoryName, outputData.getCategoryName(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null category name")
    void testNullCategoryName() {
        // Given
        String categoryId = "cat-123";
        String categoryName = null;
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertNull(outputData.getCategoryName(), "Category name should be null");
        assertEquals(categoryId, outputData.getCategoryId(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept both null strings")
    void testBothNullStrings() {
        // Given
        String categoryId = null;
        String categoryName = null;
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertNull(outputData.getCategoryId(), "Category ID should be null");
        assertNull(outputData.getCategoryName(), "Category name should be null");
        assertTrue(outputData.shouldCloseDialog(), "Close dialog flag should be preserved");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String categoryId = "";
        String categoryName = "";
        boolean closeDialog = false;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals("", outputData.getCategoryId(), "Empty category ID should be preserved");
        assertEquals("", outputData.getCategoryName(), "Empty category name should be preserved");
        assertFalse(outputData.shouldCloseDialog(), "Close dialog flag should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String categoryId = "   ";
        String categoryName = "\t\n  ";
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals("   ", outputData.getCategoryId(), "Whitespace category ID should be preserved");
        assertEquals("\t\n  ", outputData.getCategoryName(), "Whitespace category name should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String categoryId = "cat-123!@#$%";
        String categoryName = "Work & Personal 😀";
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Special characters in ID should be preserved");
        assertEquals(categoryName, outputData.getCategoryName(), "Special characters in name should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String categoryId = "类别-123";
        String categoryName = "工作 和 个人";
        boolean closeDialog = false;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Unicode characters in ID should be preserved");
        assertEquals(categoryName, outputData.getCategoryName(), "Unicode characters in name should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String categoryId = "category-" + "x".repeat(100);
        String categoryName = "Very long category name that exceeds normal expectations " + "A".repeat(200);
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Long category ID should be preserved");
        assertEquals(categoryName, outputData.getCategoryName(), "Long category name should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric strings")
    void testNumericStrings() {
        // Given
        String categoryId = "123456789";
        String categoryName = "Category123";
        boolean closeDialog = false;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals(categoryId, outputData.getCategoryId(), "Numeric ID should be preserved");
        assertEquals(categoryName, outputData.getCategoryName(), "Alphanumeric name should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalCategoryId = "cat-123";
        String originalCategoryName = "Work";
        boolean originalCloseDialog = true;
        
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(originalCategoryId, originalCategoryName, originalCloseDialog);

        // Then
        assertEquals(originalCategoryId, outputData.getCategoryId(), "Category ID should remain unchanged");
        assertEquals(originalCategoryName, outputData.getCategoryName(), "Category name should remain unchanged");
        assertEquals(originalCloseDialog, outputData.shouldCloseDialog(), "Close dialog flag should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        CreateCategoryOutputData outputData1 = new CreateCategoryOutputData("cat-1", "Work", true);
        CreateCategoryOutputData outputData2 = new CreateCategoryOutputData("cat-2", "Personal", false);

        // Then
        assertNotEquals(outputData1.getCategoryId(), outputData2.getCategoryId(), "Different instances should have different IDs");
        assertNotEquals(outputData1.getCategoryName(), outputData2.getCategoryName(), "Different instances should have different names");
        assertNotEquals(outputData1.shouldCloseDialog(), outputData2.shouldCloseDialog(), "Different instances should have different flags");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string references")
    void testStringReference() {
        // Given
        String categoryId = "cat-123";
        String categoryName = "Work";

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, true);

        // Then
        assertSame(categoryId, outputData.getCategoryId(), "Should preserve exact ID string reference");
        assertSame(categoryName, outputData.getCategoryName(), "Should preserve exact name string reference");
    }

    @Test
    @DisplayName("Should handle mixed boolean scenarios")
    void testBooleanScenarios() {
        // Test true case
        CreateCategoryOutputData trueData = new CreateCategoryOutputData("id1", "name1", true);
        assertTrue(trueData.shouldCloseDialog(), "True case should return true");

        // Test false case
        CreateCategoryOutputData falseData = new CreateCategoryOutputData("id2", "name2", false);
        assertFalse(falseData.shouldCloseDialog(), "False case should return false");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String categoryId = "a";
        String categoryName = "b";
        boolean closeDialog = true;

        // When
        CreateCategoryOutputData outputData = new CreateCategoryOutputData(categoryId, categoryName, closeDialog);

        // Then
        assertEquals("a", outputData.getCategoryId(), "Single character ID should be preserved");
        assertEquals("b", outputData.getCategoryName(), "Single character name should be preserved");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        CreateCategoryOutputData outputData = new CreateCategoryOutputData("cat-123", "Work", true);

        // Then - Multiple calls should return same values
        assertEquals(outputData.getCategoryId(), outputData.getCategoryId(), "getCategoryId should be consistent");
        assertEquals(outputData.getCategoryName(), outputData.getCategoryName(), "getCategoryName should be consistent");
        assertEquals(outputData.shouldCloseDialog(), outputData.shouldCloseDialog(), "shouldCloseDialog should be consistent");
    }

    @Test
    @DisplayName("Should handle extreme boolean combinations")
    void testExtremeBooleanCombinations() {
        // Test with null strings and true boolean
        CreateCategoryOutputData nullTrue = new CreateCategoryOutputData(null, null, true);
        assertNull(nullTrue.getCategoryId());
        assertNull(nullTrue.getCategoryName());
        assertTrue(nullTrue.shouldCloseDialog());

        // Test with empty strings and false boolean
        CreateCategoryOutputData emptyFalse = new CreateCategoryOutputData("", "", false);
        assertEquals("", emptyFalse.getCategoryId());
        assertEquals("", emptyFalse.getCategoryName());
        assertFalse(emptyFalse.shouldCloseDialog());
    }
}