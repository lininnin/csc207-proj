package entityTest;

import entity.info.Info;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InfoTest {

    @Test
    public void testBuildValidInfo() {
        Info info = new Info.Builder("Read Book")
                .description("Read 10 pages of novel")
                .category("Leisure")
                .build();

        assertEquals("Read Book", info.getName());
        assertEquals("Read 10 pages of novel", info.getDescription());
        assertEquals("Leisure", info.getCategory());
        assertNotNull(info.getId());
        assertEquals(LocalDate.now(), info.getCreatedDate());
    }

    @Test
    public void testTrimmedFields() {
        Info info = new Info.Builder("  Study  ")
                .description("  Review math  ")
                .category("  Academic ")
                .build();

        assertEquals("Study", info.getName());
        assertEquals("Review math", info.getDescription());
        assertEquals("Academic", info.getCategory());
    }

    @Test
    public void testOptionalDescriptionAndCategoryOmitted() {
        Info info = new Info.Builder("Sleep").build();
        assertEquals("Sleep", info.getName());
        assertNull(info.getDescription());
        assertNull(info.getCategory());
    }

    @Test
    public void testNullOrEmptyNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Info.Builder(null));
        assertThrows(IllegalArgumentException.class, () -> new Info.Builder(""));
        assertThrows(IllegalArgumentException.class, () -> new Info.Builder("   "));
    }

    @Test
    public void testSetNameSuccessfully() {
        Info info = new Info.Builder("Eat").build();
        info.setName("Lunch");
        assertEquals("Lunch", info.getName());
    }

    @Test
    public void testSetNameWithWhitespace() {
        Info info = new Info.Builder("Eat").build();
        info.setName("  Dinner  ");
        assertEquals("Dinner", info.getName());
    }

    @Test
    public void testSetNameNullOrEmptyThrows() {
        Info info = new Info.Builder("Play").build();
        assertThrows(IllegalArgumentException.class, () -> info.setName(null));
        assertThrows(IllegalArgumentException.class, () -> info.setName("   "));
    }

    @Test
    public void testSetDescriptionSuccessfully() {
        Info info = new Info.Builder("Walk").build();
        info.setDescription("Evening walk");
        assertEquals("Evening walk", info.getDescription());
    }

    @Test
    public void testSetDescriptionNullOrEmptyThrows() {
        Info info = new Info.Builder("Meditate").build();
        assertThrows(IllegalArgumentException.class, () -> info.setDescription(null));
        assertThrows(IllegalArgumentException.class, () -> info.setDescription("  "));
    }

    @Test
    public void testSetCategorySuccessfully() {
        Info info = new Info.Builder("Game").build();
        info.setCategory("Fun");
        assertEquals("Fun", info.getCategory());
    }

    @Test
    public void testSetCategoryNullOrEmptyThrows() {
        Info info = new Info.Builder("Code").build();
        assertThrows(IllegalArgumentException.class, () -> info.setCategory(null));
        assertThrows(IllegalArgumentException.class, () -> info.setCategory(""));
    }
}
