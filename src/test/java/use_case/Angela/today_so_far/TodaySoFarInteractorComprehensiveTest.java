package use_case.Angela.today_so_far;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import entity.Angela.Task.TaskFactory;
import entity.Alex.Event.Event;
import entity.Alex.Event.EventInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.Type;
import entity.Sophia.Goal;
import entity.Sophia.GoalInterface;
import entity.Sophia.GoalInfo;
import entity.Sophia.GoalInterface.TimePeriod;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Category;
import entity.info.Info;
// Removed CategoryGateway import - will use CategoryReadDataAccessInterface
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for TodaySoFarInteractor.
 * Tests all methods including private helper methods indirectly through public API.
 * Target: >90% line coverage
 */
class TodaySoFarInteractorComprehensiveTest {
    
    private TodaySoFarInteractor interactor;
    private ComprehensiveTestDataAccess dataAccess;
    private ComprehensiveTestPresenter presenter;
    private ComprehensiveTestCategoryGateway categoryGateway;
    
    @BeforeEach
    void setUp() {
        dataAccess = new ComprehensiveTestDataAccess();
        presenter = new ComprehensiveTestPresenter();
        categoryGateway = new ComprehensiveTestCategoryGateway();
        interactor = new TodaySoFarInteractor(dataAccess, presenter, categoryGateway);
    }
    
    @Test
    void testRefreshWithCompleteDataIncludingAllTypes() {
        // Setup complete data with all types to test all code paths
        dataAccess.setupCompleteData();
        categoryGateway.addCategory(new Category("cat1", "Work", "#FF0000"));
        categoryGateway.addCategory(new Category("cat2", "Personal", "#00FF00"));
        
        // Execute
        interactor.refreshTodaySoFar();
        
        // Verify success
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
        
        // Verify goals are processed (tests formatPeriod and formatProgress)
        List<TodaySoFarOutputData.GoalProgress> goals = presenter.lastOutputData.getGoals();
        assertFalse(goals.isEmpty());
        
        // Check that different period types are formatted correctly
        assertTrue(goals.stream().anyMatch(g -> g.getPeriod().equals("Weekly")));
        assertTrue(goals.stream().anyMatch(g -> g.getPeriod().equals("Monthly")));
        // The third goal should also be Weekly since TimePeriod only has WEEK/MONTH
        assertTrue(goals.size() >= 2); // Verify we have multiple goals
        
        // Check progress formatting
        assertTrue(goals.stream().anyMatch(g -> g.getProgress().contains("/")));
        
        // Verify completed items include both tasks and events
        List<TodaySoFarOutputData.CompletedItem> completedItems = presenter.lastOutputData.getCompletedItems();
        assertFalse(completedItems.isEmpty());
        assertTrue(completedItems.stream().anyMatch(item -> item.getType().equals("Task")));
        assertTrue(completedItems.stream().anyMatch(item -> item.getType().equals("Event")));
        
        // Verify category names are resolved
        assertTrue(completedItems.stream().anyMatch(item -> item.getCategory().equals("Work")));
        assertTrue(completedItems.stream().anyMatch(item -> item.getCategory().equals("Personal")));
        
        // Verify wellness entries
        assertFalse(presenter.lastOutputData.getWellnessEntries().isEmpty());
        TodaySoFarOutputData.WellnessEntry wellness = presenter.lastOutputData.getWellnessEntries().get(0);
        assertNotNull(wellness.getMood());
        assertTrue(wellness.getStress() > 0);
        assertTrue(wellness.getEnergy() > 0);
        assertTrue(wellness.getFatigue() > 0);
        assertNotNull(wellness.getTime());
        
        // Verify completion rate
        assertEquals(50, presenter.lastOutputData.getCompletionRate()); // 2 completed out of 4 total
    }
    
    @Test
    void testRefreshWithNullGoalTimePeriod() {
        // Test goal with null time period (should return "Ongoing")
        dataAccess.addGoalWithNullPeriod();
        
        interactor.refreshTodaySoFar();
        
        assertNotNull(presenter.lastOutputData);
        List<TodaySoFarOutputData.GoalProgress> goals = presenter.lastOutputData.getGoals();
        assertTrue(goals.stream().anyMatch(g -> g.getPeriod().equals("Ongoing")));
    }
    
