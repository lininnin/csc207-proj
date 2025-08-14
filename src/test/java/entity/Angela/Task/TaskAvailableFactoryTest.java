package entity.Angela.Task;

import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TaskAvailableFactory following Clean Architecture principles.
 * Tests the factory pattern implementation for creating TaskAvailable objects.
 */
class TaskAvailableFactoryTest {

    private TaskAvailableFactory factory;
    private Info testInfo;

    @BeforeEach
    void setUp() {
        factory = new TaskAvailableFactory();
        testInfo = new Info.Builder("Test Task")
                .description("Test Description")
                .category("category-123")
                .build();
    }

    @Test
    void testCreateTaskAvailable() {
        // Create TaskAvailable using factory
        TaskAvailable task = factory.create(testInfo);

        assertNotNull(task);
        assertEquals("Test Task", task.getInfo().getName());
        assertEquals("Test Description", task.getInfo().getDescription());
        assertEquals("category-123", task.getInfo().getCategory());
        assertNotNull(task.getId());
        assertFalse(task.isOneTime()); // Default should be false
    }

    @Test
    void testCreateTaskAvailableWithOneTimeFlag() {
        // Create one-time TaskAvailable
        TaskAvailable task = factory.create(testInfo, true);

        assertNotNull(task);
        assertTrue(task.isOneTime());
        assertEquals("Test Task", task.getInfo().getName());
    }

    @Test
    void testCreateMultipleTasksHaveUniqueIds() {
        // Create multiple tasks with different Info objects to get unique IDs
        Info info1 = new Info.Builder("Task 1").build();
        Info info2 = new Info.Builder("Task 2").build();
        Info info3 = new Info.Builder("Task 3").build();
        
        TaskAvailable task1 = factory.create(info1);
        TaskAvailable task2 = factory.create(info2);
        TaskAvailable task3 = factory.create(info3);

        assertNotNull(task1.getId());
        assertNotNull(task2.getId());
        assertNotNull(task3.getId());

        // All IDs should be unique
        assertNotEquals(task1.getId(), task2.getId());
        assertNotEquals(task2.getId(), task3.getId());
        assertNotEquals(task1.getId(), task3.getId());
    }

    @Test
    void testCreateWithEmptyInfo() {
        // Test that empty name throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            new Info.Builder("").build();
        });
    }
    
    @Test
    void testCreateWithMinimalInfo() {
        // Test with minimal valid info
        Info minimalInfo = new Info.Builder("A").build();
        
        TaskAvailable task = factory.create(minimalInfo);

        assertNotNull(task);
        assertEquals("A", task.getInfo().getName());
        assertNull(task.getInfo().getDescription());
        assertNull(task.getInfo().getCategory());
    }

    @Test
    void testCreatePreservesInfoIntegrity() {
        // Verify that the factory doesn't modify the original Info object
        String originalName = testInfo.getName();
        String originalDescription = testInfo.getDescription();
        String originalCategory = testInfo.getCategory();

        TaskAvailable task = factory.create(testInfo);

        // Original info should be unchanged
        assertEquals(originalName, testInfo.getName());
        assertEquals(originalDescription, testInfo.getDescription());
        assertEquals(originalCategory, testInfo.getCategory());

        // Task should have the same values
        assertEquals(originalName, task.getInfo().getName());
        assertEquals(originalDescription, task.getInfo().getDescription());
        assertEquals(originalCategory, task.getInfo().getCategory());
    }

    @Test
    void testCreateWithComplexInfo() {
        // Test with fully populated Info
        Info complexInfo = new Info.Builder("Complex Task")
                .description("This is a very detailed description of a complex task")
                .category("category-complex")
                .build();

        TaskAvailable task = factory.create(complexInfo, false);

        assertNotNull(task);
        assertEquals("Complex Task", task.getInfo().getName());
        assertEquals("This is a very detailed description of a complex task", 
                    task.getInfo().getDescription());
        assertEquals("category-complex", task.getInfo().getCategory());
        assertFalse(task.isOneTime());
    }

    @Test
    void testFactoryMethodOverloads() {
        // Test that both factory methods work correctly
        
        // Method 1: create(Info) - defaults to non-one-time
        TaskAvailable task1 = factory.create(testInfo);
        assertFalse(task1.isOneTime());

        // Method 2: create(Info, boolean) - explicit one-time flag
        TaskAvailable task2 = factory.create(testInfo, true);
        assertTrue(task2.isOneTime());
        
        TaskAvailable task3 = factory.create(testInfo, false);
        assertFalse(task3.isOneTime());
    }
}