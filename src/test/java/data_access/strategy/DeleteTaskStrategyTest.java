package data_access.strategy;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableFactory;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskFactory;
import entity.Angela.Task.TaskInterf;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.info.Info;
import entity.info.InfoFactory;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DeleteTaskStrategy.
 * Tests task deletion operations following Clean Architecture principles.
 */
class DeleteTaskStrategyTest {

    private DeleteTaskStrategy strategy;
    private Map<String, TaskAvailable> availableTaskTemplates;
    private Map<String, Task> todaysTasks;
    private Map<String, Info> availableTasks;
    private InfoFactory infoFactory;
    private TaskAvailableFactory taskAvailableFactory;
    private TaskFactory taskFactory;
    private BeginAndDueDatesFactory datesFactory;

    @BeforeEach
    void setUp() {
        availableTaskTemplates = new HashMap<>();
        todaysTasks = new HashMap<>();
        availableTasks = new HashMap<>();
        strategy = new DeleteTaskStrategy(availableTaskTemplates, todaysTasks, availableTasks);
        
        infoFactory = new InfoFactory();
        taskAvailableFactory = new TaskAvailableFactory();
        taskFactory = new TaskFactory();
        datesFactory = new BeginAndDueDatesFactory();
    }

    @Test
    @DisplayName("Should get available task by ID")
    void testGetTaskAvailableById() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        availableTaskTemplates.put(taskAvailable.getId(), taskAvailable);

        // When
        TaskAvailableInterf retrieved = strategy.getTaskAvailableById(taskAvailable.getId());

