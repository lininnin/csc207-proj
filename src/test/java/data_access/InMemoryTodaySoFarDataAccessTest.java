package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import entity.Alex.Event.EventInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Sophia.Goal;
import entity.Sophia.GoalInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import test_utils.TestDataResetUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test for InMemoryTodaySoFarDataAccess.
 * Tests data aggregation from multiple sources and today so far functionality.
 */
class InMemoryTodaySoFarDataAccessTest {

    @Mock
    private InMemoryTaskGateway mockTaskGateway;
    
    @Mock
    private TodaysEventDataAccessObject mockEventDataAccess;
    
    @Mock
    private TodaysWellnessLogDataAccessObject mockWellnessDataAccess;
    
    @Mock
    private GoalRepository mockGoalRepository;
    
    @Mock
    private Task mockTask1;
    
    @Mock
    private Task mockTask2;
    
    @Mock
    private Task mockTask3;
    
    @Mock
    private EventInterf mockEvent1;
    
    @Mock
    private EventInterf mockEvent2;
    
    @Mock
    private WellnessLogEntryInterf mockWellnessEntry1;
    
    @Mock
    private WellnessLogEntryInterf mockWellnessEntry2;
    
    @Mock
    private Goal mockGoal1;
    
    @Mock
    private Goal mockGoal2;

    private InMemoryTodaySoFarDataAccess dataAccess;

    @BeforeEach
    void setUp() {
        // Reset all shared singleton data for test isolation
        TestDataResetUtil.resetAllSharedData();
        
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConstructorWithAllDependencies() {
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        assertNotNull(dataAccess);
    }

    @Test
    void testConstructorWithOnlyTaskGateway() {
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        assertNotNull(dataAccess);
    }

    @Test
    void testGetCompletedTasksForTodayWithData() {
        // Setup mock tasks
        when(mockTask1.isCompleted()).thenReturn(true);
        when(mockTask2.isCompleted()).thenReturn(false);
        when(mockTask3.isCompleted()).thenReturn(true);
        
        List<Task> allTasks = Arrays.asList(mockTask1, mockTask2, mockTask3);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<TaskInterf> completed = dataAccess.getCompletedTasksForToday();
        
        assertEquals(2, completed.size());
        assertTrue(completed.contains(mockTask1));
        assertTrue(completed.contains(mockTask3));
        assertFalse(completed.contains(mockTask2));
    }

    @Test
    void testGetCompletedTasksForTodayWithNullTaskGateway() {
        dataAccess = new InMemoryTodaySoFarDataAccess(
                null, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<TaskInterf> completed = dataAccess.getCompletedTasksForToday();
        
        assertTrue(completed.isEmpty());
    }

    @Test
    void testGetCompletedTasksForTodayWithEmptyList() {
        when(mockTaskGateway.getTodaysTasks()).thenReturn(new ArrayList<>());
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        List<TaskInterf> completed = dataAccess.getCompletedTasksForToday();
        
        assertTrue(completed.isEmpty());
    }

    @Test
    void testGetCompletedEventsForTodayWithData() {
        List<EventInterf> events = Arrays.asList(mockEvent1, mockEvent2);
        when(mockEventDataAccess.getTodaysEvents()).thenReturn(events);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<EventInterf> completedEvents = dataAccess.getCompletedEventsForToday();
        
        assertEquals(2, completedEvents.size());
        assertTrue(completedEvents.contains(mockEvent1));
        assertTrue(completedEvents.contains(mockEvent2));
    }

    @Test
    void testGetCompletedEventsForTodayWithNullEventDataAccess() {
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, null, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<EventInterf> events = dataAccess.getCompletedEventsForToday();
        
        assertTrue(events.isEmpty());
    }

    @Test
    void testGetCompletedEventsForTodayWithException() {
        when(mockEventDataAccess.getTodaysEvents()).thenThrow(new RuntimeException("Test exception"));
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<EventInterf> events = dataAccess.getCompletedEventsForToday();
        
        assertTrue(events.isEmpty());
    }

    @Test
    void testGetWellnessEntriesForTodayWithData() {
        List<WellnessLogEntryInterf> entries = Arrays.asList(mockWellnessEntry1, mockWellnessEntry2);
        when(mockWellnessDataAccess.getTodaysWellnessLogEntries()).thenReturn(entries);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<WellnessLogEntryInterf> wellnessEntries = dataAccess.getWellnessEntriesForToday();
        
        assertEquals(2, wellnessEntries.size());
        assertTrue(wellnessEntries.contains(mockWellnessEntry1));
        assertTrue(wellnessEntries.contains(mockWellnessEntry2));
    }

    @Test
    void testGetWellnessEntriesForTodayWithNullWellnessDataAccess() {
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, null, mockGoalRepository
        );
        
        List<WellnessLogEntryInterf> entries = dataAccess.getWellnessEntriesForToday();
        
        assertTrue(entries.isEmpty());
    }

    @Test
    void testGetWellnessEntriesForTodayWithException() {
        when(mockWellnessDataAccess.getTodaysWellnessLogEntries()).thenThrow(new RuntimeException("Test exception"));
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<WellnessLogEntryInterf> entries = dataAccess.getWellnessEntriesForToday();
        
        assertTrue(entries.isEmpty());
    }

    @Test
    void testGetActiveGoalsWithData() {
        List<Goal> goals = Arrays.asList(mockGoal1, mockGoal2);
        when(mockGoalRepository.getTodayGoals()).thenReturn(goals);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<GoalInterface> activeGoals = dataAccess.getActiveGoals();
        
        assertEquals(2, activeGoals.size());
        assertTrue(activeGoals.contains(mockGoal1));
        assertTrue(activeGoals.contains(mockGoal2));
    }

    @Test
    void testGetActiveGoalsWithNullGoalRepository() {
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, null
        );
        
        List<GoalInterface> goals = dataAccess.getActiveGoals();
        
        assertTrue(goals.isEmpty());
    }

