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

    // Tests for createImmutable methods
    @Test
    void testCreateImmutableWithAllFields() {
        // Create ImmutableInfo with all fields populated
        ImmutableInfo immutableInfo = factory.createImmutable("Task Name", "Task Description", "category-123");

        assertNotNull(immutableInfo);
        assertEquals("Task Name", immutableInfo.getName());
        assertEquals("Task Description", immutableInfo.getDescription());
        assertEquals("category-123", immutableInfo.getCategory());
        assertNotNull(immutableInfo.getId());
        assertNotNull(immutableInfo.getCreatedDate());
    }

    @Test
    void testCreateImmutableWithNullDescription() {
        // Create ImmutableInfo with null description
        ImmutableInfo immutableInfo = factory.createImmutable("Task Name", null, "category-123");

        assertNotNull(immutableInfo);
        assertEquals("Task Name", immutableInfo.getName());
        assertNull(immutableInfo.getDescription()); // Factory creates mutable Info first, then converts - preserves null
        assertEquals("category-123", immutableInfo.getCategory());
    }

    @Test
    void testCreateImmutableWithNullCategory() {
        // Create ImmutableInfo with null category
        ImmutableInfo immutableInfo = factory.createImmutable("Task Name", "Description", null);

        assertNotNull(immutableInfo);
        assertEquals("Task Name", immutableInfo.getName());
        assertEquals("Description", immutableInfo.getDescription());
        assertNull(immutableInfo.getCategory()); // Factory creates mutable Info first, then converts - preserves null
    }

    @Test
    void testCreateImmutableWithNullName() {
        // Create ImmutableInfo with null name - should default to "Untitled"
        ImmutableInfo immutableInfo = factory.createImmutable(null, "Description", "Category");

        assertNotNull(immutableInfo);
        assertEquals("Untitled", immutableInfo.getName());
        assertEquals("Description", immutableInfo.getDescription());
        assertEquals("Category", immutableInfo.getCategory());
    }

    @Test
    void testCreateImmutableWithEmptyName() {
        // Create ImmutableInfo with empty name - should default to "Untitled"
        ImmutableInfo immutableInfo = factory.createImmutable("  ", "Description", "Category");

        assertNotNull(immutableInfo);
        assertEquals("Untitled", immutableInfo.getName());
        assertEquals("Description", immutableInfo.getDescription());
        assertEquals("Category", immutableInfo.getCategory());
    }

    @Test
    void testCreateImmutableMultipleHaveUniqueIds() {
        // Create multiple ImmutableInfo objects and verify unique IDs
        ImmutableInfo info1 = factory.createImmutable("Task 1", "Desc 1", "cat-1");
        ImmutableInfo info2 = factory.createImmutable("Task 2", "Desc 2", "cat-2");
        ImmutableInfo info3 = factory.createImmutable("Task 3", "Desc 3", "cat-3");

        assertNotNull(info1.getId());
        assertNotNull(info2.getId());
        assertNotNull(info3.getId());

        // All IDs should be unique
        assertNotEquals(info1.getId(), info2.getId());
        assertNotEquals(info2.getId(), info3.getId());
        assertNotEquals(info1.getId(), info3.getId());
    }

    @Test
    void testCreateImmutableFromInfo() {
        // Create ImmutableInfo from existing Info
        InfoInterf mutableInfo = factory.create("Original Task", "Original Description", "cat-1");
        ImmutableInfo immutableInfo = factory.createImmutable((Info) mutableInfo);

        assertNotNull(immutableInfo);
        assertEquals(mutableInfo.getName(), immutableInfo.getName());
        assertEquals(mutableInfo.getDescription(), immutableInfo.getDescription());
        assertEquals(mutableInfo.getCategory(), immutableInfo.getCategory());
        assertEquals(mutableInfo.getId(), immutableInfo.getId());
        assertEquals(mutableInfo.getCreatedDate(), immutableInfo.getCreatedDate());
    }

    @Test
    void testCreateImmutableFromNullInfo() {
        // Should throw exception when Info is null
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> factory.createImmutable((Info) null)
        );
        
        assertEquals("Info cannot be null", exception.getMessage());
    }

    @Test
    void testCreateImmutableIsActuallyImmutable() {
        // Verify that ImmutableInfo setters are no-ops
        ImmutableInfo immutableInfo = factory.createImmutable("Task", "Description", "Category");
        
        String originalName = immutableInfo.getName();
        String originalDesc = immutableInfo.getDescription();
        String originalCat = immutableInfo.getCategory();
        
        // Attempt to modify (should be no-ops)
        immutableInfo.setName("Modified Name");
        immutableInfo.setDescription("Modified Description");
        immutableInfo.setCategory("Modified Category");
        
        // Values should remain unchanged
        assertEquals(originalName, immutableInfo.getName());
        assertEquals(originalDesc, immutableInfo.getDescription());
        assertEquals(originalCat, immutableInfo.getCategory());
    }

    @Test
    void testCreateImmutableWithLongStrings() {
        // Test with long strings
        String longName = "This is a very long task name that exceeds normal length";
        String longDesc = "This is an extremely detailed description that contains " +
                         "a lot of information about the task and what needs to be done";
        String longCategory = "very-long-category-name-for-testing";

        ImmutableInfo immutableInfo = factory.createImmutable(longName, longDesc, longCategory);

        assertNotNull(immutableInfo);
        assertEquals(longName, immutableInfo.getName());
        assertEquals(longDesc, immutableInfo.getDescription());
        assertEquals(longCategory, immutableInfo.getCategory());
    }

    @Test
    void testCreateImmutableWithSpecialCharacters() {
        // Test with special characters
        ImmutableInfo immutableInfo = factory.createImmutable("Task @#$%", "Desc with ñ é ü", "cat_123-456");

        assertNotNull(immutableInfo);
        assertEquals("Task @#$%", immutableInfo.getName());
        assertEquals("Desc with ñ é ü", immutableInfo.getDescription());
        assertEquals("cat_123-456", immutableInfo.getCategory());
    }

    @Test
    void testCreateImmutableFromInfoWithNullFields() {
        // Create Info with null fields and convert to ImmutableInfo
        Info infoWithNulls = new Info.Builder("Task Name").build(); // description and category will be null
        ImmutableInfo immutableInfo = factory.createImmutable(infoWithNulls);

        assertEquals("Task Name", immutableInfo.getName());
        assertNull(immutableInfo.getDescription());
        assertNull(immutableInfo.getCategory());
    }

    @Test
    void testCreateImmutableAndMutableAreIndependent() {
        // Verify that immutable and mutable versions are independent
        String originalName = "Original Task";
        InfoInterf mutableInfo = factory.create(originalName, "Description", "Category");
        ImmutableInfo immutableInfo = factory.createImmutable((Info) mutableInfo);

        // Modify mutable
        mutableInfo.setName("Modified Name");
        
        // Immutable should remain unchanged
        assertEquals(originalName, immutableInfo.getName());
        assertEquals("Modified Name", mutableInfo.getName());
    }
}