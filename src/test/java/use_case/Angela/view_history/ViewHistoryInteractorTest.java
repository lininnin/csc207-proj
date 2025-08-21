package use_case.Angela.view_history;

import entity.Angela.TodaySoFarSnapshot;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Category;
import entity.alex.WellnessLogEntry.WellnessLogEntry;
import entity.alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.alex.WellnessLogEntry.Levels;
import entity.alex.MoodLabel.MoodLabel;
import entity.alex.MoodLabel.MoodLabelFactory;
import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.MoodLabel.Type;
import entity.info.Info;
import entity.info.InfoFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import static java.util.Collections.singletonList;

import static org.junit.Assert.*;

/**
 * Comprehensive test suite for ViewHistoryInteractor.
 * Tests all methods and edge cases to achieve >90% coverage.
 */
public class ViewHistoryInteractorTest {
    
    private ViewHistoryInteractor interactor;
    private TestDataAccess dataAccess;
    private TestPresenter presenter;
    private TaskFactory taskFactory;
    private InfoFactory infoFactory;
    private WellnessLogEntryFactory wellnessFactory;
    private MoodLabelFactory moodLabelFactory;
    
    @Before
    public void setUp() {
        dataAccess = new TestDataAccess();
        presenter = new TestPresenter();
        interactor = new ViewHistoryInteractor(dataAccess, presenter);
        taskFactory = new TaskFactory();
        infoFactory = new InfoFactory();
        wellnessFactory = new WellnessLogEntryFactory();
        moodLabelFactory = new MoodLabelFactory();
    }
    
    @After
    public void tearDown() {
        // Clean up any exported files
        String filename = "history_2024-01-15.txt";
        String filePath = System.getProperty("user.home") + "/Downloads/" + filename;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
    
    @Test
    public void testExecute_SuccessWithFullData() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        TodaySoFarSnapshot snapshot = createFullSnapshot(testDate);
        dataAccess.addSnapshot(testDate, snapshot);
        
        ViewHistoryInputData inputData = new ViewHistoryInputData(testDate);
        
        // Act
        interactor.execute(inputData);
        
        // Assert
        assertTrue(presenter.successViewCalled);
        assertFalse(presenter.failViewCalled);
        assertNotNull(presenter.outputData);
        assertEquals(testDate, presenter.outputData.getDate());
        
        // Verify we got the data
        assertFalse(presenter.outputData.getTodaysTasks().isEmpty());
        assertFalse(presenter.outputData.getTodaysEvents().isEmpty());
    }
    
    @Test
    public void testExecute_FutureDate() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusDays(1);
        ViewHistoryInputData inputData = new ViewHistoryInputData(futureDate);
        
        // Act
        interactor.execute(inputData);
        
