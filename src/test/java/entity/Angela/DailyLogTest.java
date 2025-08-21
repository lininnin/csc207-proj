package entity.Angela;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Angela.Task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for DailyLog entity.
 * Tests daily log creation and management of task, event, and wellness data.
 */
class DailyLogTest {

    @Mock
    private Task mockTask;

    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testDate = LocalDate.of(2023, 12, 15);
    }

    @Test
    void testDailyLogCreationWithDate() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        assertNotNull(dailyLog);
        assertNotNull(dailyLog.getId());
        assertEquals(testDate, dailyLog.getDate());
        assertNotNull(dailyLog.getTimeLog());
        assertTrue(dailyLog.getTimeLog().isEmpty());
        assertNotNull(dailyLog.getDailyWellnessLog());
        assertNotNull(dailyLog.getDailyEventLog());
        assertNotNull(dailyLog.getDailyTaskSummary());
    }

    @Test
    void testDailyLogCreationWithNullDate() {
        assertThrows(IllegalArgumentException.class, () -> new DailyLog(null));
    }

    @Test
    void testIdGeneration() {
        DailyLog log1 = new DailyLog(testDate);
        DailyLog log2 = new DailyLog(testDate);
        
        assertNotEquals(log1.getId(), log2.getId());
        assertTrue(log1.getId().matches("^[a-fA-F0-9-]{36}$")); // UUID format
        assertTrue(log2.getId().matches("^[a-fA-F0-9-]{36}$"));
    }

    @Test
    void testAddTaskToTimeLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        dailyLog.addTask(mockTask);
        
        assertEquals(1, dailyLog.getTimeLog().size());
        assertEquals(mockTask, dailyLog.getTimeLog().get(0));
    }

    @Test
    void testAddMultipleTasksToTimeLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        Task task2 = mock(Task.class);
        
        dailyLog.addTask(mockTask);
        dailyLog.addTask(task2);
        
        assertEquals(2, dailyLog.getTimeLog().size());
        assertEquals(mockTask, dailyLog.getTimeLog().get(0));
        assertEquals(task2, dailyLog.getTimeLog().get(1));
    }

    @Test
    void testAddNullTaskToTimeLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        assertThrows(IllegalArgumentException.class, () -> dailyLog.addTask(null));
    }

    @Test
    void testTimeLogEncapsulation() {
        DailyLog dailyLog = new DailyLog(testDate);
        dailyLog.addTask(mockTask);
        
        List<Object> timeLog = dailyLog.getTimeLog();
        
        // Should return defensive copy - modifying returned list shouldn't affect original
        timeLog.clear();
        assertEquals(1, dailyLog.getTimeLog().size()); // Original should be unchanged
    }

    @Test
    void testMarkTaskCompleted() {
        DailyLog dailyLog = new DailyLog(testDate);
        LocalDateTime completionTime = LocalDateTime.of(2023, 12, 15, 10, 30);
        
        dailyLog.markTaskCompleted(mockTask, completionTime);
        
        // Verify task's completion status was updated
        verify(mockTask).editStatus(true);
        // Verify task summary was updated
        assertNotNull(dailyLog.getDailyTaskSummary());
    }

    @Test
    void testMarkTaskCompletedWithNullTask() {
        DailyLog dailyLog = new DailyLog(testDate);
        LocalDateTime completionTime = LocalDateTime.of(2023, 12, 15, 10, 30);
        
        assertThrows(IllegalArgumentException.class, () -> 
            dailyLog.markTaskCompleted(null, completionTime));
    }

    @Test
    void testMarkTaskCompletedWithNullTime() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        assertThrows(IllegalArgumentException.class, () -> 
            dailyLog.markTaskCompleted(mockTask, null));
    }

    @Test
    void testTimeLogOrder() {
        DailyLog dailyLog = new DailyLog(testDate);
        Task task2 = mock(Task.class);
        Task task3 = mock(Task.class);
        
        dailyLog.addTask(mockTask);
        dailyLog.addTask(task2);
        dailyLog.addTask(task3);
        
        List<Object> timeLog = dailyLog.getTimeLog();
        assertEquals(mockTask, timeLog.get(0));
        assertEquals(task2, timeLog.get(1));
        assertEquals(task3, timeLog.get(2));
    }

    @Test
    void testDateImmutability() {
        DailyLog dailyLog = new DailyLog(testDate);
        LocalDate retrievedDate = dailyLog.getDate();
        
        // LocalDate is immutable, but test that we get the same date
        assertEquals(testDate, retrievedDate);
        assertEquals(2023, retrievedDate.getYear());
        assertEquals(12, retrievedDate.getMonthValue());
        assertEquals(15, retrievedDate.getDayOfMonth());
    }

    @Test
    void testComponentsNotNull() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        assertNotNull(dailyLog.getDailyWellnessLog());
        assertNotNull(dailyLog.getDailyEventLog());
        assertNotNull(dailyLog.getDailyTaskSummary());
        
        // Verify they are properly initialized for the same date
        assertEquals(testDate, dailyLog.getDailyWellnessLog().getDate());
        assertEquals(testDate, dailyLog.getDailyEventLog().getDate());
        assertEquals(testDate, dailyLog.getDailyTaskSummary().getDate());
    }

    @Test
    void testCompleteWorkflow() {
        // Test a complete daily log workflow
        DailyLog dailyLog = new DailyLog(testDate);
        LocalDateTime completionTime = LocalDateTime.of(2023, 12, 15, 14, 30);
        
        // Add task to timeline
        dailyLog.addTask(mockTask);
        
        // Mark task as completed
        dailyLog.markTaskCompleted(mockTask, completionTime);
        
        // Verify everything is set correctly
        assertEquals(testDate, dailyLog.getDate());
        assertNotNull(dailyLog.getDailyWellnessLog());
        assertNotNull(dailyLog.getDailyEventLog());
        assertNotNull(dailyLog.getDailyTaskSummary());
        assertEquals(1, dailyLog.getTimeLog().size());
        assertEquals(mockTask, dailyLog.getTimeLog().get(0));
        
        // Verify task was marked complete
        verify(mockTask).editStatus(true);
    }

    @Test
    void testMultipleTaskWorkflow() {
        DailyLog dailyLog = new DailyLog(testDate);
        Task task2 = mock(Task.class);
        Task task3 = mock(Task.class);
        LocalDateTime completionTime1 = LocalDateTime.of(2023, 12, 15, 9, 0);
        LocalDateTime completionTime2 = LocalDateTime.of(2023, 12, 15, 15, 30);
        
        // Add multiple tasks
        dailyLog.addTask(mockTask);
        dailyLog.addTask(task2);
        dailyLog.addTask(task3);
        
        // Mark some as completed
        dailyLog.markTaskCompleted(mockTask, completionTime1);
        dailyLog.markTaskCompleted(task3, completionTime2);
        
        // Verify state
        assertEquals(3, dailyLog.getTimeLog().size());
        verify(mockTask).editStatus(true);
        verify(task3).editStatus(true);
        verify(task2, never()).editStatus(anyBoolean()); // task2 was not completed
    }

    @Test
    void testTaskIntegrationWithSummary() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        // Add task - should also be added to summary
        dailyLog.addTask(mockTask);
        
        // Get the task summary and verify integration
        DailyTaskSummary summary = dailyLog.getDailyTaskSummary();
        assertNotNull(summary);
        assertEquals(testDate, summary.getDate());
        
        // The integration details depend on the actual implementation
        // This test verifies the summary is properly initialized
    }

    @Test
    void testIdUniqueness() {
        DailyLog log1 = new DailyLog(testDate);
        DailyLog log2 = new DailyLog(testDate.plusDays(1));
        DailyLog log3 = new DailyLog(testDate);
        
        // All IDs should be unique even with same/different dates
        assertNotEquals(log1.getId(), log2.getId());
        assertNotEquals(log1.getId(), log3.getId());
        assertNotEquals(log2.getId(), log3.getId());
    }
}