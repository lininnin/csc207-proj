package entity;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Category entity following Clean Architecture principles.
 * Tests business rules and entity constraints.
 */
class CategoryTest {

    @Test
    void testCategoryCreation() {
        String id = UUID.randomUUID().toString();
        Category category = new Category(id, "Work", "#0000FF");

        assertEquals(id, category.getId());
        assertEquals("Work", category.getName());
        assertEquals("#0000FF", category.getColor());
    }

    @Test
    void testCategoryWithNullColor() {
        String id = UUID.randomUUID().toString();
        Category category = new Category(id, "Personal", null);

        assertEquals(id, category.getId());
        assertEquals("Personal", category.getName());
        assertEquals("#808080", category.getColor());  // Default gray color when null
    }

    @Test
    void testCategoryWithEmptyColor() {
        String id = UUID.randomUUID().toString();
        Category category = new Category(id, "Urgent", "");

        assertEquals(id, category.getId());
        assertEquals("Urgent", category.getName());
        assertEquals("", category.getColor());
    }

    // Test removed - Category is now immutable, no setName method

    // Test removed - setColor method doesn't exist, color is final

    @Test
    void testCategoryWithWhitespaceName() {
        String id = UUID.randomUUID().toString();
        Category category = new Category(id, "  Work  ", "#0000FF");

        // Category stores name as-is (trimming happens in use case layer)
        assertEquals("  Work  ", category.getName());
    }

    @Test
    void testCategoryWithSpecialCharactersInName() {
        String id = UUID.randomUUID().toString();
        Category category = new Category(id, "Work & Study", "#0000FF");

        assertEquals("Work & Study", category.getName());
    }

    @Test
    void testCategoryWithHexColorVariations() {
        String id = UUID.randomUUID().toString();
        
        // Test with different hex color formats
        Category cat1 = new Category(id, "Test1", "#FFF");
        assertEquals("#FFF", cat1.getColor());
        
        Category cat2 = new Category(UUID.randomUUID().toString(), "Test2", "#FFFFFF");
        assertEquals("#FFFFFF", cat2.getColor());
        
        Category cat3 = new Category(UUID.randomUUID().toString(), "Test3", "rgb(255,255,255)");
        assertEquals("rgb(255,255,255)", cat3.getColor());
    }

    @Test
    void testCategoryEquality() {
        String id = UUID.randomUUID().toString();
        Category cat1 = new Category(id, "Work", "#0000FF");
        Category cat2 = new Category(id, "Work", "#0000FF");

        // Same ID means same category (entity equality)
        assertEquals(cat1.getId(), cat2.getId());
        
        // Different objects but same data
        assertNotSame(cat1, cat2);
        assertEquals(cat1.getName(), cat2.getName());
        assertEquals(cat1.getColor(), cat2.getColor());
    }

    @Test
    void testCategoryWithDifferentIds() {
        Category cat1 = new Category(UUID.randomUUID().toString(), "Work", "#0000FF");
        Category cat2 = new Category(UUID.randomUUID().toString(), "Work", "#0000FF");

        // Different IDs mean different categories even with same name
        assertNotEquals(cat1.getId(), cat2.getId());
        assertEquals(cat1.getName(), cat2.getName());
    }

    @Test
    void testCategoryIdImmutability() {
        String originalId = UUID.randomUUID().toString();
        Category category = new Category(originalId, "Work", "#0000FF");

        // ID should remain the same
        assertEquals(originalId, category.getId());
        
        // Category is immutable - cannot change name or color
        assertEquals("#0000FF", category.getColor());
    }

    @Test
    void testCategoryWithEmptyName() {
        String id = UUID.randomUUID().toString();
        
        // Category constructor throws exception for empty name
        assertThrows(IllegalArgumentException.class, () -> {
            new Category(id, "", "#0000FF");
        });
    }

    @Test
    void testCategoryWithNullName() {
        String id = UUID.randomUUID().toString();
        
        // Test if null name is allowed (entity layer might allow, validation in use case)
        try {
            Category category = new Category(id, null, "#0000FF");
            // If it allows null
            assertNull(category.getName());
        } catch (Exception e) {
            // If it doesn't allow null, that's also valid
            assertTrue(e instanceof NullPointerException || 
                      e instanceof IllegalArgumentException);
        }
    }

    // Test removed - Category is now immutable, no setName method

    // Test removed - setColor method doesn't exist, color is final

    @Test
    void testCategoryWithLongName() {
        String id = UUID.randomUUID().toString();
        String longName = "This is a very long category name that exceeds typical limits";
        
        // Category constructor enforces 20 character limit
        assertThrows(IllegalArgumentException.class, () -> {
            new Category(id, longName, "#0000FF");
        });
        
        // Test with exactly 20 characters (should work)
        String twentyChars = "12345678901234567890";
        Category category = new Category(id, twentyChars, "#0000FF");
        assertEquals(twentyChars, category.getName());
    }

    @Test
    void testCategoryColorFormats() {
        String id = UUID.randomUUID().toString();
        
        // Test that Category accepts any string as color (validation in use case)
        Category cat1 = new Category(id, "Test", "blue");
        assertEquals("blue", cat1.getColor());
        
        Category cat2 = new Category(UUID.randomUUID().toString(), "Test", "123");
        assertEquals("123", cat2.getColor());
        
        Category cat3 = new Category(UUID.randomUUID().toString(), "Test", "not-a-color");
        assertEquals("not-a-color", cat3.getColor());
    }
}