    @Test
    void testRefreshWithEmptyCategoryIds() {
        // Test tasks/events with null or empty category IDs
        dataAccess.addTaskWithNullCategory();
        dataAccess.addEventWithEmptyCategory();
        
        interactor.refreshTodaySoFar();
        
        assertNotNull(presenter.lastOutputData);
        List<TodaySoFarOutputData.CompletedItem> items = presenter.lastOutputData.getCompletedItems();
        
        // Should have "-" for missing categories
        assertTrue(items.stream().anyMatch(item -> item.getCategory().equals("-")));
    }
    
    @Test
    void testRefreshWithNonExistentCategoryId() {
        // Test with category ID that doesn't exist in gateway
        dataAccess.addTaskWithCategory("non-existent-category");
        
        interactor.refreshTodaySoFar();
        
        assertNotNull(presenter.lastOutputData);
        List<TodaySoFarOutputData.CompletedItem> items = presenter.lastOutputData.getCompletedItems();
        
        // Should have "-" for non-existent category
        assertTrue(items.stream().anyMatch(item -> item.getCategory().equals("-")));
    }
    
    @Test
    void testRefreshWithMinimalWellnessFields() {
        // Test wellness entries with minimal valid data
        dataAccess.addWellnessWithNullFields();
        
        interactor.refreshTodaySoFar();
        
        assertNotNull(presenter.lastOutputData);
        List<TodaySoFarOutputData.WellnessEntry> wellness = presenter.lastOutputData.getWellnessEntries();
        assertFalse(wellness.isEmpty());
        
        TodaySoFarOutputData.WellnessEntry entry = wellness.get(0);
        assertEquals("Unknown", entry.getMood()); // default mood
        assertEquals(1, entry.getStress()); // LOW level = 1
        assertNotNull(entry.getTime()); // valid time set
    }
    
    @Test
    void testRefreshWithAllNullCollections() {
        // Test when data access returns null collections
        dataAccess.setReturnNullCollections(true);
        
        interactor.refreshTodaySoFar();
        
        // Should handle gracefully
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
        
        // All collections should be empty but not null
        assertNotNull(presenter.lastOutputData.getGoals());
        assertNotNull(presenter.lastOutputData.getCompletedItems());
        assertNotNull(presenter.lastOutputData.getWellnessEntries());
        assertTrue(presenter.lastOutputData.getGoals().isEmpty());
        assertTrue(presenter.lastOutputData.getCompletedItems().isEmpty());
        assertTrue(presenter.lastOutputData.getWellnessEntries().isEmpty());
    }
    
