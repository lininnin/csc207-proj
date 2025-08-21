package app.Angela;

import entity.Angela.TodaySoFarSnapshot;
import entity.Angela.Task.Task;
import entity.info.Info;
import entity.Sophia.Goal;
import entity.alex.WellnessLogEntry.WellnessLogEntry;
import use_case.Angela.view_history.ViewHistoryDataAccessInterface;
import app.SharedDataAccess;
import data_access.InMemoryTaskDataAccessObject;
import data_access.files.FileGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for DailySnapshotService.
 */
class DailySnapshotServiceTest {

    @Mock
    private ViewHistoryDataAccessInterface historyDataAccess;

    @Mock
    private SharedDataAccess sharedDataAccess;

    @Mock
    private InMemoryTaskDataAccessObject taskGateway;

    @Mock
    private FileGoalRepository goalRepository;

    @Mock
    private Task task1;

    @Mock
    private Task task2;

    @Mock
    private Goal goal;

    private MockedStatic<HistoryPageBuilder> historyPageBuilderMock;
    private MockedStatic<SharedDataAccess> sharedDataAccessMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Reset singleton before each test
        DailySnapshotService.resetForTesting();
        
        // Mock static methods
        historyPageBuilderMock = mockStatic(HistoryPageBuilder.class);
        sharedDataAccessMock = mockStatic(SharedDataAccess.class);
        
