package entity.Angela;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskFactory;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2023, 12, 15);
        dailyTaskSummary = new DailyTaskSummary(testDate);
        taskFactory = new TaskFactory();
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
        Task task = taskFactory.create("task1", taskInfo, Task.Priority.MEDIUM, testDate, false, "template1");
        
        dailyTaskSummary.addScheduledTask(task);
        
        assertEquals(1, dailyTaskSummary.getScheduledTasks().size());
        assertTrue(dailyTaskSummary.getScheduledTasks().contains(task));
    }

    @Test
    void testAddCompletedTask() {
        Info taskInfo = new Info.Builder("Completed task").description("A completed task").build();
        Task task = taskFactory.create("task1", taskInfo, Task.Priority.HIGH, testDate, false, "template1");
        
        dailyTaskSummary.addCompletedTask(task);
        
        assertEquals(1, dailyTaskSummary.getCompletedTasks().size());
        assertTrue(dailyTaskSummary.getCompletedTasks().contains(task));
    }

    @Test
    void testAddMultipleScheduledTasks() {
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        
        Task task1 = taskFactory.create("task1", info1, Task.Priority.LOW, testDate, false, "template1");
        Task task2 = taskFactory.create("task2", info2, Task.Priority.MEDIUM, testDate, false, "template2");
        Task task3 = taskFactory.create("task3", info3, Task.Priority.HIGH, testDate, false, "template3");
        
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
        dailyTaskSummary.addCompletedTask(null);
        
        assertTrue(dailyTaskSummary.getScheduledTasks().isEmpty());
        assertTrue(dailyTaskSummary.getCompletedTasks().isEmpty());
    }

    @Test
    void testCompletionRateCalculation() {
        // Create scheduled tasks
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        Info info4 = new Info.Builder("Task 4").build();
        
        Task task1 = taskFactory.create("task1", info1, Task.Priority.MEDIUM, testDate, false, "template1");
        Task task2 = taskFactory.create("task2", info2, Task.Priority.MEDIUM, testDate, false, "template2");
        Task task3 = taskFactory.create("task3", info3, Task.Priority.MEDIUM, testDate, false, "template3");
        Task task4 = taskFactory.create("task4", info4, Task.Priority.MEDIUM, testDate, false, "template4");
        
        // Add scheduled tasks
        dailyTaskSummary.addScheduledTask(task1);
        dailyTaskSummary.addScheduledTask(task2);
        dailyTaskSummary.addScheduledTask(task3);
        dailyTaskSummary.addScheduledTask(task4);
        
        // Add completed tasks (2 out of 4)
        dailyTaskSummary.addCompletedTask(task1);
        dailyTaskSummary.addCompletedTask(task3);
        
        // Calculate completion rate
        dailyTaskSummary.calculateCompletionRate();
        
        assertEquals(50.0, dailyTaskSummary.getCompletionRate());
    }

    @Test
    void testCompletionRateWithNoScheduledTasks() {
        dailyTaskSummary.calculateCompletionRate();
        
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
    }

    @Test
    void testCompletionRateAllTasksCompleted() {
        Info info = new Info.Builder("Task").build();
        Task task = taskFactory.create("task1", info, Task.Priority.MEDIUM, testDate, false, "template1");
        
        dailyTaskSummary.addScheduledTask(task);
        dailyTaskSummary.addCompletedTask(task);
        dailyTaskSummary.calculateCompletionRate();
        
        assertEquals(100.0, dailyTaskSummary.getCompletionRate());
    }

    @Test
    void testCompletionRateNoTasksCompleted() {
        Info info = new Info.Builder("Task").build();
        Task task = taskFactory.create("task1", info, Task.Priority.MEDIUM, testDate, false, "template1");
        
        dailyTaskSummary.addScheduledTask(task);
        dailyTaskSummary.calculateCompletionRate();
        
        assertEquals(0.0, dailyTaskSummary.getCompletionRate());
    }

    @Test
    void testCompletionRateRoundingDown() {
        // Create 3 scheduled tasks, complete 1 (33.33...)
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        
        Task task1 = taskFactory.create("task1", info1, Task.Priority.MEDIUM, testDate, false, "template1");
        Task task2 = taskFactory.create("task2", info2, Task.Priority.MEDIUM, testDate, false, "template2");
        Task task3 = taskFactory.create("task3", info3, Task.Priority.MEDIUM, testDate, false, "template3");
        
        dailyTaskSummary.addScheduledTask(task1);
        dailyTaskSummary.addScheduledTask(task2);
        dailyTaskSummary.addScheduledTask(task3);
        
        dailyTaskSummary.addCompletedTask(task1);
        dailyTaskSummary.calculateCompletionRate();
        
        assertEquals(33.33, dailyTaskSummary.getCompletionRate(), 0.01);
    }

    @Test
    void testCategoryBreakdownCalculation() {
        // Create tasks with different categories
        Info workInfo1 = new Info.Builder("Work task 1").category("Work").build();
        Info workInfo2 = new Info.Builder("Work task 2").category("Work").build();
        Info personalInfo = new Info.Builder("Personal task").category("Personal").build();
        Info healthInfo = new Info.Builder("Health task").category("Health").build();
        
        Task workTask1 = taskFactory.create("task1", workInfo1, Task.Priority.MEDIUM, testDate, false, "template1");
        Task workTask2 = taskFactory.create("task2", workInfo2, Task.Priority.MEDIUM, testDate, false, "template2");
        Task personalTask = taskFactory.create("task3", personalInfo, Task.Priority.MEDIUM, testDate, false, "template3");
        Task healthTask = taskFactory.create("task4", healthInfo, Task.Priority.MEDIUM, testDate, false, "template4");
        
        dailyTaskSummary.addScheduledTask(workTask1);
        dailyTaskSummary.addScheduledTask(workTask2);
        dailyTaskSummary.addScheduledTask(personalTask);
        dailyTaskSummary.addScheduledTask(healthTask);
        
        dailyTaskSummary.calculateCategoryBreakdown();
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertEquals(2, breakdown.get("Work").intValue());
        assertEquals(1, breakdown.get("Personal").intValue());
        assertEquals(1, breakdown.get("Health").intValue());
    }

    @Test
    void testCategoryBreakdownWithEmptyCategories() {
        Info info1 = new Info.Builder("Task 1").category("").build();
        Info info2 = new Info.Builder("Task 2").category(null).build();
        Info info3 = new Info.Builder("Task 3").category("Work").build();
        
        Task task1 = taskFactory.create("task1", info1, Task.Priority.MEDIUM, testDate, false, "template1");
        Task task2 = taskFactory.create("task2", info2, Task.Priority.MEDIUM, testDate, false, "template2");
        Task task3 = taskFactory.create("task3", info3, Task.Priority.MEDIUM, testDate, false, "template3");
        
        dailyTaskSummary.addScheduledTask(task1);
        dailyTaskSummary.addScheduledTask(task2);
        dailyTaskSummary.addScheduledTask(task3);
        
        dailyTaskSummary.calculateCategoryBreakdown();
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertEquals(2, breakdown.get("Uncategorized").intValue());
        assertEquals(1, breakdown.get("Work").intValue());
    }

    @Test
    void testCategoryBreakdownWithNoTasks() {
        dailyTaskSummary.calculateCategoryBreakdown();
        
        assertTrue(dailyTaskSummary.getCategoryBreakdown().isEmpty());
    }

    @Test
    void testSetScheduledTasks() {
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        
        Task task1 = taskFactory.create("task1", info1, Task.Priority.MEDIUM, testDate, false, "template1");
        Task task2 = taskFactory.create("task2", info2, Task.Priority.MEDIUM, testDate, false, "template2");
        
        List<Task> tasks = Arrays.asList(task1, task2);
        dailyTaskSummary.setScheduledTasks(tasks);
        
        assertEquals(2, dailyTaskSummary.getScheduledTasks().size());
        assertTrue(dailyTaskSummary.getScheduledTasks().contains(task1));
        assertTrue(dailyTaskSummary.getScheduledTasks().contains(task2));
    }

    @Test
    void testSetCompletedTasks() {
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        
        Task task1 = taskFactory.create("task1", info1, Task.Priority.MEDIUM, testDate, false, "template1");
        Task task2 = taskFactory.create("task2", info2, Task.Priority.MEDIUM, testDate, false, "template2");
        
        List<Task> tasks = Arrays.asList(task1, task2);
        dailyTaskSummary.setCompletedTasks(tasks);
        
        assertEquals(2, dailyTaskSummary.getCompletedTasks().size());
        assertTrue(dailyTaskSummary.getCompletedTasks().contains(task1));
        assertTrue(dailyTaskSummary.getCompletedTasks().contains(task2));
    }

    @Test
    void testSetNullTaskLists() {
        dailyTaskSummary.setScheduledTasks(null);
        dailyTaskSummary.setCompletedTasks(null);
        
        assertTrue(dailyTaskSummary.getScheduledTasks().isEmpty());
        assertTrue(dailyTaskSummary.getCompletedTasks().isEmpty());
    }

    @Test
    void testTaskListEncapsulation() {
        Info info = new Info.Builder("Task").build();
        Task task = taskFactory.create("task1", info, Task.Priority.MEDIUM, testDate, false, "template1");
        
        dailyTaskSummary.addScheduledTask(task);
        
        List<Task> scheduledTasks = dailyTaskSummary.getScheduledTasks();
        assertThrows(UnsupportedOperationException.class, () -> 
                scheduledTasks.clear());
    }

    @Test
    void testCategoryBreakdownEncapsulation() {
        Info info = new Info.Builder("Task").category("Work").build();
        Task task = taskFactory.create("task1", info, Task.Priority.MEDIUM, testDate, false, "template1");
        
        dailyTaskSummary.addScheduledTask(task);
        dailyTaskSummary.calculateCategoryBreakdown();
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertThrows(UnsupportedOperationException.class, () -> 
                breakdown.put("Test", 1));
    }

    @Test
    void testCompleteWorkflow() {
        // Create a realistic daily task summary
        Info workInfo = new Info.Builder("Complete report").category("Work").build();
        Info personalInfo = new Info.Builder("Grocery shopping").category("Personal").build();
        Info healthInfo = new Info.Builder("Morning exercise").category("Health").build();
        
        Task workTask = taskFactory.create("task1", workInfo, Task.Priority.HIGH, testDate, false, "template1");
        Task personalTask = taskFactory.create("task2", personalInfo, Task.Priority.MEDIUM, testDate, false, "template2");
        Task healthTask = taskFactory.create("task3", healthInfo, Task.Priority.LOW, testDate, false, "template3");
        
        // Add scheduled tasks
        dailyTaskSummary.addScheduledTask(workTask);
        dailyTaskSummary.addScheduledTask(personalTask);
        dailyTaskSummary.addScheduledTask(healthTask);
        
        // Complete 2 out of 3 tasks
        dailyTaskSummary.addCompletedTask(workTask);
        dailyTaskSummary.addCompletedTask(healthTask);
        
        // Calculate statistics
        dailyTaskSummary.calculateCompletionRate();
        dailyTaskSummary.calculateCategoryBreakdown();
        
        // Verify results
        assertEquals(3, dailyTaskSummary.getScheduledTasks().size());
        assertEquals(2, dailyTaskSummary.getCompletedTasks().size());
        assertEquals(66.67, dailyTaskSummary.getCompletionRate(), 0.01);
        
        Map<String, Integer> breakdown = dailyTaskSummary.getCategoryBreakdown();
        assertEquals(1, breakdown.get("Work").intValue());
        assertEquals(1, breakdown.get("Personal").intValue());
        assertEquals(1, breakdown.get("Health").intValue());
    }
}