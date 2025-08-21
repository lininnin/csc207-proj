package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskFactory;
import entity.Angela.Task.TaskAvailableFactory;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import entity.info.Info;
import entity.info.InfoFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.alex.Event.EventInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Sophia.GoalInterface;
import use_case.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for CompositeTaskRepository.
 * Tests the composite pattern implementation and delegation to underlying repositories.
 */
class CompositeTaskRepositoryTest {

    @Mock
    private AvailableTaskRepository availableTaskRepository;
    
    @Mock
    private TodayTaskRepository todayTaskRepository;
    
    @Mock
    private GoalRepository goalRepository;

    private CompositeTaskRepository compositeRepository;
    private TaskFactory taskFactory;
    private TaskAvailableFactory taskAvailableFactory;
    private InfoFactory infoFactory;
    private BeginAndDueDatesFactory datesFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        compositeRepository = new CompositeTaskRepository(availableTaskRepository, todayTaskRepository);
        
        taskFactory = new TaskFactory();
        taskAvailableFactory = new TaskAvailableFactory();
        infoFactory = new InfoFactory();
        datesFactory = new BeginAndDueDatesFactory();
    }

    // ===== Constructor and Dependency Injection Tests =====

    @Test
    @DisplayName("Should initialize with required repositories")
    void testConstructorInitialization() {
        // Given & When
        CompositeTaskRepository repository = new CompositeTaskRepository(
            availableTaskRepository, todayTaskRepository
        );

        // Then
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Should set goal repository as optional dependency")
    void testSetGoalRepository() {
        // When
        compositeRepository.setGoalRepository(goalRepository);

        // Then - no exception should be thrown
        assertDoesNotThrow(() -> {
            compositeRepository.setGoalRepository(goalRepository);
        });
    }

    // ===== DeleteTaskDataAccessInterface Tests =====

    @Test
    @DisplayName("Should delegate getTaskAvailableById to available repository")
    void testGetTaskAvailableById() {
        // Given
        String taskId = "task123";
        TaskAvailable expectedTask = createTaskAvailable("Test Task", "cat1");
        when(availableTaskRepository.findById(taskId)).thenReturn(expectedTask);

        // When
        TaskAvailableInterf result = compositeRepository.getTaskAvailableById(taskId);

        // Then
        assertEquals(expectedTask, result);
        verify(availableTaskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should delegate getTodaysTaskById to today repository")
    void testGetTodaysTaskById() {
        // Given
        String taskId = "todayTask123";
        Task expectedTask = createTask("Today Task", "template1");
        when(todayTaskRepository.findById(taskId)).thenReturn(expectedTask);

        // When
        TaskInterf result = compositeRepository.getTodaysTaskById(taskId);

        // Then
        assertEquals(expectedTask, result);
        verify(todayTaskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should check if task exists in available repository")
    void testExistsInAvailable() {
        // Given
        TaskAvailable taskAvailable = createTaskAvailable("Test Task", "cat1");
        when(availableTaskRepository.exists(taskAvailable)).thenReturn(true);

        // When
        boolean exists = compositeRepository.existsInAvailable(taskAvailable);

        // Then
        assertTrue(exists);
        verify(availableTaskRepository).exists(taskAvailable);
    }

    @Test
    @DisplayName("Should check if task exists in today repository")
    void testExistsInToday() {
        // Given
        Task todayTask = createTask("Today Task", "template1");
        when(todayTaskRepository.exists(todayTask)).thenReturn(true);

        // When
        boolean exists = compositeRepository.existsInToday(todayTask);

        // Then
        assertTrue(exists);
        verify(todayTaskRepository).exists(todayTask);
    }

    @Test
    @DisplayName("Should check if template exists in today repository")
    void testTemplateExistsInToday() {
        // Given
        String templateTaskId = "template123";
        when(todayTaskRepository.existsByTemplate(templateTaskId)).thenReturn(true);

        // When
        boolean exists = compositeRepository.templateExistsInToday(templateTaskId);

        // Then
        assertTrue(exists);
        verify(todayTaskRepository).existsByTemplate(templateTaskId);
    }

    @Test
    @DisplayName("Should delete task from available repository")
    void testDeleteFromAvailable() {
        // Given
        TaskAvailable taskAvailable = createTaskAvailable("Test Task", "cat1");
        when(availableTaskRepository.delete(taskAvailable)).thenReturn(true);

        // When
        boolean deleted = compositeRepository.deleteFromAvailable(taskAvailable);

        // Then
        assertTrue(deleted);
        verify(availableTaskRepository).delete(taskAvailable);
    }

    @Test
    @DisplayName("Should delete all today's tasks with template")
    void testDeleteAllTodaysTasksWithTemplate() {
        // Given
        String templateTaskId = "template123";
        when(todayTaskRepository.deleteByTemplate(templateTaskId)).thenReturn(true);

        // When
        boolean deleted = compositeRepository.deleteAllTodaysTasksWithTemplate(templateTaskId);

        // Then
        assertTrue(deleted);
        verify(todayTaskRepository).deleteByTemplate(templateTaskId);
    }

    @Test
    @DisplayName("Should delete task completely from both repositories")
    void testDeleteTaskCompletely() {
        // Given
        String templateTaskId = "template123";
        when(todayTaskRepository.deleteByTemplate(templateTaskId)).thenReturn(true);
        when(availableTaskRepository.deleteById(templateTaskId)).thenReturn(true);

        // When
        boolean deleted = compositeRepository.deleteTaskCompletely(templateTaskId);

        // Then
        assertTrue(deleted);
        verify(todayTaskRepository).deleteByTemplate(templateTaskId);
        verify(availableTaskRepository).deleteById(templateTaskId);
    }

    @Test
    @DisplayName("Should get today's tasks by template")
    void testGetTodaysTasksByTemplate() {
        // Given
        String templateTaskId = "template123";
        Task task1 = createTask("Task 1", templateTaskId);
        Task task2 = createTask("Task 2", templateTaskId);
        List<Task> expectedTasks = Arrays.asList(task1, task2);
        when(todayTaskRepository.findByTemplate(templateTaskId)).thenReturn(expectedTasks);

        // When
        List<TaskInterf> result = compositeRepository.getTodaysTasksByTemplate(templateTaskId);

        // Then
        assertEquals(2, result.size());
        verify(todayTaskRepository).findByTemplate(templateTaskId);
    }

    @Test
    @DisplayName("Should get all today's tasks")
    void testGetAllTodaysTasks() {
        // Given
        Task task1 = createTask("Task 1", "template1");
        Task task2 = createTask("Task 2", "template2");
        List<Task> expectedTasks = Arrays.asList(task1, task2);
        when(todayTaskRepository.findAll()).thenReturn(expectedTasks);

        // When
        List<TaskInterf> result = compositeRepository.getAllTodaysTasks();

        // Then
        assertEquals(2, result.size());
        verify(todayTaskRepository).findAll();
    }

    @Test
    @DisplayName("Should get goal names targeting task when goal repository is set")
    void testGetGoalNamesTargetingTask() {
        // Given
        String taskId = "task123";
        compositeRepository.setGoalRepository(goalRepository);
        
        @SuppressWarnings("unchecked")
        List<GoalInterface> mockGoals = mock(List.class);
        when(goalRepository.findGoalsTargetingTask(taskId)).thenReturn(mockGoals);
        when(mockGoals.stream()).thenReturn(Collections.emptyList().stream());

        // When
        List<String> result = compositeRepository.getGoalNamesTargetingTask(taskId);

        // Then
        assertNotNull(result);
        verify(goalRepository).findGoalsTargetingTask(taskId);
    }

    @Test
    @DisplayName("Should return empty list when goal repository is null")
    void testGetGoalNamesTargetingTaskNoGoalRepository() {
        // Given
        String taskId = "task123";
        // goalRepository is not set (remains null)

        // When
        List<String> result = compositeRepository.getGoalNamesTargetingTask(taskId);

        // Then
        assertTrue(result.isEmpty());
        verifyNoInteractions(goalRepository);
    }

    // ===== TodaySoFarDataAccessInterface Tests =====

    @Test
    @DisplayName("Should get today's task summary")
    void testGetTodaysTaskSummary() {
        // Given
        Task task1 = createTask("Task 1", "template1");
        Task task2 = createTask("Task 2", "template2");
        task2.markComplete(); // Make one task completed
        
        List<Task> todaysTasks = Arrays.asList(task1, task2);
        when(todayTaskRepository.findAll()).thenReturn(todaysTasks);

        // When
        List<TaskInterf> result = compositeRepository.getTodaysTaskSummary();

        // Then
        assertEquals(2, result.size());
        verify(todayTaskRepository).findAll();
    }

    @Test
    @DisplayName("Should get today's events")
    void testGetTodaysEvents() {
        // Given
        @SuppressWarnings("unchecked")
        List<EventInterf> mockEvents = mock(List.class);
        when(todayTaskRepository.getTodaysEvents()).thenReturn(mockEvents);

        // When
        List<EventInterf> result = compositeRepository.getTodaysEvents();

        // Then
        assertEquals(mockEvents, result);
        verify(todayTaskRepository).getTodaysEvents();
    }

    @Test
    @DisplayName("Should get today's wellness logs")
    void testGetTodaysWellnessLogs() {
        // Given
        @SuppressWarnings("unchecked")
        List<WellnessLogEntryInterf> mockWellnessLogs = mock(List.class);
        when(todayTaskRepository.getTodaysWellnessLogs()).thenReturn(mockWellnessLogs);

        // When
        List<WellnessLogEntryInterf> result = compositeRepository.getTodaysWellnessLogs();

        // Then
        assertEquals(mockWellnessLogs, result);
        verify(todayTaskRepository).getTodaysWellnessLogs();
    }

    @Test
    @DisplayName("Should get today's goals")
    void testGetTodaysGoals() {
        // Given
        @SuppressWarnings("unchecked")
        List<GoalInterface> mockGoals = mock(List.class);
        when(todayTaskRepository.getTodaysGoals()).thenReturn(mockGoals);

        // When
        List<GoalInterface> result = compositeRepository.getTodaysGoals();

        // Then
        assertEquals(mockGoals, result);
        verify(todayTaskRepository).getTodaysGoals();
    }

    // ===== Edge Cases and Error Handling Tests =====

    @Test
    @DisplayName("Should handle null returns from repositories gracefully")
    void testNullReturns() {
        // Given
        when(availableTaskRepository.findById("nonexistent")).thenReturn(null);
        when(todayTaskRepository.findById("nonexistent")).thenReturn(null);

        // When & Then
        assertNull(compositeRepository.getTaskAvailableById("nonexistent"));
        assertNull(compositeRepository.getTodaysTaskById("nonexistent"));
    }

    @Test
    @DisplayName("Should handle empty lists from repositories")
    void testEmptyLists() {
        // Given
        when(todayTaskRepository.findAll()).thenReturn(Collections.emptyList());
        when(todayTaskRepository.findByTemplate("template")).thenReturn(Collections.emptyList());

        // When
        List<TaskInterf> allTasks = compositeRepository.getAllTodaysTasks();
        List<TaskInterf> templateTasks = compositeRepository.getTodaysTasksByTemplate("template");

        // Then
        assertTrue(allTasks.isEmpty());
        assertTrue(templateTasks.isEmpty());
    }

    @Test
    @DisplayName("Should handle deletion failures gracefully")
    void testDeletionFailures() {
        // Given
        TaskAvailable taskAvailable = createTaskAvailable("Test Task", "cat1");
        when(availableTaskRepository.delete(taskAvailable)).thenReturn(false);
        when(todayTaskRepository.deleteByTemplate("template")).thenReturn(false);

        // When
        boolean availableDeleted = compositeRepository.deleteFromAvailable(taskAvailable);
        boolean todayDeleted = compositeRepository.deleteAllTodaysTasksWithTemplate("template");

        // Then
        assertFalse(availableDeleted);
        assertFalse(todayDeleted);
    }

    // ===== Helper Methods =====

    private TaskAvailable createTaskAvailable(String name, String categoryId) {
        Info info = infoFactory.create(name, "Description for " + name, categoryId);
        BeginAndDueDates dates = datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(7));
        return taskAvailableFactory.create(info, dates, false);
    }

    private Task createTask(String name, String templateTaskId) {
        Info info = infoFactory.create(name, "Description for " + name, "cat1");
        return taskFactory.create(info, templateTaskId, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));
    }
}