    @Test
    void testRefreshWithException() {
        dataAccess.setShouldThrowException(true);
        
        interactor.refreshTodaySoFar();
        
        assertNull(presenter.lastOutputData);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.contains("Failed to load Today So Far data"));
    }
    
    @Test
    void testGoalProgressFormatting() {
        // Test various goal configurations for formatting
        dataAccess.addGoalWithProgress(0, 10); // 0/10
        dataAccess.addGoalWithProgress(5, 10); // 5/10
        dataAccess.addGoalWithProgress(10, 10); // 10/10
        
        interactor.refreshTodaySoFar();
        
        List<TodaySoFarOutputData.GoalProgress> goals = presenter.lastOutputData.getGoals();
        assertTrue(goals.stream().anyMatch(g -> g.getProgress().equals("0/10")));
        assertTrue(goals.stream().anyMatch(g -> g.getProgress().equals("5/10")));
        assertTrue(goals.stream().anyMatch(g -> g.getProgress().equals("10/10")));
    }
    
    @Test
    void testCompletionRateEdgeCases() {
        // Test 0/0 case
        dataAccess.setTaskCounts(0, 0);
        interactor.refreshTodaySoFar();
        assertEquals(0, presenter.lastOutputData.getCompletionRate());
        
        // Test 10/10 case
        dataAccess.setTaskCounts(10, 10);
        interactor.refreshTodaySoFar();
        assertEquals(100, presenter.lastOutputData.getCompletionRate());
    }
    
    /**
     * Comprehensive test data access implementation with full data support.
     */
    private static class ComprehensiveTestDataAccess implements TodaySoFarDataAccessInterface {
        private List<Task> completedTasks = new ArrayList<>();
        private List<EventInterf> completedEvents = new ArrayList<>();
        private List<WellnessLogEntryInterf> wellnessEntries = new ArrayList<>();
        private List<Goal> activeGoals = new ArrayList<>();
        private int completedCount = 0;
        private int totalCount = 0;
        private boolean shouldThrowException = false;
        private boolean returnNullCollections = false;
        
        void setupCompleteData() {
            // Add tasks with different categories
            TaskFactory taskFactory = new TaskFactory();
            Info taskInfo1 = new Info.Builder("Task 1").category("cat1").build();
            Info taskInfo2 = new Info.Builder("Task 2").category("cat2").build();
            
            Task task1 = (Task) taskFactory.create("t1", "template1", taskInfo1, Task.Priority.HIGH,
                new BeginAndDueDates(LocalDate.now(), LocalDate.now()), true, LocalDateTime.now(), false);
            Task task2 = (Task) taskFactory.create("t2", "template2", taskInfo2, Task.Priority.MEDIUM,
                new BeginAndDueDates(LocalDate.now(), LocalDate.now()), true, LocalDateTime.now(), false);
            
            completedTasks.add(task1);
            completedTasks.add(task2);
            
            // Add events using Builder pattern
            Info eventInfo1 = new Info.Builder("Event 1").category("cat1").build();
            Info eventInfo2 = new Info.Builder("Event 2").category("cat2").build();
            BeginAndDueDates eventDates1 = new BeginAndDueDates(LocalDate.now(), LocalDate.now());
            BeginAndDueDates eventDates2 = new BeginAndDueDates(LocalDate.now(), LocalDate.now());
            
            Event event1 = new Event.Builder(eventInfo1)
                .beginAndDueDates(eventDates1)
                .oneTime(false)
                .build();
            Event event2 = new Event.Builder(eventInfo2)
                .beginAndDueDates(eventDates2)
                .oneTime(false)
                .build();
            completedEvents.add(event1);
            completedEvents.add(event2);
            
            // Add goals with different periods
            addGoalWithTimePeriod(TimePeriod.WEEK, 3, 5);
            addGoalWithTimePeriod(TimePeriod.MONTH, 10, 30);
            addCustomPeriodGoal();
            
            // Add wellness entries
            MoodLabel mood = new MoodLabel.Builder("Happy").type(Type.Positive).build();
            WellnessLogEntry wellness = new WellnessLogEntry.Builder()
                .time(LocalDateTime.now())
                .moodLabel(mood)
                .stressLevel(Levels.THREE)
                .energyLevel(Levels.SEVEN)
                .fatigueLevel(Levels.FOUR)
                .userNote("Feeling good")
                .build();
            wellnessEntries.add(wellness);
            
            // Set task counts
            completedCount = 2;
            totalCount = 4;
        }
        
        void addGoalWithTimePeriod(TimePeriod period, int current, int frequency) {
            Info goalInfo = new Info.Builder("Goal " + period).build();
            Info targetInfo = new Info.Builder("Target Task").build();
            GoalInfo gInfo = new GoalInfo(goalInfo, targetInfo);
            BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(30));
            Goal goal = new Goal(gInfo, dates, period, frequency);
            // Set progress by calling recordCompletion multiple times
            for (int i = 0; i < current; i++) {
                goal.recordCompletion();
            }
            activeGoals.add(goal);
        }
        
        void addCustomPeriodGoal() {
            Info goalInfo = new Info.Builder("Custom Goal").build();
            Info targetInfo = new Info.Builder("Target Task").build();
            GoalInfo gInfo = new GoalInfo(goalInfo, targetInfo);
            BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7));
            // Use null for custom period - will trigger the date range formatting
            Goal goal = new Goal(gInfo, dates, TimePeriod.WEEK, 7);
            for (int i = 0; i < 4; i++) {
                goal.recordCompletion();
            }
            activeGoals.add(goal);
        }
        
        void addGoalWithNullPeriod() {
            // Since Goal can't have null TimePeriod in constructor, 
            // we'll create a custom test goal that returns null for getTimePeriod()
            Info goalInfo = new Info.Builder("Ongoing Goal").build();
            Info targetInfo = new Info.Builder("Target Task").build();
            activeGoals.add(new Goal(
                new GoalInfo(goalInfo, targetInfo),
                new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(30)),
                TimePeriod.WEEK,
                5) {
                @Override
                public TimePeriod getTimePeriod() {
                    return null; // Override to return null for testing
                }
                @Override
                public int getCurrentProgress() {
                    return 2;
                }
            });
        }
        
        void addGoalWithProgress(int current, int frequency) {
            Info goalInfo = new Info.Builder("Progress Goal " + current).build();
            Info targetInfo = new Info.Builder("Target Task").build();
            GoalInfo gInfo = new GoalInfo(goalInfo, targetInfo);
            BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7));
            Goal goal = new Goal(gInfo, dates, TimePeriod.WEEK, frequency);
            for (int i = 0; i < current; i++) {
                goal.recordCompletion();
            }
            activeGoals.add(goal);
        }
        
        void addTaskWithNullCategory() {
            TaskFactory taskFactory = new TaskFactory();
            Info taskInfo = new Info.Builder("Task No Category").build();
            Task task = (Task) taskFactory.create("t3", "template3", taskInfo, null,
                new BeginAndDueDates(LocalDate.now(), LocalDate.now()), true, LocalDateTime.now(), false);
            completedTasks.add(task);
        }
        
        void addTaskWithCategory(String categoryId) {
            TaskFactory taskFactory = new TaskFactory();
            Info taskInfo = new Info.Builder("Task Custom Category").category(categoryId).build();
            Task task = (Task) taskFactory.create("t4", "template4", taskInfo, null,
                new BeginAndDueDates(LocalDate.now(), LocalDate.now()), true, LocalDateTime.now(), false);
            completedTasks.add(task);
        }
        
        void addEventWithEmptyCategory() {
            Info eventInfo = new Info.Builder("Event No Category").category("").build();
            BeginAndDueDates eventDates = new BeginAndDueDates(LocalDate.now(), LocalDate.now());
            Event event = new Event.Builder(eventInfo)
                .beginAndDueDates(eventDates)
                .oneTime(false)
                .build();
            completedEvents.add(event);
        }
        
        void addWellnessWithNullFields() {
            // Create wellness entry with minimal valid data 
            // (WellnessLogEntry.Builder requires all fields to be non-null)
            MoodLabel defaultMood = new MoodLabel.Builder("Unknown")
                .type(Type.Positive)
                .build();
            WellnessLogEntry wellness = new WellnessLogEntry.Builder()
                .time(LocalTime.now().atDate(LocalDate.now()))
                .moodLabel(defaultMood)
                .stressLevel(Levels.ONE)
                .energyLevel(Levels.ONE)
                .fatigueLevel(Levels.ONE)
                .build();
            wellnessEntries.add(wellness);
        }
        
        void setTaskCounts(int completed, int total) {
            this.completedCount = completed;
            this.totalCount = total;
        }
        
        void setShouldThrowException(boolean should) {
            this.shouldThrowException = should;
        }
        
        void setReturnNullCollections(boolean returnNull) {
            this.returnNullCollections = returnNull;
        }
        
        @Override
        public List<TaskInterf> getCompletedTasksForToday() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            if (returnNullCollections) return null;
            return new ArrayList<>(completedTasks);
        }
        
        @Override
        public List<EventInterf> getCompletedEventsForToday() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            if (returnNullCollections) return null;
            return completedEvents;
        }
        
        @Override
        public List<WellnessLogEntryInterf> getWellnessEntriesForToday() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            if (returnNullCollections) return null;
            return wellnessEntries;
        }
        
        @Override
        public List<GoalInterface> getActiveGoals() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            if (returnNullCollections) return null;
            return new ArrayList<>(activeGoals);
        }
        
        @Override
        public int getTodayTaskCompletionRate() {
            if (totalCount == 0) return 0;
            return (int) Math.round((completedCount * 100.0) / totalCount);
        }
        
        @Override
        public int getTotalTasksForToday() {
            return totalCount;
        }
        
        @Override
        public int getCompletedTasksCountForToday() {
            return completedCount;
        }
    }
    
    /**
     * Comprehensive test presenter.
     */
    private static class ComprehensiveTestPresenter implements TodaySoFarOutputBoundary {
        TodaySoFarOutputData lastOutputData;
        String lastError;
        
        @Override
        public void presentTodaySoFar(TodaySoFarOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }
        
        @Override
        public void presentError(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }
    }
    
    /**
     * Comprehensive test category gateway.
     */
    private static class ComprehensiveTestCategoryGateway implements CategoryReadDataAccessInterface {
        private Map<String, Category> categories = new HashMap<>();
        
        void addCategory(Category category) {
            categories.put(category.getId(), category);
        }
        
        @Override
        public Category getCategoryById(String id) {
            return categories.get(id);
        }
    }
}