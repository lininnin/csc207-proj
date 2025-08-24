package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.info.Info;
import entity.info.InfoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AddTaskToTodayOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class AddTaskToTodayOutputDataTest {

    private Task createTestTask(String taskName) {
        InfoFactory infoFactory = new InfoFactory();
        Info info = (Info) infoFactory.create(taskName, "Test description", "Test Category");
        
        BeginAndDueDatesFactory datesFactory = new BeginAndDueDatesFactory();
        BeginAndDueDates dates = (BeginAndDueDates) datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1));
        
        TaskFactory taskFactory = new TaskFactory();
        return (Task) taskFactory.create("template-123", info, dates, false);
    }

    @Test
    @DisplayName("Should create output data with valid task and task name")
    void testValidCreation() {
        // Given
        Task task = createTestTask("Complete report");
        String taskName = "Complete report";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task reference should match");
        assertEquals(taskName, outputData.getTaskName(), "Task name should match");
    }

    @Test
    @DisplayName("Should accept null task")
    void testNullTask() {
        // Given
        Task task = null;
        String taskName = "Complete report";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertNull(outputData.getTask(), "Task should be null");
        assertEquals(taskName, outputData.getTaskName(), "Task name should be preserved");
    }

    @Test
    @DisplayName("Should accept null task name")
    void testNullTaskName() {
        // Given
        Task task = createTestTask("Complete report");
        String taskName = null;

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertNull(outputData.getTaskName(), "Task name should be null");
    }

    @Test
    @DisplayName("Should accept both null parameters")
    void testBothNull() {
        // Given
        Task task = null;
        String taskName = null;

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertNull(outputData.getTask(), "Task should be null");
        assertNull(outputData.getTaskName(), "Task name should be null");
    }

    @Test
    @DisplayName("Should handle empty task name")
    void testEmptyTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals("", outputData.getTaskName(), "Empty task name should be preserved");
    }

    @Test
    @DisplayName("Should handle whitespace-only task name")
    void testWhitespaceOnlyTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "   ";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals("   ", outputData.getTaskName(), "Whitespace task name should be preserved");
    }

    @Test
    @DisplayName("Should handle special characters in task name")
    void testSpecialCharactersTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "Complete & Submit Report!@#$%";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Special characters should be preserved");
    }

    @Test
    @DisplayName("Should handle unicode characters in task name")
    void testUnicodeTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "完成报告 😀 任务";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Unicode characters should be preserved");
    }

    @Test
    @DisplayName("Should handle long task names")
    void testLongTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "Very long task name that exceeds normal expectations " + "A".repeat(100);

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Long task name should be preserved");
    }

    @Test
    @DisplayName("Should handle numeric task names")
    void testNumericTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "123456789";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Numeric task name should be preserved");
    }

    @Test
    @DisplayName("Should handle single character task names")
    void testSingleCharacterTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "A";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals("A", outputData.getTaskName(), "Single character task name should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        Task originalTask = createTestTask("Complete report");
        String originalTaskName = "Complete report";
        
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(originalTask, originalTaskName);

        // Then
        assertSame(originalTask, outputData.getTask(), "Task reference should remain unchanged");
        assertEquals(originalTaskName, outputData.getTaskName(), "Task name should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        Task task1 = createTestTask("Task 1");
        Task task2 = createTestTask("Task 2");
        AddTaskToTodayOutputData outputData1 = new AddTaskToTodayOutputData(task1, "Task 1");
        AddTaskToTodayOutputData outputData2 = new AddTaskToTodayOutputData(task2, "Task 2");

        // Then
        assertNotSame(outputData1.getTask(), outputData2.getTask(), "Different instances should have different tasks");
        assertNotEquals(outputData1.getTaskName(), outputData2.getTaskName(), "Different instances should have different task names");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should preserve exact task reference")
    void testTaskReference() {
        // Given
        Task task = createTestTask("Complete report");

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, "Complete report");

        // Then
        assertSame(task, outputData.getTask(), "Should preserve exact task reference");
    }

    @Test
    @DisplayName("Should preserve exact string reference")
    void testStringReference() {
        // Given
        Task task = createTestTask("Complete report");
        String taskName = "Complete report";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(taskName, outputData.getTaskName(), "Should preserve exact string reference");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        Task task = createTestTask("Complete report");
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, "Complete report");

        // Then - Multiple calls should return same values
        assertSame(outputData.getTask(), outputData.getTask(), "getTask should be consistent");
        assertEquals(outputData.getTaskName(), outputData.getTaskName(), "getTaskName should be consistent");
    }

    @Test
    @DisplayName("Should handle mixed case task names")
    void testMixedCaseTaskName() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "CoMpLeTe RePoRt";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertEquals(taskName, outputData.getTaskName(), "Mixed case should be preserved exactly");
    }

    @Test
    @DisplayName("Should handle task names with newlines and tabs")
    void testTaskNameWithNewlinesAndTabs() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "Complete\\nReport\\twith\\tTabs";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertEquals(taskName, outputData.getTaskName(), "Newlines and tabs should be preserved");
    }

    @Test
    @DisplayName("Should handle leading and trailing spaces in task name")
    void testTaskNameWithSpaces() {
        // Given
        Task task = createTestTask("Test task");
        String taskName = "  Complete Report  ";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertEquals(taskName, outputData.getTaskName(), "Leading/trailing spaces should be preserved");
    }

    @Test
    @DisplayName("Should handle extreme combinations")
    void testExtremeCombinations() {
        // Test null task with empty name
        AddTaskToTodayOutputData combo1 = new AddTaskToTodayOutputData(null, "");
        assertNull(combo1.getTask());
        assertEquals("", combo1.getTaskName());

        // Test null task with whitespace name
        AddTaskToTodayOutputData combo2 = new AddTaskToTodayOutputData(null, "   ");
        assertNull(combo2.getTask());
        assertEquals("   ", combo2.getTaskName());
    }

    @Test
    @DisplayName("Should handle task name that differs from task's actual name")
    void testDifferentTaskNames() {
        // Given - Task created with one name, output data with different name
        Task task = createTestTask("Original Task Name");
        String taskName = "Different Task Name";

        // When
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);

        // Then
        assertSame(task, outputData.getTask(), "Task should be preserved");
        assertEquals(taskName, outputData.getTaskName(), "Task name should match provided name, not task's name");
        assertNotEquals(task.getInfo().getName(), outputData.getTaskName(), "Output task name should be independent of task's internal name");
    }

    @Test
    @DisplayName("Should handle tasks with different priorities")
    void testDifferentPriorities() {
        // Test with HIGH priority task
        Task highTask = createTestTask("High priority task");
        AddTaskToTodayOutputData highData = new AddTaskToTodayOutputData(highTask, "High Task");
        assertSame(highTask, highData.getTask(), "Should preserve high priority task");

        // Test with different task reference 
        Task mediumTask = createTestTask("Medium priority task");
        AddTaskToTodayOutputData mediumData = new AddTaskToTodayOutputData(mediumTask, "Medium Task");
        assertSame(mediumTask, mediumData.getTask(), "Should preserve medium priority task");
        
        // Tasks should be different instances
        assertNotSame(highData.getTask(), mediumData.getTask(), "Different tasks should have different references");
    }
}