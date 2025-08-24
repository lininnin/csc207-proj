package use_case.Angela.task.delete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteTaskOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class DeleteTaskOutputDataTest {

    @Test
    @DisplayName("Should create output data with valid parameters for deletion from both")
    void testValidCreationDeletedFromBoth() {
        // Given
        String taskId = "task-123";
        String message = "Task deleted successfully from both lists";
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should match");
        assertEquals(message, outputData.getMessage(), "Message should match");
        assertTrue(outputData.isDeletedFromBoth(), "Should be deleted from both");
    }

    @Test
    @DisplayName("Should create output data with valid parameters for deletion from single list")
    void testValidCreationDeletedFromSingle() {
        // Given
        String taskId = "task-456";
        String message = "Task deleted successfully";
        boolean deletedFromBoth = false;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should match");
        assertEquals(message, outputData.getMessage(), "Message should match");
        assertFalse(outputData.isDeletedFromBoth(), "Should be deleted from single list");
    }

    @Test
    @DisplayName("Should accept null task ID")
    void testNullTaskId() {
        // Given
        String taskId = null;
        String message = "Task deleted successfully";
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertNull(outputData.getTaskId(), "Task ID should be null");
        assertEquals(message, outputData.getMessage(), "Message should be preserved");
        assertTrue(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should accept null message")
    void testNullMessage() {
        // Given
        String taskId = "task-123";
        String message = null;
        boolean deletedFromBoth = false;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should be preserved");
        assertNull(outputData.getMessage(), "Message should be null");
        assertFalse(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should accept both null string parameters")
    void testBothNullStrings() {
        // Given
        String taskId = null;
        String message = null;
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertNull(outputData.getTaskId(), "Task ID should be null");
        assertNull(outputData.getMessage(), "Message should be null");
        assertTrue(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String taskId = "";
        String message = "";
        boolean deletedFromBoth = false;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals("", outputData.getTaskId(), "Empty task ID should be preserved");
        assertEquals("", outputData.getMessage(), "Empty message should be preserved");
        assertFalse(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String taskId = "   ";
        String message = "\\t\\n  ";
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals("   ", outputData.getTaskId(), "Whitespace task ID should be preserved");
        assertEquals("\\t\\n  ", outputData.getMessage(), "Whitespace message should be preserved");
        assertTrue(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String taskId = "task-123!@#$%";
        String message = "Task 'Complete & Submit' deleted successfully!";
        boolean deletedFromBoth = false;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Special characters in ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Special characters in message should be preserved");
        assertFalse(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String taskId = "任务-123";
        String message = "任务删除成功 😀";
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Unicode characters in ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Unicode characters in message should be preserved");
        assertTrue(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String taskId = "task-" + "x".repeat(100);
        String message = "Very long success message that exceeds normal expectations " + "A".repeat(200);
        boolean deletedFromBoth = false;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Long task ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Long message should be preserved");
        assertFalse(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric strings")
    void testNumericStrings() {
        // Given
        String taskId = "123456789";
        String message = "Task 123 deleted";
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Numeric ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Message with numbers should be preserved");
        assertTrue(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String taskId = "a";
        String message = "b";
        boolean deletedFromBoth = false;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals("a", outputData.getTaskId(), "Single character ID should be preserved");
        assertEquals("b", outputData.getMessage(), "Single character message should be preserved");
        assertFalse(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalTaskId = "task-123";
        String originalMessage = "Task deleted successfully";
        boolean originalDeletedFromBoth = true;
        
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(originalTaskId, originalMessage, originalDeletedFromBoth);

        // Then
        assertEquals(originalTaskId, outputData.getTaskId(), "Task ID should remain unchanged");
        assertEquals(originalMessage, outputData.getMessage(), "Message should remain unchanged");
        assertEquals(originalDeletedFromBoth, outputData.isDeletedFromBoth(), "DeletedFromBoth flag should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        DeleteTaskOutputData outputData1 = new DeleteTaskOutputData("task-1", "Message 1", true);
        DeleteTaskOutputData outputData2 = new DeleteTaskOutputData("task-2", "Message 2", false);

        // Then
        assertNotEquals(outputData1.getTaskId(), outputData2.getTaskId(), "Different instances should have different IDs");
        assertNotEquals(outputData1.getMessage(), outputData2.getMessage(), "Different instances should have different messages");
        assertNotEquals(outputData1.isDeletedFromBoth(), outputData2.isDeletedFromBoth(), "Different instances should have different flags");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string references")
    void testStringReference() {
        // Given
        String taskId = "task-123";
        String message = "Task deleted successfully";

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, true);

        // Then
        assertSame(taskId, outputData.getTaskId(), "Should preserve exact ID string reference");
        assertSame(message, outputData.getMessage(), "Should preserve exact message string reference");
    }

    @Test
    @DisplayName("Should handle both boolean values comprehensively")
    void testBooleanValues() {
        // Test true case
        DeleteTaskOutputData trueData = new DeleteTaskOutputData("task-1", "Message 1", true);
        assertTrue(trueData.isDeletedFromBoth(), "True case should return true");

        // Test false case
        DeleteTaskOutputData falseData = new DeleteTaskOutputData("task-2", "Message 2", false);
        assertFalse(falseData.isDeletedFromBoth(), "False case should return false");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        DeleteTaskOutputData outputData = new DeleteTaskOutputData("task-123", "Task deleted successfully", true);

        // Then - Multiple calls should return same values
        assertEquals(outputData.getTaskId(), outputData.getTaskId(), "getTaskId should be consistent");
        assertEquals(outputData.getMessage(), outputData.getMessage(), "getMessage should be consistent");
        assertEquals(outputData.isDeletedFromBoth(), outputData.isDeletedFromBoth(), "isDeletedFromBoth should be consistent");
    }

    @Test
    @DisplayName("Should handle success and error messages")
    void testDifferentMessageTypes() {
        // Test success message
        DeleteTaskOutputData successData = new DeleteTaskOutputData("task-1", "Task deleted successfully", true);
        assertEquals("Task deleted successfully", successData.getMessage());

        // Test error message
        DeleteTaskOutputData errorData = new DeleteTaskOutputData("task-2", "Error: Task could not be deleted", false);
        assertEquals("Error: Task could not be deleted", errorData.getMessage());
    }

    @Test
    @DisplayName("Should handle multiline messages")
    void testMultilineMessage() {
        // Given
        String taskId = "task-123";
        String message = "Task deleted successfully.\\nRemoved from available list.\\nRemoved from today list.";
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Multiline message should be preserved");
        assertTrue(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle mixed case strings")
    void testMixedCaseStrings() {
        // Given
        String taskId = "TaSk-123-MiXeD";
        String message = "TaSk DeLeTed SuCcEsSfUlLy";
        boolean deletedFromBoth = false;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Mixed case ID should be preserved exactly");
        assertEquals(message, outputData.getMessage(), "Mixed case message should be preserved exactly");
        assertFalse(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test null task ID, empty message, true boolean
        DeleteTaskOutputData combo1 = new DeleteTaskOutputData(null, "", true);
        assertNull(combo1.getTaskId());
        assertEquals("", combo1.getMessage());
        assertTrue(combo1.isDeletedFromBoth());

        // Test empty task ID, null message, false boolean
        DeleteTaskOutputData combo2 = new DeleteTaskOutputData("", null, false);
        assertEquals("", combo2.getTaskId());
        assertNull(combo2.getMessage());
        assertFalse(combo2.isDeletedFromBoth());
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces")
    void testLeadingAndTrailingSpaces() {
        // Given
        String taskId = "  task-123  ";
        String message = "  Task deleted successfully  ";
        boolean deletedFromBoth = true;

        // When
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, deletedFromBoth);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Leading/trailing spaces in ID should be preserved");
        assertEquals(message, outputData.getMessage(), "Leading/trailing spaces in message should be preserved");
        assertTrue(outputData.isDeletedFromBoth(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle deletion scenarios")
    void testDeletionScenarios() {
        // Test deletion from both lists
        DeleteTaskOutputData bothDeletion = new DeleteTaskOutputData("task-1", "Deleted from both available and today lists", true);
        assertTrue(bothDeletion.isDeletedFromBoth(), "Should indicate deletion from both lists");
        
        // Test deletion from single list
        DeleteTaskOutputData singleDeletion = new DeleteTaskOutputData("task-2", "Deleted from available list only", false);
        assertFalse(singleDeletion.isDeletedFromBoth(), "Should indicate deletion from single list");
    }
}