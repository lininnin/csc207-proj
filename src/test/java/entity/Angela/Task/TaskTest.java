package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Task entity following Clean Architecture principles.
 * Tests business rules and entity constraints.
 */
class TaskTest {

    private Info testInfo;
    private TaskAvailable testTemplate;

    @BeforeEach
    void setUp() {
        testInfo = new Info.Builder("Test Task")
                .description("Test Description")
                .category("category-123")
                .build();
        testTemplate = new TaskAvailable(testInfo);
    }

    @Test
    void testTaskCreationWithMinimalConstructor() {
        LocalDate dueDate = LocalDate.now().plusDays(3);
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                false  // isOneTime
        );

        assertNotNull(task.getId());
        assertEquals(testTemplate.getId(), task.getTemplateTaskId());
        assertEquals("Test Task", task.getInfo().getName());
        assertEquals("Test Description", task.getInfo().getDescription());
        assertEquals("category-123", task.getInfo().getCategory());
        assertNull(task.getPriority());  // Priority not set in minimal constructor
        assertFalse(task.isCompleted());
        assertFalse(task.isOneTime());
        assertEquals(dueDate, task.getDates().getDueDate());
    }

    @Test
    void testTaskCreationWithFullConstructor() {
        LocalDate dueDate = LocalDate.now().plusDays(3);
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);
        LocalDateTime completionTime = LocalDateTime.now();
        
        Task task = new Task(
                "task-id-1",
                testTemplate.getId(),
                testInfo,
                Task.Priority.HIGH,
                dates,
                true,  // isCompleted
                completionTime,  // completedDateTime
                false  // isOneTime
        );

        assertEquals("task-id-1", task.getId());
        assertEquals(testTemplate.getId(), task.getTemplateTaskId());
        assertEquals(Task.Priority.HIGH, task.getPriority());
        assertTrue(task.isCompleted());
        assertEquals(completionTime, task.getCompletedDateTime());
        assertFalse(task.isOneTime());
    }

    @Test
    void testTaskCreationWithNullPriority() {
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                false
        );

        assertNull(task.getPriority());
    }

    @Test
    void testTaskCreationAsOneTime() {
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                true  // One-time task
        );

        assertTrue(task.isOneTime());
    }

    @Test
    void testMarkComplete() {
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );

        assertFalse(task.isCompleted());
        
        task.markComplete();
        assertTrue(task.isCompleted());
        assertNotNull(task.getCompletedDateTime());
    }

    @Test
    void testUnmarkComplete() {
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );

        task.markComplete();
        assertTrue(task.isCompleted());
        
        task.unmarkComplete();
        assertFalse(task.isCompleted());
        assertNull(task.getCompletedDateTime());
    }

    @Test
    void testIsOverdueWithPastDueDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now().minusDays(2), yesterday);
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                false
        );

        assertTrue(task.isOverdue());
    }

    @Test
    void testIsOverdueWithTodayDueDate() {
        LocalDate today = LocalDate.now();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), today);
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                false
        );

        assertFalse(task.isOverdue());
    }

    @Test
    void testIsOverdueWithFutureDueDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), tomorrow);
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                false
        );

        assertFalse(task.isOverdue());
    }

    @Test
    void testIsOverdueWithNullDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                false
        );

        assertFalse(task.isOverdue());
    }

    @Test
    void testIsOverdueWhenCompleted() {
        // Completed tasks are never overdue
        LocalDate yesterday = LocalDate.now().minusDays(1);
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now().minusDays(2), yesterday);
        
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                dates,
                false
        );
        
        task.markComplete();
        
        // Completed tasks are never overdue
        assertFalse(task.isOverdue());
        assertTrue(task.isCompleted());
    }

    @Test
    void testSetPriority() {
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );

        assertNull(task.getPriority());
        
        task.setPriority(Task.Priority.HIGH);
        assertEquals(Task.Priority.HIGH, task.getPriority());
        
        task.setPriority(null);
        assertNull(task.getPriority());
    }

    @Test
    void testPriorityEnum() {
        // Test all priority values
        assertEquals("HIGH", Task.Priority.HIGH.toString());
        assertEquals("MEDIUM", Task.Priority.MEDIUM.toString());
        assertEquals("LOW", Task.Priority.LOW.toString());
        
        // Test valueOf
        assertEquals(Task.Priority.HIGH, Task.Priority.valueOf("HIGH"));
        assertEquals(Task.Priority.MEDIUM, Task.Priority.valueOf("MEDIUM"));
        assertEquals(Task.Priority.LOW, Task.Priority.valueOf("LOW"));
    }

    @Test
    void testTaskIdGeneration() {
        Task task1 = new Task(
                "template-1",
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );
        
        Task task2 = new Task(
                "template-2",
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );
        
        // Each task should have a unique ID
        assertNotEquals(task1.getId(), task2.getId());
        
        // But template IDs should be preserved
        assertEquals("template-1", task1.getTemplateTaskId());
        assertEquals("template-2", task2.getTemplateTaskId());
    }

    @Test
    void testOriginalPosition() {
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );

        // Initially null
        assertNull(task.getOriginalPosition());
        
        // Can be set
        task.setOriginalPosition(5);
        assertEquals(5, task.getOriginalPosition());
        
        // Can be cleared
        task.setOriginalPosition(null);
        assertNull(task.getOriginalPosition());
    }

    @Test
    void testMarkCompleteWithSpecificTime() {
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );

        LocalDateTime specificTime = LocalDateTime.of(2024, 1, 1, 12, 0);
        task.markComplete(specificTime);
        
        assertTrue(task.isCompleted());
        assertEquals(specificTime, task.getCompletedDateTime());
    }

    @Test
    void testSetOneTime() {
        Task task = new Task(
                testTemplate.getId(),
                testInfo,
                new BeginAndDueDates(LocalDate.now(), null),
                false
        );

        assertFalse(task.isOneTime());
        
        task.setOneTime(true);
        assertTrue(task.isOneTime());
        
        task.setOneTime(false);
        assertFalse(task.isOneTime());
    }
}