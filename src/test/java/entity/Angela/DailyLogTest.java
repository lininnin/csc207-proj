package entity.Angela;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Angela.Task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for DailyLog entity.
 * Tests daily log creation and management of task, event, and wellness data.
 */
class DailyLogTest {

    @Mock
    private DailyWellnessLog mockWellnessLog;
    
    @Mock
    private DailyEventLog mockEventLog;
    
    @Mock
    private DailyTaskSummary mockTaskSummary;
    
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
        assertNull(dailyLog.getDailyWellnessLog());
        assertNull(dailyLog.getDailyEventLog());
        assertNull(dailyLog.getDailyTaskSummary());
    }

    @Test
    void testDailyLogCreationWithAllComponents() {
        DailyLog dailyLog = new DailyLog(testDate, mockWellnessLog, mockEventLog, mockTaskSummary);
        
        assertNotNull(dailyLog);
        assertNotNull(dailyLog.getId());
        assertEquals(testDate, dailyLog.getDate());
        assertEquals(mockWellnessLog, dailyLog.getDailyWellnessLog());
        assertEquals(mockEventLog, dailyLog.getDailyEventLog());
        assertEquals(mockTaskSummary, dailyLog.getDailyTaskSummary());
        assertTrue(dailyLog.getTimeLog().isEmpty());
    }

    @Test
    void testDailyLogCreationWithNullDate() {
        assertThrows(IllegalArgumentException.class, () -> new DailyLog(null));
    }

    @Test
    void testDailyLogCreationWithNullDateAndComponents() {
        assertThrows(IllegalArgumentException.class, () -> 
                new DailyLog(null, mockWellnessLog, mockEventLog, mockTaskSummary));
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
    void testAddToTimeLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        dailyLog.addToTimeLog(mockTask);
        
        assertEquals(1, dailyLog.getTimeLog().size());
        assertEquals(mockTask, dailyLog.getTimeLog().get(0));
    }

    @Test
    void testAddMultipleItemsToTimeLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        Task task2 = mock(Task.class);
        String event = "Lunch meeting";
        
        dailyLog.addToTimeLog(mockTask);
        dailyLog.addToTimeLog(task2);
        dailyLog.addToTimeLog(event);
        
        assertEquals(3, dailyLog.getTimeLog().size());
        assertEquals(mockTask, dailyLog.getTimeLog().get(0));
        assertEquals(task2, dailyLog.getTimeLog().get(1));
        assertEquals(event, dailyLog.getTimeLog().get(2));
    }

    @Test
    void testAddNullToTimeLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        dailyLog.addToTimeLog(null);
        
        assertTrue(dailyLog.getTimeLog().isEmpty());
    }

    @Test
    void testTimeLogEncapsulation() {
        DailyLog dailyLog = new DailyLog(testDate);
        dailyLog.addToTimeLog(mockTask);
        
        List<Object> timeLog = dailyLog.getTimeLog();
        
        // Should return unmodifiable list
        assertThrows(UnsupportedOperationException.class, () -> 
                timeLog.add("Should not be allowed"));
    }

    @Test
    void testSetDailyWellnessLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        dailyLog.setDailyWellnessLog(mockWellnessLog);
        
        assertEquals(mockWellnessLog, dailyLog.getDailyWellnessLog());
    }

    @Test
    void testSetDailyEventLog() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        dailyLog.setDailyEventLog(mockEventLog);
        
        assertEquals(mockEventLog, dailyLog.getDailyEventLog());
    }

    @Test
    void testSetDailyTaskSummary() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        dailyLog.setDailyTaskSummary(mockTaskSummary);
        
        assertEquals(mockTaskSummary, dailyLog.getDailyTaskSummary());
    }

    @Test
    void testOverwriteComponents() {
        DailyLog dailyLog = new DailyLog(testDate, mockWellnessLog, mockEventLog, mockTaskSummary);
        
        DailyWellnessLog newWellnessLog = mock(DailyWellnessLog.class);
        DailyEventLog newEventLog = mock(DailyEventLog.class);
        DailyTaskSummary newTaskSummary = mock(DailyTaskSummary.class);
        
        dailyLog.setDailyWellnessLog(newWellnessLog);
        dailyLog.setDailyEventLog(newEventLog);
        dailyLog.setDailyTaskSummary(newTaskSummary);
        
        assertEquals(newWellnessLog, dailyLog.getDailyWellnessLog());
        assertEquals(newEventLog, dailyLog.getDailyEventLog());
        assertEquals(newTaskSummary, dailyLog.getDailyTaskSummary());
    }

    @Test
    void testSetNullComponents() {
        DailyLog dailyLog = new DailyLog(testDate, mockWellnessLog, mockEventLog, mockTaskSummary);
        
        dailyLog.setDailyWellnessLog(null);
        dailyLog.setDailyEventLog(null);
        dailyLog.setDailyTaskSummary(null);
        
        assertNull(dailyLog.getDailyWellnessLog());
        assertNull(dailyLog.getDailyEventLog());
        assertNull(dailyLog.getDailyTaskSummary());
    }

    @Test
    void testMixedTimeLogContent() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        String eventName = "Morning standup";
        Integer number = 42;
        LocalDate date = LocalDate.now();
        
        dailyLog.addToTimeLog(mockTask);
        dailyLog.addToTimeLog(eventName);
        dailyLog.addToTimeLog(number);
        dailyLog.addToTimeLog(date);
        
        List<Object> timeLog = dailyLog.getTimeLog();
        assertEquals(4, timeLog.size());
        assertEquals(mockTask, timeLog.get(0));
        assertEquals(eventName, timeLog.get(1));
        assertEquals(number, timeLog.get(2));
        assertEquals(date, timeLog.get(3));
    }

    @Test
    void testTimeLogOrder() {
        DailyLog dailyLog = new DailyLog(testDate);
        
        // Add items in specific order
        String first = "First item";
        String second = "Second item";
        String third = "Third item";
        
        dailyLog.addToTimeLog(first);
        dailyLog.addToTimeLog(second);
        dailyLog.addToTimeLog(third);
        
        List<Object> timeLog = dailyLog.getTimeLog();
        assertEquals(first, timeLog.get(0));
        assertEquals(second, timeLog.get(1));
        assertEquals(third, timeLog.get(2));
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
    void testCompleteWorkflow() {
        // Test a complete daily log workflow
        DailyLog dailyLog = new DailyLog(testDate);
        
        // Add wellness log
        dailyLog.setDailyWellnessLog(mockWellnessLog);
        
        // Add event log
        dailyLog.setDailyEventLog(mockEventLog);
        
        // Add task summary
        dailyLog.setDailyTaskSummary(mockTaskSummary);
        
        // Add some timeline entries
        dailyLog.addToTimeLog("9:00 AM - Started work");
        dailyLog.addToTimeLog(mockTask);
        dailyLog.addToTimeLog("12:00 PM - Lunch break");
        
        // Verify everything is set correctly
        assertEquals(testDate, dailyLog.getDate());
        assertEquals(mockWellnessLog, dailyLog.getDailyWellnessLog());
        assertEquals(mockEventLog, dailyLog.getDailyEventLog());
        assertEquals(mockTaskSummary, dailyLog.getDailyTaskSummary());
        assertEquals(3, dailyLog.getTimeLog().size());
        assertEquals("9:00 AM - Started work", dailyLog.getTimeLog().get(0));
        assertEquals(mockTask, dailyLog.getTimeLog().get(1));
        assertEquals("12:00 PM - Lunch break", dailyLog.getTimeLog().get(2));
    }
}