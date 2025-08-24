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
        
        TaskAvailable oneTimeTask = task.withOneTimeFlag(true);
        assertTrue(oneTimeTask.isOneTime());
        
        TaskAvailable regularTask = oneTimeTask.withOneTimeFlag(false);
        assertFalse(regularTask.isOneTime());
    }

    @Test
    void testSetPlannedDueDate() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        assertNull(task.getPlannedDueDate());
        
        String plannedDate = LocalDate.now().plusDays(3).toString();
        TaskAvailable taskWithDate = task.withPlannedDueDate(plannedDate);
        assertEquals(plannedDate, taskWithDate.getPlannedDueDate());
        
        TaskAvailable taskWithoutDate = taskWithDate.withPlannedDueDate(null);
        assertNull(taskWithoutDate.getPlannedDueDate());
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

    // Tests for uncovered methods to achieve 100% coverage

    @Test
    void testHasPlannedDueDateWithValidDate() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        // Initially false
        assertFalse(task.hasPlannedDueDate());
        
        // Set a planned due date
        TaskAvailable taskWithDate = task.withPlannedDueDate(LocalDate.now().plusDays(5).toString());
        assertTrue(taskWithDate.hasPlannedDueDate());
    }

    @Test
    void testHasPlannedDueDateWithEmptyString() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        // Set empty string
        TaskAvailable taskWithEmptyDate = task.withPlannedDueDate("");
        assertFalse(taskWithEmptyDate.hasPlannedDueDate());
    }

    @Test
    void testHasPlannedDueDateWithNull() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        // Set null
        TaskAvailable taskWithNullDate = task.withPlannedDueDate(null);
        assertFalse(taskWithNullDate.hasPlannedDueDate());
    }

    @Test
    void testWithPlannedDueDateImmutableUpdate() {
        TaskAvailable originalTask = new TaskAvailable(testInfo);
        String dueDate = LocalDate.now().plusDays(10).toString();
        
        TaskAvailable updatedTask = originalTask.withPlannedDueDate(dueDate);
        
        // Original should be unchanged
        assertNull(originalTask.getPlannedDueDate());
        
        // New task should have the due date
        assertEquals(dueDate, updatedTask.getPlannedDueDate());
        
        // Should be different instances
        assertNotSame(originalTask, updatedTask);
        
        // Should have same ID and info
        assertEquals(originalTask.getId(), updatedTask.getId());
        assertEquals(originalTask.getInfo(), updatedTask.getInfo());
    }

    @Test
    void testWithPlannedDueDateWithNull() {
        TaskAvailable originalTask = new TaskAvailable(
            "task-id",
            testInfo,
            LocalDate.now().plusDays(5).toString(),
            false
        );
        
        TaskAvailable updatedTask = originalTask.withPlannedDueDate(null);
        
        // Original should have due date
        assertNotNull(originalTask.getPlannedDueDate());
        
        // New task should have null due date
        assertNull(updatedTask.getPlannedDueDate());
    }

    @Test
    void testIsDuplicateOfWithSameNameAndCategory() {
        Info info1 = new Info.Builder("Duplicate Task")
                .category("work")
                .build();
        Info info2 = new Info.Builder("Duplicate Task")
                .category("work")
                .build();
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        
        assertTrue(task1.isDuplicateOf(task2));
        assertTrue(task2.isDuplicateOf(task1));
    }

    @Test
    void testIsDuplicateOfWithSameNameDifferentCategory() {
        Info info1 = new Info.Builder("Same Name")
                .category("work")
                .build();
        Info info2 = new Info.Builder("Same Name")
                .category("personal")
                .build();
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        
        assertFalse(task1.isDuplicateOf(task2));
        assertFalse(task2.isDuplicateOf(task1));
    }

    @Test
    void testIsDuplicateOfWithDifferentNameSameCategory() {
        Info info1 = new Info.Builder("Task One")
                .category("work")
                .build();
        Info info2 = new Info.Builder("Task Two")
                .category("work")
                .build();
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        
        assertFalse(task1.isDuplicateOf(task2));
        assertFalse(task2.isDuplicateOf(task1));
    }

    @Test
    void testIsDuplicateOfCaseInsensitive() {
        Info info1 = new Info.Builder("Test Task")
                .category("Work")
                .build();
        Info info2 = new Info.Builder("test task")
                .category("WORK")
                .build();
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        
        assertTrue(task1.isDuplicateOf(task2));
        assertTrue(task2.isDuplicateOf(task1));
    }

    @Test
    void testIsDuplicateOfWithBothNullCategories() {
        Info info1 = new Info.Builder("Task Name")
                .build(); // No category (null)
        Info info2 = new Info.Builder("Task Name")
                .build(); // No category (null)
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        
        assertTrue(task1.isDuplicateOf(task2));
        assertTrue(task2.isDuplicateOf(task1));
    }

    @Test
    void testIsDuplicateOfWithOneNullCategory() {
        Info info1 = new Info.Builder("Task Name")
                .category("work")
                .build();
        Info info2 = new Info.Builder("Task Name")
                .build(); // No category (null)
        
        TaskAvailable task1 = new TaskAvailable(info1);
        TaskAvailable task2 = new TaskAvailable(info2);
        
        assertFalse(task1.isDuplicateOf(task2));
        assertFalse(task2.isDuplicateOf(task1));
    }

    @Test
    void testIsDuplicateOfWithNull() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        assertFalse(task.isDuplicateOf(null));
    }

    @Test
    void testToString() {
        String plannedDate = LocalDate.now().plusDays(3).toString();
        TaskAvailable task = new TaskAvailable(
            "test-id-123",
            testInfo,
            plannedDate,
            true
        );
        
        String result = task.toString();
        
        // Should contain key information
        assertTrue(result.contains("TaskAvailable{"));
        assertTrue(result.contains("id='test-id-123'"));
        assertTrue(result.contains("name='Test Task'"));
        assertTrue(result.contains("category='category-123'"));
        assertTrue(result.contains("oneTime=true"));
        assertTrue(result.contains("plannedDueDate='" + plannedDate + "'"));
        assertTrue(result.contains("}"));
    }

    @Test
    void testToStringWithNullPlannedDueDate() {
        TaskAvailable task = new TaskAvailable(
            "test-id-456",
            testInfo,
            null,
            false
        );
        
        String result = task.toString();
        
        // Should not contain plannedDueDate when null
        assertTrue(result.contains("TaskAvailable{"));
        assertTrue(result.contains("id='test-id-456'"));
        assertTrue(result.contains("oneTime=false"));
        assertFalse(result.contains("plannedDueDate="));
    }

    @Test
    void testHashCode() {
        TaskAvailable task1 = new TaskAvailable(
            "same-id",
            testInfo,
            null,
            false
        );
        TaskAvailable task2 = new TaskAvailable(
            "same-id",
            testInfo,
            LocalDate.now().toString(),
            true
        );
        TaskAvailable task3 = new TaskAvailable(
            "different-id",
            testInfo,
            null,
            false
        );
        
        // Same ID should have same hash code
        assertEquals(task1.hashCode(), task2.hashCode());
        
        // Different ID should have different hash code
        assertNotEquals(task1.hashCode(), task3.hashCode());
    }

    @Test
    void testConstructorValidationNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TaskAvailable(null, testInfo, null, false);
        });
    }

    @Test
    void testConstructorValidationEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TaskAvailable("", testInfo, null, false);
        });
    }

    @Test
    void testConstructorValidationWhitespaceId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TaskAvailable("   ", testInfo, null, false);
        });
    }

    @Test
    void testConstructorValidationNullInfoInFullConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TaskAvailable("valid-id", null, null, false);
        });
    }

    @Test
    void testEqualsWithNull() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        assertFalse(task.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        TaskAvailable task = new TaskAvailable(testInfo);
        String notATask = "not a task";
        
        assertFalse(task.equals(notATask));
    }

    @Test
    void testEqualsSameInstance() {
        TaskAvailable task = new TaskAvailable(testInfo);
        
        assertTrue(task.equals(task));
    }
}