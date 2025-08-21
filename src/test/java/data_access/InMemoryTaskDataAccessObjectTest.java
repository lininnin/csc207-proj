package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableFactory;
import entity.Angela.Task.TaskFactory;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import entity.info.Info;
import entity.info.InfoInterf;
import entity.info.InfoFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for InMemoryTaskDataAccessObject following Clean Architecture testing principles.
 * Tests all interfaces implemented by the class and validates business rules.
 */
class InMemoryTaskDataAccessObjectTest {

    private InMemoryTaskDataAccessObject dataAccess;
    private InfoFactory infoFactory;
    private TaskAvailableFactory taskAvailableFactory;
    private TaskFactory taskFactory;
    private BeginAndDueDatesFactory datesFactory;

    @BeforeEach
    void setUp() {
        dataAccess = new InMemoryTaskDataAccessObject();
        infoFactory = new InfoFactory();
        taskAvailableFactory = new TaskAvailableFactory();
        taskFactory = new TaskFactory();
        datesFactory = new BeginAndDueDatesFactory();
    }

    // ===== CreateTaskDataAccessInterface Tests =====

    @Test
    @DisplayName("Should save task available and return task ID")
    void testSaveTaskAvailable() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When
        String taskId = dataAccess.saveTaskAvailable(taskAvailable);

        // Then
        assertNotNull(taskId);
        assertEquals(taskAvailable.getId(), taskId);
        
