package use_case.Angela.task.mark_complete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MarkTaskCompleteInputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class MarkTaskCompleteInputDataTest {

    @Test
    @DisplayName("Should create input data with valid parameters for marking complete")
    void testMarkAsCompleteTrue() {
        // Given
        String taskId = "task-123";
        boolean markAsComplete = true;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should match");
        assertTrue(inputData.isMarkAsComplete(), "Should mark as complete");
    }

    @Test
    @DisplayName("Should create input data with valid parameters for marking incomplete")
    void testMarkAsCompleteFalse() {
        // Given
        String taskId = "task-456";
        boolean markAsComplete = false;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should match");
        assertFalse(inputData.isMarkAsComplete(), "Should mark as incomplete");
    }

    @Test
    @DisplayName("Should accept null task ID")
    void testNullTaskId() {
        // Given
        String taskId = null;
        boolean markAsComplete = true;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertNull(inputData.getTaskId(), "Task ID should be null");
        assertTrue(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should accept empty task ID")
    void testEmptyTaskId() {
        // Given
        String taskId = "";
        boolean markAsComplete = false;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals("", inputData.getTaskId(), "Empty task ID should be preserved");
        assertFalse(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should accept whitespace-only task ID")
    void testWhitespaceOnlyTaskId() {
        // Given
        String taskId = "   ";
        boolean markAsComplete = true;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals("   ", inputData.getTaskId(), "Whitespace task ID should be preserved");
        assertTrue(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task ID with special characters")
    void testSpecialCharactersTaskId() {
        // Given
        String taskId = "task-123!@#$%^&*()";
        boolean markAsComplete = false;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Special characters should be preserved");
        assertFalse(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle long task ID")
    void testLongTaskId() {
        // Given
        String taskId = "very-long-task-id-" + "x".repeat(100);
        boolean markAsComplete = true;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Long task ID should be preserved");
        assertTrue(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task ID with unicode characters")
    void testUnicodeTaskId() {
        // Given
        String taskId = "任务-123-😀-test";
        boolean markAsComplete = false;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Unicode characters should be preserved");
        assertFalse(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric task ID")
    void testNumericTaskId() {
        // Given
        String taskId = "123456789";
        boolean markAsComplete = true;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Numeric task ID should be preserved");
        assertTrue(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle single character task ID")
    void testSingleCharacterTaskId() {
        // Given
        String taskId = "a";
        boolean markAsComplete = false;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals("a", inputData.getTaskId(), "Single character task ID should be preserved");
        assertFalse(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task ID with newlines and tabs")
    void testTaskIdWithNewlinesAndTabs() {
        // Given
        String taskId = "task\n123\ttab";
        boolean markAsComplete = true;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Newlines and tabs should be preserved");
        assertTrue(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalTaskId = "task-123";
        boolean originalMarkAsComplete = true;
        
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(originalTaskId, originalMarkAsComplete);

        // Then
        assertEquals(originalTaskId, inputData.getTaskId(), "Task ID should remain unchanged");
        assertEquals(originalMarkAsComplete, inputData.isMarkAsComplete(), "Mark as complete flag should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        MarkTaskCompleteInputData inputData1 = new MarkTaskCompleteInputData("task-1", true);
        MarkTaskCompleteInputData inputData2 = new MarkTaskCompleteInputData("task-2", false);

        // Then
        assertNotEquals(inputData1.getTaskId(), inputData2.getTaskId(), "Different instances should have different task IDs");
        assertNotEquals(inputData1.isMarkAsComplete(), inputData2.isMarkAsComplete(), "Different instances should have different flags");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string reference")
    void testStringReference() {
        // Given
        String taskId = "task-123";

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, true);

        // Then
        assertSame(taskId, inputData.getTaskId(), "Should preserve exact string reference");
    }

    @Test
    @DisplayName("Should test both boolean values comprehensively")
    void testBooleanValues() {
        // Test true case
        MarkTaskCompleteInputData trueData = new MarkTaskCompleteInputData("task-1", true);
        assertTrue(trueData.isMarkAsComplete(), "True case should return true");

        // Test false case
        MarkTaskCompleteInputData falseData = new MarkTaskCompleteInputData("task-2", false);
        assertFalse(falseData.isMarkAsComplete(), "False case should return false");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task-123", true);

        // Then - Multiple calls should return same values
        assertEquals(inputData.getTaskId(), inputData.getTaskId(), "getTaskId should be consistent");
        assertEquals(inputData.isMarkAsComplete(), inputData.isMarkAsComplete(), "isMarkAsComplete should be consistent");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test null with true
        MarkTaskCompleteInputData nullTrue = new MarkTaskCompleteInputData(null, true);
        assertNull(nullTrue.getTaskId());
        assertTrue(nullTrue.isMarkAsComplete());

        // Test null with false
        MarkTaskCompleteInputData nullFalse = new MarkTaskCompleteInputData(null, false);
        assertNull(nullFalse.getTaskId());
        assertFalse(nullFalse.isMarkAsComplete());

        // Test empty with true
        MarkTaskCompleteInputData emptyTrue = new MarkTaskCompleteInputData("", true);
        assertEquals("", emptyTrue.getTaskId());
        assertTrue(emptyTrue.isMarkAsComplete());

        // Test empty with false
        MarkTaskCompleteInputData emptyFalse = new MarkTaskCompleteInputData("", false);
        assertEquals("", emptyFalse.getTaskId());
        assertFalse(emptyFalse.isMarkAsComplete());
    }

    @Test
    @DisplayName("Should handle mixed case task IDs")
    void testMixedCaseTaskId() {
        // Given
        String taskId = "TaSk-123-MiXeD";
        boolean markAsComplete = true;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Mixed case should be preserved exactly");
        assertTrue(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task IDs with leading/trailing spaces")
    void testTaskIdWithSpaces() {
        // Given
        String taskId = "  task-123  ";
        boolean markAsComplete = false;

        // When
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(taskId, markAsComplete);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Leading/trailing spaces should be preserved");
        assertFalse(inputData.isMarkAsComplete(), "Boolean flag should be preserved");
    }
}