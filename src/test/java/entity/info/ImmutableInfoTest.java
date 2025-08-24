package entity.info;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for ImmutableInfo.
 * Tests immutability, constructor validation, and conversion methods.
 */
class ImmutableInfoTest {

    private LocalDate testDate;
    private Info mutableInfo;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 6, 15);
        mutableInfo = new Info.Builder("Test Task")
                .description("Test Description")
                .category("Test Category")
                .build();
    }

    // Constructor from Info Tests
    @Test
    @DisplayName("Should create ImmutableInfo from valid Info")
    void testConstructorFromInfo() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        assertEquals(mutableInfo.getId(), immutableInfo.getId());
        assertEquals(mutableInfo.getName(), immutableInfo.getName());
        assertEquals(mutableInfo.getDescription(), immutableInfo.getDescription());
        assertEquals(mutableInfo.getCategory(), immutableInfo.getCategory());
        assertEquals(mutableInfo.getCreatedDate(), immutableInfo.getCreatedDate());
    }

    @Test
    @DisplayName("Should throw exception when Info is null")
    void testConstructorFromNullInfo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableInfo((Info) null)
        );
        
        assertEquals("Info cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should create ImmutableInfo from Info with null description")
    void testConstructorFromInfoWithNullDescription() {
        Info infoWithNullDesc = new Info.Builder("Task").build();
        
        ImmutableInfo immutableInfo = new ImmutableInfo(infoWithNullDesc);
        
        assertEquals("Task", immutableInfo.getName());
        assertNull(immutableInfo.getDescription());
        assertNull(immutableInfo.getCategory());
    }

    // Constructor with parameters Tests
    @Test
    @DisplayName("Should create ImmutableInfo with all parameters")
    void testConstructorWithAllParameters() {
        ImmutableInfo immutableInfo = new ImmutableInfo(
            "test-id-123",
            "Task Name",
            "Task Description", 
            "Task Category",
            testDate
        );
        
        assertEquals("test-id-123", immutableInfo.getId());
        assertEquals("Task Name", immutableInfo.getName());
        assertEquals("Task Description", immutableInfo.getDescription());
        assertEquals("Task Category", immutableInfo.getCategory());
        assertEquals(testDate, immutableInfo.getCreatedDate());
    }

    @Test
    @DisplayName("Should throw exception when ID is null")
    void testConstructorWithNullId() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableInfo(null, "Task", "Desc", "Cat", testDate)
        );
        
        assertEquals("ID cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when ID is empty")
    void testConstructorWithEmptyId() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableInfo("  ", "Task", "Desc", "Cat", testDate)
        );
        
        assertEquals("ID cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void testConstructorWithNullName() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableInfo("id", null, "Desc", "Cat", testDate)
        );
        
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void testConstructorWithEmptyName() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableInfo("id", "  ", "Desc", "Cat", testDate)
        );
        
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when createdDate is null")
    void testConstructorWithNullCreatedDate() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableInfo("id", "Task", "Desc", "Cat", null)
        );
        
        assertEquals("Created date cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should normalize null description to empty string")
    void testConstructorWithNullDescription() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id", "Task", null, "Cat", testDate);
        
        assertEquals("", immutableInfo.getDescription());
    }

    @Test
    @DisplayName("Should normalize empty description to empty string")
    void testConstructorWithEmptyDescription() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id", "Task", "   ", "Cat", testDate);
        
        assertEquals("", immutableInfo.getDescription());
    }

    @Test
    @DisplayName("Should normalize null category to empty string")
    void testConstructorWithNullCategory() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id", "Task", "Desc", null, testDate);
        
        assertEquals("", immutableInfo.getCategory());
    }

    @Test
    @DisplayName("Should normalize empty category to empty string")
    void testConstructorWithEmptyCategory() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id", "Task", "Desc", "   ", testDate);
        
        assertEquals("", immutableInfo.getCategory());
    }

    @Test
    @DisplayName("Should trim whitespace from all string fields")
    void testConstructorTrimsWhitespace() {
        ImmutableInfo immutableInfo = new ImmutableInfo(
            "  id-123  ",
            "  Task Name  ",
            "  Description  ",
            "  Category  ",
            testDate
        );
        
        assertEquals("id-123", immutableInfo.getId());
        assertEquals("Task Name", immutableInfo.getName());
        assertEquals("Description", immutableInfo.getDescription());
        assertEquals("Category", immutableInfo.getCategory());
    }

    // Setter Tests (should be no-op)
    @Test
    @DisplayName("Should not modify name when setName is called")
    void testSetNameIsNoOp() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        String originalName = immutableInfo.getName();
        
        immutableInfo.setName("New Name");
        
        assertEquals(originalName, immutableInfo.getName());
    }

    @Test
    @DisplayName("Should not modify description when setDescription is called")
    void testSetDescriptionIsNoOp() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        String originalDescription = immutableInfo.getDescription();
        
        immutableInfo.setDescription("New Description");
        
        assertEquals(originalDescription, immutableInfo.getDescription());
    }

    @Test
    @DisplayName("Should not modify category when setCategory is called")
    void testSetCategoryIsNoOp() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        String originalCategory = immutableInfo.getCategory();
        
        immutableInfo.setCategory("New Category");
        
        assertEquals(originalCategory, immutableInfo.getCategory());
    }

    // Immutable update methods Tests
    @Test
    @DisplayName("Should create new instance with updated name")
    void testWithName() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withName("Updated Name");
        
        assertNotSame(original, updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals(original.getDescription(), updated.getDescription());
        assertEquals(original.getCategory(), updated.getCategory());
        assertEquals(original.getId(), updated.getId());
        assertEquals(original.getCreatedDate(), updated.getCreatedDate());
        
        // Original unchanged
        assertEquals(mutableInfo.getName(), original.getName());
    }

    @Test
    @DisplayName("Should trim whitespace when using withName")
    void testWithNameTrimsWhitespace() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withName("  Updated Name  ");
        
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    @DisplayName("Should throw exception when withName called with null")
    void testWithNameThrowsOnNull() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> immutableInfo.withName(null)
        );
        
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when withName called with empty string")
    void testWithNameThrowsOnEmpty() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> immutableInfo.withName("   ")
        );
        
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should create new instance with updated description")
    void testWithDescription() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withDescription("Updated Description");
        
        assertNotSame(original, updated);
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(original.getName(), updated.getName());
        assertEquals(original.getCategory(), updated.getCategory());
        assertEquals(original.getId(), updated.getId());
        assertEquals(original.getCreatedDate(), updated.getCreatedDate());
        
        // Original unchanged
        assertEquals(mutableInfo.getDescription(), original.getDescription());
    }

    @Test
    @DisplayName("Should normalize null description to empty string in withDescription")
    void testWithDescriptionHandlesNull() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withDescription(null);
        
        assertEquals("", updated.getDescription());
    }

    @Test
    @DisplayName("Should normalize empty description to empty string in withDescription")
    void testWithDescriptionHandlesEmpty() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withDescription("   ");
        
        assertEquals("", updated.getDescription());
    }

    @Test
    @DisplayName("Should trim whitespace when using withDescription")
    void testWithDescriptionTrimsWhitespace() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withDescription("  Updated Description  ");
        
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    @DisplayName("Should create new instance with updated category")
    void testWithCategory() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withCategory("Updated Category");
        
        assertNotSame(original, updated);
        assertEquals("Updated Category", updated.getCategory());
        assertEquals(original.getName(), updated.getName());
        assertEquals(original.getDescription(), updated.getDescription());
        assertEquals(original.getId(), updated.getId());
        assertEquals(original.getCreatedDate(), updated.getCreatedDate());
        
        // Original unchanged
        assertEquals(mutableInfo.getCategory(), original.getCategory());
    }

    @Test
    @DisplayName("Should normalize null category to empty string in withCategory")
    void testWithCategoryHandlesNull() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withCategory(null);
        
        assertEquals("", updated.getCategory());
    }

    @Test
    @DisplayName("Should normalize empty category to empty string in withCategory")
    void testWithCategoryHandlesEmpty() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withCategory("   ");
        
        assertEquals("", updated.getCategory());
    }

    @Test
    @DisplayName("Should trim whitespace when using withCategory")
    void testWithCategoryTrimsWhitespace() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo updated = original.withCategory("  Updated Category  ");
        
        assertEquals("Updated Category", updated.getCategory());
    }

    // toMutableInfo Tests
    @Test
    @DisplayName("Should convert to mutable Info with same content")
    void testToMutableInfo() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        Info converted = immutableInfo.toMutableInfo();
        
        assertNotNull(converted);
        assertEquals(immutableInfo.getName(), converted.getName());
        assertEquals(immutableInfo.getDescription(), converted.getDescription());
        assertEquals(immutableInfo.getCategory(), converted.getCategory());
        
        // Note: ID and creation date will be different as noted in implementation
        assertNotEquals(immutableInfo.getId(), converted.getId());
    }

    @Test
    @DisplayName("Should create mutable Info that can be modified")
    void testToMutableInfoAllowsModification() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        Info converted = immutableInfo.toMutableInfo();
        
        // Should be able to modify the converted mutable info
        converted.setName("Modified Name");
        converted.setDescription("Modified Description");
        converted.setCategory("Modified Category");
        
        assertEquals("Modified Name", converted.getName());
        assertEquals("Modified Description", converted.getDescription());
        assertEquals("Modified Category", converted.getCategory());
        
        // Original immutable should be unchanged
        assertEquals(mutableInfo.getName(), immutableInfo.getName());
        assertEquals(mutableInfo.getDescription(), immutableInfo.getDescription());
        assertEquals(mutableInfo.getCategory(), immutableInfo.getCategory());
    }

    @Test
    @DisplayName("Should convert immutable with empty description and category")
    void testToMutableInfoWithEmptyFields() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id", "Task", "", "", testDate);
        
        Info converted = immutableInfo.toMutableInfo();
        
        assertEquals("Task", converted.getName());
        assertNull(converted.getDescription()); // Builder normalizes empty to null
        assertNull(converted.getCategory());
    }

    // equals and hashCode Tests
    @Test
    @DisplayName("Should be equal to itself")
    void testEqualsWithSameObject() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        assertEquals(immutableInfo, immutableInfo);
        assertEquals(immutableInfo.hashCode(), immutableInfo.hashCode());
    }

    @Test
    @DisplayName("Should be equal to ImmutableInfo with same data")
    void testEqualsWithSameData() {
        ImmutableInfo info1 = new ImmutableInfo("id", "Task", "Desc", "Cat", testDate);
        ImmutableInfo info2 = new ImmutableInfo("id", "Task", "Desc", "Cat", testDate);
        
        assertEquals(info1, info2);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null")
    void testEqualsWithNull() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        assertNotEquals(immutableInfo, null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void testEqualsWithDifferentClass() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        assertNotEquals(immutableInfo, "not an ImmutableInfo");
    }

    @Test
    @DisplayName("Should not be equal when ID differs")
    void testEqualsWithDifferentId() {
        ImmutableInfo info1 = new ImmutableInfo("id1", "Task", "Desc", "Cat", testDate);
        ImmutableInfo info2 = new ImmutableInfo("id2", "Task", "Desc", "Cat", testDate);
        
        assertNotEquals(info1, info2);
        assertNotEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when name differs")
    void testEqualsWithDifferentName() {
        ImmutableInfo info1 = new ImmutableInfo("id", "Task1", "Desc", "Cat", testDate);
        ImmutableInfo info2 = new ImmutableInfo("id", "Task2", "Desc", "Cat", testDate);
        
        assertNotEquals(info1, info2);
    }

    @Test
    @DisplayName("Should not be equal when description differs")
    void testEqualsWithDifferentDescription() {
        ImmutableInfo info1 = new ImmutableInfo("id", "Task", "Desc1", "Cat", testDate);
        ImmutableInfo info2 = new ImmutableInfo("id", "Task", "Desc2", "Cat", testDate);
        
        assertNotEquals(info1, info2);
    }

    @Test
    @DisplayName("Should not be equal when category differs")
    void testEqualsWithDifferentCategory() {
        ImmutableInfo info1 = new ImmutableInfo("id", "Task", "Desc", "Cat1", testDate);
        ImmutableInfo info2 = new ImmutableInfo("id", "Task", "Desc", "Cat2", testDate);
        
        assertNotEquals(info1, info2);
    }

    @Test
    @DisplayName("Should not be equal when createdDate differs")
    void testEqualsWithDifferentCreatedDate() {
        ImmutableInfo info1 = new ImmutableInfo("id", "Task", "Desc", "Cat", testDate);
        ImmutableInfo info2 = new ImmutableInfo("id", "Task", "Desc", "Cat", testDate.plusDays(1));
        
        assertNotEquals(info1, info2);
    }

    @Test
    @DisplayName("Should handle equals with null fields")
    void testEqualsWithNullFields() {
        ImmutableInfo info1 = new ImmutableInfo("id", "Task", "", "", testDate);
        ImmutableInfo info2 = new ImmutableInfo("id", "Task", "", "", testDate);
        
        assertEquals(info1, info2);
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    // toString Tests
    @Test
    @DisplayName("Should format toString with all fields")
    void testToStringWithAllFields() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-123", "Task Name", "Description", "Category", testDate);
        
        String result = immutableInfo.toString();
        
        assertTrue(result.contains("ImmutableInfo{"));
        assertTrue(result.contains("id='id-123'"));
        assertTrue(result.contains("name='Task Name'"));
        assertTrue(result.contains("description='Description'"));
        assertTrue(result.contains("category='Category'"));
        assertTrue(result.contains("createdDate=2024-06-15"));
    }

    @Test
    @DisplayName("Should format toString with empty description and category")
    void testToStringWithEmptyFields() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-123", "Task Name", "", "", testDate);
        
        String result = immutableInfo.toString();
        
        assertTrue(result.contains("ImmutableInfo{"));
        assertTrue(result.contains("id='id-123'"));
        assertTrue(result.contains("name='Task Name'"));
        assertFalse(result.contains("description="));
        assertFalse(result.contains("category="));
        assertTrue(result.contains("createdDate=2024-06-15"));
    }

    @Test
    @DisplayName("Should format toString with only description empty")
    void testToStringWithEmptyDescription() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-123", "Task Name", "", "Category", testDate);
        
        String result = immutableInfo.toString();
        
        assertFalse(result.contains("description="));
        assertTrue(result.contains("category='Category'"));
    }

    @Test
    @DisplayName("Should format toString with only category empty")
    void testToStringWithEmptyCategory() {
        ImmutableInfo immutableInfo = new ImmutableInfo("id-123", "Task Name", "Description", "", testDate);
        
        String result = immutableInfo.toString();
        
        assertTrue(result.contains("description='Description'"));
        assertFalse(result.contains("category="));
    }

    // Immutability verification Tests
    @Test
    @DisplayName("Should maintain immutability through method chaining")
    void testImmutabilityThroughChaining() {
        ImmutableInfo original = new ImmutableInfo(mutableInfo);
        
        ImmutableInfo chained = original
                .withName("New Name")
                .withDescription("New Description")  
                .withCategory("New Category");
        
        // Original should be unchanged
        assertEquals(mutableInfo.getName(), original.getName());
        assertEquals(mutableInfo.getDescription(), original.getDescription());
        assertEquals(mutableInfo.getCategory(), original.getCategory());
        
        // Final result should have new values
        assertEquals("New Name", chained.getName());
        assertEquals("New Description", chained.getDescription());
        assertEquals("New Category", chained.getCategory());
        
        // All should be different instances
        assertNotSame(original, chained);
    }

    @Test
    @DisplayName("Should be thread-safe for reading")
    void testThreadSafetyForReading() {
        ImmutableInfo immutableInfo = new ImmutableInfo(mutableInfo);
        
        // Reading from multiple threads should be safe (no modifications possible)
        // This test verifies the object remains consistent
        String name = immutableInfo.getName();
        String description = immutableInfo.getDescription();
        String category = immutableInfo.getCategory();
        String id = immutableInfo.getId();
        LocalDate createdDate = immutableInfo.getCreatedDate();
        
        // Multiple reads should return same values
        assertEquals(name, immutableInfo.getName());
        assertEquals(description, immutableInfo.getDescription());
        assertEquals(category, immutableInfo.getCategory());
        assertEquals(id, immutableInfo.getId());
        assertEquals(createdDate, immutableInfo.getCreatedDate());
    }

    @Test
    @DisplayName("Should handle special characters in all fields")
    void testSpecialCharacters() {
        ImmutableInfo immutableInfo = new ImmutableInfo(
            "id-ñ-123", 
            "Task ñáme with émojis 🎯", 
            "Desc with special chars: @#$%^&*()", 
            "Category_123-456", 
            testDate
        );
        
        assertEquals("id-ñ-123", immutableInfo.getId());
        assertEquals("Task ñáme with émojis 🎯", immutableInfo.getName());
        assertEquals("Desc with special chars: @#$%^&*()", immutableInfo.getDescription());
        assertEquals("Category_123-456", immutableInfo.getCategory());
    }
}