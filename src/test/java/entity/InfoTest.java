package entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Info entity.
 */
public class InfoTest {

    @Test
    public void testValidInfoCreation() {
        LocalDate now = LocalDate.now();
        Info info = new Info("1", "Test Task", "Description", "Work", now);

        assertEquals("1", info.getId());
        assertEquals("Test Task", info.getName());
        assertEquals("Description", info.getDescription());
        assertEquals("Work", info.getCategory());
        assertEquals(now, info.getCreatedDate());
    }

    @Test
    public void testInfoWithNullDescription() {
        Info info = new Info("1", "Test Task", null, "Work", LocalDate.now());
        assertNull(info.getDescription());
    }

    @Test
    public void testInfoWithNullCategory() {
        Info info = new Info("1", "Test Task", "Description", null, LocalDate.now());
        assertNull(info.getCategory());
    }

    @Test
    public void testInfoDefaultCreatedDate() {
        LocalDate beforeCreation = LocalDate.now();
        Info info = new Info("1", "Test Task", null, null, null);
        LocalDate afterCreation = LocalDate.now();

        assertNotNull(info.getCreatedDate());
        assertTrue(!info.getCreatedDate().isBefore(beforeCreation));
        assertTrue(!info.getCreatedDate().isAfter(afterCreation));
    }

    @Test
    public void testNullIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Info(null, "Test", null, null, null)
        );
    }

    @Test
    public void testEmptyIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Info("", "Test", null, null, null)
        );
    }

    @Test
    public void testNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Info("1", null, null, null, null)
        );
    }

    @Test
    public void testEmptyNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Info("1", "", null, null, null)
        );
    }

    @Test
    public void testNameExceeds20CharsThrowsException() {
        String longName = "This name is definitely longer than twenty characters";
        assertThrows(IllegalArgumentException.class, () ->
                new Info("1", longName, null, null, null)
        );
    }

    @Test
    public void testDescriptionExceeds100CharsThrowsException() {
        String longDesc = "This description is way too long and exceeds one hundred characters " +
                "which should trigger an exception to be thrown by the constructor";
        assertThrows(IllegalArgumentException.class, () ->
                new Info("1", "Test", longDesc, null, null)
        );
    }

    @Test
    public void testEqualsAndHashCode() {
        Info info1 = new Info("1", "Test", null, null, LocalDate.now());
        Info info2 = new Info("1", "Different", "Desc", "Cat", LocalDate.now().minusDays(1));
        Info info3 = new Info("2", "Test", null, null, LocalDate.now());

        // Same ID means equal
        assertEquals(info1, info2);
        assertEquals(info1.hashCode(), info2.hashCode());

        // Different ID means not equal
        assertNotEquals(info1, info3);
    }
}