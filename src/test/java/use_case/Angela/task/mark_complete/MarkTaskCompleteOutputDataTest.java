package use_case.Angela.task.mark_complete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MarkTaskCompleteOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class MarkTaskCompleteOutputDataTest {

    @Test
    @DisplayName("Should create output data with valid parameters")
    void testValidCreation() {
        // Given
        String taskId = "task-123";
        String taskName = "Complete report";
        LocalDateTime completionTime = LocalDateTime.of(2024, 12, 15, 10, 30);
        double completionRate = 0.85;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should match");
        assertEquals(taskName, outputData.getTaskName(), "Task name should match");
        assertEquals(completionTime, outputData.getCompletionTime(), "Completion time should match");
        assertEquals(completionRate, outputData.getCompletionRate(), "Completion rate should match");
    }

    @Test
    @DisplayName("Should accept null task ID")
    void testNullTaskId() {
        // Given
        String taskId = null;
        String taskName = "Complete report";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 1.0;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertNull(outputData.getTaskId(), "Task ID should be null");
        assertEquals(taskName, outputData.getTaskName(), "Task name should be preserved");
        assertEquals(completionTime, outputData.getCompletionTime(), "Completion time should be preserved");
        assertEquals(completionRate, outputData.getCompletionRate(), "Completion rate should be preserved");
    }

    @Test
    @DisplayName("Should accept null task name")
    void testNullTaskName() {
        // Given
        String taskId = "task-123";
        String taskName = null;
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.75;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should be preserved");
        assertNull(outputData.getTaskName(), "Task name should be null");
        assertEquals(completionTime, outputData.getCompletionTime(), "Completion time should be preserved");
        assertEquals(completionRate, outputData.getCompletionRate(), "Completion rate should be preserved");
    }

    @Test
    @DisplayName("Should accept null completion time")
    void testNullCompletionTime() {
        // Given
        String taskId = "task-123";
        String taskName = "Complete report";
        LocalDateTime completionTime = null;
        double completionRate = 0.9;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Task name should be preserved");
        assertNull(outputData.getCompletionTime(), "Completion time should be null");
        assertEquals(completionRate, outputData.getCompletionRate(), "Completion rate should be preserved");
    }

    @Test
    @DisplayName("Should accept all null parameters except completion rate")
    void testAllNullExceptRate() {
        // Given
        String taskId = null;
        String taskName = null;
        LocalDateTime completionTime = null;
        double completionRate = 0.5;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertNull(outputData.getTaskId(), "Task ID should be null");
        assertNull(outputData.getTaskName(), "Task name should be null");
        assertNull(outputData.getCompletionTime(), "Completion time should be null");
        assertEquals(completionRate, outputData.getCompletionRate(), "Completion rate should be preserved");
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        // Given
        String taskId = "";
        String taskName = "";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.0;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals("", outputData.getTaskId(), "Empty task ID should be preserved");
        assertEquals("", outputData.getTaskName(), "Empty task name should be preserved");
        assertEquals(completionTime, outputData.getCompletionTime(), "Completion time should be preserved");
        assertEquals(0.0, outputData.getCompletionRate(), "Zero completion rate should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only strings")
    void testWhitespaceOnlyStrings() {
        // Given
        String taskId = "   ";
        String taskName = "\\t\\n  ";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 1.0;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals("   ", outputData.getTaskId(), "Whitespace task ID should be preserved");
        assertEquals("\\t\\n  ", outputData.getTaskName(), "Whitespace task name should be preserved");
        assertEquals(1.0, outputData.getCompletionRate(), "Maximum completion rate should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters")
    void testSpecialCharacters() {
        // Given
        String taskId = "task-123!@#$%";
        String taskName = "Complete & Submit Report!";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.95;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Special characters in task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Special characters in task name should be preserved");
        assertEquals(0.95, outputData.getCompletionRate(), "Completion rate should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testUnicodeCharacters() {
        // Given
        String taskId = "任务-123";
        String taskName = "完成报告 😀";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.88;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Unicode characters in task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Unicode characters in task name should be preserved");
        assertEquals(0.88, outputData.getCompletionRate(), "Completion rate should be preserved");
    }

    @Test
    @DisplayName("Should handle various completion rates")
    void testVariousCompletionRates() {
        LocalDateTime now = LocalDateTime.now();
        
        // Test 0.0 rate
        MarkTaskCompleteOutputData zeroRate = new MarkTaskCompleteOutputData("task-1", "Task 1", now, 0.0);
        assertEquals(0.0, zeroRate.getCompletionRate(), "Zero completion rate should be preserved");

        // Test 1.0 rate  
        MarkTaskCompleteOutputData fullRate = new MarkTaskCompleteOutputData("task-2", "Task 2", now, 1.0);
        assertEquals(1.0, fullRate.getCompletionRate(), "Full completion rate should be preserved");

        // Test fractional rate
        MarkTaskCompleteOutputData fractionalRate = new MarkTaskCompleteOutputData("task-3", "Task 3", now, 0.333333);
        assertEquals(0.333333, fractionalRate.getCompletionRate(), "Fractional completion rate should be preserved");

        // Test negative rate (business rule violation but technically possible)
        MarkTaskCompleteOutputData negativeRate = new MarkTaskCompleteOutputData("task-4", "Task 4", now, -0.1);
        assertEquals(-0.1, negativeRate.getCompletionRate(), "Negative completion rate should be preserved");

        // Test rate > 1.0 (business rule violation but technically possible)
        MarkTaskCompleteOutputData overRate = new MarkTaskCompleteOutputData("task-5", "Task 5", now, 1.5);
        assertEquals(1.5, overRate.getCompletionRate(), "Over-completion rate should be preserved");
    }

    @Test
    @DisplayName("Should handle various LocalDateTime values")
    void testVariousDateTimes() {
        String taskId = "task-123";
        String taskName = "Test task";
        double completionRate = 0.8;

        // Test past datetime
        LocalDateTime pastTime = LocalDateTime.of(2020, 1, 1, 8, 0);
        MarkTaskCompleteOutputData pastData = new MarkTaskCompleteOutputData(taskId, taskName, pastTime, completionRate);
        assertEquals(pastTime, pastData.getCompletionTime(), "Past completion time should be preserved");

        // Test current datetime
        LocalDateTime currentTime = LocalDateTime.now();
        MarkTaskCompleteOutputData currentData = new MarkTaskCompleteOutputData(taskId, taskName, currentTime, completionRate);
        assertEquals(currentTime, currentData.getCompletionTime(), "Current completion time should be preserved");

        // Test future datetime
        LocalDateTime futureTime = LocalDateTime.of(2030, 12, 31, 23, 59);
        MarkTaskCompleteOutputData futureData = new MarkTaskCompleteOutputData(taskId, taskName, futureTime, completionRate);
        assertEquals(futureTime, futureData.getCompletionTime(), "Future completion time should be preserved");

        // Test datetime with microseconds
        LocalDateTime preciseTime = LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456789);
        MarkTaskCompleteOutputData preciseData = new MarkTaskCompleteOutputData(taskId, taskName, preciseTime, completionRate);
        assertEquals(preciseTime, preciseData.getCompletionTime(), "Precise completion time should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalTaskId = "task-123";
        String originalTaskName = "Complete report";
        LocalDateTime originalCompletionTime = LocalDateTime.of(2024, 12, 15, 10, 30);
        double originalCompletionRate = 0.85;
        
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(originalTaskId, originalTaskName, originalCompletionTime, originalCompletionRate);

        // Then
        assertEquals(originalTaskId, outputData.getTaskId(), "Task ID should remain unchanged");
        assertEquals(originalTaskName, outputData.getTaskName(), "Task name should remain unchanged");
        assertEquals(originalCompletionTime, outputData.getCompletionTime(), "Completion time should remain unchanged");
        assertEquals(originalCompletionRate, outputData.getCompletionRate(), "Completion rate should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        MarkTaskCompleteOutputData outputData1 = new MarkTaskCompleteOutputData("task-1", "Task 1", LocalDateTime.now(), 0.8);
        MarkTaskCompleteOutputData outputData2 = new MarkTaskCompleteOutputData("task-2", "Task 2", LocalDateTime.now().plusHours(1), 0.9);

        // Then
        assertNotEquals(outputData1.getTaskId(), outputData2.getTaskId(), "Different instances should have different task IDs");
        assertNotEquals(outputData1.getTaskName(), outputData2.getTaskName(), "Different instances should have different task names");
        assertNotEquals(outputData1.getCompletionTime(), outputData2.getCompletionTime(), "Different instances should have different completion times");
        assertNotEquals(outputData1.getCompletionRate(), outputData2.getCompletionRate(), "Different instances should have different completion rates");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact references")
    void testReferencePreservation() {
        // Given
        String taskId = "task-123";
        String taskName = "Complete report";
        LocalDateTime completionTime = LocalDateTime.now();

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, 0.85);

        // Then
        assertSame(taskId, outputData.getTaskId(), "Should preserve exact task ID string reference");
        assertSame(taskName, outputData.getTaskName(), "Should preserve exact task name string reference");
        assertSame(completionTime, outputData.getCompletionTime(), "Should preserve exact completion time reference");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData("task-123", "Complete report", LocalDateTime.now(), 0.85);

        // Then - Multiple calls should return same values
        assertEquals(outputData.getTaskId(), outputData.getTaskId(), "getTaskId should be consistent");
        assertEquals(outputData.getTaskName(), outputData.getTaskName(), "getTaskName should be consistent");
        assertEquals(outputData.getCompletionTime(), outputData.getCompletionTime(), "getCompletionTime should be consistent");
        assertEquals(outputData.getCompletionRate(), outputData.getCompletionRate(), "getCompletionRate should be consistent");
    }

    @Test
    @DisplayName("Should handle mixed case strings")
    void testMixedCaseStrings() {
        // Given
        String taskId = "TaSk-123-MiXeD";
        String taskName = "CoMpLeTe RePoRt";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.77;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Mixed case task ID should be preserved exactly");
        assertEquals(taskName, outputData.getTaskName(), "Mixed case task name should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String taskId = "task-" + "x".repeat(100);
        String taskName = "Very long task name " + "A".repeat(150);
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.99;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Long task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Long task name should be preserved");
    }

    @Test
    @DisplayName("Should handle single character strings")
    void testSingleCharacterStrings() {
        // Given
        String taskId = "a";
        String taskName = "b";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.5;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals("a", outputData.getTaskId(), "Single character task ID should be preserved");
        assertEquals("b", outputData.getTaskName(), "Single character task name should be preserved");
    }

    @Test
    @DisplayName("Should handle extreme completion rate values")
    void testExtremeCompletionRates() {
        LocalDateTime now = LocalDateTime.now();
        
        // Test very small positive value
        MarkTaskCompleteOutputData smallRate = new MarkTaskCompleteOutputData("task-1", "Task 1", now, 0.0001);
        assertEquals(0.0001, smallRate.getCompletionRate(), "Very small completion rate should be preserved");

        // Test very large value
        MarkTaskCompleteOutputData largeRate = new MarkTaskCompleteOutputData("task-2", "Task 2", now, 999.999);
        assertEquals(999.999, largeRate.getCompletionRate(), "Very large completion rate should be preserved");

        // Test negative value
        MarkTaskCompleteOutputData negativeRate = new MarkTaskCompleteOutputData("task-3", "Task 3", now, -100.5);
        assertEquals(-100.5, negativeRate.getCompletionRate(), "Negative completion rate should be preserved");
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces")
    void testLeadingAndTrailingSpaces() {
        // Given
        String taskId = "  task-123  ";
        String taskName = "  Complete Report  ";
        LocalDateTime completionTime = LocalDateTime.now();
        double completionRate = 0.85;

        // When
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, taskName, completionTime, completionRate);

        // Then
        assertEquals(taskId, outputData.getTaskId(), "Leading/trailing spaces in task ID should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Leading/trailing spaces in task name should be preserved");
    }
}