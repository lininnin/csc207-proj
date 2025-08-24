package use_case.Angela.task.create;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CreateTaskInputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class CreateTaskInputDataTest {

    @Test
    @DisplayName("Should create input data with valid parameters for one-time task")
    void testValidCreationOneTime() {
        // Given
        String taskName = "Complete report";
        String description = "Finish quarterly report";
        String categoryId = "cat-123";
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Task name should match");
        assertEquals(description, inputData.getDescription(), "Description should match");
        assertEquals(categoryId, inputData.getCategoryId(), "Category ID should match");
        assertTrue(inputData.isOneTime(), "Should be one-time task");
    }

    @Test
    @DisplayName("Should create input data with valid parameters for recurring task")
    void testValidCreationRecurring() {
        // Given
        String taskName = "Daily standup";
        String description = "Team daily standup meeting";
        String categoryId = "cat-456";
        boolean isOneTime = false;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Task name should match");
        assertEquals(description, inputData.getDescription(), "Description should match");
        assertEquals(categoryId, inputData.getCategoryId(), "Category ID should match");
        assertFalse(inputData.isOneTime(), "Should be recurring task");
    }

    @Test
    @DisplayName("Should accept null task name")
    void testNullTaskName() {
        // Given
        String taskName = null;
        String description = "Test description";
        String categoryId = "cat-123";
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertNull(inputData.getTaskName(), "Task name should be null");
        assertEquals(description, inputData.getDescription(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null description")
    void testNullDescription() {
        // Given
        String taskName = "Test task";
        String description = null;
        String categoryId = "cat-123";
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Task name should be preserved");
        assertNull(inputData.getDescription(), "Description should be null");
        assertEquals(categoryId, inputData.getCategoryId(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null category ID")
    void testNullCategoryId() {
        // Given
        String taskName = "Test task";
        String description = "Test description";
        String categoryId = null;
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Task name should be preserved");
        assertEquals(description, inputData.getDescription(), "Description should be preserved");
        assertNull(inputData.getCategoryId(), "Category ID should be null");
    }

    @Test
    @DisplayName("Should accept all null string parameters")
    void testAllNullStrings() {
        // Given
        String taskName = null;
        String description = null;
        String categoryId = null;
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertNull(inputData.getTaskName(), "Task name should be null");
        assertNull(inputData.getDescription(), "Description should be null");
        assertNull(inputData.getCategoryId(), "Category ID should be null");
        assertTrue(inputData.isOneTime(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String taskName = "";
        String description = "";
        String categoryId = "";
        boolean isOneTime = false;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals("", inputData.getTaskName(), "Empty task name should be preserved");
        assertEquals("", inputData.getDescription(), "Empty description should be preserved");
        assertEquals("", inputData.getCategoryId(), "Empty category ID should be preserved");
        assertFalse(inputData.isOneTime(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String taskName = "   ";
        String description = "\\t\\n  ";
        String categoryId = "  \\r\\n";
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals("   ", inputData.getTaskName(), "Whitespace task name should be preserved");
        assertEquals("\\t\\n  ", inputData.getDescription(), "Whitespace description should be preserved");
        assertEquals("  \\r\\n", inputData.getCategoryId(), "Whitespace category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String taskName = "Task!@#$%^&*()";
        String description = "Description with symbols: <>?\"{}|";
        String categoryId = "cat-123!@#";
        boolean isOneTime = false;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Special characters in task name should be preserved");
        assertEquals(description, inputData.getDescription(), "Special characters in description should be preserved");
        assertEquals(categoryId, inputData.getCategoryId(), "Special characters in category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String taskName = "任务 Task 😀";
        String description = "描述 Description 🎯";
        String categoryId = "类别-123";
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Unicode characters in task name should be preserved");
        assertEquals(description, inputData.getDescription(), "Unicode characters in description should be preserved");
        assertEquals(categoryId, inputData.getCategoryId(), "Unicode characters in category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String taskName = "Task name " + "x".repeat(100);
        String description = "Very long description " + "A".repeat(200);
        String categoryId = "category-" + "y".repeat(50);
        boolean isOneTime = false;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Long task name should be preserved");
        assertEquals(description, inputData.getDescription(), "Long description should be preserved");
        assertEquals(categoryId, inputData.getCategoryId(), "Long category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric strings")
    void testNumericStrings() {
        // Given
        String taskName = "123456789";
        String description = "Task 456 description";
        String categoryId = "789";
        boolean isOneTime = true;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Numeric task name should be preserved");
        assertEquals(description, inputData.getDescription(), "Alphanumeric description should be preserved");
        assertEquals(categoryId, inputData.getCategoryId(), "Numeric category ID should be preserved");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String taskName = "A";
        String description = "B";
        String categoryId = "C";
        boolean isOneTime = false;

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, isOneTime);

        // Then
        assertEquals("A", inputData.getTaskName(), "Single character task name should be preserved");
        assertEquals("B", inputData.getDescription(), "Single character description should be preserved");
        assertEquals("C", inputData.getCategoryId(), "Single character category ID should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalTaskName = "Test task";
        String originalDescription = "Test description";
        String originalCategoryId = "cat-123";
        boolean originalIsOneTime = true;
        
        CreateTaskInputData inputData = new CreateTaskInputData(originalTaskName, originalDescription, originalCategoryId, originalIsOneTime);

        // Then
        assertEquals(originalTaskName, inputData.getTaskName(), "Task name should remain unchanged");
        assertEquals(originalDescription, inputData.getDescription(), "Description should remain unchanged");
        assertEquals(originalCategoryId, inputData.getCategoryId(), "Category ID should remain unchanged");
        assertEquals(originalIsOneTime, inputData.isOneTime(), "One-time flag should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        CreateTaskInputData inputData1 = new CreateTaskInputData("Task 1", "Description 1", "cat-1", true);
        CreateTaskInputData inputData2 = new CreateTaskInputData("Task 2", "Description 2", "cat-2", false);

        // Then
        assertNotEquals(inputData1.getTaskName(), inputData2.getTaskName(), "Different instances should have different task names");
        assertNotEquals(inputData1.getDescription(), inputData2.getDescription(), "Different instances should have different descriptions");
        assertNotEquals(inputData1.getCategoryId(), inputData2.getCategoryId(), "Different instances should have different category IDs");
        assertNotEquals(inputData1.isOneTime(), inputData2.isOneTime(), "Different instances should have different one-time flags");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string references")
    void testStringReference() {
        // Given
        String taskName = "Test task";
        String description = "Test description";
        String categoryId = "cat-123";

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, true);

        // Then
        assertSame(taskName, inputData.getTaskName(), "Should preserve exact task name string reference");
        assertSame(description, inputData.getDescription(), "Should preserve exact description string reference");
        assertSame(categoryId, inputData.getCategoryId(), "Should preserve exact category ID string reference");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        CreateTaskInputData inputData = new CreateTaskInputData("Test task", "Test description", "cat-123", true);

        // Then - Multiple calls should return same values
        assertEquals(inputData.getTaskName(), inputData.getTaskName(), "getTaskName should be consistent");
        assertEquals(inputData.getDescription(), inputData.getDescription(), "getDescription should be consistent");
        assertEquals(inputData.getCategoryId(), inputData.getCategoryId(), "getCategoryId should be consistent");
        assertEquals(inputData.isOneTime(), inputData.isOneTime(), "isOneTime should be consistent");
    }

    @Test
    @DisplayName("Should handle both boolean values comprehensively")
    void testBooleanValues() {
        // Test true case
        CreateTaskInputData trueData = new CreateTaskInputData("Task 1", "Description 1", "cat-1", true);
        assertTrue(trueData.isOneTime(), "True case should return true");

        // Test false case
        CreateTaskInputData falseData = new CreateTaskInputData("Task 2", "Description 2", "cat-2", false);
        assertFalse(falseData.isOneTime(), "False case should return false");
    }

    @Test
    @DisplayName("Should handle mixed case strings")
    void testMixedCaseStrings() {
        // Given
        String taskName = "TeSt TaSk NaMe";
        String description = "MiXeD cAsE dEsCrIpTiOn";
        String categoryId = "CaT-123-MiXeD";

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, true);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Mixed case task name should be preserved exactly");
        assertEquals(description, inputData.getDescription(), "Mixed case description should be preserved exactly");
        assertEquals(categoryId, inputData.getCategoryId(), "Mixed case category ID should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test null task name with true boolean
        CreateTaskInputData nullTrue = new CreateTaskInputData(null, "desc", "cat", true);
        assertNull(nullTrue.getTaskName());
        assertTrue(nullTrue.isOneTime());

        // Test empty strings with false boolean
        CreateTaskInputData emptyFalse = new CreateTaskInputData("", "", "", false);
        assertEquals("", emptyFalse.getTaskName());
        assertFalse(emptyFalse.isOneTime());
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces")
    void testLeadingAndTrailingSpaces() {
        // Given
        String taskName = "  Test Task  ";
        String description = "  Test Description  ";
        String categoryId = "  cat-123  ";

        // When
        CreateTaskInputData inputData = new CreateTaskInputData(taskName, description, categoryId, true);

        // Then
        assertEquals(taskName, inputData.getTaskName(), "Leading/trailing spaces in task name should be preserved");
        assertEquals(description, inputData.getDescription(), "Leading/trailing spaces in description should be preserved");
        assertEquals(categoryId, inputData.getCategoryId(), "Leading/trailing spaces in category ID should be preserved");
    }
}