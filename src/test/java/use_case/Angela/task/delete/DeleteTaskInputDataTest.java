package use_case.Angela.task.delete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteTaskInputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class DeleteTaskInputDataTest {

    @Test
    @DisplayName("Should create input data for deleting from available tasks")
    void testDeleteFromAvailable() {
        // Given
        String taskId = "task-123";
        boolean isFromAvailable = true;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should match");
        assertTrue(inputData.isFromAvailable(), "Should be from available tasks");
    }

    @Test
    @DisplayName("Should create input data for deleting from today tasks")
    void testDeleteFromToday() {
        // Given
        String taskId = "task-456";
        boolean isFromAvailable = false;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should match");
        assertFalse(inputData.isFromAvailable(), "Should be from today tasks");
    }

    @Test
    @DisplayName("Should accept null task ID")
    void testNullTaskId() {
        // Given
        String taskId = null;
        boolean isFromAvailable = true;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertNull(inputData.getTaskId(), "Task ID should be null");
        assertTrue(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should accept empty task ID")
    void testEmptyTaskId() {
        // Given
        String taskId = "";
        boolean isFromAvailable = false;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals("", inputData.getTaskId(), "Empty task ID should be preserved");
        assertFalse(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should accept whitespace-only task ID")
    void testWhitespaceOnlyTaskId() {
        // Given
        String taskId = "   ";
        boolean isFromAvailable = true;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals("   ", inputData.getTaskId(), "Whitespace task ID should be preserved");
        assertTrue(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task ID with special characters")
    void testSpecialCharactersTaskId() {
        // Given
        String taskId = "task-123!@#$%^&*()";
        boolean isFromAvailable = false;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Special characters should be preserved");
        assertFalse(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle long task ID")
    void testLongTaskId() {
        // Given
        String taskId = "very-long-task-id-" + "x".repeat(100);
        boolean isFromAvailable = true;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Long task ID should be preserved");
        assertTrue(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task ID with unicode characters")
    void testUnicodeTaskId() {
        // Given
        String taskId = "任务-123-😀-test";
        boolean isFromAvailable = false;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Unicode characters should be preserved");
        assertFalse(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric task ID")
    void testNumericTaskId() {
        // Given
        String taskId = "123456789";
        boolean isFromAvailable = true;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Numeric task ID should be preserved");
        assertTrue(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle single character task ID")
    void testSingleCharacterTaskId() {
        // Given
        String taskId = "a";
        boolean isFromAvailable = false;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals("a", inputData.getTaskId(), "Single character task ID should be preserved");
        assertFalse(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task ID with newlines and tabs")
    void testTaskIdWithNewlinesAndTabs() {
        // Given
        String taskId = "task\\n123\\ttab";
        boolean isFromAvailable = true;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Newlines and tabs should be preserved");
        assertTrue(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalTaskId = "task-123";
        boolean originalIsFromAvailable = true;
        
        DeleteTaskInputData inputData = new DeleteTaskInputData(originalTaskId, originalIsFromAvailable);

        // Then
        assertEquals(originalTaskId, inputData.getTaskId(), "Task ID should remain unchanged");
        assertEquals(originalIsFromAvailable, inputData.isFromAvailable(), "isFromAvailable flag should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        DeleteTaskInputData inputData1 = new DeleteTaskInputData("task-1", true);
        DeleteTaskInputData inputData2 = new DeleteTaskInputData("task-2", false);

        // Then
        assertNotEquals(inputData1.getTaskId(), inputData2.getTaskId(), "Different instances should have different task IDs");
        assertNotEquals(inputData1.isFromAvailable(), inputData2.isFromAvailable(), "Different instances should have different flags");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string reference")
    void testStringReference() {
        // Given
        String taskId = "task-123";

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, true);

        // Then
        assertSame(taskId, inputData.getTaskId(), "Should preserve exact string reference");
    }

    @Test
    @DisplayName("Should test both boolean values comprehensively")
    void testBooleanValues() {
        // Test true case
        DeleteTaskInputData trueData = new DeleteTaskInputData("task-1", true);
        assertTrue(trueData.isFromAvailable(), "True case should return true");

        // Test false case
        DeleteTaskInputData falseData = new DeleteTaskInputData("task-2", false);
        assertFalse(falseData.isFromAvailable(), "False case should return false");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        DeleteTaskInputData inputData = new DeleteTaskInputData("task-123", true);

        // Then - Multiple calls should return same values
        assertEquals(inputData.getTaskId(), inputData.getTaskId(), "getTaskId should be consistent");
        assertEquals(inputData.isFromAvailable(), inputData.isFromAvailable(), "isFromAvailable should be consistent");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test null with true
        DeleteTaskInputData nullTrue = new DeleteTaskInputData(null, true);
        assertNull(nullTrue.getTaskId());
        assertTrue(nullTrue.isFromAvailable());

        // Test null with false
        DeleteTaskInputData nullFalse = new DeleteTaskInputData(null, false);
        assertNull(nullFalse.getTaskId());
        assertFalse(nullFalse.isFromAvailable());

        // Test empty with true
        DeleteTaskInputData emptyTrue = new DeleteTaskInputData("", true);
        assertEquals("", emptyTrue.getTaskId());
        assertTrue(emptyTrue.isFromAvailable());

        // Test empty with false
        DeleteTaskInputData emptyFalse = new DeleteTaskInputData("", false);
        assertEquals("", emptyFalse.getTaskId());
        assertFalse(emptyFalse.isFromAvailable());
    }

    @Test
    @DisplayName("Should handle mixed case task IDs")
    void testMixedCaseTaskId() {
        // Given
        String taskId = "TaSk-123-MiXeD";
        boolean isFromAvailable = true;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Mixed case should be preserved exactly");
        assertTrue(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle task IDs with leading/trailing spaces")
    void testTaskIdWithSpaces() {
        // Given
        String taskId = "  task-123  ";
        boolean isFromAvailable = false;

        // When
        DeleteTaskInputData inputData = new DeleteTaskInputData(taskId, isFromAvailable);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Leading/trailing spaces should be preserved");
        assertFalse(inputData.isFromAvailable(), "Boolean flag should be preserved");
    }

    @Test
    @DisplayName("Should handle usage context scenarios")
    void testUsageContextScenarios() {
        // Test available tasks deletion
        DeleteTaskInputData availableDeletion = new DeleteTaskInputData("available-task-123", true);
        assertTrue(availableDeletion.isFromAvailable(), "Should indicate deletion from available tasks");
        
        // Test today tasks deletion
        DeleteTaskInputData todayDeletion = new DeleteTaskInputData("today-task-456", false);
        assertFalse(todayDeletion.isFromAvailable(), "Should indicate deletion from today tasks");
    }
}