        // Then
        assertNotNull(retrieved);
        assertEquals(taskAvailable, retrieved);
    }

    @Test
    @DisplayName("Should return null for non-existent task ID")
    void testGetTaskAvailableByIdNonExistent() {
        // When
        TaskAvailableInterf retrieved = strategy.getTaskAvailableById("non-existent-id");

        // Then
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Should get all available task templates")
    void testGetAllAvailableTaskTemplates() {
        // Given
        InfoInterf info1 = infoFactory.create("Task 1", "Desc 1", "cat1");
        InfoInterf info2 = infoFactory.create("Task 2", "Desc 2", "cat2");
        TaskAvailable task1 = (TaskAvailable) taskAvailableFactory.create(info1, false);
        TaskAvailable task2 = (TaskAvailable) taskAvailableFactory.create(info2, false);
        
        availableTaskTemplates.put(task1.getId(), task1);
        availableTaskTemplates.put(task2.getId(), task2);

        // When
        List<TaskAvailableInterf> tasks = strategy.getAllAvailableTaskTemplates();

        // Then
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    @DisplayName("Should check if task exists in available")
    void testExistsInAvailable() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When & Then
        assertFalse(strategy.existsInAvailable(taskAvailable));

        availableTaskTemplates.put(taskAvailable.getId(), taskAvailable);
        assertTrue(strategy.existsInAvailable(taskAvailable));
    }

    @Test
    @DisplayName("Should check if task exists in today")
    void testExistsInToday() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        Task task = (Task) taskFactory.create("template-id", info, 
                                            datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);

        // When & Then
        assertFalse(strategy.existsInToday(task));

        todaysTasks.put(task.getId(), task);
        assertTrue(strategy.existsInToday(task));
    }

    @Test
    @DisplayName("Should check if template exists in today")
    void testTemplateExistsInToday() {
        // Given
        String templateId = "template-123";
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        Task task = (Task) taskFactory.create(templateId, info, 
                                            datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        todaysTasks.put(task.getId(), task);

        // When & Then
        assertTrue(strategy.templateExistsInToday(templateId));
        assertFalse(strategy.templateExistsInToday("non-existent-template"));
    }

    @Test
    @DisplayName("Should delete task from available")
    void testDeleteFromAvailable() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        availableTaskTemplates.put(taskAvailable.getId(), taskAvailable);
        
        assertTrue(strategy.existsInAvailable(taskAvailable));

        // When
        boolean deleted = strategy.deleteFromAvailable(taskAvailable);

        // Then
        assertTrue(deleted);
        assertFalse(strategy.existsInAvailable(taskAvailable));
        assertNull(strategy.getTaskAvailableById(taskAvailable.getId()));
    }

    @Test
    @DisplayName("Should return false when deleting non-existent task from available")
    void testDeleteFromAvailableNonExistent() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When
        boolean deleted = strategy.deleteFromAvailable(taskAvailable);

        // Then
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should delete all today's tasks with template")
    void testDeleteAllTodaysTasksWithTemplate() {
        // Given
        String templateId = "template-123";
        InfoInterf info1 = infoFactory.create("Task 1", "Desc 1", "cat1");
        InfoInterf info2 = infoFactory.create("Task 2", "Desc 2", "cat1");
        
        Task task1 = (Task) taskFactory.create(templateId, info1, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        Task task2 = (Task) taskFactory.create(templateId, info2, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(2)), false);
        Task task3 = (Task) taskFactory.create("different-template", info1, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        
        todaysTasks.put(task1.getId(), task1);
        todaysTasks.put(task2.getId(), task2);
        todaysTasks.put(task3.getId(), task3);

        assertEquals(3, todaysTasks.size());

        // When
        boolean deleted = strategy.deleteAllTodaysTasksWithTemplate(templateId);

        // Then
        assertTrue(deleted);
        assertEquals(1, todaysTasks.size()); // Only task3 should remain
        assertTrue(todaysTasks.containsValue(task3));
        assertFalse(todaysTasks.containsValue(task1));
        assertFalse(todaysTasks.containsValue(task2));
    }

    @Test
    @DisplayName("Should return false when deleting tasks with non-existent template")
    void testDeleteAllTodaysTasksWithNonExistentTemplate() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        Task task = (Task) taskFactory.create("template-123", info, 
                                            datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        todaysTasks.put(task.getId(), task);

        // When
        boolean deleted = strategy.deleteAllTodaysTasksWithTemplate("non-existent-template");

        // Then
        assertFalse(deleted);
        assertEquals(1, todaysTasks.size()); // Task should remain
    }

    @Test
    @DisplayName("Should delete task completely")
    void testDeleteTaskCompletely() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        String templateId = taskAvailable.getId();
        
        // Add to available templates
        availableTaskTemplates.put(templateId, taskAvailable);
        
        // Add to legacy available tasks
        availableTasks.put(templateId, (Info) info);
        
        // Add multiple today's tasks with this template
        Task task1 = (Task) taskFactory.create(templateId, info, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        Task task2 = (Task) taskFactory.create(templateId, info, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(2)), false);
        todaysTasks.put(task1.getId(), task1);
        todaysTasks.put(task2.getId(), task2);

        assertEquals(1, availableTaskTemplates.size());
        assertEquals(1, availableTasks.size());
        assertEquals(2, todaysTasks.size());

        // When
        boolean deleted = strategy.deleteTaskCompletely(templateId);

        // Then
        assertTrue(deleted);
        assertEquals(0, availableTaskTemplates.size());
        assertEquals(0, availableTasks.size());
        assertEquals(0, todaysTasks.size());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent task completely")
    void testDeleteTaskCompletelyNonExistent() {
        // When
        boolean deleted = strategy.deleteTaskCompletely("non-existent-template");

        // Then
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should get today's tasks by template")
    void testGetTodaysTasksByTemplate() {
        // Given
        String templateId = "template-123";
        InfoInterf info1 = infoFactory.create("Task 1", "Desc 1", "cat1");
        InfoInterf info2 = infoFactory.create("Task 2", "Desc 2", "cat1");
        
        Task task1 = (Task) taskFactory.create(templateId, info1, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        Task task2 = (Task) taskFactory.create(templateId, info2, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(2)), false);
        Task task3 = (Task) taskFactory.create("different-template", info1, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        
        todaysTasks.put(task1.getId(), task1);
        todaysTasks.put(task2.getId(), task2);
        todaysTasks.put(task3.getId(), task3);

        // When
        List<TaskInterf> templateTasks = strategy.getTodaysTasksByTemplate(templateId);

        // Then
        assertEquals(2, templateTasks.size());
        assertTrue(templateTasks.contains(task1));
        assertTrue(templateTasks.contains(task2));
        assertFalse(templateTasks.contains(task3));
    }

    @Test
    @DisplayName("Should return empty list for non-existent template")
    void testGetTodaysTasksByNonExistentTemplate() {
        // When
        List<TaskInterf> templateTasks = strategy.getTodaysTasksByTemplate("non-existent-template");

        // Then
        assertTrue(templateTasks.isEmpty());
    }

    @Test
    @DisplayName("Should get all today's tasks")
    void testGetAllTodaysTasks() {
        // Given
        InfoInterf info1 = infoFactory.create("Task 1", "Desc 1", "cat1");
        InfoInterf info2 = infoFactory.create("Task 2", "Desc 2", "cat2");
        
        Task task1 = (Task) taskFactory.create("template-1", info1, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        Task task2 = (Task) taskFactory.create("template-2", info2, 
                                             datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(2)), false);
        
        todaysTasks.put(task1.getId(), task1);
        todaysTasks.put(task2.getId(), task2);

        // When
        List<TaskInterf> allTasks = strategy.getAllTodaysTasks();

        // Then
        assertEquals(2, allTasks.size());
        assertTrue(allTasks.contains(task1));
        assertTrue(allTasks.contains(task2));
    }

    @Test
    @DisplayName("Should get today's task by ID")
    void testGetTodaysTaskById() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        Task task = (Task) taskFactory.create("template-123", info, 
                                            datesFactory.create(LocalDate.now(), LocalDate.now().plusDays(1)), false);
        todaysTasks.put(task.getId(), task);

        // When
        TaskInterf retrieved = strategy.getTodaysTaskById(task.getId());

        // Then
        assertNotNull(retrieved);
        assertEquals(task, retrieved);
    }

    @Test
    @DisplayName("Should return null for non-existent today's task ID")
    void testGetTodaysTaskByIdNonExistent() {
        // When
        TaskInterf retrieved = strategy.getTodaysTaskById("non-existent-id");

        // Then
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Should return empty list when no goal repository set")
    void testGetGoalNamesTargetingTaskWithoutRepository() {
        // When
        List<String> goalNames = strategy.getGoalNamesTargetingTask("task-123");

        // Then
        assertTrue(goalNames.isEmpty());
    }

    @Test
    @DisplayName("Should handle null task ID for goal names")
    void testGetGoalNamesTargetingNullTaskId() {
        // When
        List<String> goalNames = strategy.getGoalNamesTargetingTask(null);

        // Then
        assertTrue(goalNames.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty today's tasks list")
    void testOperationsOnEmptyTodaysTasks() {
        // When & Then
        assertTrue(strategy.getAllTodaysTasks().isEmpty());
        assertTrue(strategy.getTodaysTasksByTemplate("any-template").isEmpty());
        assertNull(strategy.getTodaysTaskById("any-id"));
        assertFalse(strategy.templateExistsInToday("any-template"));
        assertFalse(strategy.deleteAllTodaysTasksWithTemplate("any-template"));
    }
}