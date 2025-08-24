package entity.Angela;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import entity.Angela.Task.TaskFactory;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskAvailableFactory;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DailyTaskSummary entity.
 * Tests task summary creation and statistics calculation.
 */
class DailyTaskSummaryTest {

    private DailyTaskSummary dailyTaskSummary;
    private LocalDate testDate;
    private TaskFactory taskFactory;
    private TaskAvailableFactory taskAvailableFactory;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2023, 12, 15);
        dailyTaskSummary = new DailyTaskSummary(testDate);
        taskFactory = new TaskFactory();
        taskAvailableFactory = new TaskAvailableFactory();
    }

    @Test
    void testDailyTaskSummaryCreation() {
        assertNotNull(dailyTaskSummary);
        assertNotNull(dailyTaskSummary.getId());
        assertEquals(testDate, dailyTaskSummary.getDate());
        assertTrue(dailyTaskSummary.getScheduledTasks().isEmpty());
        assertTrue(dailyTaskSummary.getCompletedTasks().isEmpty());
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
        assertTrue(dailyTaskSummary.getCategoryBreakdown().isEmpty());
    }

    @Test
    void testIdGeneration() {
        DailyTaskSummary summary1 = new DailyTaskSummary(testDate);
        DailyTaskSummary summary2 = new DailyTaskSummary(testDate);
        
        assertNotEquals(summary1.getId(), summary2.getId());
        assertTrue(summary1.getId().matches("^[a-fA-F0-9-]{36}$")); // UUID format
    }

    @Test
    void testAddScheduledTask() {
        Info taskInfo = new Info.Builder("Test task").description("A test task").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", taskInfo, dates, false);
        
        dailyTaskSummary.addScheduledTask(task);
        
        assertEquals(1, dailyTaskSummary.getScheduledTasks().size());
        assertTrue(dailyTaskSummary.getScheduledTasks().contains(task));
        assertEquals(0.0, dailyTaskSummary.getCompletionRate()); // Not completed yet
    }

    @Test
    void testAddCompletedTaskUpdatesStats() {
        Info taskInfo = new Info.Builder("Completed task").description("A completed task").category("Work").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", taskInfo, dates, false);
        
        // First add as scheduled
        dailyTaskSummary.addScheduledTask(task);
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
        
        // Then mark as completed
        dailyTaskSummary.markTaskCompleted(task);
        
        assertEquals(1, dailyTaskSummary.getCompletedTasks().size());
        assertTrue(dailyTaskSummary.getCompletedTasks().contains(task));
        assertEquals(1.0, dailyTaskSummary.getCompletionRate()); // 100% completion
        
        // Check category breakdown
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertEquals(1, breakdown.get("Work").intValue());
    }

    @Test
    void testAddMultipleScheduledTasks() {
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task1 = (Task) taskFactory.create("template1", info1, dates, false);
        Task task2 = (Task) taskFactory.create("template2", info2, dates, false);
        Task task3 = (Task) taskFactory.create("template3", info3, dates, false);
        
        dailyTaskSummary.addScheduledTask(task1);
        dailyTaskSummary.addScheduledTask(task2);
        dailyTaskSummary.addScheduledTask(task3);
        
        assertEquals(3, dailyTaskSummary.getScheduledTasks().size());
        assertTrue(dailyTaskSummary.getScheduledTasks().contains(task1));
        assertTrue(dailyTaskSummary.getScheduledTasks().contains(task2));
        assertTrue(dailyTaskSummary.getScheduledTasks().contains(task3));
    }

    @Test
    void testAddNullTasksIgnored() {
        dailyTaskSummary.addScheduledTask(null);
        dailyTaskSummary.markTaskCompleted(null);
        
        assertTrue(dailyTaskSummary.getScheduledTasks().isEmpty());
        assertTrue(dailyTaskSummary.getCompletedTasks().isEmpty());
        assertEquals(0.0, dailyTaskSummary.getCompletionRate()); // Should trigger updateCompletionRate() with empty list
    }
    
    @Test
    void testUpdateCompletionRateEdgeCases() {
        // Test when addScheduledTask() doesn't add task (null case) but with empty list
        DailyTaskSummary emptySummary = new DailyTaskSummary(testDate);
        
        // Try to add null task - this should not call updateCompletionRate since task is null
        emptySummary.addScheduledTask(null);
        assertEquals(0.0, emptySummary.getCompletionRate());
        
        // Test with existing tasks
        Info info = new Info.Builder("Test task").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        // Add task to trigger updateCompletionRate
        dailyTaskSummary.addScheduledTask(task);
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
        
        // Try to add the same task again - should call updateCompletionRate but not add duplicate
        dailyTaskSummary.addScheduledTask(task);
        assertEquals(1, dailyTaskSummary.getScheduledTasks().size());
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
        
        // Try to add null - should not call updateCompletionRate
        dailyTaskSummary.addScheduledTask(null);
        assertEquals(1, dailyTaskSummary.getScheduledTasks().size());
    }

    @Test
    void testCompletionRateCalculation() {
        // Create scheduled tasks
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        Info info4 = new Info.Builder("Task 4").build();
        
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task1 = (Task) taskFactory.create("template1", info1, dates, false);
        Task task2 = (Task) taskFactory.create("template2", info2, dates, false);
        Task task3 = (Task) taskFactory.create("template3", info3, dates, false);
        Task task4 = (Task) taskFactory.create("template4", info4, dates, false);
        
        // Add scheduled tasks
        dailyTaskSummary.addScheduledTask(task1);
        dailyTaskSummary.addScheduledTask(task2);
        dailyTaskSummary.addScheduledTask(task3);
        dailyTaskSummary.addScheduledTask(task4);
        
        // Complete 2 out of 4 tasks
        dailyTaskSummary.markTaskCompleted(task1);
        dailyTaskSummary.markTaskCompleted(task3);
        
        assertEquals(0.5, dailyTaskSummary.getCompletionRate(), 0.001); // 50% completion
    }

    @Test
    void testCompletionRateWithNoScheduledTasks() {
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
        
        // Also test when updateCompletionRate() is called on empty list
        Info info = new Info.Builder("Test task").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        // Try to mark a task completed when no tasks are scheduled
        // This should trigger updateCompletionRate() with empty scheduledTasks
        dailyTaskSummary.markTaskCompleted(task);
        
        // Should remain 0.0 since no tasks are scheduled
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
    }

    @Test
    void testCompletionRateAllTasksCompleted() {
        Info info = new Info.Builder("Task").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        dailyTaskSummary.addScheduledTask(task);
        dailyTaskSummary.markTaskCompleted(task);
        
        assertEquals(1.0, dailyTaskSummary.getCompletionRate()); // 100%
    }

    @Test
    void testCompletionRateNoTasksCompleted() {
        Info info = new Info.Builder("Task").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        dailyTaskSummary.addScheduledTask(task);
        
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
    }

    @Test
    void testCategoryBreakdownAutoCalculation() {
        // Create tasks with different categories
        Info workInfo1 = new Info.Builder("Work task 1").category("Work").build();
        Info workInfo2 = new Info.Builder("Work task 2").category("Work").build();
        Info personalInfo = new Info.Builder("Personal task").category("Personal").build();
        Info healthInfo = new Info.Builder("Health task").category("Health").build();
        
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task workTask1 = (Task) taskFactory.create("template1", workInfo1, dates, false);
        Task workTask2 = (Task) taskFactory.create("template2", workInfo2, dates, false);
        Task personalTask = (Task) taskFactory.create("template3", personalInfo, dates, false);
        Task healthTask = (Task) taskFactory.create("template4", healthInfo, dates, false);
        
        // Add all as scheduled
        dailyTaskSummary.addScheduledTask(workTask1);
        dailyTaskSummary.addScheduledTask(workTask2);
        dailyTaskSummary.addScheduledTask(personalTask);
        dailyTaskSummary.addScheduledTask(healthTask);
        
        // Complete them to trigger category breakdown
        dailyTaskSummary.markTaskCompleted(workTask1);
        dailyTaskSummary.markTaskCompleted(workTask2);
        dailyTaskSummary.markTaskCompleted(personalTask);
        dailyTaskSummary.markTaskCompleted(healthTask);
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertEquals(2, breakdown.get("Work").intValue());
        assertEquals(1, breakdown.get("Personal").intValue());
        assertEquals(1, breakdown.get("Health").intValue());
    }

    @Test
    void testCategoryBreakdownWithNullCategories() {
        Info info1 = new Info.Builder("Task 1").category(null).build();
        Info info2 = new Info.Builder("Task 2").category("").build();
        Info info3 = new Info.Builder("Task 3").category("Work").build();
        
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task1 = (Task) taskFactory.create("template1", info1, dates, false);
        Task task2 = (Task) taskFactory.create("template2", info2, dates, false);
        Task task3 = (Task) taskFactory.create("template3", info3, dates, false);
        
        dailyTaskSummary.addScheduledTask(task1);
        dailyTaskSummary.addScheduledTask(task2);
        dailyTaskSummary.addScheduledTask(task3);
        
        // Only task3 should appear in category breakdown when completed (has non-null category)
        dailyTaskSummary.markTaskCompleted(task1); // null category - won't be counted
        dailyTaskSummary.markTaskCompleted(task2); // empty category - won't be counted  
        dailyTaskSummary.markTaskCompleted(task3); // "Work" category - will be counted
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertEquals(1, breakdown.get("Work").intValue());
        // Null and empty categories are not counted in breakdown
        assertNull(breakdown.get(null));
        assertNull(breakdown.get(""));
    }

    @Test
    void testCategoryBreakdownWithNoCompletedTasks() {
        assertTrue(dailyTaskSummary.getCategoryBreakdown().isEmpty());
    }

    @Test
    void testTaskListEncapsulation() {
        Info info = new Info.Builder("Task").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        dailyTaskSummary.addScheduledTask(task);
        
        List<Task> scheduledTasks = dailyTaskSummary.getScheduledTasks();
        // Should return a defensive copy
        int originalSize = scheduledTasks.size();
        scheduledTasks.clear();
        
        // Original list should be unchanged
        assertEquals(1, dailyTaskSummary.getScheduledTasks().size());
    }

    @Test
    void testCategoryBreakdownEncapsulation() {
        Info info = new Info.Builder("Task").category("Work").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        dailyTaskSummary.addScheduledTask(task);
        dailyTaskSummary.markTaskCompleted(task);
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        // Should return a defensive copy
        breakdown.put("Test", 99);
        
        // Original map should be unchanged
        Map<String, Integer> originalBreakdown = dailyTaskSummary.getCategoryBreakdown();
        assertNull(originalBreakdown.get("Test"));
        assertEquals(1, originalBreakdown.get("Work").intValue());
    }

    @Test
    void testMarkTaskCompletedOnlyOnce() {
        Info info = new Info.Builder("Task").category("Work").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        dailyTaskSummary.addScheduledTask(task);
        
        // Mark completed multiple times
        dailyTaskSummary.markTaskCompleted(task);
        dailyTaskSummary.markTaskCompleted(task);
        dailyTaskSummary.markTaskCompleted(task);
        
        // Should only appear once in completed tasks
        assertEquals(1, dailyTaskSummary.getCompletedTasks().size());
        assertEquals(1, dailyTaskSummary.getCategoryBreakdown().get("Work").intValue());
    }

    @Test
    void testMarkTaskCompletedNotInScheduled() {
        Info info = new Info.Builder("Task").category("Work").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        // Try to mark as completed without adding to scheduled first
        dailyTaskSummary.markTaskCompleted(task);
        
        // Should not be added to completed tasks
        assertEquals(0, dailyTaskSummary.getCompletedTasks().size());
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
        assertTrue(dailyTaskSummary.getCategoryBreakdown().isEmpty());
    }

    @Test
    void testCompleteWorkflow() {
        // Create a realistic daily task summary
        Info workInfo = new Info.Builder("Complete report").category("Work").build();
        Info personalInfo = new Info.Builder("Grocery shopping").category("Personal").build();
        Info healthInfo = new Info.Builder("Morning exercise").category("Health").build();
        
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task workTask = (Task) taskFactory.create("template1", workInfo, dates, false);
        Task personalTask = (Task) taskFactory.create("template2", personalInfo, dates, false);
        Task healthTask = (Task) taskFactory.create("template3", healthInfo, dates, false);
        
        // Add scheduled tasks
        dailyTaskSummary.addScheduledTask(workTask);
        dailyTaskSummary.addScheduledTask(personalTask);
        dailyTaskSummary.addScheduledTask(healthTask);
        
        // Complete 2 out of 3 tasks
        dailyTaskSummary.markTaskCompleted(workTask);
        dailyTaskSummary.markTaskCompleted(healthTask);
        
        // Verify results
        assertEquals(3, dailyTaskSummary.getScheduledTasks().size());
        assertEquals(2, dailyTaskSummary.getCompletedTasks().size());
        assertEquals(2.0/3.0, dailyTaskSummary.getCompletionRate(), 0.001);
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertEquals(1, breakdown.get("Work").intValue());
        assertEquals(1, breakdown.get("Health").intValue());
        assertNull(breakdown.get("Personal")); // Not completed
    }

    @Test
    void testAddDuplicateTaskIgnored() {
        Info info = new Info.Builder("Task").build();
        BeginAndDueDates dates = new BeginAndDueDates(testDate, testDate.plusDays(1));
        Task task = (Task) taskFactory.create("template1", info, dates, false);
        
        dailyTaskSummary.addScheduledTask(task);
        dailyTaskSummary.addScheduledTask(task); // Try to add same task again
        
        // Should only appear once
        assertEquals(1, dailyTaskSummary.getScheduledTasks().size());
    }
    
    @Test
    void testUpdateCompletionRateWithEmptyList() throws Exception {
        // Use reflection to test the private updateCompletionRate method with empty scheduledTasks
        DailyTaskSummary emptySummary = new DailyTaskSummary(testDate);
        
        // Verify initial state - empty list
        assertTrue(emptySummary.getScheduledTasks().isEmpty());
        assertEquals(0.0, emptySummary.getCompletionRate());
        
        // Use reflection to call private updateCompletionRate method
        Method updateCompletionRateMethod = DailyTaskSummary.class.getDeclaredMethod("updateCompletionRate");
        updateCompletionRateMethod.setAccessible(true);
        updateCompletionRateMethod.invoke(emptySummary);
        
        // Should still be 0.0 after calling updateCompletionRate on empty list
        assertEquals(0.0, emptySummary.getCompletionRate());
    }
}