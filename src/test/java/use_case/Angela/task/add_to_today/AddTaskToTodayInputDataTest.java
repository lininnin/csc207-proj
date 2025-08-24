package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AddTaskToTodayInputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class AddTaskToTodayInputDataTest {

    @Test
    @DisplayName("Should create input data with valid parameters using 3-param constructor")
    void testValidCreationThreeParams() {
        // Given
        String taskId = "task-123";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.of(2024, 12, 31);

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should match");
        assertEquals(priority, inputData.getPriority(), "Priority should match");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should match");
        assertFalse(inputData.isTestingOverdue(), "Should default to false for testing overdue");
    }

    @Test
    @DisplayName("Should create input data with valid parameters using 4-param constructor")
    void testValidCreationFourParams() {
        // Given
        String taskId = "task-456";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = LocalDate.of(2024, 6, 15);
        boolean isTestingOverdue = true;

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate, isTestingOverdue);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should match");
        assertEquals(priority, inputData.getPriority(), "Priority should match");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should match");
        assertTrue(inputData.isTestingOverdue(), "Should be testing overdue");
    }

    @Test
    @DisplayName("Should handle all priority levels")
    void testAllPriorityLevels() {
        LocalDate dueDate = LocalDate.now();
        
        // Test HIGH priority
        AddTaskToTodayInputData highData = new AddTaskToTodayInputData("task-high", Task.Priority.HIGH, dueDate);
        assertEquals(Task.Priority.HIGH, highData.getPriority(), "Should handle HIGH priority");

        // Test MEDIUM priority
        AddTaskToTodayInputData mediumData = new AddTaskToTodayInputData("task-med", Task.Priority.MEDIUM, dueDate);
        assertEquals(Task.Priority.MEDIUM, mediumData.getPriority(), "Should handle MEDIUM priority");

        // Test LOW priority
        AddTaskToTodayInputData lowData = new AddTaskToTodayInputData("task-low", Task.Priority.LOW, dueDate);
        assertEquals(Task.Priority.LOW, lowData.getPriority(), "Should handle LOW priority");
    }

    @Test
    @DisplayName("Should accept null task ID")
    void testNullTaskId() {
        // Given
        String taskId = null;
        Task.Priority priority = Task.Priority.LOW;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertNull(inputData.getTaskId(), "Task ID should be null");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }

    @Test
    @DisplayName("Should accept null priority")
    void testNullPriority() {
        // Given
        String taskId = "task-123";
        Task.Priority priority = null;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should be preserved");
        assertNull(inputData.getPriority(), "Priority should be null");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }

    @Test
    @DisplayName("Should accept null due date")
    void testNullDueDate() {
        // Given
        String taskId = "task-123";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = null;

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Task ID should be preserved");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertNull(inputData.getDueDate(), "Due date should be null");
    }

    @Test
    @DisplayName("Should accept all null parameters")
    void testAllNull() {
        // Given
        String taskId = null;
        Task.Priority priority = null;
        LocalDate dueDate = null;
        boolean isTestingOverdue = true;

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate, isTestingOverdue);

        // Then
        assertNull(inputData.getTaskId(), "Task ID should be null");
        assertNull(inputData.getPriority(), "Priority should be null");
        assertNull(inputData.getDueDate(), "Due date should be null");
        assertTrue(inputData.isTestingOverdue(), "Testing overdue flag should be preserved");
    }

    @Test
    @DisplayName("Should handle empty task ID")
    void testEmptyTaskId() {
        // Given
        String taskId = "";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals("", inputData.getTaskId(), "Empty task ID should be preserved");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only task ID")
    void testWhitespaceOnlyTaskId() {
        // Given
        String taskId = "   ";
        Task.Priority priority = Task.Priority.LOW;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals("   ", inputData.getTaskId(), "Whitespace task ID should be preserved");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters in task ID")
    void testSpecialCharactersTaskId() {
        // Given
        String taskId = "task-123!@#$%^&*()";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Special characters should be preserved");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters in task ID")
    void testUnicodeTaskId() {
        // Given
        String taskId = "任务-123-😀-test";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Unicode characters should be preserved");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }

    @Test
    @DisplayName("Should handle various date values")
    void testVariousDateValues() {
        String taskId = "task-123";
        Task.Priority priority = Task.Priority.LOW;

        // Test past date
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        AddTaskToTodayInputData pastData = new AddTaskToTodayInputData(taskId, priority, pastDate);
        assertEquals(pastDate, pastData.getDueDate(), "Past date should be preserved");

        // Test current date
        LocalDate currentDate = LocalDate.now();
        AddTaskToTodayInputData currentData = new AddTaskToTodayInputData(taskId, priority, currentDate);
        assertEquals(currentDate, currentData.getDueDate(), "Current date should be preserved");

        // Test future date
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        AddTaskToTodayInputData futureData = new AddTaskToTodayInputData(taskId, priority, futureDate);
        assertEquals(futureDate, futureData.getDueDate(), "Future date should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        String originalTaskId = "task-123";
        Task.Priority originalPriority = Task.Priority.HIGH;
        LocalDate originalDueDate = LocalDate.of(2024, 12, 31);
        boolean originalIsTestingOverdue = true;
        
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(originalTaskId, originalPriority, originalDueDate, originalIsTestingOverdue);

        // Then
        assertEquals(originalTaskId, inputData.getTaskId(), "Task ID should remain unchanged");
        assertEquals(originalPriority, inputData.getPriority(), "Priority should remain unchanged");
        assertEquals(originalDueDate, inputData.getDueDate(), "Due date should remain unchanged");
        assertEquals(originalIsTestingOverdue, inputData.isTestingOverdue(), "Testing overdue flag should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        AddTaskToTodayInputData inputData1 = new AddTaskToTodayInputData("task-1", Task.Priority.HIGH, LocalDate.of(2024, 1, 1), true);
        AddTaskToTodayInputData inputData2 = new AddTaskToTodayInputData("task-2", Task.Priority.LOW, LocalDate.of(2024, 2, 2), false);

        // Then
        assertNotEquals(inputData1.getTaskId(), inputData2.getTaskId(), "Different instances should have different task IDs");
        assertNotEquals(inputData1.getPriority(), inputData2.getPriority(), "Different instances should have different priorities");
        assertNotEquals(inputData1.getDueDate(), inputData2.getDueDate(), "Different instances should have different due dates");
        assertNotEquals(inputData1.isTestingOverdue(), inputData2.isTestingOverdue(), "Different instances should have different testing overdue flags");
        assertNotSame(inputData1, inputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact string reference")
    void testStringReference() {
        // Given
        String taskId = "task-123";

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, Task.Priority.MEDIUM, LocalDate.now());

        // Then
        assertSame(taskId, inputData.getTaskId(), "Should preserve exact string reference");
    }

    @Test
    @DisplayName("Should preserve exact date reference")
    void testDateReference() {
        // Given
        LocalDate dueDate = LocalDate.of(2024, 12, 31);

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData("task-123", Task.Priority.HIGH, dueDate);

        // Then
        assertSame(dueDate, inputData.getDueDate(), "Should preserve exact date reference");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData("task-123", Task.Priority.MEDIUM, LocalDate.now(), true);

        // Then - Multiple calls should return same values
        assertEquals(inputData.getTaskId(), inputData.getTaskId(), "getTaskId should be consistent");
        assertEquals(inputData.getPriority(), inputData.getPriority(), "getPriority should be consistent");
        assertEquals(inputData.getDueDate(), inputData.getDueDate(), "getDueDate should be consistent");
        assertEquals(inputData.isTestingOverdue(), inputData.isTestingOverdue(), "isTestingOverdue should be consistent");
    }

    @Test
    @DisplayName("Should handle both boolean values for testing overdue")
    void testBooleanValues() {
        LocalDate dueDate = LocalDate.now();
        
        // Test true case
        AddTaskToTodayInputData trueData = new AddTaskToTodayInputData("task-1", Task.Priority.LOW, dueDate, true);
        assertTrue(trueData.isTestingOverdue(), "True case should return true");

        // Test false case
        AddTaskToTodayInputData falseData = new AddTaskToTodayInputData("task-2", Task.Priority.HIGH, dueDate, false);
        assertFalse(falseData.isTestingOverdue(), "False case should return false");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test null task ID with HIGH priority and null due date
        AddTaskToTodayInputData combo1 = new AddTaskToTodayInputData(null, Task.Priority.HIGH, null, true);
        assertNull(combo1.getTaskId());
        assertEquals(Task.Priority.HIGH, combo1.getPriority());
        assertNull(combo1.getDueDate());
        assertTrue(combo1.isTestingOverdue());

        // Test empty task ID with null priority and current date
        AddTaskToTodayInputData combo2 = new AddTaskToTodayInputData("", null, LocalDate.now(), false);
        assertEquals("", combo2.getTaskId());
        assertNull(combo2.getPriority());
        assertNotNull(combo2.getDueDate());
        assertFalse(combo2.isTestingOverdue());
    }

    @Test
    @DisplayName("Should handle mixed case task IDs")
    void testMixedCaseTaskId() {
        // Given
        String taskId = "TaSk-123-MiXeD";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Mixed case should be preserved exactly");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces in task ID")
    void testTaskIdWithSpaces() {
        // Given
        String taskId = "  task-123  ";
        Task.Priority priority = Task.Priority.LOW;
        LocalDate dueDate = LocalDate.now();

        // When
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);

        // Then
        assertEquals(taskId, inputData.getTaskId(), "Leading/trailing spaces should be preserved");
        assertEquals(priority, inputData.getPriority(), "Priority should be preserved");
        assertEquals(dueDate, inputData.getDueDate(), "Due date should be preserved");
    }
}