package entity.Angela.Task;

import entity.info.Info;
import entity.info.ImmutableInfo;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
        TaskAvailable task = (TaskAvailable) factory.create(testInfo);

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
        TaskAvailable task = (TaskAvailable) factory.create(testInfo, true);

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
        
        TaskAvailable task1 = (TaskAvailable) factory.create(info1);
        TaskAvailable task2 = (TaskAvailable) factory.create(info2);
        TaskAvailable task3 = (TaskAvailable) factory.create(info3);

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
        
        TaskAvailable task = (TaskAvailable) factory.create(minimalInfo);

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

        TaskAvailable task = (TaskAvailable) factory.create(testInfo);

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

        TaskAvailable task = (TaskAvailable) factory.create(complexInfo, false);

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
        TaskAvailable task1 = (TaskAvailable) factory.create(testInfo);
        assertFalse(task1.isOneTime());

        // Method 2: create(Info, boolean) - explicit one-time flag
        TaskAvailable task2 = (TaskAvailable) factory.create(testInfo, true);
        assertTrue(task2.isOneTime());
        
        TaskAvailable task3 = (TaskAvailable) factory.create(testInfo, false);
        assertFalse(task3.isOneTime());
    }

    // Tests for uncovered methods to achieve 100% coverage

    @Test
    void testCreateWithAllFields() {
        String plannedDueDate = LocalDate.now().plusDays(7).toString();
        
        TaskAvailable task = (TaskAvailable) factory.create(
            "test-id-123",
            testInfo,
            plannedDueDate,
            true
        );
        
        assertEquals("test-id-123", task.getId());
        assertEquals("Test Task", task.getInfo().getName());
        assertEquals(plannedDueDate, task.getPlannedDueDate());
        assertTrue(task.isOneTime());
    }

    @Test
    void testCreateWithAllFieldsNullPlannedDate() {
        TaskAvailable task = (TaskAvailable) factory.create(
            "test-id-456",
            testInfo,
            null,
            false
        );
        
        assertEquals("test-id-456", task.getId());
        assertEquals("Test Task", task.getInfo().getName());
        assertNull(task.getPlannedDueDate());
        assertFalse(task.isOneTime());
    }

    @Test
    void testCreateWithImmutableInfo() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-123", "Immutable Task", "Description", "category", LocalDate.now());
        
        TaskAvailable task = (TaskAvailable) factory.createImmutable(immutableInfo);
        
        assertNotNull(task);
        assertEquals("Immutable Task", task.getInfo().getName());
        assertEquals("Description", task.getInfo().getDescription());
        assertEquals("category", task.getInfo().getCategory());
        assertFalse(task.isOneTime());
    }

    @Test
    void testCreateWithImmutableInfoAndOneTimeFlag() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-456", "One-Time Task", "Special task", "work", LocalDate.now());
        
        TaskAvailable task = (TaskAvailable) factory.createImmutable(immutableInfo, true);
        
        assertNotNull(task);
        assertEquals("One-Time Task", task.getInfo().getName());
        assertEquals("Special task", task.getInfo().getDescription());
        assertEquals("work", task.getInfo().getCategory());
        assertTrue(task.isOneTime());
    }

    @Test
    void testCreateWithImmutableInfoFalseOneTime() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-789", "Regular Task", "", "", LocalDate.now());
        
        TaskAvailable task = (TaskAvailable) factory.createImmutable(immutableInfo, false);
        
        assertNotNull(task);
        assertEquals("Regular Task", task.getInfo().getName());
        assertNull(task.getInfo().getDescription());
        assertNull(task.getInfo().getCategory());
        assertFalse(task.isOneTime());
    }

    @Test
    void testCreateImmutableWithNullInfo() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createImmutable((ImmutableInfo) null);
        });
    }

    @Test
    void testCreateImmutableWithOneTimeFlagWithNullInfo() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createImmutable((ImmutableInfo) null, true);
        });
    }

    @Test
    void testCreateWithInfoInterfaceAsInfo() {
        InfoInterf infoInterface = testInfo; // Cast to interface
        
        TaskAvailable task = (TaskAvailable) factory.create(infoInterface);
        
        assertNotNull(task);
        assertEquals("Test Task", task.getInfo().getName());
        assertEquals("Test Description", task.getInfo().getDescription());
        assertEquals("category-123", task.getInfo().getCategory());
    }

    @Test
    void testCreateWithInfoInterfaceAsImmutableInfo() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-abc", "Interface Task", "Testing interface", "test", LocalDate.now());
        InfoInterf infoInterface = immutableInfo; // Cast to interface
        
        TaskAvailable task = (TaskAvailable) factory.create(infoInterface);
        
        assertNotNull(task);
        assertEquals("Interface Task", task.getInfo().getName());
        assertEquals("Testing interface", task.getInfo().getDescription());
        assertEquals("test", task.getInfo().getCategory());
    }

    @Test
    void testCreateWithOneTimeFlagInfoInterfaceAsInfo() {
        InfoInterf infoInterface = testInfo; // Cast to interface
        
        TaskAvailable task = (TaskAvailable) factory.create(infoInterface, true);
        
        assertNotNull(task);
        assertEquals("Test Task", task.getInfo().getName());
        assertTrue(task.isOneTime());
    }

    @Test
    void testCreateWithOneTimeFlagInfoInterfaceAsImmutableInfo() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-def", "Interface One-Time", "Testing", "category", LocalDate.now());
        InfoInterf infoInterface = immutableInfo; // Cast to interface
        
        TaskAvailable task = (TaskAvailable) factory.create(infoInterface, false);
        
        assertNotNull(task);
        assertEquals("Interface One-Time", task.getInfo().getName());
        assertFalse(task.isOneTime());
    }

    @Test
    void testCreateWithAllFieldsInfoInterfaceAsInfo() {
        String plannedDate = LocalDate.now().plusDays(3).toString();
        InfoInterf infoInterface = testInfo; // Cast to interface
        
        TaskAvailable task = (TaskAvailable) factory.create(
            "interface-id",
            infoInterface,
            plannedDate,
            true
        );
        
        assertEquals("interface-id", task.getId());
        assertEquals("Test Task", task.getInfo().getName());
        assertEquals(plannedDate, task.getPlannedDueDate());
        assertTrue(task.isOneTime());
    }

    @Test
    void testCreateWithAllFieldsInfoInterfaceAsImmutableInfo() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-ghi", "All Fields Task", "Complete test", "full", LocalDate.now());
        InfoInterf infoInterface = immutableInfo; // Cast to interface
        String plannedDate = LocalDate.now().plusDays(5).toString();
        
        TaskAvailable task = (TaskAvailable) factory.create(
            "immutable-interface-id",
            infoInterface,
            plannedDate,
            false
        );
        
        assertEquals("immutable-interface-id", task.getId());
        assertEquals("All Fields Task", task.getInfo().getName());
        assertEquals("Complete test", task.getInfo().getDescription());
        assertEquals("full", task.getInfo().getCategory());
        assertEquals(plannedDate, task.getPlannedDueDate());
        assertFalse(task.isOneTime());
    }

    @Test
    void testFactoryCreatesIndependentInstances() {
        TaskAvailable task1 = (TaskAvailable) factory.create(testInfo);
        TaskAvailable task2 = (TaskAvailable) factory.create(testInfo);
        
        // Should be different instances
        assertNotSame(task1, task2);
        
        // But have same data from shared Info
        assertEquals(task1.getInfo(), task2.getInfo());
    }

    @Test
    void testFactoryWithDifferentInfoTypes() {
        // Test with regular Info
        TaskAvailable task1 = (TaskAvailable) factory.create(testInfo, true);
        
        // Test with ImmutableInfo
        ImmutableInfo immutableInfo = new ImmutableInfo("id-jkl", "Immutable", "Desc", "Cat", LocalDate.now());
        TaskAvailable task2 = (TaskAvailable) factory.createImmutable(immutableInfo, true);
        
        // Both should be one-time tasks
        assertTrue(task1.isOneTime());
        assertTrue(task2.isOneTime());
        
        // But have different names
        assertNotEquals(task1.getInfo().getName(), task2.getInfo().getName());
    }
}