package use_case.Angela.task.remove_from_today;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for RemoveFromTodayOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class RemoveFromTodayOutputDataTest {

    @Test
    @DisplayName("Should create output data with valid parameters")
    void testValidCreation() {
        // Given
        String taskId = "task-123";
        String taskName = "Complete report";
        String message = "Task removed from today's list successfully";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should match");
        assertEquals(taskName, outputData.getTaskName(), "Task name should match");
        assertEquals(message, outputData.getMessage(), "Message should match");
    }

    @Test
    @DisplayName("Should accept null task ID")
    void testNullTaskId() {
        // Given
        String taskId = null;
        String taskName = "Complete report";
        String message = "Task removed successfully";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertNull(outputData.getTaskId(), "Task ID should be null");
        assertEquals(taskName, outputData.getTaskName(), "Task name should be preserved");
        assertEquals(message, outputData.getMessage(), "Message should be preserved");
    }

    @Test
    @DisplayName("Should accept null task name")
    void testNullTaskName() {
        // Given
        String taskId = "task-123";
        String taskName = null;
        String message = "Task removed successfully";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should be preserved");
        assertNull(outputData.getTaskName(), "Task name should be null");
        assertEquals(message, outputData.getMessage(), "Message should be preserved");
    }

    @Test
    @DisplayName("Should accept null message")
    void testNullMessage() {
        // Given
        String taskId = "task-123";
        String taskName = "Complete report";
        String message = null;

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Task name should be preserved");
        assertNull(outputData.getMessage(), "Message should be null");
    }

    @Test
    @DisplayName("Should accept all null parameters")
    void testAllNull() {
        // Given
        String taskId = null;
        String taskName = null;
        String message = null;

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertNull(outputData.getTaskId(), "Task ID should be null");
        assertNull(outputData.getTaskName(), "Task name should be null");
        assertNull(outputData.getMessage(), "Message should be null");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String taskId = "";
        String taskName = "";
        String message = "";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals("", outputData.getTaskId(), "Empty task ID should be preserved");
        assertEquals("", outputData.getTaskName(), "Empty task name should be preserved");
        assertEquals("", outputData.getMessage(), "Empty message should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String taskId = "   ";
        String taskName = "\\t\\n  ";
        String message = "  \\r\\n";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals("   ", outputData.getTaskId(), "Whitespace task ID should be preserved");
        assertEquals("\\t\\n  ", outputData.getTaskName(), "Whitespace task name should be preserved");
        assertEquals("  \\r\\n", outputData.getMessage(), "Whitespace message should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String taskId = "task-123!@#$%";
        String taskName = "Complete & Submit Report!";
        String message = "Task 'Complete Report' removed successfully: <>?\"{}|";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Special characters in task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Special characters in task name should be preserved");
        assertEquals(message, outputData.getMessage(), "Special characters in message should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String taskId = "任务-123";
        String taskName = "完成报告 😀";
        String message = "任务已成功移除 🎯";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Unicode characters in task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Unicode characters in task name should be preserved");
        assertEquals(message, outputData.getMessage(), "Unicode characters in message should be preserved");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String taskId = "task-" + "x".repeat(100);
        String taskName = "Very long task name " + "A".repeat(150);
        String message = "Very long success message " + "B".repeat(200);

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Long task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Long task name should be preserved");
        assertEquals(message, outputData.getMessage(), "Long message should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric strings")
    void testNumericStrings() {
        // Given
        String taskId = "123456789";
        String taskName = "Task123";
        String message = "Message 456";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Numeric task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Alphanumeric task name should be preserved");
        assertEquals(message, outputData.getMessage(), "Alphanumeric message should be preserved");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String taskId = "a";
        String taskName = "b";
        String message = "c";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals("a", outputData.getTaskId(), "Single character task ID should be preserved");
        assertEquals("b", outputData.getTaskName(), "Single character task name should be preserved");
        assertEquals("c", outputData.getMessage(), "Single character message should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalTaskId = "task-123";
        String originalTaskName = "Complete report";
        String originalMessage = "Task removed successfully";
        
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(originalTaskId, originalTaskName, originalMessage);

        // Then
        assertEquals(originalTaskId, outputData.getTaskId(), "Task ID should remain unchanged");
        assertEquals(originalTaskName, outputData.getTaskName(), "Task name should remain unchanged");
        assertEquals(originalMessage, outputData.getMessage(), "Message should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        RemoveFromTodayOutputData outputData1 = new RemoveFromTodayOutputData("task-1", "Task 1", "Message 1");
        RemoveFromTodayOutputData outputData2 = new RemoveFromTodayOutputData("task-2", "Task 2", "Message 2");

        // Then
        assertNotEquals(outputData1.getTaskId(), outputData2.getTaskId(), "Different instances should have different task IDs");
        assertNotEquals(outputData1.getTaskName(), outputData2.getTaskName(), "Different instances should have different task names");
        assertNotEquals(outputData1.getMessage(), outputData2.getMessage(), "Different instances should have different messages");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string references")
    void testStringReference() {
        // Given
        String taskId = "task-123";
        String taskName = "Complete report";
        String message = "Task removed successfully";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertSame(taskId, outputData.getTaskId(), "Should preserve exact task ID string reference");
        assertSame(taskName, outputData.getTaskName(), "Should preserve exact task name string reference");
        assertSame(message, outputData.getMessage(), "Should preserve exact message string reference");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-123", "Complete report", "Task removed successfully");

        // Then - Multiple calls should return same values
        assertEquals(outputData.getTaskId(), outputData.getTaskId(), "getTaskId should be consistent");
        assertEquals(outputData.getTaskName(), outputData.getTaskName(), "getTaskName should be consistent");
        assertEquals(outputData.getMessage(), outputData.getMessage(), "getMessage should be consistent");
    }

    @Test
    @DisplayName("Should handle mixed case strings")
    void testMixedCaseStrings() {
        // Given
        String taskId = "TaSk-123-MiXeD";
        String taskName = "CoMpLeTe RePoRt";
        String message = "TaSk ReMoVeD sUcCeSSfUlLy";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Mixed case task ID should be preserved exactly");
        assertEquals(taskName, outputData.getTaskName(), "Mixed case task name should be preserved exactly");
        assertEquals(message, outputData.getMessage(), "Mixed case message should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle success and error messages")
    void testDifferentMessageTypes() {
        // Test success message
        RemoveFromTodayOutputData successData = new RemoveFromTodayOutputData("task-1", "Task 1", "Task removed successfully");
        assertEquals("Task removed successfully", successData.getMessage());

        // Test error message
        RemoveFromTodayOutputData errorData = new RemoveFromTodayOutputData("task-2", "Task 2", "Error: Task could not be removed");
        assertEquals("Error: Task could not be removed", errorData.getMessage());
    }

    @Test
    @DisplayName("Should handle multiline messages")
    void testMultilineMessage() {
        // Given
        String taskId = "task-123";
        String taskName = "Complete report";
        String message = "Task removed successfully.\\nTask is no longer in today's list.\\nOperation completed.";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Task name should be preserved");
        assertEquals(message, outputData.getMessage(), "Multiline message should be preserved");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test with null task ID, empty name, whitespace message
        RemoveFromTodayOutputData combo1 = new RemoveFromTodayOutputData(null, "", "   ");
        assertNull(combo1.getTaskId());
        assertEquals("", combo1.getTaskName());
        assertEquals("   ", combo1.getMessage());

        // Test with empty task ID, null name, single char message
        RemoveFromTodayOutputData combo2 = new RemoveFromTodayOutputData("", null, "A");
        assertEquals("", combo2.getTaskId());
        assertNull(combo2.getTaskName());
        assertEquals("A", combo2.getMessage());
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces")
    void testLeadingAndTrailingSpaces() {
        // Given
        String taskId = "  task-123  ";
        String taskName = "  Complete Report  ";
        String message = "  Task removed successfully  ";

        // When
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData(taskId, taskName, message);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Leading/trailing spaces in task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Leading/trailing spaces in task name should be preserved");
        assertEquals(message, outputData.getMessage(), "Leading/trailing spaces in message should be preserved");
    }
}