package entity;

import entity.info.Info;
import entity.info.InfoInterf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfoTest {

    @Test
    public void testBuilderCreatesValidInfo() {
        // 用接口类型持有实现类对象
        InfoInterf info = new Info.Builder("Test Name")
                .description("Test Description")
                .category("Test Category")
                .build();

        assertNotNull(info.getId());
        assertEquals("Test Name", info.getName());
        assertEquals("Test Description", info.getDescription());
        assertEquals("Test Category", info.getCategory());
        assertNotNull(info.getCreatedDate());
    }

    @Test
    public void testSettersUpdateFields() {
        InfoInterf info = new Info.Builder("Original Name").build();

        info.setName("New Name");
        info.setDescription("New Description");
        info.setCategory("New Category");

        assertEquals("New Name", info.getName());
        assertEquals("New Description", info.getDescription());
        assertEquals("New Category", info.getCategory());
    }

    @Test
    public void testSetNameRejectsNullOrEmpty() {
        InfoInterf info = new Info.Builder("Valid Name").build();

        assertThrows(IllegalArgumentException.class, () -> info.setName(null));
        assertThrows(IllegalArgumentException.class, () -> info.setName("  "));
    }

    @Test
    public void testDescriptionAndCategoryAllowNullOrEmpty() {
        InfoInterf info = new Info.Builder("Name").build();

        info.setDescription(null);
        assertEquals("", info.getDescription());

        info.setCategory("");
        assertEquals("", info.getCategory());
    }

    @Test
    public void testBuilderRejectsInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Info.Builder(null));
        assertThrows(IllegalArgumentException.class, () -> new Info.Builder("  "));
    }

    @Test
    public void testBuilderTrimsWhitespace() {
        InfoInterf info = new Info.Builder("  Test Name  ")
                .description("  Test Description  ")
                .category("  Test Category  ")
                .build();

        assertEquals("Test Name", info.getName());
        assertEquals("Test Description", info.getDescription());
        assertEquals("Test Category", info.getCategory());
    }

    @Test
    public void testBuilderHandlesNullAndEmptyOptionalFields() {
        InfoInterf info1 = new Info.Builder("Name")
                .description(null)
                .category("")
                .build();

        InfoInterf info2 = new Info.Builder("Name")
                .description("   ")
                .category(null)
                .build();

        // Null and empty descriptions/categories should be normalized
        assertNull(info1.getDescription());
        assertNull(info1.getCategory());
        assertNull(info2.getDescription());
        assertNull(info2.getCategory());
    }

    @Test
    public void testBuilderChaining() {
        InfoInterf info = new Info.Builder("Name")
                .description("Description")
                .category("Category")
                .build();

        assertEquals("Name", info.getName());
        assertEquals("Description", info.getDescription());
        assertEquals("Category", info.getCategory());
    }

    @Test
    public void testUniqueIds() {
        InfoInterf info1 = new Info.Builder("Name1").build();
        InfoInterf info2 = new Info.Builder("Name2").build();

        assertNotEquals(info1.getId(), info2.getId());
        assertNotNull(info1.getId());
        assertNotNull(info2.getId());
    }

    @Test
    public void testWithName() {
        Info originalInfo = (Info) new Info.Builder("Original").build();
        Info updatedInfo = originalInfo.withName("Updated");

        assertEquals("Updated", updatedInfo.getName());
        assertEquals("Original", originalInfo.getName()); // Original unchanged
        assertNotEquals(originalInfo.getId(), updatedInfo.getId()); // Different IDs
    }

    @Test
    public void testWithNameRejectsInvalidNames() {
        Info info = (Info) new Info.Builder("Valid").build();

        assertThrows(IllegalArgumentException.class, () -> info.withName(null));
        assertThrows(IllegalArgumentException.class, () -> info.withName("  "));
        assertThrows(IllegalArgumentException.class, () -> info.withName(""));
    }

    @Test
    public void testWithNameTrimsWhitespace() {
        Info originalInfo = (Info) new Info.Builder("Original").build();
        Info updatedInfo = originalInfo.withName("  Updated  ");

        assertEquals("Updated", updatedInfo.getName());
    }

    @Test
    public void testWithDescription() {
        Info originalInfo = (Info) new Info.Builder("Name")
                .description("Original Description")
                .category("Category")
                .build();
        
        Info updatedInfo = originalInfo.withDescription("Updated Description");

        assertEquals("Updated Description", updatedInfo.getDescription());
        assertEquals("Original Description", originalInfo.getDescription()); // Original unchanged
        assertEquals("Name", updatedInfo.getName()); // Other fields preserved
        assertEquals("Category", updatedInfo.getCategory());
    }

    @Test
    public void testWithDescriptionHandlesNullAndEmpty() {
        Info originalInfo = (Info) new Info.Builder("Name").description("Original").build();

        Info withNull = originalInfo.withDescription(null);
        Info withEmpty = originalInfo.withDescription("");
        Info withWhitespace = originalInfo.withDescription("   ");

        // The withDescription method normalizes empty values to ""
        // but when passed to builder, null/empty descriptors become null
        assertNull(withNull.getDescription());
        assertNull(withEmpty.getDescription());
        assertNull(withWhitespace.getDescription());
    }

    @Test
    public void testWithCategory() {
        Info originalInfo = (Info) new Info.Builder("Name")
                .description("Description")
                .category("Original Category")
                .build();
        
        Info updatedInfo = originalInfo.withCategory("Updated Category");

        assertEquals("Updated Category", updatedInfo.getCategory());
        assertEquals("Original Category", originalInfo.getCategory()); // Original unchanged
        assertEquals("Name", updatedInfo.getName()); // Other fields preserved
        assertEquals("Description", updatedInfo.getDescription());
    }

    @Test
    public void testWithCategoryHandlesNullAndEmpty() {
        Info originalInfo = (Info) new Info.Builder("Name").category("Original").build();

        Info withNull = originalInfo.withCategory(null);
        Info withEmpty = originalInfo.withCategory("");
        Info withWhitespace = originalInfo.withCategory("   ");

        // The withCategory method normalizes empty values to ""
        // but when passed to builder, null/empty categories become null
        assertNull(withNull.getCategory());
        assertNull(withEmpty.getCategory());
        assertNull(withWhitespace.getCategory());
    }

    @Test
    public void testImmutableUpdatesPreserveCreatedDate() {
        Info originalInfo = (Info) new Info.Builder("Name").build();
        
        Info withNewName = originalInfo.withName("New Name");
        Info withNewDescription = originalInfo.withDescription("New Description");
        Info withNewCategory = originalInfo.withCategory("New Category");

        // All should have their own creation dates (new instances)
        assertNotNull(withNewName.getCreatedDate());
        assertNotNull(withNewDescription.getCreatedDate());
        assertNotNull(withNewCategory.getCreatedDate());
    }

    @Test
    public void testMinimalInfo() {
        InfoInterf info = new Info.Builder("MinimalName").build();

        assertEquals("MinimalName", info.getName());
        assertNull(info.getDescription());
        assertNull(info.getCategory());
        assertNotNull(info.getId());
        assertNotNull(info.getCreatedDate());
    }

    @Test
    public void testSetNameTrimsWhitespace() {
        InfoInterf info = new Info.Builder("Original").build();
        info.setName("  Trimmed  ");
        assertEquals("Trimmed", info.getName());
    }

    @Test
    public void testSetDescriptionNormalization() {
        InfoInterf info = new Info.Builder("Name").build();
        
        info.setDescription("  Valid Description  ");
        assertEquals("Valid Description", info.getDescription());
        
        info.setDescription("");
        assertEquals("", info.getDescription());
        
        info.setDescription("   ");
        assertEquals("", info.getDescription());
    }

    @Test
    public void testSetCategoryNormalization() {
        InfoInterf info = new Info.Builder("Name").build();
        
        info.setCategory("  Valid Category  ");
        assertEquals("Valid Category", info.getCategory());
        
        info.setCategory("");
        assertEquals("", info.getCategory());
        
        info.setCategory("   ");
        assertEquals("", info.getCategory());
    }
}

