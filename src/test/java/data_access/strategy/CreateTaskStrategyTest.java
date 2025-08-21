package data_access.strategy;

import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableFactory;
import entity.Angela.Task.TaskAvailableInterf;
import entity.info.Info;
import entity.info.InfoFactory;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for CreateTaskStrategy.
 * Tests task creation operations following Clean Architecture principles.
 */
class CreateTaskStrategyTest {

    private CreateTaskStrategy strategy;
    private Map<String, TaskAvailable> availableTaskTemplates;
    private InfoFactory infoFactory;
    private TaskAvailableFactory taskAvailableFactory;

    @BeforeEach
    void setUp() {
        availableTaskTemplates = new HashMap<>();
        strategy = new CreateTaskStrategy(availableTaskTemplates);
        infoFactory = new InfoFactory();
        taskAvailableFactory = new TaskAvailableFactory();
    }

    @Test
    @DisplayName("Should save task available and return task ID")
    void testSaveTaskAvailable() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When
        String taskId = strategy.saveTaskAvailable(taskAvailable);

        // Then
        assertNotNull(taskId);
        assertEquals(taskAvailable.getId(), taskId);
        assertTrue(availableTaskTemplates.containsKey(taskId));
        assertEquals(taskAvailable, availableTaskTemplates.get(taskId));
    }

    @Test
    @DisplayName("Should save multiple tasks with unique IDs")
    void testSaveMultipleTasksAvailable() {
        // Given
        InfoInterf info1 = infoFactory.create("Task 1", "Desc 1", "cat1");
        InfoInterf info2 = infoFactory.create("Task 2", "Desc 2", "cat2");
        TaskAvailable task1 = (TaskAvailable) taskAvailableFactory.create(info1, false);
        TaskAvailable task2 = (TaskAvailable) taskAvailableFactory.create(info2, false);

        // When
        String taskId1 = strategy.saveTaskAvailable(task1);
        String taskId2 = strategy.saveTaskAvailable(task2);

        // Then
        assertNotEquals(taskId1, taskId2);
        assertEquals(2, availableTaskTemplates.size());
        assertTrue(availableTaskTemplates.containsKey(taskId1));
        assertTrue(availableTaskTemplates.containsKey(taskId2));
    }

    @Test
    @DisplayName("Should detect existing task with same name and category")
    void testTaskExistsWithNameAndCategory() {
        // Given
        InfoInterf info = infoFactory.create("Duplicate Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        strategy.saveTaskAvailable(taskAvailable);

        // When & Then
        assertTrue(strategy.taskExistsWithNameAndCategory("Duplicate Task", "cat1"));
        assertTrue(strategy.taskExistsWithNameAndCategory("duplicate task", "cat1")); // Case insensitive
        assertFalse(strategy.taskExistsWithNameAndCategory("Duplicate Task", "cat2"));
        assertFalse(strategy.taskExistsWithNameAndCategory("Different Task", "cat1"));
    }

    @Test
    @DisplayName("Should handle null name in exists check")
    void testTaskExistsWithNullName() {
        // Given
        InfoInterf info = infoFactory.create("Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        strategy.saveTaskAvailable(taskAvailable);

        // When & Then
        assertFalse(strategy.taskExistsWithNameAndCategory(null, "cat1"));
    }

    @Test
    @DisplayName("Should handle null and empty categories correctly")
    void testTaskExistsWithNullAndEmptyCategories() {
        // Given - task with null category
        InfoInterf info1 = infoFactory.create("Task 1", "Description", null);
        TaskAvailable task1 = (TaskAvailable) taskAvailableFactory.create(info1, false);
        strategy.saveTaskAvailable(task1);

        // Given - task with empty category
        InfoInterf info2 = infoFactory.create("Task 2", "Description", "");
        TaskAvailable task2 = (TaskAvailable) taskAvailableFactory.create(info2, false);
        strategy.saveTaskAvailable(task2);

        // When & Then
        assertTrue(strategy.taskExistsWithNameAndCategory("Task 1", null));
        assertTrue(strategy.taskExistsWithNameAndCategory("Task 1", ""));
        assertTrue(strategy.taskExistsWithNameAndCategory("Task 2", null));
        assertTrue(strategy.taskExistsWithNameAndCategory("Task 2", ""));
        
        // Should not match tasks with actual categories
        assertFalse(strategy.taskExistsWithNameAndCategory("Task 1", "cat1"));
        assertFalse(strategy.taskExistsWithNameAndCategory("Task 2", "cat1"));
    }

    @Test
    @DisplayName("Should handle whitespace categories correctly")
    void testTaskExistsWithWhitespaceCategories() {
        // Given - task with whitespace-only category
        InfoInterf info = infoFactory.create("Task", "Description", "   ");
        TaskAvailable task = (TaskAvailable) taskAvailableFactory.create(info, false);
        strategy.saveTaskAvailable(task);

        // When & Then
        assertTrue(strategy.taskExistsWithNameAndCategory("Task", "   "));
        assertTrue(strategy.taskExistsWithNameAndCategory("Task", ""));
        assertTrue(strategy.taskExistsWithNameAndCategory("Task", null));
    }

    @Test
    @DisplayName("Should return all available task templates")
    void testGetAllAvailableTaskTemplates() {
        // Given
        InfoInterf info1 = infoFactory.create("Task 1", "Desc 1", "cat1");
        InfoInterf info2 = infoFactory.create("Task 2", "Desc 2", "cat2");
        TaskAvailable task1 = (TaskAvailable) taskAvailableFactory.create(info1, false);
        TaskAvailable task2 = (TaskAvailable) taskAvailableFactory.create(info2, false);

        strategy.saveTaskAvailable(task1);
        strategy.saveTaskAvailable(task2);

        // When
        List<TaskAvailableInterf> tasks = strategy.getAllAvailableTaskTemplates();

        // Then
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    @DisplayName("Should return empty list when no tasks available")
    void testGetAllAvailableTaskTemplatesEmpty() {
        // When
        List<TaskAvailableInterf> tasks = strategy.getAllAvailableTaskTemplates();

        // Then
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("Should get task available by ID")
    void testGetTaskAvailableById() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        String taskId = strategy.saveTaskAvailable(taskAvailable);

        // When
        TaskAvailableInterf retrieved = strategy.getTaskAvailableById(taskId);

        // Then
        assertNotNull(retrieved);
        assertEquals(taskAvailable, retrieved);
        assertEquals("Test Task", retrieved.getInfo().getName());
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
    @DisplayName("Should return correct available task count")
    void testGetAvailableTaskCount() {
        // Given
        assertEquals(0, strategy.getAvailableTaskCount());

        InfoInterf info1 = infoFactory.create("Task 1", "Desc 1", "cat1");
        TaskAvailable task1 = (TaskAvailable) taskAvailableFactory.create(info1, false);
        strategy.saveTaskAvailable(task1);
        assertEquals(1, strategy.getAvailableTaskCount());

        InfoInterf info2 = infoFactory.create("Task 2", "Desc 2", "cat2");
        TaskAvailable task2 = (TaskAvailable) taskAvailableFactory.create(info2, false);
        strategy.saveTaskAvailable(task2);
        assertEquals(2, strategy.getAvailableTaskCount());
    }

    @Test
    @DisplayName("Should check if task exists")
    void testExists() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);

        // When & Then
        assertFalse(strategy.exists(taskAvailable));

        strategy.saveTaskAvailable(taskAvailable);
        assertTrue(strategy.exists(taskAvailable));
    }

    @Test
    @DisplayName("Should handle case-insensitive name matching variations")
    void testCaseInsensitiveNameMatchingVariations() {
        // Given
        InfoInterf info = infoFactory.create("TEST task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        availableTaskTemplates.put(taskAvailable.getId(), taskAvailable);

        // When & Then - case insensitive matching
        assertTrue(strategy.taskExistsWithNameAndCategory("test task", "cat1"));
        assertTrue(strategy.taskExistsWithNameAndCategory("TEST TASK", "cat1"));
        assertTrue(strategy.taskExistsWithNameAndCategory("Test Task", "cat1"));
        assertFalse(strategy.taskExistsWithNameAndCategory("test task", "cat2"));
    }

    @Test
    @DisplayName("Should handle case insensitive name matching")
    void testCaseInsensitiveNameMatching() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "cat1");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        strategy.saveTaskAvailable(taskAvailable);

        // When & Then
        assertTrue(strategy.taskExistsWithNameAndCategory("Test Task", "cat1"));
        assertTrue(strategy.taskExistsWithNameAndCategory("test task", "cat1"));
        assertTrue(strategy.taskExistsWithNameAndCategory("TEST TASK", "cat1"));
        assertTrue(strategy.taskExistsWithNameAndCategory("TeSt TaSk", "cat1"));
    }

    @Test
    @DisplayName("Should handle case insensitive category matching")
    void testCaseInsensitiveCategoryMatching() {
        // Given
        InfoInterf info = infoFactory.create("Test Task", "Description", "Work");
        TaskAvailable taskAvailable = (TaskAvailable) taskAvailableFactory.create(info, false);
        strategy.saveTaskAvailable(taskAvailable);

        // When & Then
        assertTrue(strategy.taskExistsWithNameAndCategory("Test Task", "Work"));
        assertTrue(strategy.taskExistsWithNameAndCategory("Test Task", "work"));
        assertTrue(strategy.taskExistsWithNameAndCategory("Test Task", "WORK"));
        assertTrue(strategy.taskExistsWithNameAndCategory("Test Task", "WoRk"));
    }

    @Test
    @DisplayName("Should overwrite task with same ID")
    void testOverwriteTaskWithSameId() {
        // Given
        InfoInterf info1 = infoFactory.create("Original Task", "Description", "cat1");
        TaskAvailable task1 = (TaskAvailable) taskAvailableFactory.create(info1, false);
        String taskId = strategy.saveTaskAvailable(task1);

        // When - manually put another task with the same ID
        InfoInterf info2 = infoFactory.create("Updated Task", "New Description", "cat2");
        TaskAvailable task2 = new TaskAvailable(taskId, (entity.info.Info) info2, null, false);
        availableTaskTemplates.put(taskId, task2);

        // Then
        assertEquals(1, strategy.getAvailableTaskCount());
        TaskAvailableInterf retrieved = strategy.getTaskAvailableById(taskId);
        assertEquals("Updated Task", retrieved.getInfo().getName());
    }
}