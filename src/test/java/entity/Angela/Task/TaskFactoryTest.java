package entity.Angela.Task;

import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TaskFactory following Clean Architecture principles.
 * Tests the factory pattern implementation for creating Task objects.
 */
class TaskFactoryTest {

    private TaskFactory taskFactory;
    private Info testInfo;
    private BeginAndDueDates testDates;

    @BeforeEach
    void setUp() {
        taskFactory = new TaskFactory();
        testInfo = new Info.Builder("Test Task")
                .description("Test Description")
                .category("category-123")
                .build();
        testDates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    void testCreateTaskWithAllParameters() {
        // Create task with all parameters
        Task task = (Task) taskFactory.create("template-123", testInfo, testDates, false);

        assertNotNull(task);
        assertEquals("template-123", task.getTemplateTaskId());
        assertEquals("Test Task", task.getInfo().getName());
        assertEquals("Test Description", task.getInfo().getDescription());
        assertEquals("category-123", task.getInfo().getCategory());
        assertEquals(testDates, task.getDates());
        assertFalse(task.isOneTime());
        assertFalse(task.isCompleted());
        assertNull(task.getPriority()); // Default priority is null
    }

    @Test
    void testCreateTaskWithOneTimeFlag() {
        // Create one-time task
        Task task = (Task) taskFactory.create("template-456", testInfo, testDates, true);

        assertNotNull(task);
        assertTrue(task.isOneTime());
        assertEquals("template-456", task.getTemplateTaskId());
    }

    @Test
    void testCreateTaskWithNullTemplateId() {
        // Test with null template ID - should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.create(null, testInfo, testDates, false);
        });
    }
    
    @Test
    void testCreateTaskWithEmptyTemplateId() {
        // Test with empty template ID - should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.create("", testInfo, testDates, false);
        });
    }
    
    @Test
    void testCreateTaskWithNullInfo() {
        // Test with null info - should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.create("template-123", null, testDates, false);
        });
    }
    
    @Test
    void testCreateTaskWithNullDates() {
        // Test with null dates - should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.create("template-123", testInfo, null, false);
        });
    }

    @Test
    void testCreateTaskGeneratesUniqueId() {
        // Create multiple tasks and verify they have unique IDs
        Task task1 = (Task) taskFactory.create("template-1", testInfo, testDates, false);
        Task task2 = (Task) taskFactory.create("template-1", testInfo, testDates, false);

        assertNotNull(task1.getId());
        assertNotNull(task2.getId());
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void testCreateTaskWithDifferentDates() {
        // Test with past dates (for overdue testing)
        BeginAndDueDates pastDates = new BeginAndDueDates(
            LocalDate.now().minusDays(7),
            LocalDate.now().minusDays(1)
        );
        
        Task task = (Task) taskFactory.create("template-past", testInfo, pastDates, false);

        assertNotNull(task);
        assertTrue(task.isOverdue());
    }

    @Test
    void testCreateTaskWithNullDueDate() {
        // Test with no due date
        BeginAndDueDates noDueDate = new BeginAndDueDates(LocalDate.now(), null);
        
        Task task = (Task) taskFactory.create("template-no-due", testInfo, noDueDate, false);

        assertNotNull(task);
        assertFalse(task.isOverdue()); // Tasks without due dates are never overdue
    }

    @Test
    void testFactoryPreservesInfoIntegrity() {
        // Verify that the factory doesn't modify the original Info object
        String originalName = testInfo.getName();
        String originalDescription = testInfo.getDescription();
        String originalCategory = testInfo.getCategory();

        Task task = (Task) taskFactory.create("template-test", testInfo, testDates, false);

        // Original info should be unchanged
        assertEquals(originalName, testInfo.getName());
        assertEquals(originalDescription, testInfo.getDescription());
        assertEquals(originalCategory, testInfo.getCategory());

        // Task should have the same values
        assertEquals(originalName, task.getInfo().getName());
        assertEquals(originalDescription, task.getInfo().getDescription());
        assertEquals(originalCategory, task.getInfo().getCategory());
    }
}