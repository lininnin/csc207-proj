package entity.Angela.Task;

import entity.info.Info;
import entity.info.ImmutableInfo;
import entity.info.InfoInterf;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.BeginAndDueDates.ImmutableBeginAndDueDates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // Tests for template-based creation methods (uncovered)

    @Test
    void testCreateFromTemplate() {
        // Create a TaskAvailable as template
        TaskAvailable template = new TaskAvailable(testInfo).withOneTimeFlag(true);
        
        Task task = (Task) taskFactory.createFromTemplate(template);
        
        assertNotNull(task);
        assertEquals(template.getId(), task.getTemplateTaskId());
        assertEquals(template.getInfo().getName(), task.getInfo().getName());
        assertEquals(template.getInfo().getDescription(), task.getInfo().getDescription());
        assertEquals(template.getInfo().getCategory(), task.getInfo().getCategory());
        assertTrue(task.isOneTime());
        assertEquals(LocalDate.now(), task.getDates().getBeginDate());
        assertNull(task.getDates().getDueDate());
    }

    @Test
    void testCreateFromTemplateWithNullTemplate() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.createFromTemplate(null);
        });
    }

    @Test
    void testCreateFromTemplateWithDueDate() {
        TaskAvailable template = new TaskAvailable(testInfo);
        LocalDate futureDate = LocalDate.now().plusDays(5);
        
        Task task = (Task) taskFactory.createFromTemplate(template, futureDate);
        
        assertNotNull(task);
        assertEquals(template.getId(), task.getTemplateTaskId());
        assertEquals(LocalDate.now(), task.getDates().getBeginDate());
        assertEquals(futureDate, task.getDates().getDueDate());
    }

    @Test
    void testCreateFromTemplateWithPastDueDate() {
        TaskAvailable template = new TaskAvailable(testInfo);
        LocalDate pastDate = LocalDate.now().minusDays(1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.createFromTemplate(template, pastDate);
        });
    }

    @Test
    void testCreateFromTemplateWithDueDateNullTemplate() {
        LocalDate futureDate = LocalDate.now().plusDays(3);
        
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.createFromTemplate(null, futureDate);
        });
    }

    @Test
    void testCreateFromTemplateWithTodayDueDate() {
        TaskAvailable template = new TaskAvailable(testInfo);
        LocalDate today = LocalDate.now();
        
        Task task = (Task) taskFactory.createFromTemplate(template, today);
        
        assertNotNull(task);
        assertEquals(today, task.getDates().getDueDate());
    }

    @Test
    void testCreateFromTemplateWithNullDueDate() {
        TaskAvailable template = new TaskAvailable(testInfo);
        
        Task task = (Task) taskFactory.createFromTemplate(template, null);
        
        assertNotNull(task);
        assertEquals(LocalDate.now(), task.getDates().getBeginDate());
        assertNull(task.getDates().getDueDate());
    }

    // Tests for immutable template creation methods (uncovered)

    @Test
    void testCreateImmutableFromTemplate() {
        TaskAvailable template = new TaskAvailable(testInfo).withOneTimeFlag(false);
        
        Task task = (Task) taskFactory.createImmutableFromTemplate(template);
        
        assertNotNull(task);
        assertEquals(template.getId(), task.getTemplateTaskId());
        assertEquals(template.getInfo().getName(), task.getInfo().getName());
        assertFalse(task.isOneTime());
        assertEquals(LocalDate.now(), task.getDates().getBeginDate());
        assertNull(task.getDates().getDueDate());
    }

    @Test
    void testCreateImmutableFromTemplateWithNullTemplate() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.createImmutableFromTemplate(null);
        });
    }

    @Test
    void testCreateImmutableWithAllFields() {
        // Use constructor that takes id, name, description, category, and createdDate
        ImmutableInfo immutableInfo = new ImmutableInfo("id-123", "Immutable Task", "Description", "category", LocalDate.now());
        ImmutableBeginAndDueDates immutableDates = new ImmutableBeginAndDueDates(
            LocalDate.now(), 
            LocalDate.now().plusDays(7)
        );
        
        Task task = (Task) taskFactory.createImmutable(
            "template-123", 
            immutableInfo, 
            immutableDates, 
            true
        );
        
        assertNotNull(task);
        assertEquals("template-123", task.getTemplateTaskId());
        assertEquals("Immutable Task", task.getInfo().getName());
        assertEquals("Description", task.getInfo().getDescription());
        assertEquals("category", task.getInfo().getCategory());
        assertTrue(task.isOneTime());
        assertEquals(LocalDate.now(), task.getDates().getBeginDate());
        assertEquals(LocalDate.now().plusDays(7), task.getDates().getDueDate());
    }

    @Test
    void testCreateImmutableWithNullInfo() {
        ImmutableBeginAndDueDates immutableDates = new ImmutableBeginAndDueDates(
            LocalDate.now(), 
            LocalDate.now().plusDays(3)
        );
        
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.createImmutable("template-id", null, immutableDates, false);
        });
    }

    @Test
    void testCreateImmutableWithNullDates() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-456", "Task", "Desc", "cat", LocalDate.now());
        
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.createImmutable("template-id", immutableInfo, null, false);
        });
    }

    @Test
    void testCreateImmutableWithMinimalData() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-789", "Min Task", "", "", LocalDate.now());
        ImmutableBeginAndDueDates immutableDates = new ImmutableBeginAndDueDates(
            LocalDate.now(), 
            null
        );
        
        Task task = (Task) taskFactory.createImmutable(
            "min-template", 
            immutableInfo, 
            immutableDates, 
            false
        );
        
        assertNotNull(task);
        assertEquals("min-template", task.getTemplateTaskId());
        assertEquals("Min Task", task.getInfo().getName());
        assertNull(task.getInfo().getDescription());
        assertNull(task.getInfo().getCategory());
        assertFalse(task.isOneTime());
        assertNull(task.getDates().getDueDate());
    }

    // Tests for full constructor with all parameters (uncovered)

    @Test
    void testCreateWithAllParameters() {
        String taskId = "task-instance-123";
        String templateId = "template-456";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDateTime completedTime = LocalDateTime.now().minusHours(2);
        
        Task task = (Task) taskFactory.create(
            taskId, 
            templateId, 
            testInfo, 
            priority, 
            testDates, 
            true, // isCompleted
            completedTime, 
            true // isOneTime
        );
        
        assertNotNull(task);
        assertEquals(taskId, task.getId());
        assertEquals(templateId, task.getTemplateTaskId());
        assertEquals(testInfo.getName(), task.getInfo().getName());
        assertEquals(priority, task.getPriority());
        assertEquals(testDates.getBeginDate(), task.getDates().getBeginDate());
        assertTrue(task.isCompleted());
        assertEquals(completedTime, task.getCompletedDateTime());
        assertTrue(task.isOneTime());
    }

    @Test
    void testCreateWithAllParametersIncompleteTask() {
        Task task = (Task) taskFactory.create(
            "task-id", 
            "template-id", 
            testInfo, 
            null, // no priority
            testDates, 
            false, // not completed
            null, // no completion time
            false // not one-time
        );
        
        assertNotNull(task);
        assertNull(task.getPriority());
        assertFalse(task.isCompleted());
        assertNull(task.getCompletedDateTime());
        assertFalse(task.isOneTime());
    }

    // Tests for helper method coverage - ImmutableInfo conversion

    @Test
    void testCreateWithImmutableInfoInterface() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-abc", "Interface Test", "Testing", "test", LocalDate.now());
        InfoInterf infoInterface = immutableInfo;
        
        Task task = (Task) taskFactory.create("template-test", infoInterface, testDates, false);
        
        assertNotNull(task);
        assertEquals("Interface Test", task.getInfo().getName());
        assertEquals("Testing", task.getInfo().getDescription());
        assertEquals("test", task.getInfo().getCategory());
    }

    @Test
    void testCreateWithImmutableDatesInterface() {
        ImmutableBeginAndDueDates immutableDates = new ImmutableBeginAndDueDates(
            LocalDate.now(),
            LocalDate.now().plusDays(10)
        );
        BeginAndDueDatesInterf datesInterface = immutableDates;
        
        Task task = (Task) taskFactory.create("template-test", testInfo, datesInterface, false);
        
        assertNotNull(task);
        assertEquals(LocalDate.now(), task.getDates().getBeginDate());
        assertEquals(LocalDate.now().plusDays(10), task.getDates().getDueDate());
    }

    // Edge cases for type conversion validation

    @Test
    void testCreateWithUnsupportedInfoType() {
        InfoInterf unsupportedInfo = new InfoInterf() {
            @Override
            public String getId() { return "unsupported-id"; }
            @Override
            public String getName() { return "Unsupported"; }
            @Override
            public String getDescription() { return "Test"; }
            @Override
            public String getCategory() { return "test"; }
            @Override
            public LocalDate getCreatedDate() { return LocalDate.now(); }
            @Override
            public void setName(String name) {}
            @Override
            public void setDescription(String description) {}
            @Override
            public void setCategory(String category) {}
        };
        
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.create("template-id", unsupportedInfo, testDates, false);
        });
    }

    @Test
    void testCreateWithUnsupportedDatesType() {
        BeginAndDueDatesInterf unsupportedDates = new BeginAndDueDatesInterf() {
            @Override
            public LocalDate getBeginDate() { return LocalDate.now(); }
            @Override
            public LocalDate getDueDate() { return LocalDate.now().plusDays(5); }
            @Override
            public void setBeginDate(LocalDate beginDate) {}
            @Override
            public void setDueDate(LocalDate dueDate) {}
            @Override
            public boolean hasDueDate() { return true; }
            @Override
            public boolean isOverdue() { return false; }
            @Override
            public boolean isDueTodayOrFuture() { return true; }
        };
        
        assertThrows(IllegalArgumentException.class, () -> {
            taskFactory.create("template-id", testInfo, unsupportedDates, false);
        });
    }
}