        TaskAvailableInterf retrieved = dataAccess.getTaskAvailableById(taskId);
        assertNotNull(retrieved);
        assertEquals("Test Task", retrieved.getInfo().getName());
    }

    @Test
    @DisplayName("Should detect existing task with same name and category")
    void testTaskExistsWithNameAndCategory() {
        // Given
        InfoInterf info = infoFactory.create("Duplicate Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        dataAccess.saveTaskAvailable(taskAvailable);

        // When & Then
        assertTrue(dataAccess.taskExistsWithNameAndCategory("Duplicate Task", "cat1"));
        assertTrue(dataAccess.taskExistsWithNameAndCategory("duplicate task", "cat1")); // Case insensitive
        assertFalse(dataAccess.taskExistsWithNameAndCategory("Duplicate Task", "cat2"));
        assertFalse(dataAccess.taskExistsWithNameAndCategory("Different Task", "cat1"));
    }

    @Test
    @DisplayName("Should return all available task templates")
    void testGetAllAvailableTaskTemplates() {
        // Given
        createAndSaveTaskAvailable("Task 1", "cat1");
        createAndSaveTaskAvailable("Task 2", "cat2");
        createAndSaveTaskAvailable("Task 3", "cat1");

        // When
        List<TaskAvailableInterf> tasks = dataAccess.getAllAvailableTaskTemplates();

        // Then
        assertEquals(3, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getInfo().getName().equals("Task 1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getInfo().getName().equals("Task 2")));
        assertTrue(tasks.stream().anyMatch(t -> t.getInfo().getName().equals("Task 3")));
    }

    @Test
    @DisplayName("Should return correct available task count")
    void testGetAvailableTaskCount() {
        // Given
        assertEquals(0, dataAccess.getAvailableTaskCount());
        
        createAndSaveTaskAvailable("Task 1", "cat1");
        assertEquals(1, dataAccess.getAvailableTaskCount());
        
        createAndSaveTaskAvailable("Task 2", "cat2");
        assertEquals(2, dataAccess.getAvailableTaskCount());
    }

    @Test
    @DisplayName("Should check if task exists")
    void testExists() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");

        // When & Then
        assertTrue(dataAccess.exists(taskAvailable));
        
        // Create different task that doesn't exist
        InfoInterf differentInfo = infoFactory.create("Different Task", "Description", "cat1");
        TaskAvailable differentTask = (TaskAvailable) taskAvailableFactory.create(differentInfo, false);
        assertFalse(dataAccess.exists(differentTask));
    }

    // ===== EditAvailableTaskDataAccessInterface Tests =====

    @Test
    @DisplayName("Should update available task successfully")
    void testUpdateAvailableTask() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Original Task", "cat1");
        String taskId = taskAvailable.getId();

        // When
        boolean updated = dataAccess.updateAvailableTask(taskId, "Updated Task", "New Description", "cat2", true);

        // Then
        assertTrue(updated);
        TaskAvailableInterf retrieved = dataAccess.getTaskAvailableById(taskId);
        assertEquals("Updated Task", retrieved.getInfo().getName());
        assertEquals("New Description", retrieved.getInfo().getDescription());
        assertEquals("cat2", retrieved.getInfo().getCategory());
        assertTrue(retrieved.isOneTime());
    }

    @Test
    @DisplayName("Should detect duplicate name when editing task")
    void testTaskExistsWithNameAndCategoryExcluding() {
        // Given
        TaskAvailable task1 = createAndSaveTaskAvailable("Task 1", "cat1");
        TaskAvailable task2 = createAndSaveTaskAvailable("Task 2", "cat1");

        // When & Then
        assertTrue(dataAccess.taskExistsWithNameAndCategoryExcluding("Task 1", "cat1", task2.getId()));
        assertFalse(dataAccess.taskExistsWithNameAndCategoryExcluding("Task 1", "cat1", task1.getId()));
        assertFalse(dataAccess.taskExistsWithNameAndCategoryExcluding("Task 1", "cat2", task2.getId()));
    }

    // ===== AddToTodayDataAccessInterface Tests =====

    @Test
    @DisplayName("Should add task to today's list with priority and due date")
    void testAddTaskToToday() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        LocalDate dueDate = LocalDate.now().plusDays(3);

        // When
        TaskInterf todayTask = dataAccess.addTaskToToday(taskAvailable, Task.Priority.HIGH, dueDate);

        // Then
        assertNotNull(todayTask);
        assertEquals("Test Task", todayTask.getInfo().getName());
        assertEquals(Task.Priority.HIGH, ((Task) todayTask).getPriority());
        assertEquals(dueDate, ((Task) todayTask).getDates().getDueDate());
        assertEquals(taskAvailable.getId(), ((Task) todayTask).getTemplateTaskId());
    }

    @Test
    @DisplayName("Should detect if task is in today's list")
    void testIsTaskInTodaysList() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        assertFalse(dataAccess.isTaskInTodaysList(taskAvailable.getId()));

        // When
        dataAccess.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));

        // Then
        assertTrue(dataAccess.isTaskInTodaysList(taskAvailable.getId()));
    }

    @Test
    @DisplayName("Should detect if task is in today's list and not overdue")
    void testIsTaskInTodaysListAndNotOverdue() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        
        // Add overdue task
        dataAccess.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().minusDays(1));
        assertFalse(dataAccess.isTaskInTodaysListAndNotOverdue(taskAvailable.getId()));
        
        // Clear and add non-overdue task
        dataAccess.clearAllData();
        createAndSaveTaskAvailable("Test Task", "cat1");
        dataAccess.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().plusDays(1));
        assertTrue(dataAccess.isTaskInTodaysListAndNotOverdue(taskAvailable.getId()));
    }

    @Test
    @DisplayName("Should detect exact duplicate in today's list")
    void testIsExactDuplicateInTodaysList() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        LocalDate dueDate = LocalDate.now().plusDays(2);
        Task.Priority priority = Task.Priority.HIGH;
        
        dataAccess.addTaskToToday(taskAvailable, priority, dueDate);

        // When & Then
        assertTrue(dataAccess.isExactDuplicateInTodaysList(taskAvailable.getId(), priority, dueDate));
        assertFalse(dataAccess.isExactDuplicateInTodaysList(taskAvailable.getId(), Task.Priority.LOW, dueDate));
        assertFalse(dataAccess.isExactDuplicateInTodaysList(taskAvailable.getId(), priority, LocalDate.now().plusDays(3)));
    }

    // ===== EditTodayTaskDataAccessInterface Tests =====

    @Test
    @DisplayName("Should update today task priority and due date")
    void testUpdateTodayTaskPriorityAndDueDate() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        TaskInterf todayTask = dataAccess.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        // When
        boolean updated = dataAccess.updateTodayTaskPriorityAndDueDate(taskId, Task.Priority.HIGH, LocalDate.now().plusDays(5));

        // Then
        assertTrue(updated);
        TaskInterf retrievedTask = dataAccess.getTodayTaskById(taskId);
        assertEquals(Task.Priority.HIGH, ((Task) retrievedTask).getPriority());
        assertEquals(LocalDate.now().plusDays(5), ((Task) retrievedTask).getDates().getDueDate());
    }

    @Test
    @DisplayName("Should validate due dates")
    void testIsValidDueDate() {
        assertTrue(dataAccess.isValidDueDate(LocalDate.now()));
        assertTrue(dataAccess.isValidDueDate(LocalDate.now().plusDays(1)));
        assertFalse(dataAccess.isValidDueDate(LocalDate.now().minusDays(1)));
    }

    // ===== MarkTaskCompleteDataAccessInterface Tests =====

    @Test
    @DisplayName("Should update task completion status")
    void testUpdateTaskCompletionStatus() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        TaskInterf todayTask = dataAccess.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        // When
        boolean updated = dataAccess.updateTaskCompletionStatus(taskId, true);

        // Then
        assertTrue(updated);
        TaskInterf retrievedTask = dataAccess.getTodayTaskById(taskId);
        assertTrue(((Task) retrievedTask).isCompleted());

        // Mark as incomplete
        dataAccess.updateTaskCompletionStatus(taskId, false);
        retrievedTask = dataAccess.getTodayTaskById(taskId);
        assertFalse(((Task) retrievedTask).isCompleted());
    }

    // ===== RemoveFromTodayDataAccessInterface Tests =====

    @Test
    @DisplayName("Should remove task from today's list")
    void testRemoveFromTodaysList() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        TaskInterf todayTask = dataAccess.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));
        String taskId = todayTask.getId();

        assertTrue(dataAccess.isTaskInTodaysList(taskAvailable.getId()));

        // When
        boolean removed = dataAccess.removeFromTodaysList(taskId);

        // Then
        assertTrue(removed);
        assertFalse(dataAccess.isTaskInTodaysList(taskAvailable.getId()));
        assertNull(dataAccess.getTodayTaskById(taskId));
    }

    // ===== OverdueTasksDataAccessInterface Tests =====

    @Test
    @DisplayName("Should return overdue tasks")
    void testGetOverdueTasks() {
        // Given
        TaskAvailable task1 = createAndSaveTaskAvailable("Overdue Task 1", "cat1");
        TaskAvailable task2 = createAndSaveTaskAvailable("Overdue Task 2", "cat1");
        TaskAvailable task3 = createAndSaveTaskAvailable("Future Task", "cat1");

        dataAccess.addTaskToToday(task1, Task.Priority.HIGH, LocalDate.now().minusDays(2));
        dataAccess.addTaskToToday(task2, Task.Priority.MEDIUM, LocalDate.now().minusDays(5));
        dataAccess.addTaskToToday(task3, Task.Priority.LOW, LocalDate.now().plusDays(1));

        // When
        List<TaskInterf> overdueTasks = dataAccess.getOverdueTasks(3);
        List<TaskInterf> allOverdueTasks = dataAccess.getAllOverdueTasks();

        // Then
        assertEquals(1, overdueTasks.size()); // Only task overdue within 3 days
        assertEquals(2, allOverdueTasks.size()); // Both overdue tasks
        assertTrue(overdueTasks.stream().anyMatch(t -> t.getInfo().getName().equals("Overdue Task 1")));
    }

    // ===== DeleteTaskDataAccessInterface Tests =====

    @Test
    @DisplayName("Should delete task from available list")
    void testDeleteFromAvailable() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        assertTrue(dataAccess.existsInAvailable(taskAvailable));

        // When
        boolean deleted = dataAccess.deleteFromAvailable(taskAvailable);

        // Then
        assertTrue(deleted);
        assertFalse(dataAccess.existsInAvailable(taskAvailable));
        assertNull(dataAccess.getTaskAvailableById(taskAvailable.getId()));
    }

    @Test
    @DisplayName("Should delete all today's tasks with template")
    void testDeleteAllTodaysTasksWithTemplate() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        dataAccess.addTaskToToday(taskAvailable, Task.Priority.HIGH, LocalDate.now().plusDays(1));
        dataAccess.addTaskToToday(taskAvailable, Task.Priority.LOW, LocalDate.now().plusDays(2));

        assertEquals(2, dataAccess.getTodaysTasksByTemplate(taskAvailable.getId()).size());

        // When
        boolean deleted = dataAccess.deleteAllTodaysTasksWithTemplate(taskAvailable.getId());

        // Then
        assertTrue(deleted);
        assertEquals(0, dataAccess.getTodaysTasksByTemplate(taskAvailable.getId()).size());
    }

    @Test
    @DisplayName("Should delete task completely")
    void testDeleteTaskCompletely() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        dataAccess.addTaskToToday(taskAvailable, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));

        // When
        boolean deleted = dataAccess.deleteTaskCompletely(taskAvailable.getId());

        // Then
        assertTrue(deleted);
        assertNull(dataAccess.getTaskAvailableById(taskAvailable.getId()));
        assertEquals(0, dataAccess.getTodaysTasksByTemplate(taskAvailable.getId()).size());
    }

    // ===== EditCategoryTaskDataAccessInterface Tests =====

    @Test
    @DisplayName("Should find tasks by category")
    void testFindTasksByCategory() {
        // Given
        createAndSaveTaskAvailable("Task 1", "cat1");
        createAndSaveTaskAvailable("Task 2", "cat1");
        createAndSaveTaskAvailable("Task 3", "cat2");

        // When
        List<TaskAvailable> cat1Tasks = dataAccess.findAvailableTasksByCategory("cat1");
        List<TaskAvailable> cat2Tasks = dataAccess.findAvailableTasksByCategory("cat2");

        // Then
        assertEquals(2, cat1Tasks.size());
        assertEquals(1, cat2Tasks.size());
    }

    @Test
    @DisplayName("Should update task category")
    void testUpdateTaskCategory() {
        // Given
        TaskAvailable taskAvailable = createAndSaveTaskAvailable("Test Task", "cat1");
        String taskId = taskAvailable.getId();

        // When
        boolean updated = dataAccess.updateAvailableTaskCategory(taskId, "cat2");

        // Then
        assertTrue(updated);
        TaskAvailableInterf retrievedTask = dataAccess.getTaskAvailableById(taskId);
        assertEquals("cat2", retrievedTask.getInfo().getCategory());
    }

    // ===== DeleteCategoryTaskDataAccessInterface Tests =====

    @Test
    @DisplayName("Should update tasks category to null when category is deleted")
    void testUpdateTasksCategoryToNull() {
        // Given
        TaskAvailable task1 = createAndSaveTaskAvailable("Task 1", "cat1");
        TaskAvailable task2 = createAndSaveTaskAvailable("Task 2", "cat1");
        TaskAvailable task3 = createAndSaveTaskAvailable("Task 3", "cat2");

        // When
        boolean updated = dataAccess.updateTasksCategoryToNull("cat1");

        // Then
        assertTrue(updated);
        
        List<TaskAvailable> emptyTasks = dataAccess.findAvailableTasksWithEmptyCategory();
        assertEquals(2, emptyTasks.size());
        
        // Verify cat2 task is unaffected
        TaskAvailableInterf task3Retrieved = dataAccess.getTaskAvailableById(task3.getId());
        assertEquals("cat2", task3Retrieved.getInfo().getCategory());
    }

    // ===== Legacy Methods Tests =====

    @Test
    @DisplayName("Should support legacy Info-based operations")
    void testLegacyMethods() {
        // Given
        InfoInterf info = infoFactory.create("Legacy Task", "Description", "cat1");

        // When
        String taskId = dataAccess.saveAvailableTask((Info) info);

        // Then
        assertNotNull(taskId);
        assertTrue(dataAccess.availableTaskNameExists("Legacy Task"));
        assertTrue(dataAccess.availableTaskNameExists("legacy task")); // Case insensitive

        // Test update
        info.setName("Updated Legacy Task");
        assertTrue(dataAccess.updateAvailableTask((Info) info));

        // Test delete
        assertTrue(dataAccess.deleteFromAvailable(taskId));
        assertFalse(dataAccess.availableTaskNameExists("Updated Legacy Task"));
    }

    @Test
    @DisplayName("Should get today's tasks sorted alphabetically")
    void testGetTodaysTasksSorted() {
        // Given
        TaskAvailable taskC = createAndSaveTaskAvailable("Charlie Task", "cat1");
        TaskAvailable taskA = createAndSaveTaskAvailable("Alpha Task", "cat1");
        TaskAvailable taskB = createAndSaveTaskAvailable("Beta Task", "cat1");

        dataAccess.addTaskToToday(taskC, Task.Priority.HIGH, LocalDate.now().plusDays(1));
        dataAccess.addTaskToToday(taskA, Task.Priority.MEDIUM, LocalDate.now().plusDays(1));
        dataAccess.addTaskToToday(taskB, Task.Priority.LOW, LocalDate.now().plusDays(1));

        // When
        List<Task> todaysTasks = dataAccess.getTodaysTasks();

        // Then
        assertEquals(3, todaysTasks.size());
        assertEquals("Alpha Task", todaysTasks.get(0).getInfo().getName());
        assertEquals("Beta Task", todaysTasks.get(1).getInfo().getName());
        assertEquals("Charlie Task", todaysTasks.get(2).getInfo().getName());
    }

    @Test
    @DisplayName("Should clear all data for testing")
    void testClearAllData() {
        // Given
        createAndSaveTaskAvailable("Test Task", "cat1");
        assertEquals(1, dataAccess.getAvailableTaskCount());

        // When
        dataAccess.clearAllData();

        // Then
        assertEquals(0, dataAccess.getAvailableTaskCount());
        assertTrue(dataAccess.getAllAvailableTaskTemplates().isEmpty());
        assertTrue(dataAccess.getTodaysTasks().isEmpty());
    }

    // ===== Helper Methods =====

    private TaskAvailable createAndSaveTaskAvailable(String name, String categoryId) {
        InfoInterf info = infoFactory.create(name, "Description for " + name, categoryId);
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        dataAccess.saveTaskAvailable(taskAvailable);
        return taskAvailable;
    }
}