package entityTest;

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
}

