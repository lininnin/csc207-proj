package entity.Angela;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskFactory;
import entity.info.Info;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactory;
import entity.Alex.MoodLabel.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TodaySoFarSnapshot entity.
 * Tests snapshot creation and data integrity for daily summaries.
 */
class TodaySoFarSnapshotTest {

    private LocalDate testDate;
    private List<Task> todaysTasks;
    private List<Task> completedTasks;
    private List<Task> overdueTasks;
    private List<Info> todaysEvents;
    private List<TodaySoFarSnapshot.GoalProgress> goalProgress;
    private List<WellnessLogEntry> wellnessEntries;
    private TaskFactory taskFactory;
    private WellnessLogEntryFactory wellnessFactory;
    private MoodLabelFactory moodFactory;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2023, 12, 15);
        taskFactory = new TaskFactory();
        wellnessFactory = new WellnessLogEntryFactory();
        moodFactory = new MoodLabelFactory();
        
        // Set up test data
        setupTasks();
        setupEvents();
        setupGoals();
        setupWellnessEntries();
    }

    private void setupTasks() {
        todaysTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        overdueTasks = new ArrayList<>();

        // Create some test tasks
        Info taskInfo1 = new Info.Builder("Complete project").description("Finish the final report").build();
        Task task1 = taskFactory.create("task1", taskInfo1, Task.Priority.HIGH, testDate, false, "template1");
        todaysTasks.add(task1);

        Info taskInfo2 = new Info.Builder("Review code").description("Code review session").build();
        Task completedTask = taskFactory.create("task2", taskInfo2, Task.Priority.MEDIUM, testDate, false, "template2");
        completedTasks.add(completedTask);

        Info overdueInfo = new Info.Builder("Overdue task").description("This was due yesterday").build();
        Task overdueTask = taskFactory.create("task3", overdueInfo, Task.Priority.LOW, testDate.minusDays(1), false, "template3");
        overdueTasks.add(overdueTask);
    }

    private void setupEvents() {
        todaysEvents = new ArrayList<>();
        Info eventInfo = new Info.Builder("Team meeting").description("Weekly standup").build();
        todaysEvents.add(eventInfo);
    }

    private void setupGoals() {
        goalProgress = new ArrayList<>();
        TodaySoFarSnapshot.GoalProgress progress = new TodaySoFarSnapshot.GoalProgress(
                "goal1", "Exercise", "daily", 2, 3
        );
        goalProgress.add(progress);
    }

    private void setupWellnessEntries() {
        wellnessEntries = new ArrayList<>();
        MoodLabel moodLabel = moodFactory.create("Happy", Type.POSITIVE);
        WellnessLogEntry entry = wellnessFactory.create(
                "wellness1", 
                Levels.HIGH, 
                Levels.LOW, 
                Levels.HIGH, 
                Levels.LOW, 
                "Feeling good today", 
                moodLabel
        );
        wellnessEntries.add(entry);
    }

    @Test
    void testSnapshotCreation() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate,
                todaysTasks,
                completedTasks,
                67, // 67% completion rate
                overdueTasks,
                todaysEvents,
                goalProgress,
                wellnessEntries
        );

        assertNotNull(snapshot);
        assertNotNull(snapshot.getId());
        assertEquals(testDate, snapshot.getDate());
        assertNotNull(snapshot.getCreatedAt());
        assertTrue(snapshot.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testSnapshotIdGeneration() {
        TodaySoFarSnapshot snapshot1 = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 50, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );
        
        TodaySoFarSnapshot snapshot2 = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 50, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        assertNotEquals(snapshot1.getId(), snapshot2.getId());
    }

    @Test
    void testTaskData() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 75, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        assertEquals(todaysTasks.size(), snapshot.getTodaysTasks().size());
        assertEquals(completedTasks.size(), snapshot.getCompletedTasks().size());
        assertEquals(overdueTasks.size(), snapshot.getOverdueTasks().size());
        assertEquals(75, snapshot.getTaskCompletionRate());

        // Verify data integrity
        assertEquals("Complete project", snapshot.getTodaysTasks().get(0).getName());
        assertEquals("Review code", snapshot.getCompletedTasks().get(0).getName());
        assertEquals("Overdue task", snapshot.getOverdueTasks().get(0).getName());
    }

    @Test
    void testEventData() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 50, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        assertEquals(todaysEvents.size(), snapshot.getTodaysEvents().size());
        assertEquals("Team meeting", snapshot.getTodaysEvents().get(0).getName());
    }

    @Test
    void testGoalProgressData() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 50, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        assertEquals(goalProgress.size(), snapshot.getGoalProgress().size());
        TodaySoFarSnapshot.GoalProgress progress = snapshot.getGoalProgress().get(0);
        assertEquals("goal1", progress.getGoalId());
        assertEquals("Exercise", progress.getGoalName());
        assertEquals("daily", progress.getPeriod());
        assertEquals(2, progress.getCurrent());
        assertEquals(3, progress.getTarget());
        assertEquals(67, progress.getProgressPercentage()); // 2/3 * 100 = 67%
    }

    @Test
    void testWellnessData() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 50, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        assertEquals(wellnessEntries.size(), snapshot.getWellnessEntries().size());
        WellnessLogEntry entry = snapshot.getWellnessEntries().get(0);
        assertEquals("wellness1", entry.getId());
        assertEquals("Feeling good today", entry.getNotes());
        assertEquals(Levels.HIGH, entry.getMood());
    }

    @Test
    void testEmptyCollections() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate,
                new ArrayList<>(),
                new ArrayList<>(),
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertTrue(snapshot.getTodaysTasks().isEmpty());
        assertTrue(snapshot.getCompletedTasks().isEmpty());
        assertTrue(snapshot.getOverdueTasks().isEmpty());
        assertTrue(snapshot.getTodaysEvents().isEmpty());
        assertTrue(snapshot.getGoalProgress().isEmpty());
        assertTrue(snapshot.getWellnessEntries().isEmpty());
        assertEquals(0, snapshot.getTaskCompletionRate());
    }

    @Test
    void testDataEncapsulation() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 50, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        // Verify that modifying original lists doesn't affect snapshot
        int originalTasksSize = snapshot.getTodaysTasks().size();
        todaysTasks.clear();
        assertEquals(originalTasksSize, snapshot.getTodaysTasks().size());

        // Verify that modifying returned lists doesn't affect snapshot internal state
        List<Task> returnedTasks = snapshot.getTodaysTasks();
        assertThrows(UnsupportedOperationException.class, () -> returnedTasks.clear());
    }

    @Test
    void testGoalProgressCreation() {
        TodaySoFarSnapshot.GoalProgress progress = new TodaySoFarSnapshot.GoalProgress(
                "test-goal", "Test Goal", "weekly", 5, 10
        );

        assertEquals("test-goal", progress.getGoalId());
        assertEquals("Test Goal", progress.getGoalName());
        assertEquals("weekly", progress.getPeriod());
        assertEquals(5, progress.getCurrent());
        assertEquals(10, progress.getTarget());
        assertEquals(50, progress.getProgressPercentage());
    }

    @Test
    void testGoalProgressPercentageCalculation() {
        // Test various percentage calculations
        TodaySoFarSnapshot.GoalProgress progress1 = new TodaySoFarSnapshot.GoalProgress(
                "goal1", "Goal 1", "daily", 0, 5
        );
        assertEquals(0, progress1.getProgressPercentage());

        TodaySoFarSnapshot.GoalProgress progress2 = new TodaySoFarSnapshot.GoalProgress(
                "goal2", "Goal 2", "daily", 5, 5
        );
        assertEquals(100, progress2.getProgressPercentage());

        TodaySoFarSnapshot.GoalProgress progress3 = new TodaySoFarSnapshot.GoalProgress(
                "goal3", "Goal 3", "daily", 3, 7
        );
        assertEquals(43, progress3.getProgressPercentage()); // 3/7 * 100 = 42.857... rounded to 43

        TodaySoFarSnapshot.GoalProgress progress4 = new TodaySoFarSnapshot.GoalProgress(
                "goal4", "Goal 4", "daily", 8, 5
        );
        assertEquals(100, progress4.getProgressPercentage()); // Capped at 100%
    }

    @Test
    void testGoalProgressWithZeroTarget() {
        TodaySoFarSnapshot.GoalProgress progress = new TodaySoFarSnapshot.GoalProgress(
                "goal", "Goal with zero target", "daily", 1, 0
        );

        assertEquals(0, progress.getProgressPercentage());
    }

    @Test
    void testSnapshotWithNullDate() {
        assertThrows(NullPointerException.class, () -> new TodaySoFarSnapshot(
                null, todaysTasks, completedTasks, 50, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        ));
    }

    @Test
    void testSnapshotWithNullCollections() {
        // Should handle null collections gracefully by creating empty lists
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, null, null, 0, null, null, null, null
        );

        assertTrue(snapshot.getTodaysTasks().isEmpty());
        assertTrue(snapshot.getCompletedTasks().isEmpty());
        assertTrue(snapshot.getOverdueTasks().isEmpty());
        assertTrue(snapshot.getTodaysEvents().isEmpty());
        assertTrue(snapshot.getGoalProgress().isEmpty());
        assertTrue(snapshot.getWellnessEntries().isEmpty());
    }

    @Test
    void testNegativeTaskCompletionRate() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, -10, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        assertEquals(-10, snapshot.getTaskCompletionRate());
    }

    @Test
    void testOverHundredTaskCompletionRate() {
        TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                testDate, todaysTasks, completedTasks, 150, overdueTasks,
                todaysEvents, goalProgress, wellnessEntries
        );

        assertEquals(150, snapshot.getTaskCompletionRate());
    }
}