        // Assert
        assertFalse(presenter.successViewCalled);
        assertTrue(presenter.failViewCalled);
        assertEquals("Cannot view history for future dates", presenter.errorMessage);
    }
    
    @Test
    public void testExecute_NoDataAvailable() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        ViewHistoryInputData inputData = new ViewHistoryInputData(testDate);
        
        // Act
        interactor.execute(inputData);
        
        // Assert
        assertFalse(presenter.successViewCalled);
        assertTrue(presenter.failViewCalled);
        assertTrue(presenter.errorMessage.contains("No history data available"));
    }
    
    @Test
    public void testExecute_EmptySnapshot() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        TodaySoFarSnapshot emptySnapshot = new TodaySoFarSnapshot(
            testDate,
            new ArrayList<>(),  // empty today's tasks
            new ArrayList<>(),  // empty completed tasks
            0,                  // 0% completion rate
            new ArrayList<>(),  // empty overdue tasks
            new ArrayList<>(),  // empty events
            new ArrayList<>(),  // empty goal progress
            new ArrayList<>()   // empty wellness entries
        );
        dataAccess.addSnapshot(testDate, emptySnapshot);
        
        ViewHistoryInputData inputData = new ViewHistoryInputData(testDate);
        
        // Act
        interactor.execute(inputData);
        
        // Assert
        assertTrue(presenter.successViewCalled);
        assertFalse(presenter.failViewCalled);
        assertNotNull(presenter.outputData);
        assertTrue(presenter.outputData.getTodaysTasks().isEmpty());
    }
    
    @Test
    public void testLoadAvailableDates() {
        // Arrange
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 1, 16);
        LocalDate date3 = LocalDate.of(2024, 1, 17);
        
        dataAccess.addSnapshot(date1, createFullSnapshot(date1));
        dataAccess.addSnapshot(date2, createFullSnapshot(date2));
        dataAccess.addSnapshot(date3, createFullSnapshot(date3));
        
        // Act
        interactor.loadAvailableDates();
        
        // Assert
        assertTrue(presenter.availableDatesCalled);
        assertEquals(3, presenter.availableDates.size());
        assertTrue(presenter.availableDates.contains(date1));
        assertTrue(presenter.availableDates.contains(date2));
        assertTrue(presenter.availableDates.contains(date3));
    }
    
    @Test
    public void testLoadAvailableDates_Empty() {
        // Act
        interactor.loadAvailableDates();
        
        // Assert
        assertTrue(presenter.availableDatesCalled);
        assertTrue(presenter.availableDates.isEmpty());
    }
    
    @Test
    public void testExportHistory_Success() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        TodaySoFarSnapshot snapshot = createFullSnapshot(testDate);
        dataAccess.addSnapshot(testDate, snapshot);
        
        ViewHistoryInputData inputData = new ViewHistoryInputData(testDate);
        
        // Act
        interactor.exportHistory(inputData);
        
        // Assert
        assertTrue(presenter.exportSuccessCalled);
        assertFalse(presenter.exportFailureCalled);
        assertNotNull(presenter.exportPath);
        
        // Verify file was created
        File exportedFile = new File(presenter.exportPath);
        assertTrue(exportedFile.exists());
        
        // Clean up
        exportedFile.delete();
    }
    
    @Test
    public void testExportHistory_NoData() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        ViewHistoryInputData inputData = new ViewHistoryInputData(testDate);
        
        // Act
        interactor.exportHistory(inputData);
        
        // Assert
        assertFalse(presenter.exportSuccessCalled);
        assertTrue(presenter.exportFailureCalled);
        assertEquals("No data available for export", presenter.exportError);
    }
    
    @Test
    public void testExportHistory_WithAllDataTypes() throws IOException {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        TodaySoFarSnapshot snapshot = createFullSnapshot(testDate);
        dataAccess.addSnapshot(testDate, snapshot);
        
        ViewHistoryInputData inputData = new ViewHistoryInputData(testDate);
        
        // Act
        interactor.exportHistory(inputData);
        
        // Assert
        assertTrue(presenter.exportSuccessCalled);
        
        // Read and verify file content
        Path path = Paths.get(presenter.exportPath);
        String content = new String(Files.readAllBytes(path));
        
        // Verify all sections are present
        assertTrue(content.contains("=== MindTrack History Report ==="));
        assertTrue(content.contains("=== Today's Tasks ==="));
        assertTrue(content.contains("Completion Rate:"));
        assertTrue(content.contains("Completed:"));
        assertTrue(content.contains("✓ Task 1 [Work]"));
        assertTrue(content.contains("Overdue:"));
        assertTrue(content.contains("⚠ Overdue Task"));
        assertTrue(content.contains("=== Today's Events ==="));
        assertTrue(content.contains("• Event 1 [Personal]"));
        assertTrue(content.contains("=== Goal Progress ==="));
        assertTrue(content.contains("Exercise (weekly): 3/5"));
        assertTrue(content.contains("=== Wellness Log ==="));
        assertTrue(content.contains("Mood: Happy"));
        assertTrue(content.contains("Stress: 3/10"));
        assertTrue(content.contains("Energy: 7/10"));
        assertTrue(content.contains("Fatigue: 4/10"));
        
        // Clean up
        Files.delete(path);
    }
    
    @Test
    public void testExportHistory_EmptySnapshot() throws IOException {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        TodaySoFarSnapshot emptySnapshot = new TodaySoFarSnapshot(
            testDate,
            new ArrayList<>(),
            new ArrayList<>(),
            0,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );
        dataAccess.addSnapshot(testDate, emptySnapshot);
        
        ViewHistoryInputData inputData = new ViewHistoryInputData(testDate);
        
        // Act
        interactor.exportHistory(inputData);
        
        // Assert
        assertTrue(presenter.exportSuccessCalled);
        
        // Verify file was created but doesn't contain data sections
        Path path = Paths.get(presenter.exportPath);
        String content = new String(Files.readAllBytes(path));
        
        assertTrue(content.contains("=== MindTrack History Report ==="));
        assertTrue(content.contains("Completion Rate: 0%"));
        assertFalse(content.contains("Completed:"));
        assertFalse(content.contains("Overdue:"));
        assertFalse(content.contains("=== Today's Events ==="));
        assertFalse(content.contains("=== Goal Progress ==="));
        assertFalse(content.contains("=== Wellness Log ==="));
        
        // Clean up
        Files.delete(path);
    }
    
    // Helper method to create a full snapshot with all data types
    private TodaySoFarSnapshot createFullSnapshot(LocalDate date) {
        // Create tasks using Info factory with correct signature
        Info taskInfo1 = (Info) infoFactory.create("Task 1", "Description 1", "Work");
        Info taskInfo2 = (Info) infoFactory.create("Task 2", "Description 2", "Personal");
        
        // Create tasks using correct factory method signatures
        BeginAndDueDates dates1 = new BeginAndDueDates(LocalDate.now(), LocalDate.now());
        BeginAndDueDates dates2 = new BeginAndDueDates(LocalDate.now(), LocalDate.now());
        
        Task completedTask = (Task) taskFactory.create("task1", "template1", taskInfo1, Task.Priority.HIGH,
            dates1, true, LocalDateTime.now(), false);
        
        Task incompleteTask = (Task) taskFactory.create("task2", "template2", taskInfo2, Task.Priority.MEDIUM,
            dates2, false, null, false);
        
        // Create overdue task
        Info overdueInfo = (Info) infoFactory.create("Overdue Task", "Overdue", "Work");
        BeginAndDueDates overdueDates = new BeginAndDueDates(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        Task overdueTask = (Task) taskFactory.create("task3", "template3", overdueInfo, Task.Priority.HIGH,
            overdueDates, false, null, true);
        
        // Create events with different creation times for sorting test
        Info event1 = (Info) infoFactory.create("Event 1", "Event Description 1", "Personal");
        Info event2 = (Info) infoFactory.create("Event 2", "Event Description 2", "Work");
        
        // Create goal progress with proper constructor (goalId, goalName, period, current, target)
        TodaySoFarSnapshot.GoalProgress goalProgress = 
            new TodaySoFarSnapshot.GoalProgress("goal1", "Exercise", "weekly", 3, 5);
        
        // Create wellness entries with correct factory method
        MoodLabelInterf happyMood = moodLabelFactory.create("Happy", Type.Positive);
        WellnessLogEntryInterf wellnessEntry = wellnessFactory.create(
            LocalDateTime.now(),
            Levels.THREE,  // stress level
            Levels.SEVEN,  // energy level
            Levels.FOUR,   // fatigue level
            happyMood,
            "Feeling good today"
        );
        
        return new TodaySoFarSnapshot(
            date,
            Arrays.asList(completedTask, incompleteTask),
            singletonList(completedTask),
            50,  // 50% completion rate (int)
            singletonList(overdueTask),
            Arrays.asList(event2, event1),
            singletonList(goalProgress),
            singletonList((WellnessLogEntry) wellnessEntry)
        );
    }
    
    // Test implementations of interfaces
    private static class TestDataAccess implements ViewHistoryDataAccessInterface {
        private final Map<LocalDate, TodaySoFarSnapshot> snapshots = new HashMap<>();
        
        void addSnapshot(LocalDate date, TodaySoFarSnapshot snapshot) {
            snapshots.put(date, snapshot);
        }
        
        @Override
        public TodaySoFarSnapshot getSnapshot(LocalDate date) {
            return snapshots.get(date);
        }
        
        @Override
        public List<LocalDate> getAvailableDates() {
            return new ArrayList<>(snapshots.keySet());
        }
        
        @Override
        public int cleanupOldSnapshots(int daysToKeep) {
            // Test implementation - remove snapshots older than daysToKeep
            LocalDate cutoffDate = LocalDate.now().minusDays(daysToKeep);
            int initialSize = snapshots.size();
            snapshots.entrySet().removeIf(entry -> entry.getKey().isBefore(cutoffDate));
            return initialSize - snapshots.size();
        }
        
        @Override
        public boolean saveSnapshot(TodaySoFarSnapshot snapshot) {
            snapshots.put(snapshot.getDate(), snapshot);
            return true;
        }
        
        @Override
        public boolean hasSnapshot(LocalDate date) {
            return snapshots.containsKey(date);
        }
    }
    
    private static class TestPresenter implements ViewHistoryOutputBoundary {
        boolean successViewCalled = false;
        boolean failViewCalled = false;
        boolean availableDatesCalled = false;
        boolean exportSuccessCalled = false;
        boolean exportFailureCalled = false;
        
        ViewHistoryOutputData outputData;
        String errorMessage;
        List<LocalDate> availableDates;
        String exportPath;
        String exportError;
        
        @Override
        public void prepareSuccessView(ViewHistoryOutputData outputData) {
            this.successViewCalled = true;
            this.outputData = outputData;
        }
        
        @Override
        public void prepareFailView(String error) {
            this.failViewCalled = true;
            this.errorMessage = error;
        }
        
        @Override
        public void presentAvailableDates(List<LocalDate> dates) {
            this.availableDatesCalled = true;
            this.availableDates = dates;
        }
        
        @Override
        public void presentExportSuccess(String filePath) {
            this.exportSuccessCalled = true;
            this.exportPath = filePath;
        }
        
        @Override
        public void presentExportFailure(String error) {
            this.exportFailureCalled = true;
            this.exportError = error;
        }
    }
}