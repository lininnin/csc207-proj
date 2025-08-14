package entity.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for InfoFactory following Clean Architecture principles.
 * Tests the factory pattern implementation for creating Info objects.
 */
class InfoFactoryTest {

    private InfoFactory factory;
    private TestInfoFactory testFactory; // For edge case tests

    @BeforeEach
    void setUp() {
        factory = new InfoFactory();
        testFactory = new TestInfoFactory(); // For tests that need exact preservation
    }

    @Test
    void testCreateInfoWithAllFields() {
        // Create Info with all fields populated
        InfoInterf info = factory.create("Task Name", "Task Description", "category-123");

        assertNotNull(info);
        assertEquals("Task Name", info.getName());
        assertEquals("Task Description", info.getDescription());
        assertEquals("category-123", info.getCategory());
        assertNotNull(info.getId()); // Should have generated ID
    }

    @Test
    void testCreateInfoWithNullDescription() {
        // Create Info with null description
        InfoInterf info = factory.create("Task Name", null, "category-123");

        assertNotNull(info);
        assertEquals("Task Name", info.getName());
        assertNull(info.getDescription());
        assertEquals("category-123", info.getCategory());
    }

    @Test
    void testCreateInfoWithNullCategory() {
        // Create Info with null category
        InfoInterf info = factory.create("Task Name", "Description", null);

        assertNotNull(info);
        assertEquals("Task Name", info.getName());
        assertEquals("Description", info.getDescription());
        assertNull(info.getCategory());
    }

    @Test
    void testCreateInfoWithEmptyStrings() {
        // Use TestInfoFactory for edge case testing with empty strings
        InfoInterf info = testFactory.create("", "", "");

        assertNotNull(info);
        assertEquals("", info.getName());
        assertEquals("", info.getDescription());
        assertEquals("", info.getCategory());
    }

    @Test
    void testCreateMultipleInfosHaveUniqueIds() {
        // Create multiple Info objects and verify unique IDs
        InfoInterf info1 = factory.create("Task 1", "Desc 1", "cat-1");
        InfoInterf info2 = factory.create("Task 2", "Desc 2", "cat-2");
        InfoInterf info3 = factory.create("Task 3", "Desc 3", "cat-3");

        assertNotNull(info1.getId());
        assertNotNull(info2.getId());
        assertNotNull(info3.getId());

        // All IDs should be unique
        assertNotEquals(info1.getId(), info2.getId());
        assertNotEquals(info2.getId(), info3.getId());
        assertNotEquals(info1.getId(), info3.getId());
    }

    @Test
    void testCreateInfoPreservesSpaces() {
        // Use TestInfoFactory for exact space preservation testing
        InfoInterf info = testFactory.create("  Task with spaces  ", 
                                   "  Description with spaces  ", 
                                   "  category  ");

        assertEquals("  Task with spaces  ", info.getName());
        assertEquals("  Description with spaces  ", info.getDescription());
        assertEquals("  category  ", info.getCategory());
    }

    @Test
    void testCreateInfoWithLongStrings() {
        // Test with long strings
        String longName = "This is a very long task name that exceeds normal length";
        String longDesc = "This is an extremely detailed description that contains " +
                         "a lot of information about the task and what needs to be done";
        String longCategory = "very-long-category-name-for-testing";

        InfoInterf info = factory.create(longName, longDesc, longCategory);

        assertNotNull(info);
        assertEquals(longName, info.getName());
        assertEquals(longDesc, info.getDescription());
        assertEquals(longCategory, info.getCategory());
    }

    @Test
    void testFactoryCreatesIndependentObjects() {
        // Verify that created objects are independent
        InfoInterf info1 = factory.create("Task", "Description", "Category");
        InfoInterf info2 = factory.create("Task", "Description", "Category");

        // Same data but different objects
        assertNotSame(info1, info2);
        assertEquals(info1.getName(), info2.getName());
        assertEquals(info1.getDescription(), info2.getDescription());
        assertEquals(info1.getCategory(), info2.getCategory());
        assertNotEquals(info1.getId(), info2.getId()); // Different IDs
    }

    @Test
    void testInfoMutability() {
        // Test that Info objects created by factory are mutable
        InfoInterf info = factory.create("Original", "Original Desc", "cat-1");

        // Modify the info
        info.setName("Modified");
        info.setDescription("Modified Desc");
        info.setCategory("cat-2");

        // Verify changes
        assertEquals("Modified", info.getName());
        assertEquals("Modified Desc", info.getDescription());
        assertEquals("cat-2", info.getCategory());
    }

    @Test
    void testCreateInfoWithSpecialCharacters() {
        // Test with special characters
        InfoInterf info = factory.create("Task @#$%", "Desc with ñ é ü", "cat_123-456");

        assertNotNull(info);
        assertEquals("Task @#$%", info.getName());
        assertEquals("Desc with ñ é ü", info.getDescription());
        assertEquals("cat_123-456", info.getCategory());
    }
}