    @Test
    void testGetActiveGoalsWithException() {
        when(mockGoalRepository.getTodayGoals()).thenThrow(new RuntimeException("Test exception"));
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        List<GoalInterface> goals = dataAccess.getActiveGoals();
        
        assertTrue(goals.isEmpty());
    }

    @Test
    void testGetTotalTasksForTodayExcludingOverdue() {
        // Setup tasks: 2 regular, 1 overdue, 1 completed regular
        when(mockTask1.isOverdue()).thenReturn(false);
        when(mockTask2.isOverdue()).thenReturn(true);  // Overdue - should not count
        when(mockTask3.isOverdue()).thenReturn(false);
        
        List<Task> allTasks = Arrays.asList(mockTask1, mockTask2, mockTask3);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        int total = dataAccess.getTotalTasksForToday();
        
        assertEquals(2, total); // Only non-overdue tasks
    }

    @Test
    void testGetTotalTasksForTodayWithNullTaskGateway() {
        dataAccess = new InMemoryTodaySoFarDataAccess(null);
        
        int total = dataAccess.getTotalTasksForToday();
        
        assertEquals(0, total);
    }

    @Test
    void testGetCompletedTasksCountForTodayExcludingOverdue() {
        // Setup tasks: completed regular, completed overdue, incomplete regular
        when(mockTask1.isCompleted()).thenReturn(true);
        when(mockTask1.isOverdue()).thenReturn(false);
        
        when(mockTask2.isCompleted()).thenReturn(true);
        when(mockTask2.isOverdue()).thenReturn(true);  // Overdue completed - should not count
        
        when(mockTask3.isCompleted()).thenReturn(false);
        when(mockTask3.isOverdue()).thenReturn(false);
        
        List<Task> allTasks = Arrays.asList(mockTask1, mockTask2, mockTask3);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        int completed = dataAccess.getCompletedTasksCountForToday();
        
        assertEquals(1, completed); // Only non-overdue completed task
    }

    @Test
    void testGetCompletedTasksCountForTodayWithNullTaskGateway() {
        dataAccess = new InMemoryTodaySoFarDataAccess(null);
        
        int completed = dataAccess.getCompletedTasksCountForToday();
        
        assertEquals(0, completed);
    }

    @Test
    void testGetTodayTaskCompletionRateWithNoTasks() {
        when(mockTaskGateway.getTodaysTasks()).thenReturn(new ArrayList<>());
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        int rate = dataAccess.getTodayTaskCompletionRate();
        
        assertEquals(0, rate);
    }

    @Test
    void testGetTodayTaskCompletionRateWithPartialCompletion() {
        // Setup: 4 non-overdue tasks, 2 completed
        when(mockTask1.isOverdue()).thenReturn(false);
        when(mockTask1.isCompleted()).thenReturn(true);
        
        when(mockTask2.isOverdue()).thenReturn(false);
        when(mockTask2.isCompleted()).thenReturn(false);
        
        when(mockTask3.isOverdue()).thenReturn(false);
        when(mockTask3.isCompleted()).thenReturn(true);
        
        Task mockTask4 = mock(Task.class);
        when(mockTask4.isOverdue()).thenReturn(false);
        when(mockTask4.isCompleted()).thenReturn(false);
        
        List<Task> allTasks = Arrays.asList(mockTask1, mockTask2, mockTask3, mockTask4);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        int rate = dataAccess.getTodayTaskCompletionRate();
        
        assertEquals(50, rate); // 2 completed out of 4 = 50%
    }