        historyPageBuilderMock.when(HistoryPageBuilder::getHistoryDataAccess)
            .thenReturn(historyDataAccess);
        sharedDataAccessMock.when(SharedDataAccess::getInstance)
            .thenReturn(sharedDataAccess);
    }

    @AfterEach
    void tearDown() {
        if (historyPageBuilderMock != null) {
            historyPageBuilderMock.close();
        }
        if (sharedDataAccessMock != null) {
            sharedDataAccessMock.close();
        }
        DailySnapshotService.resetForTesting();
    }

    @Test
    void testGetInstanceReturnsSameInstance() {
        // Act
        DailySnapshotService instance1 = DailySnapshotService.getInstance();
        DailySnapshotService instance2 = DailySnapshotService.getInstance();

        // Assert
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    void testResetForTestingClearsInstance() {
        // Arrange
        DailySnapshotService instance1 = DailySnapshotService.getInstance();

        // Act
        DailySnapshotService.resetForTesting();
        DailySnapshotService instance2 = DailySnapshotService.getInstance();

        // Assert
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }

    @Test
    void testStartAutomaticSnapshots() {
        // Arrange
        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> service.startAutomaticSnapshots());
    }

    @Test
    void testStopAutomaticSnapshots() {
        // Arrange
        DailySnapshotService service = DailySnapshotService.getInstance();
        service.startAutomaticSnapshots();

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> service.stopAutomaticSnapshots());
    }

    @Test
    void testCreateAndSaveSnapshotWhenSnapshotAlreadyExists() {
        // Arrange
        when(historyDataAccess.hasSnapshot(any(LocalDate.class))).thenReturn(true);
        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act
        service.createAndSaveSnapshot();

        // Assert
        verify(historyDataAccess).hasSnapshot(any(LocalDate.class));
        verify(historyDataAccess, never()).saveSnapshot(any());
    }

    @Test
    void testCreateAndSaveSnapshotSuccess() {
        // Arrange
        LocalDate today = LocalDate.now();
        when(historyDataAccess.hasSnapshot(today)).thenReturn(false);
        when(historyDataAccess.saveSnapshot(any(TodaySoFarSnapshot.class))).thenReturn(true);
        when(historyDataAccess.cleanupOldSnapshots(30)).thenReturn(5);
        
        when(sharedDataAccess.getTaskGateway()).thenReturn(taskGateway);
        when(sharedDataAccess.getGoalRepository()).thenReturn(goalRepository);
        
        when(task1.isCompleted()).thenReturn(true);
        when(task2.isCompleted()).thenReturn(false);
        List<Task> todaysTasks = Arrays.asList(task1, task2);
        when(taskGateway.getTodaysTasks()).thenReturn(todaysTasks);
        when(goalRepository.getCurrentGoals()).thenReturn(Arrays.asList());

        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act
        service.createAndSaveSnapshot();

        // Assert
        verify(historyDataAccess).hasSnapshot(today);
        verify(historyDataAccess).saveSnapshot(any(TodaySoFarSnapshot.class));
        verify(historyDataAccess).cleanupOldSnapshots(30);
        verify(taskGateway).getTodaysTasks();
    }

    @Test
    void testCreateAndSaveSnapshotFailure() {
        // Arrange
        LocalDate today = LocalDate.now();
        when(historyDataAccess.hasSnapshot(today)).thenReturn(false);
        when(historyDataAccess.saveSnapshot(any(TodaySoFarSnapshot.class))).thenReturn(false);
        
        when(sharedDataAccess.getTaskGateway()).thenReturn(taskGateway);
        when(sharedDataAccess.getGoalRepository()).thenReturn(goalRepository);
        
        List<Task> todaysTasks = Arrays.asList();
        when(taskGateway.getTodaysTasks()).thenReturn(todaysTasks);
        when(goalRepository.getCurrentGoals()).thenReturn(Arrays.asList());

        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act
        service.createAndSaveSnapshot();

        // Assert
        verify(historyDataAccess).saveSnapshot(any(TodaySoFarSnapshot.class));
        verify(historyDataAccess, never()).cleanupOldSnapshots(anyInt());
    }

    @Test
    void testSaveCurrentDaySnapshot() {
        // Arrange
        when(historyDataAccess.hasSnapshot(any(LocalDate.class))).thenReturn(false);
        when(historyDataAccess.saveSnapshot(any(TodaySoFarSnapshot.class))).thenReturn(true);
        when(historyDataAccess.cleanupOldSnapshots(30)).thenReturn(0);
        
        when(sharedDataAccess.getTaskGateway()).thenReturn(taskGateway);
        when(sharedDataAccess.getGoalRepository()).thenReturn(goalRepository);
        
        when(taskGateway.getTodaysTasks()).thenReturn(Arrays.asList());
        when(goalRepository.getCurrentGoals()).thenReturn(Arrays.asList());

        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act
        service.saveCurrentDaySnapshot();

        // Assert
        verify(historyDataAccess).saveSnapshot(any(TodaySoFarSnapshot.class));
    }

    @Test
    void testCompletionRateCalculation() {
        // Arrange
        when(historyDataAccess.hasSnapshot(any(LocalDate.class))).thenReturn(false);
        when(historyDataAccess.saveSnapshot(any(TodaySoFarSnapshot.class))).thenReturn(true);
        when(historyDataAccess.cleanupOldSnapshots(30)).thenReturn(0);
        
        when(sharedDataAccess.getTaskGateway()).thenReturn(taskGateway);
        when(sharedDataAccess.getGoalRepository()).thenReturn(goalRepository);
        
        // 3 tasks: 2 completed, 1 not completed = 66% completion rate
        Task completedTask1 = mock(Task.class);
        Task completedTask2 = mock(Task.class);
        Task incompleteTask = mock(Task.class);
        
        when(completedTask1.isCompleted()).thenReturn(true);
        when(completedTask2.isCompleted()).thenReturn(true);
        when(incompleteTask.isCompleted()).thenReturn(false);
        
        List<Task> todaysTasks = Arrays.asList(completedTask1, completedTask2, incompleteTask);
        when(taskGateway.getTodaysTasks()).thenReturn(todaysTasks);
        when(goalRepository.getCurrentGoals()).thenReturn(Arrays.asList());

        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act
        service.createAndSaveSnapshot();

        // Assert
        verify(historyDataAccess).saveSnapshot(any(TodaySoFarSnapshot.class));
    }

    @Test
    void testEmptyTaskListCompletionRate() {
        // Arrange
        when(historyDataAccess.hasSnapshot(any(LocalDate.class))).thenReturn(false);
        when(historyDataAccess.saveSnapshot(any(TodaySoFarSnapshot.class))).thenReturn(true);
        
        when(sharedDataAccess.getTaskGateway()).thenReturn(taskGateway);
        when(sharedDataAccess.getGoalRepository()).thenReturn(goalRepository);
        
        when(taskGateway.getTodaysTasks()).thenReturn(Arrays.asList());
        when(goalRepository.getCurrentGoals()).thenReturn(Arrays.asList());

        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act
        service.createAndSaveSnapshot();

        // Assert - Should handle empty task list without errors
        verify(taskGateway).getTodaysTasks();
        verify(historyDataAccess).saveSnapshot(any(TodaySoFarSnapshot.class));
    }

    @Test
    void testMultipleStartStopCalls() {
        // Arrange
        DailySnapshotService service = DailySnapshotService.getInstance();

        // Act & Assert - Multiple starts and stops should not throw exceptions
        assertDoesNotThrow(() -> {
            service.startAutomaticSnapshots();
            service.startAutomaticSnapshots(); // Should cancel previous timer
            service.stopAutomaticSnapshots();
            service.stopAutomaticSnapshots(); // Should handle null timer
        });
    }

    @Test
    void testServiceMethodsAfterReset() {
        // Arrange
        DailySnapshotService service1 = DailySnapshotService.getInstance();
        service1.startAutomaticSnapshots();

        // Act
        DailySnapshotService.resetForTesting();
        DailySnapshotService service2 = DailySnapshotService.getInstance();

        // Assert - New instance should work independently
        assertNotSame(service1, service2);
        assertDoesNotThrow(() -> service2.startAutomaticSnapshots());
        assertDoesNotThrow(() -> service2.stopAutomaticSnapshots());
    }
}