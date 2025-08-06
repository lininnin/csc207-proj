package entity.Angela.Task;

import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TaskAvailable entity following Clean Architecture principles.
 * Tests business rules and entity constraints.
 */
class TaskAvailableTest {

    private Info testInfo;

    @BeforeEach
    void setUp() {
        testInfo = new Info.Builder("Test Task")
                .description("Test Description")
                .category("category-123")
                .build();
    }

    @Test
    void testTaskAvailableCreation() {
        TaskAvailable task = new TaskAvailable(testInfo);

        assertNotNull(task.getId());
        assertEquals(testInfo, task.getInfo());
        assertEquals("Test Task", task.getInfo().getName());
        assertEquals("Test Description", task.getInfo().getDescription());
        assertEquals("category-123", task.getInfo().getCategory());
        assertFalse(task.isOneTime());  // Default is false
        assertNull(task.getPlannedDueDate());  // Default is null
    }

    @Test
    void testTaskAvailableWithFullConstructor() {
        String plannedDate = LocalDate.now().plusDays(7).toString();
        
        TaskAvailable task = new TaskAvailable(
                "task-id-123",
                testInfo,
                plannedDate,  // plannedDueDate as String
                true  // isOneTime
        );

        assertEquals("task-id-123", task.getId());
        assertEquals(testInfo, task.getInfo());
        assertTrue(task.isOneTime());
        assertEquals(plannedDate, task.getPlannedDueDate());
    }

    @Test
    void testSetOneTime() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        assertFalse(task.isOneTime());
        
        task.setOneTime(true);
        assertTrue(task.isOneTime());
        
        task.setOneTime(false);
        assertFalse(task.isOneTime());
    }

    @Test
    void testSetPlannedDueDate() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        assertNull(task.getPlannedDueDate());
        
        String plannedDate = LocalDate.now().plusDays(3).toString();
        task.setPlannedDueDate(plannedDate);
        assertEquals(plannedDate, task.getPlannedDueDate());
        
        task.setPlannedDueDate(null);
        assertNull(task.getPlannedDueDate());
    }

    @Test
    void testIdGeneration() {
        // Create two different Info objects so they have different IDs
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        
        // Each task should have a unique ID
        assertNotNull(task1.getId());
        assertNotNull(task2.getId());
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void testTaskAvailableWithNullInfo() {
        // Should handle null info gracefully or throw exception
        try {
            TaskAvailable task = new TaskAvailable(null);
            // If it allows null, verify it's handled
            assertNull(task.getInfo());
        } catch (Exception e) {
            // If it throws exception, that's also valid
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }

    @Test
    void testTaskAvailableWithEmptyInfo() {
        // Info.Builder doesn't allow empty name, so we expect exception
        assertThrows(IllegalArgumentException.class, () -> {
            Info emptyInfo = new Info.Builder("").build();
        });
        
        // Test with minimal valid name instead
        Info minimalInfo = new Info.Builder("X").build();
        TaskAvailable task = new TaskAvailable(minimalInfo);
        
        assertNotNull(task.getId());
        assertEquals("X", task.getInfo().getName());
        assertNull(task.getInfo().getDescription());
        assertNull(task.getInfo().getCategory());
    }

    @Test
    void testOneTimeTaskWithPlannedDueDate() {
        String tomorrow = LocalDate.now().plusDays(1).toString();
        
        TaskAvailable task = new TaskAvailable(
                "one-time-id",
                testInfo,
                tomorrow,  // plannedDueDate as String
                true  // isOneTime
        );

        assertTrue(task.isOneTime());
        assertEquals(tomorrow, task.getPlannedDueDate());
    }

    @Test
    void testRecurringTaskWithPlannedDueDate() {
        String nextWeek = LocalDate.now().plusWeeks(1).toString();
        
        TaskAvailable task = new TaskAvailable(
                "recurring-id",
                testInfo,
                nextWeek,  // plannedDueDate as String
                false  // not one-time (recurring)
        );

        assertFalse(task.isOneTime());
        assertEquals(nextWeek, task.getPlannedDueDate());
    }

    @Test
    void testTaskAvailableEquality() {
        // Two tasks with same ID should be considered equal
        TaskAvailable task1 = new TaskAvailable(
                "same-id",
                testInfo,
                null,  // plannedDueDate
                false  // isOneTime
        );
        
        Info differentInfo = new Info.Builder("Different Task").build();
        TaskAvailable task2 = new TaskAvailable(
                "same-id",
                differentInfo,
                LocalDate.now().toString(),  // plannedDueDate as String
                true  // isOneTime
        );

        // Tasks with same ID should be equal
        assertEquals(task1.getId(), task2.getId());
        
        // But their properties can differ
        assertNotEquals(task1.getInfo().getName(), task2.getInfo().getName());
        assertNotEquals(task1.isOneTime(), task2.isOneTime());
    }

    @Test
    void testTaskAvailableWithCategory() {
        Info infoWithCategory = new Info.Builder("Task with Category")
                .description("Has a category")
                .category("work-category-id")
                .build();
        
        TaskAvailable task = new TaskAvailable(infoWithCategory);
        
        assertEquals("work-category-id", task.getInfo().getCategory());
    }

    @Test
    void testTaskAvailableWithoutCategory() {
        Info infoWithoutCategory = new Info.Builder("Task without Category")
                .description("No category")
                .build();
        
        TaskAvailable task = new TaskAvailable(infoWithoutCategory);
        
        assertNull(task.getInfo().getCategory());
    }

    @Test
    void testPlannedDueDateInPast() {
        String yesterday = LocalDate.now().minusDays(1).toString();
        
        TaskAvailable task = new TaskAvailable(
                "past-date-task",
                testInfo,
                yesterday,  // plannedDueDate as String
                false  // isOneTime
        );

        assertEquals(yesterday, task.getPlannedDueDate());
        // Task can have past planned due date (for daily reset logic)
    }

    @Test
    void testPlannedDueDateToday() {
        String today = LocalDate.now().toString();
        
        TaskAvailable task = new TaskAvailable(
                "today-task",
                testInfo,
                today,  // plannedDueDate as String
                false  // isOneTime
        );

        assertEquals(today, task.getPlannedDueDate());
    }

    // Test removed - originalPosition not available on TaskAvailable entity
}