    @Test
    void testGetTodayTaskCompletionRateWith100Percent() {
        // Setup: 2 non-overdue tasks, both completed
        when(mockTask1.isOverdue()).thenReturn(false);
        when(mockTask1.isCompleted()).thenReturn(true);
        
        when(mockTask2.isOverdue()).thenReturn(false);
        when(mockTask2.isCompleted()).thenReturn(true);
        
        List<Task> allTasks = Arrays.asList(mockTask1, mockTask2);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        int rate = dataAccess.getTodayTaskCompletionRate();
        
        assertEquals(100, rate);
    }

    @Test
    void testGetTodayTaskCompletionRateWithRounding() {
        // Setup: 3 non-overdue tasks, 1 completed = 33.33...% should round to 33%
        when(mockTask1.isOverdue()).thenReturn(false);
        when(mockTask1.isCompleted()).thenReturn(true);
        
        when(mockTask2.isOverdue()).thenReturn(false);
        when(mockTask2.isCompleted()).thenReturn(false);
        
        when(mockTask3.isOverdue()).thenReturn(false);
        when(mockTask3.isCompleted()).thenReturn(false);
        
        List<Task> allTasks = Arrays.asList(mockTask1, mockTask2, mockTask3);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        int rate = dataAccess.getTodayTaskCompletionRate();
        
        assertEquals(33, rate); // Should round 33.33... to 33
    }

    @Test
    void testCompleteWorkflowIntegration() {
        // Setup comprehensive scenario
        when(mockTask1.isOverdue()).thenReturn(false);
        when(mockTask1.isCompleted()).thenReturn(true);
        
        when(mockTask2.isOverdue()).thenReturn(true); // Overdue
        when(mockTask2.isCompleted()).thenReturn(true);
        
        when(mockTask3.isOverdue()).thenReturn(false);
        when(mockTask3.isCompleted()).thenReturn(false);
        
        List<Task> allTasks = Arrays.asList(mockTask1, mockTask2, mockTask3);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        List<EventInterf> events = Arrays.asList(mockEvent1);
        when(mockEventDataAccess.getTodaysEvents()).thenReturn(events);
        
        List<WellnessLogEntryInterf> wellness = Arrays.asList(mockWellnessEntry1);
        when(mockWellnessDataAccess.getTodaysWellnessLogEntries()).thenReturn(wellness);
        
        List<Goal> goals = Arrays.asList(mockGoal1, mockGoal2);
        when(mockGoalRepository.getTodayGoals()).thenReturn(goals);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(
                mockTaskGateway, mockEventDataAccess, mockWellnessDataAccess, mockGoalRepository
        );
        
        // Test all methods
        assertEquals(1, dataAccess.getCompletedTasksForToday().size()); // Only completed, non-overdue
        assertEquals(1, dataAccess.getCompletedEventsForToday().size());
        assertEquals(1, dataAccess.getWellnessEntriesForToday().size());
        assertEquals(2, dataAccess.getActiveGoals().size());
        assertEquals(2, dataAccess.getTotalTasksForToday()); // Only non-overdue tasks
        assertEquals(1, dataAccess.getCompletedTasksCountForToday()); // Only completed, non-overdue
        assertEquals(50, dataAccess.getTodayTaskCompletionRate()); // 1/2 = 50%
    }

    @Test
    void testMinimalConstructorWorkflow() {
        // Test using only task gateway constructor
        when(mockTask1.isOverdue()).thenReturn(false);
        when(mockTask1.isCompleted()).thenReturn(true);
        
        List<Task> allTasks = Arrays.asList((Task) mockTask1);
        when(mockTaskGateway.getTodaysTasks()).thenReturn(allTasks);
        
        dataAccess = new InMemoryTodaySoFarDataAccess(mockTaskGateway);
        
        assertEquals(1, dataAccess.getCompletedTasksForToday().size());
        assertTrue(dataAccess.getCompletedEventsForToday().isEmpty());
        assertTrue(dataAccess.getWellnessEntriesForToday().isEmpty());
        assertTrue(dataAccess.getActiveGoals().isEmpty());
        assertEquals(1, dataAccess.getTotalTasksForToday());
        assertEquals(1, dataAccess.getCompletedTasksCountForToday());
        assertEquals(100, dataAccess.getTodayTaskCompletionRate());
    }
}