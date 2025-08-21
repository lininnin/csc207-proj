package data_access.strategy;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableFactory;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import entity.info.InfoFactory;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AddToTodayStrategy.
 * Tests add to today operations following Clean Architecture principles.
 */
class AddToTodayStrategyTest {

    private AddToTodayStrategy strategy;
    private Map<String, TaskAvailable> availableTaskTemplates;
    private Map<String, Task> todaysTasks;
    private InfoFactory infoFactory;
    private TaskAvailableFactory taskAvailableFactory;

    @BeforeEach
    void setUp() {
        availableTaskTemplates = new HashMap<>();
        todaysTasks = new HashMap<>();
        strategy = new AddToTodayStrategy(availableTaskTemplates, todaysTasks);
        infoFactory = new InfoFactory();
        taskAvailableFactory = new TaskAvailableFactory();
    }

    @Test
    @DisplayName("Should get available task by ID")
    void testGetAvailableTaskById() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        availableTaskTemplates.put(taskAvailable.getId(), taskAvailable);

        // When
        TaskAvailableInterf retrieved = strategy.getAvailableTaskById(taskAvailable.getId());

        // Then
        assertNotNull(retrieved);
        assertEquals(taskAvailable, retrieved);
        assertEquals("Test Task", retrieved.getInfo().getName());
    }

    @Test
    @DisplayName("Should return null for non-existent task ID")
    void testGetAvailableTaskByIdNonExistent() {
        // When
        TaskAvailableInterf retrieved = strategy.getAvailableTaskById("non-existent-id");

        // Then
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Should add task to today with priority and due date")
    void testAddTaskToToday() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        availableTaskTemplates.put(taskAvailable.getId(), taskAvailable);
        LocalDate dueDate = LocalDate.now().plusDays(3);

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, Task.Priority.HIGH, dueDate);

        // Then
        assertNotNull(todayTask);
        assertEquals("Test Task", todayTask.getInfo().getName());
        assertEquals("Description", todayTask.getInfo().getDescription());
        assertEquals("cat1", todayTask.getInfo().getCategory());
        assertEquals(Task.Priority.HIGH, ((Task) todayTask).getPriority());
        assertEquals(dueDate, ((Task) todayTask).getDates().getDueDate());
        assertEquals(taskAvailable.getId(), ((Task) todayTask).getTemplateTaskId());
        
        // Verify it's stored in today's tasks
        assertTrue(todaysTasks.containsKey(todayTask.getId()));
        assertEquals(todayTask, todaysTasks.get(todayTask.getId()));
    }

    @Test
    @DisplayName("Should add task to today with null priority")
    void testAddTaskToTodayWithNullPriority() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        LocalDate dueDate = LocalDate.now().plusDays(1);

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, null, dueDate);

        // Then
        assertNotNull(todayTask);
        assertNull(((Task) todayTask).getPriority());
        assertEquals(dueDate, ((Task) todayTask).getDates().getDueDate());
    }

    @Test
    @DisplayName("Should handle overdue task creation")
    void testAddOverdueTaskToToday() {
        // Given
        InfoInterf info = infoFactory.create("Overdue Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        LocalDate pastDueDate = LocalDate.now().minusDays(2);

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, Task.Priority.HIGH, pastDueDate);

        // Then
        assertNotNull(todayTask);
        assertEquals(pastDueDate, ((Task) todayTask).getDates().getDueDate());
        // Begin date should be set to due date for overdue tasks
        assertEquals(pastDueDate, ((Task) todayTask).getDates().getBeginDate());
        assertTrue(((Task) todayTask).isOverdue());
    }

    @Test
    @DisplayName("Should handle task with today's due date")
    void testAddTaskWithTodayDueDate() {
        // Given
        InfoInterf info = infoFactory.create("Today Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        LocalDate today = LocalDate.now();

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, today);

        // Then
        assertNotNull(todayTask);
        assertEquals(today, ((Task) todayTask).getDates().getDueDate());
        assertEquals(today, ((Task) todayTask).getDates().getBeginDate());
        assertFalse(((Task) todayTask).isOverdue());
    }

    @Test
    @DisplayName("Should check if task is in today's list")
    void testIsTaskInTodaysList() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        String templateId = taskAvailable.getId();

        // When & Then - initially not in today's list
        assertFalse(strategy.isTaskInTodaysList(templateId));

        // Add task to today
        strategy.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));

        // Now should be in today's list
        assertTrue(strategy.isTaskInTodaysList(templateId));
    }

    @Test
    @DisplayName("Should check if task is in today's list and not overdue")
    void testIsTaskInTodaysListAndNotOverdue() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        String templateId = taskAvailable.getId();

        // Add overdue task
        strategy.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().minusDays(1));
        assertFalse(strategy.isTaskInTodaysListAndNotOverdue(templateId));

        // Clear and add non-overdue task
        todaysTasks.clear();
        strategy.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().plusDays(1));
        assertTrue(strategy.isTaskInTodaysListAndNotOverdue(templateId));
    }

    @Test
    @DisplayName("Should detect exact duplicate in today's list")
    void testIsExactDuplicateInTodaysList() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        String templateId = taskAvailable.getId();
        LocalDate dueDate = LocalDate.now().plusDays(2);
        Task.Priority priority = Task.Priority.HIGH;

        strategy.addTaskToToday(taskAvailable, priority, dueDate);

        // When & Then
        assertTrue(strategy.isExactDuplicateInTodaysList(templateId, priority, dueDate));
        assertFalse(strategy.isExactDuplicateInTodaysList(templateId, Task.Priority.LOW, dueDate));
        assertFalse(strategy.isExactDuplicateInTodaysList(templateId, priority, LocalDate.now().plusDays(3)));
        assertFalse(strategy.isExactDuplicateInTodaysList(templateId, null, dueDate));
    }

    @Test
    @DisplayName("Should handle exact duplicate with null priority")
    void testIsExactDuplicateWithNullPriority() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        String templateId = taskAvailable.getId();
        LocalDate dueDate = LocalDate.now().plusDays(2);

        strategy.addTaskToToday(taskAvailable, null, dueDate);

        // When & Then
        assertTrue(strategy.isExactDuplicateInTodaysList(templateId, null, dueDate));
        assertFalse(strategy.isExactDuplicateInTodaysList(templateId, Task.Priority.HIGH, dueDate));
    }

    @Test
    @DisplayName("Should handle multiple tasks from same template")
    void testMultipleTasksFromSameTemplate() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        String templateId = taskAvailable.getId();

        // Add multiple instances of the same template
        TaskInterf task1 = strategy.addTaskToToday(taskAvailable, Task.Priority.HIGH, LocalDate.now().plusDays(1));
        TaskInterf task2 = strategy.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().plusDays(2));

        // When & Then
        assertTrue(strategy.isTaskInTodaysList(templateId));
        assertNotEquals(task1.getId(), task2.getId()); // Different task instances
        assertEquals(2, todaysTasks.size());
        
        // Both tasks reference the same template
        assertEquals(templateId, ((Task) task1).getTemplateTaskId());
        assertEquals(templateId, ((Task) task2).getTemplateTaskId());
    }

    @Test
    @DisplayName("Should handle null due date")
    void testAddTaskWithNullDueDate() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, null);

        // Then
        assertNotNull(todayTask);
        assertNull(((Task) todayTask).getDates().getDueDate());
        assertEquals(LocalDate.now(), ((Task) todayTask).getDates().getBeginDate());
    }

    @Test
    @DisplayName("Should create independent info objects for each task")
    void testInfoIndependence() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When
        TaskInterf task1 = strategy.addTaskToToday(taskAvailable, Task.Priority.HIGH, LocalDate.now().plusDays(1));
        TaskInterf task2 = strategy.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().plusDays(2));

        // Then
        assertNotSame(task1.getInfo(), task2.getInfo()); // Different info objects
        assertNotEquals(task1.getInfo().getId(), task2.getInfo().getId()); // Different IDs
        assertEquals(task1.getInfo().getName(), task2.getInfo().getName()); // Same name
        assertEquals(task1.getInfo().getDescription(), task2.getInfo().getDescription()); // Same description
        assertEquals(task1.getInfo().getCategory(), task2.getInfo().getCategory()); // Same category
    }

    @Test
    @DisplayName("Should handle edge case with future due date")
    void testAddTaskWithFutureDueDate() {
        // Given
        InfoInterf info = infoFactory.create("Future Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        LocalDate futureDueDate = LocalDate.now().plusDays(30);

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, Task.Priority.LOW, futureDueDate);

        // Then
        assertNotNull(todayTask);
        assertEquals(futureDueDate, ((Task) todayTask).getDates().getDueDate());
        assertEquals(LocalDate.now(), ((Task) todayTask).getDates().getBeginDate());
        assertFalse(((Task) todayTask).isOverdue());
    }

    @Test
    @DisplayName("Should handle task with empty category")
    void testAddTaskWithEmptyCategory() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(todayTask);
        // Empty category may be stored as null
        assertTrue(todayTask.getInfo().getCategory() == null || 
                  todayTask.getInfo().getCategory().equals(""));
    }

    @Test
    @DisplayName("Should handle task with null category")
    void testAddTaskWithNullCategory() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", null);
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When
        TaskInterf todayTask = strategy.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(todayTask);
        assertNull(todayTask.getInfo().getCategory());
    }

    @Test
    @DisplayName("Should check for task that doesn't exist in today's list")
    void testIsTaskInTodaysListNonExistent() {
        // When & Then
        assertFalse(strategy.isTaskInTodaysList("non-existent-template-id"));
        assertFalse(strategy.isTaskInTodaysListAndNotOverdue("non-existent-template-id"));
        assertFalse(strategy.isExactDuplicateInTodaysList("non-existent-template-id", Task.Priority.HIGH, LocalDate.now()));
    }
}